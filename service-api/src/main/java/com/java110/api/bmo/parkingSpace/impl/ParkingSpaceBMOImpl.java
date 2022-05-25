package com.java110.api.bmo.parkingSpace.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.parkingSpace.IParkingSpaceBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerCarAttrInnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.ownerCarAttr.OwnerCarAttrPo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.po.room.RoomAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName ParkingSpaceBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:27
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("parkingSpaceBMOImpl")
public class ParkingSpaceBMOImpl extends ApiBaseBMO implements IParkingSpaceBMO {

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    IOwnerCarAttrInnerServiceSMO ownerCarAttrInnerServiceSMOImpl;

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void deleteParkingSpace(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessParkingSpace = new JSONObject();
        businessParkingSpace.put("psId", paramInJson.getString("psId"));
        ParkingSpacePo parkingSpacePo = BeanConvertUtil.covertBean(businessParkingSpace, ParkingSpacePo.class);
        super.delete(dataFlowContext, parkingSpacePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PARKING_SPACE);
    }

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void editParkingSpace(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(paramInJson.getString("communityId"));
        parkingSpaceDto.setPsId(paramInJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到停车位信息" + JSONObject.toJSONString(parkingSpaceDto));
        }

        parkingSpaceDto = parkingSpaceDtos.get(0);
        JSONObject businessParkingSpace = new JSONObject();

        businessParkingSpace.putAll(paramInJson);
        businessParkingSpace.put("state", parkingSpaceDto.getState());
        ParkingSpacePo parkingSpacePo = BeanConvertUtil.covertBean(businessParkingSpace, ParkingSpacePo.class);
        //parkingSpaceInnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
        super.update(dataFlowContext, parkingSpacePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PARKING_SPACE);
    }

