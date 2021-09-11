package com.java110.api.components.room;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.api.smo.IRoomServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName RoomComponent
 * @Description TODO 选择房屋组件
 * @Author wuxw
 * @Date 2019/5/7 23:40
 * @Version 1.0
 * add by wuxw 2019/5/7
 **/
@Component("searchRoom")
public class SearchRoomComponent {


    @Autowired
    private IRoomServiceSMO roomServiceSMOImpl;

    /**
     * 显示房屋信息
     *
     * @param pd 页面信息封装
     * @return ResponseEntity对象
     */
    public ResponseEntity<String> listRoom(IPageData pd) {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        if (paramIn.containsKey("roomFlag") && "1".equals(paramIn.getString("roomFlag"))) {
            return roomServiceSMOImpl.listRoomWithSell(pd);
        } else if (paramIn.containsKey("roomFlag") && "2".equals(paramIn.getString("roomFlag"))) {
            return roomServiceSMOImpl.listRoomWithOutSell(pd);
        } else {
            return roomServiceSMOImpl.listRoom(pd);
        }
    }


    public IRoomServiceSMO getRoomServiceSMOImpl() {
        return roomServiceSMOImpl;
    }

    public void setRoomServiceSMOImpl(IRoomServiceSMO roomServiceSMOImpl) {
        this.roomServiceSMOImpl = roomServiceSMOImpl;
    }
}
