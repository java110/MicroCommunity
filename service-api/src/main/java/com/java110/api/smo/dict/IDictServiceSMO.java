package com.java110.api.smo.dict;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 查询 字典服务
 */
public interface IDictServiceSMO {
    /**
     * 查询字典
     * @param pd
     * @return
     */
    public ResponseEntity<String> listDict(IPageData pd);
}
