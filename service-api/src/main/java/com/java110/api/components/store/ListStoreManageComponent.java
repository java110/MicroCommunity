package com.java110.api.components.store;


import com.java110.core.context.IPageData;
import com.java110.api.smo.store.IGetStoreSMO;
import com.java110.api.smo.store.IListStoreSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 钥匙申请组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("listStoreManage")
public class ListStoreManageComponent {

    @Autowired
    private IListStoreSMO listStoreSMOImpl;
    @Autowired
    private IGetStoreSMO getStoreSMOImpl;

    /**
     * 查询钥匙申请列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listStoreSMOImpl.listStores(pd);
    }


    public ResponseEntity<String> getStoreInfo(IPageData pd) {
        return getStoreSMOImpl.getStoreInfo(pd);
    }

    public IListStoreSMO getListStoreSMOImpl() {
        return listStoreSMOImpl;
    }

    public void setListStoreSMOImpl(IListStoreSMO listStoreSMOImpl) {
        this.listStoreSMOImpl = listStoreSMOImpl;
    }
}
