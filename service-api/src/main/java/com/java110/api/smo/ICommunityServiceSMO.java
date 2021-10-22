package com.java110.api.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 小区服务类
 */
public interface ICommunityServiceSMO {

    /**
     * 我入驻的小区
     * @param pd
     * @return
     */
    public ResponseEntity<String> listMyCommunity(IPageData pd);


    /**
     * 查询未入驻的小区
     * @param pd
     * @return
     */
    public ResponseEntity<String> listNoEnterCommunity(IPageData pd);


    /**
     * 入驻小区
     * @param pd
     * @return
     */
    public ResponseEntity<String> _saveEnterCommunity(IPageData pd);

    /**
     * 退出小区
     * @param pd
     * @return
     */
    public ResponseEntity<String> exitCommunity(IPageData pd);
}
