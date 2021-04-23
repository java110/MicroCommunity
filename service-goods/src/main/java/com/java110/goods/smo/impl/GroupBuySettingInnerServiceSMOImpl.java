package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.groupBuySetting.GroupBuySettingDto;
import com.java110.goods.dao.IGroupBuySettingServiceDao;
import com.java110.intf.goods.IGroupBuySettingInnerServiceSMO;
import com.java110.po.groupBuySetting.GroupBuySettingPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 拼团设置内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class GroupBuySettingInnerServiceSMOImpl extends BaseServiceSMO implements IGroupBuySettingInnerServiceSMO {

    @Autowired
    private IGroupBuySettingServiceDao groupBuySettingServiceDaoImpl;


    @Override
    public int saveGroupBuySetting(@RequestBody GroupBuySettingPo groupBuySettingPo) {
        int saveFlag = 1;
        groupBuySettingServiceDaoImpl.saveGroupBuySettingInfo(BeanConvertUtil.beanCovertMap(groupBuySettingPo));
        return saveFlag;
    }

    @Override
    public int updateGroupBuySetting(@RequestBody GroupBuySettingPo groupBuySettingPo) {
        int saveFlag = 1;
        groupBuySettingServiceDaoImpl.updateGroupBuySettingInfo(BeanConvertUtil.beanCovertMap(groupBuySettingPo));
        return saveFlag;
    }

    @Override
    public int deleteGroupBuySetting(@RequestBody GroupBuySettingPo groupBuySettingPo) {
        int saveFlag = 1;
        groupBuySettingPo.setStatusCd("1");
        groupBuySettingServiceDaoImpl.updateGroupBuySettingInfo(BeanConvertUtil.beanCovertMap(groupBuySettingPo));
        return saveFlag;
    }

    @Override
    public List<GroupBuySettingDto> queryGroupBuySettings(@RequestBody GroupBuySettingDto groupBuySettingDto) {

        //校验是否传了 分页信息

        int page = groupBuySettingDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            groupBuySettingDto.setPage((page - 1) * groupBuySettingDto.getRow());
        }

        List<GroupBuySettingDto> groupBuySettings = BeanConvertUtil.covertBeanList(groupBuySettingServiceDaoImpl.getGroupBuySettingInfo(BeanConvertUtil.beanCovertMap(groupBuySettingDto)), GroupBuySettingDto.class);

        return groupBuySettings;
    }


    @Override
    public int queryGroupBuySettingsCount(@RequestBody GroupBuySettingDto groupBuySettingDto) {
        return groupBuySettingServiceDaoImpl.queryGroupBuySettingsCount(BeanConvertUtil.beanCovertMap(groupBuySettingDto));
    }

    public IGroupBuySettingServiceDao getGroupBuySettingServiceDaoImpl() {
        return groupBuySettingServiceDaoImpl;
    }

    public void setGroupBuySettingServiceDaoImpl(IGroupBuySettingServiceDao groupBuySettingServiceDaoImpl) {
        this.groupBuySettingServiceDaoImpl = groupBuySettingServiceDaoImpl;
    }
}
