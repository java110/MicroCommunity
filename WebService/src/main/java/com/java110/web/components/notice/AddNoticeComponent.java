package com.java110.web.components.notice;

import com.java110.core.context.IPageData;
import com.java110.web.smo.notice.IAddNoticeSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加公告组件
 */
@Component("addNotice")
public class AddNoticeComponent {

    @Autowired
    private IAddNoticeSMO addNoticeSMOImpl;

    /**
     * 添加公告数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addNoticeSMOImpl.saveNotice(pd);
    }

    public IAddNoticeSMO getAddNoticeSMOImpl() {
        return addNoticeSMOImpl;
    }

    public void setAddNoticeSMOImpl(IAddNoticeSMO addNoticeSMOImpl) {
        this.addNoticeSMOImpl = addNoticeSMOImpl;
    }
}
