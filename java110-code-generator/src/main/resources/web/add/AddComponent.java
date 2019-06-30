package com.java110.web.components.@@templateCode@@;

import com.java110.core.context.IPageData;
import com.java110.web.smo.I@@TemplateCode@@ServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加@@templateName@@组件
 */
@Component("add@@TemplateCode@@")
public class Add@@TemplateCode@@Component {

    @Autowired
    private I@@TemplateCode@@ServiceSMO @@templateCode@@ServiceSMOImpl;

    /**
     * 查询@@templateName@@信息
     * @param pd 页面封装对象 包含页面请求数据
     * @return ResponseEntity对象返回给页面
     */
    public ResponseEntity<String> save(IPageData pd) {

        return @@templateCode@@ServiceSMOImpl.save@@TemplateCode@@(pd);
    }

    public I@@TemplateCode@@ServiceSMO get@@TemplateCode@@ServiceSMOImpl() {
        return @@templateCode@@ServiceSMOImpl;
    }

    public void set@@TemplateCode@@ServiceSMOImpl(I@@TemplateCode@@ServiceSMO @@templateCode@@ServiceSMOImpl) {
        this.@@templateCode@@ServiceSMOImpl = @@templateCode@@ServiceSMOImpl;
    }
}
