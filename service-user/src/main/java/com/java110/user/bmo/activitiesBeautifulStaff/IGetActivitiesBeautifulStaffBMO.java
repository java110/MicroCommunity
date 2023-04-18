package com.java110.user.bmo.activitiesBeautifulStaff;

import com.java110.dto.activities.ActivitiesBeautifulStaffDto;
import org.springframework.http.ResponseEntity;

public interface IGetActivitiesBeautifulStaffBMO {

    /**
     * 查询活动规则
     * add by wuxw
     *
     * @param activitiesBeautifulStaffDto
     * @return
     */
    ResponseEntity<String> get(ActivitiesBeautifulStaffDto activitiesBeautifulStaffDto);
}
