package com.java110.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.config.properties.code.Java110Properties;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.Environment;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairPoolV1InnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.community.IRepairUserV1InnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.po.fee.PayFeePo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "ownerRepair.repairDispatch")
public class RepairDispatchCmd extends Cmd {

    //派单
    public static final String ACTION_DISPATCH = "DISPATCH";

    //转单
    public static final String ACTION_TRANSFER = "TRANSFER";

    //退单
    public static final String ACTION_BACK = "BACK";

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private Java110Properties java110Properties;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;


    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键(维修师傅未处理最大单数)
    public static final String REPAIR_NUMBER = "REPAIR_NUMBER";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "staffId", "未包含员工ID信息");
        Assert.hasKeyAndValue(reqJson, "staffName", "未包含员工名称信息");
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "context", "未包含派单内容");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "action", "未包含处理动作");

        if (!reqJson.containsKey("userId")) {
            reqJson.put("userId", context.getReqHeaders().get("user-id"));
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
//        RepairUserPo repairUserPo = BeanConvertUtil.covertBean(reqJson, RepairUserPo.class);
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
    }

    private void backRepair(ICmdDataFlowContext context, JSONObject reqJson) {
        //查询订单状态
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(reqJson.getString("repairId"));
        repairDto.setCommunityId(reqJson.getString("communityId"));
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.listOnlyOne(repairDtos, "当前用户没有需要处理订单或存在多条");
        int flag = 0;
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
                    ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "收费结束，不能退单！");
                    context.setResponseEntity(responseEntity);
                    return;
                }
                PayFeePo payFeePo = new PayFeePo();
                payFeePo.setCommunityId(feeDtos.get(0).getCommunityId());
                payFeePo.setFeeId(feeDtos.get(0).getFeeId());
                //删除费用
                flag = payFeeV1InnerServiceSMOImpl.deletePayFee(payFeePo);
                if (flag < 1) {
                    throw new CmdException("删除费用失败");
                }
                //删除费用属性
//                FeeAttrPo feeAttrPo = new FeeAttrPo();
//                feeAttrPo.setAttrId(feeAttrDtos.get(0).getAttrId());
//                feeAttrPo.setCommunityId(feeAttrDtos.get(0).getCommunityId());
//                super.delete(context, feeAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FEE_INFO);
//                flag = feeAttrInnerServiceSMOImpl.d(payFeePo);
//                if (flag < 1) {
//                    throw new CmdException("删除费用失败");
//                }
            }
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);
            context.setResponseEntity(responseEntity);
        }
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setCommunityId(reqJson.getString("communityId"));
        //repairUserDto.setState(RepairUserDto.STATE_DOING);
        repairUserDto.setStaffId(reqJson.getString("userId"));
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        if (repairUserDtos != null && repairUserDtos.size() != 1) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "当前用户没有需要处理订单！");
            context.setResponseEntity(responseEntity);
            return;
        }
//        String ruId = repairUserDtos.get(0).getRuId();
//        RepairUserPo repairUserPo = new RepairUserPo();
//        repairUserPo.setRuId(repairUserDtos.get(0).getRuId());
//        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
//        repairUserPo.setState(RepairUserDto.STATE_BACK);
//        repairUserPo.setContext(reqJson.getString("context"));
//        repairUserInnerServiceSMOImpl.updateRepairUser(repairUserPo);
        //处理人信息
//        repairUserPo = new RepairUserPo();
//        repairUserPo.setRuId("-1");
//        repairUserPo.setState(RepairUserDto.STATE_DOING);
//        repairUserPo.setRepairId(reqJson.getString("repairId"));
//        repairUserPo.setStaffId(reqJson.getString("staffId"));
//        repairUserPo.setStaffName(reqJson.getString("staffName"));
        RepairUserDto repair = new RepairUserDto();
        repair.setRepairId(reqJson.getString("repairId"));
        repair.setStaffId(reqJson.getString("staffId"));
        repair.setCommunityId(reqJson.getString("communityId"));
