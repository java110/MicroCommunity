package com.java110.api.components.service;


import com.java110.core.context.IPageData;
import com.java110.api.smo.app.IListAppsSMO;
import com.java110.api.smo.service.IListServicesSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 服务组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("serviceManage")
public class ServiceManageComponent {

    @Autowired
    private IListServicesSMO listServicesSMOImpl;

    @Autowired
    private IListAppsSMO listAppsSMOImpl;

    /**
     * 查询服务列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listServicesSMOImpl.listServices(pd);
    }

    public ResponseEntity<String> loadApp(IPageData pd){
        return listAppsSMOImpl.listApps(pd);
    }

    public IListServicesSMO getListServicesSMOImpl() {
        return listServicesSMOImpl;
    }

    public void setListServicesSMOImpl(IListServicesSMO listServicesSMOImpl) {
        this.listServicesSMOImpl = listServicesSMOImpl;
    }

    public IListAppsSMO getListAppsSMOImpl() {
        return listAppsSMOImpl;
    }

    public void setListAppsSMOImpl(IListAppsSMO listAppsSMOImpl) {
        this.listAppsSMOImpl = listAppsSMOImpl;
    }
}
