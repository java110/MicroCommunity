package com.java110.web.components.activities;


import com.java110.core.context.IPageData;
import com.java110.web.smo.activities.IListActivitiessSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 活动组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("activitiesManage")
public class ActivitiesManageComponent {

    @Autowired
    private IListActivitiessSMO listActivitiessSMOImpl;

    /**
     * 查询活动列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listActivitiessSMOImpl.listActivitiess(pd);
    }

    public IListActivitiessSMO getListActivitiessSMOImpl() {
        return listActivitiessSMOImpl;
    }

    public void setListActivitiessSMOImpl(IListActivitiessSMO listActivitiessSMOImpl) {
        this.listActivitiessSMOImpl = listActivitiessSMOImpl;
    }
}
