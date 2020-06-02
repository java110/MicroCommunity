package com.java110.common.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 应用组件内部之间使用，没有给外围系统提供服务能力
 * 应用服务接口类，要求全部以字符串传输，方便微服务化
 * 文件保存 文件查询
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IFileServiceDao {

    /**
     * 保存 文件数据
     * @param fileInfo 应用信息 封装
     * @throws DAOException 操作数据库异常
     */
    int saveFile(Map fileInfo) throws DAOException;



    /**
     * 查询文件信息
     * @param info bId 信息
     * @return 应用信息
     * @throws DAOException DAO异常
     */
    List<Map> getFiles(Map info) throws DAOException;



}
