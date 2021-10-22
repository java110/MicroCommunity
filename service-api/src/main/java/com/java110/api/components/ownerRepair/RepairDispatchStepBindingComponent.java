package com.java110.api.components.ownerRepair;

import com.java110.core.context.IPageData;
import com.java110.api.smo.ownerRepair.IRepairDispatchStepBindingSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加报修派单组件
 */
@Component("repairDispatchStepBinding")
public class RepairDispatchStepBindingComponent {

    @Autowired
    private IRepairDispatchStepBindingSMO repairDispatchStepBindingSMOImpl;

    /**
     * 添加报修派单数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> binding(IPageData pd){
        return repairDispatchStepBindingSMOImpl.bindingRepairDispatchStep(pd);
    }

    public IRepairDispatchStepBindingSMO getRepairDispatchStepBindingSMOImpl() {
        return repairDispatchStepBindingSMOImpl;
    }

    public void setRepairDispatchStepBindingSMOImpl(IRepairDispatchStepBindingSMO repairDispatchStepBindingSMOImpl) {
        this.repairDispatchStepBindingSMOImpl = repairDispatchStepBindingSMOImpl;
    }
}
