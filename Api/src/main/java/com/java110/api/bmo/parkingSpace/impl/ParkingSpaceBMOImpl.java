package com.java110.api.bmo.parkingSpace.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.parkingArea.IParkingAreaBMO;
import com.java110.api.bmo.parkingSpace.IParkingSpaceBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.fee.IFeeConfigInnerServiceSMO;
import com.java110.core.smo.fee.IFeeInnerServiceSMO;
import com.java110.core.smo.parkingSpace.IParkingSpaceInnerServiceSMO;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
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
    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject deleteParkingSpace(JSONObject paramInJson) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PARKING_SPACE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessParkingSpace = new JSONObject();
        businessParkingSpace.put("psId", paramInJson.getString("psId"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessParkingSpace", businessParkingSpace);

        return business;
    }

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject editParkingSpace(JSONObject paramInJson) {

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(paramInJson.getString("communityId"));
        parkingSpaceDto.setPsId(paramInJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到停车位信息" + JSONObject.toJSONString(parkingSpaceDto));
        }

        parkingSpaceDto = parkingSpaceDtos.get(0);

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PARKING_SPACE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessParkingSpace = new JSONObject();

        businessParkingSpace.putAll(paramInJson);
        businessParkingSpace.put("state", parkingSpaceDto.getState());
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessParkingSpace", businessParkingSpace);

        return business;
    }

    /**
     * 售卖房屋信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject exitParkingSpace(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        OwnerCarDto ownerCarDto = (OwnerCarDto) paramInJson.get("ownerCarDto");

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_CAR);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessOwnerCar = new JSONObject();
        //businessUnit.putAll(paramInJson);
        businessOwnerCar.put("carId", ownerCarDto.getCarId());
        //businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessOwnerCar", businessOwnerCar);

        return business;
    }

    /**
     * 修改停车位状态信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject modifyParkingSpaceState(JSONObject paramInJson) {

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(paramInJson.getString("communityId"));
        parkingSpaceDto.setPsId(paramInJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到停车位信息" + JSONObject.toJSONString(parkingSpaceDto));
        }

        parkingSpaceDto = parkingSpaceDtos.get(0);


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PARKING_SPACE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessParkingSpace = new JSONObject();

        businessParkingSpace.putAll(BeanConvertUtil.beanCovertMap(parkingSpaceDto));
        businessParkingSpace.put("state", "F");
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessParkingSpace", businessParkingSpace);

        paramInJson.put("parkingSpaceDto", parkingSpaceDto);

        return business;
    }

    /**
     * 删除物业费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject exitParkingSpaceFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {


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
                ?FeeTypeConstant.FEE_TYPE_HIRE_DOWN_PARKING_SPACE
                :FeeTypeConstant.FEE_TYPE_SELL_DOWN_PARKING_SPACE));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "数据存在问题，停车费对应关系不是一条");
        }


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FEE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFee = new JSONObject();
        //businessUnit.putAll(paramInJson);
        businessFee.put("feeId", feeDtos.get(0).getFeeId());
        //businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFee", businessFee);

        return business;
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
    public JSONObject addParkingSpace(JSONObject paramInJson) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PARKING_SPACE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessParkingSpace = new JSONObject();
        businessParkingSpace.putAll(paramInJson);
        businessParkingSpace.put("state", "F");
        businessParkingSpace.put("psId", "-1");
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessParkingSpace", businessParkingSpace);

        return business;
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
    public JSONObject sellParkingSpace(JSONObject paramInJson) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_CAR);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessOwnerCar = new JSONObject();
        businessOwnerCar.putAll(paramInJson);
        businessOwnerCar.put("carId", "-1");
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessOwnerCar", businessOwnerCar);

        return business;
    }

    /**
     * 修改停车位状态信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject modifySellParkingSpaceState(JSONObject paramInJson) {

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(paramInJson.getString("communityId"));
        parkingSpaceDto.setPsId(paramInJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到停车位信息" + JSONObject.toJSONString(parkingSpaceDto));
        }

        parkingSpaceDto = parkingSpaceDtos.get(0);

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PARKING_SPACE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessParkingSpace = new JSONObject();

        businessParkingSpace.putAll(BeanConvertUtil.beanCovertMap(parkingSpaceDto));
        businessParkingSpace.put("state", this.isHireParkingSpace(paramInJson) ? "H" : "S");
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessParkingSpace", businessParkingSpace);

        return business;
    }


    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addParkingSpaceFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
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
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFee", businessUnit);

        return business;
    }

    /**
     * 添加费用明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeeDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_DETAIL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 3);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFeeDetail = new JSONObject();
        businessFeeDetail.putAll(paramInJson);
        businessFeeDetail.put("detailId", "-1");
        businessFeeDetail.put("primeRate", "1.00");
        businessFeeDetail.put("cycles", paramInJson.getString("cycles"));
        businessFeeDetail.put("receivableAmount", paramInJson.getString("receivableAmount"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFeeDetail", businessFeeDetail);

        return business;
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
    public void computeFeeInfo(JSONObject paramInJson) {

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

        String feeTypeCd = "1001".equals(parkingSpaceDto.getTypeCd())
                ? (this.isHireParkingSpace(paramInJson)
                ? FeeTypeConstant.FEE_TYPE_HIRE_UP_PARKING_SPACE
                : FeeTypeConstant.FEE_TYPE_SELL_UP_PARKING_SPACE)
                : (this.isHireParkingSpace(paramInJson)
                ? FeeTypeConstant.FEE_TYPE_HIRE_DOWN_PARKING_SPACE
                : FeeTypeConstant.FEE_TYPE_SELL_DOWN_PARKING_SPACE);

        paramInJson.put("feeTypeCd", feeTypeCd);

        //计算 receivableAmount


        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeTypeCd(feeTypeCd);
        feeConfigDto.setIsDefault("T");
        feeConfigDto.setCommunityId(paramInJson.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        if (feeConfigDtos == null || feeConfigDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到费用配置信息，查询多条数据");
        }

        feeConfigDto = feeConfigDtos.get(0);

        double receivableAmount = isHireParkingSpace(paramInJson) ? Double.parseDouble(feeConfigDto.getAdditionalAmount())
                * Double.parseDouble(paramInJson.getString("cycles")) : Double.parseDouble(feeConfigDto.getAdditionalAmount());

        paramInJson.put("receivableAmount", receivableAmount);
        paramInJson.put("configId",feeConfigDto.getConfigId());

        //计算 amount
        String amount = isHireParkingSpace(paramInJson) ? "-1.00" : String.valueOf(receivableAmount);
        paramInJson.put("amount", amount);

        //计算 cycles
        String cycles = isHireParkingSpace(paramInJson) ? paramInJson.getString("cycles") : "1";
        paramInJson.put("cycles", cycles);

        //计算结束时间
        String endTime = "2038-01-01 00:00:00";
        if (isHireParkingSpace(paramInJson)) {
            Date et = DateUtil.getCurrentDate();
            Calendar endCalender = Calendar.getInstance();
            endCalender.setTime(et);
            endCalender.add(Calendar.MONTH, Integer.parseInt(paramInJson.getString("cycles")));
            endTime = DateUtil.getFormatTimeString(endCalender.getTime(), DateUtil.DATE_FORMATE_STRING_A);
        }

        paramInJson.put("endTime", endTime);

    }


}
