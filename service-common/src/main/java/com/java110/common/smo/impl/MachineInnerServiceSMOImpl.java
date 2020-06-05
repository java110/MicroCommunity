package com.java110.common.smo.impl;


import com.java110.common.dao.IMachineServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.demo.DemoDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.user.UserDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    /**
     * 获取批量userId
     *
     * @param demos 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<DemoDto> demos) {
        List<String> userIds = new ArrayList<String>();
        for (DemoDto demo : demos) {
            userIds.add(demo.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param demo  小区demo信息
     * @param users 用户列表
     */
    private void refreshDemo(DemoDto demo, List<UserDto> users) {
        for (UserDto user : users) {
            if (demo.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, demo);
            }
        }
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