    /**
     * 售卖房屋信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void exitParkingSpace(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        OwnerCarDto ownerCarDto = (OwnerCarDto) paramInJson.get("ownerCarDto");

        JSONObject businessOwnerCar = new JSONObject();
        //businessUnit.putAll(paramInJson);
        businessOwnerCar.put("carId", ownerCarDto.getCarId());
        //businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        OwnerCarPo ownerCarPo = BeanConvertUtil.covertBean(businessOwnerCar, OwnerCarPo.class);
        super.delete(dataFlowContext, ownerCarPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_CAR);
    }

    /**
     * 修改停车位状态信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void modifyParkingSpaceState(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(paramInJson.getString("communityId"));
        parkingSpaceDto.setPsId(paramInJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到停车位信息" + JSONObject.toJSONString(parkingSpaceDto));
        }

        parkingSpaceDto = parkingSpaceDtos.get(0);

        JSONObject businessParkingSpace = new JSONObject();

        businessParkingSpace.putAll(BeanConvertUtil.beanCovertMap(parkingSpaceDto));
        businessParkingSpace.put("state", "F");
        ParkingSpacePo parkingSpacePo = BeanConvertUtil.covertBean(businessParkingSpace, ParkingSpacePo.class);
        super.update(dataFlowContext, parkingSpacePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PARKING_SPACE);
    }

    /**
     * 删除物业费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void exitParkingSpaceFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        ParkingSpaceDto parkingSpaceDto = (ParkingSpaceDto) paramInJson.get("parkingSpaceDto");
        //校验物业费是否已经交清
        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(paramInJson.getString("communityId"));
        feeDto.setIncomeObjId(paramInJson.getString("storeId"));
        feeDto.setPayerObjId(paramInJson.getString("psId"));
        feeDto.setFeeTypeCd("1001".equals(parkingSpaceDto.getTypeCd())
                ? ("H".equals(parkingSpaceDto.getState())
                ? FeeTypeConstant.FEE_TYPE_HIRE_UP_PARKING_SPACE
                : FeeTypeConstant.FEE_TYPE_SELL_UP_PARKING_SPACE)
                : ("H".equals(parkingSpaceDto.getState())
                ? FeeTypeConstant.FEE_TYPE_HIRE_DOWN_PARKING_SPACE
                : FeeTypeConstant.FEE_TYPE_SELL_DOWN_PARKING_SPACE));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "数据存在问题，停车费对应关系不是一条");
        }


        JSONObject businessFee = new JSONObject();
        //businessUnit.putAll(paramInJson);
        businessFee.put("feeId", feeDtos.get(0).getFeeId());
        //businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        PayFeePo payFeePo = BeanConvertUtil.covertBean(businessFee, PayFeePo.class);
        super.delete(dataFlowContext, payFeePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FEE_INFO);
    }

    /**
     * 添加小区楼信息
     * <p>
     * * name:'',
     * *                 age:'',
     * *                 link:'',
     * *                 sex:'',
     * *                 remark:''
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addParkingSpace(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        JSONObject businessParkingSpace = new JSONObject();
        businessParkingSpace.putAll(paramInJson);
        businessParkingSpace.put("state", "F");
        businessParkingSpace.put("psId", GenerateCodeFactory.getPsId(GenerateCodeFactory.CODE_PREFIX_psId));
        businessParkingSpace.put("bId", "-1");
        businessParkingSpace.put("createTime", new Date());
        ParkingSpacePo parkingSpacePo = BeanConvertUtil.covertBean(businessParkingSpace, ParkingSpacePo.class);
        super.insert(dataFlowContext, parkingSpacePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PARKING_SPACE);

        //parkingSpaceInnerServiceSMOImpl.saveParkingSpace(parkingSpacePo);
    }


    /**
     * 添加小区楼信息
     * <p>
     * * name:'',
     * *                 age:'',
     * *                 link:'',
     * *                 sex:'',
     * *                 remark:''
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void sellParkingSpace(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessOwnerCar = new JSONObject();
        businessOwnerCar.putAll(paramInJson);
        businessOwnerCar.put("memberId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_carId));
        if (!paramInJson.containsKey("carId") || paramInJson.getString("carId").startsWith("-")) {
            businessOwnerCar.put("carId", businessOwnerCar.getString("memberId"));
        }
        OwnerCarPo ownerCarPo = BeanConvertUtil.covertBean(businessOwnerCar, OwnerCarPo.class);
        ownerCarPo.setState(OwnerCarDto.STATE_NORMAL);

        //没有指定时为主要车辆
        if (!paramInJson.containsKey("carTypeCd") || StringUtil.isEmpty(paramInJson.getString("carTypeCd"))) {
            ownerCarPo.setCarTypeCd(OwnerCarDto.CAR_TYPE_PRIMARY);
        }
        //添加车辆属性
        dealOwnerCarAttr(paramInJson, ownerCarPo, dataFlowContext);
        super.insert(dataFlowContext, ownerCarPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_CAR);
    }

    private void dealOwnerCarAttr(JSONObject paramInJson, OwnerCarPo ownerCarPo, DataFlowContext dataFlowContext) {

        if (!paramInJson.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = paramInJson.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }
        JSONObject attr = null;
        int flag = 0;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            OwnerCarAttrPo ownerCarAttrPo = new OwnerCarAttrPo();
            ownerCarAttrPo.setAttrId(GenerateCodeFactory.getAttrId());
            ownerCarAttrPo.setCommunityId(ownerCarPo.getCommunityId());
            ownerCarAttrPo.setCarId(ownerCarPo.getCarId());
            ownerCarAttrPo.setSpecCd(attr.getString("specCd"));
            ownerCarAttrPo.setValue(attr.getString("value"));
            flag = ownerCarAttrInnerServiceSMOImpl.saveOwnerCarAttr(ownerCarAttrPo);
            if (flag < 1) {
                throw new CmdException("保存车辆属性失败");
            }
        }

    }

    /**
     * 修改停车位状态信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void modifySellParkingSpaceState(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(paramInJson.getString("communityId"));
        parkingSpaceDto.setPsId(paramInJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            //throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到停车位信息" + JSONObject.toJSONString(parkingSpaceDto));
            return;
        }

        parkingSpaceDto = parkingSpaceDtos.get(0);

        JSONObject businessParkingSpace = new JSONObject();

        businessParkingSpace.putAll(BeanConvertUtil.beanCovertMap(parkingSpaceDto));
        businessParkingSpace.put("state", paramInJson.getString("carNumType"));
        ParkingSpacePo parkingSpacePo = BeanConvertUtil.covertBean(businessParkingSpace, ParkingSpacePo.class);
        super.update(dataFlowContext, parkingSpacePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PARKING_SPACE);
    }


    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addParkingSpaceFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", paramInJson.getString("feeId"));
        businessUnit.put("feeTypeCd", paramInJson.getString("feeTypeCd"));
        businessUnit.put("incomeObjId", paramInJson.getString("storeId"));
        businessUnit.put("amount", paramInJson.getString("amount"));
        businessUnit.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessUnit.put("endTime", paramInJson.getString("endTime"));
        businessUnit.put("communityId", paramInJson.getString("communityId"));
        businessUnit.put("payerObjId", paramInJson.getString("psId"));
        businessUnit.put("payerObjType", "6666");
        businessUnit.put("configId", paramInJson.getString("configId"));
        businessUnit.put("feeFlag", this.isHireParkingSpace(paramInJson) ? "1003006" : "2006012");
        businessUnit.put("state", this.isHireParkingSpace(paramInJson) ? "2008001" : "2009001");
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        PayFeePo payFeePo = BeanConvertUtil.covertBean(businessUnit, PayFeePo.class);
        super.insert(dataFlowContext, payFeePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
    }

    /**
     * 添加费用明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addFeeDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessFeeDetail = new JSONObject();
        businessFeeDetail.putAll(paramInJson);
        businessFeeDetail.put("detailId", "-1");
        businessFeeDetail.put("primeRate", "1.00");
        businessFeeDetail.put("cycles", paramInJson.getString("cycles"));
        businessFeeDetail.put("receivableAmount", paramInJson.getString("receivableAmount"));
        PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(businessFeeDetail, PayFeeDetailPo.class);
        super.insert(dataFlowContext, payFeeDetailPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_DETAIL);
    }

    /**
     * 校验 是否是车位出租
     *
     * @param paramObj
     * @return
     */
    private boolean isHireParkingSpace(JSONObject paramObj) {
        if ("H".equals(paramObj.getString("sellOrHire"))) {
            return true;
        }
        return false;
    }

