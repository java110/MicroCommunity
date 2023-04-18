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


import com.java110.dto.resourceStoreTimes.ResourceStoreTimesDto;
import com.java110.intf.store.IResourceStoreTimesV1InnerServiceSMO;
import com.java110.store.dao.IAssetInventoryDetailV1ServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.assetInventory.AssetInventoryDetailDto;
import com.java110.intf.store.IAssetInventoryDetailV1InnerServiceSMO;
import com.java110.po.assetInventoryDetail.AssetInventoryDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-09-23 03:40:30 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class AssetInventoryDetailV1InnerServiceSMOImpl extends BaseServiceSMO implements IAssetInventoryDetailV1InnerServiceSMO {

    @Autowired
    private IAssetInventoryDetailV1ServiceDao assetInventoryDetailV1ServiceDaoImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;


    @Override
    public int saveAssetInventoryDetail(@RequestBody AssetInventoryDetailPo assetInventoryDetailPo) {
        int saveFlag = assetInventoryDetailV1ServiceDaoImpl.saveAssetInventoryDetailInfo(BeanConvertUtil.beanCovertMap(assetInventoryDetailPo));
        return saveFlag;
    }

     @Override
    public int updateAssetInventoryDetail(@RequestBody AssetInventoryDetailPo assetInventoryDetailPo) {
        int saveFlag = assetInventoryDetailV1ServiceDaoImpl.updateAssetInventoryDetailInfo(BeanConvertUtil.beanCovertMap(assetInventoryDetailPo));
        return saveFlag;
    }

     @Override
    public int deleteAssetInventoryDetail(@RequestBody AssetInventoryDetailPo assetInventoryDetailPo) {
       assetInventoryDetailPo.setStatusCd("1");
       int saveFlag = assetInventoryDetailV1ServiceDaoImpl.updateAssetInventoryDetailInfo(BeanConvertUtil.beanCovertMap(assetInventoryDetailPo));
       return saveFlag;
    }

    @Override
    public List<AssetInventoryDetailDto> queryAssetInventoryDetails(@RequestBody AssetInventoryDetailDto assetInventoryDetailDto) {

        //校验是否传了 分页信息

        int page = assetInventoryDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            assetInventoryDetailDto.setPage((page - 1) * assetInventoryDetailDto.getRow());
        }

        List<AssetInventoryDetailDto> assetInventoryDetails = BeanConvertUtil.covertBeanList(assetInventoryDetailV1ServiceDaoImpl.getAssetInventoryDetailInfo(BeanConvertUtil.beanCovertMap(assetInventoryDetailDto)), AssetInventoryDetailDto.class);

        return assetInventoryDetails;
    }

    @Override
    public List<AssetInventoryDetailDto> queryAssetInventoryWholeDetails(@RequestBody AssetInventoryDetailDto assetInventoryDetailDto) {

        //校验是否传了 分页信息

        int page = assetInventoryDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            assetInventoryDetailDto.setPage((page - 1) * assetInventoryDetailDto.getRow());
        }

        List<AssetInventoryDetailDto> assetInventoryDetails = BeanConvertUtil.covertBeanList(assetInventoryDetailV1ServiceDaoImpl.getAssetInventoryDetailsInfo(BeanConvertUtil.beanCovertMap(assetInventoryDetailDto)), AssetInventoryDetailDto.class);

        for (AssetInventoryDetailDto assetInventoryDetailDto1 : assetInventoryDetails) {
            ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
            resourceStoreTimesDto.setResCode(assetInventoryDetailDto1.getResCode());
            resourceStoreTimesDto.setShId(assetInventoryDetailDto1.getShId());
            List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);
            assetInventoryDetailDto1.setTimes(resourceStoreTimesDtos);
        }

        return assetInventoryDetails;
    }




    @Override
    public int queryAssetInventoryDetailsCount(@RequestBody AssetInventoryDetailDto assetInventoryDetailDto) {
        return assetInventoryDetailV1ServiceDaoImpl.queryAssetInventoryDetailsCount(BeanConvertUtil.beanCovertMap(assetInventoryDetailDto));    }

}
