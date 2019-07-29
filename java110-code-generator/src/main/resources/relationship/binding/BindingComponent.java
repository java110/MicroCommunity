package com.java110.web.components.@@templateCode@@;

import com.java110.core.context.IPageData;
import com.java110.web.smo.@@templateCode@@.I@@TemplateCode@@BindingSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加@@templateName@@组件
 */
@Component("@@templateCode@@Binding")
public class @@TemplateCode@@BindingComponent {

    @Autowired
    private I@@TemplateCode@@BindingSMO @@templateCode@@BindingSMOImpl;

    /**
     * 添加@@templateName@@数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> binding(IPageData pd){
        return @@templateCode@@BindingSMOImpl.binding@@TemplateCode@@(pd);
    }

    public I@@TemplateCode@@BindingSMO get@@TemplateCode@@BindingSMOImpl() {
        return @@templateCode@@BindingSMOImpl;
    }

    public void set@@TemplateCode@@BindingSMOImpl(I@@TemplateCode@@BindingSMO @@templateCode@@BindingSMOImpl) {
        this.@@templateCode@@BindingSMOImpl = @@templateCode@@BindingSMOImpl;
    }
}
