package com.java110.store.smo.impl;


import com.java110.dto.allocationStorehouse.AllocationUserStorehouseDto;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.store.dao.IAllocationUserStorehouseServiceDao;
import com.java110.intf.store.IAllocationUserStorehouseInnerServiceSMO;
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
 * @Description 物品供应商内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AllocationUserStorehouseInnerServiceSMOImpl extends BaseServiceSMO implements IAllocationUserStorehouseInnerServiceSMO {

    @Autowired
    private IAllocationUserStorehouseServiceDao allocationUserStorehouseServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<AllocationUserStorehouseDto> queryAllocationUserStorehouses(@RequestBody AllocationUserStorehouseDto allocationUserAllocationUserStorehouseDto) {

        //校验是否传了 分页信息

        int page = allocationUserAllocationUserStorehouseDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            allocationUserAllocationUserStorehouseDto.setPage((page - 1) * allocationUserAllocationUserStorehouseDto.getRow());
        }

        List<AllocationUserStorehouseDto> allocationUserAllocationUserStorehouses = BeanConvertUtil.covertBeanList(allocationUserStorehouseServiceDaoImpl.getAllocationUserStorehouseInfo(BeanConvertUtil.beanCovertMap(allocationUserAllocationUserStorehouseDto)), AllocationUserStorehouseDto.class);

        if (allocationUserAllocationUserStorehouses == null || allocationUserAllocationUserStorehouses.size() == 0) {
            return allocationUserAllocationUserStorehouses;
        }

        String[] userIds = getUserIds(allocationUserAllocationUserStorehouses);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (AllocationUserStorehouseDto allocationUserAllocationUserStorehouse : allocationUserAllocationUserStorehouses) {
            refreshAllocationUserStorehouse(allocationUserAllocationUserStorehouse, users);
        }
        return allocationUserAllocationUserStorehouses;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param allocationUserAllocationUserStorehouse 小区物品供应商信息
     * @param users                                  用户列表
     */
    private void refreshAllocationUserStorehouse(AllocationUserStorehouseDto allocationUserAllocationUserStorehouse, List<UserDto> users) {
        for (UserDto user : users) {
            if (allocationUserAllocationUserStorehouse.getAusId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, allocationUserAllocationUserStorehouse);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param allocationUserAllocationUserStorehouses 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<AllocationUserStorehouseDto> allocationUserAllocationUserStorehouses) {
        List<String> userIds = new ArrayList<String>();
        for (AllocationUserStorehouseDto allocationUserAllocationUserStorehouse : allocationUserAllocationUserStorehouses) {
            userIds.add(allocationUserAllocationUserStorehouse.getAusId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryAllocationUserStorehousesCount(@RequestBody AllocationUserStorehouseDto allocationUserAllocationUserStorehouseDto) {
        return allocationUserStorehouseServiceDaoImpl.queryAllocationUserStorehousesCount(BeanConvertUtil.beanCovertMap(allocationUserAllocationUserStorehouseDto));
    }

    public IAllocationUserStorehouseServiceDao getAllocationUserStorehouseServiceDaoImpl() {
        return allocationUserStorehouseServiceDaoImpl;
    }

    public void setAllocationUserStorehouseServiceDaoImpl(IAllocationUserStorehouseServiceDao allocationUserStorehouseServiceDaoImpl) {
        this.allocationUserStorehouseServiceDaoImpl = allocationUserStorehouseServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
