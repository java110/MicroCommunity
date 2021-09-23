package com.java110.api.smo.resourceStoreType;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 物品类型管理管理服务接口类
 *
 * add by fqz 2021-04-21 9:47
 */
public interface IListResourceStoreTypesSMO {

    /**
     * 查询物品管理信息
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listResourceStoreTypes(IPageData pd) throws SMOException;
}
