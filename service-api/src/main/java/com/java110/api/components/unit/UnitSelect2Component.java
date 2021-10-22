package com.java110.api.components.unit;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IUnitServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName UnitComponent
 * @Description TODO 小区单元组件java处理类
 * @Author wuxw
 * @Date 2019/5/2 19:25
 * @Version 1.0
 * add by wuxw 2019/5/2
 **/
@Component("unitSelect2")
public class UnitSelect2Component {


    @Autowired
    private IUnitServiceSMO unitServiceSMOImpl;

    /**
     * 加载小区单元
     *
     * @param pd 页面数据封装类
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> loadUnits(IPageData pd) {

        return unitServiceSMOImpl.listUnits(pd);

    }

    public IUnitServiceSMO getUnitServiceSMOImpl() {
        return unitServiceSMOImpl;
    }

    public void setUnitServiceSMOImpl(IUnitServiceSMO unitServiceSMOImpl) {
        this.unitServiceSMOImpl = unitServiceSMOImpl;
    }
}
