package com.java110.api.bmo.community.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.community.ICommunityBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.community.CommunityDto;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.*;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject updateCommunity(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(paramInJson.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listOnlyOne(communityDtos, "未查询到该小区信息【" + communityDto.getCommunityId() + "】");
        communityDto = communityDtos.get(0);

        Map oldCommunityInfo = BeanConvertUtil.beanCovertMap(communityDto);
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_COMMUNITY_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunity = new JSONObject();
        businessCommunity.putAll(oldCommunityInfo);
        businessCommunity.put("state", paramInJson.getString("state"));

        //审核未通过原因未记录，后期存储在工作流框架中

        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunity", businessCommunity);
        return business;
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject updateCommunityMember(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityMemberId(paramInJson.getString("communityMemberId"));
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);
        Assert.listOnlyOne(communityMemberDtos, "未查询到该小区成员信息【" + communityMemberDto.getCommunityMemberId() + "】");
        communityMemberDto = communityMemberDtos.get(0);

        Map oldCommunityInfo = BeanConvertUtil.beanCovertMap(communityMemberDto);
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_AUDIT_COMMUNITY_MEMBER_STATE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunity = new JSONObject();
        businessCommunity.putAll(oldCommunityInfo);
        businessCommunity.put("auditStatusCd", paramInJson.getString("state"));

        //审核未通过原因未记录，后期存储在工作流框架中

        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember", businessCommunity);
        return business;
    }

    /**
     * 添加小区成员
     *
     * @param paramInJson 接口请求数据封装
     * @return 封装好的 data数据
     */
    public JSONObject addCommunityMember(JSONObject paramInJson) {

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", paramInJson.getString("memberId"));
        businessCommunityMember.put("memberTypeCd", paramInJson.getString("memberTypeCd"));
        String auditStatusCd = MappingCache.getValue(MappingConstant.DOMAIN_COMMUNITY_MEMBER_AUDIT, paramInJson.getString("memberTypeCd"));
        auditStatusCd = StringUtils.isEmpty(auditStatusCd) ? StateConstant.AGREE_AUDIT : auditStatusCd;
        businessCommunityMember.put("auditStatusCd", auditStatusCd);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember", businessCommunityMember);

        return business;
    }

    /**
     * 添加小区成员
     * @param paramInJson
     * @return
     */
    public JSONObject deleteCommunityMember(JSONObject paramInJson){

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_QUIT_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ,2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL,CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId",paramInJson.getString("communityMemberId"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember",businessCommunityMember);

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
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember", businessCommunityMember);

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
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember", businessCommunityMember);

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
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember", businessCommunityMember);

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
    public JSONObject deleteCommunity(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_COMMUNITY_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunity = new JSONObject();
        businessCommunity.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunity", businessCommunity);
        return business;
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeeConfigProperty(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        paramInJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_PROPERTY);
        businessFeeConfig.put("feeName", "物业费[系统默认]");
        businessFeeConfig.put("feeFlag", "1003006");
        businessFeeConfig.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessFeeConfig.put("endTime", DateUtil.LAST_TIME);
        businessFeeConfig.put("computingFormula", "1001");
        businessFeeConfig.put("squarePrice", "0.00");
        businessFeeConfig.put("additionalAmount", "0.00");
        businessFeeConfig.put("communityId", paramInJson.getString("communityId"));
        businessFeeConfig.put("configId", paramInJson.getString("configId"));
        businessFeeConfig.put("isDefault", "T");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFeeConfig", businessFeeConfig);
        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeeConfigParkingSpaceUpSell(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_SELL_UP_PARKING_SPACE);
        businessFeeConfig.put("feeName", "地上出售车位费[系统默认]");
        businessFeeConfig.put("feeFlag", "2006012");
        businessFeeConfig.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessFeeConfig.put("endTime", DateUtil.LAST_TIME);
        businessFeeConfig.put("computingFormula", "2002");
        businessFeeConfig.put("squarePrice", "0.00");
        businessFeeConfig.put("additionalAmount", "0.00");
        businessFeeConfig.put("communityId", paramInJson.getString("communityId"));
        businessFeeConfig.put("configId", paramInJson.getString("configId"));
        businessFeeConfig.put("isDefault", "T");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFeeConfig", businessFeeConfig);
        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeeConfigParkingSpaceDownSell(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 3);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_SELL_DOWN_PARKING_SPACE);
        businessFeeConfig.put("feeName", "地下出售车位费[系统默认]");
        businessFeeConfig.put("feeFlag", "2006012");
        businessFeeConfig.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessFeeConfig.put("endTime", DateUtil.LAST_TIME);
        businessFeeConfig.put("computingFormula", "2002");
        businessFeeConfig.put("squarePrice", "0.00");
        businessFeeConfig.put("additionalAmount", "0.00");
        businessFeeConfig.put("communityId", paramInJson.getString("communityId"));
        businessFeeConfig.put("configId", paramInJson.getString("configId"));
        businessFeeConfig.put("isDefault", "T");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFeeConfig", businessFeeConfig);
        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeeConfigParkingSpaceUpHire(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 4);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_HIRE_UP_PARKING_SPACE);
        businessFeeConfig.put("feeName", "地上出租车位费[系统默认]");
        businessFeeConfig.put("feeFlag", "1003006");
        businessFeeConfig.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessFeeConfig.put("endTime", DateUtil.LAST_TIME);
        businessFeeConfig.put("computingFormula", "2002");
        businessFeeConfig.put("squarePrice", "0.00");
        businessFeeConfig.put("additionalAmount", "0.00");
        businessFeeConfig.put("communityId", paramInJson.getString("communityId"));
        businessFeeConfig.put("configId", paramInJson.getString("configId"));
        businessFeeConfig.put("isDefault", "T");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFeeConfig", businessFeeConfig);
        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeeConfigParkingSpaceDownHire(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 5);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_HIRE_DOWN_PARKING_SPACE);
        businessFeeConfig.put("feeName", "地下出租车位费[系统默认]");
        businessFeeConfig.put("feeFlag", "1003006");
        businessFeeConfig.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessFeeConfig.put("endTime", DateUtil.LAST_TIME);
        businessFeeConfig.put("computingFormula", "2002");
        businessFeeConfig.put("squarePrice", "0.00");
        businessFeeConfig.put("additionalAmount", "0.00");
        businessFeeConfig.put("communityId", paramInJson.getString("communityId"));
        businessFeeConfig.put("configId", paramInJson.getString("configId"));
        businessFeeConfig.put("isDefault", "T");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFeeConfig", businessFeeConfig);
        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeeConfigParkingSpaceTemp(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_CONFIG);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 6);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_TEMP_DOWN_PARKING_SPACE);
        businessFeeConfig.put("feeName", "临时车费用[系统默认]");
        businessFeeConfig.put("feeFlag", "2006012");
        businessFeeConfig.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessFeeConfig.put("endTime", DateUtil.LAST_TIME);
        businessFeeConfig.put("computingFormula", "3003");
        businessFeeConfig.put("squarePrice", "0.00");
        businessFeeConfig.put("additionalAmount", "0.00");
        businessFeeConfig.put("communityId", paramInJson.getString("communityId"));
        businessFeeConfig.put("configId", paramInJson.getString("configId"));
        businessFeeConfig.put("isDefault", "T");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFeeConfig", businessFeeConfig);
        return business;
    }

    /**
     * 添加小区成员 开发者 代理商 运维 商户
     *
     * @param paramInJson 组装 楼小区关系
     * @return 小区成员信息
     */
    public JSONArray addCommunityMembers(JSONObject paramInJson) {

        JSONArray businesses = new JSONArray();

        //添加代理商户
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_ORDER + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", paramInJson.getString("storeId"));
        businessCommunityMember.put("memberTypeCd", CommunityMemberTypeConstant.AGENT);
        businessCommunityMember.put("auditStatusCd", StateConstant.AGREE_AUDIT);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember", businessCommunityMember);
        businesses.add(business);

        //添加运维商户
        //添加开发商户
        business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_ORDER + 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", "400000000000000001");
        businessCommunityMember.put("memberTypeCd", CommunityMemberTypeConstant.OPT);
        businessCommunityMember.put("auditStatusCd", StateConstant.AGREE_AUDIT);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember", businessCommunityMember);
        businesses.add(business);

        business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_ORDER + 3);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", "400000000000000002");
        businessCommunityMember.put("memberTypeCd", CommunityMemberTypeConstant.DEV);
        businessCommunityMember.put("auditStatusCd", StateConstant.AGREE_AUDIT);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember", businessCommunityMember);
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
    public JSONObject addCommunity(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("communityId", GenerateCodeFactory.getCommunityId());
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMMUNITY_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunity = new JSONObject();
        businessCommunity.putAll(paramInJson);
        businessCommunity.put("state", "1000");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunity", businessCommunity);
        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject updateCommunityOne(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_COMMUNITY_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunity = new JSONObject();
        businessCommunity.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunity", businessCommunity);
        return business;
    }
}
