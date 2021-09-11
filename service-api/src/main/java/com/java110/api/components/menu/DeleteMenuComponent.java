package com.java110.api.components.menu;

import com.java110.core.context.IPageData;
import com.java110.api.smo.menu.IDeleteMenuSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加菜单组件
 */
@Component("deleteMenu")
public class DeleteMenuComponent {

@Autowired
private IDeleteMenuSMO deleteMenuSMOImpl;

/**
 * 添加菜单数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteMenuSMOImpl.deleteMenu(pd);
    }

public IDeleteMenuSMO getDeleteMenuSMOImpl() {
        return deleteMenuSMOImpl;
    }

public void setDeleteMenuSMOImpl(IDeleteMenuSMO deleteMenuSMOImpl) {
        this.deleteMenuSMOImpl = deleteMenuSMOImpl;
    }
            }
