package com.java110.acct.payment.adapt.easypay;

import java.io.Serializable;

/**
 * 259号文终端信息
 */

public class TerminalInfo implements Serializable {
    private String location;//	否	32	受理终端设备实时经纬度信息，格式为：纬度/经度，+表示北纬、东经， -表示南纬、西经。+37.12/-121.213
    private String terminalIp;//	否	64	商户端终端设备 IP 地址。注:如经、维度信息未上送，该字段 必送。
    private String networkLicense;//	否	5	银行卡受理终端产品应用认证编号。该编号由“中国银联标识产品企业资质认 证办公室”为通过入网认证的终端进行分配。
    private String encryptRandNum;//	否	10	仅在被扫支付类交易报文中出现:若付 款码为 19 位数字，则取后 6 位;若付款码码为 EMV 二维码，则取其 tag 57 的卡号/token 号的后 6 位
    private String secretText;//	否	16	仅在被扫支付类交易报文中出现:64bit 的密文数据，对终端硬件序列号和加密 随机因子加密后的结果。本子域取值为:64bit 密文数据进行 base64 编码后的结果。
    private String appVersion;//	否	8	仅在被扫支付类交易报文中出现:64bit 的密文数据，对终端硬件序列号和加密 随机因子加密后的结果。本子域取值为:64bit 密文数据进行 base64 编码后的结果。
    private String mobileCountryCd;//	否	3	基站信息，移动国家代码，由国际电联 (ITU) 统 一 分 配 的 移 动 国 家 代 码 (MCC)。中国为 460
    private String mobileNetNum;//	否	2	基站信息，移动网络号码，由国际电联 01 (ITU) 统 一 分 配 的 移 动 网 络 号 码，30(MNC)。移动:00、02、04、07;联通:01、06 09;电信:03、05、11
    private String iccId;//	否	2	ICCID，SIM 卡卡号
    private String locationCd1;//	否	4	位置区域码 1，LAC(移动、联通)，16进制
    private String lbsNum1;//	否	12	基站编号 1，CID(移动、联通)，16 进 制
    private String lbsSignal1;//	否	4	基站信号 1，SIG(移动、联通)，16 进 制
    private String locationCd2;//	否	4	位置区域码 2，LAC(移动、联通)，16进制
    private String lbsNum2;//	否	12	基站编号 2，CID(移动、联通)，16 进 制
    private String lbsSignal2;//	否	4	基站信号 2，SIG(移动、联通)，16 进 制
    private String locationCd3;//	否	4	位置区域码 3，LAC(移动、联通)，16进制
    private String lbsNum3;//	否	12	基站编号 3，CID(移动、联通)，16 进 制
    private String lbsSignal3;//	否	4	基站信号 3，SIG(移动、联通)，16 进 制
    private String telecomSysId;//	否	4	电信系统识别码，SID(电信)，电信 系统识别码,每个地级市只有一个 SID
    private String telecomNetId;//	否	4	电信网络识别码，NID(电信)，电信 网络识别码,由电信各由地级分公司分 配。每个地级市可能有 1 到 3 个 NID
    private String telecomLbs;//	否	4	电信基站，BID(电信)，电信网络中 的小区识别码，等效于基站
    private String telecomLbsSignal;//	否	4	电信基站信号，SIG(电信)，16 进制
    private String terminalId;//	否	8	不要填写值，除非用的是银联直连的pos或者已经与银联约定好，请咨询联调人员终端设备编号，收单机构为商户终端分 配的唯一编号。
    private String terminalType;//	否	2	不要填写值，除非用的是银联直连的pos或者已经与银联约定好，请咨询联调人员终端设备类型，受理方可参考终端注册 时的设备类型填写，取值如下:01:自动柜员机(含 ATM 和 CDM)和 private String 多媒体自助终端;02:传统 POS;03:mPOS;04:智能 POS;05:II 型固定电话06:云闪付终端;07:保留使用;08:手机 POS;09:刷脸付终端;10:条码支付受理终端;11:条码支付辅助受理终端;12:行业终端(公交、地铁用于指定行 private String 业的终端);13:MIS 终端;
    private String serialNum;//	否	50	不要填写值，除非用的是银联直连的pos或者已经与银联约定好，请咨询联调人员终端设备的硬件序列号，出现要求: 终端类型(terminal_type)填写为 02、 03、04、05、06、08、09 或 10 时，必 须填写终端序列号。

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public String getNetworkLicense() {
        return networkLicense;
    }

    public void setNetworkLicense(String networkLicense) {
        this.networkLicense = networkLicense;
    }

    public String getEncryptRandNum() {
        return encryptRandNum;
    }

    public void setEncryptRandNum(String encryptRandNum) {
        this.encryptRandNum = encryptRandNum;
    }

    public String getSecretText() {
        return secretText;
    }

    public void setSecretText(String secretText) {
        this.secretText = secretText;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getMobileCountryCd() {
        return mobileCountryCd;
    }

    public void setMobileCountryCd(String mobileCountryCd) {
        this.mobileCountryCd = mobileCountryCd;
    }

    public String getMobileNetNum() {
        return mobileNetNum;
    }

    public void setMobileNetNum(String mobileNetNum) {
        this.mobileNetNum = mobileNetNum;
    }

    public String getIccId() {
        return iccId;
    }

    public void setIccId(String iccId) {
        this.iccId = iccId;
    }

    public String getLocationCd1() {
        return locationCd1;
    }

    public void setLocationCd1(String locationCd1) {
        this.locationCd1 = locationCd1;
    }

    public String getLbsNum1() {
        return lbsNum1;
    }

    public void setLbsNum1(String lbsNum1) {
        this.lbsNum1 = lbsNum1;
    }

    public String getLbsSignal1() {
        return lbsSignal1;
    }

    public void setLbsSignal1(String lbsSignal1) {
        this.lbsSignal1 = lbsSignal1;
    }

    public String getLocationCd2() {
        return locationCd2;
    }

    public void setLocationCd2(String locationCd2) {
        this.locationCd2 = locationCd2;
    }

    public String getLbsNum2() {
        return lbsNum2;
    }

    public void setLbsNum2(String lbsNum2) {
        this.lbsNum2 = lbsNum2;
    }

    public String getLbsSignal2() {
        return lbsSignal2;
    }

    public void setLbsSignal2(String lbsSignal2) {
        this.lbsSignal2 = lbsSignal2;
    }

    public String getLocationCd3() {
        return locationCd3;
    }

    public void setLocationCd3(String locationCd3) {
        this.locationCd3 = locationCd3;
    }

    public String getLbsNum3() {
        return lbsNum3;
    }

    public void setLbsNum3(String lbsNum3) {
        this.lbsNum3 = lbsNum3;
    }

    public String getLbsSignal3() {
        return lbsSignal3;
    }

    public void setLbsSignal3(String lbsSignal3) {
        this.lbsSignal3 = lbsSignal3;
    }

    public String getTelecomSysId() {
        return telecomSysId;
    }

    public void setTelecomSysId(String telecomSysId) {
        this.telecomSysId = telecomSysId;
    }

    public String getTelecomNetId() {
        return telecomNetId;
    }

    public void setTelecomNetId(String telecomNetId) {
        this.telecomNetId = telecomNetId;
    }

    public String getTelecomLbs() {
        return telecomLbs;
    }

    public void setTelecomLbs(String telecomLbs) {
        this.telecomLbs = telecomLbs;
    }

    public String getTelecomLbsSignal() {
        return telecomLbsSignal;
    }

    public void setTelecomLbsSignal(String telecomLbsSignal) {
        this.telecomLbsSignal = telecomLbsSignal;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }
}