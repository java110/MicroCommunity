package com.java110.merchant.dao;

import com.java110.entity.merchant.BoMerchantMember;
import com.java110.entity.merchant.MerchantMember;

/**
 * Created by wuxw on 2017/8/30.
 */
public interface IMerchantMemberServiceDao {


    /**
     * 查询商户成员基本信息（一般没用，就算有用）
     * @param merchantMember
     * @return
     * @throws RuntimeException
     */
    public MerchantMember queryDataToMerchantMember(MerchantMember merchantMember) throws RuntimeException ;


    /**
     * 保存过程数据
     * @param boMerchantMember 成员信息
     * @return
     * @throws RuntimeException
     */
    public long saveDataToBoMerchantMember(BoMerchantMember boMerchantMember) throws RuntimeException;

    /**
     * 根据过程数据 保存实例数据
     * @param boMerchantMember
     * @return
     * @throws RuntimeException
     */
    public long saveDataToMerchant(BoMerchantMember boMerchantMember) throws RuntimeException;

    /**
     * 根据过程数据失效实例数据
     * @param boMerchantMember
     * @return
     * @throws RuntimeException
     */
    public long deleteDataToMerchant(BoMerchantMember boMerchantMember) throws RuntimeException;
}
