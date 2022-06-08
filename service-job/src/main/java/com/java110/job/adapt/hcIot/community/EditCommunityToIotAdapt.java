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
package com.java110.job.adapt.hcIot.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.entity.order.Business;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.po.community.CommunityPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 编辑小区小区时同步
 * HC 小区信息 同步HC物联网系统
 * <p>
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "editCommunityToIotAdapt")
public class EditCommunityToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn iotSendAsynImpl;


    /**
     * 添加小区执行类
     * {
     * "name": "HC小区",
     * "address": "青海省西宁市",
     * "cityCode": "510104",
     * "extCommunityId": "702020042194860039"
     * }
     *
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray  businessCommunitys = new JSONArray();
        if (data.containsKey(CommunityPo.class.getSimpleName())) {
            Object bObj = data.get(CommunityPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessCommunitys.add(bObj);
            } else if (bObj instanceof List) {
                businessCommunitys = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessCommunitys = (JSONArray) bObj;
            }
        }else {
            if (data instanceof JSONObject) {
                businessCommunitys.add(data);
            }
        }
        for (int bMachineIndex = 0; bMachineIndex < businessCommunitys.size(); bMachineIndex++) {
            JSONObject businessCommunity = businessCommunitys.getJSONObject(bMachineIndex);
            doEditCommunity(business, businessCommunity);
        }
    }

    /**
     * 修改小区信息
     *
     * @param business
     * @param businessCommunity
     */
    private void doEditCommunity(Business business, JSONObject businessCommunity) {
        CommunityPo communityPo = BeanConvertUtil.covertBean(businessCommunity, CommunityPo.class);
        JSONObject postParameters = new JSONObject();
        postParameters.put("name", communityPo.getName());
        postParameters.put("address", communityPo.getAddress());
        postParameters.put("cityCode", communityPo.getCityCode());
        postParameters.put("extCommunityId", communityPo.getCommunityId());
        iotSendAsynImpl.editCommunity(postParameters);
    }


}
