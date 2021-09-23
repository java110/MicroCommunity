package com.java110.api.components.serviceRegister;


import com.java110.core.context.IPageData;
import com.java110.api.smo.app.IListAppsSMO;
import com.java110.api.smo.service.IListServiceRegistersSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 服务绑定组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("serviceRegisterManage")
public class ServiceRegisterManageComponent {

    @Autowired
    private IListServiceRegistersSMO listServiceRegistersSMOImpl;

    @Autowired
    private IListAppsSMO listAppsSMOImpl;

    /**
     * 查询服务绑定列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return listServiceRegistersSMOImpl.listServiceRegisters(pd);
    }


    public ResponseEntity<String> loadApp(IPageData pd){
        return listAppsSMOImpl.listApps(pd);
    }

    public IListServiceRegistersSMO getListServiceRegistersSMOImpl() {
        return listServiceRegistersSMOImpl;
    }

    public void setListServiceRegistersSMOImpl(IListServiceRegistersSMO listServiceRegistersSMOImpl) {
        this.listServiceRegistersSMOImpl = listServiceRegistersSMOImpl;
    }

    public IListAppsSMO getListAppsSMOImpl() {
        return listAppsSMOImpl;
    }

    public void setListAppsSMOImpl(IListAppsSMO listAppsSMOImpl) {
        this.listAppsSMOImpl = listAppsSMOImpl;
    }
}
