package com.java110.boot.components;

import com.java110.boot.smo.INavServiceSMO;
import com.java110.core.context.IPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 导航栏
 * Created by wuxw on 2019/3/19.
 */

@Component("nav")
public class NavComponent {

    @Autowired
    private INavServiceSMO navServiceSMOImpl;


    /**
     * 退出登录
     *
     * @param pd 页面封装对象
     * @return 页面对象ResponseEntity
     */
    public ResponseEntity<String> logout(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = navServiceSMOImpl.doExit(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    /**
     * 获取用户信息
     *
     * @param pd 页面封装对象
     * @return 页面对象ResponseEntity
     */
    public ResponseEntity<String> getUserInfo(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = navServiceSMOImpl.getUserInfo(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


}
