package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.payFeeBatch.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bmo.fee.IFeeBMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "fee.saveParkingSpaceCreateFee")
public class SaveParkingSpaceCreateFeeCmd extends Cmd {

    @Autowired
    private IFeeBMO feeBMOImpl;

    private static final int DEFAULT_ADD_FEE_COUNT = 200;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        // super.validatePageInfo(pd);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "locationTypeCd", "未包含收费类型");
        Assert.hasKeyAndValue(reqJson, "locationObjId", "未包含收费对象");
        Assert.hasKeyAndValue(reqJson, "configId", "未包含收费项目");
        Assert.hasKeyAndValue(reqJson, "storeId", "未包含商户ID");
        //Assert.hasKeyAndValue(reqJson, "startTime", "未包含费用起始时间");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        List<OwnerCarDto> ownerCarDtos = null;
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
        //生成批次
        generatorBatch(reqJson);
        //判断收费范围
        OwnerCarDto ownerCarDto = new OwnerCarDto();

        if ("1000".equals(reqJson.getString("locationTypeCd"))) {//小区
//            ownerCarDto.setCommunityId(reqJson.getString("communityId"));
//            ownerCarDto.setValid("1");
//            ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            reqJson.put("locationObjId", "");//刷成空
            ownerCarDtos = getOwnerCarByParkingArea(reqJson);
        } else if ("2000".equals(reqJson.getString("locationTypeCd"))) {//车辆
            //ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            ownerCarDto.setCarTypeCd("1001"); //1001 业主车辆   1002 成员车辆
            ownerCarDto.setCommunityId(reqJson.getString("communityId"));
            ownerCarDto.setCarId(reqJson.getString("locationObjId"));
            ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        } else if ("3000".equals(reqJson.getString("locationTypeCd"))) {//停车场
            //ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            ownerCarDtos = getOwnerCarByParkingArea(reqJson);
        } else {
            throw new IllegalArgumentException("收费范围错误");
        }

        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            throw new IllegalArgumentException("未查到需要付费的车位");
        }

        dealParkingSpaceFee(ownerCarDtos, cmdDataFlowContext, reqJson, event);
    }


    private List<OwnerCarDto> getOwnerCarByParkingArea(JSONObject reqJson) {
        List<OwnerCarDto> ownerCarDtos = new ArrayList<>();
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
        List<String> states = new ArrayList<>();
        JSONArray carStates = reqJson.getJSONArray("carState");
        if (carStates.size() < 1) {
            throw new IllegalArgumentException("未选择车位状态");
        }

        for (int carStateIndex = 0; carStateIndex < carStates.size(); carStateIndex++) {
            states.add(carStates.getString(carStateIndex));
        }
        parkingSpaceDto.setStates(states.toArray(new String[states.size()]));
        parkingSpaceDto.setPaId(reqJson.getString("locationObjId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) {
            return null;
        }
        List<String> psIds = new ArrayList<>();
        for (int parkingSpaceIndex = 0; parkingSpaceIndex < parkingSpaceDtos.size(); parkingSpaceIndex++) {

            psIds.add(parkingSpaceDtos.get(parkingSpaceIndex).getPsId());

            if (parkingSpaceIndex % DEFAULT_ADD_FEE_COUNT == 0 && parkingSpaceIndex != 0) {

                queryOwnerCar(reqJson, psIds, ownerCarDtos);

                psIds = new ArrayList<>();
            }
        }
        if (psIds.size() > 0) {
            queryOwnerCar(reqJson, psIds, ownerCarDtos);
        }

        return ownerCarDtos;
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

    private void queryOwnerCar(JSONObject reqJson, List<String> psIds, List<OwnerCarDto> ownerCarDtos) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        ownerCarDto.setPsIds(psIds.toArray(new String[psIds.size()]));
        List<OwnerCarDto> townerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        ownerCarDtos.addAll(townerCarDtos);
    }

    private void dealParkingSpaceFee(List<OwnerCarDto> ownerCarDtos, ICmdDataFlowContext context, JSONObject reqJson, CmdEvent event) {

        List<PayFeePo> feePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrsPos = new ArrayList<>();
        JSONObject paramInObj = null;
        ResponseEntity<String> responseEntity = null;
        int failParkingSpaces = 0;
        int curFailRoomCount = 0;
        //添加单元信息
        int saveFlag = 0;
        List<ParkingSpaceDto> parkingSpaceDtos = null;
        ParkingSpaceDto parkingSpaceDto = null;
        String carName = "";
        for (int ownerCarIndex = 0; ownerCarIndex < ownerCarDtos.size(); ownerCarIndex++) {
            curFailRoomCount++;
            feePos.add(BeanConvertUtil.covertBean(feeBMOImpl.addFee(ownerCarDtos.get(ownerCarIndex), reqJson, context), PayFeePo.class));
            if (!StringUtil.isEmpty(ownerCarDtos.get(ownerCarIndex).getOwnerId())) {
                if (!FeeDto.FEE_FLAG_CYCLE.equals(reqJson.getString("feeFlag"))) {
                    feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME,
                            reqJson.containsKey("endTime") ? reqJson.getString("endTime") : reqJson.getString("configEndTime")));
                }
                feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_OWNER_ID, ownerCarDtos.get(ownerCarIndex).getOwnerId()));
                feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_OWNER_LINK, ownerCarDtos.get(ownerCarIndex).getLink()));
                feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_OWNER_NAME, ownerCarDtos.get(ownerCarIndex).getOwnerName()));
            }

            parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setCarNum(ownerCarDtos.get(ownerCarIndex).getCarNum());
            parkingSpaceDto.setCommunityId(ownerCarDtos.get(0).getCommunityId());
            parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

            if (parkingSpaceDtos != null && parkingSpaceDtos.size() > 0) {
                carName = parkingSpaceDtos.get(0).getAreaNum() + parkingSpaceDtos.get(0).getNum() + "(" + ownerCarDtos.get(ownerCarIndex).getCarNum() + ")";
            } else {
                carName = ownerCarDtos.get(ownerCarIndex).getCarNum();
            }

            //付费对象名称
            feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME,
                    carName));

            if (ownerCarIndex % DEFAULT_ADD_FEE_COUNT == 0 && ownerCarIndex != 0) {
                saveFlag = saveFeeAndAttrs(feePos, feeAttrsPos);
                feePos = new ArrayList<>();
                feeAttrsPos = new ArrayList<>();
                if (saveFlag < 1) {
                    failParkingSpaces += curFailRoomCount;
                } else {
                    curFailRoomCount = 0;
                }
            }
        }
        if (feePos != null && feePos.size() > 0) {
            saveFlag = saveFeeAndAttrs(feePos, feeAttrsPos);
            if (saveFlag < 1) {
                failParkingSpaces += curFailRoomCount;
            }
        }

        JSONObject paramOut = new JSONObject();
        paramOut.put("totalCar", ownerCarDtos.size());
        paramOut.put("successCar", ownerCarDtos.size() - failParkingSpaces);
        paramOut.put("errorCar", failParkingSpaces);

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
