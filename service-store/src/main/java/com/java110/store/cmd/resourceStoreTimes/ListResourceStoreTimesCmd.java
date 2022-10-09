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
package com.java110.store.cmd.resourceStoreTimes;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.intf.store.IResourceStoreTimesV1InnerServiceSMO;
import com.java110.po.resourceStoreTimes.ResourceStoreTimesPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.resourceStoreTimes.ResourceStoreTimesDto;
import java.util.List;
import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 查询物品进货明细 功能
 */

@Java110CmdDoc(title = "查询物品进货明细",
        description = "主要用于 查询物品进货明细",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/resourceStoreTimes.listResourceStoreTimes",
        resource = "storeDoc",
        author = "吴学文",
        serviceCode = "resourceStoreTimes.listResourceStoreTimes"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "resCode", length = 30, remark = "物品编号"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Array", length = 0, remark = "数据"),
                @Java110ParamDoc(parentNodeName = "data",name = "resCode", type = "String", length = 30,  remark = "物品编号"),
                @Java110ParamDoc(parentNodeName = "data",name = "price", type = "String", length = 30,  remark = "价格"),
                @Java110ParamDoc(parentNodeName = "data",name = "stock", type = "String", length = 30,  remark = "库存"),
        }
)

@Java110ExampleDoc(
        reqBody="http://{ip}:{port}/app/resourceStoreTimes.listResourceStoreTimes?resCode=123123",
        resBody="{'code':0,'msg':'成功','data':[{'resCode':'123','price':'1.2','stock':'12'}]}"
)

/**
 * 类表述：查询物品进货明细
 * 服务编码：resourceStoreTimes.listResourceStoreTimes
 * 请求路劲：/app/resourceStoreTimes.ListResourceStoreTimes
 * add by 吴学文 at 2022-10-06 21:26:56 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "resourceStoreTimes.listResourceStoreTimes")
public class ListResourceStoreTimesCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(ListResourceStoreTimesCmd.class);
    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

           ResourceStoreTimesDto resourceStoreTimesDto = BeanConvertUtil.covertBean(reqJson, ResourceStoreTimesDto.class);

           String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");

           resourceStoreTimesDto.setStoreId(storeId);

           int count = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimessCount(resourceStoreTimesDto);

           List<ResourceStoreTimesDto> resourceStoreTimesDtos = null;

           if (count > 0) {
               resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);
           } else {
               resourceStoreTimesDtos = new ArrayList<>();
           }

           ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, resourceStoreTimesDtos);

           ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

           cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
