package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerAttrDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.report.IReportCommunityInnerServiceSMO;
import com.java110.intf.user.IOwnerAttrInnerServiceSMO;
import com.java110.report.dao.IReportCommunityServiceDao;
import com.java110.report.dao.IReportOwnerPayFeeServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 业主缴费明细内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportCommunityInnerServiceSMOImpl extends BaseServiceSMO implements IReportCommunityInnerServiceSMO {

    @Autowired
    private IReportOwnerPayFeeServiceDao reportOwnerPayFeeServiceDaoImpl;

    @Autowired
    private IReportCommunityServiceDao reportCommunityServiceDaoImpl;

    @Autowired
    private IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl;


    @Override
    public List<RoomDto> queryRoomStructures(@RequestBody RoomDto roomDto) {

        //校验是否传了 分页信息
        List<RoomDto> roomDtos = BeanConvertUtil.covertBeanList(reportCommunityServiceDaoImpl.queryRoomStructures(BeanConvertUtil.beanCovertMap(roomDto)), RoomDto.class);

        return roomDtos;
    }

    @Override
    public List<OwnerCarDto> queryCarStructures(OwnerCarDto ownerCarDto) {
        //校验是否传了 分页信息
        List<OwnerCarDto> ownerCarDtos = BeanConvertUtil.covertBeanList(reportCommunityServiceDaoImpl.queryCarStructures(BeanConvertUtil.beanCovertMap(ownerCarDto)), OwnerCarDto.class);

        return ownerCarDtos;
    }


    @Override
    public List<RoomDto> queryRoomsTree(@RequestBody RoomDto roomDto) {
        //校验是否传了 分页信息
        List<RoomDto> roomDtos = BeanConvertUtil.covertBeanList(reportCommunityServiceDaoImpl.queryRoomsTree(BeanConvertUtil.beanCovertMap(roomDto)), RoomDto.class);

        return roomDtos;
    }

    @Override
    public int queryHisOwnerCarCount(@RequestBody OwnerCarDto ownerCarDto) {
        return reportCommunityServiceDaoImpl.queryHisOwnerCarCount(BeanConvertUtil.beanCovertMap(ownerCarDto));
    }

    @Override
    public List<OwnerCarDto> queryHisOwnerCars(@RequestBody OwnerCarDto ownerCarDto) {

        int page = ownerCarDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerCarDto.setPage((page - 1) * ownerCarDto.getRow());
        }

        List<OwnerCarDto> ownerCars = BeanConvertUtil.covertBeanList(reportCommunityServiceDaoImpl.queryHisOwnerCars(BeanConvertUtil.beanCovertMap(ownerCarDto)), OwnerCarDto.class);

        return ownerCars;
    }

    @Override
    public int queryHisOwnerCount(@RequestBody OwnerDto ownerDto) {
        return reportCommunityServiceDaoImpl.queryHisOwnerCount(BeanConvertUtil.beanCovertMap(ownerDto));
    }

    @Override
    public List<OwnerDto> queryHisOwners(@RequestBody OwnerDto ownerDto) {
        int page = ownerDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerDto.setPage((page - 1) * ownerDto.getRow());
        }

        List<OwnerDto> ownerDtos = BeanConvertUtil.covertBeanList(
                reportCommunityServiceDaoImpl.queryHisOwners(BeanConvertUtil.beanCovertMap(ownerDto)),
                OwnerDto.class);

        if (ownerDtos == null || ownerDtos.size()  < 1) {
            return ownerDtos;
        }
        String[] memberIds = getMemberIds(ownerDtos);
        OwnerAttrDto ownerAttrDto = new OwnerAttrDto();
        ownerAttrDto.setMemberIds(memberIds);
        ownerAttrDto.setCommunityId(ownerDto.getCommunityId());
        List<OwnerAttrDto> ownerAttrDtos = ownerAttrInnerServiceSMOImpl.queryOwnerAttrs(ownerAttrDto);

        for (OwnerDto owner : ownerDtos) {
            refreshOwner(owner, ownerAttrDtos);
        }


        return ownerDtos;
    }

    @Override
    public int queryHisFeeCount(@RequestBody FeeDto feeDto) {
        return reportCommunityServiceDaoImpl.queryHisFeeCount(BeanConvertUtil.beanCovertMap(feeDto));
    }

    @Override
    public List<FeeDto> queryHisFees(@RequestBody FeeDto feeDto) {
        int page = feeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeDto.setPage((page - 1) * feeDto.getRow());
        }

        List<FeeDto> feeDtos = BeanConvertUtil.covertBeanList(
                reportCommunityServiceDaoImpl.queryHisFees(BeanConvertUtil.beanCovertMap(feeDto)),
                FeeDto.class);

        return feeDtos;
    }

    /**
     * 获取批量userId
     *
     * @param owners 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getMemberIds(List<OwnerDto> owners) {
        List<String> memberIds = new ArrayList<String>();
        for (OwnerDto owner : owners) {
            memberIds.add(owner.getMemberId());
        }

        return memberIds.toArray(new String[memberIds.size()]);
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param owner 小区业主信息
     */
    private void refreshOwner(OwnerDto owner, List<OwnerAttrDto> ownerAttrDtos) {

        if (ownerAttrDtos == null || ownerAttrDtos.size() < 1) {
            return;
        }
        List<OwnerAttrDto> tmpOwnerAttrDtos = new ArrayList<>();
        for (OwnerAttrDto ownerAttrDto : ownerAttrDtos) {
            if (ownerAttrDto.getMemberId().equals(owner.getMemberId())) {
                tmpOwnerAttrDtos.add(ownerAttrDto);
            }
        }

        owner.setOwnerAttrDtos(tmpOwnerAttrDtos);
    }

}
