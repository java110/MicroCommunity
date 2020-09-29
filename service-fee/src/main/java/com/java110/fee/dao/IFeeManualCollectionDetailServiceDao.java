package com.java110.fee.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 托收明细组件内部之间使用，没有给外围系统提供服务能力
 * 托收明细服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IFeeManualCollectionDetailServiceDao {


    /**
     * 保存 托收明细信息
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveFeeManualCollectionDetailInfo(Map info) throws DAOException;


    /**
     * 查询托收明细信息（instance过程）
     * 根据bId 查询托收明细信息
     *
     * @param info bId 信息
     * @return 托收明细信息
     * @throws DAOException DAO异常
     */
    List<Map> getFeeManualCollectionDetailInfo(Map info) throws DAOException;


    /**
     * 修改托收明细信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateFeeManualCollectionDetailInfo(Map info) throws DAOException;


    /**
     * 查询托收明细总数
     *
     * @param info 托收明细信息
     * @return 托收明细数量
     */
    int queryFeeManualCollectionDetailsCount(Map info);

    double queryFeeManualCollectionDetailTotalFee(Map info);

}
