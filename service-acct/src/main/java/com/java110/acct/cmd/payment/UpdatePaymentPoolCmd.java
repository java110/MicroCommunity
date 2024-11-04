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
package com.java110.acct.cmd.payment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.payment.PaymentPoolDto;
import com.java110.intf.acct.IPaymentPoolConfigV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolValueV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.po.payment.PaymentPoolPo;
import com.java110.po.payment.PaymentPoolConfigPo;
import com.java110.po.payment.PaymentPoolValuePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 类表述：更新
 * 服务编码：paymentPool.updatePaymentPool
 * 请求路劲：/app/paymentPool.UpdatePaymentPool
 * add by 吴学文 at 2023-10-25 11:52:42 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "payment.updatePaymentPool")
public class UpdatePaymentPoolCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdatePaymentPoolCmd.class);


    @Autowired
    private IPaymentPoolV1InnerServiceSMO paymentPoolV1InnerServiceSMOImpl;

    @Autowired
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IPaymentPoolConfigV1InnerServiceSMO paymentPoolConfigV1InnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "ppId", "ppId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

        Assert.hasKey(reqJson, "paymentKeys", "未包含动态字段");

        JSONArray paymentKeys = reqJson.getJSONArray("paymentKeys");

        if (paymentKeys == null || paymentKeys.isEmpty()) {
            throw new CmdException("未包含字段");
        }
        JSONObject columns = null;
        // {"columnKey":"WECHAT_KEY","keyId":"2","name":"支付密码","page":-1,"paymentType":"WECHAT",
        // "records":0,"remark":"请输入微信v2支付秘钥","row":0,"statusCd":"0","total":0,"columnValue":"1"}
        for (int keyIndex = 0; keyIndex < paymentKeys.size(); keyIndex++) {
            columns = paymentKeys.getJSONObject(keyIndex);
            Assert.hasKeyAndValue(columns, "columnKey", "未包含key");
            Assert.hasKeyAndValue(columns, "columnValue", reqJson.getString("name") + "未填写值");
        }

        if (!PaymentPoolDto.PAY_TYPE_FEE_CONFIG.equals(reqJson.getString("payType"))) {
            return;
        }

        JSONArray configIds = reqJson.getJSONArray("configIds");
        if (configIds == null || configIds.isEmpty()) {
            throw new CmdException("未包含费用项");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        PaymentPoolPo paymentPoolPo = BeanConvertUtil.covertBean(reqJson, PaymentPoolPo.class);
        int flag = paymentPoolV1InnerServiceSMOImpl.updatePaymentPool(paymentPoolPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
        PaymentPoolValuePo paymentPoolValuePo = new PaymentPoolValuePo();
        paymentPoolValuePo.setPpId(paymentPoolPo.getPpId());
        paymentPoolValuePo.setCommunityId(paymentPoolPo.getCommunityId());
        paymentPoolValueV1InnerServiceSMOImpl.deletePaymentPoolValue(paymentPoolValuePo);

        //todo 动态字段
        JSONArray paymentKeys = reqJson.getJSONArray("paymentKeys");

        JSONObject columns = null;

        for (int keyIndex = 0; keyIndex < paymentKeys.size(); keyIndex++) {
            columns = paymentKeys.getJSONObject(keyIndex);
            paymentPoolValuePo = new PaymentPoolValuePo();
            paymentPoolValuePo.setColumnValue(columns.getString("columnValue"));
            paymentPoolValuePo.setColumnKey(columns.getString("columnKey"));
            paymentPoolValuePo.setCommunityId(paymentPoolPo.getCommunityId());
            paymentPoolValuePo.setPpId(paymentPoolPo.getPpId());
            paymentPoolValuePo.setValueId(GenerateCodeFactory.getGeneratorId("11"));
            paymentPoolValueV1InnerServiceSMOImpl.savePaymentPoolValue(paymentPoolValuePo);
        }

        // todo 保存关联关系

        if (!PaymentPoolDto.PAY_TYPE_FEE_CONFIG.equals(reqJson.getString("payType"))) {
            return;
        }
        JSONArray configIds = reqJson.getJSONArray("configIds");
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigIds(configIds.toArray(new String[configIds.size()]));
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);

        if (feeConfigDtos == null || feeConfigDtos.isEmpty()) {
            return;
        }


        PaymentPoolConfigPo paymentPoolConfigPo = new PaymentPoolConfigPo();
        paymentPoolConfigPo.setPpId(paymentPoolPo.getPpId());
        paymentPoolConfigPo.setCommunityId(paymentPoolPo.getCommunityId());
        paymentPoolConfigV1InnerServiceSMOImpl.deletePaymentPoolConfig(paymentPoolConfigPo);

        for (FeeConfigDto tmpFeeConfigDto : feeConfigDtos) {

            paymentPoolConfigPo = new PaymentPoolConfigPo();
            paymentPoolConfigPo.setConfigId(tmpFeeConfigDto.getConfigId());
            paymentPoolConfigPo.setFeeName(tmpFeeConfigDto.getFeeName());
            paymentPoolConfigPo.setCommunityId(paymentPoolPo.getCommunityId());
            paymentPoolConfigPo.setPpId(paymentPoolPo.getPpId());
            paymentPoolConfigPo.setPpcId(GenerateCodeFactory.getGeneratorId("11"));
            paymentPoolConfigV1InnerServiceSMOImpl.savePaymentPoolConfig(paymentPoolConfigPo);
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
