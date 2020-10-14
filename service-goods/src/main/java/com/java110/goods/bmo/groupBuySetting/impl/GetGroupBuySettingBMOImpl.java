package com.java110.goods.bmo.groupBuySetting.impl;

import com.java110.dto.groupBuySetting.GroupBuySettingDto;
import com.java110.goods.bmo.groupBuySetting.IGetGroupBuySettingBMO;
import com.java110.intf.IGroupBuySettingInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getGroupBuySettingBMOImpl")
public class GetGroupBuySettingBMOImpl implements IGetGroupBuySettingBMO {

    @Autowired
    private IGroupBuySettingInnerServiceSMO groupBuySettingInnerServiceSMOImpl;

    /**
     * @param groupBuySettingDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(GroupBuySettingDto groupBuySettingDto) {


        int count = groupBuySettingInnerServiceSMOImpl.queryGroupBuySettingsCount(groupBuySettingDto);

        List<GroupBuySettingDto> groupBuySettingDtos = null;
        if (count > 0) {
            groupBuySettingDtos = groupBuySettingInnerServiceSMOImpl.queryGroupBuySettings(groupBuySettingDto);
        } else {
            groupBuySettingDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) groupBuySettingDto.getRow()), count, groupBuySettingDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
