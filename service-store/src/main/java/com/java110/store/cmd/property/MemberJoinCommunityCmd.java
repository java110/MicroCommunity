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
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.menu.MenuGroupDto;
import com.java110.dto.store.StoreDto;
import com.java110.dto.workflow.WorkflowDto;
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
import com.java110.po.workflow.WorkflowPo;
import com.java110.utils.constant.StateConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
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
@Java110Cmd(serviceCode = "member.join.community")
public class MemberJoinCommunityCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(MemberJoinCommunityCmd.class);

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
        Assert.hasKeyAndValue(reqJson, "memberId", "请求报文中未包含商户");

        // 判断是否包含了小区信息
        if (!reqJson.containsKey("communityIds") || reqJson.getJSONArray("communityIds").size() < 1) {
            throw new CmdException("未包含小区");
        }

        // 判断是否包含了小区信息
        if (!reqJson.containsKey("groupIds") || reqJson.getJSONArray("groupIds").size() < 1) {
            throw new CmdException("未包含小区开通功能");
        }

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(reqJson.getString("memberId"));
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

        Assert.listOnlyOne(storeDtos, "商户不存在");

        JSONArray communityIds = reqJson.getJSONArray("communityIds");

        String communityId;
        int flag = 0;
        for (int communityIndex = 0; communityIndex < communityIds.size(); communityIndex++) {
            communityId = communityIds.getString(communityIndex);
            CommunityMemberDto communityMemberDto = new CommunityMemberDto();
            communityMemberDto.setCommunityId(communityId);
            communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
            List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);

            if (communityMemberDtos != null && communityMemberDtos.size() > 0) {
                throw new CmdException("小区已经入驻" + communityId);
            }

            //查询小区是否存在
            CommunityDto communityDto = new CommunityDto();
            communityDto.setCommunityId(communityId);
            List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

            Assert.listOnlyOne(communityDtos, "小区不存在");

            Calendar endTime = Calendar.getInstance();
            endTime.add(Calendar.MONTH, Integer.parseInt(communityDtos.get(0).getPayFeeMonth()));
            CommunityMemberPo communityMemberPo = new CommunityMemberPo();
            communityMemberPo.setCommunityMemberId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            communityMemberPo.setEndTime(DateUtil.getFormatTimeString(endTime.getTime(), DateUtil.DATE_FORMATE_STRING_A));
            communityMemberPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            communityMemberPo.setAuditStatusCd(StateConstant.AGREE_AUDIT);
            communityMemberPo.setCommunityId(communityId);
            communityMemberPo.setMemberId(storeDtos.get(0).getStoreId());
            communityMemberPo.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
            flag = communityMemberV1InnerServiceSMOImpl.saveCommunityMember(communityMemberPo);
            if (flag < 1) {
                throw new CmdException("注册失败");
            }

            //保存小区开放功能

            saveMenuGroupCommunity(reqJson, communityId, communityDtos.get(0).getName());

            // 修改投诉建议
            flag = updateWorkflow(WorkflowDto.FLOW_TYPE_COMPLAINT, communityId, storeDtos.get(0).getStoreId());
            if (flag < 1) {
                continue;
            }

            // 修改物品领用
            flag = updateWorkflow(WorkflowDto.FLOW_TYPE_COLLECTION, communityId, storeDtos.get(0).getStoreId());
            if (flag < 1) {
                continue;
            }

            // 修改
            flag = updateWorkflow(WorkflowDto.FLOW_TYPE_ALLOCATION_STOREHOUSE_GO, communityId, storeDtos.get(0).getStoreId());
            if (flag < 1) {
                continue;
            }
        }


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

    /**
     * 刷入小区ID
     *
     * @param flowType 接口请求数据封装
     * @return 封装好的 data数据
     */
    public int updateWorkflow(String flowType, String communityId, String storeId) {
        WorkflowDto workflowDto = new WorkflowDto();
        workflowDto.setCommunityId(communityId);
        workflowDto.setFlowType(flowType);
        List<WorkflowDto> workflowDtos = workflowV1InnerServiceSMOImpl.queryWorkflows(workflowDto);

        if (workflowDtos == null || workflowDtos.size() < 1) {
            return 0;
        }
        WorkflowPo workflowPo = new WorkflowPo();
        workflowPo.setFlowId(workflowDtos.get(0).getFlowId());
        workflowPo.setCommunityId(communityId);
        workflowPo.setStoreId(storeId);
        return workflowV1InnerServiceSMOImpl.updateWorkflow(workflowPo);
    }
}
