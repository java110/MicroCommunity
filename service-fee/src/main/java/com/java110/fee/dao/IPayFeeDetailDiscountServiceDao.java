package com.java110.fee.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 缴费优惠组件内部之间使用，没有给外围系统提供服务能力
 * 缴费优惠服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IPayFeeDetailDiscountServiceDao {

    /**
     * 保存 缴费优惠信息
     * @param businessPayFeeDetailDiscountInfo 缴费优惠信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessPayFeeDetailDiscountInfo(Map businessPayFeeDetailDiscountInfo) throws DAOException;



    /**
     * 查询缴费优惠信息（business过程）
     * 根据bId 查询缴费优惠信息
     * @param info bId 信息
     * @return 缴费优惠信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessPayFeeDetailDiscountInfo(Map info) throws DAOException;




    /**
     * 保存 缴费优惠信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void savePayFeeDetailDiscountInfoInstance(Map info) throws DAOException;




    /**
     * 查询缴费优惠信息（instance过程）
     * 根据bId 查询缴费优惠信息
     * @param info bId 信息
     * @return 缴费优惠信息
     * @throws DAOException DAO异常
     */
    List<Map> getPayFeeDetailDiscountInfo(Map info) throws DAOException;



    /**
     * 修改缴费优惠信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updatePayFeeDetailDiscountInfoInstance(Map info) throws DAOException;


    /**
     * 查询缴费优惠总数
     *
     * @param info 缴费优惠信息
     * @return 缴费优惠数量
     */
    int queryPayFeeDetailDiscountsCount(Map info);

}
