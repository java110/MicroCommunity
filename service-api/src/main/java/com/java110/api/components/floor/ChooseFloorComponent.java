package com.java110.api.components.floor;


import com.java110.core.context.IPageData;
import com.java110.api.smo.IFloorServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 应用组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("chooseFloor")
public class ChooseFloorComponent {

    @Autowired
    private IFloorServiceSMO listFloorsSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listFloorsSMOImpl.listFloor(pd);
    }

    public IFloorServiceSMO getListFloorsSMOImpl() {
        return listFloorsSMOImpl;
    }

    public void setListFloorsSMOImpl(IFloorServiceSMO listFloorsSMOImpl) {
        this.listFloorsSMOImpl = listFloorsSMOImpl;
    }
}
