package com.java110.api.components.basePrivilege;

import com.java110.core.context.IPageData;
import com.java110.api.smo.basePrivilege.IDeleteBasePrivilegeSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加权限组件
 */
@Component("deleteBasePrivilege")
public class DeleteBasePrivilegeComponent {

@Autowired
private IDeleteBasePrivilegeSMO deleteBasePrivilegeSMOImpl;

/**
 * 添加权限数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteBasePrivilegeSMOImpl.deleteBasePrivilege(pd);
    }

public IDeleteBasePrivilegeSMO getDeleteBasePrivilegeSMOImpl() {
        return deleteBasePrivilegeSMOImpl;
    }

public void setDeleteBasePrivilegeSMOImpl(IDeleteBasePrivilegeSMO deleteBasePrivilegeSMOImpl) {
        this.deleteBasePrivilegeSMOImpl = deleteBasePrivilegeSMOImpl;
    }
            }
