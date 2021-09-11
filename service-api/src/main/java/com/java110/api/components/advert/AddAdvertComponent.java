package com.java110.api.components.advert;

import com.java110.core.context.IPageData;
import com.java110.api.smo.advert.IAddAdvertSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加发布广告组件
 */
@Component("addAdvert")
public class AddAdvertComponent {

    @Autowired
    private IAddAdvertSMO addAdvertSMOImpl;

    /**
     * 添加发布广告数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addAdvertSMOImpl.saveAdvert(pd);
    }

    public IAddAdvertSMO getAddAdvertSMOImpl() {
        return addAdvertSMOImpl;
    }

    public void setAddAdvertSMOImpl(IAddAdvertSMO addAdvertSMOImpl) {
        this.addAdvertSMOImpl = addAdvertSMOImpl;
    }
}
