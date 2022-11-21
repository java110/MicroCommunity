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
package com.java110.acct.cmd.couponProperty;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.acct.ICouponPropertyPoolConfigV1InnerServiceSMO;
import com.java110.intf.acct.ICouponPropertyPoolV1InnerServiceSMO;
import com.java110.po.couponPropertyPool.CouponPropertyPoolPo;
import com.java110.po.couponPropertyPoolConfig.CouponPropertyPoolConfigPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 类表述：更新
 * 服务编码：couponProperty.updateCouponPropertyPool
 * 请求路劲：/app/couponProperty.UpdateCouponPropertyPool
 * add by 吴学文 at 2022-11-19 23:00:42 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "couponProperty.updateCouponPropertyPool")
public class UpdateCouponPropertyPoolCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateCouponPropertyPoolCmd.class);


    @Autowired
    private ICouponPropertyPoolV1InnerServiceSMO couponPropertyPoolV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyPoolConfigV1InnerServiceSMO couponPropertyPoolConfigV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "cppId", "cppId不能为空");

        if(!reqJson.containsKey("toTypes")){
            throw new CmdException("未包含用途");
        }

        JSONArray toTypes = reqJson.getJSONArray("toTypes");

        if(toTypes == null || toTypes.size()< 1){
            throw new CmdException("未包含用途");
        }

        JSONObject typeObj = null;
        for(int typeIndex = 0;typeIndex < toTypes.size(); typeIndex++){
            typeObj = toTypes.getJSONObject(typeIndex);
            if(!typeObj.containsKey("columnValue")){
                throw new CmdException(typeObj.getString("name")+"未填写值");
            }

            if(StringUtil.isEmpty(typeObj.getString("columnValue"))){
                throw new CmdException(typeObj.getString("name")+"未填写值");
            }
        }

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        CouponPropertyPoolPo couponPropertyPoolPo = BeanConvertUtil.covertBean(reqJson, CouponPropertyPoolPo.class);
        int flag = couponPropertyPoolV1InnerServiceSMOImpl.updateCouponPropertyPool(couponPropertyPoolPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
        CouponPropertyPoolConfigPo couponPropertyPoolConfigPo = null;
        couponPropertyPoolConfigPo = new CouponPropertyPoolConfigPo();
        couponPropertyPoolConfigPo.setCouponId(couponPropertyPoolPo.getCppId());

        couponPropertyPoolConfigV1InnerServiceSMOImpl.deleteCouponPropertyPoolConfig(couponPropertyPoolConfigPo);

        JSONArray toTypes = reqJson.getJSONArray("toTypes");

        JSONObject typeObj = null;

        for(int typeIndex = 0;typeIndex < toTypes.size(); typeIndex++){
            typeObj = toTypes.getJSONObject(typeIndex);
            couponPropertyPoolConfigPo = new CouponPropertyPoolConfigPo();
            couponPropertyPoolConfigPo.setColumnKey(typeObj.getString("columnKey"));
            couponPropertyPoolConfigPo.setColumnValue(typeObj.getString("columnValue"));
            couponPropertyPoolConfigPo.setConfigId(GenerateCodeFactory.getGeneratorId("11"));
            couponPropertyPoolConfigPo.setCouponId(couponPropertyPoolPo.getCppId());
            couponPropertyPoolConfigV1InnerServiceSMOImpl.saveCouponPropertyPoolConfig(couponPropertyPoolConfigPo);
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
