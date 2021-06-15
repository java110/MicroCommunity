package com.java110.goods.bmo.product.impl;

import com.java110.dto.file.FileRelDto;
import com.java110.dto.groupBuyProduct.GroupBuyProductDto;
import com.java110.dto.groupBuyProductSpec.GroupBuyProductSpecDto;
import com.java110.dto.product.ProductActivityDto;
import com.java110.dto.product.ProductDto;
import com.java110.dto.product.ProductSpecDetailDto;
import com.java110.dto.productDetail.ProductDetailDto;
import com.java110.dto.productSpecValue.ProductSpecValueDto;
import com.java110.goods.bmo.product.IGetProductBMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.goods.*;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getProductBMOImpl")
public class GetProductBMOImpl implements IGetProductBMO {

    @Autowired
    private IProductInnerServiceSMO productInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IProductDetailInnerServiceSMO productDetailInnerServiceSMOImpl;

    @Autowired
    private IProductSpecValueInnerServiceSMO productSpecValueInnerServiceSMOImpl;

    @Autowired
    private IProductSpecDetailInnerServiceSMO productSpecDetailInnerServiceSMOImpl;

    @Autowired
    private IGroupBuyProductInnerServiceSMO groupBuyProductInnerServiceSMOImpl;


    @Autowired
    private IGroupBuyProductSpecInnerServiceSMO groupBuyProductSpecInnerServiceSMOImpl;

