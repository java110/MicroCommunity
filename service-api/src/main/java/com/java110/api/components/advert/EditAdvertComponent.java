package com.java110.api.components.advert;

import com.java110.core.context.IPageData;
import com.java110.api.smo.advert.IEditAdvertSMO;
import com.java110.api.smo.advert.IListAdvertItemSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editAdvert")
public class EditAdvertComponent {

    @Autowired
    private IEditAdvertSMO editAdvertSMOImpl;

    @Autowired
    private IListAdvertItemSMO listAdvertItemSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editAdvertSMOImpl.updateAdvert(pd);
    }


    public ResponseEntity<String> listAdvertItem(IPageData pd){
        return listAdvertItemSMOImpl.listAdvertItems(pd);
    }

    public IEditAdvertSMO getEditAdvertSMOImpl() {
        return editAdvertSMOImpl;
    }

    public void setEditAdvertSMOImpl(IEditAdvertSMO editAdvertSMOImpl) {
        this.editAdvertSMOImpl = editAdvertSMOImpl;
    }

    public IListAdvertItemSMO getListAdvertItemSMOImpl() {
        return listAdvertItemSMOImpl;
    }

    public void setListAdvertItemSMOImpl(IListAdvertItemSMO listAdvertItemSMOImpl) {
        this.listAdvertItemSMOImpl = listAdvertItemSMOImpl;
    }
}
