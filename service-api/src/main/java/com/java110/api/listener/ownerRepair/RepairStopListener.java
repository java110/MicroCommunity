package com.java110.api.listener.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeRepairDispatchStepConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 暂停报修单
 *
 * @author fqz
 * @date 2021-12-24
 */
@Java110Listener("repairStopListener")
public class RepairStopListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeRepairDispatchStepConstant.BINDING_REPAIR_STOP;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) throws ParseException {
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "remark", "未包含派单内容");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(reqJson.getString("repairId"));
        //查询报修信息
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.listOnlyOne(repairDtos, "查询报修单错误！");
        String state = repairDtos.get(0).getState();
        if (!StringUtil.isEmpty(state) && !state.equals(RepairDto.STATE_STOP)) { //报修单不是暂停状态
            RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(reqJson, RepairPoolPo.class);
            repairPoolPo.setState(RepairDto.STATE_STOP); //将报修状态变为暂停状态
            //更新报修状态
            super.update(context, repairPoolPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR);
            RepairUserPo repairUserPo = new RepairUserPo();
            repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId)); //报修派单id
            repairUserPo.setRepairId(reqJson.getString("repairId")); //报修派单
            repairUserPo.setCommunityId(reqJson.getString("communityId")); //小区id
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            repairUserPo.setCreateTime(simpleDateFormat.format(new Date())); //创建时间
            repairUserPo.setState(RepairUserDto.STATE_STOP); //状态(暂停状态)
            repairUserPo.setContext(reqJson.getString("remark")); //报修内容
            repairUserPo.setStaffId(reqJson.getString("userId")); //当前处理员工id
            repairUserPo.setStaffName(reqJson.getString("userName")); //当前处理员工名称
            RepairUserDto repairUserDto = new RepairUserDto();
            repairUserDto.setRepairId(reqJson.getString("repairId"));
            repairUserDto.setState(RepairUserDto.STATE_DOING); //处理中状态
            //查询报修派单
            List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
            Assert.listOnlyOne(repairUserDtos, "查询报修派单错误！");
            repairUserPo.setPreStaffId(repairUserDtos.get(0).getStaffId()); //上一节点处理员工id
            repairUserPo.setPreStaffName(repairUserDtos.get(0).getStaffName()); //上一节点处理员工名称
            repairUserPo.setStartTime(simpleDateFormat.format(new Date())); //开始时间
            repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER); //审核用户
            repairUserPo.setPreRuId(repairUserDtos.get(0).getRuId()); //上一节点处理id
            super.insert(context, repairUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_USER);
        }
    }
}
