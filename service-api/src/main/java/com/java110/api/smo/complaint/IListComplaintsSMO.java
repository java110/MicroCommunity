package com.java110.api.smo.complaint;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 投诉建议管理服务接口类
 * <p>
 * add by wuxw 2019-06-29
 */
public interface IListComplaintsSMO {

    /**
     * 查询投诉建议信息
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listComplaints(IPageData pd) throws SMOException;
}
