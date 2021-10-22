package com.java110.api.components.serviceProvide;


import com.java110.core.context.IPageData;
import com.java110.api.smo.service.IListServiceProvidesSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 应用组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("chooseServiceProvide")
public class ChooseServiceProvideComponent {

    @Autowired
    private IListServiceProvidesSMO listServiceProvidesSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listServiceProvidesSMOImpl.listServiceProvides(pd);
    }

    public IListServiceProvidesSMO getListServiceProvidesSMOImpl() {
        return listServiceProvidesSMOImpl;
    }

    public void setListServiceProvidesSMOImpl(IListServiceProvidesSMO listServiceProvidesSMOImpl) {
        this.listServiceProvidesSMOImpl = listServiceProvidesSMOImpl;
    }
}
