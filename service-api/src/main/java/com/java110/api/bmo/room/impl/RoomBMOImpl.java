package com.java110.api.bmo.room.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.room.IRoomBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.po.community.CommunityMemberPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.floor.FloorPo;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.po.room.RoomAttrPo;
import com.java110.po.room.RoomPo;
import com.java110.po.unit.UnitPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.CommunityMemberTypeConstant;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StateConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName RoomBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:43
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/

@Service("roomBMOImpl")
public class RoomBMOImpl extends ApiBaseBMO implements IRoomBMO {

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;
    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMO;

    /**
     * 修改房屋
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateRoom(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(paramInJson.getString("communityId"));
        roomDto.setRoomId(paramInJson.getString("roomId"));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "存在" + roomDtos.size() + "条房屋信息");

        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(BeanConvertUtil.beanCovertMap(roomDtos.get(0)));
        businessUnit.putAll(paramInJson);
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        RoomPo roomPo = BeanConvertUtil.covertBean(businessUnit, RoomPo.class);
        super.update(dataFlowContext, roomPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ROOM_INFO);
    }


    public void addBusinessFloor(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        FloorPo floorPo = BeanConvertUtil.covertBean(paramInJson, FloorPo.class);
        super.insert(dataFlowContext, floorPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FLOOR_INFO);
    }

    /**
     * 添加小区成员
     *
     * @param paramInJson 组装 楼小区关系
     * @return 小区成员信息
     */
    public void addCommunityMember(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessCommunityMember = new JSONObject();
        businessCommunityMember.put("communityMemberId", "-1");
        businessCommunityMember.put("communityId", paramInJson.getString("communityId"));
        businessCommunityMember.put("memberId", paramInJson.getString("floorId"));
        businessCommunityMember.put("memberTypeCd", CommunityMemberTypeConstant.FLOOR);
        businessCommunityMember.put("auditStatusCd", StateConstant.AGREE_AUDIT);
        CommunityMemberPo communityMemberPo = BeanConvertUtil.covertBean(businessCommunityMember, CommunityMemberPo.class);
        super.insert(dataFlowContext, communityMemberPo, BusinessTypeConstant.BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY);
    }

    public void addBusinessUnit(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        UnitPo unitPo = BeanConvertUtil.covertBean(paramInJson, UnitPo.class);
        super.insert(dataFlowContext, unitPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_UNIT_INFO);
    }

    public void addBusinessRoom(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        RoomPo roomPo = BeanConvertUtil.covertBean(paramInJson, RoomPo.class);
        super.insert(dataFlowContext, roomPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ROOM_INFO);
    }

    /**
     * 添加小区楼信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteRoom(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(paramInJson);
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        RoomPo roomPo = BeanConvertUtil.covertBean(businessUnit, RoomPo.class);
        super.delete(dataFlowContext, roomPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ROOM_INFO);
    }

    /**
     * 售卖房屋信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void exitRoom(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        //根据ownerId 和 roomId 查询relId 删除
        OwnerRoomRelDto ownerRoomRelDto = BeanConvertUtil.covertBean(paramInJson, OwnerRoomRelDto.class);
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "数据存在问题，业主和房屋对应关系不是一条");
        }


        JSONObject businessUnit = new JSONObject();
        //businessUnit.putAll(paramInJson);
        businessUnit.put("relId", ownerRoomRelDtos.get(0).getRelId());
        //businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        OwnerRoomRelPo roomPo = BeanConvertUtil.covertBean(businessUnit, OwnerRoomRelPo.class);
        super.delete(dataFlowContext, roomPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_ROOM_REL);
    }


    /**
     * 删除物业费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void exitPropertyFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        //校验物业费是否已经交清
        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(paramInJson.getString("communityId"));
        feeDto.setIncomeObjId(paramInJson.getString("storeId"));
        feeDto.setPayerObjId(paramInJson.getString("roomId"));
        feeDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_PROPERTY);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "数据存在问题，物业费对应关系不是一条");
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
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addRoom(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(paramInJson);
        businessUnit.put("roomId", "-1");
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        RoomPo roomPo = BeanConvertUtil.covertBean(businessUnit, RoomPo.class);
        super.insert(dataFlowContext, roomPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ROOM_INFO);
    }

    @Override
    public void addRoomAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        RoomAttrPo roomAttrPo = new RoomAttrPo();
        roomAttrPo.setAttrId(GenerateCodeFactory.getAttrId());
        roomAttrPo.setRoomId(paramInJson.getString("roomId"));
        roomAttrPo.setSpecCd(paramInJson.getString("specCd"));
        roomAttrPo.setValue(paramInJson.getString("value"));
        super.insert(dataFlowContext, roomAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ROOM_INFO);
    }


    @Override
    public void updateRoomAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        RoomAttrPo roomAttrPo = new RoomAttrPo();
        roomAttrPo.setAttrId(paramInJson.getString("attrId"));
        roomAttrPo.setRoomId(paramInJson.getString("roomId"));
        roomAttrPo.setSpecCd(paramInJson.getString("specCd"));
        roomAttrPo.setValue(paramInJson.getString("value"));
        super.update(dataFlowContext, roomAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ROOM_INFO);
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
        feeConfigDto.setCommunityId(paramInJson.getString("communityId"));
        feeConfigDto.setIsDefault("T");
        feeConfigDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_PROPERTY);
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMO.queryFeeConfigs(feeConfigDto);

        Assert.listOnlyOne(feeConfigDtos, "物业费默认配置不存在或存在多条请检查");
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", "-1");
        businessUnit.put("configId", feeConfigDtos.get(0).getConfigId());
        businessUnit.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_PROPERTY);
        businessUnit.put("incomeObjId", paramInJson.getString("storeId"));
        businessUnit.put("amount", "-1.00");
        businessUnit.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessUnit.put("endTime", paramInJson.containsKey("feeEndDate") ? paramInJson.getString("feeEndDate")
                : DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessUnit.put("communityId", paramInJson.getString("communityId"));
        businessUnit.put("payerObjId", paramInJson.getString("roomId"));
        businessUnit.put("payerObjType", "3333");
        businessUnit.put("feeFlag", "1003006");
        businessUnit.put("state", "2008001");
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        PayFeePo payFeePo = BeanConvertUtil.covertBean(businessUnit, PayFeePo.class);
        super.delete(dataFlowContext, payFeePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
    }

    /**
     * 添加小区楼信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateShellRoom(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(paramInJson);
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        RoomPo roomPo = BeanConvertUtil.covertBean(businessUnit, RoomPo.class);
        super.update(dataFlowContext, roomPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ROOM_INFO);
    }


}
