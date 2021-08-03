package com.java110.store.bmo.storeMsg;

import com.java110.po.storeMsg.StoreMsgPo;
import org.springframework.http.ResponseEntity;
public interface ISaveStoreMsgBMO {


    /**
     * 添加商户消息
     * add by wuxw
     * @param storeMsgPo
     * @return
     */
    ResponseEntity<String> save(StoreMsgPo storeMsgPo);


}
