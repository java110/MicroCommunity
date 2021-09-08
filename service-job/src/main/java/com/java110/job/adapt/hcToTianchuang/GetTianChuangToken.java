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
package com.java110.job.adapt.hcToTianchuang;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Synchronized;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.job.adapt.hcIot.IotConstant;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.util.DateUtil;
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

public class GetTianChuangToken {


    public static String get(String data) {
        String tokenStr = TianChuangConstant.getAppId()+TianChuangConstant.getAppSecret()+ DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H);
        tokenStr += data;

        return AuthenticationFactory.md5(tokenStr);
    }

    /**
     * 封装头信息
     *
     * @return
     */
    public static HttpHeaders getHeaders(String serviceId,String data) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("appId", TianChuangConstant.getAppId());
        httpHeaders.add("token", get(data));
        httpHeaders.add("tranId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_tranId));
        httpHeaders.add("serviceId", serviceId);
        httpHeaders.add("serviceValue", serviceId);
        //httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        return httpHeaders;
    }
}
