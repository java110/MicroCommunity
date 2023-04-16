package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.contract.ContractCollectionPlanDto;
import com.java110.intf.store.IContractCollectionPlanInnerServiceSMO;
import com.java110.po.contractCollectionPlan.ContractCollectionPlanPo;
import com.java110.store.dao.IContractCollectionPlanServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 合同收款计划内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ContractCollectionPlanInnerServiceSMOImpl extends BaseServiceSMO implements IContractCollectionPlanInnerServiceSMO {

    @Autowired
    private IContractCollectionPlanServiceDao contractCollectionPlanServiceDaoImpl;


    @Override
    public int saveContractCollectionPlan(@RequestBody ContractCollectionPlanPo contractCollectionPlanPo) {
        int saveFlag = 1;
        contractCollectionPlanServiceDaoImpl.saveContractCollectionPlanInfo(BeanConvertUtil.beanCovertMap(contractCollectionPlanPo));
        return saveFlag;
    }

    @Override
    public int updateContractCollectionPlan(@RequestBody ContractCollectionPlanPo contractCollectionPlanPo) {
        int saveFlag = 1;
        contractCollectionPlanServiceDaoImpl.updateContractCollectionPlanInfo(BeanConvertUtil.beanCovertMap(contractCollectionPlanPo));
        return saveFlag;
    }

    @Override
    public int deleteContractCollectionPlan(@RequestBody ContractCollectionPlanPo contractCollectionPlanPo) {
        int saveFlag = 1;
        contractCollectionPlanPo.setStatusCd("1");
        contractCollectionPlanServiceDaoImpl.updateContractCollectionPlanInfo(BeanConvertUtil.beanCovertMap(contractCollectionPlanPo));
        return saveFlag;
    }

    @Override
    public List<ContractCollectionPlanDto> queryContractCollectionPlans(@RequestBody ContractCollectionPlanDto contractCollectionPlanDto) {

        //校验是否传了 分页信息

        int page = contractCollectionPlanDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            contractCollectionPlanDto.setPage((page - 1) * contractCollectionPlanDto.getRow());
        }

        List<ContractCollectionPlanDto> contractCollectionPlans = BeanConvertUtil.covertBeanList(contractCollectionPlanServiceDaoImpl.getContractCollectionPlanInfo(BeanConvertUtil.beanCovertMap(contractCollectionPlanDto)), ContractCollectionPlanDto.class);

        return contractCollectionPlans;
    }


    @Override
    public int queryContractCollectionPlansCount(@RequestBody ContractCollectionPlanDto contractCollectionPlanDto) {
        return contractCollectionPlanServiceDaoImpl.queryContractCollectionPlansCount(BeanConvertUtil.beanCovertMap(contractCollectionPlanDto));
    }

    public IContractCollectionPlanServiceDao getContractCollectionPlanServiceDaoImpl() {
        return contractCollectionPlanServiceDaoImpl;
    }

    public void setContractCollectionPlanServiceDaoImpl(IContractCollectionPlanServiceDao contractCollectionPlanServiceDaoImpl) {
        this.contractCollectionPlanServiceDaoImpl = contractCollectionPlanServiceDaoImpl;
    }
}
