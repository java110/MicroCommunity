package com.java110.api.components.unit;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IUnitServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName AddUnitComponent
 * @Description TODO 添加小区单元
 * @Author wuxw
 * @Date 2019/5/3 11:42
 * @Version 1.0
 * add by wuxw 2019/5/3
 **/
@Component("addUnit")
public class AddUnitComponent {

    @Autowired
    private IUnitServiceSMO unitServiceSMOImpl;

    /**
     * 保存 小区单元信息
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd) {
        return unitServiceSMOImpl.saveUnit(pd);
    }

    public IUnitServiceSMO getUnitServiceSMOImpl() {
        return unitServiceSMOImpl;
    }

    public void setUnitServiceSMOImpl(IUnitServiceSMO unitServiceSMOImpl) {
        this.unitServiceSMOImpl = unitServiceSMOImpl;
    }
}
