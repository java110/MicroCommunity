package com.java110.api.smo.msg;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 查询 消息
 */
public interface IListMsgSMO {

    /**
     * 查询消息
     * @param pd 上下文对象
     * @return
     */
    ResponseEntity<String> listMsg(IPageData pd);
}
