package com.java110.utils.constant;

/**
 * Created by wuxw on 2017/7/23.
 */
public class RuleDomain {


    //RULE_COND_RETURN
    public static final String RULE_COND_RETURN_0000 = "0000";//成功
    public static final String RULE_COND_RETURN_0001 = "0001";//继续调存过校验
    public static final String RULE_COND_RETURN_0002 = "0002";//存过校验失败
    public static final String RULE_COND_RETURN_0003 = "0003";//国漫预存款校验-免预存
    public static final String RULE_COND_RETURN_0004 = "0004";//国漫预存款校验-不免预存
    public static final String RULE_COND_RETURN_0005 = "0005";//JAVA校验失败
    public static final String RULE_COND_RETURN_0006 = "0006";//特定员工不需要校验
    public static final String RULE_COND_RETURN_1999 = "1999";//程序异常


    /**
     * 规则编码默认值
     */
    public static final String RULE_SERVICE_CODE_DEFAULT = "SVC0000";
    /**
     * 规则类型
     */
    public static final String RULE_TYPE_DEFAULT = "RULE0000";


    /*******************************************规则实现方式***************************************/

    public static final String RULE_TYPE_JAVA_METHOD = "1";//1 反射调用java 方法实现，

    public static final String RULE_TYPE_COND_CFG = "2";//2 通过 rule_cond_cfg 配置逻辑实现

    public static final String RULE_TYPE_STORED_PROCEDURE = "3" ;//3 调用存储过程实现，存储过程入参可以在rule_cond_cfg 表中配置




    //分隔符
    public final static String RULE_SIGN_1 = "@@";
    public final static String RULE_SIGN_2 = "$-";
    public final static String RULE_SIGN_3 = "'";
    public final static String RULE_SIGN_4 = ",";
    public final static String RULE_SIGN_5 = "#";
    public final static String RULE_SIGN_6 = ">";
    public final static String RULE_SIGN_7 = "@";
    public final static String RULE_SIGN_8 = ".";
    public final static String RULE_SIGN_9 = ";";

    public final static String RULE_IS_Y = "Y";
    public final static String RULE_IS_N = "N";



    public static final String PART_STRING_ORIGINAL_VALUE = "ORIGINAL_VALUE"; //表示字符串：ORIGINAL_VALUE，含义是在处理字符串时表示没有被任何改变的“原始值”标识


    /**
     * redis key 开始设置
     */
    public final static String REDIS_KEY_RULE_ENTRANCE ="RuleEntrance"; // redis key RuleEntrance


    public final static String REDIS_KEY_RULE ="Rule"; // redis key Rule


    public final static String REDIS_KEY_RULE_GROUP ="Rule_Group"; // redis key Rule


    public final static String REPLAY_TYPE_A = "########";				//'yyyyMMdd'
    public final static String REPLAY_TYPE_E = "##########";			//'yyyyMMddHH'
    public final static String REPLAY_TYPE_F = "############";			//'yyyyMMddHHmm'
    public final static String REPLAY_TYPE_SQL = "select";           //文件名支持sql查询


}
