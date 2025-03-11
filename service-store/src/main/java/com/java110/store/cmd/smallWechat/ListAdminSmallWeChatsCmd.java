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
package com.java110.store.cmd.smallWechat;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.smallWeChat.ApiSmallWeChatDataVo;
import com.java110.vo.api.smallWeChat.ApiSmallWeChatVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 类表述：查询
 * 服务编码：smallWechat.listSmallWechat
 * 请求路劲：/app/smallWechat.ListSmallWechat
 * add by 吴学文 at 2022-05-25 10:46:07 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "smallWeChat.listAdminSmallWeChats")
public class ListAdminSmallWeChatsCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListAdminSmallWeChatsCmd.class);
    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        super.validateAdmin(cmdDataFlowContext);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        SmallWeChatDto smallWeChatDto = BeanConvertUtil.covertBean(reqJson, SmallWeChatDto.class);
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        if (StringUtil.jsonHasKayAndValue(reqJson, "communityId")) {
            smallWeChatDto.setObjId(reqJson.getString("communityId"));
        }
        //smallWeChatDto.setWeChatType(reqJson.getString("wechatType"));
        int count = smallWechatV1InnerServiceSMOImpl.querySmallWechatsCount(smallWeChatDto);
        List<SmallWeChatDto> smallWeChats = null;
        if (count > 0) {
            smallWeChats = BeanConvertUtil.covertBeanList(smallWechatV1InnerServiceSMOImpl.querySmallWechats(smallWeChatDto), SmallWeChatDto.class);
            //freshSecure(smallWeChats, appId);
        } else {
            smallWeChats = new ArrayList<>();
        }

        refreshCommunityName(smallWeChats);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, smallWeChats);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void refreshCommunityName(List<SmallWeChatDto> smallWeChats) {

        if(ListUtil.isNull(smallWeChats)){
            return;
        }

        List<String> communityIds = new ArrayList<>();
        for (SmallWeChatDto smallWeChatDto : smallWeChats) {
            smallWeChatDto.setCommunityId(smallWeChatDto.getObjId());
            communityIds.add(smallWeChatDto.getObjId());
        }

        if(ListUtil.isNull(communityIds)){
            return ;
        }
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityIds(communityIds.toArray(new String[communityIds.size()]));
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);
        if(ListUtil.isNull(communityDtos)){
            return;
        }
        for (SmallWeChatDto smallWeChatDto : smallWeChats) {
            for (CommunityDto tCommunityDto : communityDtos) {
                if (!smallWeChatDto.getObjId().equals(tCommunityDto.getCommunityId())) {
                    continue;
                }
                smallWeChatDto.setCommunityName(tCommunityDto.getName());
            }
        }
    }


}
