package com.java110.api.bmo.owner.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.owner.IOwnerBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.community.CommunityMemberPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.file.FileRelPo;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.owner.OwnerPo;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.CommunityMemberTypeConstant;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StateConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @ClassName OwnerBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:14
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("ownerBMOImpl")
public class OwnerBMOImpl extends ApiBaseBMO implements IOwnerBMO {
    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    private static final int DEFAULT_SEQ_COMMUNITY_MEMBER = 2;

    /**
     * 添加业主应用用户关系
     *
     * @param paramInJson
     * @return
     */
    public void addOwnerAppUser(JSONObject paramInJson, CommunityDto communityDto, OwnerDto ownerDto, DataFlowContext dataFlowContext) {

        JSONObject businessOwnerAppUser = new JSONObject();
        businessOwnerAppUser.putAll(paramInJson);
        //状态类型，10000 审核中，12000 审核成功，13000 审核失败
        businessOwnerAppUser.put("state", "12000");
        businessOwnerAppUser.put("appTypeCd", "10010");
        businessOwnerAppUser.put("appUserId", "-1");
        businessOwnerAppUser.put("memberId", ownerDto.getMemberId());
        businessOwnerAppUser.put("communityName", communityDto.getName());
        businessOwnerAppUser.put("communityId", communityDto.getCommunityId());
        businessOwnerAppUser.put("appUserName", ownerDto.getName());
        businessOwnerAppUser.put("idCard", ownerDto.getIdCard());
        businessOwnerAppUser.put("link", ownerDto.getLink());
        businessOwnerAppUser.put("userId", paramInJson.getString("userId"));
        OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(businessOwnerAppUser, OwnerAppUserPo.class);
        super.insert(dataFlowContext, ownerAppUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_APP_USER);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteAuditAppUserBindingOwner(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(paramInJson, OwnerAppUserPo.class);
        super.delete(dataFlowContext, ownerAppUserPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_APP_USER);
    }

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void deleteOwner(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessOwner = new JSONObject();
        businessOwner.put("memberId", paramInJson.getString("memberId"));
        businessOwner.put("communityId", paramInJson.getString("communityId"));

        OwnerPo ownerPo = BeanConvertUtil.covertBean(businessOwner, OwnerPo.class);
        super.delete(dataFlowContext, ownerPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_INFO);
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(paramInJson.getString("ownerId"));
        //查询app用户表
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ownerAppUserDtos != null && ownerAppUserDtos.size() > 0) {
            for (OwnerAppUserDto ownerAppUser : ownerAppUserDtos) {
                OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(ownerAppUser, OwnerAppUserPo.class);
                super.delete(dataFlowContext, ownerAppUserPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_APP_USER);
            }
        }
    }


    /**
     * 退出小区成员
     *
     * @param paramInJson 接口传入入参
     * @return 订单服务能够接受的报文
     */
    public void exitCommunityMember(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        JSONObject businessCommunityMember = new JSONObject();
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setMemberId(paramInJson.getString("ownerId"));
        communityMemberDto.setCommunityId(paramInJson.getString("communityId"));
        communityMemberDto.setStatusCd(StatusConstant.STATUS_CD_VALID);
        communityMemberDto.setMemberTypeCd(CommunityMemberTypeConstant.OWNER);
        List<CommunityMemberDto> communityMemberDtoList = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

        if (communityMemberDtoList == null || communityMemberDtoList.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "业主和小区存在关系存在异常，请检查");
        }

