package com.java110.goods.bmo.groupBuyProductSpec.impl;

import com.java110.dto.groupBuyProductSpec.GroupBuyProductSpecDto;
import com.java110.goods.bmo.groupBuyProductSpec.IGetGroupBuyProductSpecBMO;
import com.java110.intf.goods.IGroupBuyProductSpecInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getGroupBuyProductSpecBMOImpl")
public class GetGroupBuyProductSpecBMOImpl implements IGetGroupBuyProductSpecBMO {

    @Autowired
    private IGroupBuyProductSpecInnerServiceSMO groupBuyProductSpecInnerServiceSMOImpl;

    /**
     * @param groupBuyProductSpecDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(GroupBuyProductSpecDto groupBuyProductSpecDto) {


        int count = groupBuyProductSpecInnerServiceSMOImpl.queryGroupBuyProductSpecsCount(groupBuyProductSpecDto);

        List<GroupBuyProductSpecDto> groupBuyProductSpecDtos = null;
        if (count > 0) {
            groupBuyProductSpecDtos = groupBuyProductSpecInnerServiceSMOImpl.queryGroupBuyProductSpecs(groupBuyProductSpecDto);
        } else {
            groupBuyProductSpecDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) groupBuyProductSpecDto.getRow()), count, groupBuyProductSpecDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
