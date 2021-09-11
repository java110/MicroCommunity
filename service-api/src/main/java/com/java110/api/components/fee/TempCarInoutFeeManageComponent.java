package com.java110.api.components.fee;


import com.java110.core.context.IPageData;
import com.java110.api.smo.fee.IFeeTypeSMO;
import com.java110.api.smo.fee.IListTempCarInoutFeeSMO;
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
@Component("tempCarInoutFeeManage")
public class TempCarInoutFeeManageComponent {

    @Autowired
    private IListTempCarInoutFeeSMO listTempCarInoutFeeSMOImpl;

    @Autowired
    private IFeeTypeSMO feeTypeSMOImpl;

    /**
     * 查询费用类型
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> listFeeType(IPageData pd) {
        return feeTypeSMOImpl.list(pd);
    }

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listTempCarInoutFeeSMOImpl.list(pd);
    }


}
