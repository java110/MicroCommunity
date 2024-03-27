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
package com.java110.fee.cmd.returnPayFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeAccountDetailDto;
import com.java110.dto.onlinePayRefund.OnlinePayRefundDto;
import com.java110.dto.payFee.PayFeeDetailDiscountDto;
import com.java110.intf.acct.IOnlinePayRefundV1InnerServiceSMO;
import com.java110.intf.fee.IFeeAccountDetailServiceSMO;
import com.java110.intf.fee.IPayFeeDetailDiscountInnerServiceSMO;
import com.java110.intf.fee.IReturnPayFeeInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.returnPayFee.ApiReturnPayFeeDataVo;
import com.java110.vo.api.returnPayFee.ApiReturnPayFeeVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.payFee.ReturnPayFeeDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：查询
 * 服务编码：returnPayFee.listReturnPayFee
 * 请求路劲：/app/returnPayFee.ListReturnPayFee
 * add by 吴学文 at 2022-02-21 12:20:03 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "returnPayFee.listReturnPayFees")
public class ListReturnPayFeesCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListReturnPayFeesCmd.class);

    @Autowired
    private IReturnPayFeeInnerServiceSMO returnPayFeeInnerServiceSMOImpl;

    @Autowired
    private IFeeAccountDetailServiceSMO feeAccountDetailServiceSMOImpl;

    @Autowired
    private IPayFeeDetailDiscountInnerServiceSMO payFeeDetailDiscountInnerServiceSMOImpl;

    @Autowired
    private IOnlinePayRefundV1InnerServiceSMO onlinePayRefundV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ReturnPayFeeDto returnPayFeeDto = BeanConvertUtil.covertBean(reqJson, ReturnPayFeeDto.class);

        int count = returnPayFeeInnerServiceSMOImpl.queryReturnPayFeesCount(returnPayFeeDto);

        List<ReturnPayFeeDto> returnPayFees;

        if (count > 0) {
            //注意这里处理 记得测试退费逻辑
            returnPayFees = returnPayFeeInnerServiceSMOImpl.queryReturnPayFees(returnPayFeeDto);
            for (ReturnPayFeeDto returnPayFee : returnPayFees) {
                FeeAccountDetailDto feeAccountDetailDto = new FeeAccountDetailDto();
                feeAccountDetailDto.setDetailId(returnPayFee.getDetailId());
                List<FeeAccountDetailDto> feeAccountDetailDtos = feeAccountDetailServiceSMOImpl.queryFeeAccountDetails(feeAccountDetailDto);
                if (!ListUtil.isNull(feeAccountDetailDtos)) {
                    returnPayFee.setFeeAccountDetailDtoList(feeAccountDetailDtos);
                }
                PayFeeDetailDiscountDto payFeeDetailDiscountDto = new PayFeeDetailDiscountDto();
                payFeeDetailDiscountDto.setDetailId(returnPayFee.getDetailId());
                List<PayFeeDetailDiscountDto> payFeeDetailDiscountDtos = payFeeDetailDiscountInnerServiceSMOImpl.queryPayFeeDetailDiscounts(payFeeDetailDiscountDto);
                if (!ListUtil.isNull(payFeeDetailDiscountDtos)) {
                    returnPayFee.setPayFeeDetailDiscountDtoList(payFeeDetailDiscountDtos);
                }
            }
        } else {
            returnPayFees = new ArrayList<>();
        }
        //todo 查询退款 支付系统返回内容
        computeOnlinePayRefundRemark(returnPayFees);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, returnPayFees);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void computeOnlinePayRefundRemark(List<ReturnPayFeeDto> returnPayFees) {

        if (ListUtil.isNull(returnPayFees)) {
            return;
        }

        List<String> detailIds = new ArrayList<>();
        for (ReturnPayFeeDto returnPayFeeDto : returnPayFees) {
            detailIds.add(returnPayFeeDto.getDetailId());
        }

        OnlinePayRefundDto onlinePayRefundDto = new OnlinePayRefundDto();
        onlinePayRefundDto.setBusiIds(detailIds.toArray(new String[detailIds.size()]));
        List<OnlinePayRefundDto> onlinePayRefundDtos = onlinePayRefundV1InnerServiceSMOImpl.queryOnlinePayRefunds(onlinePayRefundDto);

        if (ListUtil.isNull(onlinePayRefundDtos)) {
            return;
        }

        for (ReturnPayFeeDto returnPayFeeDto : returnPayFees) {
            for(OnlinePayRefundDto tmpOnlinePayRefundDto:onlinePayRefundDtos){
                if(returnPayFeeDto.getDetailId().equals(tmpOnlinePayRefundDto.getBusiId())){
                    returnPayFeeDto.setRefundState(tmpOnlinePayRefundDto.getState());
                    returnPayFeeDto.setRefundRemark(tmpOnlinePayRefundDto.getMessage());
                }
            }
        }
    }
}
