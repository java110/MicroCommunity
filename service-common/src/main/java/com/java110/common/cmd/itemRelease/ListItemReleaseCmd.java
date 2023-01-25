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
package com.java110.common.cmd.itemRelease;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.intf.common.IItemReleaseResV1InnerServiceSMO;
import com.java110.intf.common.IItemReleaseV1InnerServiceSMO;
import com.java110.po.itemRelease.ItemReleasePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.itemRelease.ItemReleaseDto;
import java.util.List;
import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Java110CmdDoc(title = "查询物品放行",
        description = "查询物品放行，供物业端使用",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/itemRelease.listItemRelease",
        resource = "commonDoc",
        author = "吴学文",
        serviceCode = "itemRelease.listItemRelease"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "page", type = "int",length = 11, remark = "页数"),
        @Java110ParamDoc(name = "row", type = "int",length = 11, remark = "行数"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "放行小区"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Array", length = -1, defaultValue = "成功", remark = "数据"),
                @Java110ParamDoc(parentNodeName = "data", name = "amount", type = "Array", length = -1,  remark = "数量"),

        }
)

@Java110ExampleDoc(
        reqBody="{'typeId':'123','communityId':'123'}",
        resBody="{'code':0,'msg':'成功'}"
)
/**
 * 类表述：查询
 * 服务编码：itemRelease.listItemRelease
 * 请求路劲：/app/itemRelease.ListItemRelease
 * add by 吴学文 at 2023-01-11 15:40:01 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "itemRelease.listItemRelease")
public class ListItemReleaseCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(ListItemReleaseCmd.class);
    @Autowired
    private IItemReleaseV1InnerServiceSMO itemReleaseV1InnerServiceSMOImpl;

    @Autowired
    private IItemReleaseResV1InnerServiceSMO itemReleaseResV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

           ItemReleaseDto itemReleaseDto = BeanConvertUtil.covertBean(reqJson, ItemReleaseDto.class);

           int count = itemReleaseV1InnerServiceSMOImpl.queryItemReleasesCount(itemReleaseDto);

           List<ItemReleaseDto> itemReleaseDtos = null;

           if (count > 0) {
               itemReleaseDtos = itemReleaseV1InnerServiceSMOImpl.queryItemReleases(itemReleaseDto);

           } else {
               itemReleaseDtos = new ArrayList<>();
           }

           ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, itemReleaseDtos);

           ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

           cmdDataFlowContext.setResponseEntity(responseEntity);
    }


}
