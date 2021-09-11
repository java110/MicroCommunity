package com.java110.api.smo.purchaseApplyDetail;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 出入库明细管理服务接口类
 * <p>
 * add by fqz 2021-04-22
 */
public interface IListPurchaseApplyDetailsSMO {

    /**
     * 查询采购申请信息
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listPurchaseApplyDetails(IPageData pd) throws SMOException;
}
