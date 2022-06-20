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
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.activities.ActivitiesDto;
import com.java110.intf.community.IActivitiesInnerServiceSMO;
import com.java110.intf.community.IActivitiesV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.activities.ApiActivitiesDataVo;
import com.java110.vo.api.activities.ApiActivitiesVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 类表述：查询
 * 服务编码：activities.listActivities
 * 请求路劲：/app/activities.ListActivities
 * add by 吴学文 at 2022-06-19 10:49:17 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "activities.listActivitiess")
public class ListActivitiessCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListActivitiessCmd.class);
    @Autowired
    private IActivitiesV1InnerServiceSMO activitiesV1InnerServiceSMOImpl;
    @Autowired
    private IActivitiesInnerServiceSMO activitiesInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {


        ActivitiesDto activitiesDto = BeanConvertUtil.covertBean(reqJson, ActivitiesDto.class);
        if (!StringUtil.isEmpty("clientType") && "H5".equals(reqJson.get("clientType"))) {
            Date day = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            activitiesDto.setStartTime(df.format(day));
            activitiesDto.setEndTime(df.format(day));
        }

        int count = activitiesInnerServiceSMOImpl.queryActivitiessCount(activitiesDto);

        List<ApiActivitiesDataVo> activitiess = null;

        if (count > 0) {
            activitiess = BeanConvertUtil.covertBeanList(activitiesInnerServiceSMOImpl.queryActivitiess(activitiesDto), ApiActivitiesDataVo.class);
        } else {
            activitiess = new ArrayList<>();
        }

        ApiActivitiesVo apiActivitiesVo = new ApiActivitiesVo();

        apiActivitiesVo.setTotal(count);
        apiActivitiesVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiActivitiesVo.setActivitiess(activitiess);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiActivitiesVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
