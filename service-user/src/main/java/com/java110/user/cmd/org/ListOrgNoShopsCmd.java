package com.java110.user.cmd.org;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.roleCommunity.RoleCommunityDto;
import com.java110.dto.shop.ShopDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.mall.IShopInnerServiceSMO;
import com.java110.intf.user.IRoleCommunityV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.community.ApiCommunityDataVo;
import com.java110.vo.api.community.ApiCommunityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "org.listOrgNoShops")
public class ListOrgNoShopsCmd extends Cmd {

    @Autowired
    private IRoleCommunityV1InnerServiceSMO roleCommunityV1InnerServiceSMO;

    @Autowired(required = false)
    private IShopInnerServiceSMO shopInnerServiceSMO;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        if(!reqJson.containsKey("storeId") || StringUtil.isEmpty(reqJson.getString("storeId"))) {
            String storeId = context.getReqHeaders().get("store-id");
            reqJson.put("storeId",storeId);
        }
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");

        Assert.hasKeyAndValue(reqJson, "roleId", "必填，请填写角色");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        RoleCommunityDto roleCommunityDto = BeanConvertUtil.covertBean(reqJson, RoleCommunityDto.class);

        List<RoleCommunityDto> orgCommunityDtos = roleCommunityV1InnerServiceSMO.queryRoleCommunitys(roleCommunityDto);
        List<String> communityIds = new ArrayList<>();
        for(RoleCommunityDto tmpOrgCommunityDto : orgCommunityDtos){
            communityIds.add(tmpOrgCommunityDto.getCommunityId());
        }
        ShopDto shopDto = BeanConvertUtil.covertBean(reqJson, ShopDto.class);
        if(communityIds.size()>0) {
            shopDto.setNotInShopId(communityIds.toArray(new String[communityIds.size()]));
        }
        shopDto.setState(ShopDto.STATE_Y);
        shopDto.setStoreId(reqJson.getString("storeId"));
        int count = shopInnerServiceSMO.queryShopsCount(shopDto);

        List<ShopDto> communitys = null;

        if (count > 0) {
            communitys = BeanConvertUtil.covertBeanList(shopInnerServiceSMO.queryShops(shopDto), ShopDto.class);
        } else {
            communitys = new ArrayList<>();
        }

        context.setResponseEntity(ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")),count,communitys));
    }
}
