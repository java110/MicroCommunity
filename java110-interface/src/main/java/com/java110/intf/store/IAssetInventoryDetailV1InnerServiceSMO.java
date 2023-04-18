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
package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.assetInventory.AssetInventoryDetailDto;
import com.java110.po.assetInventoryDetail.AssetInventoryDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-09-23 03:40:30 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/assetInventoryDetailV1Api")
public interface IAssetInventoryDetailV1InnerServiceSMO {


    @RequestMapping(value = "/saveAssetInventoryDetail", method = RequestMethod.POST)
    public int saveAssetInventoryDetail(@RequestBody AssetInventoryDetailPo assetInventoryDetailPo);

    @RequestMapping(value = "/updateAssetInventoryDetail", method = RequestMethod.POST)
    public int updateAssetInventoryDetail(@RequestBody AssetInventoryDetailPo assetInventoryDetailPo);

    @RequestMapping(value = "/deleteAssetInventoryDetail", method = RequestMethod.POST)
    public int deleteAssetInventoryDetail(@RequestBody AssetInventoryDetailPo assetInventoryDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param assetInventoryDetailDto 数据对象分享
     * @return AssetInventoryDetailDto 对象数据
     */
    @RequestMapping(value = "/queryAssetInventoryDetails", method = RequestMethod.POST)
    List<AssetInventoryDetailDto> queryAssetInventoryDetails(@RequestBody AssetInventoryDetailDto assetInventoryDetailDto);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param assetInventoryDetailDto 数据对象分享
     * @return AssetInventoryDetailDto 对象数据
     */
    @RequestMapping(value = "/queryAssetInventoryWholeDetails", method = RequestMethod.POST)
    List<AssetInventoryDetailDto> queryAssetInventoryWholeDetails(@RequestBody AssetInventoryDetailDto assetInventoryDetailDto);


    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param assetInventoryDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAssetInventoryDetailsCount", method = RequestMethod.POST)
    int queryAssetInventoryDetailsCount(@RequestBody AssetInventoryDetailDto assetInventoryDetailDto);
}
