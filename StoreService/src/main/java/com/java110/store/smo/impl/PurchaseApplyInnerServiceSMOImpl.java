package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.purchaseApply.IPurchaseApplyInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.purchaseApply.PurchaseApplyDetailDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.user.UserDto;
import com.java110.store.dao.IPurchaseApplyServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 采购申请内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class PurchaseApplyInnerServiceSMOImpl extends BaseServiceSMO implements IPurchaseApplyInnerServiceSMO {

    @Autowired
    private IPurchaseApplyServiceDao purchaseApplyServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<PurchaseApplyDto> queryPurchaseApplys(@RequestBody  PurchaseApplyDto purchaseApplyDto) {

        //校验是否传了 分页信息

        int page = purchaseApplyDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            purchaseApplyDto.setPage((page - 1) * purchaseApplyDto.getRow());
        }

        List<PurchaseApplyDto> purchaseApplys = BeanConvertUtil.covertBeanList(purchaseApplyServiceDaoImpl.getPurchaseApplyInfo(BeanConvertUtil.beanCovertMap(purchaseApplyDto)), PurchaseApplyDto.class);

        if (purchaseApplys == null || purchaseApplys.size() == 0) {
            return purchaseApplys;
        }

        String[] userIds = getUserIds(purchaseApplys);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (PurchaseApplyDto purchaseApply : purchaseApplys) {
            refreshPurchaseApply(purchaseApply, users);
        }
        return purchaseApplys;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param purchaseApply 小区采购申请信息
     * @param users 用户列表
     */
    private void refreshPurchaseApply(PurchaseApplyDto purchaseApply, List<UserDto> users) {
        for (UserDto user : users) {
            if (purchaseApply.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, purchaseApply);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param purchaseApplys 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<PurchaseApplyDto> purchaseApplys) {
        List<String> userIds = new ArrayList<String>();
        for (PurchaseApplyDto purchaseApply : purchaseApplys) {
            userIds.add(purchaseApply.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryPurchaseApplysCount(@RequestBody PurchaseApplyDto purchaseApplyDto) {
        return purchaseApplyServiceDaoImpl.queryPurchaseApplysCount(BeanConvertUtil.beanCovertMap(purchaseApplyDto));
    }

    //查询订单明细表
    @Override
    public List<PurchaseApplyDetailDto> queryPurchaseApplyDetails(PurchaseApplyDetailDto purchaseApplyDetailDto) {
        List<PurchaseApplyDetailDto> purchaseApplyDetails = BeanConvertUtil.covertBeanList(purchaseApplyServiceDaoImpl.getPurchaseApplyDetailInfo(BeanConvertUtil.beanCovertMap(purchaseApplyDetailDto)), PurchaseApplyDetailDto.class);
        return purchaseApplyDetails;
    }

    public IPurchaseApplyServiceDao getPurchaseApplyServiceDaoImpl() {
        return purchaseApplyServiceDaoImpl;
    }

    public void setPurchaseApplyServiceDaoImpl(IPurchaseApplyServiceDao purchaseApplyServiceDaoImpl) {
        this.purchaseApplyServiceDaoImpl = purchaseApplyServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
