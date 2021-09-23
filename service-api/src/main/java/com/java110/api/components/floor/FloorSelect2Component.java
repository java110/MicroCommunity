package com.java110.api.components.floor;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IFloorServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 查询楼列表组件
 */
@Component("floorSelect2")
public class FloorSelect2Component {

    @Autowired
    private IFloorServiceSMO floorServiceSMOImpl;

    /**
     * 查询小区楼信息
     *
     * @param pd 页面封装对象 包含页面请求数据
     * @return ResponseEntity对象返回给页面
     */
    public ResponseEntity<String> list(IPageData pd) {

        return floorServiceSMOImpl.listFloor(pd);
    }


    public IFloorServiceSMO getFloorServiceSMOImpl() {
        return floorServiceSMOImpl;
    }

    public void setFloorServiceSMOImpl(IFloorServiceSMO floorServiceSMOImpl) {
        this.floorServiceSMOImpl = floorServiceSMOImpl;
    }
}
