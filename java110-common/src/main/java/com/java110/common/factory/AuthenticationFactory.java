package com.java110.common.factory;

import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.NoAuthorityException;
import com.java110.entity.center.DataFlow;
import org.apache.commons.codec.digest.DigestUtils;

import javax.naming.AuthenticationException;
import java.io.UnsupportedEncodingException;

/**
 *
 * 鉴权工厂类
 * Created by wuxw on 2018/4/23.
 */
public class AuthenticationFactory {

    /**
     * md5签名
     * @param inStr
     * @return
     */
    public static String md5(String inStr) throws NoAuthorityException{
        try {
            return DigestUtils.md5Hex(inStr.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"MD5签名过程中出现错误");
        }
    }

    /**
     * dataFlow 对象签名
     * @param dataFlow
     * @return
     */
    public static String dataFlowMd5(DataFlow dataFlow) throws NoAuthorityException{
        if(dataFlow == null){
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"MD5签名过程中出现错误");
        }
        String reqInfo = dataFlow.getTransactionId() + dataFlow.getAppId() + dataFlow.getReqBusiness().toJSONString();
        return md5(reqInfo);
    }

    /**
     * md5加密
     * @param transactionId 流水
     * @param appId 应用ID
     * @param businesses 内容
     * @return
     */
    public static String md5(String transactionId,String appId,String businesses){
        return md5(transactionId+appId+businesses);
    }
}


