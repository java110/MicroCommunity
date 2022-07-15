package com.java110.common.cmd.applicationKey;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.machine.ApplicationKeyDto;
import com.java110.intf.common.IApplicationKeyInnerServiceSMO;
import com.java110.intf.common.IApplicationKeyV1InnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.po.applicationKey.ApplicationKeyPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "applicationKey.auditApplicationKey")
public class AuditApplicationKeyCmd extends Cmd {

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;

    @Autowired
    private IApplicationKeyV1InnerServiceSMO applicationKeyV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "applicationKeyId", "钥匙申请ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写审核状态");
        Assert.hasKeyAndValue(reqJson, "remark", "必填，请填写审核原因");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        //添加单元信息
        updateApplicationKey(reqJson);
    }

    /**
     * 添加钥匙申请信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    private void updateApplicationKey(JSONObject paramInJson) {
        //根据位置id 和 位置对象查询相应 设备ID

        ApplicationKeyDto applicationKeyDto = new ApplicationKeyDto();
        applicationKeyDto.setApplicationKeyId(paramInJson.getString("applicationKeyId"));
        applicationKeyDto.setCommunityId(paramInJson.getString("communityId"));
        List<ApplicationKeyDto> applicationKeyDtos = applicationKeyInnerServiceSMOImpl.queryApplicationKeys(applicationKeyDto);
        Assert.listOnlyOne(applicationKeyDtos, "未找到申请记录或找到多条记录");

        ApplicationKeyPo applicationKeyPo = BeanConvertUtil.covertBean(applicationKeyDtos.get(0), ApplicationKeyPo.class);
        applicationKeyPo.setState("1100".equals(paramInJson.getString("state")) ? "10001" : "10003");

        int flag = applicationKeyV1InnerServiceSMOImpl.updateApplicationKey(applicationKeyPo);

        if (flag < 1) {
            throw new CmdException("申请钥匙失败");
        }
    }
}
