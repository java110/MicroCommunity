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

import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.StringUtil;

/**
 * HC 物联网常量类
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 9:49
 */
public class IotConstant {

    public static final String IOT_DOMAIN = "IOT"; // 物联网域
    public static final String IOT_URL = "IOT_URL"; // 物联网域
    public static final String IOT_APP_ID = "APP_ID"; // 物联网域
    public static final String IOT_APP_SECRET = "APP_SECRET"; // 物联网域

    private static final String DEFAULT_IOT_URL = "https://things.homecommunity.cn/";

    private static final String DEFAULT_APP_ID = "e86a6a373c354927bea5fd21a0bec617";
    private static final String DEFAULT_APP_SECRET = "ead9a2f67f96e2b8ed2fe38cc9709463";

    public static final String GET_TOKEN_URL = " /extApi/auth/getAccessToken?appId=APP_ID&appSecret=APP_SECRET";

    //添加小区
    public static final String ADD_COMMUNITY_URL = "/extApi/community/addCommunity";
    //修改小区
    public static final String UPDATE_COMMUNITY_URL = "/extApi/community/updateCommunity";
    //删除小区
    public static final String DELETE_COMMUNITY_URL = "/extApi/community/deleteCommunity";

    //添加设备
    public static final String ADD_MACHINE_URL = "/extApi/machine/addMachine";
    //修改设备
    public static final String UPDATE_MACHINE_URL = "/extApi/machine/updateMachine";
    //删除设备
    public static final String DELETE_MACHINE_URL = "/extApi/machine/deleteMachine";
    //开门接口
    public static final String OPEN_DOOR = "/extApi/machine/openDoor";
    //重启接口
    public static final String RESTART_MACHINE = "/extApi/machine/restartMachine";

    public static final String HC_TOKEN = "HC_ACCESS_TOKEN";

    //单位为秒
    public static final int DEFAULT_LOG_TIME = 5 * 60;

    //添加业主
    public static final String ADD_OWNER = "/extApi/user/addUser";
    public static final String EDIT_OWNER = "/extApi/user/updateUser";
    public static final String DELETE_OWNER = "/extApi/user/deleteUser";


    public static String getUrl(String param) {
        String url = MappingCache.getValue(IOT_DOMAIN, IotConstant.IOT_URL);

        if (StringUtil.isEmpty(url)) {
            return DEFAULT_IOT_URL + param;
        }

        return url + param;
    }

    public static String getAppId() {
        String appId = MappingCache.getValue(IOT_DOMAIN, IotConstant.IOT_APP_ID);

        if (StringUtil.isEmpty(appId)) {
            return DEFAULT_APP_ID;
        }

        return appId;
    }

    public static String getAppSecret() {
        String appSecret = MappingCache.getValue(IOT_DOMAIN, IotConstant.IOT_APP_SECRET);

        if (StringUtil.isEmpty(appSecret)) {
            return DEFAULT_APP_SECRET;
        }

        return appSecret;
    }
}
