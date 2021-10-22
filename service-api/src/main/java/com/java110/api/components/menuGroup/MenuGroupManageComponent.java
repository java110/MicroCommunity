package com.java110.api.components.menuGroup;


import com.java110.core.context.IPageData;
import com.java110.api.smo.menu.IListMenuGroupsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 菜单组组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("menuGroupManage")
public class MenuGroupManageComponent {

    @Autowired
    private IListMenuGroupsSMO listMenuGroupsSMOImpl;

    /**
     * 查询菜单组列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listMenuGroupsSMOImpl.listMenuGroups(pd);
    }

    public IListMenuGroupsSMO getListMenuGroupsSMOImpl() {
        return listMenuGroupsSMOImpl;
    }

    public void setListMenuGroupsSMOImpl(IListMenuGroupsSMO listMenuGroupsSMOImpl) {
        this.listMenuGroupsSMOImpl = listMenuGroupsSMOImpl;
    }
}
