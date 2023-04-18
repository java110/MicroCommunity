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


import com.java110.common.dao.IMarketSmsV1ServiceDao;
import com.java110.dto.market.MarketSmsValueDto;
import com.java110.intf.common.IMarketSmsV1InnerServiceSMO;
import com.java110.dto.market.MarketSmsDto;
import com.java110.intf.common.IMarketSmsValueV1InnerServiceSMO;
import com.java110.po.marketSms.MarketSmsPo;
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
 * add by 吴学文 at 2022-10-23 17:43:58 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class MarketSmsV1InnerServiceSMOImpl extends BaseServiceSMO implements IMarketSmsV1InnerServiceSMO {

    @Autowired
    private IMarketSmsV1ServiceDao marketSmsV1ServiceDaoImpl;

    @Autowired
    private IMarketSmsValueV1InnerServiceSMO marketSmsValueV1InnerServiceSMOImpl;


    @Override
    public int saveMarketSms(@RequestBody  MarketSmsPo marketSmsPo) {
        int saveFlag = marketSmsV1ServiceDaoImpl.saveMarketSmsInfo(BeanConvertUtil.beanCovertMap(marketSmsPo));
        return saveFlag;
    }

     @Override
    public int updateMarketSms(@RequestBody  MarketSmsPo marketSmsPo) {
        int saveFlag = marketSmsV1ServiceDaoImpl.updateMarketSmsInfo(BeanConvertUtil.beanCovertMap(marketSmsPo));
        return saveFlag;
    }

     @Override
    public int deleteMarketSms(@RequestBody  MarketSmsPo marketSmsPo) {
       marketSmsPo.setStatusCd("1");
       int saveFlag = marketSmsV1ServiceDaoImpl.updateMarketSmsInfo(BeanConvertUtil.beanCovertMap(marketSmsPo));
       return saveFlag;
    }

    @Override
    public List<MarketSmsDto> queryMarketSmss(@RequestBody  MarketSmsDto marketSmsDto) {

        //校验是否传了 分页信息

        int page = marketSmsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            marketSmsDto.setPage((page - 1) * marketSmsDto.getRow());
        }

        List<MarketSmsDto> marketSmss = BeanConvertUtil.covertBeanList(marketSmsV1ServiceDaoImpl.getMarketSmsInfo(BeanConvertUtil.beanCovertMap(marketSmsDto)), MarketSmsDto.class);

        refreshMarketValues(marketSmss);
        return marketSmss;
    }

    /**
     * 输入 值
     * @param marketSmss
     */
    private void refreshMarketValues(List<MarketSmsDto> marketSmss) {

        if(marketSmss == null || marketSmss.size() <1){
            return ;
        }

        List<String> smsIds = new ArrayList<>();

        for(MarketSmsDto marketSmsDto : marketSmss){
            smsIds.add(marketSmsDto.getSmsId());
        }


        MarketSmsValueDto marketSmsValueDto = new MarketSmsValueDto();
        marketSmsValueDto.setSmsIds(smsIds.toArray(new String[smsIds.size()]));
        List<MarketSmsValueDto> smsValueDtos = marketSmsValueV1InnerServiceSMOImpl.queryMarketSmsValues(marketSmsValueDto);

        List<MarketSmsValueDto> marketSmsValueDtos = null;
        for(MarketSmsDto marketSmsDto : marketSmss){
            marketSmsValueDtos = new ArrayList<>();
            for(MarketSmsValueDto smsValueDto : smsValueDtos){
                if(!marketSmsDto.getSmsId().equals(smsValueDto.getSmsId())){
                    continue;
                }
                marketSmsValueDtos.add(smsValueDto);
            }

            marketSmsDto.setValues(marketSmsValueDtos);
        }
    }


    @Override
    public int queryMarketSmssCount(@RequestBody MarketSmsDto marketSmsDto) {
        return marketSmsV1ServiceDaoImpl.queryMarketSmssCount(BeanConvertUtil.beanCovertMap(marketSmsDto));    }

}
