package com.java110.api.smo.inspection;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 巡检路线管理服务接口类
 *
 * add by wuxw 2019-06-29
 */
public interface IListInspectionRoutesSMO {

    /**
     * 查询巡检路线信息
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listInspectionRoutes(IPageData pd) throws SMOException;
}
