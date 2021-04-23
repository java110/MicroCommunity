package com.java110.job.bmo.businessDatabus;

import com.java110.po.businessDatabus.BusinessDatabusPo;
import org.springframework.http.ResponseEntity;

public interface ISaveBusinessDatabusBMO {


    /**
     * 添加业务数据同步
     * add by wuxw
     *
     * @param businessDatabusPo
     * @return
     */
    ResponseEntity<String> save(BusinessDatabusPo businessDatabusPo);


}
