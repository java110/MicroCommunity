package com.java110.goods.bmo.productLabel.impl;

import com.java110.dto.productLabel.ProductLabelDto;
import com.java110.goods.bmo.productLabel.IGetProductLabelBMO;
import com.java110.intf.goods.IProductLabelInnerServiceSMO;
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

    /**
     * @param productLabelDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ProductLabelDto productLabelDto) {


        int count = productLabelInnerServiceSMOImpl.queryProductLabelsCount(productLabelDto);

        List<ProductLabelDto> productLabelDtos = null;
        if (count > 0) {
            productLabelDtos = productLabelInnerServiceSMOImpl.queryProductLabels(productLabelDto);
        } else {
            productLabelDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) productLabelDto.getRow()), count, productLabelDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
