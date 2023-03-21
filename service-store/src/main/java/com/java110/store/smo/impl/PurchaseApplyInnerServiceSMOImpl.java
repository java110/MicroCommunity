package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.purchaseApply.PurchaseApplyDetailDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.store.dao.IPurchaseApplyServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.purchaseApply.PurchaseApplyDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public int savePurchaseApply(@RequestBody PurchaseApplyPo purchaseApplyPo) {

        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = purchaseApplyPo.getPurchaseApplyDetailPos();

        for (PurchaseApplyDetailPo purchaseApplyDetailPo : purchaseApplyDetailPos) {
            purchaseApplyDetailPo.setApplyOrderId(purchaseApplyPo.getApplyOrderId());
        }

        int saveFlag = purchaseApplyServiceDaoImpl.savePurchaseApply(BeanConvertUtil.beanCovertMap(purchaseApplyPo));

        if (saveFlag < 1) {
            return saveFlag;
        }

        //保存订单明细
        saveFlag = purchaseApplyServiceDaoImpl.savePurchaseApplyDetailInfo(purchaseApplyDetailPos);
        return saveFlag;
    }

    @Override
    public List<PurchaseApplyDto> queryPurchaseApplys(@RequestBody PurchaseApplyDto purchaseApplyDto) {

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

    @Override
    public List<PurchaseApplyDto> queryPurchaseApplyAndDetails(@RequestBody PurchaseApplyDto purchaseApplyDto) {

        //校验是否传了 分页信息

        int page = purchaseApplyDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            purchaseApplyDto.setPage((page - 1) * purchaseApplyDto.getRow());
        }

        List<PurchaseApplyDto> purchaseApplys = BeanConvertUtil.covertBeanList(
                purchaseApplyServiceDaoImpl.getPurchaseApplyInfo(BeanConvertUtil.beanCovertMap(purchaseApplyDto)), PurchaseApplyDto.class);

        if (purchaseApplys == null || purchaseApplys.size() == 0) {
            return purchaseApplys;
        }
        //刷入采购详情
        freshPurchaseApplyDetail(purchaseApplys);

        String[] userIds = getUserIds(purchaseApplys);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (PurchaseApplyDto purchaseApply : purchaseApplys) {
            refreshPurchaseApply(purchaseApply, users);
        }
        return purchaseApplys;
    }

    private void freshPurchaseApplyDetail(List<PurchaseApplyDto> purchaseApplys) {

        List<String> applyOrderIds = new ArrayList<String>();
        for (PurchaseApplyDto purchaseApplyDto : purchaseApplys) {
            applyOrderIds.add(purchaseApplyDto.getApplyOrderId());
        }

        if (applyOrderIds.size() < 1) {
            return;
        }

        String[] tmpApplyOrderIds = applyOrderIds.toArray(new String[applyOrderIds.size()]);

        Map info = new HashMap<>();
        info.put("applyOrderIds", tmpApplyOrderIds);
        List<Map> details = purchaseApplyServiceDaoImpl.getPurchaseApplyDetailInfo(info);

        List<PurchaseApplyDetailDto> purchaseApplyDetailDtos = BeanConvertUtil.covertBeanList(details, PurchaseApplyDetailDto.class);

        List<PurchaseApplyDetailDto> purchaseApplyDetailDtoList = null;
        for (PurchaseApplyDto purchaseApplyDto : purchaseApplys) {
            purchaseApplyDetailDtoList = new ArrayList<>();
            for (PurchaseApplyDetailDto purchaseApplyDetailDto : purchaseApplyDetailDtos) {
                if (purchaseApplyDto.getApplyOrderId().equals(purchaseApplyDetailDto.getApplyOrderId())) {
                    purchaseApplyDetailDtoList.add(purchaseApplyDetailDto);
                }
            }
            purchaseApplyDto.setPurchaseApplyDetailVo(BeanConvertUtil.covertBeanList(purchaseApplyDetailDtoList, PurchaseApplyDetailVo.class));
        }

    }


    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param purchaseApply 小区采购申请信息
     * @param users         用户列表
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

    @Override
    public void updatePurchaseApply(@RequestBody PurchaseApplyPo purchaseApplyPo) {
        purchaseApplyServiceDaoImpl.updatePurchaseApplyInfoInstance(BeanConvertUtil.beanCovertMap(purchaseApplyPo));
    }

    /**
     * 获取下级处理人id
     *
     * @param purchaseApplyDto
     * @return
     */
    @Override
    public List<PurchaseApplyDto> getActRuTaskUserId(@RequestBody PurchaseApplyDto purchaseApplyDto) {
        List<PurchaseApplyDto> purchaseApplys = BeanConvertUtil.covertBeanList(purchaseApplyServiceDaoImpl.getActRuTaskUserId(BeanConvertUtil.beanCovertMap(purchaseApplyDto)), PurchaseApplyDto.class);
        return purchaseApplys;
    }

    /**
     * 获取流程任务id
     *
     * @param purchaseApplyDto
     * @return
     */
    @Override
    public List<PurchaseApplyDto> getActRuTaskId(@RequestBody PurchaseApplyDto purchaseApplyDto) {
        List<PurchaseApplyDto> purchaseApplys = BeanConvertUtil.covertBeanList(purchaseApplyServiceDaoImpl.getActRuTaskId(BeanConvertUtil.beanCovertMap(purchaseApplyDto)), PurchaseApplyDto.class);
        return purchaseApplys;
    }

    @Override
    public void updateActRuTaskById(@RequestBody PurchaseApplyDto purchaseApplyDto) {
        purchaseApplyServiceDaoImpl.updateActRuTaskById(BeanConvertUtil.beanCovertMap(purchaseApplyDto));
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
