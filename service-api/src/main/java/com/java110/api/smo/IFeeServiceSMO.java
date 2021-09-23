package com.java110.api.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 费用服务类
 */
public interface IFeeServiceSMO {

    /**
     * 物业配置费
     *
     * @param pd 页面数据封装对象
     * @param feeTypeCd 费用类型
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> loadPropertyConfigFee(IPageData pd, String feeTypeCd);


    /**
     * 停车配置费
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> loadParkingSpaceConfigFee(IPageData pd);

    /**
     * 缴费
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> payFee(IPageData pd);

    /**
     * 查询主费用
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> loadFeeByRoomId(IPageData pd);


    /**
     * 查询主费用
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> loadFeeByPsId(IPageData pd);

    /**
     * 查询费用明细
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> loadFeeDetail(IPageData pd);

    /**
     * 保存或修改物业费配置
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> saveOrUpdatePropertyFeeConfig(IPageData pd);

    /**
     * 保存或修改停车费配置
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> saveOrUpdateParkingSpaceFeeConfig(IPageData pd);

    /**
     * 查询费用信息
     * @param pd 页面数据封装
     * @return 费用信息
     */
    ResponseEntity<String> listArrearsFee(IPageData pd);


}
