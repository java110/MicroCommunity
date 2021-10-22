package com.java110.api.components.fee;


import com.java110.core.context.IPageData;
import com.java110.api.smo.fee.IDeleteFeeSMO;
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
@Component("deleteFee")
public class DeleteFeeComponent {

    @Autowired
    private IDeleteFeeSMO deleteFeeSMOImpl;

    /**
     * 查询费用项列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deleteFeeSMOImpl.deleteFee(pd);
    }


    public IDeleteFeeSMO getDeleteFeeSMOImpl() {
        return deleteFeeSMOImpl;
    }

    public void setDeleteFeeSMOImpl(IDeleteFeeSMO deleteFeeSMOImpl) {
        this.deleteFeeSMOImpl = deleteFeeSMOImpl;
    }
}
