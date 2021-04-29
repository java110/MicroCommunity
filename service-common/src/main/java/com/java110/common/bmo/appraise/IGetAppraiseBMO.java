package com.java110.common.bmo.appraise;

import com.java110.dto.appraise.AppraiseDto;
import org.springframework.http.ResponseEntity;

/**
 * 保存评价接口类
 */

public interface IGetAppraiseBMO {

    /**
     * 查询接口评价
     *
     * @param appraiseDto
     * @return
     */
    public ResponseEntity<String> getAppraise(AppraiseDto appraiseDto);
}
