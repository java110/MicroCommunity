package com.java110.front.components.@@templateCode@@;

import com.java110.core.context.IPageData;
import com.java110.web.smo.@@templateCode@@.IDelete@@TemplateCode@@SMO;
import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Component;

/**
 * 添加@@templateName@@组件
 */
@Component("delete@@TemplateCode@@")
public class Delete@@TemplateCode@@Component {

@Autowired
private IDelete@@TemplateCode@@SMO delete@@TemplateCode@@SMOImpl;

/**
 * 添加@@templateName@@数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return delete@@TemplateCode@@SMOImpl.delete@@TemplateCode@@(pd);
    }

public IDelete@@TemplateCode@@SMO getDelete@@TemplateCode@@SMOImpl() {
        return delete@@TemplateCode@@SMOImpl;
    }

public void setDelete@@TemplateCode@@SMOImpl(IDelete@@TemplateCode@@SMO delete@@TemplateCode@@SMOImpl) {
        this.delete@@TemplateCode@@SMOImpl = delete@@TemplateCode@@SMOImpl;
    }
            }