    /**
     * 计算费用信息
     *
     * @param paramInJson 传入数据字段
     */
    public void computeFeeInfo(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        //根据停车位ID查询是地上还是地下停车位
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setPsId(paramInJson.getString("psId"));
        parkingSpaceDto.setCommunityId(paramInJson.getString("communityId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未找到或找到多条车位信息");
        }

        parkingSpaceDto = parkingSpaceDtos.get(0);
        paramInJson.put("parkingSpaceDto", parkingSpaceDto);

        // 计算feeTypeCd

//        String feeTypeCd = "1001".equals(parkingSpaceDto.getTypeCd())
//                ? (this.isHireParkingSpace(paramInJson)
//                ? FeeTypeConstant.FEE_TYPE_HIRE_UP_PARKING_SPACE
//                : FeeTypeConstant.FEE_TYPE_SELL_UP_PARKING_SPACE)
//                : (this.isHireParkingSpace(paramInJson)
//                ? FeeTypeConstant.FEE_TYPE_HIRE_DOWN_PARKING_SPACE
//                : FeeTypeConstant.FEE_TYPE_SELL_DOWN_PARKING_SPACE);
        String feeTypeCd = FeeTypeConstant.FEE_TYPE_CAR;
        paramInJson.put("feeTypeCd", feeTypeCd);

        //计算 receivableAmount


        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(paramInJson.getString("configId"));
        feeConfigDto.setCommunityId(paramInJson.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        if (feeConfigDtos == null || feeConfigDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到费用配置信息，查询多条数据");
        }

        feeConfigDto = feeConfigDtos.get(0);

        double receivableAmount = Double.parseDouble(feeConfigDto.getAdditionalAmount())
                * Double.parseDouble(paramInJson.getString("cycles"));

        paramInJson.put("receivableAmount", receivableAmount);
        paramInJson.put("configId", feeConfigDto.getConfigId());

        //计算 amount
        String amount = "-1.00";
        paramInJson.put("amount", amount);

        //计算 cycles
        String cycles = paramInJson.getString("cycles");
        paramInJson.put("cycles", cycles);

        //计算结束时间
        String endTime = "";

        Date et = DateUtil.getCurrentDate();
        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(et);
        endCalender.add(Calendar.MONTH, Integer.parseInt(paramInJson.getString("cycles")));
        endTime = DateUtil.getFormatTimeString(endCalender.getTime(), DateUtil.DATE_FORMATE_STRING_A);


        paramInJson.put("endTime", endTime);

    }


}
