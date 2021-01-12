package com.java110.api.listener.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.owner.IOwnerBMO;
import com.java110.api.bmo.room.IRoomBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.po.owner.OwnerPo;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * @ClassName SaveUnitListener
 * @Description TODO 售卖房屋信息
 * @Author wuxw
 * @Date 2019/5/3 11:54
 * @Version 1.0
 * add by wuxw 2019/5/3
 **/
@Java110Listener("saveOwnerShopsListener")
public class SaveOwnerShopsListener extends AbstractServiceApiPlusListener {
    private static Logger logger = LoggerFactory.getLogger(SaveOwnerShopsListener.class);

    @Autowired
    private IRoomBMO roomBMOImpl;

    @Autowired
    private IOwnerBMO ownerBMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_OWNER_SHOPS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "roomId", "请求报文中未包含roomId节点");
        Assert.jsonObjectHaveKey(reqJson, "ownerName", "请求报文中未包含ownerName节点");
        Assert.jsonObjectHaveKey(reqJson, "tel", "请求报文中未包含tel节点");
        Assert.jsonObjectHaveKey(reqJson, "startTime", "请求报文中未包含tel节点");
        Assert.jsonObjectHaveKey(reqJson, "endTime", "请求报文中未包含tel节点");
        Assert.jsonObjectHaveKey(reqJson, "shopsState", "请求报文中未包含状态节点");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        if (!reqJson.containsKey("ownerId")
                || reqJson.getString("ownerId").startsWith("-")
                || StringUtil.isEmpty(reqJson.getString("ownerId"))
        ) {
            OwnerPo ownerPo = new OwnerPo();
            ownerPo.setUserId(context.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
            ownerPo.setAge("1");
            ownerPo.setCommunityId(reqJson.getString("communityId"));
            ownerPo.setIdCard("");
            ownerPo.setLink(reqJson.getString("tel"));
            ownerPo.setMemberId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId));
            ownerPo.setName(reqJson.getString("ownerName"));
            ownerPo.setOwnerId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId));
            ownerPo.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
            ownerPo.setRemark(reqJson.getString("remark"));
            ownerBMOImpl.addOwner(JSONObject.parseObject(JSONObject.toJSONString(ownerPo)), context);
            reqJson.put("ownerId", ownerPo.getOwnerId());
        }

        //查询商铺是否为 出租 或者空闲
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(reqJson.getString("roomId"));
        roomDto.setCommunityId(reqJson.getString("communityId"));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "商铺不存在");

        if (!"2006,2008".contains(roomDtos.get(0).getState())) {
            throw new IllegalArgumentException("当前商铺状态不允许操作");
        }

        //判断房屋是有租客
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(reqJson.getString("roomId"));
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if(ownerRoomRelDtos != null && ownerRoomRelDtos.size()> 0){
            JSONObject businessUnit = new JSONObject();
            businessUnit.put("relId", ownerRoomRelDtos.get(0).getRelId());
            OwnerRoomRelPo roomPo = BeanConvertUtil.covertBean(businessUnit, OwnerRoomRelPo.class);
            super.delete(context, roomPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_ROOM_REL);
        }

        //添加房屋关系
        reqJson.put("state", "2001");
        roomBMOImpl.sellRoom(reqJson, context);

        //更新房屋信息为售出
        reqJson.put("state", reqJson.getString("shopsState"));
        roomBMOImpl.updateShellRoom(reqJson, context);

        //添加物业费用信息
        //roomBMOImpl.addPropertyFee(reqJson, context);
    }


    public ICommunityInnerServiceSMO getCommunityInnerServiceSMOImpl() {
        return communityInnerServiceSMOImpl;
    }

    public void setCommunityInnerServiceSMOImpl(ICommunityInnerServiceSMO communityInnerServiceSMOImpl) {
        this.communityInnerServiceSMOImpl = communityInnerServiceSMOImpl;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IUnitInnerServiceSMO getUnitInnerServiceSMOImpl() {
        return unitInnerServiceSMOImpl;
    }

    public void setUnitInnerServiceSMOImpl(IUnitInnerServiceSMO unitInnerServiceSMOImpl) {
        this.unitInnerServiceSMOImpl = unitInnerServiceSMOImpl;
    }
}
