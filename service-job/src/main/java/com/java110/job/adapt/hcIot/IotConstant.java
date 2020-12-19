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

/**
 * HC 物联网常量类
 *
 * @desc add by 吴学文 9:49
 */
public class IotConstant {

    public static final String IOT_URL = "https://things.homecommunity.cn:9999/";

    public static final String APP_ID = "e86a6a373c354927bea5fd21a0bec617";
    public static final String APP_SECRET = "ead9a2f67f96e2b8ed2fe38cc9709463";

    public static final String GET_TOKEN_URL = IOT_URL + " /extApi/auth/getAccessToken?appId=" + APP_ID + "&appSecret=" + APP_SECRET;

    //添加设备
    public static final String ADD_MACHINE_URL = IOT_URL + "/extApi/machine/addMachine";
    public static final String UPDATE_MACHINE_URL = IOT_URL + "/extApi/machine/updateMachine";
    public static final String DELETE_MACHINE_URL = IOT_URL + "/extApi/machine/deleteMachine";
    public static final String OPEN_DOOR = IOT_URL + "/extApi/machine/openDoor";
    public static final String RESTART_MACHINE = IOT_URL + "/extApi/machine/restartMachine";

    public static final String HC_TOKEN = "HC_ACCESS_TOKEN";

    //单位为秒
    public static final int DEFAULT_LOG_TIME = 5 * 60;

    //添加业主
    public static final String ADD_OWNER = "/extApi/user/addUser";
    public static final String EDIT_OWNER = "/extApi/user/updateUser";
    public static final String DELETE_OWNER = "/extApi/user/deleteUser";
}
