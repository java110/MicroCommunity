package com.java110.goods.bmo.productAttr.impl;

import com.java110.dto.productAttr.ProductAttrDto;
import com.java110.goods.bmo.productAttr.IGetProductAttrBMO;
import com.java110.intf.goods.IProductAttrInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getProductAttrBMOImpl")
public class GetProductAttrBMOImpl implements IGetProductAttrBMO {

    @Autowired
    private IProductAttrInnerServiceSMO productAttrInnerServiceSMOImpl;

    /**
     * @param productAttrDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ProductAttrDto productAttrDto) {


        int count = productAttrInnerServiceSMOImpl.queryProductAttrsCount(productAttrDto);

        List<ProductAttrDto> productAttrDtos = null;
        if (count > 0) {
            productAttrDtos = productAttrInnerServiceSMOImpl.queryProductAttrs(productAttrDto);
        } else {
            productAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) productAttrDto.getRow()), count, productAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
