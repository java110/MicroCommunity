package com.java110.vo;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * @ClassName ResultVo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/28 18:41
 * @Version 1.0
 * add by wuxw 2020/5/28
 **/
public class ResultVo implements Serializable {

    public static final int CODE_ERROR = 404;// 未知异常

    public static final int CODE_OK = 200; // 成功

    public static final int CODE_MACHINE_OK = 0; // 成功

    public static final int CODE_MACHINE_ERROR = -1; // 未知异常

    public static final int CODE_UNAUTHORIZED = 401; //认证失败
    public static final int CODE_WECHAT_UNAUTHORIZED = 1401; //认证失败

    public static final int ORDER_ERROR = 500; //订单调度异常


    public static final String MSG_ERROR = "未知异常";// 未知异常

    public static final String MSG_OK = "成功"; // 成功

    public static final String MSG_UNAUTHORIZED = "认证失败"; //认证失败

    // 分页页数
    private int page;
    // 行数
    private int rows;

    //页数
    private int records;

    // 总记录数
    private int total;

    //状态嘛
    private int code;

    //错误提示
    private String msg;

    //数据对象
    private Object data;

    public ResultVo() {
    }

    public ResultVo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVo(Object data) {
        this.code = CODE_OK;
        this.msg = MSG_OK;
        this.data = data;
    }

    public ResultVo(int records, int total, Object data) {
        this.code = CODE_OK;
        this.msg = MSG_OK;
        this.records = records;
        this.total = total;
        this.data = data;
    }

    public ResultVo(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultVo(int records, int total, int code, String msg, Object data) {
        this.records = records;
        this.total = total;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    /**
     * 创建ResponseEntity对象
     *
     * @param records 页数
     * @param total   总记录数
     * @param data    数据对象
     * @return
     */
    public static ResponseEntity<String> createResponseEntity(int records, int total, Object data) {
        ResultVo resultVo = new ResultVo(records, total, data);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }

    /**
     * 页面跳转
     * @param url
     * @return
     */
    public static ResponseEntity<String> redirectPage(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, url);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("123123", headers, HttpStatus.BAD_REQUEST);
        return responseEntity;
    }

    /**
     * 创建ResponseEntity对象
     *
     * @param code 状态嘛
     * @param msg  返回信息
     * @param data 数据对象
     * @return
     */
    public static ResponseEntity<String> createResponseEntity(int code, String msg, Object data) {
        ResultVo resultVo = new ResultVo(code, msg, data);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }

    /**
     * 创建ResponseEntity对象
     *
     * @param records 页数
     * @param total   总记录数
     * @param code    状态嘛
     * @param msg     返回信息
     * @param data    数据对象
     * @return
     */
    public static ResponseEntity<String> createResponseEntity(int records, int total, int code, String msg, Object data) {
        ResultVo resultVo = new ResultVo(records, total, code, msg, data);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }
}
