package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeReceiptDetailDto;
import com.java110.fee.dao.IFeeReceiptDetailServiceDao;
import com.java110.intf.fee.IFeeReceiptDetailInnerServiceSMO;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;
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
 * @Description 收据明细内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeReceiptDetailInnerServiceSMOImpl extends BaseServiceSMO implements IFeeReceiptDetailInnerServiceSMO {

    @Autowired
    private IFeeReceiptDetailServiceDao feeReceiptDetailServiceDaoImpl;


    @Override
    public int saveFeeReceiptDetail(@RequestBody FeeReceiptDetailPo feeReceiptDetailPo) {
        int saveFlag = 1;
        feeReceiptDetailServiceDaoImpl.saveFeeReceiptDetailInfo(BeanConvertUtil.beanCovertMap(feeReceiptDetailPo));
        return saveFlag;
    }

    @Override
    public int saveFeeReceiptDetails(@RequestBody List<FeeReceiptDetailPo> feeReceiptDetailPos) {
        List<Map> fees = new ArrayList<>();
        for (FeeReceiptDetailPo feeReceiptDetailPo : feeReceiptDetailPos) {
            fees.add(BeanConvertUtil.beanCovertMap(feeReceiptDetailPo));
        }

        Map info = new HashMap();
        info.put("feeReceiptDetailPos", fees);
        feeReceiptDetailServiceDaoImpl.saveFeeReceiptDetails(info);
        return 1;
    }


    @Override
    public int updateFeeReceiptDetail(@RequestBody FeeReceiptDetailPo feeReceiptDetailPo) {
        int saveFlag = 1;
        feeReceiptDetailServiceDaoImpl.updateFeeReceiptDetailInfo(BeanConvertUtil.beanCovertMap(feeReceiptDetailPo));
        return saveFlag;
    }

    @Override
    public int deleteFeeReceiptDetail(@RequestBody FeeReceiptDetailPo feeReceiptDetailPo) {
        int saveFlag = 1;
        feeReceiptDetailPo.setStatusCd("1");
        feeReceiptDetailServiceDaoImpl.updateFeeReceiptDetailInfo(BeanConvertUtil.beanCovertMap(feeReceiptDetailPo));
        return saveFlag;
    }

    @Override
    public List<FeeReceiptDetailDto> queryFeeReceiptDetails(@RequestBody FeeReceiptDetailDto feeReceiptDetailDto) {

        //校验是否传了 分页信息

        int page = feeReceiptDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeReceiptDetailDto.setPage((page - 1) * feeReceiptDetailDto.getRow());
        }

        List<FeeReceiptDetailDto> feeReceiptDetails = BeanConvertUtil.covertBeanList(feeReceiptDetailServiceDaoImpl.getFeeReceiptDetailInfo(BeanConvertUtil.beanCovertMap(feeReceiptDetailDto)), FeeReceiptDetailDto.class);

        return feeReceiptDetails;
    }


    @Override
    public int queryFeeReceiptDetailsCount(@RequestBody FeeReceiptDetailDto feeReceiptDetailDto) {
        return feeReceiptDetailServiceDaoImpl.queryFeeReceiptDetailsCount(BeanConvertUtil.beanCovertMap(feeReceiptDetailDto));
    }

    public IFeeReceiptDetailServiceDao getFeeReceiptDetailServiceDaoImpl() {
        return feeReceiptDetailServiceDaoImpl;
    }

    public void setFeeReceiptDetailServiceDaoImpl(IFeeReceiptDetailServiceDao feeReceiptDetailServiceDaoImpl) {
        this.feeReceiptDetailServiceDaoImpl = feeReceiptDetailServiceDaoImpl;
    }
}
