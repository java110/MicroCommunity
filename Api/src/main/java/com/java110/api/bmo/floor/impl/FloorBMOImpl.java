package com.java110.api.bmo.floor.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.floor.IFloorBMO;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.dto.CommunityMemberDto;
import com.java110.utils.constant.*;
import com.java110.utils.exception.ListenerExecuteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName FloorBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 22:36
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("floorBMOImpl")
public class FloorBMOImpl extends ApiBaseBMO implements IFloorBMO {
    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    private static final int DEFAULT_SEQ_COMMUNITY_MEMBER = 2;
    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject deleteFloor(JSONObject paramInJson) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FLOOR_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFloor = new JSONObject();
        businessFloor.put("floorId", paramInJson.getString("floorId"));
        businessFloor.put("communityId", paramInJson.getString("communityId"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFloor", businessFloor);

        return business;
    }

    /**
     * 退出小区成员
     *
     * @param paramInJson 接口传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject exitCommunityMember(JSONObject paramInJson) {

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_QUIT_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunityMember = new JSONObject();
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setMemberId(paramInJson.getString("floorId"));
        communityMemberDto.setCommunityId(paramInJson.getString("communityId"));
        communityMemberDto.setStatusCd(StatusConstant.STATUS_CD_VALID);
        communityMemberDto.setMemberTypeCd(CommunityMemberTypeConstant.FLOOR);
        List<CommunityMemberDto> communityMemberDtoList = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

        if (communityMemberDtoList == null || communityMemberDtoList.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "小区楼和小区存在关系存在异常，请检查");
        }


        businessCommunityMember.put("communityMemberId", communityMemberDtoList.get(0).getCommunityMemberId());
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember", businessCommunityMember);

        return business;
    }
    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject editFloor(JSONObject paramInJson) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FLOOR_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFloor = new JSONObject();
        businessFloor.put("floorId", paramInJson.getString("floorId"));
        businessFloor.put("name", paramInJson.getString("name"));
        businessFloor.put("remark", paramInJson.getString("remark"));
        businessFloor.put("userId", paramInJson.getString("userId"));
        businessFloor.put("floorNum", paramInJson.getString("floorNum"));
        businessFloor.put("communityId", paramInJson.getString("communityId"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFloor", businessFloor);

        return business;
    }

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFloor(JSONObject paramInJson) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FLOOR_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFloor = new JSONObject();
        businessFloor.put("floorId", paramInJson.getString("floorId"));
        businessFloor.put("name", paramInJson.getString("name"));
        businessFloor.put("remark", paramInJson.getString("remark"));
        businessFloor.put("userId", paramInJson.getString("userId"));
        businessFloor.put("floorNum", paramInJson.getString("floorNum"));
        businessFloor.put("communityId", paramInJson.getString("communityId"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFloor", businessFloor);

        return business;
    }


    /**
     * 添加小区成员
     *
     * @param paramInJson 组装 楼小区关系
     * @return 小区成员信息
     */
    public JSONObject addCommunityMember(JSONObject paramInJson) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ_COMMUNITY_MEMBER);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", paramInJson.getString("floorId"));
        businessCommunityMember.put("memberTypeCd", CommunityMemberTypeConstant.FLOOR);
        businessCommunityMember.put("auditStatusCd", StateConstant.AGREE_AUDIT);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommunityMember", businessCommunityMember);

        return business;
    }
}
