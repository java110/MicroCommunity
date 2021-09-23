package com.java110.api.components.fee;


import com.java110.core.context.IPageData;
import com.java110.api.smo.fee.IListFeeConfigsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 费用项组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("feeConfigManage")
public class FeeConfigManageComponent {

    @Autowired
    private IListFeeConfigsSMO listFeeConfigsSMOImpl;

    /**
     * 查询费用项列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listFeeConfigsSMOImpl.listFeeConfigs(pd);
    }

    public IListFeeConfigsSMO getListFeeConfigsSMOImpl() {
        return listFeeConfigsSMOImpl;
    }

    public void setListFeeConfigsSMOImpl(IListFeeConfigsSMO listFeeConfigsSMOImpl) {
        this.listFeeConfigsSMOImpl = listFeeConfigsSMOImpl;
    }
}
