package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.importFee.ImportFeeDetailDto;
import com.java110.fee.dao.IImportFeeDetailServiceDao;
import com.java110.intf.fee.IImportFeeDetailInnerServiceSMO;
import com.java110.po.importFeeDetail.ImportFeeDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用导入明细内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ImportFeeDetailInnerServiceSMOImpl extends BaseServiceSMO implements IImportFeeDetailInnerServiceSMO {

    @Autowired
    private IImportFeeDetailServiceDao importFeeDetailServiceDaoImpl;


    @Override
    public int saveImportFeeDetail(@RequestBody ImportFeeDetailPo importFeeDetailPo) {
        int saveFlag = 1;
        importFeeDetailServiceDaoImpl.saveImportFeeDetailInfo(BeanConvertUtil.beanCovertMap(importFeeDetailPo));
        return saveFlag;
    }

    @Override
    public int saveImportFeeDetails(@RequestBody List<ImportFeeDetailPo> importFeeDetailPos) {
        List<Map> fees = new ArrayList<>();
        for (ImportFeeDetailPo payFeeDetailPo : importFeeDetailPos) {
            fees.add(BeanConvertUtil.beanCovertMap(payFeeDetailPo));
        }

        Map info = new HashMap();
        info.put("importFeeDetailPos", fees);
        importFeeDetailServiceDaoImpl.saveImportFeeDetails(info);
        return 1;
    }

    @Override
    public int updateImportFeeDetail(@RequestBody ImportFeeDetailPo importFeeDetailPo) {
        int saveFlag = 1;
        importFeeDetailServiceDaoImpl.updateImportFeeDetailInfo(BeanConvertUtil.beanCovertMap(importFeeDetailPo));
        return saveFlag;
    }

    @Override
    public int deleteImportFeeDetail(@RequestBody ImportFeeDetailPo importFeeDetailPo) {
        int saveFlag = 1;
        importFeeDetailPo.setStatusCd("1");
        importFeeDetailServiceDaoImpl.updateImportFeeDetailInfo(BeanConvertUtil.beanCovertMap(importFeeDetailPo));
        return saveFlag;
    }

    @Override
    public List<ImportFeeDetailDto> queryImportFeeDetails(@RequestBody ImportFeeDetailDto importFeeDetailDto) {

        //校验是否传了 分页信息

        int page = importFeeDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            importFeeDetailDto.setPage((page - 1) * importFeeDetailDto.getRow());
        }

        List<ImportFeeDetailDto> importFeeDetails = BeanConvertUtil.covertBeanList(importFeeDetailServiceDaoImpl.getImportFeeDetailInfo(BeanConvertUtil.beanCovertMap(importFeeDetailDto)), ImportFeeDetailDto.class);

        return importFeeDetails;
    }


    @Override
    public int queryImportFeeDetailsCount(@RequestBody ImportFeeDetailDto importFeeDetailDto) {
        return importFeeDetailServiceDaoImpl.queryImportFeeDetailsCount(BeanConvertUtil.beanCovertMap(importFeeDetailDto));
    }

    public IImportFeeDetailServiceDao getImportFeeDetailServiceDaoImpl() {
        return importFeeDetailServiceDaoImpl;
    }

    public void setImportFeeDetailServiceDaoImpl(IImportFeeDetailServiceDao importFeeDetailServiceDaoImpl) {
        this.importFeeDetailServiceDaoImpl = importFeeDetailServiceDaoImpl;
    }
}
