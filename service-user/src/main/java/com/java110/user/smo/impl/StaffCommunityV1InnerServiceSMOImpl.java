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


import com.java110.dto.community.CommunityDto;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.user.dao.IStaffCommunityV1ServiceDao;
import com.java110.intf.user.IStaffCommunityV1InnerServiceSMO;
import com.java110.dto.staffCommunity.StaffCommunityDto;
import com.java110.po.staffCommunity.StaffCommunityPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.dto.PageDto;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2025-03-18 14:06:45 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class StaffCommunityV1InnerServiceSMOImpl extends BaseServiceSMO implements IStaffCommunityV1InnerServiceSMO {

    @Autowired
    private IStaffCommunityV1ServiceDao staffCommunityV1ServiceDaoImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;


    @Override
    public int saveStaffCommunity(@RequestBody StaffCommunityPo staffCommunityPo) {
        int saveFlag = staffCommunityV1ServiceDaoImpl.saveStaffCommunityInfo(BeanConvertUtil.beanCovertMap(staffCommunityPo));
        return saveFlag;
    }

    @Override
    public int updateStaffCommunity(@RequestBody StaffCommunityPo staffCommunityPo) {
        int saveFlag = staffCommunityV1ServiceDaoImpl.updateStaffCommunityInfo(BeanConvertUtil.beanCovertMap(staffCommunityPo));
        return saveFlag;
    }

    @Override
    public int deleteStaffCommunity(@RequestBody StaffCommunityPo staffCommunityPo) {
        staffCommunityPo.setStatusCd("1");
        int saveFlag = staffCommunityV1ServiceDaoImpl.updateStaffCommunityInfo(BeanConvertUtil.beanCovertMap(staffCommunityPo));
        return saveFlag;
    }

    @Override
    public List<StaffCommunityDto> queryStaffCommunitys(@RequestBody StaffCommunityDto staffCommunityDto) {

        //校验是否传了 分页信息

        int page = staffCommunityDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            staffCommunityDto.setPage((page - 1) * staffCommunityDto.getRow());
        }

        List<StaffCommunityDto> staffCommunitys = BeanConvertUtil.covertBeanList(staffCommunityV1ServiceDaoImpl.getStaffCommunityInfo(BeanConvertUtil.beanCovertMap(staffCommunityDto)), StaffCommunityDto.class);

        return staffCommunitys;
    }


    @Override
    public int queryStaffCommunitysCount(@RequestBody StaffCommunityDto staffCommunityDto) {
        return staffCommunityV1ServiceDaoImpl.queryStaffCommunitysCount(BeanConvertUtil.beanCovertMap(staffCommunityDto));
    }

    @Override
    public List<String> queryStaffCommunityIds(@RequestBody String staffId) {
        List<String> communityIds = new ArrayList<>();
        UserDto userDto = new UserDto();
        userDto.setUserId(staffId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            communityIds.add("-1");
            return communityIds;
        }

        if (UserDto.LEVEL_CD_ADMIN.equals(userDtos.get(0).getLevelCd())) {
            return communityIds;
        }

        StaffCommunityDto staffCommunityDto = new StaffCommunityDto();
        staffCommunityDto.setStaffId(staffId);
        List<StaffCommunityDto> staffCommunityDtos = queryStaffCommunitys(staffCommunityDto);
        if (ListUtil.isNull(userDtos)) {
            communityIds.add("-1");
            return communityIds;
        }
        for (StaffCommunityDto tStaffCommunityDto : staffCommunityDtos) {
            communityIds.add(tStaffCommunityDto.getCommunityId());
        }
        return communityIds;
    }

}
