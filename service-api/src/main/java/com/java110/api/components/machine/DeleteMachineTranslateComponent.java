package com.java110.api.components.machine;

import com.java110.core.context.IPageData;
import com.java110.api.smo.machine.IDeleteMachineTranslateSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加设备同步组件
 */
@Component("deleteMachineTranslate")
public class DeleteMachineTranslateComponent {

@Autowired
private IDeleteMachineTranslateSMO deleteMachineTranslateSMOImpl;

/**
 * 添加设备同步数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteMachineTranslateSMOImpl.deleteMachineTranslate(pd);
    }

public IDeleteMachineTranslateSMO getDeleteMachineTranslateSMOImpl() {
        return deleteMachineTranslateSMOImpl;
    }

public void setDeleteMachineTranslateSMOImpl(IDeleteMachineTranslateSMO deleteMachineTranslateSMOImpl) {
        this.deleteMachineTranslateSMOImpl = deleteMachineTranslateSMOImpl;
    }
            }
