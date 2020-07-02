package com.java110.api.listener.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ownerRepair.IOwnerRepairBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.community.IRepairInnerServiceSMO;
import com.java110.core.smo.community.IRepairUserInnerServiceSMO;
import com.java110.core.smo.fee.IFeeAttrInnerServiceSMO;
import com.java110.core.smo.fee.IFeeInnerServiceSMO;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeRepairDispatchStepConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("repairDispatchListener")
public class RepairDispatchListener extends AbstractServiceApiPlusListener {


    //派单
    public static final String ACTION_DISPATCH = "DISPATCH";

    //转单
    public static final String ACTION_TRANSFER = "TRANSFER";
    //退单
    public static final String ACTION_BACK = "BACK";


    private static Logger logger = LoggerFactory.getLogger(RepairDispatchListener.class);

    @Autowired
    private IOwnerRepairBMO ownerRepairBMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "staffId", "未包含员工ID信息");
        Assert.hasKeyAndValue(reqJson, "staffName", "未包含员工名称信息");
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "context", "未包含派单内容");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "action", "未包含处理动作");


    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        RepairUserPo repairUserPo = BeanConvertUtil.covertBean(reqJson, RepairUserPo.class);

        String action = reqJson.getString("action");

        switch (action) {
            case ACTION_DISPATCH:
                dispacthRepair(context, reqJson);
                break;
            case ACTION_TRANSFER:
                transferRepair(context, reqJson);
                break;
            case ACTION_BACK:
                backRepair(context, reqJson);
                break;

        }

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);

        context.setResponseEntity(responseEntity);

    }

    private void backRepair(DataFlowContext context, JSONObject reqJson) {

        //查询订单状态
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(reqJson.getString("repairId"));
        repairDto.setCommunityId(reqJson.getString("communityId"));
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.listOnlyOne(repairDtos, "当前用户没有需要处理订单或存在多条");

        //待评价
        if (RepairDto.STATE_APPRAISE.equals(repairDtos.get(0).getState())) {
            FeeAttrDto feeAttrDto = new FeeAttrDto();
            feeAttrDto.setCommunityId(reqJson.getString("communityId"));
            feeAttrDto.setSpecCd(FeeAttrDto.SPEC_CD_REPAIR);
            feeAttrDto.setValue(reqJson.getString("repairId"));
            List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);
            if (feeAttrDtos != null && feeAttrDtos.size() > 0) {
                FeeDto feeDto = new FeeDto();
                feeDto.setCommunityId(reqJson.getString("communityId"));
                feeDto.setFeeId(feeAttrDtos.get(0).getFeeId());
                List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
                //收费结束
                if (FeeDto.STATE_FINISH.equals(feeDtos.get(0).getState())) {
                    throw new IllegalArgumentException("收费结束，不能退单");
                }

                PayFeePo payFeePo = new PayFeePo();
                payFeePo.setCommunityId(feeDtos.get(0).getCommunityId());
                payFeePo.setFeeId(feeDtos.get(0).getFeeId());
                //删除费用
                super.delete(context, payFeePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FEE_INFO);

                //删除费用属性
                FeeAttrPo feeAttrPo = new FeeAttrPo();
                feeAttrPo.setAttrId(feeAttrDtos.get(0).getAttrId());
                feeAttrPo.setCommunityId(feeAttrDtos.get(0).getCommunityId());
                super.delete(context, feeAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FEE_INFO);

            }
        }


        String userId = reqJson.getString("userId");
        String userName = reqJson.getString("userName");

        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setCommunityId(reqJson.getString("communityId"));
        repairUserDto.setState(RepairUserDto.STATE_DOING);
        repairUserDto.setStaffId(userId);
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        Assert.listOnlyOne(repairUserDtos, "当前用户没有需要处理订单");
        //插入派单者的信息
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(repairUserDtos.get(0).getRuId());
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_BACK);
        repairUserPo.setContext(reqJson.getString("context"));
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        super.update(context, repairUserPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR_USER);
        //处理人信息
        repairUserPo = new RepairUserPo();
        repairUserPo.setRuId("-1");
        repairUserPo.setState(RepairUserDto.STATE_DOING);
        repairUserPo.setRepairId(reqJson.getString("repairId"));
        repairUserPo.setStaffId(reqJson.getString("staffId"));
        repairUserPo.setStaffName(reqJson.getString("staffName"));
        repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setStaffId(reqJson.getString("staffId"));
        repairUserDto.setCommunityId(reqJson.getString("communityId"));
        repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserDto.setState(RepairUserDto.STATE_TRANSFER);
        repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);

        if (repairUserDtos == null || repairUserDtos.size() < 1) {
            if (RepairDto.REPAIR_WAY_GRABBING.equals(repairDtos.get(0).getRepairWay())
                    || RepairDto.REPAIR_WAY_TRAINING.equals(repairDtos.get(0).getRepairWay())
            ) {
                ownerRepairBMOImpl.modifyBusinessRepairDispatch(reqJson, context, RepairDto.STATE_WAIT);
                return ;
            } else {
                throw new IllegalArgumentException("未找到上级处理人");
            }
        }

        repairUserPo.setPreStaffId(repairUserDtos.get(0).getPreStaffId());
        repairUserPo.setPreStaffName(repairUserDtos.get(0).getPreStaffName());
        repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER);
        repairUserPo.setContext("");
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        super.insert(context, repairUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_USER);
        ownerRepairBMOImpl.modifyBusinessRepairDispatch(reqJson, context, RepairDto.STATE_BACK);
    }

    /**
     * 转单
     *
     * @param context
     * @param reqJson
     */
    private void transferRepair(DataFlowContext context, JSONObject reqJson) {
        String userId = reqJson.getString("userId");
        String userName = reqJson.getString("userName");

        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setCommunityId(reqJson.getString("communityId"));
        repairUserDto.setState(RepairUserDto.STATE_DOING);
        repairUserDto.setStaffId(userId);
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        Assert.listOnlyOne(repairUserDtos, "当前用户没有需要处理订单");
        //插入派单者的信息
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(repairUserDtos.get(0).getRuId());
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_TRANSFER);
        repairUserPo.setContext(reqJson.getString("context"));
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        super.update(context, repairUserPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR_USER);
        //处理人信息
        repairUserPo = new RepairUserPo();
        repairUserPo.setRuId("-1");
        repairUserPo.setState(RepairUserDto.STATE_DOING);
        repairUserPo.setRepairId(reqJson.getString("repairId"));
        repairUserPo.setStaffId(reqJson.getString("staffId"));
        repairUserPo.setStaffName(reqJson.getString("staffName"));
        repairUserPo.setPreStaffId(userId);
        repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setPreStaffName(userName);
        repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER);
        repairUserPo.setContext("");
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        super.insert(context, repairUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_USER);
        ownerRepairBMOImpl.modifyBusinessRepairDispatch(reqJson, context, RepairDto.STATE_TRANSFER);

    }

    /**
     * 派单处理
     *
     * @param context
     * @param reqJson
     */
    private void dispacthRepair(DataFlowContext context, JSONObject reqJson) {
        String userId = reqJson.getString("userId");
        String userName = reqJson.getString("userName");

        // 自己的单子状态修改为转单
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId("-1");
        repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_TRANSFER);
        repairUserPo.setRepairId(reqJson.getString("repairId"));
        repairUserPo.setStaffId(userId);
        repairUserPo.setStaffName(userName);
        freshPreStaff(reqJson, repairUserPo);
        repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER);
        repairUserPo.setContext(reqJson.getString("context"));
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        super.insert(context, repairUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_USER);
        //处理人信息
        repairUserPo = new RepairUserPo();
        repairUserPo.setRuId("-2");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 30);
        repairUserPo.setStartTime(DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_DOING);
        repairUserPo.setRepairId(reqJson.getString("repairId"));
        repairUserPo.setStaffId(reqJson.getString("staffId"));
        repairUserPo.setStaffName(reqJson.getString("staffName"));
        repairUserPo.setPreStaffId(userId);
        repairUserPo.setPreStaffName(userName);
        repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER);
        repairUserPo.setContext("");
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        super.insert(context, repairUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_USER);
        ownerRepairBMOImpl.modifyBusinessRepairDispatch(reqJson, context, RepairDto.STATE_TAKING);
    }

    /**
     * 刷入上一处理人
     *
     * @param reqJson
     * @param repairUserPo
     */
    private void freshPreStaff(JSONObject reqJson, RepairUserPo repairUserPo) {

        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setCommunityId(reqJson.getString("communityId"));
        //repairUserDto.setRepairEvent(RepairUserDto.REPAIR_EVENT_START_USER);
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        if (repairUserDtos == null || repairUserDtos.size() < 1) {
            repairUserPo.setPreStaffId("-1");
            repairUserPo.setPreStaffName("-1");
        } else {
            int pos = repairUserDtos.size() - 1;
            repairUserPo.setPreStaffId(repairUserDtos.get(pos).getStaffId());
            repairUserPo.setPreStaffName(repairUserDtos.get(pos).getStaffName());
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeRepairDispatchStepConstant.BINDING_REPAIR_DISPATCH;
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
