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
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;

/**
 * env
 *
 */
public class Environment {

    // property
    public static String secureCode = "amF2YTExMA==";

    public final static String ENV_ACTIVE = "ACTIVE";

    public final static String DEFAULT_ACTIVE="dev";
    public final static String DEFAULT_PHONE="cc_phone";

    /**
     * 环境变量
     * @param profile
     * @return
     */
    public static String getEnv(String profile){
       return System.getenv(profile);
    }

    public static boolean testEnv(){
       String curEnv =  getEnv(ENV_ACTIVE);

       if(DEFAULT_ACTIVE.equals(curEnv) || StringUtil.isEmpty(curEnv)){
           return true;
       }

       return false;
    }



    public static String getSecureCode() {
        return secureCode;
    }

    public static boolean isOwnerPhone(Java110Properties java110Properties) {

        if(!testEnv()){
            return true;
        }

        if(StringUtil.isEmpty(java110Properties.getTestSwitch())
                || "0".equals(java110Properties.getTestSwitch())){
            return false;
        }


        return true;
    }
}
