package com.java110.acct.payment.adapt.spdb;

/**
 * API平台APP参数维护，若您有多个，可在此添加，通过new SPDBSecurity(SPDBApp spdbApp)初始化
 * 仅测试环境推荐此方式，生产环境相关参数建议存放数据库做成可配置项
 *
 * @author z
 */
public enum SPDBApp {
    /**
     * APP参数列表
     */
    TEST("b*******-****-****-****-***********3",
            "N**************************************************************0",
            "1**************************************************************s", "1********************************************************************************************************************************4"),

    ;

    /**
     * API平台APP的唯一标识
     */
    private final String clientId;
    /**
     * API平台APP的secret
     */
    private final String secret;
    /**
     * 合作方SM2私钥
     */
    private final String sm2PrivateKey;
    /**
     * 浦发SM2公钥
     */
    private final String spdbSM2PublicKey;

    SPDBApp(String clientId, String secret, String sm2PrivateKey, String spdbSM2PublicKey) {
        this.clientId = clientId;
        this.secret = secret;
        this.sm2PrivateKey = sm2PrivateKey;
        this.spdbSM2PublicKey = spdbSM2PublicKey;
    }

    public String getClientId() {
        return clientId;
    }

    public String getSecret() {
        return secret;
    }

    public String getSm2PrivateKey() {
        return sm2PrivateKey;
    }

    public String getSpdbSM2PublicKey() {
        return spdbSM2PublicKey;
    }
}
