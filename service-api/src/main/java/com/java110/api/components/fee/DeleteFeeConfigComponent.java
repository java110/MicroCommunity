package com.java110.api.components.fee;

import com.java110.core.context.IPageData;
import com.java110.api.smo.fee.IDeleteFeeConfigSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加费用项组件
 */
@Component("deleteFeeConfig")
public class DeleteFeeConfigComponent {

@Autowired
private IDeleteFeeConfigSMO deleteFeeConfigSMOImpl;

/**
 * 添加费用项数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteFeeConfigSMOImpl.deleteFeeConfig(pd);
    }

public IDeleteFeeConfigSMO getDeleteFeeConfigSMOImpl() {
        return deleteFeeConfigSMOImpl;
    }

public void setDeleteFeeConfigSMOImpl(IDeleteFeeConfigSMO deleteFeeConfigSMOImpl) {
        this.deleteFeeConfigSMOImpl = deleteFeeConfigSMOImpl;
    }
            }
