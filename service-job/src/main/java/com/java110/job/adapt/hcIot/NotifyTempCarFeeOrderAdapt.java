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
package com.java110.job.adapt.hcIot;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.tempCarFeeConfig.TempCarPayOrderDto;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

/**
 * 查询临时停车费订单
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 15:29
 */
@Component(value = "notifyTempCarFeeOrderAdapt")
public class NotifyTempCarFeeOrderAdapt extends DatabusAdaptImpl {

    @Autowired
    RestTemplate outRestTemplate;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    /**
     * 获取历史停车费订单
     *
     * @param tempCarPayOrderDto 业务信息
     * @return
     */
    @Override
    public ResultVo notifyTempCarFeeOrder(TempCarPayOrderDto tempCarPayOrderDto) {

        JSONObject postParameters = new JSONObject();
        postParameters.put("taskId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        postParameters.put("carNum", tempCarPayOrderDto.getCarNum());
        postParameters.put("extPaId", tempCarPayOrderDto.getPaId());
        postParameters.put("orderId", tempCarPayOrderDto.getOrderId());
        postParameters.put("amount", tempCarPayOrderDto.getAmount());
        postParameters.put("payCharge", tempCarPayOrderDto.getPayCharge());
        postParameters.put("payTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        postParameters.put("payType", tempCarPayOrderDto.getPayType());
        postParameters.put("extPccIds",tempCarPayOrderDto.getPccIds());
        if(!StringUtil.isEmpty(tempCarPayOrderDto.getMachineId())) {
            postParameters.put("extMachineId", tempCarPayOrderDto.getMachineId());
        }

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters.toJSONString(), getHeaders(outRestTemplate));
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(IotConstant.getUrl(IotConstant.NOTIFY_TEMP_CAR_FEE_ORDER), HttpMethod.POST, httpEntity, String.class);
        System.out.println("缴费通知"+IotConstant.getUrl(IotConstant.NOTIFY_TEMP_CAR_FEE_ORDER));
        System.out.println("请求报文"+postParameters.toJSONString());
        System.out.println("返回报文"+responseEntity);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return new ResultVo(ResultVo.CODE_ERROR, responseEntity.getBody());
        }
        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        return new ResultVo(paramOut.getInteger("code"), paramOut.getString("msg"), paramOut.getJSONObject("data"));

    }

}
