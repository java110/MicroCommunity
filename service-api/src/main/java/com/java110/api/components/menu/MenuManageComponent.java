package com.java110.api.components.menu;


import com.java110.core.context.IPageData;
import com.java110.api.smo.menu.IListMenusSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 菜单组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("menuManage")
public class MenuManageComponent {

    @Autowired
    private IListMenusSMO listMenusSMOImpl;

    /**
     * 查询菜单列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return listMenusSMOImpl.listMenus(pd);
    }

    public IListMenusSMO getListMenusSMOImpl() {
        return listMenusSMOImpl;
    }

    public void setListMenusSMOImpl(IListMenusSMO listMenusSMOImpl) {
        this.listMenusSMOImpl = listMenusSMOImpl;
    }
}
