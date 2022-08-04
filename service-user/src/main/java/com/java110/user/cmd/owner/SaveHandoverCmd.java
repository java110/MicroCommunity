package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
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
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.owner.OwnerPo;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.po.payFeeBatch.PayFeeBatchPo;
import com.java110.po.room.RoomPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "owner.saveHandover")
public class SaveHandoverCmd extends Cmd {

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求报文中未包含userId");
        Assert.jsonObjectHaveKey(reqJson, "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(reqJson, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(reqJson, "sex", "请求报文中未包含sex");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");

        if (!reqJson.containsKey("rooms")) {
            throw new CmdException("未包含房屋信息");
        }

        JSONArray rooms = reqJson.getJSONArray("rooms");

        if (rooms.size() < 1) {
            throw new CmdException("未包含房屋信息");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        OwnerPo ownerPo = BeanConvertUtil.covertBean(reqJson, OwnerPo.class);
        int flag = 0;
        if (ownerPo.getAge().equals("")) {
            ownerPo.setAge(null);
        }
        if (reqJson.containsKey("ownerId") && !StringUtil.isEmpty(reqJson.getString("ownerId"))) {
            ownerPo.setMemberId(ownerPo.getOwnerId());
            flag = ownerV1InnerServiceSMOImpl.updateOwner(ownerPo);
        } else {
            ownerPo.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
            ownerPo.setOwnerId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId));
            ownerPo.setMemberId(ownerPo.getOwnerId());
            ownerPo.setState(OwnerDto.STATE_FINISH);
            ownerPo.setOwnerFlag(OwnerDto.OWNER_FLAG_TRUE);
            flag = ownerV1InnerServiceSMOImpl.saveOwner(ownerPo);
            reqJson.put("ownerId", ownerPo.getOwnerId());
        }
        if (flag < 1) {
            throw new CmdException("操作业主失败");
        }
        JSONArray rooms = reqJson.getJSONArray("rooms");
        OwnerRoomRelPo ownerRoomRelPo = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            ownerRoomRelPo = new OwnerRoomRelPo();
            ownerRoomRelPo.setbId("-1");
            ownerRoomRelPo.setEndTime(DateUtil.getLastTime());
            ownerRoomRelPo.setOwnerId(reqJson.getString("ownerId"));
            ownerRoomRelPo.setOperate("ADD");
            ownerRoomRelPo.setRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
            ownerRoomRelPo.setRoomId(rooms.getJSONObject(roomIndex).getString("roomId"));
            ownerRoomRelPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            ownerRoomRelPo.setState("2001");
            ownerRoomRelPo.setUserId("-1");
            flag = ownerRoomRelV1InnerServiceSMOImpl.saveOwnerRoomRel(ownerRoomRelPo);
            if (flag < 1) {
                throw new CmdException("操作业主失败");
            }
            RoomPo roomPo = new RoomPo();
            roomPo.setRoomId(rooms.getJSONObject(roomIndex).getString("roomId"));
            roomPo.setCommunityId(reqJson.getString("communityId"));
            roomPo.setState(RoomDto.STATE_SELL);
            flag = roomV1InnerServiceSMOImpl.updateRoom(roomPo);
            if (flag < 1) {
                throw new CmdException("操作业主失败");
            }
        }
        if (!reqJson.containsKey("fees")) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return;
        }
        JSONArray fees = reqJson.getJSONArray("fees");
        if (fees.size() < 1) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return;
        }
        generatorBatch(reqJson);
        JSONObject paramInJson = null;
        PayFeePo payFeePo = null;
        FeeAttrPo feeAttrPo = null;
        List<RoomDto> roomDtos = null;
        RoomDto roomDto = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {

            roomDto = new RoomDto();
            roomDto.setRoomId(rooms.getJSONObject(roomIndex).getString("roomId"));
            roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

            Assert.listOnlyOne(roomDtos, "房屋不存在");

            for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {

                paramInJson = fees.getJSONObject(feeIndex);

                String time = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A);

                if (paramInJson.containsKey("startTime")) {
                    time = paramInJson.getString("startTime");
                }

                //查询费用项
                FeeConfigDto feeConfigDto = new FeeConfigDto();
                feeConfigDto.setConfigId(fees.getJSONObject(feeIndex).getString("configId"));
                List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
                Assert.listOnlyOne(feeConfigDtos, "查询费用项错误！");
                JSONObject businessUnit = new JSONObject();
                businessUnit.put("feeId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
                businessUnit.put("configId", fees.getJSONObject(feeIndex).getString("configId"));
                businessUnit.put("feeTypeCd", feeConfigDtos.get(0).getFeeTypeCd());
                businessUnit.put("incomeObjId", reqJson.getString("storeId"));
                businessUnit.put("amount", "-1.00");
                if (paramInJson.containsKey("amount") && !StringUtil.isEmpty(paramInJson.getString("amount"))) {
                    businessUnit.put("amount", paramInJson.getString("amount"));
                }
                businessUnit.put("startTime", time);
                businessUnit.put("endTime", time);
                businessUnit.put("communityId", reqJson.getString("communityId"));
                businessUnit.put("payerObjId", rooms.getJSONObject(roomIndex).getString("roomId"));
                businessUnit.put("payerObjType", "3333");
                businessUnit.put("feeFlag", feeConfigDtos.get(0).getFeeFlag());
                businessUnit.put("state", "2008001");
                businessUnit.put("batchId", reqJson.getString("batchId"));
                businessUnit.put("userId", "-1");
                businessUnit.put("bId", "-1");

                payFeePo = BeanConvertUtil.covertBean(businessUnit, PayFeePo.class);

                flag = payFeeV1InnerServiceSMOImpl.savePayFee(payFeePo);

                if (flag < 1) {
                    throw new CmdException("保存费用失败");
                }

                feeAttrPo = addFeeAttr(businessUnit, FeeAttrDto.SPEC_CD_OWNER_ID, reqJson.getString("ownerId"));
                flag = feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
                if (flag < 1) {
                    throw new CmdException("保存费用失败");
                }
                feeAttrPo = addFeeAttr(businessUnit, FeeAttrDto.SPEC_CD_OWNER_LINK, reqJson.getString("link"));
                flag = feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
                if (flag < 1) {
                    throw new CmdException("保存费用失败");
                }
                feeAttrPo = addFeeAttr(businessUnit, FeeAttrDto.SPEC_CD_OWNER_NAME, reqJson.getString("name"));
                flag = feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
                if (flag < 1) {
                    throw new CmdException("保存费用失败");
                }

                feeAttrPo = addFeeAttr(businessUnit, FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME,
                        roomDtos.get(roomIndex).getFloorNum() + "-" + roomDtos.get(roomIndex).getUnitNum() + "-" + roomDtos.get(roomIndex).getRoomNum());

                flag = feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
                if (flag < 1) {
                    throw new CmdException("保存费用失败");
                }

                if (!FeeDto.FEE_FLAG_CYCLE.equals(businessUnit.getString("feeFlag"))) {
                    feeAttrPo = addFeeAttr(businessUnit, FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME,
                            paramInJson.containsKey("endTime") ? paramInJson.getString("endTime") : feeConfigDtos.get(0).getEndTime());
                    flag = feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
                    if (flag < 1) {
                        throw new CmdException("保存费用失败");
                    }
                }
            }
        }

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


    public FeeAttrPo addFeeAttr(JSONObject paramInJson, String specCd, String value) {
        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(paramInJson.getString("communityId"));
        feeAttrPo.setSpecCd(specCd);
        feeAttrPo.setValue(value);
        feeAttrPo.setFeeId(paramInJson.getString("feeId"));
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        return feeAttrPo;
    }
}