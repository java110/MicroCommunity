package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFeeBatch.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
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
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "fee.createFeeByCombo")
public class CreateFeeByComboCmd extends Cmd {

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;


    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    /**
     * communityId: vc.getCurrentCommunity().communityId,
     * configs: _fees,
     * payerObjId: $that.createFeeByComboInfo.payerObjId,
     * payerObjName: $that.createFeeByComboInfo.payerObjName,
     * payerObjType: $that.createFeeByComboInfo.payerObjType,
     * comboId: $that.createFeeByComboInfo.comboId
     *
     * @param event              事件对象
     * @param cmdDataFlowContext 请求报文数据
     * @param reqJson
     * @throws CmdException
     */
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "payerObjId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "payerObjName", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "payerObjType", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "comboId", "请求报文中未包含communityId节点");

        if (!reqJson.containsKey("configs")) {
            throw new IllegalArgumentException("不包含费用信息");
        }

        JSONArray configs = reqJson.getJSONArray("configs");

        if (configs == null || configs.size() < 1) {
            throw new IllegalArgumentException("不包含费用信息");
        }

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        //生成批次号
        generatorBatch(reqJson);

        JSONArray configs = reqJson.getJSONArray("configs");

        for (int configIndex = 0; configIndex < configs.size(); configIndex++) {
            doCreateConfigFee(reqJson, configs.getJSONObject(configIndex));
        }

    }

    private void doCreateConfigFee(JSONObject reqJson, JSONObject config) {

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setConfigId(config.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "当前费用项ID不存在或存在多条" + reqJson.getString("configId"));

        if (!FeeDto.FEE_FLAG_CYCLE.equals(feeConfigDtos.get(0).getFeeFlag()) && config.containsKey("endTime")) {
            Date endTime = null;
            Date configEndTime = null;
            try {
                endTime = DateUtil.getDateFromString(config.getString("endTime"), DateUtil.DATE_FORMATE_STRING_B);
                configEndTime = DateUtil.getDateFromString(feeConfigDtos.get(0).getEndTime(), DateUtil.DATE_FORMATE_STRING_A);
                if (endTime.getTime() > configEndTime.getTime()) {
                    throw new IllegalArgumentException("结束时间不能超过费用项时间");
                }
            } catch (ParseException e) {
                throw new IllegalArgumentException("结束时间错误" + reqJson.getString("endTime"));
            }
        }

        List<PayFeePo> feePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrsPos = new ArrayList<>();

        feePos.add(BeanConvertUtil.covertBean(addRoomFee(config, reqJson,feeConfigDtos), PayFeePo.class));

        if (!FeeDto.FEE_FLAG_CYCLE.equals(feeConfigDtos.get(0).getFeeFlag())) {
            feeAttrsPos.add(addFeeAttr(reqJson, FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME,
                    config.containsKey("endTime") ? config.getString("endTime") : reqJson.getString("configEndTime")));
        }


        saveOwnerFeeAttr(reqJson, feeAttrsPos);


        feeAttrsPos.add(addFeeAttr(reqJson, FeeAttrDto.SPEC_CD_COMBO_ID,
                reqJson.getString("comboId")));


        int saveFlag = saveFeeAndAttrs(feePos, feeAttrsPos);

        if (saveFlag < 1) {
            throw new CmdException("创建费用失败");
        }

    }

    private void saveOwnerFeeAttr(JSONObject reqJson, List<FeeAttrPo> feeAttrsPos) {

        String objName = "";
        String ownerId = "";
        String tel = "";
        String name = "";

        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(reqJson.getString("payerObjType"))) {
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(reqJson.getString("payerObjId"));
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
        } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(reqJson.getString("payerObjType"))) {
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCarId(reqJson.getString("payerObjId"));
            ownerCarDto.setCommunityId(reqJson.getString("communityId"));
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

            if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
                return;
            }
            objName = ownerCarDtos.get(0).getCarNum();
            ownerId = ownerCarDtos.get(0).getOwnerId();
            tel = ownerCarDtos.get(0).getLink();
            name = ownerCarDtos.get(0).getOwnerName();
        } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(reqJson.getString("payerObjType"))) {


            ContractDto contractDto = new ContractDto();
            contractDto.setContractId(reqJson.getString("payerObjId"));
            List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

            if (contractDtos == null || contractDtos.size() < 1) {
                return;
            }

            objName = contractDtos.get(0).getContractCode();
            tel = contractDtos.get(0).getbLink();
            name = contractDtos.get(0).getPartyB();
        }
        if (!StringUtil.isEmpty(ownerId)) {
            feeAttrsPos.add(addFeeAttr(reqJson, FeeAttrDto.SPEC_CD_OWNER_ID, ownerId));
        }
        if (!StringUtil.isEmpty(tel)) {
            feeAttrsPos.add(addFeeAttr(reqJson, FeeAttrDto.SPEC_CD_OWNER_LINK, tel));
        }
        if (!StringUtil.isEmpty(name)) {
            feeAttrsPos.add(addFeeAttr(reqJson, FeeAttrDto.SPEC_CD_OWNER_NAME, name));
        }
        //付费对象名称
        if (!StringUtil.isEmpty(objName)) {
            feeAttrsPos.add(addFeeAttr(reqJson, FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME,
                    objName));
        }
    }


    private int saveFeeAndAttrs(List<PayFeePo> feePos, List<FeeAttrPo> feeAttrsPos) {
        int flag = feeInnerServiceSMOImpl.saveFee(feePos);
        if (flag < 1) {
            return flag;
        }

        flag = feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrsPos);

        return flag;
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject addRoomFee(JSONObject config, JSONObject paramInJson,List<FeeConfigDto> feeConfigDtos) {
        //获取费用标识
        //查询费用项]
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        businessUnit.put("configId", feeConfigDtos.get(0).getConfigId());
        businessUnit.put("feeTypeCd", feeConfigDtos.get(0).getFeeTypeCd());
        businessUnit.put("incomeObjId", paramInJson.getString("storeId"));
        businessUnit.put("amount", "-1.00");
        if (config.containsKey("amount") && !StringUtil.isEmpty(config.getString("amount"))) {
            businessUnit.put("amount", config.getString("amount"));
        }
        businessUnit.put("startTime", config.getString("startTime"));
        businessUnit.put("endTime", config.getString("startTime"));
        businessUnit.put("communityId", paramInJson.getString("communityId"));
        businessUnit.put("payerObjId", paramInJson.getString("payerObjId"));
        businessUnit.put("payerObjType", paramInJson.getString("payerObjType"));
        businessUnit.put("feeFlag", feeConfigDtos.get(0).getFeeFlag());
        businessUnit.put("state", "2008001");
        businessUnit.put("batchId", paramInJson.getString("batchId"));
        businessUnit.put("userId", paramInJson.getString("userId"));

        paramInJson.put("feeId", businessUnit.getString("feeId"));
        return businessUnit;
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
