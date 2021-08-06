package com.java110.user.smo.impl;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.PageDto;
import com.java110.dto.owner.OwnerAttrDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IInitializeOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.owner.OwnerPo;
import com.java110.user.dao.IInitializeOwneServiceDao;
import com.java110.user.dao.IOwnerServiceDao;
import com.java110.utils.constant.OwnerTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
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


        return deleteFast;
    }

    public IInitializeOwneServiceDao getInitializeOwnerServiceDaoImpl() {
        return initializeOwnerServiceDaoImpl;
    }

    public void setInitializeOwnerServiceDaoImpl(IInitializeOwneServiceDao initializeOwnerServiceDaoImpl) {
        this.initializeOwnerServiceDaoImpl = initializeOwnerServiceDaoImpl;
    }
}
