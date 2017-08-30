package com.java110.merchant.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.ProtocolUtil;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantMember;
import com.java110.merchant.dao.IMerchantMemberServiceDao;
import com.java110.merchant.smo.IMerchantMemberServiceSMO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商户成员
 * Created by wuxw on 2017/8/30.
 */
public class MerchantMemberServiceSMOImpl implements IMerchantMemberServiceSMO {

    @Autowired
    IMerchantMemberServiceDao iMerchantMemberServiceDao;


    @Override
    public String queryMerchantMember(MerchantMember merchantMember) throws Exception {

        LoggerEngine.debug("商户成员信息查询入参：" + JSONObject.toJSONString(merchantMember));

        if(merchantMember == null || StringUtils.isBlank(merchantMember.getMerchantId()) ){
            throw new IllegalArgumentException("客户信息查询入参为空，merchantId 为空 "+JSONObject.toJSONString(merchantMember));
        }
        MerchantMember newMerchantMember = iMerchantMemberServiceDao.queryDataToMerchantMember(merchantMember);

        if(newMerchantMember == null){
            return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"未找到商户成员信息",null);
        }

        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功", JSONObject.parseObject(JSONObject.toJSONString(newMerchantMember)));
    }


    public IMerchantMemberServiceDao getiMerchantMemberServiceDao() {
        return iMerchantMemberServiceDao;
    }

    public void setiMerchantMemberServiceDao(IMerchantMemberServiceDao iMerchantMemberServiceDao) {
        this.iMerchantMemberServiceDao = iMerchantMemberServiceDao;
    }
}
