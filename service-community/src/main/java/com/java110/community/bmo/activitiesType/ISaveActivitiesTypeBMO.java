package com.java110.community.bmo.activitiesType;

import com.java110.po.activitiesType.ActivitiesTypePo;
import org.springframework.http.ResponseEntity;
public interface ISaveActivitiesTypeBMO {


    /**
     * 添加信息分类
     * add by wuxw
     * @param activitiesTypePo
     * @return
     */
    ResponseEntity<String> save(ActivitiesTypePo activitiesTypePo);


}
