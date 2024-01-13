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


import com.java110.user.dao.IPrivilegeRelV1ServiceDao;
import com.java110.intf.user.IPrivilegeRelV1InnerServiceSMO;
import com.java110.dto.privilege.PrivilegeRelDto;
import com.java110.po.privilege.PrivilegeRelPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-08-13 00:41:10 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class PrivilegeRelV1InnerServiceSMOImpl extends BaseServiceSMO implements IPrivilegeRelV1InnerServiceSMO {

    @Autowired
    private IPrivilegeRelV1ServiceDao privilegeRelV1ServiceDaoImpl;


    @Override
    public int savePrivilegeRel(@RequestBody  PrivilegeRelPo privilegeRelPo) {
        int saveFlag = privilegeRelV1ServiceDaoImpl.savePrivilegeRelInfo(BeanConvertUtil.beanCovertMap(privilegeRelPo));
        return saveFlag;
    }

     @Override
    public int updatePrivilegeRel(@RequestBody  PrivilegeRelPo privilegeRelPo) {
        int saveFlag = privilegeRelV1ServiceDaoImpl.updatePrivilegeRelInfo(BeanConvertUtil.beanCovertMap(privilegeRelPo));
        return saveFlag;
    }

     @Override
    public int deletePrivilegeRel(@RequestBody  PrivilegeRelPo privilegeRelPo) {
       privilegeRelPo.setStatusCd("1");
       int saveFlag = privilegeRelV1ServiceDaoImpl.updatePrivilegeRelInfo(BeanConvertUtil.beanCovertMap(privilegeRelPo));
       return saveFlag;
    }

    @Override
    public List<PrivilegeRelDto> queryPrivilegeRels(@RequestBody PrivilegeRelDto privilegeRelDto) {

        //校验是否传了 分页信息

        int page = privilegeRelDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            privilegeRelDto.setPage((page - 1) * privilegeRelDto.getRow());
        }

        List<PrivilegeRelDto> privilegeRels = BeanConvertUtil.covertBeanList(privilegeRelV1ServiceDaoImpl.getPrivilegeRelInfo(BeanConvertUtil.beanCovertMap(privilegeRelDto)), PrivilegeRelDto.class);

        return privilegeRels;
    }


    @Override
    public int queryPrivilegeRelsCount(@RequestBody PrivilegeRelDto privilegeRelDto) {
        return privilegeRelV1ServiceDaoImpl.queryPrivilegeRelsCount(BeanConvertUtil.beanCovertMap(privilegeRelDto));    }

}
