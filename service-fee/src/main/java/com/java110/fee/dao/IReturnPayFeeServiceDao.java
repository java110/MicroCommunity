package com.java110.fee.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 退费表组件内部之间使用，没有给外围系统提供服务能力
 * 退费表服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IReturnPayFeeServiceDao {

    /**
     * 保存 退费表信息
     *
     * @param businessReturnPayFeeInfo 退费表信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessReturnPayFeeInfo(Map businessReturnPayFeeInfo) throws DAOException;


    /**
     * 查询退费表信息（business过程）
     * 根据bId 查询退费表信息
     *
     * @param info bId 信息
     * @return 退费表信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessReturnPayFeeInfo(Map info) throws DAOException;


    /**
     * 保存 退费表信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveReturnPayFeeInfoInstance(Map info) throws DAOException;


    /**
     * 查询退费表信息（instance过程）
     * 根据bId 查询退费表信息
     *
     * @param info bId 信息
     * @return 退费表信息
     * @throws DAOException DAO异常
     */
    List<Map> getReturnPayFeeInfo(Map info) throws DAOException;

    /**
     * 查询退费表信息（instance过程）
     * 根据bId 查询退费表信息
     *
     * @param info bId 信息
     * @return 退费表信息
     * @throws DAOException DAO异常
     */
    List<Map> getRoomReturnPayFeeInfo(Map info) throws DAOException;

    /**
     * 查询退费表信息（instance过程）
     * 根据bId 查询退费表信息
     *
     * @param info bId 信息
     * @return 退费表信息
     * @throws DAOException DAO异常
     */
    List<Map> getCarReturnPayFeeInfo(Map info) throws DAOException;


    /**
     * 修改退费表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateReturnPayFeeInfoInstance(Map info) throws DAOException;


    /**
     * 查询退费表总数
     *
     * @param info 退费表信息
     * @return 退费表数量
     */
    int queryReturnPayFeesCount(Map info);

}
