package com.java110.core.context;

/**
 * @author wux
 * @create 2019-02-07 下午7:10
 * @desc 订单返回抽象类
 **/
public class AbstractOrderResponse implements IOrderResponse {

    private String code;

    private String message;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
