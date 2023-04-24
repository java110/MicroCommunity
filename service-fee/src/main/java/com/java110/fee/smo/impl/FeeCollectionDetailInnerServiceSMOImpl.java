package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeCollectionDetailDto;
import com.java110.fee.dao.IFeeCollectionDetailServiceDao;
import com.java110.intf.fee.IFeeCollectionDetailInnerServiceSMO;
import com.java110.po.feeCollectionDetail.FeeCollectionDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 催缴单内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeCollectionDetailInnerServiceSMOImpl extends BaseServiceSMO implements IFeeCollectionDetailInnerServiceSMO {

    @Autowired
    private IFeeCollectionDetailServiceDao feeCollectionDetailServiceDaoImpl;


    @Override
    public int saveFeeCollectionDetail(@RequestBody FeeCollectionDetailPo feeCollectionDetailPo) {
        int saveFlag = 1;
        feeCollectionDetailServiceDaoImpl.saveFeeCollectionDetailInfo(BeanConvertUtil.beanCovertMap(feeCollectionDetailPo));
        return saveFlag;
    }

    @Override
    public int updateFeeCollectionDetail(@RequestBody FeeCollectionDetailPo feeCollectionDetailPo) {
        int saveFlag = 1;
        feeCollectionDetailServiceDaoImpl.updateFeeCollectionDetailInfo(BeanConvertUtil.beanCovertMap(feeCollectionDetailPo));
        return saveFlag;
    }

    @Override
    public int deleteFeeCollectionDetail(@RequestBody FeeCollectionDetailPo feeCollectionDetailPo) {
        int saveFlag = 1;
        feeCollectionDetailPo.setStatusCd("1");
        feeCollectionDetailServiceDaoImpl.updateFeeCollectionDetailInfo(BeanConvertUtil.beanCovertMap(feeCollectionDetailPo));
        return saveFlag;
    }

    @Override
    public List<FeeCollectionDetailDto> queryFeeCollectionDetails(@RequestBody FeeCollectionDetailDto feeCollectionDetailDto) {

        //校验是否传了 分页信息

        int page = feeCollectionDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeCollectionDetailDto.setPage((page - 1) * feeCollectionDetailDto.getRow());
        }

        List<FeeCollectionDetailDto> feeCollectionDetails = BeanConvertUtil.covertBeanList(feeCollectionDetailServiceDaoImpl.getFeeCollectionDetailInfo(BeanConvertUtil.beanCovertMap(feeCollectionDetailDto)), FeeCollectionDetailDto.class);

        return feeCollectionDetails;
    }


    @Override
    public int queryFeeCollectionDetailsCount(@RequestBody FeeCollectionDetailDto feeCollectionDetailDto) {
        return feeCollectionDetailServiceDaoImpl.queryFeeCollectionDetailsCount(BeanConvertUtil.beanCovertMap(feeCollectionDetailDto));
    }

    public IFeeCollectionDetailServiceDao getFeeCollectionDetailServiceDaoImpl() {
        return feeCollectionDetailServiceDaoImpl;
    }

    public void setFeeCollectionDetailServiceDaoImpl(IFeeCollectionDetailServiceDao feeCollectionDetailServiceDaoImpl) {
        this.feeCollectionDetailServiceDaoImpl = feeCollectionDetailServiceDaoImpl;
    }
}
