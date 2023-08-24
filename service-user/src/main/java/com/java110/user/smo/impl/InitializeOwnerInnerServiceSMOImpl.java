package com.java110.user.smo.impl;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.user.IInitializeOwnerInnerServiceSMO;
import com.java110.user.dao.IInitializeOwneServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 业主内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class InitializeOwnerInnerServiceSMOImpl extends BaseServiceSMO implements IInitializeOwnerInnerServiceSMO {

    @Autowired
    private IInitializeOwneServiceDao initializeOwnerServiceDaoImpl;
    @Override
    public int deleteBuildingOwner(@RequestBody Map communityId) {
        int deleteFast = initializeOwnerServiceDaoImpl.deleteBuildingOwner(communityId);

        //todo 删除 业主绑定数据
        //initializeOwnerServiceDaoImpl.deleteUser(communityId);
        initializeOwnerServiceDaoImpl.deleteOwnerAppUser(communityId);
        return deleteFast;
    }

    public IInitializeOwneServiceDao getInitializeOwnerServiceDaoImpl() {
        return initializeOwnerServiceDaoImpl;
    }

    public void setInitializeOwnerServiceDaoImpl(IInitializeOwneServiceDao initializeOwnerServiceDaoImpl) {
        this.initializeOwnerServiceDaoImpl = initializeOwnerServiceDaoImpl;
    }
}
