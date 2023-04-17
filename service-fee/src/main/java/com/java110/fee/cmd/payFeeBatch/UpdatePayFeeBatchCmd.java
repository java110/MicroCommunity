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
package com.java110.fee.cmd.payFeeBatch;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.meterWater.MeterWaterDto;
import com.java110.dto.payFeeBatch.PayFeeBatchDto;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IMeterWaterV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.java110.po.fee.PayFeePo;
import com.java110.po.payFeeBatch.PayFeeBatchPo;
import com.java110.utils.constant.StateConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 类表述：更新
 * 服务编码：payFeeBatch.updatePayFeeBatch
 * 请求路劲：/app/payFeeBatch.UpdatePayFeeBatch
 * add by 吴学文 at 2021-09-22 18:00:14 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "payFeeBatch.updatePayFeeBatch")
public class UpdatePayFeeBatchCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdatePayFeeBatchCmd.class);


    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IMeterWaterV1InnerServiceSMO meterWaterV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "batchId", "batchId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
        Assert.hasKeyAndValue(reqJson, "state", "状态不能为空");
        Assert.hasKeyAndValue(reqJson, "msg", "消息不能为空");
    }

    @Override
    //@Java110Transactional
    //这里不开启事务，删除可能几千条 事务压力太大

    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        PayFeeBatchPo payFeeBatchPo = BeanConvertUtil.covertBean(reqJson, PayFeeBatchPo.class);
        if (StateConstant.AGREE_AUDIT.equals(reqJson.getString("state"))) {
            payFeeBatchPo.setState(PayFeeBatchDto.STATE_SUCCESS);
        } else {
            payFeeBatchPo.setState(PayFeeBatchDto.STATE_FAIL);
        }
        int flag = payFeeBatchV1InnerServiceSMOImpl.updatePayFeeBatch(payFeeBatchPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        if (PayFeeBatchDto.STATE_FAIL.equals(payFeeBatchPo.getState())) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return;
        }

        PayFeePo feePo = new PayFeePo();
        feePo.setBatchId(payFeeBatchPo.getBatchId());
        feePo.setCommunityId(payFeeBatchPo.getCommunityId());
        feeInnerServiceSMOImpl.deleteFeesByBatch(feePo);

        //todo 删除抄表记录
        MeterWaterDto meterWaterDto = new MeterWaterDto();
        meterWaterDto.setCommunityId(payFeeBatchPo.getCommunityId());
        meterWaterDto.setBatchId(payFeeBatchPo.getBatchId());
        meterWaterV1InnerServiceSMOImpl.deleteMeterWaterByBatch(meterWaterDto);

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
