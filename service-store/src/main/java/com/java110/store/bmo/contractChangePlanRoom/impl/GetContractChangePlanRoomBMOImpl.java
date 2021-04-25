package com.java110.api.bmo.contractChangePlanRoom.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.contractChangePlanRoom.IContractChangePlanRoomBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.store.IWechatSmsTemplateInnerServiceSMO;
import com.java110.po.wechatSmsTemplate.WechatSmsTemplatePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.contractChangePlanRoom.IContractChangePlanRoomInnerServiceSMO;
import com.java110.dto.contractChangePlanRoom.ContractChangePlanRoomDto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("getContractChangePlanRoomBMOImpl")
public class GetContractChangePlanRoomBMOImpl implements IGetContractChangePlanRoomBMO {

    @Autowired
    private IContractChangePlanRoomInnerServiceSMO contractChangePlanRoomInnerServiceSMOImpl;

    /**
     *
     *
     * @param  contractChangePlanRoomDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractChangePlanRoomDto contractChangePlanRoomDto) {


        int count = contractChangePlanRoomInnerServiceSMOImpl.queryContractChangePlanRoomsCount(contractChangePlanRoomDto);

        List<ContractChangePlanRoomDto> contractChangePlanRoomDtos = null;
        if (count > 0) {
            contractChangePlanRoomDtos = contractChangePlanRoomInnerServiceSMOImpl.queryContractChangePlanRooms(contractChangePlanRoomDto);
        } else {
            contractChangePlanRoomDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractChangePlanRoomDto.getRow()), count, contractChangePlanRoomDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
