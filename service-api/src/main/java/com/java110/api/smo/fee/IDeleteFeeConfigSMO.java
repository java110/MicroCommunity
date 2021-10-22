package com.java110.api.smo.fee;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加费用项接口
 *
 * add by wuxw 2019-06-30
 */
public interface IDeleteFeeConfigSMO {

    /**
     * 添加费用项
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> deleteFeeConfig(IPageData pd);
}
