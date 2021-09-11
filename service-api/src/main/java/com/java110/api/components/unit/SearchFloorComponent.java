package com.java110.api.components.unit;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IFloorServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName searchFloor
 * @Description TODO
 * @Author wuxw
 * @Date 2019/4/30 11:16
 * @Version 1.0
 * add by wuxw 2019/4/30
 **/

@Component("searchFloor")
public class SearchFloorComponent {

    @Autowired
    private IFloorServiceSMO floorServiceSMOImpl;

    /**
     * 查询小区楼信息
     *
     * @param pd
     * @return 返回小区楼信息
     */
    public ResponseEntity<String> listFloor(IPageData pd) {
        return floorServiceSMOImpl.listFloor(pd);
    }


    public IFloorServiceSMO getFloorServiceSMOImpl() {
        return floorServiceSMOImpl;
    }

    public void setFloorServiceSMOImpl(IFloorServiceSMO floorServiceSMOImpl) {
        this.floorServiceSMOImpl = floorServiceSMOImpl;
    }
}
