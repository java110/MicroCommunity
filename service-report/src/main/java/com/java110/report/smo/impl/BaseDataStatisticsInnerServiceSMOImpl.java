package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.RoomDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.report.IBaseDataStatisticsInnerServiceSMO;
import com.java110.report.dao.IBaseDataStatisticsServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName BaseDataStatisticsInnerServiceSMOImpl
 * @Description 基础数据统计实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class BaseDataStatisticsInnerServiceSMOImpl extends BaseServiceSMO implements IBaseDataStatisticsInnerServiceSMO {


    @Autowired
    private IBaseDataStatisticsServiceDao baseDataStatisticsServiceDaoImpl;


    /**
     * 查询房屋总数
     *
     * @param roomDto 数据对象分享
     * @return
     */
    @Override
    public long getRoomCount(@RequestBody RoomDto roomDto) {
        int info = baseDataStatisticsServiceDaoImpl.getRoomCount(BeanConvertUtil.beanCovertMap(roomDto));
        return info;
    }

    /**
     * 查询房屋信息
     *
     * @param roomDto 数据对象分享
     * @return
     */
    @Override
    public List<RoomDto> getRoomInfo(@RequestBody RoomDto roomDto) {
        int page = roomDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            roomDto.setPage((page - 1) * roomDto.getRow());
        }

        List<Map> info = baseDataStatisticsServiceDaoImpl.getRoomInfo(BeanConvertUtil.beanCovertMap(roomDto));
        return BeanConvertUtil.covertBeanList(info, RoomDto.class);
    }
}
