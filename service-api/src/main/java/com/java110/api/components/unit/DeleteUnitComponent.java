package com.java110.api.components.unit;


import com.java110.core.context.IPageData;
import com.java110.api.smo.IUnitServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 删除小区单元组件
 */
@Component("deleteUnit")
public class DeleteUnitComponent {

    @Autowired
    private IUnitServiceSMO unitServiceSMOImpl;


    /**
     * 删除小区单元信息
     *
     * @param pd 页面数据封装
     * @return ResponseEntity对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return unitServiceSMOImpl.deleteUnit(pd);
    }

    public IUnitServiceSMO getUnitServiceSMOImpl() {
        return unitServiceSMOImpl;
    }

    public void setUnitServiceSMOImpl(IUnitServiceSMO unitServiceSMOImpl) {
        this.unitServiceSMOImpl = unitServiceSMOImpl;
    }
}
