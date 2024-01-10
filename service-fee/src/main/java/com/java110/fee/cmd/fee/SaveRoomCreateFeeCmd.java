package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.room.RoomDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFee.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bmo.fee.IFeeBMO;
import com.java110.fee.feeMonth.IPayFeeMonth;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.payFee.PayFeeBatchPo;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 创建费用
 */
@Java110Cmd(serviceCode = "fee.saveRoomCreateFee")
public class SaveRoomCreateFeeCmd extends Cmd {

    private static final int DEFAULT_ADD_FEE_COUNT = 200;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IFeeBMO feeBMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonth payFeeMonthImpl;

    @Autowired
    private IRuleGeneratorPayFeeBillV1InnerServiceSMO ruleGeneratorPayFeeBillV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        // super.validatePageInfo(pd);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "locationObjId", "未包含收费对象");
        Assert.hasKeyAndValue(reqJson, "configId", "未包含收费项目");
        Assert.hasKeyAndValue(reqJson, "storeId", "未包含商户ID");

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setConfigId(reqJson.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "当前费用项ID不存在或存在多条" + reqJson.getString("configId"));
        reqJson.put("feeTypeCd", feeConfigDtos.get(0).getFeeTypeCd());
        reqJson.put("feeFlag", feeConfigDtos.get(0).getFeeFlag());
        reqJson.put("configEndTime", feeConfigDtos.get(0).getEndTime());


        if (!FeeDto.FEE_FLAG_CYCLE.equals(feeConfigDtos.get(0).getFeeFlag()) && reqJson.containsKey("endTime")) {
            Date endTime = null;
            Date configEndTime = null;

            endTime = DateUtil.getDateFromStringB(reqJson.getString("endTime"));
            configEndTime = DateUtil.getDateFromStringA(feeConfigDtos.get(0).getEndTime());
            if (endTime.getTime() > configEndTime.getTime()) {
                throw new IllegalArgumentException("结束时间不能超过费用项时间");
            }
        }

        if (FeeConfigDto.COMPUTING_FORMULA_RANT_RATE.equals(feeConfigDtos.get(0).getComputingFormula())) {
            Assert.hasKeyAndValue(reqJson, "rateCycle", "未包含递增周期");
            Assert.hasKeyAndValue(reqJson, "rate", "未包含递增率");
            Assert.hasKeyAndValue(reqJson, "rateStartTime", "未包含递增开始时间");
            reqJson.put("configComputingFormula", feeConfigDtos.get(0).getComputingFormula());
        }


    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String userId = cmdDataFlowContext.getReqHeaders().get(CommonConstant.USER_ID);
        reqJson.put("userId", userId);
        List<RoomDto> roomDtos = null;

        //todo 生成批次号
        generatorBatch(reqJson);

