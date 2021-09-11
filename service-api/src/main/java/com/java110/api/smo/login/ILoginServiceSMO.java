package com.java110.api.smo.login;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * Created by wuxw on 2019/3/20.
 */
public interface ILoginServiceSMO {

    /**
     * 登录接口
     * @param pd 页面请求对象
     * @return
     */
    public ResponseEntity<String> doLogin(IPageData pd);


    /**
     * 生成验证码信息
     * @param pd 页面请求对象
     * @return
     */
    public ResponseEntity<String> generateValidateCode(IPageData pd);

    /**
     * 验证码校验
     * @param pd 页面请求对象
     * @return
     */
    public ResponseEntity<String> validate(IPageData pd);


}
