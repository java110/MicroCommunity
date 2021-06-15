package com.java110.fee.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 收据明细组件内部之间使用，没有给外围系统提供服务能力
 * 收据明细服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IFeeReceiptDetailServiceDao {


    /**
     * 保存 收据明细信息
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveFeeReceiptDetailInfo(Map info) throws DAOException;

    /**
     * 批量保存 收据明细
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveFeeReceiptDetails(Map info) throws DAOException;


    /**
     * 查询收据明细信息（instance过程）
     * 根据bId 查询收据明细信息
     *
     * @param info bId 信息
     * @return 收据明细信息
     * @throws DAOException DAO异常
     */
    List<Map> getFeeReceiptDetailInfo(Map info) throws DAOException;


    /**
     * 修改收据明细信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateFeeReceiptDetailInfo(Map info) throws DAOException;


    /**
     * 查询收据明细总数
     *
     * @param info 收据明细信息
     * @return 收据明细数量
     */
    int queryFeeReceiptDetailsCount(Map info);

}
