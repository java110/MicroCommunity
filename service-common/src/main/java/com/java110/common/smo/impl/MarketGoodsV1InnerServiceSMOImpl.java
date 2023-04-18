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
package com.java110.common.smo.impl;


import com.java110.common.dao.IMarketGoodsV1ServiceDao;
import com.java110.dto.market.MarketGoodsItemDto;
import com.java110.intf.common.IMarketGoodsItemV1InnerServiceSMO;
import com.java110.intf.common.IMarketGoodsV1InnerServiceSMO;
import com.java110.dto.market.MarketGoodsDto;
import com.java110.po.marketGoods.MarketGoodsPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-10-25 10:58:15 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class MarketGoodsV1InnerServiceSMOImpl extends BaseServiceSMO implements IMarketGoodsV1InnerServiceSMO {

    @Autowired
    private IMarketGoodsV1ServiceDao marketGoodsV1ServiceDaoImpl;

    @Autowired
    private IMarketGoodsItemV1InnerServiceSMO marketGoodsItemV1InnerServiceSMOImpl;


    @Override
    public int saveMarketGoods(@RequestBody  MarketGoodsPo marketGoodsPo) {
        int saveFlag = marketGoodsV1ServiceDaoImpl.saveMarketGoodsInfo(BeanConvertUtil.beanCovertMap(marketGoodsPo));
        return saveFlag;
    }

     @Override
    public int updateMarketGoods(@RequestBody  MarketGoodsPo marketGoodsPo) {
        int saveFlag = marketGoodsV1ServiceDaoImpl.updateMarketGoodsInfo(BeanConvertUtil.beanCovertMap(marketGoodsPo));
        return saveFlag;
    }

     @Override
    public int deleteMarketGoods(@RequestBody  MarketGoodsPo marketGoodsPo) {
       marketGoodsPo.setStatusCd("1");
       int saveFlag = marketGoodsV1ServiceDaoImpl.updateMarketGoodsInfo(BeanConvertUtil.beanCovertMap(marketGoodsPo));
       return saveFlag;
    }

    @Override
    public List<MarketGoodsDto> queryMarketGoodss(@RequestBody  MarketGoodsDto marketGoodsDto) {

        //校验是否传了 分页信息

        int page = marketGoodsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            marketGoodsDto.setPage((page - 1) * marketGoodsDto.getRow());
        }

        List<MarketGoodsDto> marketGoodss = BeanConvertUtil.covertBeanList(marketGoodsV1ServiceDaoImpl.getMarketGoodsInfo(BeanConvertUtil.beanCovertMap(marketGoodsDto)), MarketGoodsDto.class);

        if(marketGoodss == null || marketGoodss.size()<1 || marketGoodss.size()>20){
            return marketGoodss;
        }
        List<String> goodsIds = new ArrayList<>();

        for(MarketGoodsDto marketGoodsDto1 : marketGoodss){
            goodsIds.add(marketGoodsDto1.getGoodsId());
        }

        MarketGoodsItemDto marketGoodsItemDto = new MarketGoodsItemDto();
        marketGoodsItemDto.setGoodsIds(goodsIds.toArray(new String[goodsIds.size()]));
        List<MarketGoodsItemDto> marketGoodsItemDtos = marketGoodsItemV1InnerServiceSMOImpl.queryMarketGoodssGroupCount(marketGoodsItemDto);

        for(MarketGoodsDto marketGoodsDto1 : marketGoodss){
            for(MarketGoodsItemDto marketGoodsItemDto1:marketGoodsItemDtos){
                if(!marketGoodsDto1.getGoodsId().equals(marketGoodsItemDto1.getGoodsId())){
                    continue;
                }
                marketGoodsDto1.setGoodsCount(marketGoodsItemDto1.getGoodsCount());
            }
        }

        return marketGoodss;
    }


    @Override
    public int queryMarketGoodssCount(@RequestBody MarketGoodsDto marketGoodsDto) {
        return marketGoodsV1ServiceDaoImpl.queryMarketGoodssCount(BeanConvertUtil.beanCovertMap(marketGoodsDto));    }

}
