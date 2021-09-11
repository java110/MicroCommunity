package com.java110.api.smo.ownerRepair;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加业主报修接口
 *
 * add by wuxw 2019-06-30
 */
public interface IAddOwnerRepairSMO {

    /**
     * 添加业主报修
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> saveOwnerRepair(IPageData pd);
}
