package com.java110.api.components.fee;

import com.java110.core.context.IPageData;
import com.java110.api.smo.fee.IEditFeeConfigSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editFeeConfig")
public class EditFeeConfigComponent {

    @Autowired
    private IEditFeeConfigSMO editFeeConfigSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editFeeConfigSMOImpl.updateFeeConfig(pd);
    }

    public IEditFeeConfigSMO getEditFeeConfigSMOImpl() {
        return editFeeConfigSMOImpl;
    }

    public void setEditFeeConfigSMOImpl(IEditFeeConfigSMO editFeeConfigSMOImpl) {
        this.editFeeConfigSMOImpl = editFeeConfigSMOImpl;
    }
}
