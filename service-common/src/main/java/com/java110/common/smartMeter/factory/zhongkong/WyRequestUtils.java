package com.java110.common.smartMeter.factory.zhongkong;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.charge.factory.dingding.DingdingChargeUtils;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.PayUtil;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WyRequestUtils {
    private static Logger logger = LoggerFactory.getLogger(WyRequestUtils.class);

    private static String secretKey = "Wr7nGOUMMjC0F8SzsGKAoRo8BpQ10nNk";

	/*public static void main(String[] args) {
		String bh="WY_001";
		String amount="100.00";
		Map<String,String> paramMap=new HashMap<>();
		paramMap.put("bh",bh);
		paramMap.put("amount",amount);
		System.out.println(getSign(paramMap));
		//d7ebdef32e1021b4c05320c4d634c1d7
	}*/

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    private static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        //升序排序
        Map<String, String> sortMap = new TreeMap<>(String::compareTo);
        sortMap.putAll(map);
        return sortMap;
    }

    public static String getSign(Map<String, String> map) {
        //按Key进行排序
        Map<String, String> sortMap = sortMapByKey(map);
        StringBuilder signStr = new StringBuilder();
        if (sortMap != null) {
            for (Map.Entry<String, String> entry : sortMap.entrySet()) {
                signStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        String sign = signStr.append(secretKey).toString();
        return AuthenticationFactory.md5(sign);
    }

    /**
     * 请求充电桩 接口
     * <p>
     * MappingCache.getValue(DING_DING_DOMAIN, DING_DING_APP_ID),
     * MappingCache.getValue(DING_DING_DOMAIN, DING_DING_APP_SECURE
     *
     * @param url
     * @param reqMap
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public static String execute(String url, Map<String, String> reqMap, HttpMethod httpMethod)  {
        RestTemplate outRestTemplate = ApplicationContextFactory.getBean("outRestTemplate", RestTemplate.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity httpEntity = null;
        if(reqMap != null) {
            reqMap.put("sign", getSign(reqMap));
            httpEntity = new HttpEntity(JSONObject.toJSONString(reqMap), httpHeaders);
        }else{
            httpEntity = new HttpEntity("", httpHeaders);
        }
        ResponseEntity<String> response = null;
        try {
            response = outRestTemplate.exchange(url, httpMethod, httpEntity, String.class);
        } catch (HttpStatusCodeException e) {
            logger.error("请求异常", e.getResponseBodyAsString());
            return e.getResponseBodyAsString();
        }

        return response.getBody();
    }

    /**
     * 请求充电桩 接口
     * <p>
     * MappingCache.getValue(DING_DING_DOMAIN, DING_DING_APP_ID),
     * MappingCache.getValue(DING_DING_DOMAIN, DING_DING_APP_SECURE
     *
     * @param url
     * @param reqMap
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public static String executeReads(String url, Map<String, String> reqMap, HttpMethod httpMethod)  {
        RestTemplate outRestTemplate = ApplicationContextFactory.getBean("outRestTemplate", RestTemplate.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity httpEntity = null;
        if(reqMap != null) {
            //reqMap.put("sign", getSign(reqMap));
            httpEntity = new HttpEntity(JSONObject.toJSONString(reqMap), httpHeaders);
        }else{
            httpEntity = new HttpEntity("", httpHeaders);
        }
        ResponseEntity<String> response = null;
        try {
            response = outRestTemplate.exchange(url, httpMethod, httpEntity, String.class);
        } catch (HttpStatusCodeException e) {
            logger.error("请求异常", e.getResponseBodyAsString());
            return e.getResponseBodyAsString();
        }

        return response.getBody();
    }
}
