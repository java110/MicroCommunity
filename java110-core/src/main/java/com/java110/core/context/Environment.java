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
package com.java110.core.context;


import com.java110.config.properties.code.Java110Properties;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.StringUtil;

/**
 * env
 */
public class Environment {

    // property
    public static String secureCode = "amF2YTExMA==";

    public final static String ENV_ACTIVE = "ACTIVE";

    public final static String DEFAULT_ACTIVE = "dev";
    public final static String DEFAULT_PHONE = "cc_phone";

    public final static String SPRING_CLOUD = "CLOUD"; // 环境是spring boot cloud
    public final static String SPRING_BOOT = "BOOT"; // 环境是spring boot cloud

    private static String systemStartWay = "CLOUD"; // 环境是spring boot cloud

    /**
     * 环境变量
     *
     * @param profile
     * @return
     */
    public static String getEnv(String profile) {
        return System.getenv(profile);
    }

    private static boolean testEnv() {
        String curEnv = getEnv(ENV_ACTIVE);

        if (DEFAULT_ACTIVE.equals(curEnv) || StringUtil.isEmpty(curEnv)) {
            return true;
        }

        return false;
    }


    public static String getSecureCode() {
        return secureCode;
    }

    /**
     * 判断是否为手机开发模式
     *
     * @param java110Properties
     * @return
     */
    public static boolean isOwnerPhone(Java110Properties java110Properties) {
        //开关是否打开为测试模式
        if (StringUtil.isEmpty(java110Properties.getTestSwitch())
                || "0".equals(java110Properties.getTestSwitch())) {
            return false;
        }

        // 二次判断是否为测试换件
        if (!testEnv()) {
            return false;
        }

        return true;
    }

    public static void isDevEnv(){
        String env = MappingCache.getValue(MappingConstant.ENV_DOMAIN,"HC_ENV");
        if ("DEV".equals(env) || "TEST".equals(env)) {
            throw new IllegalArgumentException("为了保证体验 此功能演示环境不开放");
        }
    }


    /**
     * boot 方式启动
     * @return
     */
    public static boolean isStartBootWay(){
        if(Environment.SPRING_BOOT.equals(systemStartWay)){
            return true;
        }

        return false;
    }

    public static String getSystemStartWay() {
        return systemStartWay;
    }

    public static void setSystemStartWay(String systemStartWay) {
        Environment.systemStartWay = systemStartWay;
    }
}
