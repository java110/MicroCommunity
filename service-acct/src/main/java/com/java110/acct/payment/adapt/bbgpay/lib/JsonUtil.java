package com.java110.acct.payment.adapt.bbgpay.lib;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtil {
	/**
	 * 将json转化成map
	 *
	 * @param json
	 * @return
	 */
	public static Map<String, Object> jsonToMap(String json) {
		try {
			return JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
			}, Feature.OrderedField);
		} catch (Exception e) {
			return new HashMap<>();
		}

	}

	/**
	 * 将map转化成json
	 *
	 * @param map
	 * @return
	 */
	public static String mapToJson(Map<String, Object> map) {
		return JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
	}

	public static String mapToJson2(Map<String, String> map) throws Exception {
		return JSON.toJSONString(map, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);
	}

	/**
	 * Json转Object
	 *
	 * @return
	 */
	public static <T> T jsonToObject(Class<T> clazz, String json) {
		return JSON.parseObject(json, clazz);
	}

	/**
	 * 对map 进行排序
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, Object> sortMap = new TreeMap<String, Object>(new Comparator<String>() {

			@Override
			public int compare(String str0, String str1) {
				return str0.compareTo(str1);
			}
		});
		sortMap.putAll(map);
		return sortMap;
	}
}