//        repair.setRuId(repairUserDtos.get(0).getPreRuId());
        repair.setStates(new String[]{RepairUserDto.STATE_TRANSFER, RepairUserDto.STATE_CLOSE, RepairUserDto.STATE_STOP});
        List<RepairUserDto> repairUsers = repairUserInnerServiceSMOImpl.queryRepairUsers(repair);
        if (repairUsers == null || repairUsers.size() < 1) { //指派的不能退单
            if (RepairDto.REPAIR_WAY_GRABBING.equals(repairDtos.get(0).getRepairWay())
                    || RepairDto.REPAIR_WAY_TRAINING.equals(repairDtos.get(0).getRepairWay())) {
                modifyBusinessRepairDispatch(reqJson, RepairDto.STATE_WAIT);//维修单变成未派单
                //把自己改成退单
                RepairUserPo repairUser = new RepairUserPo();
                repairUser.setRuId(repairUserDtos.get(0).getRuId());
                repairUser.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                repairUser.setState(RepairUserDto.STATE_BACK);
                repairUser.setContext(reqJson.getString("context"));
                repairUser.setCommunityId(reqJson.getString("communityId"));
                flag = repairUserV1InnerServiceSMOImpl.updateRepairUserNew(repairUser);
                if (flag < 1) {
                    throw new CmdException("修改用户失败");
                }
                return;
            } else {
                ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "非常抱歉当前不能退单，报修设置为指派模式不可进行退单！");
                context.setResponseEntity(responseEntity);
                return;
            }
        }
        //把自己改成退单
        RepairUserPo repairUser = new RepairUserPo();
        repairUser.setRuId(repairUserDtos.get(0).getRuId());
        repairUser.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUser.setState(RepairUserDto.STATE_BACK);
        repairUser.setContext(reqJson.getString("context"));
        repairUser.setCommunityId(reqJson.getString("communityId"));
        flag = repairUserV1InnerServiceSMOImpl.updateRepairUserNew(repairUser);
        if (flag < 1) {
            throw new CmdException("修改用户失败");
        }
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
        repairUserPo.setRepairId(reqJson.getString("repairId"));
        repairUserPo.setbId("-1");
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        repairUserPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_DOING);
        repairUserPo.setContext("");
        repairUserPo.setStaffId(repairUserDtos.get(0).getPreStaffId());
        repairUserPo.setStaffName(repairUserDtos.get(0).getPreStaffName());
        repairUserPo.setPreStaffId(repairUserDtos.get(0).getStaffId());
        repairUserPo.setPreStaffName(repairUserDtos.get(0).getStaffName());
        repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER);
        repairUserPo.setPreRuId(repairUserDtos.get(0).getRuId());
        flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
        if (flag < 1) {
            throw new CmdException("修改用户失败");
        }
        modifyBusinessRepairDispatch(reqJson, RepairDto.STATE_BACK);
    }

    /**
     * 转单
     *
     * @param context
     * @param reqJson
     */
    private void transferRepair(ICmdDataFlowContext context, JSONObject reqJson) {
        //获取接受转单的员工
        String staffId = reqJson.getString("staffId");
        int flag = 0;
        RepairDto repair = new RepairDto();
        repair.setStaffId(staffId);
        repair.setState("10001"); //处理中
        int i = repairInnerServiceSMOImpl.queryStaffRepairsCount(repair);
        //取出开关映射的值(维修师傅未处理最大单数)
        String repairNumber = MappingCache.getValue(MappingConstant.REPAIR_DOMAIN, REPAIR_NUMBER);
        if (i >= Integer.parseInt(repairNumber)) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "该员工有超过" + Integer.parseInt(repairNumber) + "条未处理的订单急需处理，请安排其他维修人员处理！");
            context.setResponseEntity(responseEntity);
            return;
        }
        String userId = reqJson.getString("userId");
        String userName = reqJson.getString("userName");
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setCommunityId(reqJson.getString("communityId"));
        repairUserDto.setState(RepairUserDto.STATE_DOING);
        repairUserDto.setStaffId(userId);
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        if (repairUserDtos != null && repairUserDtos.size() != 1) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "当前用户没有需要处理订单！");
            context.setResponseEntity(responseEntity);
            return;
        }
        //插入派单者的信息
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(repairUserDtos.get(0).getRuId());
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_TRANSFER);
        repairUserPo.setContext(reqJson.getString("context"));
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        flag = repairUserV1InnerServiceSMOImpl.updateRepairUserNew(repairUserPo);
        if (flag < 1) {
            throw new CmdException("修改用户失败");
        }        //处理人信息
        repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
        repairUserPo.setState(RepairUserDto.STATE_DOING);
        repairUserPo.setRepairId(reqJson.getString("repairId"));
        repairUserPo.setStaffId(reqJson.getString("staffId"));
        repairUserPo.setStaffName(reqJson.getString("staffName"));
        repairUserPo.setPreStaffId(userId);
        repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setPreStaffName(userName);
        repairUserPo.setPreRuId(repairUserDtos.get(0).getRuId());
        repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER);
        repairUserPo.setContext("");
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
        if (flag < 1) {
            throw new CmdException("修改用户失败");
        }
        modifyBusinessRepairDispatch(reqJson, RepairDto.STATE_TRANSFER);

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 派单处理
     *
     * @param context
     * @param reqJson
     */
    private void dispacthRepair(ICmdDataFlowContext context, JSONObject reqJson) {
        int flag = 0;
        //获取接受派单的员工
        String staffId = reqJson.getString("staffId");
        RepairDto repair = new RepairDto();
        repair.setStaffId(staffId);
        repair.setState("10001"); //处理中
        int i = repairInnerServiceSMOImpl.queryStaffRepairsCount(repair);
        //取出开关映射的值(维修师傅未处理最大单数)
        String repairNumber = MappingCache.getValue(MappingConstant.REPAIR_DOMAIN, REPAIR_NUMBER);
        if (i >= Integer.parseInt(repairNumber)) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "该员工有超过" + Integer.parseInt(repairNumber) + "条未处理的订单急需处理，请安排其他维修人员处理！");
            context.setResponseEntity(responseEntity);
            return;
        }

        //手机端处理
        if (Environment.isOwnerPhone(java110Properties)) {
            modifyBusinessRepairDispatch(reqJson, RepairDto.STATE_TAKING);
            return;
        }
        //获取报修id
        String repairId = reqJson.getString("repairId");
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(repairId);
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        if (repairDtos == null || repairDtos.size() < 1) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "数据错误！");
            context.setResponseEntity(responseEntity);
        } else {
            //获取状态
            String state = repairDtos.get(0).getState();
            if (state.equals("1000")) {   //1000表示未派单
                String userId = reqJson.getString("userId");
                String userName = reqJson.getString("userName");

                String ruId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId);
                // 自己的单子状态修改为转单
                RepairUserPo repairUserPo = new RepairUserPo();
                repairUserPo.setRuId(ruId);
                repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                repairUserPo.setState(RepairUserDto.STATE_DISPATCH);
                repairUserPo.setRepairId(reqJson.getString("repairId"));
                repairUserPo.setStaffId(userId);
                repairUserPo.setStaffName(userName);
                freshPreStaff(reqJson, repairUserPo);
                repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER);
                repairUserPo.setContext(reqJson.getString("context"));
                repairUserPo.setCommunityId(reqJson.getString("communityId"));
                flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
                if (flag < 1) {
                    throw new CmdException("修改用户失败");
                }
                //处理人信息
                repairUserPo = new RepairUserPo();
                repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
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
                repairUserPo.setPreRuId(ruId);
                repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER);
                repairUserPo.setContext("");
                repairUserPo.setCommunityId(reqJson.getString("communityId"));
                flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
                if (flag < 1) {
                    throw new CmdException("修改用户失败");
                }
                modifyBusinessRepairDispatch(reqJson, RepairDto.STATE_TAKING);
                ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);
                context.setResponseEntity(responseEntity);
            } else if (state.equals("1100")) {   //1100表示接单
                ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "该订单处于接单状态，无法进行派单！");
                context.setResponseEntity(responseEntity);
            } else {
                ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "状态异常！");
                context.setResponseEntity(responseEntity);
            }
        }
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
            repairUserPo.setPreRuId("-1");
        } else {
            int pos = repairUserDtos.size() - 1;
            repairUserPo.setPreStaffId(repairUserDtos.get(pos).getStaffId());
            repairUserPo.setPreStaffName(repairUserDtos.get(pos).getStaffName());
            repairUserPo.setPreRuId(repairUserDtos.get(pos).getRuId());
        }
    }

    public void modifyBusinessRepairDispatch(JSONObject paramInJson, String state) {
        //查询报修单
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(paramInJson.getString("repairId"));
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.isOne(repairDtos, "查询到多条数据，repairId=" + repairDto.getRepairId());
        JSONObject businessOwnerRepair = new JSONObject();
        businessOwnerRepair.putAll(BeanConvertUtil.beanCovertMap(repairDtos.get(0)));
        businessOwnerRepair.put("state", state);
        //计算 应收金额
        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(businessOwnerRepair, RepairPoolPo.class);
        int flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
        if (flag < 1) {
            throw new CmdException("修改工单失败");
        }
    }
}
