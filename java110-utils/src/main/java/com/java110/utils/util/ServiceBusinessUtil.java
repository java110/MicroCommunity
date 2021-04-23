/**
 * 文件名：CodeMapUtil.java
 * <p>
 * 版本信息：
 * 日期：2017-3-1
 * Copyright 亚信联创 Corporation 2017
 * 版权所有
 */
package com.java110.utils.util;

import com.java110.utils.cache.ServiceBusinessCache;
import com.java110.entity.order.ServiceBusiness;

import java.util.List;

/**
 * 类名称：ServiceBusinessUtil
 * 类描述：
 * 创建人：吴学文
 * 创建时间：2017-3-1
 */
@SuppressWarnings("unchecked")
public class ServiceBusinessUtil {


    /**
     * 查询所有数据
     *
     * @return
     */
    public static List<ServiceBusiness> getServiceBusinesses() {
        return ServiceBusinessCache.getServiceBusiness();
    }

    /**
     * 根据业务类型查询 业务信息
     *
     * @param businessTypeCd  业务类型
     * @return
     */
    public static ServiceBusiness getServiceBusiness(String businessTypeCd) {

        if(StringUtil.isEmpty(businessTypeCd)){
            return null;
        }

        List<ServiceBusiness> serviceBusinesses = getServiceBusinesses();

        if(serviceBusinesses == null || serviceBusinesses.size() == 0){
            return null;
        }

        for(ServiceBusiness serviceBusiness : serviceBusinesses){
            if(businessTypeCd.equals(serviceBusiness.getBusinessTypeCd())){
                return serviceBusiness;
            }
        }

        return null;

    }
}
