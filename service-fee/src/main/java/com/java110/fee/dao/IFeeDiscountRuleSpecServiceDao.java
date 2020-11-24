package com.java110.fee.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 折扣规则配置组件内部之间使用，没有给外围系统提供服务能力
 * 折扣规则配置服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IFeeDiscountRuleSpecServiceDao {


    /**
     * 保存 折扣规则配置信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveFeeDiscountRuleSpecInfo(Map info) throws DAOException;




    /**
     * 查询折扣规则配置信息（instance过程）
     * 根据bId 查询折扣规则配置信息
     * @param info bId 信息
     * @return 折扣规则配置信息
     * @throws DAOException DAO异常
     */
    List<Map> getFeeDiscountRuleSpecInfo(Map info) throws DAOException;



    /**
     * 修改折扣规则配置信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateFeeDiscountRuleSpecInfo(Map info) throws DAOException;


    /**
     * 查询折扣规则配置总数
     *
     * @param info 折扣规则配置信息
     * @return 折扣规则配置数量
     */
    int queryFeeDiscountRuleSpecsCount(Map info);

}
