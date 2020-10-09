package com.java110.goods.bmo.productSpec.impl;

import com.java110.dto.product.ProductSpecDetailDto;
import com.java110.dto.product.ProductSpecDto;
import com.java110.goods.bmo.productSpec.IGetProductSpecBMO;
import com.java110.intf.goods.IProductSpecDetailInnerServiceSMO;
import com.java110.intf.goods.IProductSpecInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getProductSpecBMOImpl")
public class GetProductSpecBMOImpl implements IGetProductSpecBMO {

    @Autowired
    private IProductSpecInnerServiceSMO productSpecInnerServiceSMOImpl;

    @Autowired
    private IProductSpecDetailInnerServiceSMO productSpecDetailInnerServiceSMOImpl;

    /**
     * @param productSpecDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ProductSpecDto productSpecDto) {

        int count = productSpecInnerServiceSMOImpl.queryProductSpecsCount(productSpecDto);

        List<ProductSpecDto> productSpecDtos = null;
        if (count > 0) {
            productSpecDtos = productSpecInnerServiceSMOImpl.queryProductSpecs(productSpecDto);
            freshSpecDetail(productSpecDtos);
        } else {
            productSpecDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) productSpecDto.getRow()), count, productSpecDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void freshSpecDetail(List<ProductSpecDto> productSpecDtos) {

        if (productSpecDtos == null || productSpecDtos.size() < 1) {
            return;
        }
        List<String> specIds = new ArrayList<>();
        for (ProductSpecDto productSpecDto : productSpecDtos) {
            specIds.add(productSpecDto.getSpecId());
        }

        ProductSpecDetailDto productSpecDetailDto = new ProductSpecDetailDto();
        productSpecDetailDto.setSpecIds(specIds.toArray(new String[specIds.size()]));
        productSpecDetailDto.setStoreId(productSpecDtos.get(0).getStoreId());
        List<ProductSpecDetailDto> productSpecDetailDtos = productSpecDetailInnerServiceSMOImpl.queryProductSpecDetails(productSpecDetailDto);
        List<ProductSpecDetailDto> tmpProductSpecDetailDtos = null;
        for (ProductSpecDto productSpecDto : productSpecDtos) {
            tmpProductSpecDetailDtos = new ArrayList<>();
            for (ProductSpecDetailDto tmpProductSpecDetailDto : productSpecDetailDtos) {
                if (productSpecDto.getSpecId().equals(tmpProductSpecDetailDto.getSpecId())) {
                    tmpProductSpecDetailDtos.add(tmpProductSpecDetailDto);
                }
            }
            productSpecDto.setProductSpecDetails(tmpProductSpecDetailDtos);
        }
    }

}
