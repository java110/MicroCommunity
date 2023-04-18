package com.java110.common.smo.impl;

import com.java110.common.dao.IMachineAuthServiceDao;
import com.java110.dto.machine.MachineAuthDto;
import com.java110.intf.common.IMachineAuthInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.machineAuth.MachineAuthPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 设备权限内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class MachineAuthInnerServiceSMOImpl extends BaseServiceSMO implements IMachineAuthInnerServiceSMO {

    @Autowired
    private IMachineAuthServiceDao machineAuthServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<MachineAuthDto> queryMachineAuths(@RequestBody MachineAuthDto machineAuthDto) {

        //校验是否传了 分页信息

        int page = machineAuthDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            machineAuthDto.setPage((page - 1) * machineAuthDto.getRow());
        }

        List<MachineAuthDto> machineAuths = BeanConvertUtil.covertBeanList(machineAuthServiceDaoImpl.getMachineAuthInfo(BeanConvertUtil.beanCovertMap(machineAuthDto)), MachineAuthDto.class);

        if (machineAuths == null || machineAuths.size() == 0) {
            return machineAuths;
        }

        String[] userIds = getUserIds(machineAuths);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (MachineAuthDto machineAuth : machineAuths) {
            refreshMachineAuth(machineAuth, users);
        }
        return machineAuths;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param machineAuth 小区设备权限信息
     * @param users       用户列表
     */
    private void refreshMachineAuth(MachineAuthDto machineAuth, List<UserDto> users) {
        for (UserDto user : users) {
            if (machineAuth.getAuthId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, machineAuth);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param machineAuths 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<MachineAuthDto> machineAuths) {
        List<String> userIds = new ArrayList<String>();
        for (MachineAuthDto machineAuth : machineAuths) {
            userIds.add(machineAuth.getAuthId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryMachineAuthsCount(@RequestBody MachineAuthDto machineAuthDto) {
        return machineAuthServiceDaoImpl.queryMachineAuthsCount(BeanConvertUtil.beanCovertMap(machineAuthDto));
    }

    @Override
    public int saveMachineAuth(@RequestBody MachineAuthPo machineAuthPo) {
        return machineAuthServiceDaoImpl.saveMachineAuth(BeanConvertUtil.beanCovertMap(machineAuthPo));
    }

    @Override
    public int updateMachineAuth(@RequestBody MachineAuthPo machineAuthPo) {
        return machineAuthServiceDaoImpl.updateMachineAuthInfoInstance(BeanConvertUtil.beanCovertMap(machineAuthPo));
    }

    @Override
    public int deleteMachineAuth(@RequestBody MachineAuthPo machineAuthPo) {
        return machineAuthServiceDaoImpl.deleteMachineAuth(BeanConvertUtil.beanCovertMap(machineAuthPo));
    }

    public IMachineAuthServiceDao getMachineAuthServiceDaoImpl() {
        return machineAuthServiceDaoImpl;
    }

    public void setMachineAuthServiceDaoImpl(IMachineAuthServiceDao machineAuthServiceDaoImpl) {
        this.machineAuthServiceDaoImpl = machineAuthServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
