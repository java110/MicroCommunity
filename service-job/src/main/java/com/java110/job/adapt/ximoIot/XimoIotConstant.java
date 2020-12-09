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
package com.java110.job.adapt.ximoIot;

/**
 * 吸墨常量类
 *
 * @desc add by 吴学文 9:49
 */
public class XimoIotConstant {

    public static final String IOT_URL = "https://cloud-api.test.thinmoo.com/";

    public static final String APP_ID = "e86a6a373c354927bea5fd21a0bec617";
    public static final String APP_SECRET = "ead9a2f67f96e2b8ed2fe38cc9709463";

    public static final String GET_TOKEN_URL = IOT_URL + "/platCompany/extapi/getAccessToken?appId=" + APP_ID + "&appSecret=" + APP_SECRET;

    //添加设备
    public static final String ADD_MACHINE_URL = IOT_URL + "/devDevice/extapi/add";
    public static final String UPDATE_MACHINE_URL = IOT_URL + "/devDevice/extapi/update";
    public static final String DELETE_MACHINE_URL = IOT_URL + "/devDevice/extapi/delete";
    public static final String OPEN_DOOR = IOT_URL + "/sqDoor/extapi/remoteOpenDoor";

    public static final String XI_MO_TOKEN = "XI_MO_TOKEN";

    //单位为秒
    public static final int DEFAULT_LOG_TIME = 5 * 60;

    //添加业主
    public static final String ADD_OWNER = "/persEmpHousehold/extapi/add";
}
