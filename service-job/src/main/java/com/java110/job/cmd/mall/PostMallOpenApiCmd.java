package com.java110.job.cmd.mall;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.community.CommunityMemberDto;
import com.java110.dto.store.StoreDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.store.IStoreUserV1InnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.job.adapt.hcIotNew.http.ISendIot;
import com.java110.job.mall.ISendMall;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "mall.postMallOpenApi")
public class PostMallOpenApiCmd extends Cmd {

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private IStoreUserV1InnerServiceSMO storeUserV1InnerServiceSMOImpl;

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private ISendMall sendMallImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "mallApiCode", "未包含MALL接口编码");

        String mallSwitch = MappingCache.getValue("MALL", "MALL_SWITCH");
        if (!"ON".equals(mallSwitch)) {
            throw new CmdException("商城系统未部署");
        }

        String userId = CmdContextUtils.getUserId(context);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");

        reqJson.put("propertyUserTel", userDtos.get(0).getTel());
        reqJson.put("propertyUserId", userDtos.get(0).getUserId());

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String communityId = reqJson.getString("communityId");
        if(!StringUtil.isEmpty(communityId)){
            CommunityDto communityDto = new CommunityDto();
            communityDto.setCommunityId(communityId);
            List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

            if (ListUtil.isNull(communityDtos)) {
                throw new CmdException("小区不存在");
            }


            //todo 查询物业信息
            CommunityMemberDto communityMemberDto = new CommunityMemberDto();
            communityMemberDto.setCommunityId(communityDtos.get(0).getCommunityId());
            communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
            List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);
            if (ListUtil.isNull(communityMemberDtos)) {
                throw new CmdException("物业不存在");
            }


            StoreDto storeDto = new StoreDto();
            storeDto.setStoreId(communityMemberDtos.get(0).getMemberId());
            List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

            if (ListUtil.isNull(storeDtos)) {
                throw new CmdException("物业不存在");
            }

            reqJson.put("communityId", communityDtos.get(0).getCommunityId());
            reqJson.put("communityName", communityDtos.get(0).getName());
            reqJson.put("storeId", storeDtos.get(0).getStoreId());
            reqJson.put("storeName", storeDtos.get(0).getName());
        }


        ResultVo resultVo = sendMallImpl.post("/mall/api/common.openCommonApi", reqJson);

        if (resultVo.getCode() != ResultVo.CODE_OK) {
            throw new CmdException(resultVo.getMsg());
        }

        context.setResponseEntity(ResultVo.createResponseEntity(resultVo));

    }
}
