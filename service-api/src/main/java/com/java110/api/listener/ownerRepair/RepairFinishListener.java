package com.java110.api.listener.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ownerRepair.IOwnerRepairBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.community.IRepairInnerServiceSMO;
import com.java110.core.smo.community.IRepairUserInnerServiceSMO;
import com.java110.core.smo.fee.IFeeConfigInnerServiceSMO;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.constant.ServiceCodeRepairDispatchStepConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 完成报修
 * add by wuxw 2019-06-30
 */
@Java110Listener("repairCloseListener")
public class RepairFinishListener extends AbstractServiceApiPlusListener {


    private static Logger logger = LoggerFactory.getLogger(RepairFinishListener.class);

    @Autowired
    private IOwnerRepairBMO ownerRepairBMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "context", "未包含派单内容");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "amount", "未包含金额");
        Assert.hasKeyAndValue(reqJson, "feeFlag", "未包含费用标识");


    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        String userId = reqJson.getString("userId");
        String userName = reqJson.getString("userName");

        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setCommunityId(reqJson.getString("communityId"));
        repairUserDto.setState(RepairUserDto.STATE_DOING);
        repairUserDto.setStaffId(userId);
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        Assert.listOnlyOne(repairUserDtos, "当前用户没有需要处理订单");
        // 1.0 关闭自己订单
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(repairUserDtos.get(0).getRuId());
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_CLOSE);
        repairUserPo.setContext(reqJson.getString("context"));
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        super.update(context, repairUserPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR_USER);
        //2.0 给开始节点派支付单
        repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setCommunityId(reqJson.getString("communityId"));
        repairUserDto.setRepairEvent(RepairUserDto.REPAIR_EVENT_START_USER);
        repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        Assert.listOnlyOne(repairUserDtos, "数据错误 该订单没有发起人");
        repairUserPo = new RepairUserPo();
        repairUserPo.setRuId("-1");
        repairUserPo.setState(RepairUserDto.STATE_DOING);
        repairUserPo.setRepairId(reqJson.getString("repairId"));
        repairUserPo.setStaffId(repairUserDtos.get(0).getStaffId());
        repairUserPo.setStaffName(repairUserDtos.get(0).getStaffName());
        repairUserPo.setPreStaffId(userId);
        repairUserPo.setPreStaffName(userName);
        repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_PAY_USER);
        repairUserPo.setContext("");
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        super.insert(context, repairUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_USER);

        if ("200".equals(reqJson.getString("feeFlag"))) { // 没有费用
            ownerRepairBMOImpl.modifyBusinessRepairDispatch(reqJson, context, RepairDto.STATE_APPRAISE);
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        //3.0 生成支付费用
        //查询默认费用项
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_REPAIR);
        feeConfigDto.setIsDefault(FeeConfigDto.DEFAULT_FEE_CONFIG);
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        Assert.listOnlyOne(feeConfigDtos, "默认维修费用有多条或不存在");

        PayFeePo feePo = new PayFeePo();
        feePo.setAmount(reqJson.getString("amount"));
        feePo.setCommunityId(reqJson.getString("communityId"));
        feePo.setConfigId(feeConfigDtos.get(0).getConfigId());
        feePo.setEndTime(DateUtil.getLastTime());
        feePo.setFeeFlag(feeConfigDtos.get(0).getFeeFlag());
        feePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        feePo.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_REPAIR);
        feePo.setIncomeObjId(reqJson.getString("storeId"));
        feePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        RepairDto repairDto = new RepairDto();
        repairDto.setCommunityId(reqJson.getString("communityId"));
        repairDto.setRepairId(reqJson.getString("repairId"));
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.listOnlyOne(repairDtos, "维修单有多条或不存在");

        feePo.setPayerObjId(repairDtos.get(0).getRoomId());
        feePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        feePo.setState(FeeDto.STATE_DOING);
        feePo.setUserId(userId);
        super.insert(context, feePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);

        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setAttrId("-1");
        feeAttrPo.setFeeId(feePo.getFeeId());
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_REPAIR);
        feeAttrPo.setCommunityId(reqJson.getString("communityId"));
        feeAttrPo.setValue(reqJson.getString("repairId"));
        super.insert(context, feeAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);

        ownerRepairBMOImpl.modifyBusinessRepairDispatch(reqJson, context, RepairDto.STATE_PAY);


        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);

        context.setResponseEntity(responseEntity);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeRepairDispatchStepConstant.BINDING_REPAIR_FINISH;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IRepairInnerServiceSMO getRepairInnerServiceSMOImpl() {
        return repairInnerServiceSMOImpl;
    }

    public void setRepairInnerServiceSMOImpl(IRepairInnerServiceSMO repairInnerServiceSMOImpl) {
        this.repairInnerServiceSMOImpl = repairInnerServiceSMOImpl;
    }
}
