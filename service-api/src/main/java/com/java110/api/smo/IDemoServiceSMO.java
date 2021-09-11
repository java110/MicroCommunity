package com.java110.api.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 用例接口
 */
public interface IDemoServiceSMO {

    /**
     * 查询用例信息
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> listDemo(IPageData pd);

    /**
     * 查询用例信息
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> listDemoStudy(IPageData pd);


    /**
     * 添加用例信息
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> saveDemo(IPageData pd);

    /**
     * 编辑用例信息
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> editDemo(IPageData pd);

    /**
     * 删除用例
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> deleteDemo(IPageData pd);
}
