package com.java110.common.bmo.appraise;

import com.java110.po.appraise.AppraisePo;
import org.springframework.http.ResponseEntity;

/**
 * 保存评价接口类
 */

public interface ISaveAppraiseBMO {

    /**
     * 保存接口评价
     *
     * @param appraisePo
     * @return
     */
    public ResponseEntity<String> saveAppraise(AppraisePo appraisePo);
}
