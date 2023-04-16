package com.java110.store.smo.impl;


import com.java110.intf.store.IContractFileInnerServiceSMO;
import com.java110.po.contractFile.ContractFilePo;
import com.java110.store.dao.IContractFileServiceDao;
import com.java110.dto.contract.ContractFileDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 合同附件内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ContractFileInnerServiceSMOImpl extends BaseServiceSMO implements IContractFileInnerServiceSMO {

    @Autowired
    private IContractFileServiceDao contractFileServiceDaoImpl;


    @Override
    public int saveContractFile(@RequestBody ContractFilePo contractFilePo) {
        int saveFlag = 1;
        contractFileServiceDaoImpl.saveContractFileInfo(BeanConvertUtil.beanCovertMap(contractFilePo));
        return saveFlag;
    }

     @Override
    public int updateContractFile(@RequestBody  ContractFilePo contractFilePo) {
        int saveFlag = 1;
         contractFileServiceDaoImpl.updateContractFileInfo(BeanConvertUtil.beanCovertMap(contractFilePo));
        return saveFlag;
    }

     @Override
    public int deleteContractFile(@RequestBody  ContractFilePo contractFilePo) {
        int saveFlag = 1;
        contractFilePo.setStatusCd("1");
        contractFileServiceDaoImpl.updateContractFileInfo(BeanConvertUtil.beanCovertMap(contractFilePo));
        return saveFlag;
    }

    @Override
    public List<ContractFileDto> queryContractFiles(@RequestBody  ContractFileDto contractFileDto) {

        //校验是否传了 分页信息

        int page = contractFileDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            contractFileDto.setPage((page - 1) * contractFileDto.getRow());
        }

        List<ContractFileDto> contractFiles = BeanConvertUtil.covertBeanList(contractFileServiceDaoImpl.getContractFileInfo(BeanConvertUtil.beanCovertMap(contractFileDto)), ContractFileDto.class);

        return contractFiles;
    }


    @Override
    public int queryContractFilesCount(@RequestBody ContractFileDto contractFileDto) {
        return contractFileServiceDaoImpl.queryContractFilesCount(BeanConvertUtil.beanCovertMap(contractFileDto));    }

    public IContractFileServiceDao getContractFileServiceDaoImpl() {
        return contractFileServiceDaoImpl;
    }

    public void setContractFileServiceDaoImpl(IContractFileServiceDao contractFileServiceDaoImpl) {
        this.contractFileServiceDaoImpl = contractFileServiceDaoImpl;
    }
}
