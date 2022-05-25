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


import com.java110.store.dao.IStoreV1ServiceDao;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.dto.store.StoreDto;
import com.java110.po.store.StorePo;
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
 * add by 吴学文 at 2022-02-28 10:46:30 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class StoreV1InnerServiceSMOImpl extends BaseServiceSMO implements IStoreV1InnerServiceSMO {

    @Autowired
    private IStoreV1ServiceDao storeV1ServiceDaoImpl;


    @Override
    public int saveStore(@RequestBody  StorePo storePo) {
        int saveFlag = storeV1ServiceDaoImpl.saveStoreInfo(BeanConvertUtil.beanCovertMap(storePo));
        return saveFlag;
    }

     @Override
    public int updateStore(@RequestBody  StorePo storePo) {
        int saveFlag = storeV1ServiceDaoImpl.updateStoreInfo(BeanConvertUtil.beanCovertMap(storePo));
        return saveFlag;
    }

     @Override
    public int deleteStore(@RequestBody  StorePo storePo) {
       storePo.setStatusCd("1");
       int saveFlag = storeV1ServiceDaoImpl.updateStoreInfo(BeanConvertUtil.beanCovertMap(storePo));
       return saveFlag;
    }

    @Override
    public List<StoreDto> queryStores(@RequestBody StoreDto storeDto) {

        //校验是否传了 分页信息

        int page = storeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            storeDto.setPage((page - 1) * storeDto.getRow());
        }

        List<StoreDto> stores = BeanConvertUtil.covertBeanList(storeV1ServiceDaoImpl.getStoreInfo(BeanConvertUtil.beanCovertMap(storeDto)), StoreDto.class);

        return stores;
    }


    @Override
    public int queryStoresCount(@RequestBody StoreDto storeDto) {
        return storeV1ServiceDaoImpl.queryStoresCount(BeanConvertUtil.beanCovertMap(storeDto));    }

}
