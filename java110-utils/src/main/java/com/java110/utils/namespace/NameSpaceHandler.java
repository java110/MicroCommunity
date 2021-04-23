package com.java110.utils.namespace;

/**
 * 由于缓存系统保存数据时，需要有命名空间的概念，为了统一管理
 * 这里统一设计
 *
 * 0-65535 之间
 *
 * Created by wuxw on 2017/3/16.
 */
public class NameSpaceHandler {

    private static final int DEFAULT_NAMESPACE = 101; // 默认命名空间


    /**
     * 对应于code_mapping 表的域 DynamicConstant
     */
    public static final String CODE_MAPPING_DOMAIN_DYNAMIC_CONSTANT = "DynamicConstant";

    /**
     * 对应于code_mapping 表的域 sms
     */
    public static final String CODE_MAPPING_DOMAIN_SMS = "sms";

    /**
     * 销售品规格域
     */
    public static final String OFFER_SPEC_DOMAIN="offer_spec";

    /**
     * 用户信息域 user表信息
     */
    public static final String USER_DOMAIN = "user";

    /**
     * 根据域生成命名空间
     * @param domain
     * @return
     */
    public static int getNameSpaceHandler(String domain) {

        if(CODE_MAPPING_DOMAIN_DYNAMIC_CONSTANT.equals(domain)){
            return 102;
        }else if(CODE_MAPPING_DOMAIN_SMS.equals(domain)){
            return 103;
        }else if(OFFER_SPEC_DOMAIN.equals(domain)){
            return 104;
        }else if(USER_DOMAIN.equals(domain)){
            return 105;
        }
        return DEFAULT_NAMESPACE;
    }

    /**
     * 获取默认命名空间
     * @return
     */
    public static int getDefaultNamespace(){
        return getNameSpaceHandler(null);
    }

}
