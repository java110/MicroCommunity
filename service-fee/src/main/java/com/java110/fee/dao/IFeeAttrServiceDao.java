package com.java110.fee.dao;


import com.java110.po.fee.FeeAttrPo;
import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 费用属性组件内部之间使用，没有给外围系统提供服务能力
 * 费用属性服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IFeeAttrServiceDao {

    /**
     * 保存 费用属性信息
     * @param businessFeeAttrInfo 费用属性信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessFeeAttrInfo(Map businessFeeAttrInfo) throws DAOException;



    /**
     * 查询费用属性信息（business过程）
     * 根据bId 查询费用属性信息
     * @param info bId 信息
     * @return 费用属性信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessFeeAttrInfo(Map info) throws DAOException;




    /**
     * 保存 费用属性信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveFeeAttrInfoInstance(Map info) throws DAOException;




    /**
     * 查询费用属性信息（instance过程）
     * 根据bId 查询费用属性信息
     * @param info bId 信息
     * @return 费用属性信息
     * @throws DAOException DAO异常
     */
    List<Map> getFeeAttrInfo(Map info) throws DAOException;



    /**
     * 修改费用属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateFeeAttrInfoInstance(Map info) throws DAOException;


    /**
     * 查询费用属性总数
     *
     * @param info 费用属性信息
     * @return 费用属性数量
     */
    int queryFeeAttrsCount(Map info);


    /**
     * 保存费用属性
     * @param info
     * @return
     */
    int saveFeeAttrs(Map info);

}
