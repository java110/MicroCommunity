package com.java110.api.smo.org;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 组织管理管理服务接口类
 * <p>
 * add by wuxw 2019-06-29
 */
public interface IListOrgNoCommunitysSMO {

    /**
     * 查询组织管理信息
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listOrgNoCommunitys(IPageData pd) throws SMOException;
}
