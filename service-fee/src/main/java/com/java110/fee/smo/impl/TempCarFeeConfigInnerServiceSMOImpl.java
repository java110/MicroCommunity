package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.PageDto;
import com.java110.dto.fee.TempCarFeeResult;
import com.java110.dto.machine.CarInoutDetailDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigAttrDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeRuleDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeRuleSpecDto;
import com.java110.fee.dao.ITempCarFeeConfigServiceDao;
import com.java110.intf.fee.IComputeTempCarFee;
import com.java110.intf.fee.ITempCarFeeConfigAttrInnerServiceSMO;
import com.java110.intf.fee.ITempCarFeeConfigInnerServiceSMO;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
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
    private static Logger logger = LoggerFactory.getLogger(TempCarFeeConfigInnerServiceSMOImpl.class);

    @Autowired
    private ITempCarFeeConfigServiceDao tempCarFeeConfigServiceDaoImpl;

    @Autowired
    private ITempCarFeeConfigAttrInnerServiceSMO tempCarFeeConfigAttrInnerServiceSMOImpl;


    @Override
    public List<TempCarFeeConfigDto> queryTempCarFeeConfigs(@RequestBody TempCarFeeConfigDto tempCarFeeConfigDto) {

        //校验是否传了 分页信息

        int page = tempCarFeeConfigDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            tempCarFeeConfigDto.setPage((page - 1) * tempCarFeeConfigDto.getRow());
        }

        List<TempCarFeeConfigDto> tempCarFeeConfigs = BeanConvertUtil.covertBeanList(tempCarFeeConfigServiceDaoImpl.getTempCarFeeConfigInfo(BeanConvertUtil.beanCovertMap(tempCarFeeConfigDto)), TempCarFeeConfigDto.class);

        refreshTempCarFeeConfig(tempCarFeeConfigs);

        return tempCarFeeConfigs;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param tempCarFeeConfigs 小区临时车收费标准信息
     */
    private void refreshTempCarFeeConfig(List<TempCarFeeConfigDto> tempCarFeeConfigs) {
        if (tempCarFeeConfigs == null || tempCarFeeConfigs.size() < 1) {
            return;
        }
        List<String> configIds = new ArrayList<>();
        for (TempCarFeeConfigDto tempCarFeeConfigDto : tempCarFeeConfigs) {
            configIds.add(tempCarFeeConfigDto.getConfigId());
        }

        TempCarFeeConfigAttrDto tempCarFeeConfigAttrDto = new TempCarFeeConfigAttrDto();
        tempCarFeeConfigAttrDto.setConfigIds(configIds.toArray(new String[configIds.size()]));
        tempCarFeeConfigAttrDto.setCommunityId(tempCarFeeConfigs.get(0).getCommunityId());
        List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrDtos = tempCarFeeConfigAttrInnerServiceSMOImpl.queryTempCarFeeConfigAttrs(tempCarFeeConfigAttrDto);

        if (tempCarFeeConfigAttrDtos == null || tempCarFeeConfigAttrDtos.size() < 1) {
            return;
        }
        List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrDtoList = null;
        for (TempCarFeeConfigDto tempCarFeeConfigDto : tempCarFeeConfigs) {
            tempCarFeeConfigAttrDtoList = new ArrayList<>();
            for (TempCarFeeConfigAttrDto tCarFeeConfigAttrDto : tempCarFeeConfigAttrDtos) {
                if (tempCarFeeConfigDto.getConfigId().equals(tCarFeeConfigAttrDto.getConfigId())) {
                    tempCarFeeConfigAttrDtoList.add(tCarFeeConfigAttrDto);
                }
            }
            tempCarFeeConfigDto.setTempCarFeeConfigAttrs(tempCarFeeConfigAttrDtoList);
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

    @Override
    public List<CarInoutDto> computeTempCarFee(@RequestBody List<CarInoutDto> carInoutDtos) {
        for (CarInoutDto carInoutDto : carInoutDtos) {
            try {
                TempCarFeeConfigDto tempCarFeeConfigDto = new TempCarFeeConfigDto();
                tempCarFeeConfigDto.setPaId(carInoutDto.getPaId());
                tempCarFeeConfigDto.setCommunityId(carInoutDto.getCommunityId());
                List<TempCarFeeConfigDto> tempCarFeeConfigDtos = queryTempCarFeeConfigs(tempCarFeeConfigDto);

                if (tempCarFeeConfigDtos == null || tempCarFeeConfigDtos.size() < 1) {
                    continue;
                }
                TempCarFeeConfigAttrDto tempCarFeeConfigAttrDto = new TempCarFeeConfigAttrDto();
                tempCarFeeConfigAttrDto.setConfigId(tempCarFeeConfigDtos.get(0).getConfigId());
                tempCarFeeConfigAttrDto.setCommunityId(tempCarFeeConfigDto.getCommunityId());

                List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrDtos = tempCarFeeConfigAttrInnerServiceSMOImpl.queryTempCarFeeConfigAttrs(tempCarFeeConfigAttrDto);
                IComputeTempCarFee computeTempCarFee = ApplicationContextFactory.getBean(tempCarFeeConfigDtos.get(0).getRuleId(), IComputeTempCarFee.class);


                TempCarFeeResult result = computeTempCarFee.computeTempCarFee(carInoutDto, tempCarFeeConfigDtos.get(0), tempCarFeeConfigAttrDtos);
                carInoutDto.setMin(result.getMin());
                carInoutDto.setHours(result.getHours());
                if (CarInoutDto.CAR_TYPE_MONTH.equals(carInoutDto.getCarType())) {
                    carInoutDto.setPayCharge("0.00");
                } else {
                    carInoutDto.setPayCharge(result.getPayCharge() + "");
                }
            } catch (Exception e) {
                logger.error("临时车算费失败", e);
            }
        }

        return carInoutDtos;
    }

    @Override
    public List<CarInoutDetailDto> computeTempCarInoutDetailFee(@RequestBody List<CarInoutDetailDto> carInoutDtos) {
        TempCarFeeConfigDto tempCarFeeConfigDto = new TempCarFeeConfigDto();
        tempCarFeeConfigDto.setPaId(carInoutDtos.get(0).getPaId());
        tempCarFeeConfigDto.setCommunityId(carInoutDtos.get(0).getCommunityId());
        List<TempCarFeeConfigDto> tempCarFeeConfigDtos = queryTempCarFeeConfigs(tempCarFeeConfigDto);

        if (tempCarFeeConfigDtos == null || tempCarFeeConfigDtos.size() < 1) {
            return carInoutDtos;
        }
        TempCarFeeConfigAttrDto tempCarFeeConfigAttrDto = new TempCarFeeConfigAttrDto();
        tempCarFeeConfigAttrDto.setConfigId(tempCarFeeConfigDto.getConfigId());
        tempCarFeeConfigAttrDto.setCommunityId(tempCarFeeConfigDto.getCommunityId());

        List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrDtos = tempCarFeeConfigAttrInnerServiceSMOImpl.queryTempCarFeeConfigAttrs(tempCarFeeConfigAttrDto);
        IComputeTempCarFee computeTempCarFee = ApplicationContextFactory.getBean(tempCarFeeConfigDtos.get(0).getRuleId(), IComputeTempCarFee.class);
        for (CarInoutDetailDto carInoutDto : carInoutDtos) {
            try {
                if (CarInoutDetailDto.CAR_INOUT_IN.equals(carInoutDto.getCarInout())
                        && !CarInoutDetailDto.STATE_OUT.equals(carInoutDto.getInState())
                ) {
                    //进场失败
                    if (CarInoutDetailDto.STATE_IN_FAIL.equals(carInoutDto.getInState())) {
                        carInoutDto.setMin(0);
                        carInoutDto.setHours(0);
                        carInoutDto.setPayCharge(0 + "");
                        continue;
                    }
                    TempCarFeeResult result = computeTempCarFee.computeTempCarFee(carInoutDto, tempCarFeeConfigDtos.get(0), tempCarFeeConfigAttrDtos);
                    carInoutDto.setMin(result.getMin());
                    carInoutDto.setHours(result.getHours());
                    carInoutDto.setPayCharge(result.getPayCharge() + "");
                } else {
                    //获取停车时间
                    Date stateDate = DateUtil.getDateFromString(carInoutDto.getInTime(), DateUtil.DATE_FORMATE_STRING_A);
                    Date endDate = null;
                    if(carInoutDto.getOutTime() == null){
                        endDate = DateUtil.getCurrentDate();
                    }else{
                        endDate = DateUtil.getDateFromString(carInoutDto.getOutTime(), DateUtil.DATE_FORMATE_STRING_A);
                    }

                    long min = (endDate.getTime() - stateDate.getTime()) / (60 * 1000);
                    long hours = min / 60; //因为两者都是整数，你得到一个int
                    long minutes = min % 60;
                    carInoutDto.setMin(minutes);
                    carInoutDto.setHours(hours);
                    carInoutDto.setPayCharge(carInoutDto.getRealCharge());
                }
            } catch (Exception e) {
                logger.error("临时车算费失败", e);
            }
        }

        return carInoutDtos;
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
