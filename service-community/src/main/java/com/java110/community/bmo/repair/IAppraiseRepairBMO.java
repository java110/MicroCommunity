package com.java110.community.bmo.repair;

import com.java110.dto.appraise.AppraiseDto;
import org.springframework.http.ResponseEntity;

/**
 * 保存评价接口类
 */
public interface IAppraiseRepairBMO {

    /**
     * 保存接口评价
     *
     * @param appraiseDto
     * @return
     */
    public ResponseEntity<String> appraiseRepair(AppraiseDto appraiseDto);
}
