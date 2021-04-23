package com.java110.community.bmo.activitiesType;
import com.java110.po.activitiesType.ActivitiesTypePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteActivitiesTypeBMO {


    /**
     * 修改信息分类
     * add by wuxw
     * @param activitiesTypePo
     * @return
     */
    ResponseEntity<String> delete(ActivitiesTypePo activitiesTypePo);


}
