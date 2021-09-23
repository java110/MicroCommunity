package com.java110.api.components.corder;

import com.java110.core.context.IPageData;
import com.java110.api.smo.corder.IListCordersSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 订单
 */
@Component("corderManage")
public class CorderManageComponent {
    @Autowired
    private IListCordersSMO listCordersSMOImpl;

    /**
     * 查询发布广告列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listCordersSMOImpl.listCorders(pd);
    }


    public IListCordersSMO getListCordersSMOImpl() {
        return listCordersSMOImpl;
    }

    public void setListCordersSMOImpl(IListCordersSMO listCordersSMOImpl) {
        this.listCordersSMOImpl = listCordersSMOImpl;
    }
}
