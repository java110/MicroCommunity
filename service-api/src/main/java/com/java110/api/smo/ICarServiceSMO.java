package com.java110.api.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 小区楼接口类
 */
public interface ICarServiceSMO {

    /**
     * 查询小区楼信息
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> listCar(IPageData pd);


    /**
     * 添加小区楼信息
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> saveCar(IPageData pd);

    /**
     * 编辑小区楼信息
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> editCar(IPageData pd);

    /**
     * 删除小区楼
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> deleteCar(IPageData pd);

    /**
     * 获取车辆类型
     * @param pd 页面请求数据封装
     * @return
     */
    ResponseEntity<String> listCarType(IPageData pd);
}
