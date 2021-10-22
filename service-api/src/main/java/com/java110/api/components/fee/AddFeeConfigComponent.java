package com.java110.api.components.fee;

import com.java110.core.context.IPageData;
import com.java110.api.smo.fee.IAddFeeConfigSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加费用项组件
 */
@Component("addFeeConfig")
public class AddFeeConfigComponent {

    @Autowired
    private IAddFeeConfigSMO addFeeConfigSMOImpl;

    /**
     * 添加费用项数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addFeeConfigSMOImpl.saveFeeConfig(pd);
    }

    public IAddFeeConfigSMO getAddFeeConfigSMOImpl() {
        return addFeeConfigSMOImpl;
    }

    public void setAddFeeConfigSMOImpl(IAddFeeConfigSMO addFeeConfigSMOImpl) {
        this.addFeeConfigSMOImpl = addFeeConfigSMOImpl;
    }
}
