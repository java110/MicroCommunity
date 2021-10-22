package com.java110.api.components.serviceProvide;

import com.java110.core.context.IPageData;
import com.java110.api.smo.service.IDeleteServiceProvideSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加服务提供组件
 */
@Component("deleteServiceProvide")
public class DeleteServiceProvideComponent {

@Autowired
private IDeleteServiceProvideSMO deleteServiceProvideSMOImpl;

/**
 * 添加服务提供数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteServiceProvideSMOImpl.deleteServiceProvide(pd);
    }

public IDeleteServiceProvideSMO getDeleteServiceProvideSMOImpl() {
        return deleteServiceProvideSMOImpl;
    }

public void setDeleteServiceProvideSMOImpl(IDeleteServiceProvideSMO deleteServiceProvideSMOImpl) {
        this.deleteServiceProvideSMOImpl = deleteServiceProvideSMOImpl;
    }
            }
