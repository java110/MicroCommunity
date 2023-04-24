package com.java110.scm.supplier.defaultSupplier;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.supplier.SupplierConfigDto;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class DefaultSupplierFactory {

    private static final Logger logger = LoggerFactory.getLogger(DefaultSupplierFactory.class);

    //应用ID
    public static final String APP_ID = "APP_ID";

    //应用秘钥
    public static final String APP_SECURE = "APP_SECURE";

    public static final String TOKEN_URL = "TOKEN_URL";
    public static final String COUPON_QRCODE_URL = "COUPON_QRCODE_URL";


    /**
     * 封装头信息
     *
     * @return
     */
    private static HttpHeaders getHeaders(RestTemplate outRestTemplate, List<SupplierConfigDto> supplierConfigDtos) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("access-token", get(outRestTemplate, false, supplierConfigDtos));
        //httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        return httpHeaders;
    }

    public static String get(RestTemplate restTemplate, boolean refreshAccessToken, List<SupplierConfigDto> supplierConfigDtos) {
        String appId = getConfigValue(supplierConfigDtos, APP_ID);
        String appSecure = getConfigValue(supplierConfigDtos, APP_SECURE);
        String tokenUrl = getConfigValue(supplierConfigDtos, TOKEN_URL);

        String token = CommonCache.getValue(appId + "_ACCESS_TOKEN");
        if (!StringUtil.isEmpty(token) && !refreshAccessToken) {
            return token;
        }
        HttpHeaders headers = new HttpHeaders();
        JSONObject param = new JSONObject();
        param.put("appId", appId);
        param.put("appSecure", appSecure);
        HttpEntity httpEntity = new HttpEntity(param, headers);
        ResponseEntity<String> tokenRes = restTemplate.exchange(tokenUrl, HttpMethod.POST, httpEntity, String.class);

        if (tokenRes.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("获取token失败" + tokenRes.getBody());
        }
        JSONObject tokenObj = JSONObject.parseObject(tokenRes.getBody());

        if (!tokenObj.containsKey("code") || ResultVo.CODE_OK != tokenObj.getInteger("code")) {
            throw new IllegalArgumentException("获取token失败" + tokenRes.getBody());
        }

        token = tokenObj.getJSONObject("data").getString("accessToken");
        int expiresIn = tokenObj.getJSONObject("data").getInteger("expiresIn");

        CommonCache.setValue(appId + "_ACCESS_TOKEN", token, expiresIn - 200);

        return token;
    }

    public static String getConfigValue(List<SupplierConfigDto> supplierConfigDtos, String key) {
        for (SupplierConfigDto supplierConfigDto : supplierConfigDtos) {
            if (supplierConfigDto.getColumnKey().equals(key)) {
                return supplierConfigDto.getColumnValue();
            }
        }

        return "";
    }

    public static JSONObject execute(RestTemplate outRestTemplate, JSONObject param, String url, List<SupplierConfigDto> supplierConfigDtos) {
        ResponseEntity<String> responseEntity = null;
        HttpEntity httpEntity = null;
        JSONObject paramOut = null;
        try {
            httpEntity = new HttpEntity(param.toJSONString(), getHeaders(outRestTemplate, supplierConfigDtos));
            responseEntity = outRestTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            paramOut = JSONObject.parseObject(responseEntity.getBody());

            if (paramOut.getInteger("code") != ResultVo.CODE_OK) {
                throw new CmdException(paramOut.getString("msg"));
            }
        } catch (HttpStatusCodeException e) {
            logger.error("请求失败" + e.getResponseBodyAsString(), e);
            throw e;
        } catch (Exception e){
            logger.error("请求失败", e);
            throw e;
        }finally {
            logger.debug("请求url:{},请求报文:{},返回报文：{}", url, httpEntity, responseEntity);
        }

        return paramOut;
    }

}
