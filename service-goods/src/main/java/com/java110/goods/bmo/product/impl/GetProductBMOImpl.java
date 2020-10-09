package com.java110.goods.bmo.product.impl;

import com.java110.dto.product.ProductDto;
import com.java110.goods.bmo.product.IGetProductBMO;
import com.java110.intf.goods.IProductInnerServiceSMO;
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

    /**
     * @param productDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ProductDto productDto) {


        int count = productInnerServiceSMOImpl.queryProductsCount(productDto);

        List<ProductDto> productDtos = null;
        if (count > 0) {
            productDtos = productInnerServiceSMOImpl.queryProducts(productDto);
        } else {
            productDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) productDto.getRow()), count, productDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
