package com.java110.api.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 *
 * 注册业务处理接口
 *
 * Created by wuxw on 2019/3/23.
 */
public interface IRegisterServiceSMO {

    /**
     * 用户注册
     * @param pd
     * @return
     */
    public ResponseEntity<String> doRegister(IPageData pd);

    /**
     * 发送手机验证码
     * @param pd
     * @return
     */
    public ResponseEntity<String> sendTelMessageCode(IPageData pd);


    /**
     * 验证码校验
     * @param pd 页面请求对象
     * @return
     */
    public ResponseEntity<String> validate(IPageData pd);

    /**
     * 加载地区
     * @param pd 页面请求对象
     * @return
     */
    public ResponseEntity<String> loadArea(IPageData pd);
}
