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

import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Base64Convert;

import java.io.IOException;

/**
 * 安全
 */
public class SecureInvocation {


    public static boolean visitSecure(){
        return true;
    }

    public static boolean secure(Class clazz){
        //校验
        String name = clazz.getName();

        if(!name.contains(getSecureCode())){
            return false;
        }

        if(!CommonConstant.COOKIE_AUTH_TOKEN.contains(getSecureCode())){
            return false;
        }

        return true;
    }

    public static String getSecureCode(){
        try {
            return new String(Base64Convert.base64ToByte(Environment.getSecureCode()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
