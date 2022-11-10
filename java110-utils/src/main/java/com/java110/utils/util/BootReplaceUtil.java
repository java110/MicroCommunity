package com.java110.utils.util;

public class BootReplaceUtil {

    public static String replaceServiceName(String requestUrl){
        if(requestUrl.contains("http://community-service")){
            requestUrl = requestUrl.replace("community-service","127.0.0.1:8008");
        }

        if(requestUrl.contains("http://user-service")){
            requestUrl = requestUrl.replace("user-service","127.0.0.1:8008");
        }

        if(requestUrl.contains("http://common-service")){
            requestUrl = requestUrl.replace("common-service","127.0.0.1:8008");
        }

        if(requestUrl.contains("http://fee-service")){
            requestUrl = requestUrl.replace("fee-service","127.0.0.1:8008");
        }

        if(requestUrl.contains("http://acct-service")){
            requestUrl = requestUrl.replace("acct-service","127.0.0.1:8008");
        }
        if(requestUrl.contains("http://dev-service")){
            requestUrl = requestUrl.replace("dev-service","127.0.0.1:8008");
        }

        if(requestUrl.contains("http://job-service")){
            requestUrl = requestUrl.replace("job-service","127.0.0.1:8008");
        }

        if(requestUrl.contains("http://oa-service")){
            requestUrl = requestUrl.replace("oa-service","127.0.0.1:8008");
        }

        if(requestUrl.contains("http://store-service")){
            requestUrl = requestUrl.replace("store-service","127.0.0.1:8008");
        }

        if(requestUrl.contains("http://order-service")){
            requestUrl = requestUrl.replace("order-service","127.0.0.1:8008");
        }

        if(requestUrl.contains("http://report-service")){
            requestUrl = requestUrl.replace("report-service","127.0.0.1:8008");
        }

        if(requestUrl.contains("http://scm-service")){
            requestUrl = requestUrl.replace("scm-service","127.0.0.1:8008");
        }


        return requestUrl;
    }
}
