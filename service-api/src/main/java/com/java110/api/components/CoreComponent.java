package com.java110.api.components;

import com.java110.core.context.IPageData;
import com.java110.api.smo.dict.IDictServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName CoreComponent
 * @Description TODO
 * @Author wuxw
 * @Date 2020/1/30 17:23
 * @Version 1.0
 * add by wuxw 2020/1/30
 **/
@Component("core")
public class CoreComponent {
    @Autowired
    private IDictServiceSMO dictServiceSMOImpl;

    /**
     * 查询活动列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return dictServiceSMOImpl.listDict(pd);
    }

    public IDictServiceSMO getDictServiceSMOImpl() {
        return dictServiceSMOImpl;
    }

    public void setDictServiceSMOImpl(IDictServiceSMO dictServiceSMOImpl) {
        this.dictServiceSMOImpl = dictServiceSMOImpl;
    }
}
