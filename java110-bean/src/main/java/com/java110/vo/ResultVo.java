package com.java110.vo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;

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

    public static final int CODE_OK = 0; // 成功

    public static final int CODE_MACHINE_OK = 0; // 成功

    public static final int CODE_MACHINE_ERROR = -1; // 未知异常

    public static final int CODE_UNAUTHORIZED = 401; //认证失败
    public static final int CODE_WECHAT_UNAUTHORIZED = 1401; //认证失败
    public static final int CODE_BUSINESS_VERIFICATION = 5010; //业务校验未通过

    public static final int ORDER_ERROR = 500; //订单调度异常


    public static final String MSG_ERROR = "未知异常";// 未知异常

    public static final String MSG_OK = "成功"; // 成功

    public static final String MSG_UNAUTHORIZED = "认证失败"; //认证失败

    public static final int DEFAULT_RECORD = 1;
    public static final int DEFAULT_TOTAL = 1;

    public static final int CODE_WAIT_PAY = 41;// 支付未完成

    public static final String EMPTY_ARRAY = "[]";

    // 分页页数
    private int page;
    // 行数
    private int rows;

    //页数
    private int records;

    // 总记录数
    private long total;

    //状态
    private int code;

    //错误提示
    private String msg;

    //数据对象
    private Object data;

    //用来存放大计、小计金额
    private Object sumTotal;

    //所需数据
    private Object rep;

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

    public ResultVo(int records, long total, Object data) {
        this.code = CODE_OK;
        this.msg = MSG_OK;
        this.records = records;
        this.total = total;
        this.data = data;
    }

    public ResultVo(int records, int total, Object data, Object sumTotal) {
        this.code = CODE_OK;
        this.msg = MSG_OK;
        this.records = records;
        this.total = total;
        this.data = data;
        this.sumTotal = sumTotal;
    }

    public ResultVo(int records, int total, Object data, Object sumTotal, Object rep) {
        this.code = CODE_OK;
        this.msg = MSG_OK;
        this.records = records;
        this.total = total;
        this.data = data;
        this.sumTotal = sumTotal;
        this.rep = rep;
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

    public long getTotal() {
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

    public Object getSumTotal() {
        return sumTotal;
    }

    public void setSumTotal(Object sumTotal) {
        this.sumTotal = sumTotal;
    }

    public Object getRep() {
        return rep;
    }

    public void setRep(Object rep) {
        this.rep = rep;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
    }


    /**
     * 创建ResponseEntity对象
     *
     * @param data 数据对象
     * @return
     */
    public static ResponseEntity<String> createResponseEntity(Object data) {
        ResultVo resultVo = new ResultVo(DEFAULT_RECORD, DEFAULT_TOTAL, data);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }

    /**
     * 创建ResponseEntity对象
     *
     * @param resultVo 数据对象
     * @return
     */
    public static ResponseEntity<String> createResponseEntity(ResultVo resultVo) {
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }

    /**
     * 成功通用回复
     *
     * @return
     */
    public static ResponseEntity<String> success() {
        ResultVo resultVo = new ResultVo(CODE_OK, MSG_OK);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }

    /**
     * 成功通用回复
     *
     * @return
     */
    public static ResponseEntity<String> error(String msg,HttpStatus status) {
        ResultVo resultVo = new ResultVo(CODE_ERROR, msg);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), status);
        return responseEntity;
    }

    /**
     * 成功通用回复
     *
     * @return
     */
    public static ResponseEntity<String> error(String msg) {
        ResultVo resultVo = new ResultVo(CODE_ERROR, msg);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }

    /**
     * 成功通用回复
     *
     * @return
     */
    public static ResponseEntity<String> error(String msg,Object data) {
        ResultVo resultVo = new ResultVo(CODE_ERROR, msg,data);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
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
     * 创建ResponseEntity对象
     *
     * @param records
     * @param total
     * @param data
     * @param sumTotal
     * @return
     */
    public static ResponseEntity<String> createResponseEntity(int records, int total, Object data, Object sumTotal) {
        ResultVo resultVo = new ResultVo(records, total, data, sumTotal);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }

    /**
     * 页面跳转
     *
     * @param url
     * @return
     */
    public static ResponseEntity<String> redirectPage(String url) {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, url);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", headers, HttpStatus.FOUND);
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
     * @param code 状态嘛
     * @param msg  返回信息
     * @return
     */
    public static ResponseEntity<String> createResponseEntity(int code, String msg) {
        ResultVo resultVo = new ResultVo(code, msg);
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
