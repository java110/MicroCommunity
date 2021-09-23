package com.java110.api.smo.common;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @ClassName ICommonSMO
 * @Description 公共业务处理类
 * @Author wuxw
 * @Date 2020/3/8 23:35
 * @Version 1.0
 * add by wuxw 2020/3/8
 **/
public interface ICommonGetSMO {

    /**
     * 添加添加业主
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> doService(IPageData pd);
}
