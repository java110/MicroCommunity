package com.java110.goods.bmo.groupBuyProduct.impl;

import com.java110.dto.file.FileRelDto;
import com.java110.dto.groupBuyProduct.GroupBuyProductDto;
import com.java110.dto.groupBuyProductSpec.GroupBuyProductSpecDto;
import com.java110.dto.product.ProductSpecDetailDto;
import com.java110.dto.productDetail.ProductDetailDto;
import com.java110.dto.productSpecValue.ProductSpecValueDto;
import com.java110.goods.bmo.groupBuyProduct.IGetGroupBuyProductBMO;
import com.java110.intf.goods.IGroupBuyProductInnerServiceSMO;
import com.java110.intf.goods.IGroupBuyProductSpecInnerServiceSMO;
import com.java110.intf.goods.IProductDetailInnerServiceSMO;
import com.java110.intf.goods.IProductSpecValueInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.goods.IProductInnerServiceSMO;
import com.java110.intf.goods.IProductSpecDetailInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getGroupBuyProductBMOImpl")
public class GetGroupBuyProductBMOImpl implements IGetGroupBuyProductBMO {

    @Autowired
    private IGroupBuyProductInnerServiceSMO groupBuyProductInnerServiceSMOImpl;

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
    private IGroupBuyProductSpecInnerServiceSMO groupBuyProductSpecInnerServiceSMOImpl;

    /**
     * @param groupBuyProductDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(GroupBuyProductDto groupBuyProductDto) {


        int count = groupBuyProductInnerServiceSMOImpl.queryGroupBuyProductsCount(groupBuyProductDto);

        List<GroupBuyProductDto> groupBuyProductDtos = null;
        if (count > 0) {
            groupBuyProductDtos = groupBuyProductInnerServiceSMOImpl.queryGroupBuyProducts(groupBuyProductDto);
            freshProductDtos(groupBuyProductDtos, groupBuyProductDto);
        } else {
            groupBuyProductDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) groupBuyProductDto.getRow()), count, groupBuyProductDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }


    private void freshProductDtos(List<GroupBuyProductDto> productDtos, GroupBuyProductDto productDto) {

        if (productDtos == null || productDtos.size() < 1) {
            return;
        }

        //输入
        List<String> productIds = new ArrayList<>();

        for (GroupBuyProductDto tmpProductDto : productDtos) {
            productIds.add(tmpProductDto.getProductId());
        }

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjIds(productIds.toArray(new String[productIds.size()]));
        fileRelDto.setRelTypeCd(FileRelDto.REL_TYPE_CD_GOODS_COVER);
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        //刷入图片信息
        List<String> photoVos = null;
        String url = null;
        for (GroupBuyProductDto tmpProductDto : productDtos) {
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (tmpProductDto.getProductId().equals(tmpFileRelDto.getObjId())) {
                    url = "/callComponent/download/getFile/file?fileId=" + tmpFileRelDto.getFileRealName() + "&communityId=-1";
                    tmpProductDto.setCoverPhoto(url);
                }
            }
        }

        //刷入库存和销量
        GroupBuyProductSpecDto productSpecValueDto = new GroupBuyProductSpecDto();
        productSpecValueDto.setStoreId(productDtos.get(0).getStoreId());
        productSpecValueDto.setProductIds(productIds.toArray(new String[productIds.size()]));
        List<GroupBuyProductSpecDto> productSpecValueDtos = groupBuyProductSpecInnerServiceSMOImpl.queryProductStockAndSales(productSpecValueDto);
        for (GroupBuyProductDto tmpProduct : productDtos) {
            for (GroupBuyProductSpecDto tmpProdSpecValue : productSpecValueDtos) {
                if (tmpProdSpecValue.getProductId().equals(tmpProduct.getProductId())) {
                    tmpProduct.setStock(tmpProdSpecValue.getGroupStock());
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
        for (GroupBuyProductDto tmpProductDto : productDtos) {
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (tmpProductDto.getProductId().equals(tmpFileRelDto.getObjId())) {
                    url = "/callComponent/download/getFile/file?fileId=" + tmpFileRelDto.getFileRealName() + "&communityId=-1";
                    photos.add(url);
                }
            }
            tmpProductDto.setCarouselFigurePhotos(photos);
        }

        freshProductSpecValue(productDtos);

        ProductDetailDto productDetailDto = new ProductDetailDto();
        productDetailDto.setProductId(productDtos.get(0).getProductId());
        productDetailDto.setStoreId(productDtos.get(0).getStoreId());
        List<ProductDetailDto> productDetailDtos = productDetailInnerServiceSMOImpl.queryProductDetails(productDetailDto);

        if (productDetailDtos == null || productDetailDtos.size() < 1) {
            return;
        }

        productDtos.get(0).setContent(productDetailDtos.get(0).getContent());
    }

    private void freshProductSpecValue(List<GroupBuyProductDto> productDtos) {
        GroupBuyProductSpecDto productSpecValueDto = new GroupBuyProductSpecDto();
        productSpecValueDto.setProductId(productDtos.get(0).getProductId());
        productSpecValueDto.setStoreId(productDtos.get(0).getStoreId());
        List<GroupBuyProductSpecDto> productSpecValueDtos = groupBuyProductSpecInnerServiceSMOImpl.queryGroupBuyProductSpecs(productSpecValueDto);

        if (productSpecValueDtos == null || productSpecValueDtos.size() < 1) {
            return;
        }
        productDtos.get(0).setGroupBuyProductSpecs(productSpecValueDtos);

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
