package com.java110.api.components.menu;


import com.java110.core.context.IPageData;
import com.java110.api.smo.menu.IListMenusSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 应用组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("chooseMenu")
public class ChooseMenuComponent {

    @Autowired
    private IListMenusSMO listMenusSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listMenusSMOImpl.listMenus(pd);
    }

    public IListMenusSMO getListMenusSMOImpl() {
        return listMenusSMOImpl;
    }

    public void setListMenusSMOImpl(IListMenusSMO listMenusSMOImpl) {
        this.listMenusSMOImpl = listMenusSMOImpl;
    }
}
