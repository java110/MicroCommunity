package com.java110.api.components.carBlackWhite;

import com.java110.core.context.IPageData;
import com.java110.api.smo.carBlackWhite.IEditCarBlackWhiteSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editCarBlackWhite")
public class EditCarBlackWhiteComponent {

    @Autowired
    private IEditCarBlackWhiteSMO editCarBlackWhiteSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editCarBlackWhiteSMOImpl.updateCarBlackWhite(pd);
    }

    public IEditCarBlackWhiteSMO getEditCarBlackWhiteSMOImpl() {
        return editCarBlackWhiteSMOImpl;
    }

    public void setEditCarBlackWhiteSMOImpl(IEditCarBlackWhiteSMO editCarBlackWhiteSMOImpl) {
        this.editCarBlackWhiteSMOImpl = editCarBlackWhiteSMOImpl;
    }
}
