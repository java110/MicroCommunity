package com.java110.api.components.service;

import com.java110.core.context.IPageData;
import com.java110.api.smo.service.IListServicesSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName ChooseServiceComponent
 * @Description TODO
 * @Author wuxw
 * @Date 2019/7/20 16:39
 * @Version 1.0
 * add by wuxw 2019/7/20
 **/
@Component("chooseService")
public class ChooseServiceComponent {

    @Autowired
    private IListServicesSMO listServicesSMOImpl;

    /**
     * 查询服务列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listServicesSMOImpl.listServices(pd);
    }

    public IListServicesSMO getListServicesSMOImpl() {
        return listServicesSMOImpl;
    }

    public void setListServicesSMOImpl(IListServicesSMO listServicesSMOImpl) {
        this.listServicesSMOImpl = listServicesSMOImpl;
    }
}
