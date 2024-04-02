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
package com.java110.common.cmd.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.client.RestTemplate;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.system.SystemInfoDto;
import com.java110.intf.common.ISystemInfoV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;


/**
 * 类表述：查询
 * 服务编码：system.listSystemInfo
 * 请求路劲：/app/system.ListSystemInfo
 * add by 吴学文 at 2022-08-16 23:57:44 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "system.listSystemInfo")
public class ListSystemInfoCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListSystemInfoCmd.class);
    @Autowired
    private ISystemInfoV1InnerServiceSMO systemInfoV1InnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        SystemInfoDto systemInfoDto = BeanConvertUtil.covertBean(reqJson, SystemInfoDto.class);

        int count = systemInfoV1InnerServiceSMOImpl.querySystemInfosCount(systemInfoDto);

        List<SystemInfoDto> systemInfoDtos = null;

        if (count > 0) {
            systemInfoDtos = systemInfoV1InnerServiceSMOImpl.querySystemInfos(systemInfoDto);
            systemInfoDtos.get(0).setOwnerUrl(UrlCache.getOwnerUrl());
            systemInfoDtos.get(0).setPropertyUrl(UrlCache.getPropertyPhoneUrl());
        } else {
            systemInfoDtos = new ArrayList<>();
        }

        //todo 查询第三方开发的插件
        queryThirdPlugin(systemInfoDtos);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, systemInfoDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void queryThirdPlugin(List<SystemInfoDto> systemInfoDtos) {

        String pluginSwitch = MappingCache.getValue("PLUGIN", "PLUGIN_SWITCH");

        if (!"ON".equals(pluginSwitch)) {
            return;
        }

        if (ListUtil.isNull(systemInfoDtos)) {
            return;
        }

        try {
            JSONObject paramIn = new JSONObject();

            HttpHeaders header = new HttpHeaders();
            HttpEntity<String> httpEntity = new HttpEntity<String>(paramIn.toJSONString(), header);

            String pluginUrl = MappingCache.getValue("PLUGIN", "PLUGIN_URL") + "/plugin/plugin.queryValidPlugins";

            ResponseEntity<String> tokenRes = outRestTemplate.exchange(pluginUrl, HttpMethod.POST, httpEntity, String.class);

            String body = tokenRes.getBody();
            JSONObject paramOut = JSONObject.parseObject(body);

            systemInfoDtos.get(0).setPlugins(paramOut.getJSONArray("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
