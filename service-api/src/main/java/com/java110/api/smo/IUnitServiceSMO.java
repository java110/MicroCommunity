package com.java110.api.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @ClassName IUnitServiceSMO
 * @Description TODO 小区单元服务类
 * @Author wuxw
 * @Date 2019/5/2 19:27
 * @Version 1.0
 * add by wuxw 2019/5/2
 **/
public interface IUnitServiceSMO {

    /**
     * 加载小区单元
     * 单元目前不考虑 分页问题
     *
     * @param pd 页面数据封装 包含小区楼ID
     * @return ResponseEntity 对象给前段页面
     */
    ResponseEntity<String> listUnits(IPageData pd);


    /**
     * 保存小区单元信息
     *
     * @param pd 页面数据封装
     * @return ResponseEntity对象
     */
    ResponseEntity<String> saveUnit(IPageData pd);

    /**
     * 修改小区单元信息
     *
     * @param pd 页面数据封装
     * @return ResponseEntity对象
     */
    ResponseEntity<String> updateUnit(IPageData pd);

    /**
     * 删除小区单元
     *
     * @param pd 页面数据封装
     * @return ResponseEntity对象
     */
    ResponseEntity<String> deleteUnit(IPageData pd);
}
