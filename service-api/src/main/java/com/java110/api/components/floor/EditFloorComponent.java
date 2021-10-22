package com.java110.api.components.floor;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IFloorServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName EditFloorComponent
 * @Description TODO 编辑小区楼信息
 * @Author wuxw
 * @Date 2019/4/28 15:10
 * @Version 1.0
 * add by wuxw 2019/4/28
 **/

@Component("editFloor")
public class EditFloorComponent {
    @Autowired
    private IFloorServiceSMO floorServiceSMOImpl;

    /**
     * 修改小区楼信息
     *
     * @param pd 页面数据封装
     * @return 返回ResponseEntity对象
     */
    public ResponseEntity<String> changeFloor(IPageData pd) {
        return floorServiceSMOImpl.editFloor(pd);
    }


    public IFloorServiceSMO getFloorServiceSMOImpl() {
        return floorServiceSMOImpl;
    }

    public void setFloorServiceSMOImpl(IFloorServiceSMO floorServiceSMOImpl) {
        this.floorServiceSMOImpl = floorServiceSMOImpl;
    }
}
