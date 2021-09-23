package com.java110.api.components.menuGroup;

import com.java110.core.context.IPageData;
import com.java110.api.smo.menu.IAddMenuGroupSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加菜单组组件
 */
@Component("addMenuGroup")
public class AddMenuGroupComponent {

    @Autowired
    private IAddMenuGroupSMO addMenuGroupSMOImpl;

    /**
     * 添加菜单组数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addMenuGroupSMOImpl.saveMenuGroup(pd);
    }

    public IAddMenuGroupSMO getAddMenuGroupSMOImpl() {
        return addMenuGroupSMOImpl;
    }

    public void setAddMenuGroupSMOImpl(IAddMenuGroupSMO addMenuGroupSMOImpl) {
        this.addMenuGroupSMOImpl = addMenuGroupSMOImpl;
    }
}
