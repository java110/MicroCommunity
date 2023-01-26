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


import com.java110.dto.ownerSettledSetting.OwnerSettledSettingDto;
import com.java110.intf.user.IOwnerSettledSettingV1InnerServiceSMO;
import com.java110.user.dao.IOwnerSettledApplyV1ServiceDao;
import com.java110.intf.user.IOwnerSettledApplyV1InnerServiceSMO;
import com.java110.dto.ownerSettledApply.OwnerSettledApplyDto;
import com.java110.po.ownerSettledApply.OwnerSettledApplyPo;
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
 * add by 吴学文 at 2023-01-26 00:52:26 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class OwnerSettledApplyV1InnerServiceSMOImpl extends BaseServiceSMO implements IOwnerSettledApplyV1InnerServiceSMO {

    @Autowired
    private IOwnerSettledApplyV1ServiceDao ownerSettledApplyV1ServiceDaoImpl;

    @Autowired
    private IOwnerSettledSettingV1InnerServiceSMO ownerSettledSettingV1InnerServiceSMOImpl;


    @Override
    public int saveOwnerSettledApply(@RequestBody OwnerSettledApplyPo ownerSettledApplyPo) {
        int saveFlag = ownerSettledApplyV1ServiceDaoImpl.saveOwnerSettledApplyInfo(BeanConvertUtil.beanCovertMap(ownerSettledApplyPo));
        return saveFlag;
    }

    @Override
    public int updateOwnerSettledApply(@RequestBody OwnerSettledApplyPo ownerSettledApplyPo) {
        int saveFlag = ownerSettledApplyV1ServiceDaoImpl.updateOwnerSettledApplyInfo(BeanConvertUtil.beanCovertMap(ownerSettledApplyPo));
        return saveFlag;
    }

    @Override
    public int deleteOwnerSettledApply(@RequestBody OwnerSettledApplyPo ownerSettledApplyPo) {
        ownerSettledApplyPo.setStatusCd("1");
        int saveFlag = ownerSettledApplyV1ServiceDaoImpl.updateOwnerSettledApplyInfo(BeanConvertUtil.beanCovertMap(ownerSettledApplyPo));
        return saveFlag;
    }

    @Override
    public List<OwnerSettledApplyDto> queryOwnerSettledApplys(@RequestBody OwnerSettledApplyDto ownerSettledApplyDto) {

        //校验是否传了 分页信息

        int page = ownerSettledApplyDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerSettledApplyDto.setPage((page - 1) * ownerSettledApplyDto.getRow());
        }

        List<OwnerSettledApplyDto> ownerSettledApplys = BeanConvertUtil.covertBeanList(ownerSettledApplyV1ServiceDaoImpl.getOwnerSettledApplyInfo(BeanConvertUtil.beanCovertMap(ownerSettledApplyDto)), OwnerSettledApplyDto.class);

        if (ownerSettledApplys == null || ownerSettledApplys.size() < 1) {
            return ownerSettledApplys;
        }
        OwnerSettledSettingDto ownerSettledSettingDto = new OwnerSettledSettingDto();
        ownerSettledSettingDto.setCommunityId(ownerSettledApplys.get(0).getCommunityId());
       List<OwnerSettledSettingDto> settings = ownerSettledSettingV1InnerServiceSMOImpl.queryOwnerSettledSettings(ownerSettledSettingDto);

        if (settings == null || settings.size() < 1) {
            return ownerSettledApplys;
        }

        for(OwnerSettledApplyDto tmpOwnerSettledApplyDto: ownerSettledApplys){
            settings.get(0).setRemark("");
            tmpOwnerSettledApplyDto.setFlowId(settings.get(0).getFlowId());
            tmpOwnerSettledApplyDto.setFlowName(settings.get(0).getFlowName());
            tmpOwnerSettledApplyDto.setAuditWay(settings.get(0).getAuditWay());
        }

        return ownerSettledApplys;
    }


    @Override
    public int queryOwnerSettledApplysCount(@RequestBody OwnerSettledApplyDto ownerSettledApplyDto) {
        return ownerSettledApplyV1ServiceDaoImpl.queryOwnerSettledApplysCount(BeanConvertUtil.beanCovertMap(ownerSettledApplyDto));
    }

}
