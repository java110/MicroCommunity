package com.java110.store.smo.impl.businesstype;


import com.java110.common.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.businessytpe.IC_business_typeInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.UserDto;
import com.java110.dto.businesstypecd.C_business_typeDto;
import com.java110.store.dao.IC_business_typeServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description BusinessType内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class C_business_typeInnerServiceSMOImpl extends BaseServiceSMO implements IC_business_typeInnerServiceSMO {

    @Autowired
    private IC_business_typeServiceDao c_business_typeServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<C_business_typeDto> queryC_business_types(@RequestBody  C_business_typeDto c_business_typeDto) {

        //校验是否传了 分页信息

        int page = c_business_typeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            c_business_typeDto.setPage((page - 1) * c_business_typeDto.getRow());
            c_business_typeDto.setRow(page * c_business_typeDto.getRow());
        }

        List<C_business_typeDto> c_business_types = BeanConvertUtil.covertBeanList(c_business_typeServiceDaoImpl.getC_business_typeInfo(BeanConvertUtil.beanCovertMap(c_business_typeDto)), C_business_typeDto.class);

        if (c_business_types == null || c_business_types.size() == 0) {
            return c_business_types;
        }

        String[] userIds = getUserIds(c_business_types);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (C_business_typeDto c_business_type : c_business_types) {
            refreshC_business_type(c_business_type, users);
        }
        return c_business_types;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param c_business_type 小区BusinessType信息
     * @param users 用户列表
     */
    private void refreshC_business_type(C_business_typeDto c_business_type, List<UserDto> users) {
        for (UserDto user : users) {
            if (c_business_type.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, c_business_type);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param c_business_types 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<C_business_typeDto> c_business_types) {
        List<String> userIds = new ArrayList<String>();
        for (C_business_typeDto c_business_type : c_business_types) {
            userIds.add(c_business_type.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryC_business_typesCount(@RequestBody C_business_typeDto c_business_typeDto) {
        return c_business_typeServiceDaoImpl.queryC_business_typesCount(BeanConvertUtil.beanCovertMap(c_business_typeDto));    }

    public IC_business_typeServiceDao getC_business_typeServiceDaoImpl() {
        return c_business_typeServiceDaoImpl;
    }

    public void setC_business_typeServiceDaoImpl(IC_business_typeServiceDao c_business_typeServiceDaoImpl) {
        this.c_business_typeServiceDaoImpl = c_business_typeServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
