package com.java110.goods.bmo.groupBuyBatch.impl;

import com.java110.dto.groupBuyBatch.GroupBuyBatchDto;
import com.java110.goods.bmo.groupBuyBatch.IGetGroupBuyBatchBMO;
import com.java110.intf.goods.IGroupBuyBatchInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getGroupBuyBatchBMOImpl")
public class GetGroupBuyBatchBMOImpl implements IGetGroupBuyBatchBMO {

    @Autowired
    private IGroupBuyBatchInnerServiceSMO groupBuyBatchInnerServiceSMOImpl;

    /**
     * @param groupBuyBatchDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(GroupBuyBatchDto groupBuyBatchDto) {


        int count = groupBuyBatchInnerServiceSMOImpl.queryGroupBuyBatchsCount(groupBuyBatchDto);

        List<GroupBuyBatchDto> groupBuyBatchDtos = null;
        if (count > 0) {
            groupBuyBatchDtos = groupBuyBatchInnerServiceSMOImpl.queryGroupBuyBatchs(groupBuyBatchDto);
        } else {
            groupBuyBatchDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) groupBuyBatchDto.getRow()), count, groupBuyBatchDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
