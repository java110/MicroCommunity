package com.java110.store.dao;


import com.java110.common.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * BusinessType组件内部之间使用，没有给外围系统提供服务能力
 * BusinessType服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IC_business_typeServiceDao {

    /**
     * 保存 BusinessType信息
     * @param businessC_business_typeInfo BusinessType信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessC_business_typeInfo(Map businessC_business_typeInfo) throws DAOException;



    /**
     * 查询BusinessType信息（business过程）
     * 根据bId 查询BusinessType信息
     * @param info bId 信息
     * @return BusinessType信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessC_business_typeInfo(Map info) throws DAOException;




    /**
     * 保存 BusinessType信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveC_business_typeInfoInstance(Map info) throws DAOException;




    /**
     * 查询BusinessType信息（instance过程）
     * 根据bId 查询BusinessType信息
     * @param info bId 信息
     * @return BusinessType信息
     * @throws DAOException DAO异常
     */
    List<Map> getC_business_typeInfo(Map info) throws DAOException;



    /**
     * 修改BusinessType信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateC_business_typeInfoInstance(Map info) throws DAOException;


    /**
     * 查询BusinessType总数
     *
     * @param info BusinessType信息
     * @return BusinessType数量
     */
    int queryC_business_typesCount(Map info);

}
