package com.java110.api.components.menu;

import com.java110.core.context.IPageData;
import com.java110.api.smo.menu.IAddMenuSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加菜单组件
 */
@Component("addMenu")
public class AddMenuComponent {

    @Autowired
    private IAddMenuSMO addMenuSMOImpl;

    /**
     * 添加菜单数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addMenuSMOImpl.saveMenu(pd);
    }

    public IAddMenuSMO getAddMenuSMOImpl() {
        return addMenuSMOImpl;
    }

    public void setAddMenuSMOImpl(IAddMenuSMO addMenuSMOImpl) {
        this.addMenuSMOImpl = addMenuSMOImpl;
    }
}
