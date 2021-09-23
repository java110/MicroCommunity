package com.java110.api.components.activities;

import com.java110.core.context.IPageData;
import com.java110.api.smo.activities.IEditActivitiesSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editActivitiesView")
public class EditActivitiesViewComponent {

    @Autowired
    private IEditActivitiesSMO editActivitiesSMOImpl;

    /**
     * 添加小区数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd) {
        return editActivitiesSMOImpl.updateActivities(pd);
    }

    public IEditActivitiesSMO getEditActivitiesSMOImpl() {
        return editActivitiesSMOImpl;
    }

    public void setEditActivitiesSMOImpl(IEditActivitiesSMO editActivitiesSMOImpl) {
        this.editActivitiesSMOImpl = editActivitiesSMOImpl;
    }
}
