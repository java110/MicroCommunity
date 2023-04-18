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
package com.java110.community.cmd.communityVenue;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.intf.community.ICommunityVenueV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.community.CommunityVenueDto;
import java.util.List;
import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Java110CmdDoc(title = "查询场馆",
        description = "查询系统中的查询场馆",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/communityVenue.ListCommunityVenue",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "communityVenue.ListCommunityVenue"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "page", type = "int", length = 11, remark = "分页页数"),
        @Java110ParamDoc(name = "row", type = "int", length = 11, remark = "分页行数"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Array", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data", name = "communityId", type = "String", remark = "小区ID"),
                @Java110ParamDoc(parentNodeName = "data", name = "name", type = "String", remark = "场地名称"),
                @Java110ParamDoc(parentNodeName = "data", name = "venueId", type = "String", remark = "场馆编号"),
        }
)

@Java110ExampleDoc(
        reqBody = "http://{ip}:{port}/app/communityVenue.ListCommunityVenue?communityId=2022081539020475&page=1&row=10",
        resBody = "{\"code\":0,\"data\":[{\"name\":\"无需文\",\"communityId\":\"2022081539020475\",\"venueId\":\"102022093043260007\"}],\"msg\":\"成功\",\"page\":0,\"records\":1,\"rows\":0,\"total\":2}"
)

/**
 * 类表述：查询
 * 服务编码：communityVenue.listCommunityVenue
 * 请求路劲：/app/communityVenue.ListCommunityVenue
 * add by 吴学文 at 2022-10-03 03:05:16 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "communityVenue.listCommunityVenue")
public class ListCommunityVenueCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(ListCommunityVenueCmd.class);
    @Autowired
    private ICommunityVenueV1InnerServiceSMO communityVenueV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);

        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

           CommunityVenueDto communityVenueDto = BeanConvertUtil.covertBean(reqJson, CommunityVenueDto.class);

           int count = communityVenueV1InnerServiceSMOImpl.queryCommunityVenuesCount(communityVenueDto);

           List<CommunityVenueDto> communityVenueDtos = null;

           if (count > 0) {
               communityVenueDtos = communityVenueV1InnerServiceSMOImpl.queryCommunityVenues(communityVenueDto);
           } else {
               communityVenueDtos = new ArrayList<>();
           }

           ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, communityVenueDtos);

           ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

           cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
