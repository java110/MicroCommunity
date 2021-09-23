package com.java110.api.components.inspectionPoint;


import com.java110.core.context.IPageData;
import com.java110.api.smo.inspection.IListInspectionPointsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 应用组件管理类
 * <p>
 * add by zc
 * <p>
 * 2020-02-10
 */
@Component("chooseInspectionPoint")
public class ChooseInspectionPointComponent {

    @Autowired
    private IListInspectionPointsSMO listInspectionPointsSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listInspectionPointsSMOImpl.listInspectionPoints(pd);
    }

    public IListInspectionPointsSMO getListInspectionPointsSMOImpl() {
        return listInspectionPointsSMOImpl;
    }

    public void setListInspectionPointsSMOImpl(IListInspectionPointsSMO listInspectionPointsSMOImpl) {
        this.listInspectionPointsSMOImpl = listInspectionPointsSMOImpl;
    }
}
