package com.java110.api.smo.complaint;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 投诉建议
 */
public interface ISaveComplaintSMO {

    /**
     * 提交投诉建议
     * @param pd
     * @return
     */
    public ResponseEntity<String> save(IPageData pd);

}