        businessCommunityMember.put("communityMemberId", communityMemberDtoList.get(0).getCommunityMemberId());
        CommunityMemberPo communityMemberPo = BeanConvertUtil.covertBean(businessCommunityMember, CommunityMemberPo.class);
        super.delete(dataFlowContext, communityMemberPo, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_QUIT_COMMUNITY);
    }

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void editOwner(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(paramInJson.getString("memberId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        Assert.listOnlyOne(ownerDtos, "未查询到业主信息或查询到多条");

        JSONObject businessOwner = new JSONObject();
        businessOwner.putAll(BeanConvertUtil.beanCovertMap(ownerDtos.get(0)));
        businessOwner.putAll(paramInJson);

        if (paramInJson.containsKey("wxPhoto")) {
            businessOwner.put("link", paramInJson.getString("wxPhoto"));
        }
        businessOwner.put("state", ownerDtos.get(0).getState());
        OwnerPo ownerPo = BeanConvertUtil.covertBean(businessOwner, OwnerPo.class);
        super.update(dataFlowContext, ownerPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_INFO);
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(paramInJson.getString("ownerId"));
        //查询app用户表
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ownerAppUserDtos != null && ownerAppUserDtos.size() > 0) {
            for (OwnerAppUserDto ownerAppUser : ownerAppUserDtos) {
                OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(ownerAppUser, OwnerAppUserPo.class);
                ownerAppUserPo.setLink(paramInJson.getString("link"));
                ownerAppUserPo.setIdCard(paramInJson.getString("idCard"));
                super.update(dataFlowContext, ownerAppUserPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_APP_USER);
            }
        }
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void editOwnerPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setRelTypeCd("10000");
        fileRelDto.setObjId(paramInJson.getString("memberId"));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() == 0) {
            JSONObject businessUnit = new JSONObject();
            businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
            businessUnit.put("relTypeCd", "10000");
            businessUnit.put("saveWay", "table");
            businessUnit.put("objId", paramInJson.getString("memberId"));
            businessUnit.put("fileRealName", paramInJson.getString("ownerPhotoId"));
            businessUnit.put("fileSaveName", paramInJson.getString("ownerPhotoId"));
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
            super.insert(dataFlowContext, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
            return;
        }

        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(BeanConvertUtil.beanCovertMap(fileRelDtos.get(0)));
        businessUnit.put("fileRealName", paramInJson.getString("ownerPhotoId"));
        businessUnit.put("fileSaveName", paramInJson.getString("fileSaveName"));
        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
        super.update(dataFlowContext, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FILE_REL);


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
    public void addOwner(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessOwner = new JSONObject();
        businessOwner.putAll(paramInJson);
        businessOwner.put("state", "2000");
        OwnerPo ownerPo = BeanConvertUtil.covertBean(businessOwner, OwnerPo.class);
        super.insert(dataFlowContext, ownerPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_INFO);
    }


    /**
     * 添加小区成员
     *
     * @param paramInJson 组装 楼小区关系
     * @return 小区成员信息
     */
    public void addCommunityMember(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        //查询小区是否存在
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(paramInJson.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos, "小区不存");

        JSONObject businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", paramInJson.getString("ownerId"));
        businessCommunityMember.put("memberTypeCd", CommunityMemberTypeConstant.OWNER);
        businessCommunityMember.put("auditStatusCd", StateConstant.AGREE_AUDIT);
        businessCommunityMember.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.MONTH, Integer.parseInt(communityDtos.get(0).getPayFeeMonth()));
        businessCommunityMember.put("endTime", DateUtil.getFormatTimeString(endTime.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        CommunityMemberPo communityMemberPo = BeanConvertUtil.covertBean(businessCommunityMember, CommunityMemberPo.class);
        super.insert(dataFlowContext, communityMemberPo, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);
    }


    /**
     * 售卖房屋信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void sellRoom(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(paramInJson);
        businessUnit.put("relId", "-1");
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        OwnerRoomRelPo ownerRoomRelPo = BeanConvertUtil.covertBean(businessUnit, OwnerRoomRelPo.class);
        super.insert(dataFlowContext, ownerRoomRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_ROOM_REL);
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addPropertyFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_PROPERTY);
        feeConfigDto.setIsDefault("T");
        feeConfigDto.setCommunityId(paramInJson.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        if (feeConfigDtos == null || feeConfigDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到费用配置信息，查询多条数据");
        }

        feeConfigDto = feeConfigDtos.get(0);

        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", "-1");
        businessUnit.put("configId", feeConfigDto.getConfigId());
        businessUnit.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_PROPERTY);
        businessUnit.put("incomeObjId", paramInJson.getString("storeId"));
        businessUnit.put("amount", "-1.00");
        businessUnit.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessUnit.put("endTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessUnit.put("communityId", paramInJson.getString("communityId"));
        businessUnit.put("payerObjId", paramInJson.getString("roomId"));
        businessUnit.put("payerObjType", "3333");
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        PayFeePo payFeePo = BeanConvertUtil.covertBean(businessUnit, PayFeePo.class);
        super.insert(dataFlowContext, payFeePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addOwnerPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
        businessUnit.put("relTypeCd", "10000");
        businessUnit.put("saveWay", "table");
        businessUnit.put("objId", paramInJson.getString("memberId"));
        businessUnit.put("fileRealName", paramInJson.getString("ownerPhotoId"));
        businessUnit.put("fileSaveName", paramInJson.getString("fileSaveName"));
        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
        super.insert(dataFlowContext, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
    }

    /**
     * 添加审核业主绑定信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateAuditAppUserBindingOwner(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setAppUserId(paramInJson.getString("appUserId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        Assert.listOnlyOne(ownerAppUserDtos, "存在多条审核单或未找到审核单");

        JSONObject businessOwnerAppUser = new JSONObject();
        businessOwnerAppUser.putAll(BeanConvertUtil.beanCovertMap(ownerAppUserDtos.get(0)));
        businessOwnerAppUser.put("state", paramInJson.getString("state"));
        businessOwnerAppUser.put("appUserId", paramInJson.getString("appUserId"));
        OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(businessOwnerAppUser, OwnerAppUserPo.class);
        super.update(dataFlowContext, ownerAppUserPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_APP_USER);
    }

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void editOwnerPhotoPass(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(paramInJson.getString("memberId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        Assert.listOnlyOne(ownerDtos, "未查询到业主信息或查询到多条");

        JSONObject businessOwner = new JSONObject();
        Map ownerDtoMap = BeanConvertUtil.beanCovertMap(ownerDtos.get(0));
        businessOwner.putAll(ownerDtoMap);

        OwnerPo ownerPo = BeanConvertUtil.covertBean(businessOwner, OwnerPo.class);
        super.update(dataFlowContext, ownerPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_INFO);

    }

}
