package com.java110.api.components.service;

import com.java110.core.context.IPageData;
import com.java110.api.smo.service.IDeleteServiceImplSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加服务实现组件
 */
@Component("deleteServiceImpl")
public class DeleteServiceImplComponent {

@Autowired
private IDeleteServiceImplSMO deleteServiceImplSMOImpl;

/**
 * 添加服务实现数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteServiceImplSMOImpl.deleteServiceImpl(pd);
    }

public IDeleteServiceImplSMO getDeleteServiceImplSMOImpl() {
        return deleteServiceImplSMOImpl;
    }

public void setDeleteServiceImplSMOImpl(IDeleteServiceImplSMO deleteServiceImplSMOImpl) {
        this.deleteServiceImplSMOImpl = deleteServiceImplSMOImpl;
    }
            }
