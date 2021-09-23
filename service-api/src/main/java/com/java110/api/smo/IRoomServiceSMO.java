package com.java110.api.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 房屋服务类
 */
public interface IRoomServiceSMO {

    /**
     * 添加房屋信息
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> saveRoom(IPageData pd);


    /**
     * 查询 房间信息
     *
     * @param pd 页面数据封装对象  分页信息 房屋编号 单元信息
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> listRoom(IPageData pd);


    /**
     * 根据业主查询 房间信息
     *
     * @param pd 页面数据封装对象  分页信息 房屋编号 单元信息
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> listRoomByOwner(IPageData pd);

    /**
     * 查询 房间信息 未被销售的
     *
     * @param pd 页面数据封装对象  分页信息 房屋编号 单元信息
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> listRoomWithOutSell(IPageData pd);

    /**
     * 查询 房间信息 已销售的
     *
     * @param pd 页面数据封装对象  分页信息 房屋编号 单元信息
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> listRoomWithSell(IPageData pd);


    /**
     * 修改房屋信息
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> updateRoom(IPageData pd);

    /**
     * 删除房屋信息
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> deleteRoom(IPageData pd);

    /**
     * 销售房屋信息
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> sellRoom(IPageData pd);


    /**
     * 退房处理
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    ResponseEntity<String> exitRoom(IPageData pd);
}
