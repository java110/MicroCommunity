package com.java110.goods.bmo.productSpecValue.impl;

import com.java110.dto.productSpecValue.ProductSpecValueDto;
import com.java110.goods.bmo.productSpecValue.IGetProductSpecValueBMO;
import com.java110.intf.goods.IProductSpecValueInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getProductSpecValueBMOImpl")
public class GetProductSpecValueBMOImpl implements IGetProductSpecValueBMO {

    @Autowired
    private IProductSpecValueInnerServiceSMO productSpecValueInnerServiceSMOImpl;

    /**
     * @param productSpecValueDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ProductSpecValueDto productSpecValueDto) {


        int count = productSpecValueInnerServiceSMOImpl.queryProductSpecValuesCount(productSpecValueDto);

        List<ProductSpecValueDto> productSpecValueDtos = null;
        if (count > 0) {
            productSpecValueDtos = productSpecValueInnerServiceSMOImpl.queryProductSpecValues(productSpecValueDto);
        } else {
            productSpecValueDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) productSpecValueDto.getRow()), count, productSpecValueDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
