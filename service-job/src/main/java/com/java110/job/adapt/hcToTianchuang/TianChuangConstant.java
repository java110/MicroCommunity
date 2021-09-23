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

import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.StringUtil;

/**
 * 天创常量类
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 9:49
 */
public class TianChuangConstant {

    public static final String TC_DOMAIN = "TC"; // 公安天创
    public static final String TC_URL = "TC_URL"; // 天创地址
    public static final String TC_APP_ID = "TC_APP_ID"; // 物联网域
    public static final String TC_APP_SECRET = "TC_APP_SECRET"; // 物联网域

    private static final String DEFAULT_TC_URL = "http://112.51.96.125:9080/ywxzservice/dbClient.do";
    //
    private static final String DEFAULT_APP_ID = "26810B9FE0532D03";
    public static final String DEFAULT_APP_SECRET = "CAD3218426800B9FE0532D03A8C0310E";
    private static final String DEFAULT_COMPANY = "122350112432"; //单位编号

    private static final String TC_COMPANY = "COMPANY";//公司

    //政务小区编码/楼栋外部便阿门/房屋外部编码/业主外部编码 SPEC_CD
    public static final String EXT_TC_COMMUNITY_ID = "1329000004";
    //政务小区编码/楼栋外部便阿门/房屋外部编码/业主外部编码 SPEC_CD
    public static final String EXT_TC_MACHINE_ID = "1329000004";
    public static final String EXT_TC_OWNER_ID = "1329000004";

    //政务小区编码/楼栋外部便阿门/房屋外部编码/业主外部编码 SPEC_CD
    public static final String EXT_TC_COMMUNITY_QRCODE = "3329000004";

    public static final String EXT_TC_ROOM_QRCODE = "4329000004";

    public static final String SERVICE_ID_COMMUNITY = "ZHSQ_XQSBXX";

    public static final String SERVICE_ID_MACHINE = "ZHSQ_MJSBXX";

    public static final String SERVICE_ID_OWNER = "ZHSQ_ZHXX";

    public static final String SERVICE_ID_PERSON_INOUT = "ZHSQ_RYJCJL";

    public static String getUrl() {
        return getUrl("");
    }
    public static String getUrl(String param) {
        String url = MappingCache.getValue(TC_DOMAIN, TianChuangConstant.TC_URL);

        if (StringUtil.isEmpty(url)) {
            return DEFAULT_TC_URL + param;
        }

        return url + param;
    }

    public static String getAppId() {
        String appId = MappingCache.getValue(TC_DOMAIN, TianChuangConstant.TC_APP_ID);

        if (StringUtil.isEmpty(appId)) {
            return DEFAULT_APP_ID;
        }

        return appId;
    }

    public static String getAppSecret() {
        String appSecret = MappingCache.getValue(TC_DOMAIN, TianChuangConstant.TC_APP_SECRET);

        if (StringUtil.isEmpty(appSecret)) {
            return DEFAULT_APP_SECRET;
        }

        return appSecret;
    }

    public static String getCompany() {
        String appSecret = MappingCache.getValue(TC_DOMAIN, TianChuangConstant.TC_COMPANY);

        if (StringUtil.isEmpty(appSecret)) {
            return DEFAULT_COMPANY;
        }

        return appSecret;
    }
}
