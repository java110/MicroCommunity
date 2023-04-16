package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.contract.ContractPartyaDto;
import com.java110.intf.store.IContractPartyaInnerServiceSMO;
import com.java110.po.contractPartya.ContractPartyaPo;
import com.java110.store.dao.IContractPartyaServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 合同房屋内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ContractPartyaInnerServiceSMOImpl extends BaseServiceSMO implements IContractPartyaInnerServiceSMO {

    @Autowired
    private IContractPartyaServiceDao contractPartyaServiceDaoImpl;


    @Override
    public int saveContractPartya(@RequestBody ContractPartyaPo contractPartyaPo) {
        int saveFlag = 1;
        contractPartyaServiceDaoImpl.saveContractPartyaInfo(BeanConvertUtil.beanCovertMap(contractPartyaPo));
        return saveFlag;
    }

    @Override
    public int updateContractPartya(@RequestBody ContractPartyaPo contractPartyaPo) {
        int saveFlag = 1;
        contractPartyaServiceDaoImpl.updateContractPartyaInfo(BeanConvertUtil.beanCovertMap(contractPartyaPo));
        return saveFlag;
    }

    @Override
    public int deleteContractPartya(@RequestBody ContractPartyaPo contractPartyaPo) {
        int saveFlag = 1;
        contractPartyaPo.setStatusCd("1");
        contractPartyaServiceDaoImpl.updateContractPartyaInfo(BeanConvertUtil.beanCovertMap(contractPartyaPo));
        return saveFlag;
    }

    @Override
    public List<ContractPartyaDto> queryContractPartyas(@RequestBody ContractPartyaDto contractPartyaDto) {

        //校验是否传了 分页信息

        int page = contractPartyaDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            contractPartyaDto.setPage((page - 1) * contractPartyaDto.getRow());
        }

        List<ContractPartyaDto> contractPartyas = BeanConvertUtil.covertBeanList(contractPartyaServiceDaoImpl.getContractPartyaInfo(BeanConvertUtil.beanCovertMap(contractPartyaDto)), ContractPartyaDto.class);

        return contractPartyas;
    }


    @Override
    public int queryContractPartyasCount(@RequestBody ContractPartyaDto contractPartyaDto) {
        return contractPartyaServiceDaoImpl.queryContractPartyasCount(BeanConvertUtil.beanCovertMap(contractPartyaDto));
    }

    public IContractPartyaServiceDao getContractPartyaServiceDaoImpl() {
        return contractPartyaServiceDaoImpl;
    }

    public void setContractPartyaServiceDaoImpl(IContractPartyaServiceDao contractPartyaServiceDaoImpl) {
        this.contractPartyaServiceDaoImpl = contractPartyaServiceDaoImpl;
    }
}
