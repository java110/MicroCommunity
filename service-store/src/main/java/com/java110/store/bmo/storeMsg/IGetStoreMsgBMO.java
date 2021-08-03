package com.java110.store.bmo.storeMsg;
import com.java110.dto.storeMsg.StoreMsgDto;
import org.springframework.http.ResponseEntity;
public interface IGetStoreMsgBMO {


    /**
     * 查询商户消息
     * add by wuxw
     * @param  storeMsgDto
     * @return
     */
    ResponseEntity<String> get(StoreMsgDto storeMsgDto);


}
