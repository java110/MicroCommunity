package com.java110.utils.util;

/**
 * 接口交互工具类
 * <p>
 * 负责接口之间的交互，包括
 * 请求协议创建 返回协议创建
 * 请求协议：
 * {"tcpCont":{"transactionID":"1000000200201702133115853347","reqTime":"20170213082832","channelId":"151745673",
 * "busCode":"BUS80001","sign":"123456","type":"REQUEST"},"svcCont":{"user":{......},"account":{.....},"prod":{.....}}}
 * <p>
 * <p>
 * 返回协议：
 * {"tcpCont":{"transactionId":"1000000200201702133115853347","resTime":"20170213082832","channelId":"151745673",
 * "busCode":"BUS70001","sign":"123456","type":"RESPONSE","resultCode":"0000","resultMsg":"成功"},"SvcCont":{"users":[{......}],"accounts":[{......}],"prods":[{......}]}}
 * tcpCont 表示头部信息描述
 * transactionId 交易流水为18位，102017021300000001,10（系统标识，内部调用默认全部使用10，外部系统，需要管理员分配）+八位日期+八位序列
 * reqTime 交易请求时间 例如20170101010101 年月日时分秒
 * channelId 每个系统分配渠道ID
 * busCode 服务编码，BUS80001 查询单， BUS80002 校验单，BUS80003 正式单，BUS70001 查询单返回， BUS70002 校验单返回，BUS70003 正式单返回
 * sign 组件密码，每个服务可能不一样，需要和专业人员联系
 * type 报文类型 REQUEST 请求报文， RESPONSE 返回报文
 * resultCode 返回报文时的编码 成功 0000 失败 1999（通用失败，每个业务失败情况可能不一致）
 * SvcCont 业务信息描述
 * users 用户信息描述
 * accounts 用户信息描述
 * prods 产品信息描述
 * Created by wuxw on 2017/2/13.
 */
public class TransferUtil {


    /**
     * 创建头部信息描述
     *
     * @param transactionId 交易流水为18位，102017021300000001,10（系统标识，内部调用默认全部使用10，外部系统，需要管理员分配）+八位日期+八位序列
     * @param resTime       交易请求时间 例如20170101010101 年月日时分秒
     * @param channelId     每个系统分配渠道ID
     * @param busCode       服务编码，BUS80001 查询单， BUS80002 校验单，BUS80003 正式单，BUS70001 查询单返回， BUS70002 校验单返回，BUS70003 正式单返回
     * @param sign          组件密码，每个服务可能不一样，需要和专业人员联系
     * @return 头部信息描述
     */
    public static String createRequestTcpCont(String transactionId, String resTime, String channelId,
                                              String busCode, String sign) {
        String type = "REQUEST"; //报文类型 REQUEST 请求报文， RESPONSE 返回报文
        return "";
    }

    public static String createResponseTcpCont(String transactionId, String resTime, String channelId,
                                               String busCode, String sign, String resultCode, String resultMsg) {
        return "";
    }
}
