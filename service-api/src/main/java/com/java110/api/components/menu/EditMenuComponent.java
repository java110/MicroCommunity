package com.java110.api.components.menu;

import com.java110.core.context.IPageData;
import com.java110.api.smo.menu.IEditMenuSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editMenu")
public class EditMenuComponent {

    @Autowired
    private IEditMenuSMO editMenuSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editMenuSMOImpl.updateMenu(pd);
    }

    public IEditMenuSMO getEditMenuSMOImpl() {
        return editMenuSMOImpl;
    }

    public void setEditMenuSMOImpl(IEditMenuSMO editMenuSMOImpl) {
        this.editMenuSMOImpl = editMenuSMOImpl;
    }
}
