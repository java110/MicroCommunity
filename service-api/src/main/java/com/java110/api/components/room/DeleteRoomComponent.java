package com.java110.api.components.room;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IRoomServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName DeleteRoomComponent
 * @Description TODO
 * @Author wuxw
 * @Date 2019/5/8 18:22
 * @Version 1.0
 * add by wuxw 2019/5/8
 **/
@Component("deleteRoom")
public class DeleteRoomComponent {

    @Autowired
    private IRoomServiceSMO roomServiceSMOImpl;

    /**
     * 删除房屋信息
     *
     * @param pd 页面数据信息
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return roomServiceSMOImpl.deleteRoom(pd);
    }
}
