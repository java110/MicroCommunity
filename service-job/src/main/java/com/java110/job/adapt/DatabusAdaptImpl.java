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
package com.java110.job.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.entity.order.Business;
import com.java110.job.adapt.hcIot.GetToken;
import com.java110.vo.ResultVo;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * @desc add by 吴学文 15:21
 */
public abstract class DatabusAdaptImpl implements IDatabusAdapt {

    /**
     * 封装头信息
     *
     * @return
     */
    protected HttpHeaders getHeaders(RestTemplate outRestTemplate) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("access_token", GetToken.get(outRestTemplate,false));
        //httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        return httpHeaders;
    }


    /**
     * 开门
     *
     * @param paramIn 业务信息
     * @return
     */
    @Override
    public ResultVo openDoor(JSONObject paramIn) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }

    /**
     * 重启设备
     *
     * @param paramIn
     * @return
     */
    @Override
    public ResultVo restartMachine(JSONObject paramIn) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }

    /**
     * 失败重新送IOT
     *
     * @param paramIn
     * @return
     */
    @Override
    public ResultVo reSendToIot(JSONObject paramIn) {
        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }


    /**
     * 业主处理执行
     *
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {

    }
}
