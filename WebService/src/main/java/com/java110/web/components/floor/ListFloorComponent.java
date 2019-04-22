package com.java110.web.components.floor;

import com.java110.core.context.IPageData;
import com.java110.web.smo.IFloorServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 查询楼列表组件
 */
@Component("listFloor")
public class ListFloorComponent {

    @Autowired
    private IFloorServiceSMO floorServiceSMOImpl;

    public ResponseEntity<String> list(IPageData pd){
        return floorServiceSMOImpl.listFloor(pd);
    }


    public IFloorServiceSMO getFloorServiceSMOImpl() {
        return floorServiceSMOImpl;
    }

    public void setFloorServiceSMOImpl(IFloorServiceSMO floorServiceSMOImpl) {
        this.floorServiceSMOImpl = floorServiceSMOImpl;
    }
}
