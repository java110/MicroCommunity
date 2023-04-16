package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.contract.ContractTypeTemplateDto;
import com.java110.intf.store.IContractTypeTemplateInnerServiceSMO;
import com.java110.po.contractTypeTemplate.ContractTypeTemplatePo;
import com.java110.store.dao.IContractTypeTemplateServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 合同属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ContractTypeTemplateInnerServiceSMOImpl extends BaseServiceSMO implements IContractTypeTemplateInnerServiceSMO {

    @Autowired
    private IContractTypeTemplateServiceDao contractTypeTemplateServiceDaoImpl;


    @Override
    public int saveContractTypeTemplate(@RequestBody ContractTypeTemplatePo contractTypeTemplatePo) {
        int saveFlag = 1;
        contractTypeTemplateServiceDaoImpl.saveContractTypeTemplateInfo(BeanConvertUtil.beanCovertMap(contractTypeTemplatePo));
        return saveFlag;
    }

    @Override
    public int updateContractTypeTemplate(@RequestBody ContractTypeTemplatePo contractTypeTemplatePo) {
        int saveFlag = 1;
        contractTypeTemplateServiceDaoImpl.updateContractTypeTemplateInfo(BeanConvertUtil.beanCovertMap(contractTypeTemplatePo));
        return saveFlag;
    }

    @Override
    public int deleteContractTypeTemplate(@RequestBody ContractTypeTemplatePo contractTypeTemplatePo) {
        int saveFlag = 1;
        contractTypeTemplatePo.setStatusCd("1");
        contractTypeTemplateServiceDaoImpl.updateContractTypeTemplateInfo(BeanConvertUtil.beanCovertMap(contractTypeTemplatePo));
        return saveFlag;
    }

    @Override
    public List<ContractTypeTemplateDto> queryContractTypeTemplates(@RequestBody ContractTypeTemplateDto contractTypeTemplateDto) {

        //校验是否传了 分页信息

        int page = contractTypeTemplateDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            contractTypeTemplateDto.setPage((page - 1) * contractTypeTemplateDto.getRow());
        }

        List<ContractTypeTemplateDto> contractTypeTemplates = BeanConvertUtil.covertBeanList(contractTypeTemplateServiceDaoImpl.getContractTypeTemplateInfo(BeanConvertUtil.beanCovertMap(contractTypeTemplateDto)), ContractTypeTemplateDto.class);

        return contractTypeTemplates;
    }


    @Override
    public int queryContractTypeTemplatesCount(@RequestBody ContractTypeTemplateDto contractTypeTemplateDto) {
        return contractTypeTemplateServiceDaoImpl.queryContractTypeTemplatesCount(BeanConvertUtil.beanCovertMap(contractTypeTemplateDto));
    }

    public IContractTypeTemplateServiceDao getContractTypeTemplateServiceDaoImpl() {
        return contractTypeTemplateServiceDaoImpl;
    }

    public void setContractTypeTemplateServiceDaoImpl(IContractTypeTemplateServiceDao contractTypeTemplateServiceDaoImpl) {
        this.contractTypeTemplateServiceDaoImpl = contractTypeTemplateServiceDaoImpl;
    }
}
