package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.feeReceipt.FeeReceiptDto;
import com.java110.fee.dao.IFeeReceiptServiceDao;
import com.java110.intf.fee.IFeeReceiptInnerServiceSMO;
import com.java110.po.feeReceipt.FeeReceiptPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 收据内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeReceiptInnerServiceSMOImpl extends BaseServiceSMO implements IFeeReceiptInnerServiceSMO {

    @Autowired
    private IFeeReceiptServiceDao feeReceiptServiceDaoImpl;


    @Override
    public int saveFeeReceipt(@RequestBody FeeReceiptPo feeReceiptPo) {
        int saveFlag = 1;
        feeReceiptServiceDaoImpl.saveFeeReceiptInfo(BeanConvertUtil.beanCovertMap(feeReceiptPo));
        return saveFlag;
    }

    @Override
    public int updateFeeReceipt(@RequestBody FeeReceiptPo feeReceiptPo) {
        int saveFlag = 1;
        feeReceiptServiceDaoImpl.updateFeeReceiptInfo(BeanConvertUtil.beanCovertMap(feeReceiptPo));
        return saveFlag;
    }

    @Override
    public int deleteFeeReceipt(@RequestBody FeeReceiptPo feeReceiptPo) {
        int saveFlag = 1;
        feeReceiptPo.setStatusCd("1");
        feeReceiptServiceDaoImpl.updateFeeReceiptInfo(BeanConvertUtil.beanCovertMap(feeReceiptPo));
        return saveFlag;
    }

    @Override
    public List<FeeReceiptDto> queryFeeReceipts(@RequestBody FeeReceiptDto feeReceiptDto) {

        //校验是否传了 分页信息

        int page = feeReceiptDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeReceiptDto.setPage((page - 1) * feeReceiptDto.getRow());
        }

        List<FeeReceiptDto> feeReceipts = BeanConvertUtil.covertBeanList(feeReceiptServiceDaoImpl.getFeeReceiptInfo(BeanConvertUtil.beanCovertMap(feeReceiptDto)), FeeReceiptDto.class);

        return feeReceipts;
    }


    @Override
    public int queryFeeReceiptsCount(@RequestBody FeeReceiptDto feeReceiptDto) {
        return feeReceiptServiceDaoImpl.queryFeeReceiptsCount(BeanConvertUtil.beanCovertMap(feeReceiptDto));
    }

    public IFeeReceiptServiceDao getFeeReceiptServiceDaoImpl() {
        return feeReceiptServiceDaoImpl;
    }

    public void setFeeReceiptServiceDaoImpl(IFeeReceiptServiceDao feeReceiptServiceDaoImpl) {
        this.feeReceiptServiceDaoImpl = feeReceiptServiceDaoImpl;
    }
}
