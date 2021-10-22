package com.java110.api.components.room;

import com.java110.core.context.IPageData;
import com.java110.api.smo.addRoomBinding.IAddRoomBindingBindingSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加添加房屋组件
 */
@Component("addRoomBindingBinding")
public class AddRoomBindingBindingComponent {

    @Autowired
    private IAddRoomBindingBindingSMO addRoomBindingBindingSMOImpl;

    /**
     * 添加添加房屋数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> binding(IPageData pd){
        return addRoomBindingBindingSMOImpl.bindingAddRoomBinding(pd);
    }

    public IAddRoomBindingBindingSMO getAddRoomBindingBindingSMOImpl() {
        return addRoomBindingBindingSMOImpl;
    }

    public void setAddRoomBindingBindingSMOImpl(IAddRoomBindingBindingSMO addRoomBindingBindingSMOImpl) {
        this.addRoomBindingBindingSMOImpl = addRoomBindingBindingSMOImpl;
    }
}
