package com.java110.api.smo.parkingArea;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 停车场管理服务接口类
 *
 * add by wuxw 2019-06-29
 */
public interface IListParkingAreasSMO {

    /**
     * 查询停车场信息
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listParkingAreas(IPageData pd) throws SMOException;
}
