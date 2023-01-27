package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.contract.ContractDto;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.po.contract.ContractPo;
import com.java110.store.dao.IContractServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 合同管理内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ContractInnerServiceSMOImpl extends BaseServiceSMO implements IContractInnerServiceSMO {

    @Autowired
    private IContractServiceDao contractServiceDaoImpl;


    @Override
    public int saveContract(@RequestBody ContractPo contractPo) {
        int saveFlag = 1;
        contractServiceDaoImpl.saveContractInfo(BeanConvertUtil.beanCovertMap(contractPo));
        return saveFlag;
    }

    @Override
    public int updateContract(@RequestBody ContractPo contractPo) {
        int saveFlag = 1;
        contractServiceDaoImpl.updateContractInfo(BeanConvertUtil.beanCovertMap(contractPo));
        return saveFlag;
    }

    @Override
    public int deleteContract(@RequestBody ContractPo contractPo) {
        int saveFlag = 1;
        contractPo.setStatusCd("1");
        contractServiceDaoImpl.updateContractInfo(BeanConvertUtil.beanCovertMap(contractPo));
        return saveFlag;
    }

    @Override
    public List<ContractDto> queryContracts(@RequestBody ContractDto contractDto) {

        //校验是否传了 分页信息

        int page = contractDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            contractDto.setPage((page - 1) * contractDto.getRow());
        }

        List<ContractDto> contracts = BeanConvertUtil.covertBeanList(contractServiceDaoImpl.getContractInfo(BeanConvertUtil.beanCovertMap(contractDto)), ContractDto.class);

        return contracts;
    }


    @Override
    public int queryContractsCount(@RequestBody ContractDto contractDto) {
        return contractServiceDaoImpl.queryContractsCount(BeanConvertUtil.beanCovertMap(contractDto));
    }

    @Override
    public List<Map> queryContractsByOwnerIds(@RequestBody Map info) {
        return contractServiceDaoImpl.queryContractsByOwnerIds(info);
    }

    public IContractServiceDao getContractServiceDaoImpl() {
        return contractServiceDaoImpl;
    }

    public void setContractServiceDaoImpl(IContractServiceDao contractServiceDaoImpl) {
        this.contractServiceDaoImpl = contractServiceDaoImpl;
    }
}
