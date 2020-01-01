package com.java110.common.smo.impl;

import com.java110.common.dao.IAreaServiceDao;
import com.java110.common.dao.IFileServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.common.IAreaInnerServiceSMO;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.dto.area.AreaDto;
import com.java110.dto.file.FileDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AreaInnerServiceSMOImpl extends BaseServiceSMO implements IAreaInnerServiceSMO {

    @Autowired
    private IAreaServiceDao areaServiceDaoImpl;

    @Override
    public List<AreaDto> getArea(@RequestBody AreaDto areaDto) {

        List<AreaDto> areas = BeanConvertUtil.covertBeanList(areaServiceDaoImpl.getAreas(BeanConvertUtil.beanCovertMap(areaDto)), AreaDto.class);

        return areas;
    }

    @Override
    public List<AreaDto> getProvCityArea(@RequestBody AreaDto areaDto) {

        List<AreaDto> areas = BeanConvertUtil.covertBeanList(areaServiceDaoImpl.getProvCityArea(BeanConvertUtil.beanCovertMap(areaDto)), AreaDto.class);

        return areas;
    }
}
