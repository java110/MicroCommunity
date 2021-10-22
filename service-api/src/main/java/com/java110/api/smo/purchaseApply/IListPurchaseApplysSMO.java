package com.java110.api.smo.purchaseApply;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 采购申请管理服务接口类
 *
 * add by wuxw 2019-06-29
 */
public interface IListPurchaseApplysSMO {

    /**
     * 查询采购申请信息
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listPurchaseApplys(IPageData pd) throws SMOException;
}
