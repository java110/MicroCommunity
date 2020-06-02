package com.java110.common.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 黑白名单组件内部之间使用，没有给外围系统提供服务能力
 * 黑白名单服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface ICarBlackWhiteServiceDao {

    /**
     * 保存 黑白名单信息
     * @param businessCarBlackWhiteInfo 黑白名单信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessCarBlackWhiteInfo(Map businessCarBlackWhiteInfo) throws DAOException;



    /**
     * 查询黑白名单信息（business过程）
     * 根据bId 查询黑白名单信息
     * @param info bId 信息
     * @return 黑白名单信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessCarBlackWhiteInfo(Map info) throws DAOException;




    /**
     * 保存 黑白名单信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveCarBlackWhiteInfoInstance(Map info) throws DAOException;




    /**
     * 查询黑白名单信息（instance过程）
     * 根据bId 查询黑白名单信息
     * @param info bId 信息
     * @return 黑白名单信息
     * @throws DAOException DAO异常
     */
    List<Map> getCarBlackWhiteInfo(Map info) throws DAOException;



    /**
     * 修改黑白名单信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateCarBlackWhiteInfoInstance(Map info) throws DAOException;


    /**
     * 查询黑白名单总数
     *
     * @param info 黑白名单信息
     * @return 黑白名单数量
     */
    int queryCarBlackWhitesCount(Map info);

}
