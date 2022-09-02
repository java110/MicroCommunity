package com.java110.community.cmd.community;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.org.OrgCommunityDto;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.dto.roleCommunity.RoleCommunityDto;
import com.java110.dto.store.StoreDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.utils.constant.StateConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.community.ApiCommunityDataVo;
import com.java110.vo.api.community.ApiCommunityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "community.listMyEnteredCommunitys")
public class ListMyEnteredCommunitysCmd extends Cmd {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelInnerServiceSMO orgStaffRelInnerServiceSMOImpl;

    @Autowired
    private IRoleCommunityV1InnerServiceSMO roleCommunityV1InnerServiceSMOImpl;

    @Autowired
    private IOrgInnerServiceSMO orgInnerServiceSMOImpl;

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

        if (!reqJson.containsKey("storeId") || StringUtil.isEmpty(reqJson.getString("storeId"))) {
            reqJson.put("storeId", cmdDataFlowContext.getReqHeaders().get("store-id"));
        }

        if (!reqJson.containsKey("userId") || StringUtil.isEmpty(reqJson.getString("userId"))) {
            reqJson.put("userId", cmdDataFlowContext.getReqHeaders().get("user-id"));
        }

        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含商户信息");
        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含用户信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {


        // 判断是不是管理员，管理员反馈 物业 的所有小区
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        userDto.setPage(1);
        userDto.setRow(1);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");

        //校验商户是否存在;
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(reqJson.getString("storeId"));
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

        Assert.listOnlyOne(storeDtos, "商户不存在");

        int count = 0;
        List<ApiCommunityDataVo> communitys = null;
        if(UserDto.LEVEL_CD_ADMIN.equals(userDtos.get(0).getLevelCd())){
            CommunityDto communityDto = BeanConvertUtil.covertBean(reqJson, CommunityDto.class);
            communityDto.setMemberId(reqJson.getString("storeId"));
            communityDto.setAuditStatusCd(StateConstant.AGREE_AUDIT);
            if (reqJson.containsKey("communityName")) {
                communityDto.setName(reqJson.getString("communityName"));
            }
            count = communityInnerServiceSMOImpl.queryCommunitysCount(communityDto);
            if (count > 0) {
                communitys = BeanConvertUtil.covertBeanList(communityInnerServiceSMOImpl.queryCommunitys(communityDto), ApiCommunityDataVo.class);
            } else {
                communitys = new ArrayList<>();
            }
        }else{
            RoleCommunityDto orgCommunityDto = BeanConvertUtil.covertBean(reqJson, RoleCommunityDto.class);
            orgCommunityDto.setStaffId(userDtos.get(0).getUserId());
            count = roleCommunityV1InnerServiceSMOImpl.queryRoleCommunitysNameCount(orgCommunityDto);
            if (count > 0) {
                List<RoleCommunityDto> roleCommunityDtos = roleCommunityV1InnerServiceSMOImpl.queryRoleCommunitysName(orgCommunityDto);
                communitys = BeanConvertUtil.covertBeanList(roleCommunityDtos, ApiCommunityDataVo.class);
                for (RoleCommunityDto tmpOrgCommunityDto : roleCommunityDtos) {
                    for (ApiCommunityDataVo tmpApiCommunityDataVo : communitys) {
                        if (tmpOrgCommunityDto.getCommunityId().equals(tmpApiCommunityDataVo.getCommunityId())) {
                            tmpApiCommunityDataVo.setName(tmpOrgCommunityDto.getCommunityName());
                        }
                    }
                }
            } else {
                communitys = new ArrayList<>();
            }

        }
        //兼容 系统刚开始没有小区时
        if (communitys.size() < 1 && (StoreDto.STORE_TYPE_ADMIN.equals(storeDtos.get(0).getStoreTypeCd()) || StoreDto.STORE_TYPE_DEV.equals(storeDtos.get(0).getStoreTypeCd()))) {
            ApiCommunityDataVo apiCommunityDataVo = new ApiCommunityDataVo();
            apiCommunityDataVo.setCommunityId("-1");
            apiCommunityDataVo.setName("默认小区");
            apiCommunityDataVo.setTel("18909711234");
            apiCommunityDataVo.setState("1100");
            communitys.add(apiCommunityDataVo);
        }

        //if(communitys.size() < 1 && )
        ApiCommunityVo apiCommunityVo = new ApiCommunityVo();
        apiCommunityVo.setTotal(count);
        apiCommunityVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiCommunityVo.setCommunitys(communitys);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiCommunityVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
