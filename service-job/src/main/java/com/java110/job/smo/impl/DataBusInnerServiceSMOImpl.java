package com.java110.job.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.entity.order.Business;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 定时任务属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class DataBusInnerServiceSMOImpl extends BaseServiceSMO implements IDataBusInnerServiceSMO {

    @Override
    public boolean exchange(@RequestBody List<Business> businesses) {
        return false;
    }
}
