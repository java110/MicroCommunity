package com.java110.api.components.notice;

import com.java110.core.context.IPageData;
import com.java110.api.smo.notice.IEditNoticeSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editNotice")
public class EditNoticeComponent {

    @Autowired
    private IEditNoticeSMO editNoticeSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editNoticeSMOImpl.updateNotice(pd);
    }

    public IEditNoticeSMO getEditNoticeSMOImpl() {
        return editNoticeSMOImpl;
    }

    public void setEditNoticeSMOImpl(IEditNoticeSMO editNoticeSMOImpl) {
        this.editNoticeSMOImpl = editNoticeSMOImpl;
    }
}
