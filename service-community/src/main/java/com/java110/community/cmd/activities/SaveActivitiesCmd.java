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
package com.java110.community.cmd.activities;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.activities.ActivitiesTypeDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.roleCommunity.RoleCommunityDto;
import com.java110.dto.store.StoreDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IActivitiesTypeInnerServiceSMO;
import com.java110.intf.community.IActivitiesV1InnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.activities.ActivitiesPo;
import com.java110.po.file.FileRelPo;
import com.java110.utils.constant.StateConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.community.ApiCommunityDataVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述：保存
 * 服务编码：activities.saveActivities
 * 请求路劲：/app/activities.SaveActivities
 * add by 吴学文 at 2022-06-19 10:49:17 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "activities.saveActivities")
public class SaveActivitiesCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveActivitiesCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IActivitiesV1InnerServiceSMO activitiesV1InnerServiceSMOImpl;

    @Autowired
    private IActivitiesTypeInnerServiceSMO activitiesTypeInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IRoleCommunityV1InnerServiceSMO roleCommunityV1InnerServiceSMOImpl;

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "title", "必填，请填写业活动标题");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择活动类型");
        Assert.hasKeyAndValue(reqJson, "headerImg", "必填，请选择头部照片");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写活动内容");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String userId = cmdDataFlowContext.getReqHeaders().get("user-id");
        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setPage(1);
        userDto.setRow(1);
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");
        reqJson.put("userId", userDtos.get(0).getUserId());
        reqJson.put("userName", userDtos.get(0).getName());

        if (!reqJson.containsKey("isMoreCommunity") || "N".equals(reqJson.getString("isMoreCommunity"))) {
            addActivities(cmdDataFlowContext, reqJson);
            return;
        }

        reqJson.put("storeId", storeId);
        List<ApiCommunityDataVo> communityDataVos = getCommunitys(reqJson);

        if (communityDataVos == null || communityDataVos.size() < 1) {
            return;
        }

        ActivitiesTypeDto activitiesTypeDto = new ActivitiesTypeDto();
        activitiesTypeDto.setCommunityId(reqJson.getString("communityId"));
        activitiesTypeDto.setTypeCd(reqJson.getString("typeCd"));
        List<ActivitiesTypeDto> oneActivitiesTypeDtos = activitiesTypeInnerServiceSMOImpl.queryActivitiesTypes(activitiesTypeDto);

        Assert.listOnlyOne(oneActivitiesTypeDtos, "通知类型不存在");
        List<ActivitiesTypeDto> activitiesTypeDtos = null;
        for (ApiCommunityDataVo apiCommunityDataVo : communityDataVos) {
            reqJson.put("communityId", apiCommunityDataVo.getCommunityId());
            activitiesTypeDto = new ActivitiesTypeDto();
            activitiesTypeDto.setCommunityId(reqJson.getString("communityId"));
            activitiesTypeDto.setTypeName(oneActivitiesTypeDtos.get(0).getTypeName());
            activitiesTypeDtos = activitiesTypeInnerServiceSMOImpl.queryActivitiesTypes(activitiesTypeDto);
            if (activitiesTypeDtos == null || activitiesTypeDtos.size() < 1) {
                continue;
            }
            reqJson.put("typeCd", activitiesTypeDtos.get(0).getTypeCd());
            addActivities(cmdDataFlowContext, reqJson);
        }
    }

    public List<ApiCommunityDataVo> getCommunitys(JSONObject reqJson) {
        //1.0 先查询 员工对应的部门
        List<ApiCommunityDataVo> communitys = null;
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
        if (UserDto.LEVEL_CD_ADMIN.equals(userDtos.get(0).getLevelCd())) {
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
        } else {
            RoleCommunityDto orgCommunityDto = BeanConvertUtil.covertBean(reqJson, RoleCommunityDto.class);
            orgCommunityDto.setStaffId(userDtos.get(0).getStaffId());
            count = roleCommunityV1InnerServiceSMOImpl.queryRoleCommunitysCount(orgCommunityDto);
            if (count > 0) {
                List<RoleCommunityDto> roleCommunityDtos = roleCommunityV1InnerServiceSMOImpl.queryRoleCommunitys(orgCommunityDto);
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
        return communitys;
    }

    public void addActivities(ICmdDataFlowContext context, JSONObject reqJson) {
        reqJson.put("activitiesId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_activitiesId));
        if (reqJson.containsKey("headerImg") && !StringUtils.isEmpty(reqJson.getString("headerImg"))) {
            FileRelPo fileRelPo = new FileRelPo();
            fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
            fileRelPo.setFileRealName(reqJson.getString("headerImg"));
            fileRelPo.setFileSaveName(reqJson.getString("headerImg"));
            fileRelPo.setObjId(reqJson.getString("activitiesId"));
            fileRelPo.setSaveWay("table");
            fileRelPo.setRelTypeCd("70000");
            int flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            if (flag < 1) {
                throw new CmdException("保存广告失败");
            }
        }
        ActivitiesPo activitiesPo = BeanConvertUtil.covertBean(reqJson, ActivitiesPo.class);
        activitiesPo.setReadCount("0");
        activitiesPo.setLikeCount("0");
        activitiesPo.setCollectCount("0");
        activitiesPo.setState("11000");
        int flag = activitiesV1InnerServiceSMOImpl.saveActivities(activitiesPo);
        if (flag < 1) {
            throw new CmdException("保存广告失败");
        }
    }
}
