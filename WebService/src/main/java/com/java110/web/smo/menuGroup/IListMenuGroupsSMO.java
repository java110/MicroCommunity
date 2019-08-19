package com.java110.web.smo.menuGroup;

import com.java110.common.exception.SMOException;
import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 菜单组管理服务接口类
 *
 * add by wuxw 2019-06-29
 */
public interface IListMenuGroupsSMO {

    /**
     * 查询菜单组信息
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listMenuGroups(IPageData pd) throws SMOException;
}
