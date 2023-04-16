package com.java110.store.smo.impl;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.contract.ContractTypeDto;
import com.java110.intf.store.IContractTypeInnerServiceSMO;
import com.java110.po.contractType.ContractTypePo;
import com.java110.store.dao.IContractTypeServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 合同类型内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ContractTypeInnerServiceSMOImpl extends BaseServiceSMO implements IContractTypeInnerServiceSMO {

    @Autowired
    private IContractTypeServiceDao contractTypeServiceDaoImpl;


    @Override
    public int saveContractType(@RequestBody ContractTypePo contractTypePo) {
        int saveFlag = 1;
        contractTypeServiceDaoImpl.saveContractTypeInfo(BeanConvertUtil.beanCovertMap(contractTypePo));
        return saveFlag;
    }

    @Override
    public int updateContractType(@RequestBody ContractTypePo contractTypePo) {
        int saveFlag = 1;
        contractTypeServiceDaoImpl.updateContractTypeInfo(BeanConvertUtil.beanCovertMap(contractTypePo));
        return saveFlag;
    }

    @Override
    public int deleteContractType(@RequestBody ContractTypePo contractTypePo) {
        int saveFlag = 1;
        contractTypePo.setStatusCd("1");
        contractTypeServiceDaoImpl.updateContractTypeInfo(BeanConvertUtil.beanCovertMap(contractTypePo));
        return saveFlag;
    }

    @Override
    public List<ContractTypeDto> queryContractTypes(@RequestBody ContractTypeDto contractTypeDto) {

        //校验是否传了 分页信息

        int page = contractTypeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            contractTypeDto.setPage((page - 1) * contractTypeDto.getRow());
        }

        List<ContractTypeDto> contractTypes = BeanConvertUtil.covertBeanList(contractTypeServiceDaoImpl.getContractTypeInfo(BeanConvertUtil.beanCovertMap(contractTypeDto)), ContractTypeDto.class);

        return contractTypes;
    }


    @Override
    public int queryContractTypesCount(@RequestBody ContractTypeDto contractTypeDto) {
        return contractTypeServiceDaoImpl.queryContractTypesCount(BeanConvertUtil.beanCovertMap(contractTypeDto));
    }

    public IContractTypeServiceDao getContractTypeServiceDaoImpl() {
        return contractTypeServiceDaoImpl;
    }

    public void setContractTypeServiceDaoImpl(IContractTypeServiceDao contractTypeServiceDaoImpl) {
        this.contractTypeServiceDaoImpl = contractTypeServiceDaoImpl;
    }
}
