package com.java110.goods.bmo.groupBuy.impl;

import com.java110.dto.groupBuy.GroupBuyDto;
import com.java110.goods.bmo.groupBuy.IGetGroupBuyBMO;
import com.java110.intf.goods.IGroupBuyInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getGroupBuyBMOImpl")
public class GetGroupBuyBMOImpl implements IGetGroupBuyBMO {

    @Autowired
    private IGroupBuyInnerServiceSMO groupBuyInnerServiceSMOImpl;

    /**
     * @param groupBuyDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(GroupBuyDto groupBuyDto) {


        int count = groupBuyInnerServiceSMOImpl.queryGroupBuysCount(groupBuyDto);

        List<GroupBuyDto> groupBuyDtos = null;
        if (count > 0) {
            groupBuyDtos = groupBuyInnerServiceSMOImpl.queryGroupBuys(groupBuyDto);
        } else {
            groupBuyDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) groupBuyDto.getRow()), count, groupBuyDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
