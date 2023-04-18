package com.java110.community.bmo.activitiesType;
import com.java110.dto.activities.ActivitiesTypeDto;
import org.springframework.http.ResponseEntity;
public interface IGetActivitiesTypeBMO {


    /**
     * 查询信息分类
     * add by wuxw
     * @param  activitiesTypeDto
     * @return
     */
    ResponseEntity<String> get(ActivitiesTypeDto activitiesTypeDto);


}
