package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.RoomDto;
import com.java110.dto.reportOwnerPayFee.ReportOwnerPayFeeDto;
import com.java110.intf.report.IReportCommunityInnerServiceSMO;
import com.java110.intf.report.IReportOwnerPayFeeInnerServiceSMO;
import com.java110.po.reportOwnerPayFee.ReportOwnerPayFeePo;
import com.java110.report.dao.IReportCommunityServiceDao;
import com.java110.report.dao.IReportOwnerPayFeeServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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




    @Override
    public List<RoomDto> queryRoomStructures(@RequestBody RoomDto roomDto) {

        //校验是否传了 分页信息
        List<RoomDto> roomDtos = BeanConvertUtil.covertBeanList(reportCommunityServiceDaoImpl.queryRoomStructures(BeanConvertUtil.beanCovertMap(roomDto)), RoomDto.class);

        return roomDtos;
    }


}
