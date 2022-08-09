package com.java110.boot.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 当行栏组件
 * Created by Administrator on 2019/4/1.
 */
public interface INavServiceSMO {


    /**
     * 用户退出
     *
     * @param pd 页面封装数据
     * @return ResponseEntity对象
     */
    ResponseEntity<String> doExit(IPageData pd);

    /**
     * 获取用户信息
     *
     * @param pd 页面封装数据
     * @return ResponseEntity对象
     */
    ResponseEntity<String> getUserInfo(IPageData pd);


}
