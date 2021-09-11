package com.java110.api.components.carInout;


import com.java110.core.context.IPageData;
import com.java110.api.smo.carBlackWhite.IListCarInoutsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 车辆进场组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("carInManage")
public class CarInManageComponent {

    @Autowired
    private IListCarInoutsSMO listCarInoutsSMOImpl;

    /**
     * 查询车辆进场列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return listCarInoutsSMOImpl.listCarInouts(pd);
    }

    public IListCarInoutsSMO getListCarInoutsSMOImpl() {
        return listCarInoutsSMOImpl;
    }

    public void setListCarInoutsSMOImpl(IListCarInoutsSMO listCarInoutsSMOImpl) {
        this.listCarInoutsSMOImpl = listCarInoutsSMOImpl;
    }
}
