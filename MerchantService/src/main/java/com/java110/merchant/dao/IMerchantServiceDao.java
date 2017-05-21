package com.java110.merchant.dao;


import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;

/**
 * 商户组件内部之间使用，没有给外围系统提供服务能力
 * 商户服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IMerchantServiceDao {

    /**
     * 保存商户基本信息(过程表)
     * @param boMerchant 商户基本信息
     * @return
     */
    public int saveDataToBoMerchant(BoMerchant boMerchant) throws RuntimeException;

    /**
     * 保存商户属性(过程表)
     * @param boMerchantAttr 商户属性
     * @return
     * @throws RuntimeException
     */
    public int saveDataToBoMerchantAttr(BoMerchantAttr boMerchantAttr) throws RuntimeException ;

    /**
     *  同事保存商户基本信息和属性(过程表)
     * @param boMerchantInfo 商户信息
     * @return
     * @throws RuntimeException
     */
    public String saveDataToBoMerchantAndBoMerchantAttr(String boMerchantInfo) throws RuntimeException;

    /**
     * 保存商户基本信息
     * @param merchant
     * @return
     * @throws RuntimeException
     */
    public int saveDataToMerchant(Merchant merchant) throws RuntimeException;

    /**
     *  保存商户属性
     * @param merchantAttr
     * @return
     * @throws RuntimeException
     */
    public int saveDataToMerchantAttr(MerchantAttr merchantAttr) throws RuntimeException;

    /**
     * 删除商户基本信息（实例数据）
     * @param merchant
     * @return
     * @throws RuntimeException
     */
    public int deleteDataToMerchant(Merchant merchant) throws RuntimeException;

    /**
     *  删除商户属性（实例数据）
     * @param merchantAttr
     * @return
     * @throws RuntimeException
     */
    public int deleteDataToMerchantAttr(MerchantAttr merchantAttr) throws RuntimeException;

    /**
     * 同事保存商户基本信息和属性
     * @param merchantInfo
     * @return
     * @throws RuntimeException
     */
    public String saveDataToMerchantAndMerchantAttr(String merchantInfo) throws RuntimeException;


    /**
     * 更新商户基本信息
     * @param merchant
     * @return
     * @throws RuntimeException
     */
    public String updateDataToMerchant(String merchant) throws RuntimeException;

    /**
     *  更新商户属性
     * @param merchantAttr
     * @return
     * @throws RuntimeException
     */
    public String updateDataToMerchantAttr(String merchantAttr) throws RuntimeException;

    /**
     * 同事更新商户基本信息和属性
     * @param merchantInfo
     * @return
     * @throws RuntimeException
     */
    public String updateDataToMerchantAndMerchantAttr(String merchantInfo) throws RuntimeException;



    /**
     * 查询商户基本信息（一般没用，就算有用）
     * @param merchant
     * @return
     * @throws RuntimeException
     */
    public Merchant queryDataToMerchant(Merchant merchant) throws RuntimeException ;



    /**
     *  查询商户属性
     * @param merchantAttr
     * @return
     * @throws RuntimeException
     */
    public List<MerchantAttr> queryDataToMerchantAttr(MerchantAttr merchantAttr) throws RuntimeException;

    /**
     * 查询保存商户基本信息和属性
     * @param merchantInfo
     * @return
     * @throws RuntimeException
     */
    public String queryDataToMerchantAndMerchantAttr(String merchantInfo) throws RuntimeException;

    /**
     *
     * 查询 客户基本信息（过程表bo_merchant）
     *
     * @param BoMerchant
     * @return
     * @throws Exception
     */
    public List<BoMerchant> queryBoMerchant(BoMerchant BoMerchant) throws Exception;

    /**
     *
     * 查询 客户属性信息（过程表 bo_merchant_attr）
     *
     * @param BoMerchantAttr
     * @return
     * @throws Exception
     */
    public List<BoMerchantAttr> queryBoMerchantAttr(BoMerchantAttr BoMerchantAttr) throws Exception;


}