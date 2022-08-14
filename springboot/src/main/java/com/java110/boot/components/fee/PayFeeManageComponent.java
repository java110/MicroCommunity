package com.java110.boot.components.fee;


import com.java110.boot.smo.fee.IListPayFeeSMO;
import com.java110.core.context.IPageData;
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
@Component("payFeeManage")
public class PayFeeManageComponent {

    @Autowired
    private IListPayFeeSMO listPayFeeSMOImpl;



    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listPayFeeSMOImpl.list(pd);
    }

    public IListPayFeeSMO getListPayFeeSMOImpl() {
        return listPayFeeSMOImpl;
    }

    public void setListPayFeeSMOImpl(IListPayFeeSMO listPayFeeSMOImpl) {
        this.listPayFeeSMOImpl = listPayFeeSMOImpl;
    }
}
