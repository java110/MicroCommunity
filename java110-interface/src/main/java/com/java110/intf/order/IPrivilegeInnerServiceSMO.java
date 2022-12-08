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
package com.java110.intf.order;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IPrivilegeInnerServiceSMO
 * @Description 权限内部微服务交互类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "order-service", configuration = {FeignConfiguration.class})
@RequestMapping("/privilegeApi")
public interface IPrivilegeInnerServiceSMO {

    /**
     * <p>查询权限用户</p>
     *
     * query privilege of property staff
     *
     * @param privilegeDto 权限对象
     * @return OrderDto 对象数据
     */
    @RequestMapping(value = "/queryPrivilegeUsers", method = RequestMethod.POST)
    List<UserDto> queryPrivilegeUsers(@RequestBody BasePrivilegeDto privilegeDto);

}
