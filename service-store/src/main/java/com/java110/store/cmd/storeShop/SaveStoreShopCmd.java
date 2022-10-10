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
package com.java110.store.cmd.storeShop;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.store.IStoreShopV1InnerServiceSMO;
import com.java110.po.storeShop.StoreShopPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：保存
 * 服务编码：storeShop.saveStoreShop
 * 请求路劲：/app/storeShop.SaveStoreShop
 * add by 吴学文 at 2022-10-10 17:54:27 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "storeShop.saveStoreShop")
public class SaveStoreShopCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveStoreShopCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IStoreShopV1InnerServiceSMO storeShopV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "shopLogo", "请求报文中未包含shopLogo");
Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
Assert.hasKeyAndValue(reqJson, "shopName", "请求报文中未包含shopName");
Assert.hasKeyAndValue(reqJson, "shopDesc", "请求报文中未包含shopDesc");
Assert.hasKeyAndValue(reqJson, "mapX", "请求报文中未包含mapX");
Assert.hasKeyAndValue(reqJson, "mapY", "请求报文中未包含mapY");
Assert.hasKeyAndValue(reqJson, "sendAddress", "请求报文中未包含sendAddress");
Assert.hasKeyAndValue(reqJson, "returnAddress", "请求报文中未包含returnAddress");
Assert.hasKeyAndValue(reqJson, "returnPerson", "请求报文中未包含returnPerson");
Assert.hasKeyAndValue(reqJson, "returnLink", "请求报文中未包含returnLink");
Assert.hasKeyAndValue(reqJson, "statusCd", "请求报文中未包含statusCd");
Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
Assert.hasKeyAndValue(reqJson, "shopType", "请求报文中未包含shopType");
Assert.hasKeyAndValue(reqJson, "openType", "请求报文中未包含openType");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

       StoreShopPo storeShopPo = BeanConvertUtil.covertBean(reqJson, StoreShopPo.class);
        storeShopPo.setShopId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = storeShopV1InnerServiceSMOImpl.saveStoreShop(storeShopPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
