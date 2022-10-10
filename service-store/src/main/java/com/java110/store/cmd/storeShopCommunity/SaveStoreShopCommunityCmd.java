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
package com.java110.store.cmd.storeShopCommunity;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.store.IStoreShopCommunityV1InnerServiceSMO;
import com.java110.po.storeShopCommunity.StoreShopCommunityPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：保存
 * 服务编码：storeShopCommunity.saveStoreShopCommunity
 * 请求路劲：/app/storeShopCommunity.SaveStoreShopCommunity
 * add by 吴学文 at 2022-10-11 01:12:22 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "storeShopCommunity.saveStoreShopCommunity")
public class SaveStoreShopCommunityCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveStoreShopCommunityCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IStoreShopCommunityV1InnerServiceSMO storeShopCommunityV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "shopId", "请求报文中未包含shopId");
Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
Assert.hasKeyAndValue(reqJson, "communityName", "请求报文中未包含communityName");
Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
Assert.hasKeyAndValue(reqJson, "statusCd", "请求报文中未包含statusCd");
Assert.hasKeyAndValue(reqJson, "address", "请求报文中未包含address");
Assert.hasKeyAndValue(reqJson, "cityCode", "请求报文中未包含cityCode");
Assert.hasKeyAndValue(reqJson, "codeName", "请求报文中未包含codeName");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

       StoreShopCommunityPo storeShopCommunityPo = BeanConvertUtil.covertBean(reqJson, StoreShopCommunityPo.class);
        storeShopCommunityPo.setScId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = storeShopCommunityV1InnerServiceSMOImpl.saveStoreShopCommunity(storeShopCommunityPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
