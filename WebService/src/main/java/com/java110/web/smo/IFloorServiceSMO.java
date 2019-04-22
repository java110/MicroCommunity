package com.java110.web.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 小区楼接口类
 */
public interface IFloorServiceSMO {

    /**
     * 查询小区楼信息
     * @param pd 页面数据封装对象
     * @return
     */
    ResponseEntity<String> listFloor(IPageData pd);
}
