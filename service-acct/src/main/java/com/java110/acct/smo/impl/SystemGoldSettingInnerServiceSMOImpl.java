package com.java110.acct.smo.impl;


import com.java110.acct.dao.ISystemGoldSettingServiceDao;
import com.java110.intf.acct.ISystemGoldSettingInnerServiceSMO;
import com.java110.dto.systemGoldSetting.SystemGoldSettingDto;
import com.java110.po.systemGoldSetting.SystemGoldSettingPo;
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
 * @Description 金币设置内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class SystemGoldSettingInnerServiceSMOImpl extends BaseServiceSMO implements ISystemGoldSettingInnerServiceSMO {

    @Autowired
    private ISystemGoldSettingServiceDao systemGoldSettingServiceDaoImpl;


    @Override
    public int saveSystemGoldSetting(@RequestBody SystemGoldSettingPo systemGoldSettingPo) {
        int saveFlag = 1;
        systemGoldSettingServiceDaoImpl.saveSystemGoldSettingInfo(BeanConvertUtil.beanCovertMap(systemGoldSettingPo));
        return saveFlag;
    }

     @Override
    public int updateSystemGoldSetting(@RequestBody  SystemGoldSettingPo systemGoldSettingPo) {
        int saveFlag = 1;
         systemGoldSettingServiceDaoImpl.updateSystemGoldSettingInfo(BeanConvertUtil.beanCovertMap(systemGoldSettingPo));
        return saveFlag;
    }

     @Override
    public int deleteSystemGoldSetting(@RequestBody  SystemGoldSettingPo systemGoldSettingPo) {
        int saveFlag = 1;
        systemGoldSettingPo.setStatusCd("1");
        systemGoldSettingServiceDaoImpl.updateSystemGoldSettingInfo(BeanConvertUtil.beanCovertMap(systemGoldSettingPo));
        return saveFlag;
    }

    @Override
    public List<SystemGoldSettingDto> querySystemGoldSettings(@RequestBody  SystemGoldSettingDto systemGoldSettingDto) {

        //校验是否传了 分页信息

        int page = systemGoldSettingDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            systemGoldSettingDto.setPage((page - 1) * systemGoldSettingDto.getRow());
        }

        List<SystemGoldSettingDto> systemGoldSettings = BeanConvertUtil.covertBeanList(systemGoldSettingServiceDaoImpl.getSystemGoldSettingInfo(BeanConvertUtil.beanCovertMap(systemGoldSettingDto)), SystemGoldSettingDto.class);

        return systemGoldSettings;
    }


    @Override
    public int querySystemGoldSettingsCount(@RequestBody SystemGoldSettingDto systemGoldSettingDto) {
        return systemGoldSettingServiceDaoImpl.querySystemGoldSettingsCount(BeanConvertUtil.beanCovertMap(systemGoldSettingDto));    }

    public ISystemGoldSettingServiceDao getSystemGoldSettingServiceDaoImpl() {
        return systemGoldSettingServiceDaoImpl;
    }

    public void setSystemGoldSettingServiceDaoImpl(ISystemGoldSettingServiceDao systemGoldSettingServiceDaoImpl) {
        this.systemGoldSettingServiceDaoImpl = systemGoldSettingServiceDaoImpl;
    }
}
