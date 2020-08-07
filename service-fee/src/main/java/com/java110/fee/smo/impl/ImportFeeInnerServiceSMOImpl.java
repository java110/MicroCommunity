package com.java110.fee.smo.impl;


import com.java110.fee.dao.IImportFeeServiceDao;
import com.java110.dto.importFee.ImportFeeDto;
import com.java110.intf.fee.IImportFeeInnerServiceSMO;
import com.java110.po.importFee.ImportFeePo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用导入内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ImportFeeInnerServiceSMOImpl extends BaseServiceSMO implements IImportFeeInnerServiceSMO {

    @Autowired
    private IImportFeeServiceDao importFeeServiceDaoImpl;


    @Override
    public int saveImportFee(@RequestBody ImportFeePo importFeePo) {
        int saveFlag = 1;
        importFeeServiceDaoImpl.saveImportFeeInfo(BeanConvertUtil.beanCovertMap(importFeePo));
        return saveFlag;
    }

     @Override
    public int updateImportFee(@RequestBody  ImportFeePo importFeePo) {
        int saveFlag = 1;
         importFeeServiceDaoImpl.updateImportFeeInfo(BeanConvertUtil.beanCovertMap(importFeePo));
        return saveFlag;
    }

     @Override
    public int deleteImportFee(@RequestBody  ImportFeePo importFeePo) {
        int saveFlag = 1;
        importFeePo.setStatusCd("1");
        importFeeServiceDaoImpl.updateImportFeeInfo(BeanConvertUtil.beanCovertMap(importFeePo));
        return saveFlag;
    }

    @Override
    public List<ImportFeeDto> queryImportFees(@RequestBody  ImportFeeDto importFeeDto) {

        //校验是否传了 分页信息

        int page = importFeeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            importFeeDto.setPage((page - 1) * importFeeDto.getRow());
        }

        List<ImportFeeDto> importFees = BeanConvertUtil.covertBeanList(importFeeServiceDaoImpl.getImportFeeInfo(BeanConvertUtil.beanCovertMap(importFeeDto)), ImportFeeDto.class);

        return importFees;
    }


    @Override
    public int queryImportFeesCount(@RequestBody ImportFeeDto importFeeDto) {
        return importFeeServiceDaoImpl.queryImportFeesCount(BeanConvertUtil.beanCovertMap(importFeeDto));    }

    public IImportFeeServiceDao getImportFeeServiceDaoImpl() {
        return importFeeServiceDaoImpl;
    }

    public void setImportFeeServiceDaoImpl(IImportFeeServiceDao importFeeServiceDaoImpl) {
        this.importFeeServiceDaoImpl = importFeeServiceDaoImpl;
    }
}
