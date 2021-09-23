package com.java110.api.components.machine;

import com.java110.core.context.IPageData;
import com.java110.api.smo.machine.IAddMachineTranslateSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加设备同步组件
 */
@Component("addMachineTranslate")
public class AddMachineTranslateComponent {

    @Autowired
    private IAddMachineTranslateSMO addMachineTranslateSMOImpl;

    /**
     * 添加设备同步数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addMachineTranslateSMOImpl.saveMachineTranslate(pd);
    }

    public IAddMachineTranslateSMO getAddMachineTranslateSMOImpl() {
        return addMachineTranslateSMOImpl;
    }

    public void setAddMachineTranslateSMOImpl(IAddMachineTranslateSMO addMachineTranslateSMOImpl) {
        this.addMachineTranslateSMOImpl = addMachineTranslateSMOImpl;
    }
}
