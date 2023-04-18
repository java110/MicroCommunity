package com.java110.common.smo.impl;


import com.java110.common.dao.IMachineTranslateErrorServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.machine.MachineTranslateErrorDto;
import com.java110.intf.common.IMachineTranslateErrorInnerServiceSMO;
import com.java110.po.machineTranslateError.MachineTranslateErrorPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description IOT同步错误日志记录内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class MachineTranslateErrorInnerServiceSMOImpl extends BaseServiceSMO implements IMachineTranslateErrorInnerServiceSMO {

    @Autowired
    private IMachineTranslateErrorServiceDao machineTranslateErrorServiceDaoImpl;


    @Override
    public int saveMachineTranslateError(@RequestBody MachineTranslateErrorPo machineTranslateErrorPo) {
        int saveFlag = 1;
        machineTranslateErrorServiceDaoImpl.saveMachineTranslateErrorInfo(BeanConvertUtil.beanCovertMap(machineTranslateErrorPo));
        return saveFlag;
    }

    @Override
    public int updateMachineTranslateError(@RequestBody MachineTranslateErrorPo machineTranslateErrorPo) {
        int saveFlag = 1;
        machineTranslateErrorServiceDaoImpl.updateMachineTranslateErrorInfo(BeanConvertUtil.beanCovertMap(machineTranslateErrorPo));
        return saveFlag;
    }

    @Override
    public int deleteMachineTranslateError(@RequestBody MachineTranslateErrorPo machineTranslateErrorPo) {
        int saveFlag = 1;
        machineTranslateErrorPo.setStatusCd("1");
        machineTranslateErrorServiceDaoImpl.updateMachineTranslateErrorInfo(BeanConvertUtil.beanCovertMap(machineTranslateErrorPo));
        return saveFlag;
    }

    @Override
    public List<MachineTranslateErrorDto> queryMachineTranslateErrors(@RequestBody MachineTranslateErrorDto machineTranslateErrorDto) {

        //校验是否传了 分页信息

        int page = machineTranslateErrorDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            machineTranslateErrorDto.setPage((page - 1) * machineTranslateErrorDto.getRow());
        }

        List<MachineTranslateErrorDto> machineTranslateErrors = BeanConvertUtil.covertBeanList(machineTranslateErrorServiceDaoImpl.getMachineTranslateErrorInfo(BeanConvertUtil.beanCovertMap(machineTranslateErrorDto)), MachineTranslateErrorDto.class);

        return machineTranslateErrors;
    }


    @Override
    public int queryMachineTranslateErrorsCount(@RequestBody MachineTranslateErrorDto machineTranslateErrorDto) {
        return machineTranslateErrorServiceDaoImpl.queryMachineTranslateErrorsCount(BeanConvertUtil.beanCovertMap(machineTranslateErrorDto));
    }

    public IMachineTranslateErrorServiceDao getMachineTranslateErrorServiceDaoImpl() {
        return machineTranslateErrorServiceDaoImpl;
    }

    public void setMachineTranslateErrorServiceDaoImpl(IMachineTranslateErrorServiceDao machineTranslateErrorServiceDaoImpl) {
        this.machineTranslateErrorServiceDaoImpl = machineTranslateErrorServiceDaoImpl;
    }
}
