package com.java110.fee.dao;


import com.java110.po.fee.PayFeePo;
import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 费用组件内部之间使用，没有给外围系统提供服务能力
 * 费用服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IFeeServiceDao {

    /**
     * 保存 费用信息
     *
     * @param businessFeeInfo 费用信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessFeeInfo(Map businessFeeInfo) throws DAOException;


    /**
     * 查询费用信息（business过程）
     * 根据bId 查询费用信息
     *
     * @param info bId 信息
     * @return 费用信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessFeeInfo(Map info) throws DAOException;


    /**
     * 保存 费用信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveFeeInfoInstance(Map info) throws DAOException;


    /**
     * 查询费用信息（instance过程）
     * 根据bId 查询费用信息
     *
     * @param info bId 信息
     * @return 费用信息
     * @throws DAOException DAO异常
     */
    List<Map> getFeeInfo(Map info) throws DAOException;


    /**
     * 修改费用信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateFeeInfoInstance(Map info) throws DAOException;


    /**
     * 查询费用总数
     *
     * @param info 费用信息
     * @return 费用数量
     */
    int queryFeesCount(Map info);


    /**
     * 查询费用总数
     *
     * @param info 费用信息
     * @return 费用数量
     */
    int queryFeeByAttrCount(Map info);


    /**
     * 查询费用信息（instance过程）
     * 根据bId 查询费用信息
     *
     * @param info bId 信息
     * @return 费用信息
     * @throws DAOException DAO异常
     */
    List<Map> queryFeeByAttr(Map info) throws DAOException;

    /**
     * 查询费用账期
     *
     * @param info 费用信息
     * @return 费用数量
     */
    public int queryBillCount(Map info);

    /**
     * 查询账期
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    public List<Map> queryBills(Map info) throws DAOException;

    /**
     * 查询账单欠费总数
     *
     * @param info 费用信息
     * @return 费用数量
     */
    public int queryBillOweFeeCount(Map info);

    /**
     * 查询账单欠费
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    public List<Map> queryBillOweFees(Map info) throws DAOException;

    /**
     * 查询账单欠费
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    int insertBillOweFees(Map info) throws DAOException;


    /**
     * 修改账单欠费
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    int updateBillOweFees(Map info) throws DAOException;

    /**
     *  保存账单
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    int insertBill(Map info) throws DAOException;

    /**
     * 保存费用
     * @param info
     * @return
     */
    int insertFees(Map info);


    int computeBillOweFeeCount(Map beanCovertMap);

    List<Map> computeBillOweFee(Map beanCovertMap);

    int computeEveryOweFeeCount(Map beanCovertMap);

    List<Map> computeEveryOweFee(Map beanCovertMap);

    int deleteFeesByBatch(Map beanCovertMap);
}
