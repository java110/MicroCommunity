package com.java110.api.components.service;

import com.java110.core.context.IPageData;
import com.java110.api.smo.service.IDeleteServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加服务组件
 */
@Component("deleteService")
public class DeleteServiceComponent {

@Autowired
private IDeleteServiceSMO deleteServiceSMOImpl;

/**
 * 添加服务数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteServiceSMOImpl.deleteService(pd);
    }

public IDeleteServiceSMO getDeleteServiceSMOImpl() {
        return deleteServiceSMOImpl;
    }

public void setDeleteServiceSMOImpl(IDeleteServiceSMO deleteServiceSMOImpl) {
        this.deleteServiceSMOImpl = deleteServiceSMOImpl;
    }
            }
