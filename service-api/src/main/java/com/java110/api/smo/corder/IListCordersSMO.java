package com.java110.api.smo.corder;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 查询广告列表
 */
public interface IListCordersSMO {
    /**
     * 查询发布广告信息
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listCorders(IPageData pd) throws SMOException;
}
