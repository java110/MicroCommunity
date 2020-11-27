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
package com.java110.dto.feeDiscount;

import com.java110.dto.PageDto;
import com.java110.dto.feeDiscountRuleSpec.FeeDiscountRuleSpecDto;

import java.io.Serializable;
import java.util.List;

/**
 * @desc add by 吴学文 9:53
 */
public class ComputeDiscountDto extends PageDto implements Serializable {

    private String discountId;

    private String discountType;

    private String ruleName;

    private String ruleId;

    private List<FeeDiscountRuleSpecDto> feeDiscountRuleSpecs;

    private double discountPrice;


    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public List<FeeDiscountRuleSpecDto> getFeeDiscountRuleSpecs() {
        return feeDiscountRuleSpecs;
    }

    public void setFeeDiscountRuleSpecs(List<FeeDiscountRuleSpecDto> feeDiscountRuleSpecs) {
        this.feeDiscountRuleSpecs = feeDiscountRuleSpecs;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
}
