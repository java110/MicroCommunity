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
package com.java110.api.smo.login;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 管理员免密登录物业账号 接口类
 * Created by wuxw on 2019/3/20.
 */
public interface IAdminLoginPropertyAccountServiceSMO {

    /**
     * 登录接口
     * @param pd 页面请求对象
     * @return
     */
    public ResponseEntity<String> doLogin(IPageData pd);




}
