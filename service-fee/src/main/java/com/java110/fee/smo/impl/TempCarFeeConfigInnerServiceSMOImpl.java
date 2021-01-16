package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeRuleDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeRuleSpecDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.dao.ITempCarFeeConfigServiceDao;
import com.java110.intf.fee.ITempCarFeeConfigInnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 临时车收费标准内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class TempCarFeeConfigInnerServiceSMOImpl extends BaseServiceSMO implements ITempCarFeeConfigInnerServiceSMO {

    @Autowired
    private ITempCarFeeConfigServiceDao tempCarFeeConfigServiceDaoImpl;


    @Override
    public List<TempCarFeeConfigDto> queryTempCarFeeConfigs(@RequestBody TempCarFeeConfigDto tempCarFeeConfigDto) {

        //校验是否传了 分页信息

        int page = tempCarFeeConfigDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            tempCarFeeConfigDto.setPage((page - 1) * tempCarFeeConfigDto.getRow());
        }

        List<TempCarFeeConfigDto> tempCarFeeConfigs = BeanConvertUtil.covertBeanList(tempCarFeeConfigServiceDaoImpl.getTempCarFeeConfigInfo(BeanConvertUtil.beanCovertMap(tempCarFeeConfigDto)), TempCarFeeConfigDto.class);

        return tempCarFeeConfigs;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param tempCarFeeConfig 小区临时车收费标准信息
     * @param users            用户列表
     */
    private void refreshTempCarFeeConfig(TempCarFeeConfigDto tempCarFeeConfig, List<UserDto> users) {
        for (UserDto user : users) {
            if (tempCarFeeConfig.getConfigId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, tempCarFeeConfig);
            }
        }
    }

    @Override
    public int queryTempCarFeeConfigsCount(@RequestBody TempCarFeeConfigDto tempCarFeeConfigDto) {
        return tempCarFeeConfigServiceDaoImpl.queryTempCarFeeConfigsCount(BeanConvertUtil.beanCovertMap(tempCarFeeConfigDto));
    }

    @Override
    public List<TempCarFeeRuleDto> queryTempCarFeeRules(@RequestBody TempCarFeeRuleDto tempCarFeeRuleDto) {

        List<TempCarFeeRuleDto> tempCarFeeRuleDtos = BeanConvertUtil.covertBeanList(tempCarFeeConfigServiceDaoImpl.queryTempCarFeeRules(BeanConvertUtil.beanCovertMap(tempCarFeeRuleDto)), TempCarFeeRuleDto.class);
        freshRuleSpecs(tempCarFeeRuleDtos);
        return tempCarFeeRuleDtos;
    }

    private void freshRuleSpecs(List<TempCarFeeRuleDto> tempCarFeeRuleDtos) {
        if (tempCarFeeRuleDtos == null || tempCarFeeRuleDtos.size() < 1) {
            return;
        }
        List<String> ruleIds = new ArrayList<>();
        for (TempCarFeeRuleDto tempCarFeeRuleDto : tempCarFeeRuleDtos) {
            ruleIds.add(tempCarFeeRuleDto.getRuleId());
        }

        TempCarFeeRuleSpecDto tempCarFeeRuleSpecDto = new TempCarFeeRuleSpecDto();
        tempCarFeeRuleSpecDto.setRuleIds(ruleIds.toArray(new String[ruleIds.size()]));
        List<TempCarFeeRuleSpecDto> tempCarFeeRuleSpecDtos = BeanConvertUtil.covertBeanList(
                tempCarFeeConfigServiceDaoImpl.queryTempCarFeeRuleSpecs(
                        BeanConvertUtil.beanCovertMap(tempCarFeeRuleSpecDto)), TempCarFeeRuleSpecDto.class);
        List<TempCarFeeRuleSpecDto> tCarFeeRuleSpecDtos = null;
        for (TempCarFeeRuleDto tempCarFeeRuleDto : tempCarFeeRuleDtos) {
            tCarFeeRuleSpecDtos = new ArrayList<>();
            for (TempCarFeeRuleSpecDto tCarFeeRuleSpecDto : tempCarFeeRuleSpecDtos) {
                if (tempCarFeeRuleDto.getRuleId().equals(tCarFeeRuleSpecDto.getRuleId())) {
                    tCarFeeRuleSpecDtos.add(tCarFeeRuleSpecDto);
                }
            }
            tempCarFeeRuleDto.setTempCarFeeRuleSpecs(tCarFeeRuleSpecDtos);
        }
    }

    public ITempCarFeeConfigServiceDao getTempCarFeeConfigServiceDaoImpl() {
        return tempCarFeeConfigServiceDaoImpl;
    }

    public void setTempCarFeeConfigServiceDaoImpl(ITempCarFeeConfigServiceDao tempCarFeeConfigServiceDaoImpl) {
        this.tempCarFeeConfigServiceDaoImpl = tempCarFeeConfigServiceDaoImpl;
    }


}
