package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.fee.BillDto;
import com.java110.dto.fee.BillOweFeeDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.dao.IFeeServiceDao;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeInnerServiceSMOImpl extends BaseServiceSMO implements IFeeInnerServiceSMO {

    @Autowired
    private IFeeServiceDao feeServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<FeeDto> queryFees(@RequestBody FeeDto feeDto) {

        //校验是否传了 分页信息

        int page = feeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeDto.setPage((page - 1) * feeDto.getRow());
        }

        List<FeeDto> fees = BeanConvertUtil.covertBeanList(feeServiceDaoImpl.getFeeInfo(BeanConvertUtil.beanCovertMap(feeDto)), FeeDto.class);

        if (fees == null || fees.size() == 0) {
            return fees;
        }

        String[] userIds = getUserIds(fees);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (FeeDto fee : fees) {
            refreshFee(fee, users);
        }
        return fees;
    }

    @Override
    public List<FeeDto> queryBusinessFees(@RequestBody FeeDto feeDto) {

        List<Map> fees = feeServiceDaoImpl.getBusinessFeeInfo(BeanConvertUtil.beanCovertMap(feeDto));

        return BeanConvertUtil.covertBeanList(fees, FeeDto.class);

    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param fee   小区费用信息
     * @param users 用户列表
     */
    private void refreshFee(FeeDto fee, List<UserDto> users) {
        for (UserDto user : users) {
            if (fee.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, fee);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param fees 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<FeeDto> fees) {
        List<String> userIds = new ArrayList<String>();
        for (FeeDto fee : fees) {
            userIds.add(fee.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryFeesCount(@RequestBody FeeDto feeDto) {
        return feeServiceDaoImpl.queryFeesCount(BeanConvertUtil.beanCovertMap(feeDto));
    }

    @Override
    public List<FeeDto> queryFeeByAttr(FeeAttrDto feeAttrDto) {
        //校验是否传了 分页信息

        int page = feeAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeAttrDto.setPage((page - 1) * feeAttrDto.getRow());
        }

        List<FeeDto> fees = BeanConvertUtil.covertBeanList(feeServiceDaoImpl.queryFeeByAttr(BeanConvertUtil.beanCovertMap(feeAttrDto)), FeeDto.class);

        return fees;
    }

    @Override
    public int queryFeeByAttrCount(FeeAttrDto feeAttrDto) {
        return feeServiceDaoImpl.queryFeeByAttrCount(BeanConvertUtil.beanCovertMap(feeAttrDto));
    }


    @Override
    public int queryBillCount(@RequestBody BillDto billDto) {
        return feeServiceDaoImpl.queryBillCount(BeanConvertUtil.beanCovertMap(billDto));
    }

    /**
     * 查询账期
     *
     * @param billDto
     * @return
     */
    @Override
    public List<BillDto> queryBills(@RequestBody BillDto billDto) {

        //校验是否传了 分页信息

        int page = billDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            billDto.setPage((page - 1) * billDto.getRow());
        }

        List<BillDto> billDtos = BeanConvertUtil.covertBeanList(feeServiceDaoImpl.queryBills(BeanConvertUtil.beanCovertMap(billDto)), BillDto.class);

        return billDtos;

    }


    @Override
    public int queryBillOweFeeCount(@RequestBody BillOweFeeDto billDto) {
        return feeServiceDaoImpl.queryBillOweFeeCount(BeanConvertUtil.beanCovertMap(billDto));
    }

    /**
     * 查询账期
     *
     * @param billDto
     * @return
     */
    @Override
    public List<BillOweFeeDto> queryBillOweFees(@RequestBody BillOweFeeDto billDto) {

        //校验是否传了 分页信息

        int page = billDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            billDto.setPage((page - 1) * billDto.getRow());
        }

        List<BillOweFeeDto> billOweFeeDtos = BeanConvertUtil.covertBeanList(feeServiceDaoImpl.queryBillOweFees(BeanConvertUtil.beanCovertMap(billDto)), BillOweFeeDto.class);

        return billOweFeeDtos;

    }

    /**
     * 保存欠费
     *
     * @param billDto
     * @return
     */
    @Override
    public int insertBillOweFees(@RequestBody BillOweFeeDto billDto) {
        return feeServiceDaoImpl.insertBillOweFees(BeanConvertUtil.beanCovertMap(billDto));
    }

    /**
     * 保存账单
     *
     * @param billDto
     * @return
     */
    @Override
    public int insertBill(@RequestBody BillDto billDto) {
        return feeServiceDaoImpl.insertBill(BeanConvertUtil.beanCovertMap(billDto));
    }

    @Override
    public int updateFee(@RequestBody PayFeePo payFeePo) {
        feeServiceDaoImpl.updateFeeInfoInstance(BeanConvertUtil.beanCovertMap(payFeePo));
        return 1;
    }


    public IFeeServiceDao getFeeServiceDaoImpl() {
        return feeServiceDaoImpl;
    }

    public void setFeeServiceDaoImpl(IFeeServiceDao feeServiceDaoImpl) {
        this.feeServiceDaoImpl = feeServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
