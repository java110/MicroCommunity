package com.java110.merchant.smo;

import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantMember;

/**
 * Created by wuxw on 2017/8/30.
 */
public interface IMerchantMemberServiceSMO {

    /**
     * 查询商户成员
     * 包括 基本信息merchant 和 属性信息 merchantAttr
     * @param merchantMember
     * @return
     * @throws Exception
     */
    public String queryMerchantMember(MerchantMember merchantMember) throws Exception;
}
