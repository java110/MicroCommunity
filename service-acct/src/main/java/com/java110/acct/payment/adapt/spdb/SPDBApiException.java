package com.java110.acct.payment.adapt.spdb;

/**
 * @author z
 **/
public class SPDBApiException extends Exception {

    public SPDBApiException(int httpStatus, String httpBody) {
        super(String.format("浦发银行API调用失败%n请根据响应报文至API官网排查错误原因：https://open.spdb.com.cn/develop/#/document/2/15%nHTTP状态码：%d%n报文：%s", httpStatus, httpBody));
    }

    public SPDBApiException(String errMsg, int httpStatus, String httpBody) {
        super(String.format("浦发银行API调用失败%n%s%nHTTP状态码：%d%n报文：%s", errMsg, httpStatus, httpBody));
    }
}
