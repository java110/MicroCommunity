package com.java110.api.components.basePrivilege;


import com.java110.core.context.IPageData;
import com.java110.api.smo.basePrivilege.IListBasePrivilegesSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 权限组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("basePrivilegeManage")
public class BasePrivilegeManageComponent {

    @Autowired
    private IListBasePrivilegesSMO listBasePrivilegesSMOImpl;

    /**
     * 查询权限列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return listBasePrivilegesSMOImpl.listBasePrivileges(pd);
    }

    public IListBasePrivilegesSMO getListBasePrivilegesSMOImpl() {
        return listBasePrivilegesSMOImpl;
    }

    public void setListBasePrivilegesSMOImpl(IListBasePrivilegesSMO listBasePrivilegesSMOImpl) {
        this.listBasePrivilegesSMOImpl = listBasePrivilegesSMOImpl;
    }
}
