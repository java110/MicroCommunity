package com.java110.common.bmo.smsConfig;
import com.java110.dto.smsConfig.SmsConfigDto;
import org.springframework.http.ResponseEntity;
public interface IGetSmsConfigBMO {


    /**
     * 查询短信配置
     * add by wuxw
     * @param  smsConfigDto
     * @return
     */
    ResponseEntity<String> get(SmsConfigDto smsConfigDto);


}
