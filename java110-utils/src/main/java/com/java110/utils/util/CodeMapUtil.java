/**
 * 文件名：CodeMapUtil.java
 * <p>
 * 版本信息：
 * 日期：2017-3-1
 * Copyright 亚信联创 Corporation 2017
 * 版权所有
 */
package com.java110.utils.util;

import com.java110.utils.log.LoggerEngine;
import com.java110.entity.mapping.CodeMapping;
import com.java110.utils.namespace.NameSpaceHandler;

import java.util.List;

/**
 * 类名称：CodeMapUtil
 * 类描述：
 * 创建人：wuxw
 * 创建时间：2017-3-1
 */
@SuppressWarnings("unchecked")

@Deprecated
public class CodeMapUtil extends LoggerEngine {



    //常量域
    public static final String DOMAIN_DYNAMIC_CONSTANT = "DynamicConstant";

    //短信域
    public static final String DOMAIN_SMS = "sms";





    /**
     * 根据domain 查询 映射
     *
     * @param domain 域
     * @param isReload 是否重新加载数据至缓存
     * @return
     */
    public static List<CodeMapping> getCodeMappingByDomain(String domain, Boolean isReload) {

        List<CodeMapping> codeMappings = null;

        String tmpDomain = CacheUtil.KEY_CODE_MAPPING_PREFIX+domain;

        //先存tair中查询，是否有数据
        codeMappings = CacheUtil.gets(tmpDomain, CodeMapping.class);

        if ((codeMappings == null || codeMappings.size() == 0) && isReload) {
            codeMappings = reloadCodeMappingByDomain(domain);
        }

        return codeMappings;
    }

    /**
     * 重新加载数据，并且将数据保存至 tair中,为了避免服务之间传递大量的数据，
     * 将数据保存至tair中设计到user服务去处理，等user服务返回后，再次调用tair
     *
     * @param domain
     * @return
     */
    public static List<CodeMapping> reloadCodeMappingByDomain(String domain) {
        //return getICommonBMO().reloadCodeMappingByDomain(domain);
        return null;
    }

    /**
     * 根据省内编码获取集团编码
     *
     * @param pCode  省内编码
     * @param domain 映射域
     * @return
     */
    /*public static String getHCodeByPCode(String pCode, String domain) {
        if (null == pCode || "".equals(pCode) || null == domain || "".equals(domain)) {
            return "";
        }
        List<CodeMapping> codeMappings = getCodeMappingByDomain(domain,true);

        //如果还是为空，则直接返回""
        if (codeMappings == null || codeMappings.size() == 0) {
            return "";
        }

        for (CodeMapping codeMap : codeMappings) {
            if (pCode.equals(codeMap.getP_code())) {
                return codeMap.getH_code();
            }
        }

        return "";
    }*/



    /**
     * 根据集团编码获取省内编码
     *
     * @param hCode  集团编码
     * @param domain 映射域
     * @return
     */
    public static String getPCodeByHCode(String hCode, String domain) {
        if (null == hCode || "".equals(hCode) || null == domain || "".equals(domain)) {
            return "";
        }
        CodeMapping codeMapping = CacheUtil.get(NameSpaceHandler.getNameSpaceHandler(domain),CacheUtil.KEY_CODE_MAPPING_PREFIX+hCode,CodeMapping.class);
        return codeMapping != null ? codeMapping.getP_code():"";
    }


    /**
     * 根据集团编码获取描述字段。
     *
     * @param hCode  集团编码
     * @param domain 映射域
     * @return
     */
    public static String getDescByHCode(String hCode, String domain) {
        if (null == hCode || "".equals(hCode) || null == domain || "".equals(domain)) {
            return "";
        }
        CodeMapping codeMapping = CacheUtil.get(NameSpaceHandler.getNameSpaceHandler(domain),CacheUtil.KEY_CODE_MAPPING_PREFIX+hCode,CodeMapping.class);
        return codeMapping != null ? codeMapping.getDescription():"";
    }


    /**
     * 根据动态常量名称获取动态常量值
     *
     * @param hCode 常量域对应的H_Code
     * @return
     * @author wuxw
     */
    public static String getDynamicConstantValue(String hCode) {
        if (null == hCode || "".equals(hCode)) {
            return null;
        }
        return getPCodeByHCode(hCode, DOMAIN_DYNAMIC_CONSTANT);
    }


    /**
     * 根据动态常量名称获取标识描述
     *
     * @param hCode
     * @return
     * @author wuxw
     */
    public static String getDynamicConstantDesc(String hCode) {
        if (null == hCode || "".equals(hCode)) {
            return null;
        }
        return getDescByHCode(hCode, DOMAIN_DYNAMIC_CONSTANT);
    }
}
