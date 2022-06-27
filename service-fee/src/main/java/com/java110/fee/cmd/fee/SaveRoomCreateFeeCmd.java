package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFeeBatch.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bmo.fee.IFeeBMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.payFeeBatch.PayFeeBatchPo;
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


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        // super.validatePageInfo(pd);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "locationTypeCd", "未包含收费范围");
        Assert.hasKeyAndValue(reqJson, "locationObjId", "未包含收费对象");
        Assert.hasKeyAndValue(reqJson, "configId", "未包含收费项目");
        //Assert.hasKeyAndValue(reqJson, "startTime", "未包含收费其实时间");
        //Assert.hasKeyAndValue(reqJson, "billType", "未包含出账类型");
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
            try {
                endTime = DateUtil.getDateFromString(reqJson.getString("endTime"), DateUtil.DATE_FORMATE_STRING_B);
                configEndTime = DateUtil.getDateFromString(feeConfigDtos.get(0).getEndTime(), DateUtil.DATE_FORMATE_STRING_A);
                if (endTime.getTime() > configEndTime.getTime()) {
                    throw new IllegalArgumentException("结束时间不能超过费用项时间");
                }
            } catch (ParseException e) {
                throw new IllegalArgumentException("结束时间错误" + reqJson.getString("endTime"));
            }
        }

        if (FeeConfigDto.COMPUTING_FORMULA_RANT_RATE.equals(feeConfigDtos.get(0).getComputingFormula())){
            Assert.hasKeyAndValue(reqJson, "rateCycle", "未包含递增周期");
            Assert.hasKeyAndValue(reqJson, "rate", "未包含递增率");
            Assert.hasKeyAndValue(reqJson, "rateStartTime", "未包含递增开始时间");
            reqJson.put("configComputingFormula",feeConfigDtos.get(0).getComputingFormula());
        }


    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String userId = cmdDataFlowContext.getReqHeaders().get(CommonConstant.USER_ID);
        reqJson.put("userId", userId);
        List<RoomDto> roomDtos = null;


        //生成批次号
        generatorBatch(reqJson);
        //判断收费范围
        RoomDto roomDto = new RoomDto();
        /*if (reqJson.containsKey("roomState") && RoomDto.STATE_SELL.equals(reqJson.getString("roomState"))) {
            roomDto.setState(RoomDto.STATE_SELL);
        }*/
        if (reqJson.containsKey("roomState")
                && (reqJson.getString("roomState").contains(",") || !StringUtil.isEmpty(reqJson.getString("roomState")))) {
            String states = reqJson.getString("roomState");
            roomDto.setStates(states.split(","));
        }
        //如果区分了 房屋类型则设置
        if (reqJson.containsKey("roomType")) {
            roomDto.setRoomType(reqJson.getString("roomType"));
        }
        if (reqJson.containsKey("feeLayer") && !"全部".equals(reqJson.getString("feeLayer"))) {
            String[] layers = reqJson.getString("feeLayer").split("#");
            roomDto.setLayers(layers);
        }
        if ("1000".equals(reqJson.getString("locationTypeCd"))) {//小区
            roomDto.setCommunityId(reqJson.getString("communityId"));
            roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        } else if ("4000".equals(reqJson.getString("locationTypeCd"))) {//楼栋
            //RoomDto roomDto = new RoomDto();
            roomDto.setCommunityId(reqJson.getString("communityId"));
            roomDto.setFloorId(reqJson.getString("locationObjId"));
            roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        } else if ("2000".equals(reqJson.getString("locationTypeCd"))) {//单元
            //RoomDto roomDto = new RoomDto();
            roomDto.setCommunityId(reqJson.getString("communityId"));
            roomDto.setUnitId(reqJson.getString("locationObjId"));
            roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        } else if ("3000".equals(reqJson.getString("locationTypeCd"))) {//房屋
            //RoomDto roomDto = new RoomDto();
            roomDto.setCommunityId(reqJson.getString("communityId"));
            roomDto.setRoomId(reqJson.getString("locationObjId"));
            roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        } else {
            throw new IllegalArgumentException("收费范围错误");
        }
        if (roomDtos == null || roomDtos.size() < 1) {
            throw new IllegalArgumentException("未查到需要付费的房屋或未绑定业主");
        }
        dealRoomFee(roomDtos, cmdDataFlowContext, reqJson, event);
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

    private void dealRoomFee(List<RoomDto> roomDtos, ICmdDataFlowContext context, JSONObject reqJson, CmdEvent event) {
        List<String> roomIds = new ArrayList<>();
        for (RoomDto roomDto : roomDtos) {
            roomIds.add(roomDto.getRoomId());
        }
        //房屋刷入业主信息
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(roomDtos.get(0).getCommunityId());
        ownerDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);
        for (RoomDto roomDto : roomDtos) {
            for (OwnerDto tmpOwnerDto : ownerDtos) {
                if (roomDto.getRoomId().equals(tmpOwnerDto.getRoomId())) {
                    roomDto.setOwnerId(tmpOwnerDto.getOwnerId());
                    roomDto.setOwnerName(tmpOwnerDto.getName());
                    roomDto.setLink(tmpOwnerDto.getLink());
                }
            }
        }
        List<PayFeePo> feePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrsPos = new ArrayList<>();
        ResponseEntity<String> responseEntity = null;
        int failRooms = 0;
        //添加单元信息
        int curFailRoomCount = 0;
        int saveFlag = 0;
        for (int roomIndex = 0; roomIndex < roomDtos.size(); roomIndex++) {
            curFailRoomCount++;
            //加入 房屋费用
            feePos.add(BeanConvertUtil.covertBean(feeBMOImpl.addRoomFee(roomDtos.get(roomIndex), reqJson, context), PayFeePo.class));
            if (!StringUtil.isEmpty(roomDtos.get(roomIndex).getOwnerId())) {
                if (!FeeDto.FEE_FLAG_CYCLE.equals(reqJson.getString("feeFlag"))) {
                    feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME,
                            reqJson.containsKey("endTime") ? reqJson.getString("endTime") : reqJson.getString("configEndTime")));
                }
                feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_OWNER_ID, roomDtos.get(roomIndex).getOwnerId()));
                feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_OWNER_LINK, roomDtos.get(roomIndex).getLink()));
                feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_OWNER_NAME, roomDtos.get(roomIndex).getOwnerName()));
            }

            //定制开发 加入
            //1、对合同约定的租金递增比例、递增年限各不相同的问题，支持按合同到期日期设租金递增比例。
            //2、能自动设置递增的租金实行自动计算当月的租金。
            if(reqJson.containsKey("configComputingFormula")
                    && FeeConfigDto.COMPUTING_FORMULA_RANT_RATE.equals(reqJson.getString("configComputingFormula"))){
                feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_RATE_CYCLE, reqJson.getString("rateCycle")));
                feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_RATE, reqJson.getString("rate")));
                feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_RATE_START_TIME, reqJson.getString("rateStartTime")));
            }

            //付费对象名称
            feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME,
                    roomDtos.get(roomIndex).getFloorNum() + "-" + roomDtos.get(roomIndex).getUnitNum() + "-" + roomDtos.get(roomIndex).getRoomNum()));

            if (roomIndex % DEFAULT_ADD_FEE_COUNT == 0 && roomIndex != 0) {
                saveFlag = saveFeeAndAttrs(feePos, feeAttrsPos);
                feePos = new ArrayList<>();
                feeAttrsPos = new ArrayList<>();
                if (saveFlag < 1) {
                    failRooms += curFailRoomCount;
                } else {
                    curFailRoomCount = 0;
                }
            }
        }
        if (feePos != null && feePos.size() > 0) {
            saveFlag = saveFeeAndAttrs(feePos, feeAttrsPos);
            if (saveFlag < 1) {
                failRooms += curFailRoomCount;
            }
        }
        JSONObject paramOut = new JSONObject();
        paramOut.put("totalRoom", roomDtos.size());
        paramOut.put("successRoom", roomDtos.size() - failRooms);
        paramOut.put("errorRoom", failRooms);
        responseEntity = new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    private int saveFeeAndAttrs(List<PayFeePo> feePos, List<FeeAttrPo> feeAttrsPos) {
        int flag = feeInnerServiceSMOImpl.saveFee(feePos);
        if (flag < 1) {
            return flag;
        }

        flag = feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrsPos);

        return flag;
    }


}
