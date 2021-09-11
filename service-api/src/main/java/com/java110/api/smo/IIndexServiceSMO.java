package com.java110.api.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 首页服务类
 */
public interface IIndexServiceSMO {

    /**
     * 获取统计信息
     * @param pd 页面请求数据
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> getStatisticInformation(IPageData pd);
}
