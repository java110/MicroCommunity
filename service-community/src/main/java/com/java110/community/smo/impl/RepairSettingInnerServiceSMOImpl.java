package com.java110.community.smo.impl;


import com.java110.community.dao.IRepairSettingServiceDao;
import com.java110.core.smo.repairSetting.IRepairSettingInnerServiceSMO;
import com.java110.dto.repair.RepairSettingDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 报修设置内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class RepairSettingInnerServiceSMOImpl extends BaseServiceSMO implements IRepairSettingInnerServiceSMO {

    @Autowired
    private IRepairSettingServiceDao repairSettingServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<RepairSettingDto> queryRepairSettings(@RequestBody  RepairSettingDto repairSettingDto) {

        //校验是否传了 分页信息

        int page = repairSettingDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            repairSettingDto.setPage((page - 1) * repairSettingDto.getRow());
        }

        List<RepairSettingDto> repairSettings = BeanConvertUtil.covertBeanList(repairSettingServiceDaoImpl.getRepairSettingInfo(BeanConvertUtil.beanCovertMap(repairSettingDto)), RepairSettingDto.class);

        if (repairSettings == null || repairSettings.size() == 0) {
            return repairSettings;
        }

        String[] userIds = getUserIds(repairSettings);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (RepairSettingDto repairSetting : repairSettings) {
            refreshRepairSetting(repairSetting, users);
        }
        return repairSettings;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param repairSetting 小区报修设置信息
     * @param users 用户列表
     */
    private void refreshRepairSetting(RepairSettingDto repairSetting, List<UserDto> users) {
        for (UserDto user : users) {
            if (repairSetting.getSettingId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, repairSetting);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param repairSettings 小区楼信息
     * @return 批量userIds 信息
     */
     private String[] getUserIds(List<RepairSettingDto> repairSettings) {
        List<String> userIds = new ArrayList<String>();
        for (RepairSettingDto repairSetting : repairSettings) {
            userIds.add(repairSetting.getSettingId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryRepairSettingsCount(@RequestBody RepairSettingDto repairSettingDto) {
        return repairSettingServiceDaoImpl.queryRepairSettingsCount(BeanConvertUtil.beanCovertMap(repairSettingDto));    }

    public IRepairSettingServiceDao getRepairSettingServiceDaoImpl() {
        return repairSettingServiceDaoImpl;
    }

    public void setRepairSettingServiceDaoImpl(IRepairSettingServiceDao repairSettingServiceDaoImpl) {
        this.repairSettingServiceDaoImpl = repairSettingServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
