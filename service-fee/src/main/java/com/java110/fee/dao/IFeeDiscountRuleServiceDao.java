package com.java110.fee.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 费用折扣规则组件内部之间使用，没有给外围系统提供服务能力
 * 费用折扣规则服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IFeeDiscountRuleServiceDao {


    /**
     * 保存 费用折扣规则信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveFeeDiscountRuleInfo(Map info) throws DAOException;




    /**
     * 查询费用折扣规则信息（instance过程）
     * 根据bId 查询费用折扣规则信息
     * @param info bId 信息
     * @return 费用折扣规则信息
     * @throws DAOException DAO异常
     */
    List<Map> getFeeDiscountRuleInfo(Map info) throws DAOException;



    /**
     * 修改费用折扣规则信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateFeeDiscountRuleInfo(Map info) throws DAOException;


    /**
     * 查询费用折扣规则总数
     *
     * @param info 费用折扣规则信息
     * @return 费用折扣规则数量
     */
    int queryFeeDiscountRulesCount(Map info);

}
