package com.java110.fee.bmo.tempCarFee;/*
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

import com.java110.dto.tempCarFeeConfig.TempCarFeeRuleDto;
import com.java110.dto.tempCarFeeConfig.TempCarPayOrderDto;
import org.springframework.http.ResponseEntity;

/**
 * 查询临时车收费规则
 */
public interface IGetTempCarFeeRules {

    /**
     * 查询规则
     *
     * @param tempCarFeeRuleDto 收费规则 入参
     * @return 收费规则及规则规格
     */
    ResponseEntity<String> queryRules(TempCarFeeRuleDto tempCarFeeRuleDto);

    /**
     * 查询临时车支付订单
     *
     * @param tempCarPayOrderDto
     * @return
     */
    ResponseEntity<String> getTempCarFeeOrder(TempCarPayOrderDto tempCarPayOrderDto);

    ResponseEntity<String> notifyTempCarFeeOrder(TempCarPayOrderDto tempCarPayOrderDto);
}
