package com.java110.web.components.@@templateCode@@;

import com.java110.core.context.IPageData;
import com.java110.web.smo.@@templateCode@@.IEdit@@TemplateCode@@SMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("edit@@TemplateCode@@")
public class Edit@@TemplateCode@@Component {

    @Autowired
    private IEdit@@TemplateCode@@SMO edit@@TemplateCode@@SMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return edit@@TemplateCode@@SMOImpl.update@@TemplateCode@@(pd);
    }

    public IEdit@@TemplateCode@@SMO getEdit@@TemplateCode@@SMOImpl() {
        return edit@@TemplateCode@@SMOImpl;
    }

    public void setEdit@@TemplateCode@@SMOImpl(IEdit@@TemplateCode@@SMO edit@@TemplateCode@@SMOImpl) {
        this.edit@@TemplateCode@@SMOImpl = edit@@TemplateCode@@SMOImpl;
    }
}
