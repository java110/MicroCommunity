package com.java110.api.components.menuGroup;

import com.java110.core.context.IPageData;
import com.java110.api.smo.menu.IDeleteMenuGroupSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加菜单组组件
 */
@Component("deleteMenuGroup")
public class DeleteMenuGroupComponent {

@Autowired
private IDeleteMenuGroupSMO deleteMenuGroupSMOImpl;

/**
 * 添加菜单组数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteMenuGroupSMOImpl.deleteMenuGroup(pd);
    }

public IDeleteMenuGroupSMO getDeleteMenuGroupSMOImpl() {
        return deleteMenuGroupSMOImpl;
    }

public void setDeleteMenuGroupSMOImpl(IDeleteMenuGroupSMO deleteMenuGroupSMOImpl) {
        this.deleteMenuGroupSMOImpl = deleteMenuGroupSMOImpl;
    }
            }
