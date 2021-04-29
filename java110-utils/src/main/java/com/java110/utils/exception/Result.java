package com.java110.utils.exception;

import java.io.Serializable;

/**
 * Created by wuxw on 2018/4/14.
 */
public class Result implements Serializable {
    public final static Result SUCCESS = new Result("0000", "成功");
    public final static Result SYS_ERROR = new Result("1999", "失败");

    private String code = "0000";
    private String msg = "成功";

    /**
     * Result构造函数

     * @param code
     * @param msg
     */
    public Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, String msg) {
        this.code = code+"";
        this.msg = msg;
    }


    public Result(Result result) {
        this.code = result.getCode();
        this.msg = result.getMsg();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 只要错误编码相同,就认为两个对象相同

     * @param r
     * @return
     */
    @Override
    public boolean equals(Object r) {
        boolean b = false;
        if (r instanceof Result) {
            if (getCode() == ((Result) r).getCode())
                b = true;
            else
                b = false;
        } else
            b = super.equals(r);
        return b;
    }
    @Override
    public int hashCode(){
        return super.hashCode();
    }
    /**
     * 返回Result对象的toString字符串

     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"code\":\"");
        sb.append(getCode());
        sb.append("\",");
        sb.append("\"msg\":");
        sb.append(getMsg());
        sb.append("\"}");
        return sb.toString();
    }
}
