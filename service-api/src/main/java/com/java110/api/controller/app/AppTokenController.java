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

import com.java110.core.base.controller.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName HealthController
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/16 23:57
 * @Version 1.0
 * add by wuxw 2020/3/16
 **/
@RestController
@RequestMapping(path = "/app/appToken")
public class AppTokenController extends BaseController {

    @RequestMapping(path = "/checkToken")
    public ResponseEntity<String> checkToken(HttpServletRequest request) {
        return new ResponseEntity<String>("", HttpStatus.OK);
    }

}
