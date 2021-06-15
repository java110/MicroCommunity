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
package com.java110.fee.bmo.tempCarFee.impl;

import com.java110.dto.tempCarFeeConfig.TempCarFeeRuleDto;
import com.java110.dto.tempCarFeeConfig.TempCarPayOrderDto;
import com.java110.fee.bmo.tempCarFee.IGetTempCarFeeRules;
import com.java110.intf.fee.ITempCarFeeConfigInnerServiceSMO;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc add by 吴学文 15:03
 */
@Service
public class GetTempCarFeeRulesImpl implements IGetTempCarFeeRules {

    @Autowired
    private ITempCarFeeConfigInnerServiceSMO tempCarFeeConfigInnerServiceSMOImpl;

    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> queryRules(TempCarFeeRuleDto tempCarFeeRuleDto) {

        List<TempCarFeeRuleDto> tempCarFeeRuleDtos = tempCarFeeConfigInnerServiceSMOImpl.queryTempCarFeeRules(tempCarFeeRuleDto);

        return ResultVo.createResponseEntity(tempCarFeeRuleDtos);
    }

    /**
     * 查询是临时车支付订单
     *
     * @param tempCarPayOrderDto
     * @return
     */
    @Override
    public ResponseEntity<String> getTempCarFeeOrder(TempCarPayOrderDto tempCarPayOrderDto) {

        ResultVo resultVo = dataBusInnerServiceSMOImpl.getTempCarFeeOrder(tempCarPayOrderDto);
        return ResultVo.createResponseEntity(resultVo);
    }

    @Override
    public ResponseEntity<String> notifyTempCarFeeOrder(TempCarPayOrderDto tempCarPayOrderDto) {
        ResultVo resultVo = dataBusInnerServiceSMOImpl.notifyTempCarFeeOrder(tempCarPayOrderDto);
        return ResultVo.createResponseEntity(resultVo);
    }
}
