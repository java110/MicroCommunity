package com.java110.job.common;


import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Map;

/**
 * 自定义PropertyPlaceholderConfigurer返回properties内容
 * 
 * @author 吕振龙
 * 
 */
public class CustomizedPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {


	private static Map<String, Object> ctxPropertiesMap;

	/**
	 * @return the ctxPropertiesMap
	 */
	public static Map<String, Object> getCtxPropertiesMap() {
		return ctxPropertiesMap;
	}


	/**根据KEY查询值
	 * @param name
	 * @return
	 */
	public static Object getContextProperty(String name) {
		return ctxPropertiesMap.get(name);
	}


}