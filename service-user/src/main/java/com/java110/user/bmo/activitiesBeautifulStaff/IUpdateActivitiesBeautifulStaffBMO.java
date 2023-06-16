package com.java110.user.bmo.activitiesBeautifulStaff;
import com.java110.po.activities.ActivitiesBeautifulStaffPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateActivitiesBeautifulStaffBMO {


    /**
     * 修改活动规则
     * add by wuxw
     * @param activitiesBeautifulStaffPo
     * @return
     */
    ResponseEntity<String> update(ActivitiesBeautifulStaffPo activitiesBeautifulStaffPo);


}
