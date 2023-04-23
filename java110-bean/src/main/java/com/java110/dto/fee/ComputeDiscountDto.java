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
package com.java110.dto.fee;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.List;

/**
 * @desc add by 吴学文 9:53
 */
public class ComputeDiscountDto extends PageDto implements Serializable {

    private String discountId;

    private String discountType;

    private String discountName;

    private String ruleName;

    private String ruleId;

    //映射关系值
    private String value;

    private List<FeeDiscountSpecDto> feeDiscountSpecs;

    private double discountPrice;
    private String ardId;


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

    public List<FeeDiscountSpecDto> getFeeDiscountSpecs() {
        return feeDiscountSpecs;
    }

    public void setFeeDiscountSpecs(List<FeeDiscountSpecDto> feeDiscountSpecs) {
        this.feeDiscountSpecs = feeDiscountSpecs;
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

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getArdId() {
        return ardId;
    }

    public void setArdId(String ardId) {
        this.ardId = ardId;
    }
}