    /**
     * @param productDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ProductDto productDto) {

        int count = productInnerServiceSMOImpl.queryProductsCount(productDto);

        List<ProductDto> productDtos = null;
        if (count > 0) {
            productDtos = productInnerServiceSMOImpl.queryProducts(productDto);

            freshProductDtos(productDtos, productDto);
        } else {
            productDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) productDto.getRow()), count, productDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void freshProductDtos(List<ProductDto> productDtos, ProductDto productDto) {

        if (productDtos == null || productDtos.size() < 1) {
            return;
        }

        //输入
        List<String> productIds = new ArrayList<>();

        for (ProductDto tmpProductDto : productDtos) {
            productIds.add(tmpProductDto.getProductId());
        }

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjIds(productIds.toArray(new String[productIds.size()]));
        fileRelDto.setRelTypeCd(FileRelDto.REL_TYPE_CD_GOODS_COVER);
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        //刷入图片信息
        List<String> photoVos = null;
        String url = null;
        for (ProductDto tmpProductDto : productDtos) {
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (tmpProductDto.getProductId().equals(tmpFileRelDto.getObjId())) {
                    url = "/callComponent/download/getFile/file?fileId=" + tmpFileRelDto.getFileRealName() + "&communityId=-1";
                    tmpProductDto.setCoverPhoto(url);
                }
            }
        }

        //刷入库存和销量
        ProductSpecValueDto productSpecValueDto = new ProductSpecValueDto();
        productSpecValueDto.setStoreId(productDtos.get(0).getStoreId());
        productSpecValueDto.setProductIds(productIds.toArray(new String[productIds.size()]));
        List<ProductSpecValueDto> productSpecValueDtos = productSpecValueInnerServiceSMOImpl.queryProductStockAndSales(productSpecValueDto);
        for (ProductDto tmpProduct : productDtos) {
            for (ProductSpecValueDto tmpProdSpecValue : productSpecValueDtos) {
                if (tmpProdSpecValue.getProductId().equals(tmpProduct.getProductId())) {
                    tmpProduct.setSales(tmpProdSpecValue.getSales());
                    tmpProduct.setStock(tmpProdSpecValue.getStock());
                }
            }
        }

        // 查询规格
        if (productDtos.size() > 1) {
            return;
        }

        //输入轮播图
        fileRelDto = new FileRelDto();
        fileRelDto.setObjId(productDtos.get(0).getProductId());
        fileRelDto.setRelTypeCd(FileRelDto.REL_TYPE_CD_GOODS_CAROUSEL_FIGURE);
        fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        List<String> photos = new ArrayList<>();
        for (ProductDto tmpProductDto : productDtos) {
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (tmpProductDto.getProductId().equals(tmpFileRelDto.getObjId())) {
                    url = "/callComponent/download/getFile/file?fileId=" + tmpFileRelDto.getFileRealName() + "&communityId=-1";
                    photos.add(url);
                }
            }
            tmpProductDto.setCarouselFigurePhotos(photos);
        }

        freshProductSpecValue(productDtos);
        judgeGroupBuyProduct(productDtos.get(0));


        ProductDetailDto productDetailDto = new ProductDetailDto();
        productDetailDto.setProductId(productDtos.get(0).getProductId());
        productDetailDto.setStoreId(productDtos.get(0).getStoreId());
        List<ProductDetailDto> productDetailDtos = productDetailInnerServiceSMOImpl.queryProductDetails(productDetailDto);

        if (productDetailDtos == null || productDetailDtos.size() < 1) {
            return;
        }

        productDtos.get(0).setContent(productDetailDtos.get(0).getContent());
    }

    private void judgeGroupBuyProduct(ProductDto productDto) {
        //判断商品是否 拼团
        GroupBuyProductDto groupBuyProductDto = new GroupBuyProductDto();
        groupBuyProductDto.setProductId(productDto.getProductId());
        groupBuyProductDto.setStoreId(productDto.getStoreId());
        List<GroupBuyProductDto> groupBuyProductDtos = groupBuyProductInnerServiceSMOImpl.queryGroupBuyProducts(groupBuyProductDto);

        if (groupBuyProductDtos == null || groupBuyProductDtos.size() < 1) {
            return;
        }

        productDto.setActType(ProductActivityDto.ACT_TYPE_GROUP);
        productDto.setActEndTime(groupBuyProductDtos.get(0).getBatchEndTime());
        productDto.setActProdName(groupBuyProductDtos.get(0).getGroupProdName());
        productDto.setActProdDesc(groupBuyProductDtos.get(0).getGroupProdDesc());

        GroupBuyProductSpecDto groupBuyProductSpecDto = new GroupBuyProductSpecDto();
        groupBuyProductSpecDto.setStoreId(groupBuyProductDtos.get(0).getStoreId());
        groupBuyProductSpecDto.setProductId(groupBuyProductDtos.get(0).getProductId());
        groupBuyProductSpecInnerServiceSMOImpl.queryGroupBuyProductSpecs(groupBuyProductSpecDto);


    }

    private void freshProductSpecValue(List<ProductDto> productDtos) {
        ProductSpecValueDto productSpecValueDto = new ProductSpecValueDto();
        productSpecValueDto.setProductId(productDtos.get(0).getProductId());
        productSpecValueDto.setStoreId(productDtos.get(0).getStoreId());
        List<ProductSpecValueDto> productSpecValueDtos = productSpecValueInnerServiceSMOImpl.queryProductSpecValues(productSpecValueDto);

        if (productSpecValueDtos == null || productSpecValueDtos.size() < 1) {
            return;
        }
        productDtos.get(0).setProductSpecValues(productSpecValueDtos);

        List<String> specIds = new ArrayList<>();
        for (ProductSpecValueDto productSpecValue : productSpecValueDtos) {
            specIds.add(productSpecValue.getSpecId());
        }

        ProductSpecDetailDto productSpecDetailDto = new ProductSpecDetailDto();
        productSpecDetailDto.setSpecIds(specIds.toArray(new String[specIds.size()]));
        productSpecDetailDto.setStoreId(productDtos.get(0).getStoreId());
        List<ProductSpecDetailDto> productSpecDetailDtos = productSpecDetailInnerServiceSMOImpl.queryProductSpecDetails(productSpecDetailDto);
        List<ProductSpecDetailDto> tmpProductSpecDetailDtos = null;
        for (ProductSpecValueDto productSpecValue : productSpecValueDtos) {
            tmpProductSpecDetailDtos = new ArrayList<>();
            for (ProductSpecDetailDto tmpProductSpecDetailDto : productSpecDetailDtos) {
                if (productSpecValue.getSpecId().equals(tmpProductSpecDetailDto.getSpecId())) {
                    tmpProductSpecDetailDtos.add(tmpProductSpecDetailDto);
                }
            }
            productSpecValue.setProductSpecDetails(tmpProductSpecDetailDtos);
        }
    }

}
