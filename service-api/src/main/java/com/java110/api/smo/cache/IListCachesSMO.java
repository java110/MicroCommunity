package com.java110.api.smo.cache;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 缓存管理服务接口类
 *
 * add by wuxw 2019-06-29
 */
public interface IListCachesSMO {

    /**
     * 查询缓存信息
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层 异常
     */
    ResponseEntity<String> listCaches(IPageData pd) throws SMOException;


    /**
     * 刷新缓存
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层 异常
     */
    ResponseEntity<String> flushCache(IPageData pd) throws SMOException;
}
