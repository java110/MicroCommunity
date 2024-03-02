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


import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.resource.ResourceStoreTimesDto;
import com.java110.po.resource.ResourceStoreTimesPo;
import com.java110.store.dao.IResourceStoreTimesV1ServiceDao;
import com.java110.intf.store.IResourceStoreTimesV1InnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-10-06 21:26:56 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class ResourceStoreTimesV1InnerServiceSMOImpl extends BaseServiceSMO implements IResourceStoreTimesV1InnerServiceSMO {

    @Autowired
    private IResourceStoreTimesV1ServiceDao resourceResourceStoreTimesTimesV1ServiceDaoImpl;


    @Override
    public int saveResourceStoreTimes(@RequestBody ResourceStoreTimesPo resourceResourceStoreTimesTimesPo) {
        int saveFlag = resourceResourceStoreTimesTimesV1ServiceDaoImpl.saveResourceStoreTimesInfo(BeanConvertUtil.beanCovertMap(resourceResourceStoreTimesTimesPo));
        return saveFlag;
    }

    @Override
    public int updateResourceStoreTimes(@RequestBody ResourceStoreTimesPo resourceResourceStoreTimesTimesPo) {
        int saveFlag = resourceResourceStoreTimesTimesV1ServiceDaoImpl.updateResourceStoreTimesInfo(BeanConvertUtil.beanCovertMap(resourceResourceStoreTimesTimesPo));
        return saveFlag;
    }


    @Override
    public int saveOrUpdateResourceStoreTimes(@RequestBody ResourceStoreTimesPo resourceStoreTimesPo) {
        ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
        resourceStoreTimesDto.setStoreId(resourceStoreTimesPo.getStoreId());
        resourceStoreTimesDto.setPrice(resourceStoreTimesPo.getPrice());
        resourceStoreTimesDto.setResCode(resourceStoreTimesPo.getResCode());
        resourceStoreTimesDto.setShId(resourceStoreTimesPo.getShId());
        resourceStoreTimesDto.setCommunityId(resourceStoreTimesPo.getCommunityId());
        List<ResourceStoreTimesDto> resourceStoreTimesDtos = queryResourceStoreTimess(resourceStoreTimesDto);
        if (resourceStoreTimesDtos == null || resourceStoreTimesDtos.size() < 1) {
            resourceStoreTimesPo.setTimesId(GenerateCodeFactory.getGeneratorId("11"));
            return saveResourceStoreTimes(resourceStoreTimesPo);
        }
        BigDecimal stock = new BigDecimal(resourceStoreTimesDtos.get(0).getStock());
        BigDecimal newStock = new BigDecimal(resourceStoreTimesPo.getStock());
        BigDecimal bigDecimal = stock.add(newStock).setScale(2, BigDecimal.ROUND_HALF_UP);
        resourceStoreTimesPo.setStock(String.valueOf(bigDecimal));
        resourceStoreTimesPo.setTimesId(resourceStoreTimesDtos.get(0).getTimesId());
        return updateResourceStoreTimes(resourceStoreTimesPo);
    }


    @Override
    public int deleteResourceStoreTimes(@RequestBody ResourceStoreTimesPo resourceResourceStoreTimesTimesPo) {
        resourceResourceStoreTimesTimesPo.setStatusCd("1");
        int saveFlag = resourceResourceStoreTimesTimesV1ServiceDaoImpl.updateResourceStoreTimesInfo(BeanConvertUtil.beanCovertMap(resourceResourceStoreTimesTimesPo));
        return saveFlag;
    }

    @Override
    public List<ResourceStoreTimesDto> queryResourceStoreTimess(@RequestBody ResourceStoreTimesDto resourceResourceStoreTimesTimesDto) {

        //校验是否传了 分页信息

        int page = resourceResourceStoreTimesTimesDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            resourceResourceStoreTimesTimesDto.setPage((page - 1) * resourceResourceStoreTimesTimesDto.getRow());
        }

        List<ResourceStoreTimesDto> resourceResourceStoreTimesTimess = BeanConvertUtil.covertBeanList(resourceResourceStoreTimesTimesV1ServiceDaoImpl.getResourceStoreTimesInfo(BeanConvertUtil.beanCovertMap(resourceResourceStoreTimesTimesDto)), ResourceStoreTimesDto.class);

        return resourceResourceStoreTimesTimess;
    }


    @Override
    public int queryResourceStoreTimessCount(@RequestBody ResourceStoreTimesDto resourceResourceStoreTimesTimesDto) {
        return resourceResourceStoreTimesTimesV1ServiceDaoImpl.queryResourceStoreTimessCount(BeanConvertUtil.beanCovertMap(resourceResourceStoreTimesTimesDto));
    }
    @Override
    public int queryResourceStoreTimessCountStock(@RequestBody ResourceStoreTimesDto resourceResourceStoreTimesTimesDto) {
        return resourceResourceStoreTimesTimesV1ServiceDaoImpl.queryResourceStoreTimessCountStock(BeanConvertUtil.beanCovertMap(resourceResourceStoreTimesTimesDto));
    }

}
