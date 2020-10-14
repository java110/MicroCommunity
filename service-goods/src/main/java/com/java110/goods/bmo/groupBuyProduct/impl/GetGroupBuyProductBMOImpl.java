package com.java110.goods.bmo.groupBuyProduct.impl;

import com.java110.dto.groupBuyProduct.GroupBuyProductDto;
import com.java110.goods.bmo.groupBuyProduct.IGetGroupBuyProductBMO;
import com.java110.intf.IGroupBuyProductInnerServiceSMO;
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

    /**
     * @param groupBuyProductDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(GroupBuyProductDto groupBuyProductDto) {


        int count = groupBuyProductInnerServiceSMOImpl.queryGroupBuyProductsCount(groupBuyProductDto);

        List<GroupBuyProductDto> groupBuyProductDtos = null;
        if (count > 0) {
            groupBuyProductDtos = groupBuyProductInnerServiceSMOImpl.queryGroupBuyProducts(groupBuyProductDto);
        } else {
            groupBuyProductDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) groupBuyProductDto.getRow()), count, groupBuyProductDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
