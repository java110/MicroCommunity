package com.java110.api.smo.machine;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 修改设备同步接口
 *
 * add by wuxw 2019-06-30
 */
public interface IEditMachineTranslateSMO {

    /**
     * 修改小区
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> updateMachineTranslate(IPageData pd);
}
