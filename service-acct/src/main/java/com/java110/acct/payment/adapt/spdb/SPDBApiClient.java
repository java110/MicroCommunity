package com.java110.acct.payment.adapt.spdb;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.body.MultipartBody;
import cn.hutool.json.JSONUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * API调用实现
 *
 * @author z
 */
public class SPDBApiClient {

    private final SPDBSecurity spdbSecurity;

    /**
     * HTTP请求超时时间5分钟，可自行调整
     */
    private static final Integer HTTP_TIMEOUT = 5 * 60 * 1000;

    private static final String ERROR_HTTP_UNAUTHORIZED = "请检查接口是否订阅并审批通过，沙盒环境审批通过后需等2小时数据同步";
    private static final String ERROR_HTTP_BAD_METHOD = "HTTP请求方式有误，请参考API文档请求方式，通常有POST和GET两种";
    private static final String ERROR_SIGN = "响应头签名为空或响应验签未通过，请检浦发SM2公钥参数";

    private static final String HEAD_SPDB_CLIENT_ID = "X-SPDB-Client-ID";
    private static final String HEAD_SPDB_SM = "X-SPDB-SM";
    private static final String HEAD_SPDB_ENCRYPTION = "X-SPDB-Encryption";
    private static final String HEAD_SPDB_SIGNATURE = "X-SPDB-SIGNATURE";
    private static final String HEAD_SPDB_LABEL = "X-SPDB-LABEL";
    private static final String HEAD_SPDB_META_DATA = "X-SPDB-MetaData";
    private static final String HEAD_SPDB_CONTENT_TYPE = "Content-Type";
    private static final String HEAD_SPDB_TRUE = "true";
    private static final String HEAD_SPDB_CONTENT_TYPE_STR = "application/json;charset=utf-8";

    public SPDBApiClient(SPDBSecurity spdbSecurity) {
        this.spdbSecurity = spdbSecurity;
    }

    /**
     * 全报文加密接入方式，POST请求
     *
     * @param url     请求地址
     * @param reqBody 请求明文
     * @return 响应报文
     * @throws Exception 异常
     */
    public SPDBApiResponse post(String url, String reqBody) throws Exception {
        HttpRequest httpRequest = HttpUtil.createPost(url);
        String body = spdbSecurity.encrypt(reqBody);
        httpRequest.body(body);
        buildReqHeader(httpRequest, reqBody, null, true);
        return req(httpRequest, true, true);
    }

    /**
     * 普通验签接入方式，POST请求
     *
     * @param url              请求地址
     * @param reqBody          请求明文
     * @param isVerifyRespSign 是否验证响应签名
     * @return 响应报文
     * @throws Exception 异常
     */
    public SPDBApiResponse postWithNoEncrypt(String url, String reqBody, boolean isVerifyRespSign) throws Exception {
        HttpRequest httpRequest = HttpUtil.createPost(url);
        httpRequest.body(reqBody);
        buildReqHeader(httpRequest, reqBody, null, false);
        return req(httpRequest, false, isVerifyRespSign);
    }

    /**
     * 普通验签接入方式，POST请求，可添加额外请求头
     *
     * @param url              请求地址
     * @param reqBody          请求明文
     * @param isVerifyRespSign 是否验证响应签名
     * @param headers          额外的请求头
     * @return 响应报文
     * @throws Exception 异常
     */
    public SPDBApiResponse postWithNoEncrypt(String url, String reqBody, boolean isVerifyRespSign, Map<String, String> headers) throws Exception {
        HttpRequest httpRequest = HttpUtil.createPost(url);
        httpRequest.body(reqBody);
        buildReqHeader(httpRequest, reqBody, headers, false);
        return req(httpRequest, false, isVerifyRespSign);
    }

