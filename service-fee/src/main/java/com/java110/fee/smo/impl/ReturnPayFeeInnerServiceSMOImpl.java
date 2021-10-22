package com.java110.fee.smo.impl;


import com.java110.dto.returnPayFee.ReturnPayFeeDto;
import com.java110.fee.dao.IReturnPayFeeServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.fee.IReturnPayFeeInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 退费表内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReturnPayFeeInnerServiceSMOImpl extends BaseServiceSMO implements IReturnPayFeeInnerServiceSMO {

    @Autowired
    private IReturnPayFeeServiceDao returnPayFeeServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<ReturnPayFeeDto> queryReturnPayFees(@RequestBody ReturnPayFeeDto returnPayFeeDto) {

        //校验是否传了 分页信息

        int page = returnPayFeeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            returnPayFeeDto.setPage((page - 1) * returnPayFeeDto.getRow());
        }

        List<ReturnPayFeeDto> returnPayFees = BeanConvertUtil.covertBeanList(returnPayFeeServiceDaoImpl.getReturnPayFeeInfo(BeanConvertUtil.beanCovertMap(returnPayFeeDto)), ReturnPayFeeDto.class);

        if (returnPayFees == null || returnPayFees.size() == 0) {
            return returnPayFees;
        }

//        String[] userIds = getUserIds(returnPayFees);
//        //根据 userId 查询用户信息
//        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);
//
//        for (ReturnPayFeeDto returnPayFee : returnPayFees) {
//            refreshReturnPayFee(returnPayFee, users);
//        }
        return returnPayFees;
    }

    @Override
    public List<ReturnPayFeeDto> queryRoomReturnPayFees(@RequestBody ReturnPayFeeDto returnPayFeeDto) {

        //校验是否传了 分页信息

        int page = returnPayFeeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            returnPayFeeDto.setPage((page - 1) * returnPayFeeDto.getRow());
        }

        List<ReturnPayFeeDto> returnPayFees = BeanConvertUtil.covertBeanList(returnPayFeeServiceDaoImpl.getRoomReturnPayFeeInfo(BeanConvertUtil.beanCovertMap(returnPayFeeDto)), ReturnPayFeeDto.class);

        if (returnPayFees == null || returnPayFees.size() == 0) {
            return returnPayFees;
        }
        return returnPayFees;
    }

    @Override
    public List<ReturnPayFeeDto> queryCarReturnPayFees(@RequestBody ReturnPayFeeDto returnPayFeeDto) {

        //校验是否传了 分页信息

        int page = returnPayFeeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            returnPayFeeDto.setPage((page - 1) * returnPayFeeDto.getRow());
        }

        List<ReturnPayFeeDto> returnPayFees = BeanConvertUtil.covertBeanList(returnPayFeeServiceDaoImpl.getCarReturnPayFeeInfo(BeanConvertUtil.beanCovertMap(returnPayFeeDto)), ReturnPayFeeDto.class);

        if (returnPayFees == null || returnPayFees.size() == 0) {
            return returnPayFees;
        }
        return returnPayFees;
    }

//    /**
//     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
//     *
//     * @param returnPayFee 小区退费表信息
//     * @param users        用户列表
//     */
//    private void refreshReturnPayFee(ReturnPayFeeDto returnPayFee, List<UserDto> users) {
//        for (UserDto user : users) {
//            if (returnPayFee.getUserId().equals(user.getUserId())) {
//                BeanConvertUtil.covertBean(user, returnPayFee);
//            }
//        }
//    }
//
//    /**
//     * 获取批量userId
//     *
//     * @param returnPayFees 小区楼信息
//     * @return 批量userIds 信息
//     */
//    private String[] getUserIds(List<ReturnPayFeeDto> returnPayFees) {
//        List<String> userIds = new ArrayList<String>();
//        for (ReturnPayFeeDto returnPayFee : returnPayFees) {
//            userIds.add(returnPayFee.getUserId());
//        }
//
//        return userIds.toArray(new String[userIds.size()]);
//    }

    @Override
    public int queryReturnPayFeesCount(@RequestBody ReturnPayFeeDto returnPayFeeDto) {
        return returnPayFeeServiceDaoImpl.queryReturnPayFeesCount(BeanConvertUtil.beanCovertMap(returnPayFeeDto));
    }

    public IReturnPayFeeServiceDao getReturnPayFeeServiceDaoImpl() {
        return returnPayFeeServiceDaoImpl;
    }

    public void setReturnPayFeeServiceDaoImpl(IReturnPayFeeServiceDao returnPayFeeServiceDaoImpl) {
        this.returnPayFeeServiceDaoImpl = returnPayFeeServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
