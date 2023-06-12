package com.java110.service.smo;

import com.java110.service.context.DataQuery;
import com.java110.utils.exception.BusinessException;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * 公用查询处理
 * Created by wuxw on 2018/4/19.
 */
public interface ICmdServiceSMO {

    /**
     * cmd 调用 处理类
     * @param reqJson 请求报文json
     * @return
     */
    ResponseEntity<String> cmd(String reqJson, Map<String, String> headers) throws Exception;

}
