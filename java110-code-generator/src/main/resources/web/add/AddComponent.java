package com.java110.front.components.@@templateCode@@;

import com.java110.core.context.IPageData;
import com.java110.web.smo.@@templateCode@@.IAdd@@TemplateCode@@SMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加@@templateName@@组件
 */
@Component("add@@TemplateCode@@")
public class Add@@TemplateCode@@Component {

    @Autowired
    private IAdd@@TemplateCode@@SMO add@@TemplateCode@@SMOImpl;

    /**
     * 添加@@templateName@@数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return add@@TemplateCode@@SMOImpl.save@@TemplateCode@@(pd);
    }

    public IAdd@@TemplateCode@@SMO getAdd@@TemplateCode@@SMOImpl() {
        return add@@TemplateCode@@SMOImpl;
    }

    public void setAdd@@TemplateCode@@SMOImpl(IAdd@@TemplateCode@@SMO add@@TemplateCode@@SMOImpl) {
        this.add@@TemplateCode@@SMOImpl = add@@TemplateCode@@SMOImpl;
    }
}
