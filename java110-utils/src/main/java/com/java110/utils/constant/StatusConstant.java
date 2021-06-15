package com.java110.utils.constant;

/**
 * 封装订单
 * Created by wuxw on 2017/4/24.
 */
public class StatusConstant {


    /**
     * 保存数据成功
     */
    public final static String STATUS_CD_SAVE = "S";//保存成功


    public final static String STATUS_CD_DELETE = "D";//作废订单
    public final static String STATUS_CD_ERROR = "E";//错误订单
    public final static String STATUS_CD_NOTIFY_ERROR = "NE";//通知错误订单
    public final static String STATUS_CD_COMPLETE = "C";//错误订单
    public final static String STATUS_CD_BUSINESS = "B";//BUSINESS过程
    public final static String STATUS_CD_BUSINESS_COMPLETE = "BC";//BUSINESS过程 完成
    public final static String STATUS_CD_INSTANCE = "I";//INSTANCE过程
    public final static String STATUS_CD_DELETE_ORDER = "DO";// 撤单


    public final static String OPERATE_ADD = "ADD";
    public final static String OPERATE_MOD = "MOD";
    public final static String OPERATE_DEL = "DEl";



    /**
     * 有效的，在用的
     */
    public final static String STATUS_CD_VALID = "0";

    /**
     * 无效的，不在用的
     */
    public final static String STATUS_CD_INVALID = "1";


    public final static String REQUEST_BUSINESS_TYPE = "Q";
    public final static String REQUEST_BUSINESS_TYPE_BUSINESS = "B";
    public final static String REQUEST_BUSINESS_TYPE_INSTANCE = "I";
    public final static String REQUEST_BUSINESS_TYPE_DELETE = "DO";
    public final static String RESPONSE_BUSINESS_TYPE = "B";
    public final static String NOTIFY_BUSINESS_TYPE = "N";


}
