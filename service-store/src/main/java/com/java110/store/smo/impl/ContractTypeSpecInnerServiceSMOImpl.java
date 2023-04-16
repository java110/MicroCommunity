package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.contract.ContractTypeSpecDto;
import com.java110.intf.store.IContractTypeSpecInnerServiceSMO;
import com.java110.po.contractTypeSpec.ContractTypeSpecPo;
import com.java110.store.dao.IContractTypeSpecServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 合同类型规格内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ContractTypeSpecInnerServiceSMOImpl extends BaseServiceSMO implements IContractTypeSpecInnerServiceSMO {

    @Autowired
    private IContractTypeSpecServiceDao contractTypeSpecServiceDaoImpl;


    @Override
    public int saveContractTypeSpec(@RequestBody ContractTypeSpecPo contractTypeSpecPo) {
        int saveFlag = 1;
        contractTypeSpecServiceDaoImpl.saveContractTypeSpecInfo(BeanConvertUtil.beanCovertMap(contractTypeSpecPo));
        return saveFlag;
    }

    @Override
    public int updateContractTypeSpec(@RequestBody ContractTypeSpecPo contractTypeSpecPo) {
        int saveFlag = 1;
        contractTypeSpecServiceDaoImpl.updateContractTypeSpecInfo(BeanConvertUtil.beanCovertMap(contractTypeSpecPo));
        return saveFlag;
    }

    @Override
    public int deleteContractTypeSpec(@RequestBody ContractTypeSpecPo contractTypeSpecPo) {
        int saveFlag = 1;
        contractTypeSpecPo.setStatusCd("1");
        contractTypeSpecServiceDaoImpl.updateContractTypeSpecInfo(BeanConvertUtil.beanCovertMap(contractTypeSpecPo));
        return saveFlag;
    }

    @Override
    public List<ContractTypeSpecDto> queryContractTypeSpecs(@RequestBody ContractTypeSpecDto contractTypeSpecDto) {

        //校验是否传了 分页信息

        int page = contractTypeSpecDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            contractTypeSpecDto.setPage((page - 1) * contractTypeSpecDto.getRow());
        }

        List<ContractTypeSpecDto> contractTypeSpecs = BeanConvertUtil.covertBeanList(contractTypeSpecServiceDaoImpl.getContractTypeSpecInfo(BeanConvertUtil.beanCovertMap(contractTypeSpecDto)), ContractTypeSpecDto.class);

        return contractTypeSpecs;
    }


    @Override
    public int queryContractTypeSpecsCount(@RequestBody ContractTypeSpecDto contractTypeSpecDto) {
        return contractTypeSpecServiceDaoImpl.queryContractTypeSpecsCount(BeanConvertUtil.beanCovertMap(contractTypeSpecDto));
    }

    public IContractTypeSpecServiceDao getContractTypeSpecServiceDaoImpl() {
        return contractTypeSpecServiceDaoImpl;
    }

    public void setContractTypeSpecServiceDaoImpl(IContractTypeSpecServiceDao contractTypeSpecServiceDaoImpl) {
        this.contractTypeSpecServiceDaoImpl = contractTypeSpecServiceDaoImpl;
    }
}
