package com.java110.hardwareAdapation.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineRecordInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.UserDto;
import com.java110.dto.hardwareAdapation.MachineRecordDto;
import com.java110.hardwareAdapation.dao.IMachineRecordServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 设备上报内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class MachineRecordInnerServiceSMOImpl extends BaseServiceSMO implements IMachineRecordInnerServiceSMO {

    @Autowired
    private IMachineRecordServiceDao machineRecordServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<MachineRecordDto> queryMachineRecords(@RequestBody MachineRecordDto machineRecordDto) {

        //校验是否传了 分页信息

        int page = machineRecordDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            machineRecordDto.setPage((page - 1) * machineRecordDto.getRow());
        }

        List<MachineRecordDto> machineRecords = BeanConvertUtil.covertBeanList(machineRecordServiceDaoImpl.getMachineRecordInfo(BeanConvertUtil.beanCovertMap(machineRecordDto)), MachineRecordDto.class);


        return machineRecords;
    }



    @Override
    public int queryMachineRecordsCount(@RequestBody MachineRecordDto machineRecordDto) {
        return machineRecordServiceDaoImpl.queryMachineRecordsCount(BeanConvertUtil.beanCovertMap(machineRecordDto));
    }

    public IMachineRecordServiceDao getMachineRecordServiceDaoImpl() {
        return machineRecordServiceDaoImpl;
    }

    public void setMachineRecordServiceDaoImpl(IMachineRecordServiceDao machineRecordServiceDaoImpl) {
        this.machineRecordServiceDaoImpl = machineRecordServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
