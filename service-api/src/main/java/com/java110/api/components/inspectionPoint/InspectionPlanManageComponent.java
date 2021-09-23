package com.java110.api.components.inspectionPoint;


import com.java110.core.context.IPageData;
import com.java110.api.smo.inspection.IListInspectionPlansSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 巡检计划组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("inspectionPlanManage")
public class InspectionPlanManageComponent {

    @Autowired
    private IListInspectionPlansSMO listInspectionPlansSMOImpl;

    /**
     * 查询巡检计划列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return listInspectionPlansSMOImpl.listInspectionPlans(pd);
    }

    public IListInspectionPlansSMO getListInspectionPlansSMOImpl() {
        return listInspectionPlansSMOImpl;
    }

    public void setListInspectionPlansSMOImpl(IListInspectionPlansSMO listInspectionPlansSMOImpl) {
        this.listInspectionPlansSMOImpl = listInspectionPlansSMOImpl;
    }
}
