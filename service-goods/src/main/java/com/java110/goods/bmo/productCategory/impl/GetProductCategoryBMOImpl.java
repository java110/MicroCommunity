package com.java110.goods.bmo.productCategory.impl;

import com.java110.dto.productCategory.ProductCategoryDto;
import com.java110.goods.bmo.productCategory.IGetProductCategoryBMO;
import com.java110.intf.goods.IProductCategoryInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getProductCategoryBMOImpl")
public class GetProductCategoryBMOImpl implements IGetProductCategoryBMO {

    @Autowired
    private IProductCategoryInnerServiceSMO productCategoryInnerServiceSMOImpl;

    /**
     * @param productCategoryDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ProductCategoryDto productCategoryDto) {


        int count = productCategoryInnerServiceSMOImpl.queryProductCategorysCount(productCategoryDto);

        List<ProductCategoryDto> productCategoryDtos = null;
        if (count > 0) {
            productCategoryDtos = productCategoryInnerServiceSMOImpl.queryProductCategorys(productCategoryDto);
        } else {
            productCategoryDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) productCategoryDto.getRow()), count, productCategoryDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
