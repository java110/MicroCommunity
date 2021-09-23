package com.java110.api.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 剩余车位
 */
public interface IRemainingParkingSpaceServiceSMO {

    /**
     * 剩余车位
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> list(IPageData pd);

}
