package com.java110.store.smo.impl;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.userStorehouse.UserStorehouseDto;
import com.java110.intf.store.IUserStorehouseInnerServiceSMO;
import com.java110.po.userStorehouse.UserStorehousePo;
import com.java110.store.dao.IUserStorehouseServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 个人物品内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class UserStorehouseInnerServiceSMOImpl extends BaseServiceSMO implements IUserStorehouseInnerServiceSMO {

    @Autowired
    private IUserStorehouseServiceDao useStorehouseServiceDaoImpl;


    @Override
    public List<UserStorehouseDto> queryUserStorehouses(@RequestBody UserStorehouseDto userUserStorehousehouseDto) {

        //校验是否传了 分页信息

        int page = userUserStorehousehouseDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            userUserStorehousehouseDto.setPage((page - 1) * userUserStorehousehouseDto.getRow());
        }

        List<UserStorehouseDto> userStorehouses = BeanConvertUtil.covertBeanList(useStorehouseServiceDaoImpl.getUserStorehouseInfo(BeanConvertUtil.beanCovertMap(userUserStorehousehouseDto)), UserStorehouseDto.class);
        return userStorehouses;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param userUserStorehousehouse 小区个人物品信息
     * @param users                   用户列表
     */
    private void refreshUserStorehouse(UserStorehouseDto userUserStorehousehouse, List<UserDto> users) {
        for (UserDto user : users) {
            if (userUserStorehousehouse.getUsId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, userUserStorehousehouse);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param userStorehouses 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<UserStorehouseDto> userStorehouses) {
        List<String> userIds = new ArrayList<String>();
        for (UserStorehouseDto userUserStorehousehouse : userStorehouses) {
            userIds.add(userUserStorehousehouse.getUsId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryUserStorehousesCount(@RequestBody UserStorehouseDto userUserStorehousehouseDto) {
        return useStorehouseServiceDaoImpl.queryUserStorehousesCount(BeanConvertUtil.beanCovertMap(userUserStorehousehouseDto));
    }

    @Override
    public int saveUserStorehouses(@RequestBody UserStorehousePo userStorehousePo) {
        return useStorehouseServiceDaoImpl.saveUserStorehouses(BeanConvertUtil.beanCovertMap(userStorehousePo));
    }

    @Override
    public int updateUserStorehouses(@RequestBody UserStorehousePo userStorehousePo) {
        int flag = 1;
        useStorehouseServiceDaoImpl.updateUserStorehouseInfoInstance(BeanConvertUtil.beanCovertMap(userStorehousePo));
        return flag;
    }

    public IUserStorehouseServiceDao getUserStorehouseServiceDaoImpl() {
        return useStorehouseServiceDaoImpl;
    }

    public void setUserStorehouseServiceDaoImpl(IUserStorehouseServiceDao useStorehouseServiceDaoImpl) {
        this.useStorehouseServiceDaoImpl = useStorehouseServiceDaoImpl;
    }

}
