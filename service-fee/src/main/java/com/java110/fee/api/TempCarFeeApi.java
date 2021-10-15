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
package com.java110.fee.api;

import com.java110.dto.tempCarFeeConfig.TempCarFeeRuleDto;
import com.java110.dto.tempCarFeeConfig.TempCarPayOrderDto;
import com.java110.fee.bmo.tempCarFee.IGetTempCarFeeRules;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 临时车费接口类
 *
 * @desc add by 吴学文 14:53
 */
@RestController
@RequestMapping(value = "/tempCarFee")
public class TempCarFeeApi {

    @Autowired
    private IGetTempCarFeeRules getTempCarFeeRulesImpl;

    /**
     * 查询；临时车停车收费规则
     *
     * @param ruleId 规则ID
     * @return
     * @serviceCode /tempCarFee/queryTempCarFeeRules
     * @path /app/tempCarFee/queryTempCarFeeRules
     */
    @RequestMapping(value = "/queryTempCarFeeRules", method = RequestMethod.GET)
    public ResponseEntity<String> queryTempCarFeeRules(
            @RequestParam(value = "ruleId", required = false) String ruleId) {
        TempCarFeeRuleDto tempCarFeeRuleDto = new TempCarFeeRuleDto();
        tempCarFeeRuleDto.setRuleId(ruleId);
        return getTempCarFeeRulesImpl.queryRules(tempCarFeeRuleDto);
    }


//    /**
//     * 查询；临时车停车收费规则
//     *
//     * @param paId 停车场ID
//     * @return
//     * @serviceCode /tempCarFee/getTempCarFeeOrder
//     * @path /app/tempCarFee/getTempCarFeeOrder
//     */
//    @RequestMapping(value = "/getTempCarFeeOrder", method = RequestMethod.GET)
//    public ResponseEntity<String> getTempCarFeeOrder(
//            @RequestParam(value = "paId", required = false) String paId,
//            @RequestParam(value = "carNum", required = false) String carNum
//    ) {
//        TempCarPayOrderDto tempCarPayOrderDto = new TempCarPayOrderDto();
//        tempCarPayOrderDto.setPaId(paId);
//        tempCarPayOrderDto.setCarNum(carNum);
//        return getTempCarFeeRulesImpl.getTempCarFeeOrder(tempCarPayOrderDto);
//    }

    /**
     * 缴费通知
     *
     * @param reqParam 停车场ID
     * @return
     * @serviceCode /tempCarFee/notifyTempCarFeeOrder
     * @path /app/tempCarFee/notifyTempCarFeeOrder
     */
    @RequestMapping(value = "/notifyTempCarFeeOrder", method = RequestMethod.POST)
    public ResponseEntity<String> notifyTempCarFeeOrder(@RequestBody String reqParam) {
        TempCarPayOrderDto tempCarPayOrderDto = BeanConvertUtil.covertBean(reqParam, TempCarPayOrderDto.class);
        return getTempCarFeeRulesImpl.notifyTempCarFeeOrder(tempCarPayOrderDto);
    }
}
