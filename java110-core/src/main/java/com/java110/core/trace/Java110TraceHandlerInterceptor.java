package com.java110.core.trace;

import com.java110.core.log.LoggerFactory;
import com.java110.dto.trace.TraceAnnotationsDto;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 所有请求 调用链 拦截
 * Created by wuxw on 2018/5/2.
 */
@Component
public class Java110TraceHandlerInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(Java110TraceHandlerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("进入拦截器Java110TraceHandlerInterceptor>>preHandle");
        // 获取组件名称 和方法名称
        String url = request.getRequestURI() != null ? request.getRequestURI() : "";
        Map<String, Object> headers = new HashMap<>();
        Enumeration reqHeaderEnum = request.getHeaderNames();
        while (reqHeaderEnum.hasMoreElements()) {
            String headerName = (String) reqHeaderEnum.nextElement();
            headers.put(headerName.toLowerCase(), request.getHeader(headerName));
        }
        //调用链logSwatch
        Java110TraceFactory.createTrace(url, headers);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.debug("完成拦截器Java110TraceHandlerInterceptor>>afterCompletion");
        Java110TraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_CLIENT_RECEIVE);

        //response.getOutputStream();

    }

//    public String getReqData(HttpServletRequest request) throws Exception{
//        String reqData = "";
//        if ("POST,PUT".contains(request.getMethod())) {
//            InputStream in = request.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//            //reader.
//            StringBuffer sb = new StringBuffer();
//            String str = "";
//            while ((str = reader.readLine()) != null) {
//                sb.append(str);
//            }
//            reqData = sb.toString();
//        } else {
//            Map<String, String[]> params = request.getParameterMap();
//            if (params != null && !params.isEmpty()) {
//                JSONObject paramObj = new JSONObject();
//                for (String key : params.keySet()) {
//                    if (params.get(key).length > 0) {
//                        String value = "";
//                        for (int paramIndex = 0; paramIndex < params.get(key).length; paramIndex++) {
//                            value += (params.get(key)[paramIndex] + ",");
//                        }
//                        value = value.endsWith(",") ? value.substring(0, value.length() - 1) : value;
//                        paramObj.put(key, value);
//                    }
//                    continue;
//                }
//                reqData = paramObj.toJSONString();
//            }
//        }
//
//        return reqData;
//    }


}
