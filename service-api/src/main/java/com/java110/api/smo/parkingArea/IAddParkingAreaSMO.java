package com.java110.api.smo.parkingArea;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加停车场接口
 *
 * add by wuxw 2019-06-30
 */
public interface IAddParkingAreaSMO {

    /**
     * 添加停车场
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> saveParkingArea(IPageData pd);
}
