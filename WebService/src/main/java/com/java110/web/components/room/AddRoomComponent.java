package com.java110.web.components.room;

import com.java110.core.context.IPageData;
import com.java110.web.smo.IUnitServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName AddRoomComponent
 * @Description TODO 添加房屋
 * @Author wuxw
 * @Date 2019/5/6 20:23
 * @Version 1.0
 * add by wuxw 2019/5/6
 **/
@Component("addRoom")
public class AddRoomComponent {

    @Autowired
    private IUnitServiceSMO unitServiceSMOImpl;

    /**
     * 根据 floorId 查询单元信息
     *
     * @param pd 包含floorId 和小区ID 页面封装对象
     * @return 单元信息
     */
    public ResponseEntity<String> loadUnits(IPageData pd) {
        return unitServiceSMOImpl.listUnits(pd);
    }


    public IUnitServiceSMO getUnitServiceSMOImpl() {
        return unitServiceSMOImpl;
    }

    public void setUnitServiceSMOImpl(IUnitServiceSMO unitServiceSMOImpl) {
        this.unitServiceSMOImpl = unitServiceSMOImpl;
    }
}
