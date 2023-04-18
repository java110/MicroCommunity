package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.activities.ActivitiesRuleDto;
import com.java110.intf.user.IActivitiesRuleInnerServiceSMO;
import com.java110.po.activitiesRule.ActivitiesRulePo;
import com.java110.user.dao.IActivitiesRuleServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 活动规则内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ActivitiesRuleInnerServiceSMOImpl extends BaseServiceSMO implements IActivitiesRuleInnerServiceSMO {

    @Autowired
    private IActivitiesRuleServiceDao activitiesRuleServiceDaoImpl;


    @Override
    public int saveActivitiesRule(@RequestBody ActivitiesRulePo activitiesRulePo) {
        int saveFlag = 1;
        activitiesRuleServiceDaoImpl.saveActivitiesRuleInfo(BeanConvertUtil.beanCovertMap(activitiesRulePo));
        return saveFlag;
    }

    @Override
    public int updateActivitiesRule(@RequestBody ActivitiesRulePo activitiesRulePo) {
        int saveFlag = 1;
        activitiesRuleServiceDaoImpl.updateActivitiesRuleInfo(BeanConvertUtil.beanCovertMap(activitiesRulePo));
        return saveFlag;
    }

    @Override
    public int deleteActivitiesRule(@RequestBody ActivitiesRulePo activitiesRulePo) {
        int saveFlag = 1;
        activitiesRulePo.setStatusCd("1");
        activitiesRuleServiceDaoImpl.updateActivitiesRuleInfo(BeanConvertUtil.beanCovertMap(activitiesRulePo));
        return saveFlag;
    }

    @Override
    public List<ActivitiesRuleDto> queryActivitiesRules(@RequestBody ActivitiesRuleDto activitiesRuleDto) {

        //校验是否传了 分页信息

        int page = activitiesRuleDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            activitiesRuleDto.setPage((page - 1) * activitiesRuleDto.getRow());
        }

        List<ActivitiesRuleDto> activitiesRules = BeanConvertUtil.covertBeanList(activitiesRuleServiceDaoImpl.getActivitiesRuleInfo(BeanConvertUtil.beanCovertMap(activitiesRuleDto)), ActivitiesRuleDto.class);

        return activitiesRules;
    }


    @Override
    public int queryActivitiesRulesCount(@RequestBody ActivitiesRuleDto activitiesRuleDto) {
        return activitiesRuleServiceDaoImpl.queryActivitiesRulesCount(BeanConvertUtil.beanCovertMap(activitiesRuleDto));
    }

    public IActivitiesRuleServiceDao getActivitiesRuleServiceDaoImpl() {
        return activitiesRuleServiceDaoImpl;
    }

    public void setActivitiesRuleServiceDaoImpl(IActivitiesRuleServiceDao activitiesRuleServiceDaoImpl) {
        this.activitiesRuleServiceDaoImpl = activitiesRuleServiceDaoImpl;
    }
}
