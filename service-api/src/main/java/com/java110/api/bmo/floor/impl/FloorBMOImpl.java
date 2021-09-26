package com.java110.api.bmo.floor.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.floor.IFloorBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.community.CommunityDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.dto.CommunityMemberDto;
import com.java110.po.community.CommunityMemberPo;
import com.java110.po.floor.FloorPo;
import com.java110.utils.constant.*;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
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
    public void deleteFloor(JSONObject paramInJson, DataFlowContext context) {

        FloorPo floorPo = BeanConvertUtil.covertBean(paramInJson, FloorPo.class);
        super.delete(context, floorPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FLOOR_INFO);
    }

    /**
     * 退出小区成员
     *
     * @param paramInJson 接口传入入参
     * @return 订单服务能够接受的报文
     */
    public void exitCommunityMember(JSONObject paramInJson, DataFlowContext context) {

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
        CommunityMemberPo communityMemberPo = BeanConvertUtil.covertBean(businessCommunityMember, CommunityMemberPo.class);
        super.delete(context, communityMemberPo, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_QUIT_COMMUNITY);

    }

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void editFloor(JSONObject paramInJson, DataFlowContext context) {

        JSONObject businessFloor = new JSONObject();
        businessFloor.put("floorId", paramInJson.getString("floorId"));
        businessFloor.put("name", paramInJson.getString("name"));
        businessFloor.put("remark", paramInJson.getString("remark"));
        businessFloor.put("userId", paramInJson.getString("userId"));
        businessFloor.put("floorNum", paramInJson.getString("floorNum"));
        businessFloor.put("communityId", paramInJson.getString("communityId"));
        businessFloor.put("floorArea", paramInJson.getString("floorArea"));

        FloorPo floorPo = BeanConvertUtil.covertBean(businessFloor, FloorPo.class);
        super.update(context, floorPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FLOOR_INFO);
    }

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addFloor(JSONObject paramInJson, DataFlowContext context) {

        JSONObject businessFloor = new JSONObject();
        businessFloor.put("floorId", paramInJson.getString("floorId"));
        businessFloor.put("name", paramInJson.getString("name"));
        businessFloor.put("remark", paramInJson.getString("remark"));
        businessFloor.put("userId", paramInJson.getString("userId"));
        businessFloor.put("floorNum", paramInJson.getString("floorNum"));
        businessFloor.put("communityId", paramInJson.getString("communityId"));
        businessFloor.put("floorArea", paramInJson.getString("floorArea"));
        FloorPo floorPo = BeanConvertUtil.covertBean(businessFloor, FloorPo.class);
        super.insert(context, floorPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FLOOR_INFO);
    }


    /**
     * 添加小区成员
     *
     * @param paramInJson 组装 楼小区关系
     * @return 小区成员信息
     */
    public void addCommunityMember(JSONObject paramInJson, DataFlowContext context) {

        //查询小区是否存在
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(paramInJson.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos, "小区不存");

        JSONObject businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", paramInJson.getString("floorId"));
        businessCommunityMember.put("memberTypeCd", CommunityMemberTypeConstant.FLOOR);
        businessCommunityMember.put("auditStatusCd", StateConstant.AGREE_AUDIT);

        businessCommunityMember.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        Calendar endTime  = Calendar.getInstance();
        endTime.add(Calendar.MONTH,Integer.parseInt(communityDtos.get(0).getPayFeeMonth()));
        businessCommunityMember.put("endTime", DateUtil.getFormatTimeString(endTime.getTime(),DateUtil.DATE_FORMATE_STRING_A));

        CommunityMemberPo communityMemberPo = BeanConvertUtil.covertBean(businessCommunityMember, CommunityMemberPo.class);
        super.insert(context, communityMemberPo, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);
    }
}
