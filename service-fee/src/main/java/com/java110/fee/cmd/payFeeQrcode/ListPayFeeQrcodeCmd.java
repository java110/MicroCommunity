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
package com.java110.fee.cmd.payFeeQrcode;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.fee.IPayFeeQrcodeV1InnerServiceSMO;
import com.java110.po.payFeeQrcode.PayFeeQrcodePo;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.CacheUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.payFeeQrcode.PayFeeQrcodeDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：payFeeQrcode.listPayFeeQrcode
 * 请求路劲：/app/payFeeQrcode.ListPayFeeQrcode
 * add by 吴学文 at 2023-09-04 23:24:48 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "payFeeQrcode.listPayFeeQrcode")
public class ListPayFeeQrcodeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListPayFeeQrcodeCmd.class);
    @Autowired
    private IPayFeeQrcodeV1InnerServiceSMO payFeeQrcodeV1InnerServiceSMOImpl;

    private static final String payQrCode = "/#/pages/fee/payQrCode";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        PayFeeQrcodeDto payFeeQrcodeDto = BeanConvertUtil.covertBean(reqJson, PayFeeQrcodeDto.class);

        int count = payFeeQrcodeV1InnerServiceSMOImpl.queryPayFeeQrcodesCount(payFeeQrcodeDto);

        List<PayFeeQrcodeDto> payFeeQrcodeDtos = null;

        if (count > 0) {
            payFeeQrcodeDtos = payFeeQrcodeV1InnerServiceSMOImpl.queryPayFeeQrcodes(payFeeQrcodeDto);
            // todo 计算 二维码地址
            computeQrCodeUrl(payFeeQrcodeDtos);
        } else {
            payFeeQrcodeDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, payFeeQrcodeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 计算 支付二维码地址
     *
     * @param payFeeQrcodeDtos
     */
    private void computeQrCodeUrl(List<PayFeeQrcodeDto> payFeeQrcodeDtos) {

        if (payFeeQrcodeDtos == null || payFeeQrcodeDtos.size() < 1) {
            return;
        }
        String ownerUrl = UrlCache.getOwnerUrl();

        String qrcodeUrl = "";
        for (PayFeeQrcodeDto payFeeQrcodeDto : payFeeQrcodeDtos) {
            qrcodeUrl = ownerUrl + payQrCode + "?communityId=" + payFeeQrcodeDto.getCommunityId() + "&pfqId=" + payFeeQrcodeDto.getPfqId();
            payFeeQrcodeDto.setQrCodeUrl(qrcodeUrl);
        }
    }
}
