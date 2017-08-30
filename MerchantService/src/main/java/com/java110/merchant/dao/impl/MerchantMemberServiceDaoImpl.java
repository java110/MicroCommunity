package com.java110.merchant.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantMember;
import com.java110.merchant.dao.IMerchantMemberServiceDao;

/**
 * Created by wuxw on 2017/8/30.
 */
public class MerchantMemberServiceDaoImpl extends BaseServiceDao implements IMerchantMemberServiceDao {

    /**
     * 查询商户成员信息
     * @param merchantMember
     * @return
     * @throws RuntimeException
     */
    @Override
    public MerchantMember queryDataToMerchantMember(MerchantMember merchantMember) throws RuntimeException {
            LoggerEngine.debug("----【MerchantMemberServiceDaoImpl.queryDataToMerchantMember】查询数据入参 : " + JSONObject.toJSONString(merchantMember));
            //为了保险起见，再测检测reqList 是否有值
            if(merchantMember == null){
                LoggerEngine.debug("----【MerchantMemberServiceDaoImpl.queryDataToMerchantMember】查询数据出错 : " + JSONObject.toJSONString(merchantMember));
                throw new IllegalArgumentException("请求参数错误，merchant : " + JSONObject.toJSONString(merchantMember));
            }

            MerchantMember newMerchantMember  = sqlSessionTemplate.selectOne("merchantMemberServiceDaoImpl.queryDataToMerchantMember",merchantMember);

            LoggerEngine.debug("----【MerchantMemberServiceDaoImpl.queryDataToMerchantMember】保存数据出参 :newMerchant " + JSONObject.toJSONString(newMerchantMember));

            return newMerchantMember;
    }
}
