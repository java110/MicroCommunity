package com.java110.api.components.ownerRepair;

import com.java110.core.context.IPageData;
import com.java110.api.smo.ownerRepair.ICloseRepairDispatchSMO;
import com.java110.api.smo.ownerRepair.IListOwnerRepairsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName MyRepairDispatchComponent
 * @Description TODO
 * @Author wuxw
 * @Date 2019/10/20 18:21
 * @Version 1.0
 * add by wuxw 2019/10/20
 **/
@Component("myRepairDispatch")
public class MyRepairDispatchComponent {


    @Autowired
    private IListOwnerRepairsSMO listOwnerRepairsSMOImpl;

    @Autowired
    private ICloseRepairDispatchSMO closeRepairDispatchSMOImpl;

    /**
     * 查询业主报修列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return listOwnerRepairsSMOImpl.listOwnerRepairs(pd);
    }

    /**
     * 结单处理
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> closeOrder(IPageData pd){
        return closeRepairDispatchSMOImpl.close(pd);
    }


    public IListOwnerRepairsSMO getListOwnerRepairsSMOImpl() {
        return listOwnerRepairsSMOImpl;
    }

    public void setListOwnerRepairsSMOImpl(IListOwnerRepairsSMO listOwnerRepairsSMOImpl) {
        this.listOwnerRepairsSMOImpl = listOwnerRepairsSMOImpl;
    }


    public ICloseRepairDispatchSMO getCloseRepairDispatchSMOImpl() {
        return closeRepairDispatchSMOImpl;
    }

    public void setCloseRepairDispatchSMOImpl(ICloseRepairDispatchSMO closeRepairDispatchSMOImpl) {
        this.closeRepairDispatchSMOImpl = closeRepairDispatchSMOImpl;
    }
}
