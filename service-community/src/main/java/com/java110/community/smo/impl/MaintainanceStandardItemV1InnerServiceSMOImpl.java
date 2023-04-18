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
package com.java110.community.smo.impl;


import com.java110.community.dao.IMaintainanceStandardItemV1ServiceDao;
import com.java110.intf.community.IMaintainanceStandardItemV1InnerServiceSMO;
import com.java110.dto.maintainance.MaintainanceStandardItemDto;
import com.java110.po.maintainanceStandardItem.MaintainanceStandardItemPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-11-06 17:20:01 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class MaintainanceStandardItemV1InnerServiceSMOImpl extends BaseServiceSMO implements IMaintainanceStandardItemV1InnerServiceSMO {

    @Autowired
    private IMaintainanceStandardItemV1ServiceDao maintainanceStandardItemV1ServiceDaoImpl;


    @Override
    public int saveMaintainanceStandardItem(@RequestBody  MaintainanceStandardItemPo maintainanceStandardItemPo) {
        int saveFlag = maintainanceStandardItemV1ServiceDaoImpl.saveMaintainanceStandardItemInfo(BeanConvertUtil.beanCovertMap(maintainanceStandardItemPo));
        return saveFlag;
    }

     @Override
    public int updateMaintainanceStandardItem(@RequestBody  MaintainanceStandardItemPo maintainanceStandardItemPo) {
        int saveFlag = maintainanceStandardItemV1ServiceDaoImpl.updateMaintainanceStandardItemInfo(BeanConvertUtil.beanCovertMap(maintainanceStandardItemPo));
        return saveFlag;
    }

     @Override
    public int deleteMaintainanceStandardItem(@RequestBody  MaintainanceStandardItemPo maintainanceStandardItemPo) {
       maintainanceStandardItemPo.setStatusCd("1");
       int saveFlag = maintainanceStandardItemV1ServiceDaoImpl.updateMaintainanceStandardItemInfo(BeanConvertUtil.beanCovertMap(maintainanceStandardItemPo));
       return saveFlag;
    }

    @Override
    public List<MaintainanceStandardItemDto> queryMaintainanceStandardItems(@RequestBody  MaintainanceStandardItemDto maintainanceStandardItemDto) {

        //校验是否传了 分页信息

        int page = maintainanceStandardItemDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            maintainanceStandardItemDto.setPage((page - 1) * maintainanceStandardItemDto.getRow());
        }

        List<MaintainanceStandardItemDto> maintainanceStandardItems = BeanConvertUtil.covertBeanList(maintainanceStandardItemV1ServiceDaoImpl.getMaintainanceStandardItemInfo(BeanConvertUtil.beanCovertMap(maintainanceStandardItemDto)), MaintainanceStandardItemDto.class);

        return maintainanceStandardItems;
    }


    @Override
    public int queryMaintainanceStandardItemsCount(@RequestBody MaintainanceStandardItemDto maintainanceStandardItemDto) {
        return maintainanceStandardItemV1ServiceDaoImpl.queryMaintainanceStandardItemsCount(BeanConvertUtil.beanCovertMap(maintainanceStandardItemDto));    }

    @Override
    public List<MaintainanceStandardItemDto> queryMaintainanceStandardItemsGroupCount(@RequestBody MaintainanceStandardItemDto maintainanceStandardItemDto) {
        List<MaintainanceStandardItemDto> maintainanceStandardItems = BeanConvertUtil.covertBeanList(maintainanceStandardItemV1ServiceDaoImpl.queryMaintainanceStandardItemsGroupCount(BeanConvertUtil.beanCovertMap(maintainanceStandardItemDto)), MaintainanceStandardItemDto.class);

        return maintainanceStandardItems;
    }

}
