package com.java110.order.smo;

import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * 中心服务 SMO 业务逻辑接口
 * Created by wuxw on 2018/4/13.
 */
public interface ICenterServiceSMO {

    /**
     * 业务统一处理服务方法
     * @param reqJson 请求报文json
     * @return
     */
    @Deprecated
    public String service(String reqJson, Map<String,String> headers) throws SMOException;

    /**
     * 透传业务
     * @param reqJson
     * @param headers
     * @return
     * @throws SMOException
     */
    public String serviceTransfer(String reqJson, Map<String,String> headers) throws SMOException;

    /**
     * 业务统一处理服务方法
     * @param reqJson 请求报文json
     * @return
     */
    public ResponseEntity<String> serviceApi(String reqJson, Map<String,String> headers) throws SMOException;

    /**
     * 接受业务系统通知消息
     * @param receiveJson 接受报文
     * @throws SMOException
     */
    public void receiveBusinessSystemNotifyMessage(String receiveJson) throws SMOException;
}