        RoomDto roomDto = new RoomDto();
        //roomDto.setRoomType(reqJson.getString("roomType"));
        roomDto.setCommunityId(reqJson.getString("communityId"));
        roomDto.setRoomId(reqJson.getString("locationObjId"));
        roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        if (roomDtos == null || roomDtos.isEmpty()) {
            throw new IllegalArgumentException("未查到需要付费的房屋或未绑定业主");
        }
        dealRoomFee(roomDtos.get(0), cmdDataFlowContext, reqJson, event);
    }

    private void dealRoomFee(RoomDto roomDto, ICmdDataFlowContext context, JSONObject reqJson, CmdEvent event) {

        //todo 房屋刷入业主信息
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(roomDto.getCommunityId());
        ownerDto.setRoomId(roomDto.getRoomId());
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);

        if (ownerDtos != null && !ownerDtos.isEmpty()) {
            roomDto.setOwnerId(ownerDtos.get(0).getOwnerId());
            roomDto.setOwnerName(ownerDtos.get(0).getName());
            roomDto.setLink(ownerDtos.get(0).getLink());
        }

        List<PayFeePo> feePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrsPos = new ArrayList<>();
        ResponseEntity<String> responseEntity = null;

        //todo 加入 房屋费用
        feePos.add(BeanConvertUtil.covertBean(feeBMOImpl.addRoomFee(roomDto, reqJson, context), PayFeePo.class));

        //todo 走账单模式
        String billModal = ruleGeneratorPayFeeBillV1InnerServiceSMOImpl.needGeneratorBillData(feePos);
        if ("Y".equals(billModal)) {
            JSONObject paramOut = new JSONObject();
            paramOut.put("totalRoom", 1);
            paramOut.put("successRoom", 1);
            paramOut.put("errorRoom", 0);
            responseEntity = new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }

        if (!StringUtil.isEmpty(roomDto.getOwnerId())) {
            if (!FeeDto.FEE_FLAG_CYCLE.equals(reqJson.getString("feeFlag"))) {
                feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME, reqJson.containsKey("endTime") ? reqJson.getString("endTime") : reqJson.getString("configEndTime")));
            }
            feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_OWNER_ID, roomDto.getOwnerId()));
            feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_OWNER_LINK, roomDto.getLink()));
            feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_OWNER_NAME, roomDto.getOwnerName()));
        }

        //todo 定制开发 加入
        //1、对合同约定的租金递增比例、递增年限各不相同的问题，支持按合同到期日期设租金递增比例。
        //2、能自动设置递增的租金实行自动计算当月的租金。
        if (reqJson.containsKey("configComputingFormula") && FeeConfigDto.COMPUTING_FORMULA_RANT_RATE.equals(reqJson.getString("configComputingFormula"))) {
            feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_RATE_CYCLE, reqJson.getString("rateCycle")));
            feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_RATE, reqJson.getString("rate")));
            feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_RATE_START_TIME, reqJson.getString("rateStartTime")));
        }
        //付费对象名称
        feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME, roomDto.getFloorNum() + "-" + roomDto.getUnitNum() + "-" + roomDto.getRoomNum()));

        int saveFlag = saveFeeAndAttrs(feePos, feeAttrsPos);

        JSONObject paramOut = new JSONObject();
        paramOut.put("totalRoom", 1);
        paramOut.put("successRoom", 1);
        paramOut.put("errorRoom", 0);
        responseEntity = new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    private int saveFeeAndAttrs(List<PayFeePo> feePos, List<FeeAttrPo> feeAttrsPos) {
        if (feePos == null || feePos.size() < 1) {
            return 1;
        }
        int flag = feeInnerServiceSMOImpl.saveFee(feePos);
        if (flag < 1) {
            return flag;
        }

        flag = feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrsPos);

        // todo 这里异步的方式计算 月数据 和欠费数据
        List<String> feeIds = new ArrayList<>();
        for (PayFeePo feePo : feePos) {
            feeIds.add(feePo.getFeeId());
        }
        payFeeMonthImpl.doGeneratorFeeMonths(feeIds, feePos.get(0).getCommunityId());

        payFeeMonthImpl.doGeneratorOweFees(feeIds, feePos.get(0).getCommunityId());
        return flag;
    }


    /**
     * 生成批次号
     *
     * @param reqJson
     */
    private void generatorBatch(JSONObject reqJson) {
        PayFeeBatchPo payFeeBatchPo = new PayFeeBatchPo();
        payFeeBatchPo.setBatchId(GenerateCodeFactory.getGeneratorId("12"));
        payFeeBatchPo.setCommunityId(reqJson.getString("communityId"));
        payFeeBatchPo.setCreateUserId(reqJson.getString("userId"));
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");
        payFeeBatchPo.setCreateUserName(userDtos.get(0).getUserName());
        payFeeBatchPo.setState(PayFeeBatchDto.STATE_NORMAL);
        payFeeBatchPo.setMsg("正常");
        int flag = payFeeBatchV1InnerServiceSMOImpl.savePayFeeBatch(payFeeBatchPo);

        if (flag < 1) {
            throw new IllegalArgumentException("生成批次失败");
        }

        reqJson.put("batchId", payFeeBatchPo.getBatchId());
    }


}
