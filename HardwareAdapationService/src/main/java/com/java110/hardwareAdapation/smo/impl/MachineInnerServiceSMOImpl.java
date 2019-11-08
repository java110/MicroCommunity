package com.java110.hardwareAdapation.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.hardwareAdapation.MachineDto;
import com.java110.hardwareAdapation.dao.IMachineServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 设备内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class MachineInnerServiceSMOImpl extends BaseServiceSMO implements IMachineInnerServiceSMO {

    @Autowired
    private IMachineServiceDao machineServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<MachineDto> queryMachines(@RequestBody MachineDto machineDto) {

        //校验是否传了 分页信息

        int page = machineDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            machineDto.setPage((page - 1) * machineDto.getRow());
        }

        List<MachineDto> machines = BeanConvertUtil.covertBeanList(machineServiceDaoImpl.getMachineInfo(BeanConvertUtil.beanCovertMap(machineDto)), MachineDto.class);


        return machines;
    }

    @Override
    public int queryMachinesCount(@RequestBody MachineDto machineDto) {
        return machineServiceDaoImpl.queryMachinesCount(BeanConvertUtil.beanCovertMap(machineDto));
    }

    public IMachineServiceDao getMachineServiceDaoImpl() {
        return machineServiceDaoImpl;
    }

    public void setMachineServiceDaoImpl(IMachineServiceDao machineServiceDaoImpl) {
        this.machineServiceDaoImpl = machineServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
