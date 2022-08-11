package com.java110.api.components.org;

import com.java110.core.context.IPageData;
import com.java110.api.smo.org.IListOrgsSMO;
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
@Component("chooseOrg")
public class ChooseOrgComponent {

    @Autowired
    private IListOrgsSMO listOrgsSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listOrgsSMOImpl.listOrgs(pd);
    }

    public IListOrgsSMO getListOrgsSMOImpl() {
        return listOrgsSMOImpl;
    }

    public void setListOrgsSMOImpl(IListOrgsSMO listOrgsSMOImpl) {
        this.listOrgsSMOImpl = listOrgsSMOImpl;
    }
}
