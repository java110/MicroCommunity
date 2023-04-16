package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.contract.ContractChangePlanDetailDto;
import com.java110.intf.store.IContractChangePlanDetailInnerServiceSMO;
import com.java110.po.contractChangePlanDetail.ContractChangePlanDetailPo;
import com.java110.store.dao.IContractChangePlanDetailServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 合同变更明细内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ContractChangePlanDetailInnerServiceSMOImpl extends BaseServiceSMO implements IContractChangePlanDetailInnerServiceSMO {

    @Autowired
    private IContractChangePlanDetailServiceDao contractChangePlanDetailServiceDaoImpl;


    @Override
    public int saveContractChangePlanDetail(@RequestBody ContractChangePlanDetailPo contractChangePlanDetailPo) {
        int saveFlag = 1;
        contractChangePlanDetailServiceDaoImpl.saveContractChangePlanDetailInfo(BeanConvertUtil.beanCovertMap(contractChangePlanDetailPo));
        return saveFlag;
    }

    @Override
    public int updateContractChangePlanDetail(@RequestBody ContractChangePlanDetailPo contractChangePlanDetailPo) {
        int saveFlag = 1;
        contractChangePlanDetailServiceDaoImpl.updateContractChangePlanDetailInfo(BeanConvertUtil.beanCovertMap(contractChangePlanDetailPo));
        return saveFlag;
    }

    @Override
    public int deleteContractChangePlanDetail(@RequestBody ContractChangePlanDetailPo contractChangePlanDetailPo) {
        int saveFlag = 1;
        contractChangePlanDetailPo.setState("DEL");
        contractChangePlanDetailServiceDaoImpl.updateContractChangePlanDetailInfo(BeanConvertUtil.beanCovertMap(contractChangePlanDetailPo));
        return saveFlag;
    }

    @Override
    public List<ContractChangePlanDetailDto> queryContractChangePlanDetails(@RequestBody ContractChangePlanDetailDto contractChangePlanDetailDto) {

        //校验是否传了 分页信息

        int page = contractChangePlanDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            contractChangePlanDetailDto.setPage((page - 1) * contractChangePlanDetailDto.getRow());
        }

        List<ContractChangePlanDetailDto> contractChangePlanDetails = BeanConvertUtil.covertBeanList(contractChangePlanDetailServiceDaoImpl.getContractChangePlanDetailInfo(BeanConvertUtil.beanCovertMap(contractChangePlanDetailDto)), ContractChangePlanDetailDto.class);

        return contractChangePlanDetails;
    }


    @Override
    public int queryContractChangePlanDetailsCount(@RequestBody ContractChangePlanDetailDto contractChangePlanDetailDto) {
        return contractChangePlanDetailServiceDaoImpl.queryContractChangePlanDetailsCount(BeanConvertUtil.beanCovertMap(contractChangePlanDetailDto));
    }

    public IContractChangePlanDetailServiceDao getContractChangePlanDetailServiceDaoImpl() {
        return contractChangePlanDetailServiceDaoImpl;
    }

    public void setContractChangePlanDetailServiceDaoImpl(IContractChangePlanDetailServiceDao contractChangePlanDetailServiceDaoImpl) {
        this.contractChangePlanDetailServiceDaoImpl = contractChangePlanDetailServiceDaoImpl;
    }
}
