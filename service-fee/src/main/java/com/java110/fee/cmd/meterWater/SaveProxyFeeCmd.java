package com.java110.fee.cmd.meterWater;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFeeBatch.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.payFeeBatch.PayFeeBatchPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "meterWater.saveProxyFee")
public class SaveProxyFeeCmd extends Cmd {

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;


    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "feeTypeCd", "请求报文中未包含费用类型");
        Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含费用项");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "consumption", "请求报文中未包含consumption");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含结束时间");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        //生成批次
        generatorBatch(reqJson);

        List<PayFeePo> payFeePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();

        PayFeePo payFeePo = BeanConvertUtil.covertBean(reqJson, PayFeePo.class);
        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeePo.setIncomeObjId(reqJson.getString("storeId"));
        //payFeePo.setAmount("-1");
        payFeePo.setStartTime(reqJson.getString("startTime"));
        payFeePo.setEndTime(reqJson.getString("startTime"));
        payFeePo.setPayerObjId(reqJson.getString("objId"));
        payFeePo.setPayerObjType(reqJson.getString("objType"));
        payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeePo.setState(FeeDto.STATE_DOING);
        payFeePo.setUserId(reqJson.getString("userId"));
        payFeePo.setBatchId(reqJson.getString("batchId"));

        payFeePos.add(payFeePo);

        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(reqJson.getString("communityId"));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_PROXY_CONSUMPTION);
        feeAttrPo.setValue(reqJson.getString("consumption"));
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPos.add(feeAttrPo);
        feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(reqJson.getString("communityId"));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
        feeAttrPo.setValue(reqJson.getString("endTime"));
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPos.add(feeAttrPo);
        String objName = "";
        String ownerId = "";
        String tel = "";
        String name = "";
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(reqJson.getString("objType"))) {
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(reqJson.getString("objId"));
            roomDto.setCommunityId(reqJson.getString("communityId"));
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
            if (roomDtos == null || roomDtos.size() != 1) {
                return;
            }
            roomDto = roomDtos.get(0);
            objName = roomDto.getFloorNum() + "-" + roomDto.getUnitNum() + "-" + roomDto.getRoomNum();

            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setRoomId(roomDto.getRoomId());
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);

            if (ownerDtos != null && ownerDtos.size() > 0) {
                ownerId = ownerDtos.get(0).getOwnerId();
                tel = ownerDtos.get(0).getLink();
                name = ownerDtos.get(0).getName();
            }
        } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(reqJson.getString("objType"))) {
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCarId(reqJson.getString("objId"));
            ownerCarDto.setCommunityId(reqJson.getString("communityId"));
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

            if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
                return;
            }
            objName = ownerCarDtos.get(0).getCarNum();
            ownerId = ownerCarDtos.get(0).getOwnerId();
            tel = ownerCarDtos.get(0).getLink();
            name = ownerCarDtos.get(0).getOwnerName();
        } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(reqJson.getString("objType"))) {


            ContractDto contractDto = new ContractDto();
            contractDto.setContractId(reqJson.getString("objId"));
            List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

            if (contractDtos == null || contractDtos.size() < 1) {
                return;
            }

            objName = contractDtos.get(0).getContractCode();
            tel = contractDtos.get(0).getbLink();
            name = contractDtos.get(0).getPartyB();
        }
        addFeeAttr(FeeAttrDto.SPEC_CD_OWNER_ID, ownerId, feeAttrPos, payFeePo);
        addFeeAttr(FeeAttrDto.SPEC_CD_OWNER_NAME, name, feeAttrPos, payFeePo);
        addFeeAttr(FeeAttrDto.SPEC_CD_OWNER_LINK, tel, feeAttrPos, payFeePo);
        addFeeAttr(FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME, objName, feeAttrPos, payFeePo);

        int saveFlag = saveFeeAndAttrs(payFeePos, feeAttrPos);

        if (saveFlag < 1) {
            throw new IllegalArgumentException("创建费用失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());

    }


    private void addFeeAttr(String specCd, String value, List<FeeAttrPo> feeAttrPos, PayFeePo payFeePo) {

        if (StringUtil.isEmpty(value)) {
            return;
        }

        FeeAttrPo curFeeAttrPo = new FeeAttrPo();
        curFeeAttrPo.setFeeId(payFeePo.getFeeId());
        curFeeAttrPo.setCommunityId(payFeePo.getCommunityId());
        curFeeAttrPo.setValue(value);
        curFeeAttrPo.setSpecCd(specCd);
        curFeeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPos.add(curFeeAttrPo);

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

    private int saveFeeAndAttrs(List<PayFeePo> feePos, List<FeeAttrPo> feeAttrsPos) {
        int flag = feeInnerServiceSMOImpl.saveFee(feePos);
        if (flag < 1) {
            return flag;
        }

        flag = feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrsPos);

        return flag;
    }
}
