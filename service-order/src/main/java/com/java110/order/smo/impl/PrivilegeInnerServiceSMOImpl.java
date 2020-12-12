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
package com.java110.order.smo.impl;

import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.order.dao.IPrivilegeDAO;
import com.java110.utils.util.BeanConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * privilege inner transaction class
 * <p>
 * Created by wuxw on 2019/4/1.
 */

@Service("privilegeInnerServiceSMOImpl")
public class PrivilegeInnerServiceSMOImpl implements IPrivilegeInnerServiceSMO {

    private final static Logger logger = LoggerFactory.getLogger(PrivilegeInnerServiceSMOImpl.class);

    @Autowired
    private IPrivilegeDAO privilegeDAOImpl;


    @Override
    public List<UserDto> queryPrivilegeUsers(BasePrivilegeDto privilegeDto) {
        List<Map> userIds = privilegeDAOImpl.queryPrivilegeUsers(BeanConvertUtil.beanCovertMap(privilegeDto));

        return BeanConvertUtil.covertBeanList(userIds, UserDto.class);
    }
}
