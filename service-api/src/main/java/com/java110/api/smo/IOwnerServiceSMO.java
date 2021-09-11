package com.java110.api.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 小区楼接口类
 */
public interface IOwnerServiceSMO {

    /**
     * 查询小区楼信息
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> listOwner(IPageData pd);

    /**
     * 查询业主成员
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    public ResponseEntity<String> listOwnerMember(IPageData pd);

    /**
     * 添加小区楼信息
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> saveOwner(IPageData pd);

    /**
     * 编辑小区楼信息
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> editOwner(IPageData pd);

    /**
     * 删除小区楼
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> deleteOwner(IPageData pd);
}
