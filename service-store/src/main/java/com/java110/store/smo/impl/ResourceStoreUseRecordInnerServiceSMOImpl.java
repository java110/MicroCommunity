package com.java110.store.smo.impl;

import com.java110.dto.resourceStoreUseRecord.ResourceStoreUseRecordDto;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.store.dao.IResourceStoreUseRecordServiceDao;
import com.java110.intf.store.IResourceStoreUseRecordInnerServiceSMO;
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
 * @Description 物品使用记录内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ResourceStoreUseRecordInnerServiceSMOImpl extends BaseServiceSMO implements IResourceStoreUseRecordInnerServiceSMO {

    @Autowired
    private IResourceStoreUseRecordServiceDao resourceResourceStoreUseRecordUseRecordServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<ResourceStoreUseRecordDto> queryResourceStoreUseRecords(@RequestBody ResourceStoreUseRecordDto resourceResourceStoreUseRecordUseRecordDto) {

        //校验是否传了 分页信息

        int page = resourceResourceStoreUseRecordUseRecordDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            resourceResourceStoreUseRecordUseRecordDto.setPage((page - 1) * resourceResourceStoreUseRecordUseRecordDto.getRow());
        }

        List<ResourceStoreUseRecordDto> resourceResourceStoreUseRecordUseRecords = BeanConvertUtil.covertBeanList(resourceResourceStoreUseRecordUseRecordServiceDaoImpl.getResourceStoreUseRecordInfo(BeanConvertUtil.beanCovertMap(resourceResourceStoreUseRecordUseRecordDto)), ResourceStoreUseRecordDto.class);

        if (resourceResourceStoreUseRecordUseRecords == null || resourceResourceStoreUseRecordUseRecords.size() == 0) {
            return resourceResourceStoreUseRecordUseRecords;
        }

        String[] userIds = getUserIds(resourceResourceStoreUseRecordUseRecords);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (ResourceStoreUseRecordDto resourceResourceStoreUseRecordUseRecord : resourceResourceStoreUseRecordUseRecords) {
            refreshResourceStoreUseRecord(resourceResourceStoreUseRecordUseRecord, users);
        }
        return resourceResourceStoreUseRecordUseRecords;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param resourceResourceStoreUseRecordUseRecord 小区物品使用记录信息
     * @param users                                   用户列表
     */
    private void refreshResourceStoreUseRecord(ResourceStoreUseRecordDto resourceResourceStoreUseRecordUseRecord, List<UserDto> users) {
        for (UserDto user : users) {
            if (resourceResourceStoreUseRecordUseRecord.getRsurId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, resourceResourceStoreUseRecordUseRecord);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param resourceResourceStoreUseRecordUseRecords 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<ResourceStoreUseRecordDto> resourceResourceStoreUseRecordUseRecords) {
        List<String> userIds = new ArrayList<String>();
        for (ResourceStoreUseRecordDto resourceResourceStoreUseRecordUseRecord : resourceResourceStoreUseRecordUseRecords) {
            userIds.add(resourceResourceStoreUseRecordUseRecord.getRsurId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryResourceStoreUseRecordsCount(@RequestBody ResourceStoreUseRecordDto resourceResourceStoreUseRecordUseRecordDto) {
        return resourceResourceStoreUseRecordUseRecordServiceDaoImpl.queryResourceStoreUseRecordsCount(BeanConvertUtil.beanCovertMap(resourceResourceStoreUseRecordUseRecordDto));
    }

    public IResourceStoreUseRecordServiceDao getResourceStoreUseRecordServiceDaoImpl() {
        return resourceResourceStoreUseRecordUseRecordServiceDaoImpl;
    }

    public void setResourceStoreUseRecordServiceDaoImpl(IResourceStoreUseRecordServiceDao resourceResourceStoreUseRecordUseRecordServiceDaoImpl) {
        this.resourceResourceStoreUseRecordUseRecordServiceDaoImpl = resourceResourceStoreUseRecordUseRecordServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
