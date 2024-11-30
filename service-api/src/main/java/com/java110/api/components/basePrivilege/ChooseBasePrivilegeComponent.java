package com.java110.api.components.basePrivilege;


import com.java110.api.smo.basePrivilege.IListBasePrivilegesSMO;
import com.java110.core.context.IPageData;
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
@Component("chooseBasePrivilege")
public class ChooseBasePrivilegeComponent {

    @Autowired
    private IListBasePrivilegesSMO listBasePrivilegesSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listBasePrivilegesSMOImpl.listBasePrivileges(pd);
    }

    public IListBasePrivilegesSMO getListBasePrivilegesSMOImpl() {
        return listBasePrivilegesSMOImpl;
    }

    public void setListBasePrivilegesSMOImpl(IListBasePrivilegesSMO listBasePrivilegesSMOImpl) {
        this.listBasePrivilegesSMOImpl = listBasePrivilegesSMOImpl;
    }
}
