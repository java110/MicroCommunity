package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.storehouse.StorehouseDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.store.IStorehouseInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.store.dao.IStorehouseServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 仓库内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class StorehouseInnerServiceSMOImpl extends BaseServiceSMO implements IStorehouseInnerServiceSMO {

    @Autowired
    private IStorehouseServiceDao storehouseServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<StorehouseDto> queryStorehouses(@RequestBody StorehouseDto storehouseDto) {

        //校验是否传了 分页信息

        int page = storehouseDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            storehouseDto.setPage((page - 1) * storehouseDto.getRow());
        }

        List<StorehouseDto> storehouses = BeanConvertUtil.covertBeanList(storehouseServiceDaoImpl.getStorehouseInfo(BeanConvertUtil.beanCovertMap(storehouseDto)), StorehouseDto.class);

        return storehouses;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param storehouse 小区仓库信息
     * @param users      用户列表
     */
    private void refreshStorehouse(StorehouseDto storehouse, List<UserDto> users) {
        for (UserDto user : users) {
            if (storehouse.getShId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, storehouse);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param storehouses 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<StorehouseDto> storehouses) {
        List<String> userIds = new ArrayList<String>();
        for (StorehouseDto storehouse : storehouses) {
            userIds.add(storehouse.getShId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryStorehousesCount(@RequestBody StorehouseDto storehouseDto) {
        return storehouseServiceDaoImpl.queryStorehousesCount(BeanConvertUtil.beanCovertMap(storehouseDto));
    }

    public IStorehouseServiceDao getStorehouseServiceDaoImpl() {
        return storehouseServiceDaoImpl;
    }

    public void setStorehouseServiceDaoImpl(IStorehouseServiceDao storehouseServiceDaoImpl) {
        this.storehouseServiceDaoImpl = storehouseServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
