package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.rentingAppointment.RentingAppointmentDto;
import com.java110.intf.user.IRentingAppointmentInnerServiceSMO;
import com.java110.po.rentingAppointment.RentingAppointmentPo;
import com.java110.user.dao.IRentingAppointmentServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 租赁预约内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class RentingAppointmentInnerServiceSMOImpl extends BaseServiceSMO implements IRentingAppointmentInnerServiceSMO {

    @Autowired
    private IRentingAppointmentServiceDao rentingAppointmentServiceDaoImpl;


    @Override
    public int saveRentingAppointment(@RequestBody RentingAppointmentPo rentingAppointmentPo) {
        int saveFlag = 1;
        rentingAppointmentServiceDaoImpl.saveRentingAppointmentInfo(BeanConvertUtil.beanCovertMap(rentingAppointmentPo));
        return saveFlag;
    }

    @Override
    public int updateRentingAppointment(@RequestBody RentingAppointmentPo rentingAppointmentPo) {
        int saveFlag = 1;
        rentingAppointmentServiceDaoImpl.updateRentingAppointmentInfo(BeanConvertUtil.beanCovertMap(rentingAppointmentPo));
        return saveFlag;
    }

    @Override
    public int deleteRentingAppointment(@RequestBody RentingAppointmentPo rentingAppointmentPo) {
        int saveFlag = 1;
        rentingAppointmentPo.setStatusCd("1");
        rentingAppointmentServiceDaoImpl.updateRentingAppointmentInfo(BeanConvertUtil.beanCovertMap(rentingAppointmentPo));
        return saveFlag;
    }

    @Override
    public List<RentingAppointmentDto> queryRentingAppointments(@RequestBody RentingAppointmentDto rentingAppointmentDto) {

        //校验是否传了 分页信息

        int page = rentingAppointmentDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            rentingAppointmentDto.setPage((page - 1) * rentingAppointmentDto.getRow());
        }

        List<RentingAppointmentDto> rentingAppointments = BeanConvertUtil.covertBeanList(rentingAppointmentServiceDaoImpl.getRentingAppointmentInfo(BeanConvertUtil.beanCovertMap(rentingAppointmentDto)), RentingAppointmentDto.class);

        return rentingAppointments;
    }


    @Override
    public int queryRentingAppointmentsCount(@RequestBody RentingAppointmentDto rentingAppointmentDto) {
        return rentingAppointmentServiceDaoImpl.queryRentingAppointmentsCount(BeanConvertUtil.beanCovertMap(rentingAppointmentDto));
    }

    public IRentingAppointmentServiceDao getRentingAppointmentServiceDaoImpl() {
        return rentingAppointmentServiceDaoImpl;
    }

    public void setRentingAppointmentServiceDaoImpl(IRentingAppointmentServiceDao rentingAppointmentServiceDaoImpl) {
        this.rentingAppointmentServiceDaoImpl = rentingAppointmentServiceDaoImpl;
    }
}
