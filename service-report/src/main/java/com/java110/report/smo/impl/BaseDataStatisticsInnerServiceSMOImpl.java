package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.report.IBaseDataStatisticsInnerServiceSMO;
import com.java110.report.dao.IBaseDataStatisticsServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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

    @Override
    public long getReceivedRoomCount(@RequestBody RoomDto roomDto) {
        int info = baseDataStatisticsServiceDaoImpl.getReceivedRoomCount(BeanConvertUtil.beanCovertMap(roomDto));
        return info;
    }

    @Override
    public List<RoomDto> getReceivedRoomInfo(@RequestBody RoomDto roomDto) {
        int page = roomDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            roomDto.setPage((page - 1) * roomDto.getRow());
        }

        List<Map> info = baseDataStatisticsServiceDaoImpl.getReceivedRoomInfo(BeanConvertUtil.beanCovertMap(roomDto));
        return BeanConvertUtil.covertBeanList(info, RoomDto.class);
    }

    @Override
    public long getOweRoomCount(@RequestBody RoomDto roomDto) {
        int info = baseDataStatisticsServiceDaoImpl.getOweRoomCount(BeanConvertUtil.beanCovertMap(roomDto));
        return info;
    }

    @Override
    public List<RoomDto> getOweRoomInfo(@RequestBody RoomDto roomDto) {
        int page = roomDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            roomDto.setPage((page - 1) * roomDto.getRow());
        }

        List<Map> info = baseDataStatisticsServiceDaoImpl.getOweRoomInfo(BeanConvertUtil.beanCovertMap(roomDto));
        return BeanConvertUtil.covertBeanList(info, RoomDto.class);
    }

    @Override
    public List<Map> getCommunityFeeDetailCount(@RequestBody Map info) {
        List<Map> infos = baseDataStatisticsServiceDaoImpl.getCommunityFeeDetailCount(info);
        return infos;
    }

    @Override
    public List<Map> getCommunityRepairCount(@RequestBody Map info) {
        List<Map> infos = baseDataStatisticsServiceDaoImpl.getCommunityRepairCount(info);
        return infos;
    }

    @Override
    public List<Map> getCommunityFeeDetailCountAnalysis(@RequestBody Map info) {
        List<Map> infos = baseDataStatisticsServiceDaoImpl.getCommunityFeeDetailCountAnalysis(info);
        return infos;
    }

    @Override
    public List<Map> getCommunityRepairCountAnalysis(@RequestBody Map info) {
        List<Map> infos = baseDataStatisticsServiceDaoImpl.getCommunityRepairCountAnalysis(info);
        return infos;
    }

    @Override
    public List<Map> getCommunityInspectionAnalysis(@RequestBody Map info) {
        List<Map> infos = baseDataStatisticsServiceDaoImpl.getCommunityInspectionAnalysis(info);
        return infos;
    }

    @Override
    public List<Map> getCommunityMaintainanceAnalysis(@RequestBody Map info) {
        List<Map> infos = baseDataStatisticsServiceDaoImpl.getCommunityMaintainanceAnalysis(info);
        return infos;
    }

    @Override
    public List<Map> getCommunityItemInAnalysis(@RequestBody Map info) {
        List<Map> infos = baseDataStatisticsServiceDaoImpl.getCommunityItemInAnalysis(info);
        return infos;
    }

    @Override
    public List<Map> getCommunityItemOutAnalysis(@RequestBody Map info) {
        List<Map> infos = baseDataStatisticsServiceDaoImpl.getCommunityItemOutAnalysis(info);
        return infos;
    }

    @Override
    public List<Map> getCommunityCarInAnalysis(@RequestBody Map info) {
        List<Map> infos = baseDataStatisticsServiceDaoImpl.getCommunityCarInAnalysis(info);
        return infos;
    }

    @Override
    public List<Map> getCommunityPersonInAnalysis(@RequestBody Map info) {
        List<Map> infos = baseDataStatisticsServiceDaoImpl.getCommunityPersonInAnalysis(info);
        return infos;
    }

    @Override
    public List<Map> getCommunityContractAnalysis(@RequestBody Map info) {
        List<Map> infos = baseDataStatisticsServiceDaoImpl.getCommunityContractAnalysis(info);
        return infos;
    }

    @Override
    public List<Map> getPropertyFeeSummaryData(@RequestBody Map info) {
        int page = Integer.parseInt(info.get("page").toString());
        int row = Integer.parseInt(info.get("row").toString());

        if (page != PageDto.DEFAULT_PAGE) {
            info.put("page",(page - 1) * row);
            info.put("row", row);
        }
        List<Map> infos = baseDataStatisticsServiceDaoImpl.getPropertyFeeSummaryData(info);
        return infos;
    }

    @Override
    public int getPropertyFeeSummaryDataCount(@RequestBody Map info) {
        int count = baseDataStatisticsServiceDaoImpl.getPropertyFeeSummaryDataCount(info);
        return count;
    }

    @Override
    public List<Map> computeEveryMonthFee(@RequestBody Map info) {
        List<Map> infos = baseDataStatisticsServiceDaoImpl.computeEveryMonthFee(info);
        return infos;
    }

    @Override
    public int getParkingFeeSummaryDataCount(@RequestBody Map info) {
        int count = baseDataStatisticsServiceDaoImpl.getParkingFeeSummaryDataCount(info);
        return count;
    }

    @Override
    public List<Map> getParkingFeeSummaryData(@RequestBody Map info) {
        int page = Integer.parseInt(info.get("page").toString());
        int row = Integer.parseInt(info.get("row").toString());

        if (page != PageDto.DEFAULT_PAGE) {
            info.put("page",(page - 1) * row);
            info.put("row", row);
        }
        List<Map> infos = baseDataStatisticsServiceDaoImpl.getParkingFeeSummaryData(info);
        return infos;
    }
}
