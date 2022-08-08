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
package com.java110.store.smo.impl;


import com.java110.dto.resourceStoreSpecification.ResourceStoreSpecificationDto;
import com.java110.po.resourceStoreSpecification.ResourceStoreSpecificationPo;
import com.java110.store.dao.IResourceStoreSpecificationV1ServiceDao;
import com.java110.intf.store.IResourceStoreSpecificationV1InnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-08-08 14:19:39 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class ResourceStoreSpecificationV1InnerServiceSMOImpl extends BaseServiceSMO implements IResourceStoreSpecificationV1InnerServiceSMO {

    @Autowired
    private IResourceStoreSpecificationV1ServiceDao resourceResourceStoreSpecificationSpecificationV1ServiceDaoImpl;


    @Override
    public int saveResourceStoreSpecification(@RequestBody ResourceStoreSpecificationPo resourceResourceStoreSpecificationSpecificationPo) {
        int saveFlag = resourceResourceStoreSpecificationSpecificationV1ServiceDaoImpl.saveResourceStoreSpecificationInfo(BeanConvertUtil.beanCovertMap(resourceResourceStoreSpecificationSpecificationPo));
        return saveFlag;
    }

     @Override
    public int updateResourceStoreSpecification(@RequestBody  ResourceStoreSpecificationPo resourceResourceStoreSpecificationSpecificationPo) {
        int saveFlag = resourceResourceStoreSpecificationSpecificationV1ServiceDaoImpl.updateResourceStoreSpecificationInfo(BeanConvertUtil.beanCovertMap(resourceResourceStoreSpecificationSpecificationPo));
        return saveFlag;
    }

     @Override
    public int deleteResourceStoreSpecification(@RequestBody  ResourceStoreSpecificationPo resourceResourceStoreSpecificationSpecificationPo) {
       resourceResourceStoreSpecificationSpecificationPo.setStatusCd("1");
       int saveFlag = resourceResourceStoreSpecificationSpecificationV1ServiceDaoImpl.updateResourceStoreSpecificationInfo(BeanConvertUtil.beanCovertMap(resourceResourceStoreSpecificationSpecificationPo));
       return saveFlag;
    }

    @Override
    public List<ResourceStoreSpecificationDto> queryResourceStoreSpecifications(@RequestBody  ResourceStoreSpecificationDto resourceResourceStoreSpecificationSpecificationDto) {

        //校验是否传了 分页信息

        int page = resourceResourceStoreSpecificationSpecificationDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            resourceResourceStoreSpecificationSpecificationDto.setPage((page - 1) * resourceResourceStoreSpecificationSpecificationDto.getRow());
        }

        List<ResourceStoreSpecificationDto> resourceResourceStoreSpecificationSpecifications = BeanConvertUtil.covertBeanList(resourceResourceStoreSpecificationSpecificationV1ServiceDaoImpl.getResourceStoreSpecificationInfo(BeanConvertUtil.beanCovertMap(resourceResourceStoreSpecificationSpecificationDto)), ResourceStoreSpecificationDto.class);

        return resourceResourceStoreSpecificationSpecifications;
    }


    @Override
    public int queryResourceStoreSpecificationsCount(@RequestBody ResourceStoreSpecificationDto resourceResourceStoreSpecificationSpecificationDto) {
        return resourceResourceStoreSpecificationSpecificationV1ServiceDaoImpl.queryResourceStoreSpecificationsCount(BeanConvertUtil.beanCovertMap(resourceResourceStoreSpecificationSpecificationDto));    }

}
