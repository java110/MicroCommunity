package com.java110.common.dao;

import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

public interface IAreaServiceDao {

    /**
     * 查询区域信息
     * @param info bId 信息
     * @return 应用信息
     * @throws DAOException DAO异常
     */
    List<Map> getAreas(Map info) throws DAOException;

    /**
     * 查询省份 城市 区域
     * @param info bId 信息
     * @return 应用信息
     * @throws DAOException DAO异常
     */
    public List<Map> getProvCityArea(Map info) throws DAOException;

    /**
     * 查询完整省份 城市 区域   如：传入区，返回市省，传入  街道，返回区，市，省
     * @param info bId 信息
     * @return 应用信息
     * @throws DAOException DAO异常
     */
    public List<Map> getWholeArea(Map info) throws DAOException;


}
