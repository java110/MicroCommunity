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


import com.java110.community.dao.IDataPrivilegeUnitV1ServiceDao;
import com.java110.dto.UnitDto;
import com.java110.dto.data.DataPrivilegeStaffDto;
import com.java110.intf.user.IDataPrivilegeStaffV1InnerServiceSMO;
import com.java110.intf.community.IDataPrivilegeUnitV1InnerServiceSMO;
import com.java110.dto.data.DataPrivilegeUnitDto;
import com.java110.po.dataPrivilegeUnit.DataPrivilegeUnitPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-09-28 16:32:34 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class DataPrivilegeUnitV1InnerServiceSMOImpl extends BaseServiceSMO implements IDataPrivilegeUnitV1InnerServiceSMO {

    @Autowired
    private IDataPrivilegeUnitV1ServiceDao dataPrivilegeUnitV1ServiceDaoImpl;

    @Autowired
    private IDataPrivilegeStaffV1InnerServiceSMO dataPrivilegeStaffV1InnerServiceSMOImpl;


    @Override
    public int saveDataPrivilegeUnit(@RequestBody  DataPrivilegeUnitPo dataPrivilegeUnitPo) {
        int saveFlag = dataPrivilegeUnitV1ServiceDaoImpl.saveDataPrivilegeUnitInfo(BeanConvertUtil.beanCovertMap(dataPrivilegeUnitPo));
        return saveFlag;
    }

     @Override
    public int updateDataPrivilegeUnit(@RequestBody  DataPrivilegeUnitPo dataPrivilegeUnitPo) {
        int saveFlag = dataPrivilegeUnitV1ServiceDaoImpl.updateDataPrivilegeUnitInfo(BeanConvertUtil.beanCovertMap(dataPrivilegeUnitPo));
        return saveFlag;
    }

     @Override
    public int deleteDataPrivilegeUnit(@RequestBody  DataPrivilegeUnitPo dataPrivilegeUnitPo) {
       dataPrivilegeUnitPo.setStatusCd("1");
       int saveFlag = dataPrivilegeUnitV1ServiceDaoImpl.updateDataPrivilegeUnitInfo(BeanConvertUtil.beanCovertMap(dataPrivilegeUnitPo));
       return saveFlag;
    }

    @Override
    public List<DataPrivilegeUnitDto> queryDataPrivilegeUnits(@RequestBody  DataPrivilegeUnitDto dataPrivilegeUnitDto) {

        //校验是否传了 分页信息

        int page = dataPrivilegeUnitDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            dataPrivilegeUnitDto.setPage((page - 1) * dataPrivilegeUnitDto.getRow());
        }

        List<DataPrivilegeUnitDto> dataPrivilegeUnits = BeanConvertUtil.covertBeanList(dataPrivilegeUnitV1ServiceDaoImpl.getDataPrivilegeUnitInfo(BeanConvertUtil.beanCovertMap(dataPrivilegeUnitDto)), DataPrivilegeUnitDto.class);

        return dataPrivilegeUnits;
    }


    @Override
    public int queryDataPrivilegeUnitsCount(@RequestBody DataPrivilegeUnitDto dataPrivilegeUnitDto) {
        return dataPrivilegeUnitV1ServiceDaoImpl.queryDataPrivilegeUnitsCount(BeanConvertUtil.beanCovertMap(dataPrivilegeUnitDto));    }

    @Override
    public int queryUnitsNotInDataPrivilegeCount(@RequestBody DataPrivilegeUnitDto dataPrivilegeUnitDto) {
        return dataPrivilegeUnitV1ServiceDaoImpl.queryUnitsNotInDataPrivilegeCount(BeanConvertUtil.beanCovertMap(dataPrivilegeUnitDto));
    }

    @Override
    public List<UnitDto> queryUnitsNotInDataPrivilege(@RequestBody DataPrivilegeUnitDto dataPrivilegeUnitDto) {
        //校验是否传了 分页信息

        int page = dataPrivilegeUnitDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            dataPrivilegeUnitDto.setPage((page - 1) * dataPrivilegeUnitDto.getRow());
        }

        List<UnitDto> unitDtos = BeanConvertUtil.covertBeanList(
                dataPrivilegeUnitV1ServiceDaoImpl.queryUnitsNotInDataPrivilege(BeanConvertUtil.beanCovertMap(dataPrivilegeUnitDto)),
                UnitDto.class);

        return unitDtos;
    }

    @Override
    public String[] queryDataPrivilegeUnitsByStaff(@RequestBody DataPrivilegeStaffDto dataPrivilegeStaffDto) {

        if(StringUtil.isEmpty(dataPrivilegeStaffDto.getStaffId())){
            return new String[0];
        }

        List<DataPrivilegeStaffDto> dataPrivilegeStaffDtos = dataPrivilegeStaffV1InnerServiceSMOImpl.queryDataPrivilegeStaffs(dataPrivilegeStaffDto);

        if(dataPrivilegeStaffDtos == null || dataPrivilegeStaffDtos.size()<1){
            return new String[0];
        }

        List<String> dpIds = new ArrayList<>();

        for(DataPrivilegeStaffDto dataPrivilegeStaffDto1 : dataPrivilegeStaffDtos){
            dpIds.add(dataPrivilegeStaffDto1.getDpId());
        }

        DataPrivilegeUnitDto dataPrivilegeUnitDto = new DataPrivilegeUnitDto();
        dataPrivilegeUnitDto.setDpIds(dpIds.toArray(new String[dpIds.size()]));
        List<DataPrivilegeUnitDto> dataPrivilegeUnitDtos = queryDataPrivilegeUnits(dataPrivilegeUnitDto);

        if(dataPrivilegeUnitDtos == null || dataPrivilegeUnitDtos.size()<1){
            return new String[0];
        }
        List<String> unitIds = new ArrayList<>();
        for(DataPrivilegeUnitDto dataPrivilegeUnitDto1 : dataPrivilegeUnitDtos){
            unitIds.add(dataPrivilegeUnitDto1.getUnitId());
        }

        return unitIds.toArray(new String[unitIds.size()]);
    }

}
