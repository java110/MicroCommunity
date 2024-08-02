package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.fee.bmo.IQueryOweFee;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

/**
 * 查询业主综合缴费（欠费）
 */
@Java110Cmd(serviceCode = "fee.queryOwnerOweFee")
public class QueryOwnerOweFeeCmd extends Cmd {

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IQueryOweFee queryOweFeeImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        super.validatePageInfo(reqJson);


        String userId = CmdContextUtils.getUserId(context);
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userId);
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ListUtil.isNull(ownerAppUserDtos)
                || OwnerAppUserDto.STATE_AUDIT_ERROR.equals(ownerAppUserDtos.get(0).getState())) {
            throw new CmdException("用户未认证,请先认证");
        }

        if (OwnerAppUserDto.STATE_AUDITING.equals(ownerAppUserDtos.get(0).getState())) {
            ResultVo resultVo = new ResultVo(-101, "认证审核中");
            context.setResponseEntity(ResultVo.createResponseEntity(resultVo));
            return;
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerAppUserDtos.get(0).getMemberId());
        ownerDto.setCommunityId(ownerAppUserDtos.get(0).getCommunityId());
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if (ListUtil.isNull(ownerDtos)) {
            throw new CmdException("业主不存在");
        }

        ownerDto = ownerDtos.get(0);

        reqJson.put("ownerId", ownerDto.getOwnerId());
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        FeeDto feeDto = BeanConvertUtil.covertBean(reqJson, FeeDto.class);

        String payObjId = reqJson.getString("payObjId");
        if (!StringUtil.isEmpty(payObjId)) {
            feeDto.setPayerObjId("");
            if (payObjId.contains(",")) {
                feeDto.setPayerObjIds(payObjId.split(","));
            } else {
                feeDto.setPayerObjId(payObjId);
            }
        }
        String targetEndTime = reqJson.getString("targetEndTime");
        if (!StringUtil.isEmpty(targetEndTime)) {
            targetEndTime = targetEndTime + " 23:59:59";
            feeDto.setTargetEndTime(targetEndTime);
        }
        ResponseEntity<String> responseEntity = queryOweFeeImpl.query(feeDto);
        context.setResponseEntity(responseEntity);
    }
}
