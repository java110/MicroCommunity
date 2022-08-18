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


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.intf.user.IOrgV1InnerServiceSMO;
import com.java110.po.org.OrgStaffRelPo;
import com.java110.store.dao.IOrgStaffRelV1ServiceDao;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.staff.ApiStaffDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2021-10-08 16:25:38 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class OrgStaffRelV1InnerServiceSMOImpl extends BaseServiceSMO implements IOrgStaffRelV1InnerServiceSMO {

    @Autowired
    private IOrgStaffRelV1ServiceDao orgStaffRelV1ServiceDaoImpl;

    @Autowired
    private IOrgV1InnerServiceSMO orgV1InnerServiceSMOImpl;


    @Override
    public int saveOrgStaffRel(@RequestBody OrgStaffRelPo orgStaffRelPo) {
        int saveFlag = orgStaffRelV1ServiceDaoImpl.saveOrgStaffRelInfo(BeanConvertUtil.beanCovertMap(orgStaffRelPo));
        return saveFlag;
    }

    @Override
    public int updateOrgStaffRel(@RequestBody OrgStaffRelPo orgStaffRelPo) {
        int saveFlag = orgStaffRelV1ServiceDaoImpl.updateOrgStaffRelInfo(BeanConvertUtil.beanCovertMap(orgStaffRelPo));
        return saveFlag;
    }

    @Override
    public int deleteOrgStaffRel(@RequestBody OrgStaffRelPo orgStaffRelPo) {
        orgStaffRelPo.setStatusCd("1");
        int saveFlag = orgStaffRelV1ServiceDaoImpl.updateOrgStaffRelInfo(BeanConvertUtil.beanCovertMap(orgStaffRelPo));
        return saveFlag;
    }

    @Override
    public List<OrgStaffRelDto> queryOrgStaffRels(@RequestBody OrgStaffRelDto orgStaffRelDto) {

        //校验是否传了 分页信息

        int page = orgStaffRelDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            orgStaffRelDto.setPage((page - 1) * orgStaffRelDto.getRow());
        }

        List<OrgStaffRelDto> orgStaffRels = BeanConvertUtil.covertBeanList(orgStaffRelV1ServiceDaoImpl.getOrgStaffRelInfo(BeanConvertUtil.beanCovertMap(orgStaffRelDto)), OrgStaffRelDto.class);

        return orgStaffRels;
    }

    @Override
    public List<OrgStaffRelDto> queryStaffOrgNames(@RequestBody OrgStaffRelDto orgStaffRelDto) {

        //校验是否传了 分页信息

        int page = orgStaffRelDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            orgStaffRelDto.setPage((page - 1) * orgStaffRelDto.getRow());
        }

        List<OrgStaffRelDto> orgStaffRels = BeanConvertUtil.covertBeanList(orgStaffRelV1ServiceDaoImpl.getOrgStaffRelInfo(BeanConvertUtil.beanCovertMap(orgStaffRelDto)), OrgStaffRelDto.class);

        if (orgStaffRels == null || orgStaffRels.size() < 1) {
            return orgStaffRels;
        }
        refreshOrgs(orgStaffRels);

        return orgStaffRels;
    }
    private void refreshOrgs(List<OrgStaffRelDto> staffs) {
        if (staffs == null || staffs.size() < 1) {
            return;
        }

        List<String> staffIds = new ArrayList<>();
        for (OrgStaffRelDto apiStaffDataVo : staffs) {
            staffIds.add(apiStaffDataVo.getStaffId());
        }

        OrgDto orgDto = new OrgDto();
        orgDto.setStoreId(staffs.get(0).getStoreId());
        List<OrgDto> orgDtos = orgV1InnerServiceSMOImpl.queryOrgs(orgDto);
        if (orgDtos == null || orgDtos.size() < 1) {
            return;
        }

        for (OrgStaffRelDto apiStaffDataVo : staffs) {
            if (StringUtil.isEmpty(apiStaffDataVo.getOrgId())) {
                continue;
            }
            apiStaffDataVo.setParentOrgId(apiStaffDataVo.getOrgId());

            findParents(apiStaffDataVo, orgDtos, null, 0);

        }

    }


    private void findParents(OrgStaffRelDto apiStaffDataVo, List<OrgDto> orgDtos, OrgDto curOrgDto, int orgDeep) {
        for (OrgDto orgDto : orgDtos) {
            curOrgDto = orgDto;
            if (!apiStaffDataVo.getParentOrgId().equals(orgDto.getOrgId())) { // 他自己跳过
                continue;
            }

            //如果到一级 就结束
            if (OrgDto.ORG_LEVEL_STORE.equals(apiStaffDataVo.getOrgLevel())) {
                continue;
            }

            apiStaffDataVo.setParentOrgId(orgDto.getParentOrgId());

            if (StringUtil.isEmpty(apiStaffDataVo.getOrgName())) {
                apiStaffDataVo.setOrgName(orgDto.getOrgName());
                continue;
            }
            apiStaffDataVo.setOrgName(orgDto.getOrgName() + " / " + apiStaffDataVo.getOrgName());
            apiStaffDataVo.setOrgLevel(orgDto.getOrgLevel());
        }

        if (curOrgDto != null && OrgDto.ORG_LEVEL_STORE.equals(curOrgDto.getOrgLevel())) {
            return;
        }

        if (curOrgDto != null && curOrgDto.getParentOrgId().equals(curOrgDto.getOrgId())) {
            return;
        }

        if (curOrgDto != null && "-1".equals(curOrgDto.getParentOrgId())) {
            return;
        }

        orgDeep += 1;

        if (orgDeep > 20) {
            return;
        }

        findParents(apiStaffDataVo, orgDtos, curOrgDto, orgDeep);
    }


    @Override
    public int queryOrgStaffRelsCount(@RequestBody OrgStaffRelDto orgStaffRelDto) {
        return orgStaffRelV1ServiceDaoImpl.queryOrgStaffRelsCount(BeanConvertUtil.beanCovertMap(orgStaffRelDto));
    }

}
