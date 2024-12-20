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
package com.java110.store.cmd.resourceSupplier;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.store.IResourceSupplierV1InnerServiceSMO;
import com.java110.po.resource.ResourceSupplierPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：更新
 * 服务编码：resourceSupplier.updateResourceSupplier
 * 请求路劲：/app/resourceSupplier.UpdateResourceSupplier
 * add by 吴学文 at 2022-08-07 16:56:05 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "resourceSupplier.updateResourceSupplier")
public class UpdateResourceSupplierCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(UpdateResourceSupplierCmd.class);

    @Autowired
    private IResourceSupplierV1InnerServiceSMO resourceSupplierV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "rsId", "rsId不能为空");
        Assert.hasKeyAndValue(reqJson, "supplierName", "请求报文中未包含supplierName");
        Assert.hasKeyAndValue(reqJson, "address", "请求报文中未包含address");
        Assert.hasKeyAndValue(reqJson, "tel", "请求报文中未包含tel");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        String regex = "1[3-9]\\d{9}";
        String regex2 = "0\\d{2,3}-?[1-9]\\d{4,9}";
        String tel = reqJson.getString("tel"); //获取手机号
        if (!tel.matches(regex) && !tel.matches(regex2)) {
            throw new IllegalArgumentException("供应商联系方式格式不对！");
        }
        if (!reqJson.containsKey("storeId")) {
            String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");
            reqJson.put("storeId", storeId);
        }
        if (!reqJson.containsKey("userId")) {
            reqJson.put("userId", "-1");
        }
        if (!reqJson.containsKey("userName")) {
            reqJson.put("userName", "未知");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
       ResourceSupplierPo resourceSupplierPo = BeanConvertUtil.covertBean(reqJson, ResourceSupplierPo.class);
        int flag = resourceSupplierV1InnerServiceSMOImpl.updateResourceSupplier(resourceSupplierPo);
        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
