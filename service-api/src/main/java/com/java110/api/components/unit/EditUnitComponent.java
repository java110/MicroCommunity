package com.java110.api.components.unit;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IUnitServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName EditUnitComponent
 * @Description TODO 编辑小区楼单元信息
 * @Author wuxw
 * @Date 2019/5/3 18:12
 * @Version 1.0
 * add by wuxw 2019/5/3
 **/
@Component("editUnit")
public class EditUnitComponent {

    @Autowired
    private IUnitServiceSMO unitServiceSMOImpl;

    /**
     * 修改小区单元信息
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd) {
        return unitServiceSMOImpl.updateUnit(pd);
    }

    public IUnitServiceSMO getUnitServiceSMOImpl() {
        return unitServiceSMOImpl;
    }

    public void setUnitServiceSMOImpl(IUnitServiceSMO unitServiceSMOImpl) {
        this.unitServiceSMOImpl = unitServiceSMOImpl;
    }
}
