package com.java110.vo.api;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ApiRoomVo
 * @Description TODO
 * @Author wuxw
 * @Date 2019/5/8 1:00
 * @Version 1.0
 * add by wuxw 2019/5/8
 **/
public class ApiRoomVo extends MorePageVo implements Serializable {

    private List<ApiRoomDataVo> rooms;


    public List<ApiRoomDataVo> getRooms() {
        return rooms;
    }

    public void setRooms(List<ApiRoomDataVo> rooms) {
        this.rooms = rooms;
    }
}
