package com.java110.store.smo.impl;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.store.IAllocationStorehouseApplyInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.allocationStorehouse.AllocationStorehouseApplyDto;
import com.java110.dto.user.UserDto;
import com.java110.store.dao.IAllocationStorehouseApplyServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 调拨申请内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AllocationStorehouseApplyInnerServiceSMOImpl extends BaseServiceSMO implements IAllocationStorehouseApplyInnerServiceSMO {

    @Autowired
    private IAllocationStorehouseApplyServiceDao allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl;


    @Override
    public List<AllocationStorehouseApplyDto> queryAllocationStorehouseApplys(@RequestBody AllocationStorehouseApplyDto allocationAllocationStorehouseApplyhouseApplyDto) {

        //校验是否传了 分页信息

        int page = allocationAllocationStorehouseApplyhouseApplyDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            allocationAllocationStorehouseApplyhouseApplyDto.setPage((page - 1) * allocationAllocationStorehouseApplyhouseApplyDto.getRow());
        }

        List<AllocationStorehouseApplyDto> allocationAllocationStorehouseApplyhouseApplys = BeanConvertUtil.covertBeanList(allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl.getAllocationStorehouseApplyInfo(BeanConvertUtil.beanCovertMap(allocationAllocationStorehouseApplyhouseApplyDto)), AllocationStorehouseApplyDto.class);

        return allocationAllocationStorehouseApplyhouseApplys;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param allocationAllocationStorehouseApplyhouseApply 小区调拨申请信息
     * @param users                                         用户列表
     */
    private void refreshAllocationStorehouseApply(AllocationStorehouseApplyDto allocationAllocationStorehouseApplyhouseApply, List<UserDto> users) {
        for (UserDto user : users) {
            if (allocationAllocationStorehouseApplyhouseApply.getApplyId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, allocationAllocationStorehouseApplyhouseApply);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param allocationAllocationStorehouseApplyhouseApplys 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<AllocationStorehouseApplyDto> allocationAllocationStorehouseApplyhouseApplys) {
        List<String> userIds = new ArrayList<String>();
        for (AllocationStorehouseApplyDto allocationAllocationStorehouseApplyhouseApply : allocationAllocationStorehouseApplyhouseApplys) {
            userIds.add(allocationAllocationStorehouseApplyhouseApply.getApplyId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryAllocationStorehouseApplysCount(@RequestBody AllocationStorehouseApplyDto allocationAllocationStorehouseApplyhouseApplyDto) {
        return allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl.queryAllocationStorehouseApplysCount(BeanConvertUtil.beanCovertMap(allocationAllocationStorehouseApplyhouseApplyDto));
    }

    /**
     * 添加调拨申请记录
     *
     * @param allocationStorehouseApplyDto
     */
    @Override
    public void saveAllocationStorehouseApplys(@RequestBody AllocationStorehouseApplyDto allocationStorehouseApplyDto) {
        allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl.saveAllocationStorehouseApplyInfo(BeanConvertUtil.beanCovertMap(allocationStorehouseApplyDto));
    }

    public IAllocationStorehouseApplyServiceDao getAllocationStorehouseApplyServiceDaoImpl() {
        return allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl;
    }

    public void setAllocationStorehouseApplyServiceDaoImpl(IAllocationStorehouseApplyServiceDao allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl) {
        this.allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl = allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl;
    }


}