    /**
     * 图文信息上传
     *
     * @param url         请求地址
     * @param formDataMap 请求表单
     * @param headersMap  请求头
     * @return 响应报文
     * @throws Exception 异常
     */
    public String postWithFile(String url, Map<String, Object> formDataMap, Map<String, String> headersMap) throws Exception {
        HttpRequest httpRequest = HttpUtil.createPost(url);
        httpRequest.form(formDataMap);

        // 组装请求头
        httpRequest.addHeaders(headersMap);
        httpRequest.header(HEAD_SPDB_CLIENT_ID, spdbSecurity.getClientId());
        httpRequest.header(HEAD_SPDB_SM, HEAD_SPDB_TRUE);
        httpRequest.header(HEAD_SPDB_CONTENT_TYPE, ContentType.MULTIPART.getValue());

        // 生成签名
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Map<String, Object> formMap = httpRequest.form();
            MultipartBody.create(formMap, Charset.forName(StandardCharsets.UTF_8.name())).write(outputStream);
            byte[] formByte = outputStream.toByteArray();
            httpRequest.header(HEAD_SPDB_SIGNATURE, spdbSecurity.signNormalWithByte(formByte));
        }

        HttpResponse httpResponse = httpRequest.execute();
        int httpStatus = httpResponse.getStatus();
        if (httpStatus == HttpStatus.HTTP_OK) {
            return httpResponse.body();
        } else {
            throw new SPDBApiException(httpStatus, httpResponse.body());
        }

    }

    /**
     * 全报文加密接入方式，GET请求
     *
     * @param url     请求地址
     * @param reqBody 请求体
     * @return 响应体
     * @throws Exception 异常
     */
    public SPDBApiResponse get(String url, String reqBody) throws Exception {
        url += "?encryptBody=" + spdbSecurity.encrypt(reqBody);
        HttpRequest httpRequest = HttpUtil.createGet(url);
        httpRequest.setUrl(UrlBuilder.ofHttpWithoutEncode(url));
        buildReqHeader(httpRequest, reqBody, null, true);
        return req(httpRequest, true, true);
    }

    /**
     * 普通验签接入方式，GET请求
     *
     * @param url              请求地址
     * @param param            GET请求的URL参数，例：a=1&b=2&c=3
     * @param isVerifyRespSign 是否验证响应签名
     * @return 响应体
     * @throws Exception 异常
     */
    public SPDBApiResponse getWithNoEncrypt(String url, String param, boolean isVerifyRespSign) throws Exception {
        url += "?" + param;
        HttpRequest httpRequest = HttpUtil.createGet(url);
        httpRequest.setUrl(UrlBuilder.ofHttpWithoutEncode(url));
        buildReqHeader(httpRequest, param, null, false);
        return req(httpRequest, false, isVerifyRespSign);
    }

    /**
     * 普通验签接入方式，GET请求
     *
     * @param url     请求地址
     * @param param   GET请求的URL参数，例：a=1&b=2&c=3
     * @param headers 自定义请求头
     * @return 响应体
     * @throws Exception 异常
     */
    public SPDBApiResponse getWithNoEncrypt(String url, String param, boolean isVerifyRespSign, Map<String, String> headers) throws Exception {
        url += "?" + param;
        HttpRequest httpRequest = HttpUtil.createGet(url);
        httpRequest.setUrl(UrlBuilder.ofHttpWithoutEncode(url));
        buildReqHeader(httpRequest, param, headers, false);
        return req(httpRequest, false, isVerifyRespSign);
    }

    /**
     * 【公共文件下载】接口专用
     *
     * @param url   请求地址
     * @param param GET请求的URL参数，例：fileId=0d01160ae808b0b0bb4f2ada958b96c9182ec52e
     * @param label 场景编号，例：0001
     * @return 响应文件流
     * @throws Exception 异常
     */
    public InputStream downloadFile(String url, String param, String label) throws Exception {
        url += "?" + param;
        HttpRequest httpRequest = HttpUtil.createGet(url);
        httpRequest.setUrl(UrlBuilder.ofHttpWithoutEncode(url));
        Map<String, String> headersMap = new HashMap<>(1);
        headersMap.put(HEAD_SPDB_LABEL, label);
        buildReqHeader(httpRequest, param, headersMap, false);
        HttpResponse httpResponse = httpRequest.execute();
        int httpStatus = httpResponse.getStatus();
        if (httpStatus == HttpStatus.HTTP_OK) {
            return httpResponse.bodyStream();
        } else {
            throw new SPDBApiException(httpStatus, httpResponse.body());
        }
    }

    /**
     * 【公共文件上传】接口专用
     *
     * @param url      请求地址
     * @param spdbFile 请求体
     * @return 响应文件流
     * @throws Exception 异常
     */
    public String uploadFile(String url, SPDBFile spdbFile, String label) throws Exception {
        File file = spdbFile.getFile();
        if (file == null || !file.isFile()) {
            throw new FileNotFoundException("文件不存在，请在SPDBFile变量初始化文件参数");
        }
        spdbFile.build();

        HttpRequest httpRequest = HttpUtil.createPost(url);
        httpRequest.form("s3File", file);

        String metaData = JSONUtil.toJsonStr(spdbFile);

        httpRequest.header(HEAD_SPDB_CLIENT_ID, spdbSecurity.getClientId());
        httpRequest.header(HEAD_SPDB_SM, HEAD_SPDB_TRUE);
        httpRequest.header(HEAD_SPDB_LABEL, label);

        httpRequest.header(HEAD_SPDB_SIGNATURE, spdbSecurity.signNormal(metaData));
        httpRequest.header(HEAD_SPDB_META_DATA, metaData);

        HttpResponse httpResponse = httpRequest.execute();
        int httpStatus = httpResponse.getStatus();
        String resBody = httpResponse.body();
        if (httpStatus == HttpStatus.HTTP_OK) {
            return resBody;
        } else {
            throw new SPDBApiException(httpStatus, resBody);
        }
    }

    /**
     * 构建请求头
     *
     * @param httpRequest 请求
     * @param data        报文明文
     * @param encrypt     是否全报文加密方式接入
     */
    private void buildReqHeader(HttpRequest httpRequest, String data, Map<String, String> headers, boolean encrypt) throws Exception {
        httpRequest.addHeaders(headers);
        httpRequest.header(HEAD_SPDB_CONTENT_TYPE, HEAD_SPDB_CONTENT_TYPE_STR);
        httpRequest.header(HEAD_SPDB_CLIENT_ID, spdbSecurity.getClientId());
        httpRequest.header(HEAD_SPDB_SM, HEAD_SPDB_TRUE);

        if (encrypt) {
            httpRequest.header(HEAD_SPDB_ENCRYPTION, HEAD_SPDB_TRUE);
            httpRequest.header(HEAD_SPDB_SIGNATURE, spdbSecurity.sign(data));
        } else {
            httpRequest.header(HEAD_SPDB_SIGNATURE, spdbSecurity.signNormal(data));
        }

    }

    /**
     * http请求
     *
     * @param httpRequest      请求
     * @param isEncrypt        是否为全文加密请求
     * @param isVerifyRespSign 是否验证响应签名
     * @return 响应体
     * @throws Exception 异常
     */
    private SPDBApiResponse req(HttpRequest httpRequest, boolean isEncrypt, boolean isVerifyRespSign) throws SPDBApiException {
        httpRequest.timeout(HTTP_TIMEOUT);
        HttpResponse httpResponse = httpRequest.execute();
        int httpStatus = httpResponse.getStatus();
        String httpResBody = httpResponse.body();
        if (httpStatus == HttpStatus.HTTP_OK) {
            String resBody = isEncrypt ? spdbSecurity.decrypt(httpResBody) : httpResBody;
            if (isVerifyRespSign) {
                String spdbSign = httpResponse.header(HEAD_SPDB_SIGNATURE);
                if (StrUtil.isBlank(spdbSign) || !spdbSecurity.verifySign(resBody, spdbSign)) {
                    throw new SPDBApiException(ERROR_SIGN, httpStatus, "报文：" + resBody + "\n签名：" + spdbSign);
                }
            }

            return new SPDBApiResponse(resBody, httpResBody, httpStatus, httpResponse.headers());
        } else if (httpStatus == HttpStatus.HTTP_UNAUTHORIZED) {
            throw new SPDBApiException(ERROR_HTTP_UNAUTHORIZED, httpStatus, null);
        } else if (httpStatus == HttpStatus.HTTP_BAD_METHOD) {
            throw new SPDBApiException(ERROR_HTTP_BAD_METHOD, httpStatus, httpResBody);
        } else {
            throw new SPDBApiException(httpStatus, httpResBody);
        }
    }
}
