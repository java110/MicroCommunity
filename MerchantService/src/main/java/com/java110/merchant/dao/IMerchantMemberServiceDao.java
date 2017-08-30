package com.java110.merchant.dao;

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
}
