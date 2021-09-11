package com.java110.api.components.carBlackWhite;

import com.java110.core.context.IPageData;
import com.java110.api.smo.carBlackWhite.IAddCarBlackWhiteSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加黑白名单组件
 */
@Component("addCarBlackWhite")
public class AddCarBlackWhiteComponent {

    @Autowired
    private IAddCarBlackWhiteSMO addCarBlackWhiteSMOImpl;

    /**
     * 添加黑白名单数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addCarBlackWhiteSMOImpl.saveCarBlackWhite(pd);
    }

    public IAddCarBlackWhiteSMO getAddCarBlackWhiteSMOImpl() {
        return addCarBlackWhiteSMOImpl;
    }

    public void setAddCarBlackWhiteSMOImpl(IAddCarBlackWhiteSMO addCarBlackWhiteSMOImpl) {
        this.addCarBlackWhiteSMOImpl = addCarBlackWhiteSMOImpl;
    }
}
