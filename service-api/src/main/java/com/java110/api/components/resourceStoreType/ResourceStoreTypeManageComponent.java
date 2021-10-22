package com.java110.api.components.resourceStoreType;

import com.java110.core.context.IPageData;
import com.java110.api.smo.resourceStoreType.IListResourceStoreTypesSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 物品管理组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("resourceStoreTypeManage")
public class ResourceStoreTypeManageComponent {

    @Autowired
    private IListResourceStoreTypesSMO listResourceStoreTypesSMOImpl;

    /**
     * 查询物品管理列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listResourceStoreTypesSMOImpl.listResourceStoreTypes(pd);
    }

    public IListResourceStoreTypesSMO getListResourceStoresSMOImpl() {
        return listResourceStoreTypesSMOImpl;
    }

    public void setListResourceStoresSMOImpl(IListResourceStoreTypesSMO listResourceStoreTypesSMOImpl) {
        this.listResourceStoreTypesSMOImpl = listResourceStoreTypesSMOImpl;
    }
}
