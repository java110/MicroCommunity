package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.contract.ContractAttrDto;
import com.java110.intf.store.IContractAttrInnerServiceSMO;
import com.java110.po.contractAttr.ContractAttrPo;
import com.java110.store.dao.IContractAttrServiceDao;
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
public class ContractAttrInnerServiceSMOImpl extends BaseServiceSMO implements IContractAttrInnerServiceSMO {

    @Autowired
    private IContractAttrServiceDao contractAttrServiceDaoImpl;


    @Override
    public int saveContractAttr(@RequestBody ContractAttrPo contractAttrPo) {
        int saveFlag = 1;
        contractAttrServiceDaoImpl.saveContractAttrInfo(BeanConvertUtil.beanCovertMap(contractAttrPo));
        return saveFlag;
    }

    @Override
    public int updateContractAttr(@RequestBody ContractAttrPo contractAttrPo) {
        int saveFlag = 1;
        contractAttrServiceDaoImpl.updateContractAttrInfo(BeanConvertUtil.beanCovertMap(contractAttrPo));
        return saveFlag;
    }

    @Override
    public int deleteContractAttr(@RequestBody ContractAttrPo contractAttrPo) {
        int saveFlag = 1;
        contractAttrPo.setStatusCd("1");
        contractAttrServiceDaoImpl.updateContractAttrInfo(BeanConvertUtil.beanCovertMap(contractAttrPo));
        return saveFlag;
    }

    @Override
    public List<ContractAttrDto> queryContractAttrs(@RequestBody ContractAttrDto contractAttrDto) {

        //校验是否传了 分页信息

        int page = contractAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            contractAttrDto.setPage((page - 1) * contractAttrDto.getRow());
        }

        List<ContractAttrDto> contractAttrs = BeanConvertUtil.covertBeanList(contractAttrServiceDaoImpl.getContractAttrInfo(BeanConvertUtil.beanCovertMap(contractAttrDto)), ContractAttrDto.class);

        return contractAttrs;
    }


    @Override
    public int queryContractAttrsCount(@RequestBody ContractAttrDto contractAttrDto) {
        return contractAttrServiceDaoImpl.queryContractAttrsCount(BeanConvertUtil.beanCovertMap(contractAttrDto));
    }

    public IContractAttrServiceDao getContractAttrServiceDaoImpl() {
        return contractAttrServiceDaoImpl;
    }

    public void setContractAttrServiceDaoImpl(IContractAttrServiceDao contractAttrServiceDaoImpl) {
        this.contractAttrServiceDaoImpl = contractAttrServiceDaoImpl;
    }
}
