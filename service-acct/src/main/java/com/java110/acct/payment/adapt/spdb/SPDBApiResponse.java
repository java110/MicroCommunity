package com.java110.acct.payment.adapt.spdb;

import java.util.List;
import java.util.Map;

/**
 * API调用响应对象
 *
 * @author z
 */
public class SPDBApiResponse {
    /**
     * HTTP响应体，解密后的
     */
    private String resBody;

    /**
     * HTTP响应体，非解密后的原报文体
     */
    private String resOriBody;

    /**
     * HTTP响应状态码；200、401、500等
     */
    private Integer httpStatus;

    /**
     * HTTP响应头
     */
    private Map<String, List<String>> resHeaders;

    public SPDBApiResponse(String resBody, String resOriBody, Integer httpStatus, Map<String, List<String>> resHeaders) {
        this.resBody = resBody;
        this.resOriBody = resOriBody;
        this.httpStatus = httpStatus;
        this.resHeaders = resHeaders;
    }

    public String getResBody() {
        return resBody;
    }

    public void setResBody(String resBody) {
        this.resBody = resBody;
    }

    public String getResOriBody() {
        return resOriBody;
    }

    public void setResOriBody(String resOriBody) {
        this.resOriBody = resOriBody;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Map<String, List<String>> getResHeaders() {
        return resHeaders;
    }

    public void setResHeaders(Map<String, List<String>> resHeaders) {
        this.resHeaders = resHeaders;
    }

    @Override
    public String toString() {
        return "SPDBApiResponse{" +
                "resBody='" + resBody + '\'' +
                ", resOriBody='" + resOriBody + '\'' +
                ", httpStatus=" + httpStatus +
                ", resHeaders=" + headersToString() +
                '}';
    }

    public String headersToString() {
        if (resHeaders != null && !resHeaders.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{");
            resHeaders.forEach((key, value) -> stringBuilder.append(key).append("=").append(String.join(",", value)).append(";"));
            stringBuilder.append("}");
            return stringBuilder.toString();
        } else {
            return "";
        }
    }
}
