package com.java110.api.components.fee;


import com.java110.api.smo.fee.IListFeeSummarySMO;
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
@Component("feeSummary")
public class FeeSummaryComponent {

    @Autowired
    private IListFeeSummarySMO listFeeSummarySMOImpl;


    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listFeeSummarySMOImpl.list(pd);
    }

    public IListFeeSummarySMO getListFeeSummarySMOImpl() {
        return listFeeSummarySMOImpl;
    }

    public void setListFeeSummarySMOImpl(IListFeeSummarySMO listFeeSummarySMOImpl) {
        this.listFeeSummarySMOImpl = listFeeSummarySMOImpl;
    }
}
