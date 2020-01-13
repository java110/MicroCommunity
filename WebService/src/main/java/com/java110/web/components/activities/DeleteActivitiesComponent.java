package com.java110.web.components.activities;

import com.java110.core.context.IPageData;
import com.java110.web.smo.activities.IDeleteActivitiesSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加活动组件
 */
@Component("deleteActivities")
public class DeleteActivitiesComponent {

    @Autowired
    private IDeleteActivitiesSMO deleteActivitiesSMOImpl;

    /**
     * 添加活动数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deleteActivitiesSMOImpl.deleteActivities(pd);
    }

    public IDeleteActivitiesSMO getDeleteActivitiesSMOImpl() {
        return deleteActivitiesSMOImpl;
    }

    public void setDeleteActivitiesSMOImpl(IDeleteActivitiesSMO deleteActivitiesSMOImpl) {
        this.deleteActivitiesSMOImpl = deleteActivitiesSMOImpl;
    }
}
