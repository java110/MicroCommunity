package com.java110.api.components.serviceRegister;

import com.java110.core.context.IPageData;
import com.java110.api.smo.service.IDeleteServiceRegisterSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加服务绑定组件
 */
@Component("deleteServiceRegister")
public class DeleteServiceRegisterComponent {

@Autowired
private IDeleteServiceRegisterSMO deleteServiceRegisterSMOImpl;

/**
 * 添加服务绑定数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteServiceRegisterSMOImpl.deleteServiceRegister(pd);
    }

public IDeleteServiceRegisterSMO getDeleteServiceRegisterSMOImpl() {
        return deleteServiceRegisterSMOImpl;
    }

public void setDeleteServiceRegisterSMOImpl(IDeleteServiceRegisterSMO deleteServiceRegisterSMOImpl) {
        this.deleteServiceRegisterSMOImpl = deleteServiceRegisterSMOImpl;
    }
            }
