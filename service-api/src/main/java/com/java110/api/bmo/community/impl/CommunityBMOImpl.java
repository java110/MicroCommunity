package com.java110.api.bmo.community.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.community.ICommunityBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.intf.common.IWorkflowInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.po.community.CommunityAttrPo;
import com.java110.po.community.CommunityMemberPo;
import com.java110.po.community.CommunityPo;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.po.workflow.WorkflowPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.*;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CommunityBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 21:22
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("communityBMOImpl")
public class CommunityBMOImpl extends ApiBaseBMO implements ICommunityBMO {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateCommunity(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(paramInJson.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listOnlyOne(communityDtos, "未查询到该小区信息【" + communityDto.getCommunityId() + "】");
        communityDto = communityDtos.get(0);

        Map oldCommunityInfo = BeanConvertUtil.beanCovertMap(communityDto);
        oldCommunityInfo.put("state", paramInJson.getString("state"));
        CommunityPo communityPo = BeanConvertUtil.covertBean(oldCommunityInfo, CommunityPo.class);

        super.update(dataFlowContext, communityPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_COMMUNITY_INFO);

    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateCommunityMember(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityMemberId(paramInJson.getString("communityMemberId"));
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);
        Assert.listOnlyOne(communityMemberDtos, "未查询到该小区成员信息【" + communityMemberDto.getCommunityMemberId() + "】");
        communityMemberDto = communityMemberDtos.get(0);

        Map oldCommunityInfo = BeanConvertUtil.beanCovertMap(communityMemberDto);
        oldCommunityInfo.put("auditStatusCd", paramInJson.getString("state"));
        CommunityMemberPo communityMemberPo = BeanConvertUtil.covertBean(oldCommunityInfo, CommunityMemberPo.class);
        super.update(dataFlowContext, communityMemberPo, BusinessTypeConstant.BUSINESS_TYPE_AUDIT_COMMUNITY_MEMBER_STATE);

    }

    /**
     * 添加小区成员
     *
     * @param paramInJson 接口请求数据封装
     * @return 封装好的 data数据
     */
    public JSONObject addCommunityMember(JSONObject paramInJson) {

        //查询小区是否存在
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(paramInJson.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos, "小区不存在");

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", paramInJson.getString("memberId"));
        businessCommunityMember.put("memberTypeCd", paramInJson.getString("memberTypeCd"));
        businessCommunityMember.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.MONTH, Integer.parseInt(communityDtos.get(0).getPayFeeMonth()));
        businessCommunityMember.put("endTime", DateUtil.getFormatTimeString(endTime.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        String auditStatusCd = MappingCache.getValue(MappingConstant.DOMAIN_COMMUNITY_MEMBER_AUDIT, paramInJson.getString("memberTypeCd"));
        auditStatusCd = StringUtils.isEmpty(auditStatusCd) ? StateConstant.AGREE_AUDIT : auditStatusCd;
        businessCommunityMember.put("auditStatusCd", auditStatusCd);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(CommunityMemberPo.class.getSimpleName(), businessCommunityMember);

        return business;
    }

    /**
     * 添加小区成员
     *
     * @param paramInJson 接口请求数据封装
     * @return 封装好的 data数据
     */
    public JSONObject updateWorkflow(JSONObject paramInJson,String flowType) {
        WorkflowDto workflowDto = new WorkflowDto();
        workflowDto.setCommunityId(paramInJson.getString("communityId"));
        workflowDto.setFlowType(flowType);
        List<WorkflowDto> workflowDtos = workflowInnerServiceSMOImpl.queryWorkflows(workflowDto);

        if (workflowDtos == null || workflowDtos.size() < 1) {
            return null;
        }
        return getBusiness(paramInJson, workflowDtos);
    }

    /**
     * 修改物品领用
     *
     * @param paramInJson
     * @return
     */
    public JSONObject updateComplaint2(JSONObject paramInJson) {
        WorkflowDto workflowDto = new WorkflowDto();
        workflowDto.setCommunityId(paramInJson.getString("communityId"));
        workflowDto.setFlowType(WorkflowDto.FLOW_TYPE_COLLECTION);
        List<WorkflowDto> workflowDtos = workflowInnerServiceSMOImpl.queryWorkflows(workflowDto);

        if (workflowDtos == null || workflowDtos.size() < 1) {
            return null;
        }
        return getBusiness(paramInJson, workflowDtos);
    }

    /**
     * 修改物品被调拨
     *
     * @param paramInJson
     * @return
     */
    public JSONObject updateComplaint3(JSONObject paramInJson) {
        WorkflowDto workflowDto = new WorkflowDto();
        workflowDto.setCommunityId(paramInJson.getString("communityId"));
        workflowDto.setFlowType(WorkflowDto.FLOW_TYPE_ALLOCATION_STOREHOUSE_GO);
        List<WorkflowDto> workflowDtos = workflowInnerServiceSMOImpl.queryWorkflows(workflowDto);

        if (workflowDtos == null || workflowDtos.size() < 1) {
            return null;
        }
        return getBusiness(paramInJson, workflowDtos);
    }

    public JSONObject getBusiness(JSONObject paramInJson, List<WorkflowDto> workflowDtos) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_WORKFLOW);
        business.put(CommonConstant.HTTP_SEQ, 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunityMember = new JSONObject();
        businessCommunityMember.put("flowId", workflowDtos.get(0).getFlowId());
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("storeId", paramInJson.getString("memberId"));
        JSONArray data = new JSONArray();
        data.add(businessCommunityMember);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(WorkflowPo.class.getSimpleName(), data);
        return business;
    }


    /**
     * 添加小区成员
     *
     * @param paramInJson
     * @return
     */
    public JSONObject deleteCommunityMember(JSONObject paramInJson) {

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_QUIT_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", paramInJson.getString("communityMemberId"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(CommunityMemberPo.class.getSimpleName(), businessCommunityMember);

        return business;
    }

    /**
     * 退出小区成员
     *
     * @param paramInJson 接口传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONArray exitCommunityMember(JSONObject paramInJson) {

        JSONArray businesses = new JSONArray();

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_QUIT_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunityMember = new JSONObject();
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setMemberTypeCd(CommunityMemberTypeConstant.AGENT);
        communityMemberDto.setCommunityId(paramInJson.getString("communityId"));
        communityMemberDto.setStatusCd(StatusConstant.STATUS_CD_VALID);
        List<CommunityMemberDto> communityMemberDtoList = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

        if (communityMemberDtoList == null || communityMemberDtoList.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "小区和代理商存在关系存在异常，请检查");
        }


        businessCommunityMember.put("communityMemberId", communityMemberDtoList.get(0).getCommunityMemberId());
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(CommunityMemberPo.class.getSimpleName(), businessCommunityMember);

        businesses.add(business);

        //开发者
        business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_QUIT_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        businessCommunityMember = new JSONObject();
        communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setMemberTypeCd(CommunityMemberTypeConstant.DEV);
        communityMemberDto.setCommunityId(paramInJson.getString("communityId"));
        communityMemberDto.setStatusCd(StatusConstant.STATUS_CD_VALID);
        communityMemberDtoList = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

        if (communityMemberDtoList == null || communityMemberDtoList.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "小区和开发者存在关系存在异常，请检查");
        }


        businessCommunityMember.put("communityMemberId", communityMemberDtoList.get(0).getCommunityMemberId());
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(CommunityMemberPo.class.getSimpleName(), businessCommunityMember);

        businesses.add(business);
        //运维团队
        business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_QUIT_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 3);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        businessCommunityMember = new JSONObject();
        communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setMemberTypeCd(CommunityMemberTypeConstant.OPT);
        communityMemberDto.setCommunityId(paramInJson.getString("communityId"));
        communityMemberDto.setStatusCd(StatusConstant.STATUS_CD_VALID);
        communityMemberDtoList = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

        if (communityMemberDtoList == null || communityMemberDtoList.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "小区和运维团队存在关系存在异常，请检查");
        }


        businessCommunityMember.put("communityMemberId", communityMemberDtoList.get(0).getCommunityMemberId());
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(CommunityMemberPo.class.getSimpleName(), businessCommunityMember);

        businesses.add(business);

        return businesses;
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteCommunity(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        CommunityPo communityPo = BeanConvertUtil.covertBean(paramInJson, CommunityPo.class);
        super.delete(dataFlowContext, communityPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_COMMUNITY_INFO);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addFeeConfigProperty(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_PROPERTY);
        businessFeeConfig.put("feeName", "物业费[系统默认]");
        businessFeeConfig.put("feeFlag", "1003006");
        businessFeeConfig.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessFeeConfig.put("endTime", DateUtil.LAST_TIME);
        businessFeeConfig.put("computingFormula", "1001");
        businessFeeConfig.put("squarePrice", "0.00");
        businessFeeConfig.put("paymentCd", "1200");
        businessFeeConfig.put("additionalAmount", "0.00");
        businessFeeConfig.put("communityId", paramInJson.getString("communityId"));
        businessFeeConfig.put("configId", paramInJson.getString("configId"));
        businessFeeConfig.put("billType", "002");//按月出账
        businessFeeConfig.put("paymentCycle", "1");//按月出账
        businessFeeConfig.put("isDefault", "T");
        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(businessFeeConfig, PayFeeConfigPo.class);
        super.insert(dataFlowContext, payFeeConfigPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addFeeConfigRepair(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_REPAIR);
        businessFeeConfig.put("feeName", "报修费[系统默认]");
        businessFeeConfig.put("deductFrom", "Y");
        businessFeeConfig.put("feeFlag", "2006012"); //一次性费用
        businessFeeConfig.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessFeeConfig.put("endTime", DateUtil.LAST_TIME);
        businessFeeConfig.put("computingFormula", "4004");
        businessFeeConfig.put("squarePrice", "0.00");
        businessFeeConfig.put("additionalAmount", "0.00");
        businessFeeConfig.put("communityId", paramInJson.getString("communityId"));
        businessFeeConfig.put("configId", paramInJson.getString("configId"));
        businessFeeConfig.put("billType", "003");//按天出账
        businessFeeConfig.put("paymentCd", "2100");
        businessFeeConfig.put("paymentCycle", "1");//按月出账
        businessFeeConfig.put("isDefault", "T");
        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(businessFeeConfig, PayFeeConfigPo.class);

        super.insert(dataFlowContext, payFeeConfigPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addFeeConfigParkingSpaceUpSell(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_CAR);
        businessFeeConfig.put("feeName", "地上出售车位费[系统默认]");
        businessFeeConfig.put("feeFlag", "2006012");
        businessFeeConfig.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessFeeConfig.put("endTime", DateUtil.LAST_TIME);
        businessFeeConfig.put("computingFormula", "2002");
        businessFeeConfig.put("squarePrice", "0.00");
        businessFeeConfig.put("additionalAmount", "0.00");
        businessFeeConfig.put("communityId", paramInJson.getString("communityId"));
        businessFeeConfig.put("configId", paramInJson.getString("configId"));
        businessFeeConfig.put("billType", "003");//按天出账
        businessFeeConfig.put("paymentCd", "1200");
        businessFeeConfig.put("paymentCycle", "1");//按月出账
        businessFeeConfig.put("isDefault", "T");
        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(businessFeeConfig, PayFeeConfigPo.class);

        super.insert(dataFlowContext, payFeeConfigPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addFeeConfigParkingSpaceDownSell(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_CAR);
        businessFeeConfig.put("feeName", "地下出售车位费[系统默认]");
        businessFeeConfig.put("feeFlag", "2006012");
        businessFeeConfig.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessFeeConfig.put("endTime", DateUtil.LAST_TIME);
        businessFeeConfig.put("computingFormula", "2002");
        businessFeeConfig.put("squarePrice", "0.00");
        businessFeeConfig.put("additionalAmount", "0.00");
        businessFeeConfig.put("communityId", paramInJson.getString("communityId"));
        businessFeeConfig.put("configId", paramInJson.getString("configId"));
        businessFeeConfig.put("billType", "003");//按天出账
        businessFeeConfig.put("paymentCd", "1200");
        businessFeeConfig.put("paymentCycle", "1");//按月出账
        businessFeeConfig.put("isDefault", "T");
        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(businessFeeConfig, PayFeeConfigPo.class);

        super.insert(dataFlowContext, payFeeConfigPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addFeeConfigParkingSpaceUpHire(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_CAR);
        businessFeeConfig.put("feeName", "地上出租车位费[系统默认]");
        businessFeeConfig.put("feeFlag", "1003006");
        businessFeeConfig.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessFeeConfig.put("endTime", DateUtil.LAST_TIME);
        businessFeeConfig.put("computingFormula", "2002");
        businessFeeConfig.put("squarePrice", "0.00");
        businessFeeConfig.put("additionalAmount", "0.00");
        businessFeeConfig.put("communityId", paramInJson.getString("communityId"));
        businessFeeConfig.put("configId", paramInJson.getString("configId"));
        businessFeeConfig.put("billType", "003");//按天出账
        businessFeeConfig.put("paymentCd", "1200");
        businessFeeConfig.put("paymentCycle", "1");//按月出账
        businessFeeConfig.put("isDefault", "T");
        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(businessFeeConfig, PayFeeConfigPo.class);

        super.insert(dataFlowContext, payFeeConfigPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addFeeConfigParkingSpaceDownHire(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_CAR);
        businessFeeConfig.put("feeName", "地下出租车位费[系统默认]");
        businessFeeConfig.put("feeFlag", "1003006");
        businessFeeConfig.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessFeeConfig.put("endTime", DateUtil.LAST_TIME);
        businessFeeConfig.put("computingFormula", "2002");
        businessFeeConfig.put("squarePrice", "0.00");
        businessFeeConfig.put("additionalAmount", "0.00");
        businessFeeConfig.put("communityId", paramInJson.getString("communityId"));
        businessFeeConfig.put("configId", paramInJson.getString("configId"));
        businessFeeConfig.put("billType", "003");//按天出账

        businessFeeConfig.put("paymentCd", "1200");
        businessFeeConfig.put("paymentCycle", "1");//按月出账
        businessFeeConfig.put("isDefault", "T");
        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(businessFeeConfig, PayFeeConfigPo.class);

        super.insert(dataFlowContext, payFeeConfigPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addFeeConfigParkingSpaceTemp(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_TEMP_DOWN_PARKING_SPACE);
        businessFeeConfig.put("feeName", "临时车费用[系统默认]");
        businessFeeConfig.put("deductFrom", "Y");
        businessFeeConfig.put("feeFlag", "2006012");
        businessFeeConfig.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessFeeConfig.put("endTime", DateUtil.LAST_TIME);
        businessFeeConfig.put("computingFormula", "3003");
        businessFeeConfig.put("squarePrice", "0.00");
        businessFeeConfig.put("additionalAmount", "0.00");
        businessFeeConfig.put("communityId", paramInJson.getString("communityId"));
        businessFeeConfig.put("configId", paramInJson.getString("configId"));
        businessFeeConfig.put("billType", "004");//按天出账
        businessFeeConfig.put("paymentCd", "1200");
        businessFeeConfig.put("paymentCycle", "1");//按月出账
        businessFeeConfig.put("isDefault", "T");
        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(businessFeeConfig, PayFeeConfigPo.class);

        super.insert(dataFlowContext, payFeeConfigPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG);
    }

    /**
     * 添加小区成员 开发者 代理商 运维 商户
     *
     * @param paramInJson 组装 楼小区关系
     * @return 小区成员信息
     */
    public void addCommunityMembers(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", paramInJson.getString("storeId"));
        businessCommunityMember.put("memberTypeCd", CommunityMemberTypeConstant.AGENT);
        businessCommunityMember.put("auditStatusCd", StateConstant.AGREE_AUDIT);
        CommunityMemberPo communityMemberPo = BeanConvertUtil.covertBean(businessCommunityMember, CommunityMemberPo.class);
        super.insert(dataFlowContext, communityMemberPo, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);

        //添加运维商户
        //添加开发商户
        businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", "400000000000000001");
        businessCommunityMember.put("memberTypeCd", CommunityMemberTypeConstant.OPT);
        businessCommunityMember.put("auditStatusCd", StateConstant.AGREE_AUDIT);
        communityMemberPo = BeanConvertUtil.covertBean(businessCommunityMember, CommunityMemberPo.class);
        super.insert(dataFlowContext, communityMemberPo, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);

        businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", "400000000000000002");
        businessCommunityMember.put("memberTypeCd", CommunityMemberTypeConstant.DEV);
        businessCommunityMember.put("auditStatusCd", StateConstant.AGREE_AUDIT);
        communityMemberPo = BeanConvertUtil.covertBean(businessCommunityMember, CommunityMemberPo.class);
        super.insert(dataFlowContext, communityMemberPo, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addCommunity(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("communityId", GenerateCodeFactory.getCommunityId());
        paramInJson.put("state", "1000");
        paramInJson.put("communityArea", "0");
        CommunityPo communityPo = BeanConvertUtil.covertBean(paramInJson, CommunityPo.class);
        super.insert(dataFlowContext, communityPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMMUNITY_INFO);

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        CommunityAttrPo communityAttrPo = BeanConvertUtil.covertBean(paramInJson, CommunityAttrPo.class);
        super.insert(dataFlowContext, communityAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMMUNITY_INFO);

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        CommunityAttrPo communityAttrPo = BeanConvertUtil.covertBean(paramInJson, CommunityAttrPo.class);
        super.insert(dataFlowContext, communityAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_COMMUNITY_INFO);

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateCommunityOne(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        CommunityPo communityPo = BeanConvertUtil.covertBean(paramInJson, CommunityPo.class);

        super.update(dataFlowContext, communityPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_COMMUNITY_INFO);
    }
}
