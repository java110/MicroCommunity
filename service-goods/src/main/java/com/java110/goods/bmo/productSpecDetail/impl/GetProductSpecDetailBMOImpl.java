package com.java110.goods.bmo.productSpecDetail.impl;

import com.java110.dto.product.ProductSpecDetailDto;
import com.java110.goods.bmo.productSpecDetail.IGetProductSpecDetailBMO;
import com.java110.intf.goods.IProductSpecDetailInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getProductSpecDetailBMOImpl")
public class GetProductSpecDetailBMOImpl implements IGetProductSpecDetailBMO {

    @Autowired
    private IProductSpecDetailInnerServiceSMO productSpecDetailInnerServiceSMOImpl;

    /**
     * @param productSpecDetailDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ProductSpecDetailDto productSpecDetailDto) {


        int count = productSpecDetailInnerServiceSMOImpl.queryProductSpecDetailsCount(productSpecDetailDto);

        List<ProductSpecDetailDto> productSpecDetailDtos = null;
        if (count > 0) {
            productSpecDetailDtos = productSpecDetailInnerServiceSMOImpl.queryProductSpecDetails(productSpecDetailDto);
        } else {
            productSpecDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) productSpecDetailDto.getRow()), count, productSpecDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
