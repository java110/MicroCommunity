package com.java110.store.smo.impl;


import com.java110.dto.purchaseApplyDetail.PurchaseApplyDetailDto;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.store.dao.IPurchaseApplyDetailServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.store.IPurchaseApplyDetailInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 订单明细内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class PurchaseApplyDetailInnerServiceSMOImpl extends BaseServiceSMO implements IPurchaseApplyDetailInnerServiceSMO {

    @Autowired
    private IPurchaseApplyDetailServiceDao purchaseApplyDetailServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<PurchaseApplyDetailDto> queryPurchaseApplyDetails(@RequestBody  PurchaseApplyDetailDto purchaseApplyDetailDto) {

        //校验是否传了 分页信息

        int page = purchaseApplyDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            purchaseApplyDetailDto.setPage((page - 1) * purchaseApplyDetailDto.getRow());
        }

        List<PurchaseApplyDetailDto> purchaseApplyDetails = BeanConvertUtil.covertBeanList(purchaseApplyDetailServiceDaoImpl.getPurchaseApplyDetailInfo(BeanConvertUtil.beanCovertMap(purchaseApplyDetailDto)), PurchaseApplyDetailDto.class);

        if (purchaseApplyDetails == null || purchaseApplyDetails.size() == 0) {
            return purchaseApplyDetails;
        }

//        //String[] userIds = getUserIds(purchaseApplyDetails);
//        //根据 userId 查询用户信息
//        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

//        for (PurchaseApplyDetailDto purchaseApplyDetail : purchaseApplyDetails) {
//            refreshPurchaseApplyDetail(purchaseApplyDetail, users);
//        }
        return purchaseApplyDetails;
    }

//    /**
//     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
//     *
//     * @param purchaseApplyDetail 小区订单明细信息
//     * @param users 用户列表
//     */
//    private void refreshPurchaseApplyDetail(PurchaseApplyDetailDto purchaseApplyDetail, List<UserDto> users) {
//        for (UserDto user : users) {
//            if (purchaseApplyDetail.getUserId().equals(user.getUserId())) {
//                BeanConvertUtil.covertBean(user, purchaseApplyDetail);
//            }
//        }
//    }

//    /**
//     * 获取批量userId
//     *
//     * @param purchaseApplyDetails 小区楼信息
//     * @return 批量userIds 信息
//     */
//    private String[] getUserIds(List<PurchaseApplyDetailDto> purchaseApplyDetails) {
//        List<String> userIds = new ArrayList<String>();
//        for (PurchaseApplyDetailDto purchaseApplyDetail : purchaseApplyDetails) {
//            userIds.add(purchaseApplyDetail.getUserId());
//        }
//
//        return userIds.toArray(new String[userIds.size()]);
//    }

    @Override
    public int queryPurchaseApplyDetailsCount(@RequestBody PurchaseApplyDetailDto purchaseApplyDetailDto) {
        return purchaseApplyDetailServiceDaoImpl.queryPurchaseApplyDetailsCount(BeanConvertUtil.beanCovertMap(purchaseApplyDetailDto));    }

    @Override
    public int updatePurchaseApplyDetail(@RequestBody PurchaseApplyDetailPo purchaseApplyDetailPo) {
        return purchaseApplyDetailServiceDaoImpl.updatePurchaseApplyDetailInfoInstance(BeanConvertUtil.beanCovertMap(purchaseApplyDetailPo));
    }

    public IPurchaseApplyDetailServiceDao getPurchaseApplyDetailServiceDaoImpl() {
        return purchaseApplyDetailServiceDaoImpl;
    }

    public void setPurchaseApplyDetailServiceDaoImpl(IPurchaseApplyDetailServiceDao purchaseApplyDetailServiceDaoImpl) {
        this.purchaseApplyDetailServiceDaoImpl = purchaseApplyDetailServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
