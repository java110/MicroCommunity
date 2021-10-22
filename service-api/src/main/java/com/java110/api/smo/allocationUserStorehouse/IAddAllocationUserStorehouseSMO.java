package com.java110.api.smo.allocationUserStorehouse;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加转赠记录接口
 *
 * add by fqz 2021-04-24
 */
public interface IAddAllocationUserStorehouseSMO {

    /**
     * 添加采购申请
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> saveAllocationUserStorehouse(IPageData pd);
}
