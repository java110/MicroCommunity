package com.java110.api.smo.visit;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 修改访客登记接口
 * <p>
 * add by wuxw 2019-06-30
 */
public interface IEditVisitSMO {

    /**
     * 修改小区
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> updateVisit(IPageData pd);

}
