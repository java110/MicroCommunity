package com.java110.goods.bmo.productDetail.impl;

import com.java110.dto.productDetail.ProductDetailDto;
import com.java110.goods.bmo.productDetail.IGetProductDetailBMO;
import com.java110.intf.IProductDetailInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getProductDetailBMOImpl")
public class GetProductDetailBMOImpl implements IGetProductDetailBMO {

    @Autowired
    private IProductDetailInnerServiceSMO productDetailInnerServiceSMOImpl;

    /**
     * @param productDetailDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ProductDetailDto productDetailDto) {


        int count = productDetailInnerServiceSMOImpl.queryProductDetailsCount(productDetailDto);

        List<ProductDetailDto> productDetailDtos = null;
        if (count > 0) {
            productDetailDtos = productDetailInnerServiceSMOImpl.queryProductDetails(productDetailDto);
        } else {
            productDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) productDetailDto.getRow()), count, productDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
