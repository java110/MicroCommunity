package com.java110.common.bmo.smsConfig;
import com.java110.po.smsConfig.SmsConfigPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateSmsConfigBMO {


    /**
     * 修改短信配置
     * add by wuxw
     * @param smsConfigPo
     * @return
     */
    ResponseEntity<String> update(SmsConfigPo smsConfigPo);


}
