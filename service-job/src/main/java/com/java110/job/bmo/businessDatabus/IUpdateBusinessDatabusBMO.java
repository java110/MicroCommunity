package com.java110.job.bmo.businessDatabus;

import com.java110.po.business.BusinessDatabusPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateBusinessDatabusBMO {


    /**
     * 修改业务数据同步
     * add by wuxw
     *
     * @param businessDatabusPo
     * @return
     */
    ResponseEntity<String> update(BusinessDatabusPo businessDatabusPo);


}
