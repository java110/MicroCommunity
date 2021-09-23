package com.java110.api.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * Created by Administrator on 2019/4/1.
 */
public interface IMenuServiceSMO {

    /**
     * 根据用户查菜单
     * @return
     */
     ResponseEntity<String> queryMenusByUserId(IPageData pd);

}
