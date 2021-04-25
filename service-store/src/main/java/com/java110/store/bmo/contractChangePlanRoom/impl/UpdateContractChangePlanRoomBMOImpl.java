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

@Service("updateContractChangePlanRoomBMOImpl")
public class UpdateContractChangePlanRoomBMOImpl implements IUpdateContractChangePlanRoomBMO {

    @Autowired
    private IContractChangePlanRoomInnerServiceSMO contractChangePlanRoomInnerServiceSMOImpl;

    /**
     *
     *
     * @param contractChangePlanRoomPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ContractChangePlanRoomPo contractChangePlanRoomPo) {

        int flag = contractChangePlanRoomInnerServiceSMOImpl.updateContractChangePlanRoom(contractChangePlanRoomPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
