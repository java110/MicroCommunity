/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.store.cmd.property;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.Environment;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.menu.MenuGroupDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.common.IWorkflowV1InnerServiceSMO;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.intf.store.IStoreAttrV1InnerServiceSMO;
import com.java110.intf.store.IStoreUserV1InnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.community.CommunityMemberPo;
import com.java110.po.menuGroupCommunity.MenuGroupCommunityPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述：保存
 * 服务编码：store.saveStore
 * 请求路劲：/app/store.SaveStore
 * add by 吴学文 at 2022-02-28 10:46:30 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "member.quit.community")
public class MemberQuitCommunityCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(MemberQuitCommunityCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IStoreAttrV1InnerServiceSMO storeAttrV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IStoreUserV1InnerServiceSMO storeUserV1InnerServiceSMOImpl;

    @Autowired
    private IOrgV1InnerServiceSMO orgV1InnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelV1InnerServiceSMO orgStaffRelV1InnerServiceSMOImpl;

    @Autowired
    private IWorkflowV1InnerServiceSMO workflowV1InnerServiceSMOImpl;

    @Autowired
    private IPrivilegeUserV1InnerServiceSMO privilegeUserV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private IMenuGroupV1InnerServiceSMO menuGroupV1InnerServiceSMOImpl;

    @Autowired
    private IMenuGroupCommunityV1InnerServiceSMO menuGroupCommunityV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Environment.isDevEnv();
        Assert.hasKeyAndValue(reqJson, "memberId", "请求报文中未包含商户");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区");


    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(reqJson.getString("memberId"));
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

        Assert.listOnlyOne(storeDtos, "商户不存在");

        String communityId = reqJson.getString("communityId");

        //释放小区
        CommunityMemberPo communityMemberPo = new CommunityMemberPo();
        communityMemberPo.setMemberId(storeDto.getStoreId());
        communityMemberPo.setCommunityId(communityId);
        int flag = communityMemberV1InnerServiceSMOImpl.deleteCommunityMember(communityMemberPo);
        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }


        MenuGroupCommunityPo menuGroupCommunityPo = new MenuGroupCommunityPo();
        menuGroupCommunityPo.setCommunityId(communityId);
        flag = menuGroupCommunityV1InnerServiceSMOImpl.deleteMenuGroupCommunity(menuGroupCommunityPo);
        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }


        cmdDataFlowContext.setResponseEntity(ResultVo.success());


    }

    private void saveMenuGroupCommunity(JSONObject reqJson, String communityId, String communityName) {
        List<MenuGroupDto> menuGroupDtos = null;
        MenuGroupDto menuGroupDto = null;
        if (!reqJson.containsKey("groupIds") || reqJson.getJSONArray("groupIds").size() < 1) {
            menuGroupDto = new MenuGroupDto();
            menuGroupDto.setStoreType(StoreDto.STORE_TYPE_PROPERTY);
            menuGroupDtos = menuGroupV1InnerServiceSMOImpl.queryMenuGroups(menuGroupDto);
        } else {
            menuGroupDto = new MenuGroupDto();
            JSONArray groupIds = reqJson.getJSONArray("groupIds");
            String groupId;
            List<String> gIds = new ArrayList<>();
            for (int groupIndex = 0; groupIndex < groupIds.size(); groupIndex++) {
                groupId = groupIds.getString(groupIndex);
                gIds.add(groupId);
            }
            menuGroupDto.setgIds(gIds.toArray(new String[gIds.size()]));
            menuGroupDtos = menuGroupV1InnerServiceSMOImpl.queryMenuGroups(menuGroupDto);
        }

        if (menuGroupDtos == null || menuGroupDtos.size() < 1) {
            throw new IllegalArgumentException("没有分配任何功能");
        }

        List<MenuGroupCommunityPo> menuGroupCommunityPos = new ArrayList<>();
        MenuGroupCommunityPo tmpMenuGroupCommunityPo = null;
        for (MenuGroupDto menuGroupDto1 : menuGroupDtos) {
            tmpMenuGroupCommunityPo = new MenuGroupCommunityPo();
            tmpMenuGroupCommunityPo.setCommunityId(communityId);
            tmpMenuGroupCommunityPo.setCommunityName(communityName);
            tmpMenuGroupCommunityPo.setGcId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            tmpMenuGroupCommunityPo.setgId(menuGroupDto1.getgId());
            tmpMenuGroupCommunityPo.setName(menuGroupDto1.getName());
            menuGroupCommunityPos.add(tmpMenuGroupCommunityPo);
        }
        int flag = menuGroupCommunityV1InnerServiceSMOImpl.saveMenuGroupCommunitys(menuGroupCommunityPos);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }
    }

}
