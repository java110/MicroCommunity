package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.contractChangePlan.ContractChangePlanDto;
import com.java110.intf.store.IContractChangePlanInnerServiceSMO;
import com.java110.po.contractChangePlan.ContractChangePlanPo;
import com.java110.store.dao.IContractChangePlanServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 合同变更计划内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ContractChangePlanInnerServiceSMOImpl extends BaseServiceSMO implements IContractChangePlanInnerServiceSMO {

    @Autowired
    private IContractChangePlanServiceDao contractChangePlanServiceDaoImpl;


    @Override
    public int saveContractChangePlan(@RequestBody ContractChangePlanPo contractChangePlanPo) {
        int saveFlag = 1;
        contractChangePlanServiceDaoImpl.saveContractChangePlanInfo(BeanConvertUtil.beanCovertMap(contractChangePlanPo));
        return saveFlag;
    }

    @Override
    public int updateContractChangePlan(@RequestBody ContractChangePlanPo contractChangePlanPo) {
        int saveFlag = 1;
        contractChangePlanServiceDaoImpl.updateContractChangePlanInfo(BeanConvertUtil.beanCovertMap(contractChangePlanPo));
        return saveFlag;
    }

    @Override
    public int deleteContractChangePlan(@RequestBody ContractChangePlanPo contractChangePlanPo) {
        int saveFlag = 1;
        contractChangePlanPo.setStatusCd("1");
        contractChangePlanServiceDaoImpl.updateContractChangePlanInfo(BeanConvertUtil.beanCovertMap(contractChangePlanPo));
        return saveFlag;
    }

    @Override
    public List<ContractChangePlanDto> queryContractChangePlans(@RequestBody ContractChangePlanDto contractChangePlanDto) {

        //校验是否传了 分页信息

        int page = contractChangePlanDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            contractChangePlanDto.setPage((page - 1) * contractChangePlanDto.getRow());
        }

        List<ContractChangePlanDto> contractChangePlans = BeanConvertUtil.covertBeanList(contractChangePlanServiceDaoImpl.getContractChangePlanInfo(BeanConvertUtil.beanCovertMap(contractChangePlanDto)), ContractChangePlanDto.class);

        return contractChangePlans;
    }


    @Override
    public int queryContractChangePlansCount(@RequestBody ContractChangePlanDto contractChangePlanDto) {
        return contractChangePlanServiceDaoImpl.queryContractChangePlansCount(BeanConvertUtil.beanCovertMap(contractChangePlanDto));
    }

    public IContractChangePlanServiceDao getContractChangePlanServiceDaoImpl() {
        return contractChangePlanServiceDaoImpl;
    }

    public void setContractChangePlanServiceDaoImpl(IContractChangePlanServiceDao contractChangePlanServiceDaoImpl) {
        this.contractChangePlanServiceDaoImpl = contractChangePlanServiceDaoImpl;
    }
}
