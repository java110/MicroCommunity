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
import com.java110.core.annotation.Java110Synchronized;
import com.java110.core.client.RestTemplate;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.http.*;

/**
 * 获取token
 * <p>
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 9:46
 */

public class GetToken {


    @Java110Synchronized(value = "hc_get_token")
    public static String get(RestTemplate restTemplate,boolean refreshAccessToken) {
        String token = CommonCache.getValue(IotConstant.HC_TOKEN);
        if (!StringUtil.isEmpty(token) && !refreshAccessToken) {
            return token;
        }
        HttpHeaders headers = new HttpHeaders();
        HttpEntity httpEntity = new HttpEntity(headers);
        String url = IotConstant.getUrl(IotConstant.GET_TOKEN_URL.replace("APP_ID", IotConstant.getAppId()).replace("APP_SECRET", IotConstant.getAppSecret()));
        ResponseEntity<String> tokenRes = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

        if (tokenRes.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("获取token失败" + tokenRes.getBody());
        }
        JSONObject tokenObj = JSONObject.parseObject(tokenRes.getBody());

        if (!tokenObj.containsKey("code") || ResultVo.CODE_OK != tokenObj.getInteger("code")) {
            throw new IllegalArgumentException("获取token失败" + tokenRes.getBody());
        }

        token = tokenObj.getJSONObject("data").getString("access_token");
        int expiresIn = tokenObj.getJSONObject("data").getInteger("expires_in");

        CommonCache.setValue(IotConstant.HC_TOKEN, token, expiresIn - 200);

        return token;
    }
}
