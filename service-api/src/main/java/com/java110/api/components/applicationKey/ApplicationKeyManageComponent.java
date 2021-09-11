package com.java110.api.components.applicationKey;


import com.java110.core.context.IPageData;
import com.java110.api.smo.applicationKey.IListApplicationKeysSMO;
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
@Component("applicationKeyManage")
public class ApplicationKeyManageComponent {

    @Autowired
    private IListApplicationKeysSMO listApplicationKeysSMOImpl;

    /**
     * 查询钥匙申请列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listApplicationKeysSMOImpl.listApplicationKeys(pd);
    }

    public IListApplicationKeysSMO getListApplicationKeysSMOImpl() {
        return listApplicationKeysSMOImpl;
    }

    public void setListApplicationKeysSMOImpl(IListApplicationKeysSMO listApplicationKeysSMOImpl) {
        this.listApplicationKeysSMOImpl = listApplicationKeysSMOImpl;
    }
}
