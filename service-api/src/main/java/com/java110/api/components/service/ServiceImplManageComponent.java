package com.java110.api.components.service;


import com.java110.core.context.IPageData;
import com.java110.api.smo.service.IListServiceImplsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 服务实现组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("serviceImplManage")
public class ServiceImplManageComponent {

    @Autowired
    private IListServiceImplsSMO listServiceImplsSMOImpl;

    /**
     * 查询服务实现列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return listServiceImplsSMOImpl.listServiceImpls(pd);
    }

    public IListServiceImplsSMO getListServiceImplsSMOImpl() {
        return listServiceImplsSMOImpl;
    }

    public void setListServiceImplsSMOImpl(IListServiceImplsSMO listServiceImplsSMOImpl) {
        this.listServiceImplsSMOImpl = listServiceImplsSMOImpl;
    }
}
