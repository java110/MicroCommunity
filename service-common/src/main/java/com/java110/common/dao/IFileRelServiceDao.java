package com.java110.common.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 文件存放组件内部之间使用，没有给外围系统提供服务能力
 * 文件存放服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IFileRelServiceDao {

    /**
     * 保存 文件存放信息
     *
     * @param businessFileRelInfo 文件存放信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessFileRelInfo(Map businessFileRelInfo) throws DAOException;


    /**
     * 查询文件存放信息（business过程）
     * 根据bId 查询文件存放信息
     *
     * @param info bId 信息
     * @return 文件存放信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessFileRelInfo(Map info) throws DAOException;


    /**
     * 保存 文件存放信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveFileRelInfoInstance(Map info) throws DAOException;


    /**
     * 保存文件管理
     *
     * @param info
     * @return
     */
    int saveFileRel(Map info);

    int deleteFileRel(Map info);


    /**
     * 查询文件存放信息（instance过程）
     * 根据bId 查询文件存放信息
     *
     * @param info bId 信息
     * @return 文件存放信息
     * @throws DAOException DAO异常
     */
    List<Map> getFileRelInfo(Map info) throws DAOException;


    /**
     * 修改文件存放信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateFileRelInfoInstance(Map info) throws DAOException;


    /**
     * 查询文件存放总数
     *
     * @param info 文件存放信息
     * @return 文件存放数量
     */
    int queryFileRelsCount(Map info);

}
