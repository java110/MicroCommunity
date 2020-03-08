package com.java110.front.components.@@templateCode@@;


import com.java110.core.context.IPageData;
import com.java110.web.smo.@@templateCode@@.IList@@TemplateCode@@sSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 应用组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("choose@@TemplateCode@@")
public class Choose@@TemplateCode@@Component {

    @Autowired
    private IList@@TemplateCode@@sSMO list@@TemplateCode@@sSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return list@@TemplateCode@@sSMOImpl.list@@TemplateCode@@s(pd);
    }

    public IList@@TemplateCode@@sSMO getList@@TemplateCode@@sSMOImpl() {
        return list@@TemplateCode@@sSMOImpl;
    }

    public void setList@@TemplateCode@@sSMOImpl(IList@@TemplateCode@@sSMO list@@TemplateCode@@sSMOImpl) {
        this.list@@TemplateCode@@sSMOImpl = list@@TemplateCode@@sSMOImpl;
    }
}
