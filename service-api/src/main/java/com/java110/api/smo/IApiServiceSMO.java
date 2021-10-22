package com.java110.api.smo;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * 中心服务 SMO 业务逻辑接口
 * Created by wuxw on 2018/4/13.
 */
public interface IApiServiceSMO {

    /**
     * 业务统一处理服务方法
     * @param reqJson 请求报文json
     * @return
     */
     ResponseEntity<String> service(String reqJson, Map<String, String> headers) throws SMOException;
}
