package com.java110.common.smo.impl;


import com.java110.common.dao.IApplicationKeyServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.common.IApplicationKeyInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.machine.ApplicationKeyDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 钥匙申请内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ApplicationKeyInnerServiceSMOImpl extends BaseServiceSMO implements IApplicationKeyInnerServiceSMO {

    @Autowired
    private IApplicationKeyServiceDao applicationKeyServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<ApplicationKeyDto> queryApplicationKeys(@RequestBody ApplicationKeyDto applicationKeyDto) {

        //校验是否传了 分页信息

        int page = applicationKeyDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            applicationKeyDto.setPage((page - 1) * applicationKeyDto.getRow());
        }

        List<ApplicationKeyDto> applicationKeys = BeanConvertUtil.covertBeanList(applicationKeyServiceDaoImpl.getApplicationKeyInfo(BeanConvertUtil.beanCovertMap(applicationKeyDto)), ApplicationKeyDto.class);

        return applicationKeys;
    }


    @Override
    public int queryApplicationKeysCount(@RequestBody ApplicationKeyDto applicationKeyDto) {
        return applicationKeyServiceDaoImpl.queryApplicationKeysCount(BeanConvertUtil.beanCovertMap(applicationKeyDto));
    }

    public IApplicationKeyServiceDao getApplicationKeyServiceDaoImpl() {
        return applicationKeyServiceDaoImpl;
    }

    public void setApplicationKeyServiceDaoImpl(IApplicationKeyServiceDao applicationKeyServiceDaoImpl) {
        this.applicationKeyServiceDaoImpl = applicationKeyServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
