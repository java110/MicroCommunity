package com.java110.fee.dao;

import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 费用明细组件内部之间使用，没有给外围系统提供服务能力
 * 费用明细服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IFeeDetailServiceDao {

    /**
     * 保存 费用明细信息
     *
     * @param businessFeeDetailInfo 费用明细信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessFeeDetailInfo(Map businessFeeDetailInfo) throws DAOException;


    /**
     * 查询费用明细信息（business过程）
     * 根据bId 查询费用明细信息
     *
     * @param info bId 信息
     * @return 费用明细信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessFeeDetailInfo(Map info) throws DAOException;


    /**
     * 保存 费用明细信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveFeeDetailInfoInstance(Map info) throws DAOException;


    /**
     * 查询费用明细信息（instance过程）
     * 根据bId 查询费用明细信息
     *
     * @param info bId 信息
     * @return 费用明细信息
     * @throws DAOException DAO异常
     */
    List<Map> getFeeDetailInfo(Map info) throws DAOException;


    /**
     * 修改费用明细信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateFeeDetailInfoInstance(Map info) throws DAOException;


    /**
     * 查询费用明细总数
     *
     * @param info 费用明细信息
     * @return 费用明细数量
     */
    int queryFeeDetailsCount(Map info);

    /**
     * 保存 费用明细信息
     *
     * @param feeDetail 费用明细信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveFeeDetail(Map feeDetail) throws DAOException;

}
