package com.java110.job.bmo.businessDatabus;
import com.java110.po.businessDatabus.BusinessDatabusPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteBusinessDatabusBMO {


    /**
     * 修改业务数据同步
     * add by wuxw
     * @param businessDatabusPo
     * @return
     */
    ResponseEntity<String> delete(BusinessDatabusPo businessDatabusPo);


}
