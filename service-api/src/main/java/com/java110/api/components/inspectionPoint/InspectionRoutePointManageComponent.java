package com.java110.api.components.inspectionPoint;


import com.java110.core.context.IPageData;
import com.java110.api.smo.inspection.IListInspectionRoutePointsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 巡检路线组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("inspectionRoutePointManage")
public class InspectionRoutePointManageComponent {

    @Autowired
    private IListInspectionRoutePointsSMO listInspectionRoutePointsSMOImpl;

    /**
     * 查询巡检路线列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return listInspectionRoutePointsSMOImpl.listInspectionRoutePoint(pd);
    }

    public IListInspectionRoutePointsSMO getListInspectionRoutePointsSMOImpl() {
        return listInspectionRoutePointsSMOImpl;
    }

    public void setListInspectionRoutePointsSMOImpl(IListInspectionRoutePointsSMO listInspectionRoutePointsSMOImpl) {
        this.listInspectionRoutePointsSMOImpl = listInspectionRoutePointsSMOImpl;
    }
}
