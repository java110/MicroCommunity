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
package com.java110.api.controller.app;

import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName EnvController 环境查询控制类
 * @Description TODO
 * @Author wuxw
 * @Date 2020/2/10 23:17
 * @Version 1.0
 * add by wuxw 2020/2/10
 **/
@RestController
@RequestMapping(path = "/app/env")
public class EnvController {

    @RequestMapping(path = "/getEnv", method = RequestMethod.GET)
    public ResponseEntity<String> getEnv(HttpServletRequest request) {
        String env = MappingCache.getValue(MappingConstant.ENV_DOMAIN,"HC_ENV");
        return new ResponseEntity<>(env, HttpStatus.OK);
    }


}
