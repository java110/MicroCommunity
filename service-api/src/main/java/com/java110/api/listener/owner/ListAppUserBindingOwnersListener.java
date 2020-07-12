package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 查询小区侦听类
 */
@Java110Listener("listAppUserBindingOwnersListener")
public class ListAppUserBindingOwnersListener extends AbstractServiceApiListener {

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.LIST_APPUSERBINDINGOWNERS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //super.validatePageInfo(reqJson);

        //  Map<String, String> headers = event.getDataFlowContext().getRequestHeaders();

        //   Assert.hasKeyAndValue(headers, "userid", "请求头中未包含用户信息");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        Map<String, String> headers = event.getDataFlowContext().getRequestHeaders();

        //     String userId = headers.get("userid");

        //根据userId 查询openId
//        UserDto userDto = new UserDto();
//        userDto.setUserId(userId);
//        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);
//
//        Assert.listOnlyOne(userDtos, "未找到相应用户信息，或查询到多条");
//
//        String openId = userDtos.get(0).getOpenId();

        if (!reqJson.containsKey("page")) {
            reqJson.put("page", 1);
        }
        if (!reqJson.containsKey("row")) {
            reqJson.put("row", 10);
        }

        OwnerAppUserDto ownerAppUserDto = BeanConvertUtil.covertBean(reqJson, OwnerAppUserDto.class);
        if (headers.containsKey("userid")) {//这里微信小程序
            ownerAppUserDto.setUserId(headers.get("userid"));
        }

        int count = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsersCount(ownerAppUserDto);


        List<OwnerAppUserDto> ownerAppUserDtos = null;
        if (count > 0) {
            ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
            refreshCommunityArea(ownerAppUserDtos);
        } else {
            ownerAppUserDtos = new ArrayList<>();
        }
        context.setResponseEntity(ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")),count,ownerAppUserDtos));

    }

    /**
     * 刷入小区地区
     *
     * @param ownerAppUserDtos
     */
    private void refreshCommunityArea(List<OwnerAppUserDto> ownerAppUserDtos) {
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityIds(getCommunityIds(ownerAppUserDtos));
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        for (CommunityDto tmpCommunityDto : communityDtos) {
            for (OwnerAppUserDto ownerAppUserDto : ownerAppUserDtos) {
                if (ownerAppUserDto.getCommunityId().equals(tmpCommunityDto.getCommunityId())) {
                    ownerAppUserDto.setAreaCode(tmpCommunityDto.getAreaCode());
                    ownerAppUserDto.setAreaName(tmpCommunityDto.getAreaName());
                    ownerAppUserDto.setParentAreaCode(tmpCommunityDto.getParentAreaCode());
                    ownerAppUserDto.setParentAreaName(tmpCommunityDto.getParentAreaName());
                }
            }
        }


    }

    /**
     * 获取批量userIdsaveOwner
     *
     * @param ownerAppUserDtos 业主绑定信息
     * @return 批量userIds 信息
     */
    private String[] getCommunityIds(List<OwnerAppUserDto> ownerAppUserDtos) {
        List<String> communityIds = new ArrayList<String>();
        for (OwnerAppUserDto ownerAppUserDto : ownerAppUserDtos) {
            communityIds.add(ownerAppUserDto.getCommunityId());
        }

        return communityIds.toArray(new String[communityIds.size()]);
    }

    public IOwnerAppUserInnerServiceSMO getOwnerAppUserInnerServiceSMOImpl() {
        return ownerAppUserInnerServiceSMOImpl;
    }

    public void setOwnerAppUserInnerServiceSMOImpl(IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl) {
        this.ownerAppUserInnerServiceSMOImpl = ownerAppUserInnerServiceSMOImpl;
    }
}
