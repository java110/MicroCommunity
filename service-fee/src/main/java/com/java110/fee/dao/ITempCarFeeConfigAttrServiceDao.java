package com.java110.fee.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 临时车收费标准属性组件内部之间使用，没有给外围系统提供服务能力
 * 临时车收费标准属性服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface ITempCarFeeConfigAttrServiceDao {

    /**
     * 保存 临时车收费标准属性信息
     * @param businessTempCarFeeConfigAttrInfo 临时车收费标准属性信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessTempCarFeeConfigAttrInfo(Map businessTempCarFeeConfigAttrInfo) throws DAOException;



    /**
     * 查询临时车收费标准属性信息（business过程）
     * 根据bId 查询临时车收费标准属性信息
     * @param info bId 信息
     * @return 临时车收费标准属性信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessTempCarFeeConfigAttrInfo(Map info) throws DAOException;




    /**
     * 保存 临时车收费标准属性信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveTempCarFeeConfigAttrInfoInstance(Map info) throws DAOException;




    /**
     * 查询临时车收费标准属性信息（instance过程）
     * 根据bId 查询临时车收费标准属性信息
     * @param info bId 信息
     * @return 临时车收费标准属性信息
     * @throws DAOException DAO异常
     */
    List<Map> getTempCarFeeConfigAttrInfo(Map info) throws DAOException;



    /**
     * 修改临时车收费标准属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateTempCarFeeConfigAttrInfoInstance(Map info) throws DAOException;


    /**
     * 查询临时车收费标准属性总数
     *
     * @param info 临时车收费标准属性信息
     * @return 临时车收费标准属性数量
     */
    int queryTempCarFeeConfigAttrsCount(Map info);

}
