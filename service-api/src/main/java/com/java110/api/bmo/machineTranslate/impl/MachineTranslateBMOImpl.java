package com.java110.api.bmo.machineTranslate.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.machineTranslate.IMachineTranslateBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.machine.ApplicationKeyDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.IApplicationKeyInnerServiceSMO;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.car.CarInoutDetailPo;
import com.java110.po.car.CarInoutPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.file.FileRelPo;
import com.java110.po.machine.MachineRecordPo;
import com.java110.po.machine.MachineTranslatePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommunityMemberTypeConstant;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName MachineTranslateBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 22:53
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("machineTranslateBMOImpl")
public class MachineTranslateBMOImpl extends ApiBaseBMO implements IMachineTranslateBMO {
    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;
    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteMachineTranslate(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        MachineTranslatePo machineTranslatePo = BeanConvertUtil.covertBean(paramInJson, MachineTranslatePo.class);
        super.delete(dataFlowContext, machineTranslatePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_MACHINE_TRANSLATE);

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addCarInoutDetail(JSONObject paramInJson, DataFlowContext dataFlowContext, String communityId, MachineDto machineDto) {

        JSONObject businessCarInoutDetail = new JSONObject();
        businessCarInoutDetail.put("carNum", paramInJson.getString("carNum"));
        businessCarInoutDetail.put("inoutId", paramInJson.getString("inoutId"));
        businessCarInoutDetail.put("communityId", communityId);
        businessCarInoutDetail.put("machineId", machineDto.getMachineId());
        businessCarInoutDetail.put("machineCode", machineDto.getMachineCode());
        businessCarInoutDetail.put("carInout", machineDto.getDirection());
        businessCarInoutDetail.put("detailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        paramInJson.put("detailId",businessCarInoutDetail.getString("detailId"));
        CarInoutDetailPo carInoutDetailPo = BeanConvertUtil.covertBean(businessCarInoutDetail, CarInoutDetailPo.class);
        super.insert(dataFlowContext, carInoutDetailPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_CAR_INOUT);

    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addCarInout(JSONObject paramInJson, DataFlowContext dataFlowContext, String communityId) {

        if (!paramInJson.containsKey("inoutId") || "-1".equals(paramInJson.getString("inoutId"))) {
            paramInJson.put("inoutId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_inoutId));
        }
        JSONObject businessCarInout = new JSONObject();
        businessCarInout.put("carNum", paramInJson.getString("carNum"));
        businessCarInout.put("inoutId", paramInJson.getString("inoutId"));
        businessCarInout.put("communityId", communityId);
        businessCarInout.put("state", "100300");
        businessCarInout.put("inTime", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
        CarInoutPo carInoutPo = BeanConvertUtil.covertBean(businessCarInout, CarInoutPo.class);
        super.insert(dataFlowContext, carInoutPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_CAR_INOUT);
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addCarInoutFee(JSONObject paramInJson, DataFlowContext dataFlowContext, String communityId) {
        addCarInoutFee(paramInJson, dataFlowContext, communityId, DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
    }


    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addCarInoutFee(JSONObject paramInJson, DataFlowContext dataFlowContext, String communityId, String startTime) {
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(communityId);
        communityMemberDto.setMemberTypeCd(CommunityMemberTypeConstant.PROPERTY);
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);
        String storeId = "-1";
        if (communityMemberDtos != null && communityMemberDtos.size() > 0) {
            storeId = communityMemberDtos.get(0).getMemberId();
        }

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_TEMP_DOWN_PARKING_SPACE);
        feeConfigDto.setIsDefault("T");
        feeConfigDto.setCommunityId(communityId);
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        if (feeConfigDtos == null || feeConfigDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到费用配置信息，查询多条数据");
        }

        feeConfigDto = feeConfigDtos.get(0);

        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", "-1");
        businessUnit.put("configId", feeConfigDto.getConfigId());
        businessUnit.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_TEMP_DOWN_PARKING_SPACE);
        businessUnit.put("incomeObjId", storeId);
        businessUnit.put("amount", "-1.00");
        businessUnit.put("startTime", startTime);
        businessUnit.put("endTime", DateUtil.getLastTime()); // 临时车将结束时间刷成2038年
        businessUnit.put("communityId", communityId);
        businessUnit.put("payerObjId", paramInJson.getString("inoutId"));
        businessUnit.put("payerObjType", "9999");
        businessUnit.put("feeFlag", "2006012"); // 一次性费用
        businessUnit.put("state", "2008001"); // 收费中
        businessUnit.put("userId", "-1");
        PayFeePo payFeePo = BeanConvertUtil.covertBean(businessUnit, PayFeePo.class);
        super.insert(dataFlowContext, payFeePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
    }

    public void modifyCarInout(JSONObject reqJson, DataFlowContext context, CarInoutDto carInoutDto) {
        modifyCarInout(reqJson, context, carInoutDto, "100500", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
    }

    public void modifyCarInout(JSONObject reqJson, DataFlowContext context, CarInoutDto carInoutDto, String state, String endTime) {

        JSONObject businessCarInout = new JSONObject();
        businessCarInout.putAll(BeanConvertUtil.beanCovertMap(carInoutDto));
        businessCarInout.put("state", state);
        businessCarInout.put("outTime", endTime);
        CarInoutPo carInoutPo = BeanConvertUtil.covertBean(businessCarInout, CarInoutPo.class);
        super.update(context, carInoutPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_CAR_INOUT);
    }

    /**
     * 保存照片
     *
     * @param reqJson
     * @param context
     */
    public void savePhoto(JSONObject reqJson, DataFlowContext context) {
        FileDto fileDto = new FileDto();
        fileDto.setCommunityId(reqJson.getString("communityId"));
        fileDto.setFileId(reqJson.getString("fileId"));
        fileDto.setFileName(reqJson.getString("fileId"));
        fileDto.setContext(reqJson.getString("photo"));
        fileDto.setSuffix("jpeg");
        String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", "-1");
        businessUnit.put("relTypeCd", reqJson.getString("relTypeCd"));
        businessUnit.put("saveWay", "table");
        businessUnit.put("objId", reqJson.getString("machineRecordId"));
        businessUnit.put("fileRealName", reqJson.getString("fileId"));
        businessUnit.put("fileSaveName", fileName);
        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
        super.insert(context, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addMachineRecord(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        if (!paramInJson.containsKey("openTypeCd")) {
            paramInJson.put("openTypeCd", "1000");
        }

        if (!paramInJson.containsKey("recordTypeCd")) {
            paramInJson.put("openTypeCd", "8888");
        }
        paramInJson.put("fileTime", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));

        paramInJson.put("machineRecordId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineRecordId));

        String objId = paramInJson.getString("userId");
        //这里objId 可能是 业主ID 也可能是钥匙ID
        //先根据业主ID去查询
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(paramInJson.getString("communityId"));
        ownerDto.setMemberId(objId);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        if (ownerDtos != null && ownerDtos.size() > 0) {
            Assert.listOnlyOne(ownerDtos, "根据业主ID查询到多条记录");
            paramInJson.put("name", ownerDtos.get(0).getName());
            paramInJson.put("tel", ownerDtos.get(0).getLink());
            paramInJson.put("idCard", ownerDtos.get(0).getIdCard());
            paramInJson.put("relTypeCd", "60000");
        } else { //钥匙申请ID
            ApplicationKeyDto applicationKeyDto = new ApplicationKeyDto();
            applicationKeyDto.setCommunityId(paramInJson.getString("communityId"));
            applicationKeyDto.setApplicationKeyId(objId);
            List<ApplicationKeyDto> applicationKeyDtos = applicationKeyInnerServiceSMOImpl.queryApplicationKeys(applicationKeyDto);

            Assert.listOnlyOne(applicationKeyDtos, "根据钥匙ID未查询到记录或查询到多条记录");

            paramInJson.put("name", applicationKeyDtos.get(0).getName());
            paramInJson.put("tel", applicationKeyDtos.get(0).getTel());
            paramInJson.put("idCard", applicationKeyDtos.get(0).getIdCard());
            paramInJson.put("relTypeCd", "60000");

        }

        //计算 应收金额
        MachineRecordPo machineRecordPo = BeanConvertUtil.covertBean(paramInJson, MachineRecordPo.class);
        super.insert(dataFlowContext, machineRecordPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_MACHINE_RECORD);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addMachineTranslate(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessMachineTranslate = new JSONObject();
        businessMachineTranslate.putAll(paramInJson);
        businessMachineTranslate.put("machineTranslateId", "-1");
        //计算 应收金额

        MachineTranslatePo machineTranslatePo = BeanConvertUtil.covertBean(businessMachineTranslate, MachineTranslatePo.class);
        super.insert(dataFlowContext, machineTranslatePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_MACHINE_TRANSLATE);

    }

    /**
     * 添加设备同步信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateMachineTranslate(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        MachineTranslatePo machineTranslatePo = BeanConvertUtil.covertBean(paramInJson, MachineTranslatePo.class);
        super.update(dataFlowContext, machineTranslatePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_MACHINE_TRANSLATE);
    }
}
