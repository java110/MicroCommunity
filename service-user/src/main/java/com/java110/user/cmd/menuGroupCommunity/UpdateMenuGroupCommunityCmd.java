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
package com.java110.user.cmd.menuGroupCommunity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.Environment;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.menu.MenuGroupDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.user.IMenuGroupCommunityV1InnerServiceSMO;
import com.java110.intf.user.IMenuGroupV1InnerServiceSMO;
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
 * 类表述：更新
 * 服务编码：menuGroupCommunity.updateMenuGroupCommunity
 * 请求路劲：/app/menuGroupCommunity.UpdateMenuGroupCommunity
 * add by 吴学文 at 2022-02-28 18:49:04 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "menuGroupCommunity.updateMenuGroupCommunity")
public class UpdateMenuGroupCommunityCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateMenuGroupCommunityCmd.class);


    @Autowired
    private IMenuGroupCommunityV1InnerServiceSMO menuGroupCommunityV1InnerServiceSMOImpl;

    @Autowired
    private IMenuGroupV1InnerServiceSMO menuGroupV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;
    public static final String CODE_PREFIX_ID = "10";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Environment.isDevEnv();
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");


        // 判断是否包含了小区信息
        if (!reqJson.containsKey("groupIds") || reqJson.getJSONArray("groupIds").size() < 1) {
            throw new CmdException("未包含小区开通功能");
        }

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {


        //查询小区是否存在
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(reqJson.getString("communityId"));
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos, "小区不存在");


        MenuGroupCommunityPo menuGroupCommunityPo = new MenuGroupCommunityPo();
        menuGroupCommunityPo.setCommunityId(reqJson.getString("communityId"));
        int flag = menuGroupCommunityV1InnerServiceSMOImpl.deleteMenuGroupCommunity(menuGroupCommunityPo);
        if (flag < 1) {
            //throw new CmdException("删除数据失败"); //有可能 已经上线的客户 没有 这个数据 所以不需判断
        }

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
            tmpMenuGroupCommunityPo.setCommunityId(reqJson.getString("communityId"));
            tmpMenuGroupCommunityPo.setCommunityName(communityDtos.get(0).getName());
            tmpMenuGroupCommunityPo.setGcId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            tmpMenuGroupCommunityPo.setgId(menuGroupDto1.getgId());
            tmpMenuGroupCommunityPo.setName(menuGroupDto1.getName());
            menuGroupCommunityPos.add(tmpMenuGroupCommunityPo);
        }
        flag = menuGroupCommunityV1InnerServiceSMOImpl.saveMenuGroupCommunitys(menuGroupCommunityPos);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
