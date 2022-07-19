package com.java110.common.smo.impl;


import com.java110.common.dao.IMachineAttrServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.common.IMachineAttrInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.machine.MachineAttrDto;
import com.java110.po.machine.MachineAttrPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 设备属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class MachineAttrInnerServiceSMOImpl extends BaseServiceSMO implements IMachineAttrInnerServiceSMO {

    @Autowired
    private IMachineAttrServiceDao machineAttrServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<MachineAttrDto> queryMachineAttrs(@RequestBody MachineAttrDto machineAttrDto) {

        //校验是否传了 分页信息

        int page = machineAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            machineAttrDto.setPage((page - 1) * machineAttrDto.getRow());
        }

        List<MachineAttrDto> machineAttrs = BeanConvertUtil.covertBeanList(machineAttrServiceDaoImpl.getMachineAttrInfo(BeanConvertUtil.beanCovertMap(machineAttrDto)), MachineAttrDto.class);

        return machineAttrs;
    }

    @Override
    public int queryMachineAttrsCount(@RequestBody MachineAttrDto machineAttrDto) {
        return machineAttrServiceDaoImpl.queryMachineAttrsCount(BeanConvertUtil.beanCovertMap(machineAttrDto));
    }

    @Override
    public int saveMachineAttrs(@RequestBody MachineAttrPo machineAttrPo) {
        return machineAttrServiceDaoImpl.saveMachineAttrs(BeanConvertUtil.beanCovertMap(machineAttrPo));
    }

    @Override
    public int updateMachineAttrs(@RequestBody MachineAttrPo machineAttrPo) {
        return machineAttrServiceDaoImpl.updateMachineAttrs(BeanConvertUtil.beanCovertMap(machineAttrPo));
    }

    public IMachineAttrServiceDao getMachineAttrServiceDaoImpl() {
        return machineAttrServiceDaoImpl;
    }

    public void setMachineAttrServiceDaoImpl(IMachineAttrServiceDao machineAttrServiceDaoImpl) {
        this.machineAttrServiceDaoImpl = machineAttrServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
