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
package com.java110.user.smo.impl;


import com.java110.po.fee.PayFeePo;
import com.java110.user.dao.IWechatSubscribeV1ServiceDao;
import com.java110.intf.user.IWechatSubscribeV1InnerServiceSMO;
import com.java110.dto.wechatSubscribe.WechatSubscribeDto;
import com.java110.po.wechatSubscribe.WechatSubscribePo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-02-22 16:17:23 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class WechatSubscribeV1InnerServiceSMOImpl extends BaseServiceSMO implements IWechatSubscribeV1InnerServiceSMO {

    @Autowired
    private IWechatSubscribeV1ServiceDao wechatSubscribeV1ServiceDaoImpl;


    @Override
    public int saveWechatSubscribe(@RequestBody  WechatSubscribePo wechatSubscribePo) {
        int saveFlag = wechatSubscribeV1ServiceDaoImpl.saveWechatSubscribeInfo(BeanConvertUtil.beanCovertMap(wechatSubscribePo));
        return saveFlag;
    }

    @Override
    public int saveWechatSubscribes(@RequestBody  List<WechatSubscribePo> wechatSubscribePos){

        List<Map> wechatSubscribe = new ArrayList<>();
        for (WechatSubscribePo payFeePo : wechatSubscribePos) {
            wechatSubscribe.add(BeanConvertUtil.beanCovertMap(payFeePo));
        }

        Map info = new HashMap();
        info.put("wechatSubscribes", wechatSubscribe);
        int saveFlag = wechatSubscribeV1ServiceDaoImpl.saveWechatSubscribeInfos(info);
        return saveFlag;
    }

     @Override
    public int updateWechatSubscribe(@RequestBody  WechatSubscribePo wechatSubscribePo) {
        int saveFlag = wechatSubscribeV1ServiceDaoImpl.updateWechatSubscribeInfo(BeanConvertUtil.beanCovertMap(wechatSubscribePo));
        return saveFlag;
    }

     @Override
    public int deleteWechatSubscribe(@RequestBody  WechatSubscribePo wechatSubscribePo) {
       wechatSubscribePo.setStatusCd("1");
       int saveFlag = wechatSubscribeV1ServiceDaoImpl.updateWechatSubscribeInfo(BeanConvertUtil.beanCovertMap(wechatSubscribePo));
       return saveFlag;
    }

    @Override
    public List<WechatSubscribeDto> queryWechatSubscribes(@RequestBody  WechatSubscribeDto wechatSubscribeDto) {

        //校验是否传了 分页信息

        int page = wechatSubscribeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            wechatSubscribeDto.setPage((page - 1) * wechatSubscribeDto.getRow());
        }

        List<WechatSubscribeDto> wechatSubscribes = BeanConvertUtil.covertBeanList(wechatSubscribeV1ServiceDaoImpl.getWechatSubscribeInfo(BeanConvertUtil.beanCovertMap(wechatSubscribeDto)), WechatSubscribeDto.class);

        return wechatSubscribes;
    }
    @Override
    public List<WechatSubscribeDto> queryDistinctWechatSubscribes(@RequestBody WechatSubscribeDto wechatSubscribeDto) {
        //校验是否传了 分页信息
        int page = wechatSubscribeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            wechatSubscribeDto.setPage((page - 1) * wechatSubscribeDto.getRow());
        }

        List<WechatSubscribeDto> wechatSubscribes = BeanConvertUtil.covertBeanList(wechatSubscribeV1ServiceDaoImpl.queryDistinctWechatSubscribes(BeanConvertUtil.beanCovertMap(wechatSubscribeDto)), WechatSubscribeDto.class);

        return wechatSubscribes;
    }


    @Override
    public int queryWechatSubscribesCount(@RequestBody WechatSubscribeDto wechatSubscribeDto) {
        return wechatSubscribeV1ServiceDaoImpl.queryWechatSubscribesCount(BeanConvertUtil.beanCovertMap(wechatSubscribeDto));    }

}
