package com.java110.web.components.activities;

import com.java110.core.context.IPageData;
import com.java110.web.smo.activities.IAddActivitiesSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加活动组件
 */
@Component("addActivitiesView")
public class AddActivitiesViewComponent {

    @Autowired
    private IAddActivitiesSMO addActivitiesSMOImpl;

    /**
     * 添加活动数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd) {
        return addActivitiesSMOImpl.saveActivities(pd);
    }

    public IAddActivitiesSMO getAddActivitiesSMOImpl() {
        return addActivitiesSMOImpl;
    }

    public void setAddActivitiesSMOImpl(IAddActivitiesSMO addActivitiesSMOImpl) {
        this.addActivitiesSMOImpl = addActivitiesSMOImpl;
    }
}
