package com.java110.fee.smo.impl;


import com.java110.core.annotation.Java110Transactional;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.fee.dao.IFeeDetailServiceDao;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用明细内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeDetailInnerServiceSMOImpl extends BaseServiceSMO implements IFeeDetailInnerServiceSMO {

    @Autowired
    private IFeeDetailServiceDao feeDetailServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<FeeDetailDto> queryFeeDetails(@RequestBody FeeDetailDto feeDetailDto) {

        //校验是否传了 分页信息

        int page = feeDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeDetailDto.setPage((page - 1) * feeDetailDto.getRow());
        }

        List<FeeDetailDto> feeDetails = BeanConvertUtil.covertBeanList(feeDetailServiceDaoImpl.getFeeDetailInfo(BeanConvertUtil.beanCovertMap(feeDetailDto)), FeeDetailDto.class);

        refreshFeeDetail(feeDetails);
        return feeDetails;
    }

    @Override
    public List<FeeDetailDto> queryBusinessFeeDetails(@RequestBody FeeDetailDto feeDetailDto) {

        //校验是否传了 分页信息

        int page = feeDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeDetailDto.setPage((page - 1) * feeDetailDto.getRow());
        }

        List<FeeDetailDto> feeDetails = BeanConvertUtil.covertBeanList(feeDetailServiceDaoImpl.getBusinessFeeDetailInfo(BeanConvertUtil.beanCovertMap(feeDetailDto)), FeeDetailDto.class);

        refreshFeeDetail(feeDetails);
        return feeDetails;
    }

    private void refreshFeeDetail(List<FeeDetailDto> feeDetails) {
        if(feeDetails == null || feeDetails.size() < 1){
            return ;
        }

        for(FeeDetailDto feeDetailDto : feeDetails){
            if(!StringUtil.isEmpty(feeDetailDto.getImportFeeName())){
                feeDetailDto.setFeeName(feeDetailDto.getImportFeeName());
            }
        }
    }


    @Override
    public int queryFeeDetailsCount(@RequestBody FeeDetailDto feeDetailDto) {
        return feeDetailServiceDaoImpl.queryFeeDetailsCount(BeanConvertUtil.beanCovertMap(feeDetailDto));
    }

    @Override
    @Java110Transactional
    public int saveFeeDetail(@RequestBody PayFeeDetailPo payFeeDetailPo) {
        feeDetailServiceDaoImpl.saveFeeDetail(BeanConvertUtil.beanCovertMap(payFeeDetailPo));
        return 1;
    }

    public IFeeDetailServiceDao getFeeDetailServiceDaoImpl() {
        return feeDetailServiceDaoImpl;
    }

    public void setFeeDetailServiceDaoImpl(IFeeDetailServiceDao feeDetailServiceDaoImpl) {
        this.feeDetailServiceDaoImpl = feeDetailServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
