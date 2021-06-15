package com.java110.goods.bmo.productLabel.impl;

import com.java110.dto.file.FileRelDto;
import com.java110.dto.product.ProductDto;
import com.java110.dto.product.ProductSpecDetailDto;
import com.java110.dto.productDetail.ProductDetailDto;
import com.java110.dto.productLabel.ProductLabelDto;
import com.java110.dto.productSpecValue.ProductSpecValueDto;
import com.java110.goods.bmo.productLabel.IGetProductLabelBMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.goods.IProductLabelInnerServiceSMO;
import com.java110.intf.goods.IProductSpecDetailInnerServiceSMO;
import com.java110.intf.goods.IProductSpecValueInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getProductLabelBMOImpl")
public class GetProductLabelBMOImpl implements IGetProductLabelBMO {

    @Autowired
    private IProductLabelInnerServiceSMO productLabelInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IProductSpecValueInnerServiceSMO productSpecValueInnerServiceSMOImpl;

    @Autowired
    private IProductSpecDetailInnerServiceSMO productSpecDetailInnerServiceSMOImpl;

    /**
     * @param productLabelDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ProductLabelDto productLabelDto) {


        int count = productLabelInnerServiceSMOImpl.queryProductLabelsCount(productLabelDto);

        List<ProductLabelDto> productLabelDtos = null;
        if (count > 0) {
            productLabelDtos = productLabelInnerServiceSMOImpl.queryProductLabels(productLabelDto);
            freshProductDtos(productLabelDtos,productLabelDto);
        } else {
            productLabelDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) productLabelDto.getRow()), count, productLabelDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }


    private void freshProductDtos(List<ProductLabelDto> productDtos, ProductDto productDto) {

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
        freshProductSpecValue(productDtos);

    }

    private void freshProductSpecValue(List<ProductLabelDto> productDtos) {
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
