package com.java110.service.filter;

import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.FilterException;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by wuxw on 2018/5/2.
 */
public class JwtFilter extends GenericFilterBean {

    private final static Logger logger = LoggerFactory.getLogger(JwtFilter.class);


    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        String token = "";
        try {
            //获取token
            token = this.getToken(request);
            try {
                Map<String, String> claims = AuthenticationFactory.verifyToken(token);
                request.setAttribute("claims", claims);

            } catch (Exception e) {
                //Invalid signature/claims
                logger.error("解析token 失败 ：", e);
                throw new FilterException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "您还没有登录，请先登录");
            }

            chain.doFilter(req, res);
        }catch (FilterException e){
            if("POST".equals(request.getMethod())){
                writeJson(response,
                        DataTransactionFactory.pageResponseJson(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,e.getMessage(),null),
                        "UTF-8");
            }else{
                response.sendRedirect("/flow/login");
            }

        }catch (Exception e){
            response.sendRedirect("/flow/login");
        }
    }

    /**
     * 获取TOKEN
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request) throws FilterException{
        String token = "";
        for (Cookie cookie : request.getCookies()) {
            if (CommonConstant.COOKIE_AUTH_TOKEN.equals(cookie.getName())) {
                token = cookie.getValue();
            }
        }

        if (StringUtil.isNullOrNone(token)) {
            throw new FilterException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "您还没有登录，请先登录");
        }
        return token;
    }

    private void writeJson(HttpServletResponse response, String data, String encoding){
        //设置编码格式
        response.setContentType("text/plain;charset=" + encoding);
        response.setCharacterEncoding(encoding);

        PrintWriter out = null;
        try{
            out = response.getWriter();
            out.write(data);
            out.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
