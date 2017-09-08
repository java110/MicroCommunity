package com.java110.merchant.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.merchant.BoMerchantMember;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantMember;
import com.java110.merchant.dao.IMerchantMemberServiceDao;
import org.springframework.stereotype.Service;

/**
 * Created by wuxw on 2017/8/30.
 */
@Service("merchantMemberServiceDaoImpl")
public class MerchantMemberServiceDaoImpl extends BaseServiceDao implements IMerchantMemberServiceDao {

    /**
     * 查询商户成员信息
     * @param merchantMember
     * @return
     * @throws RuntimeException
     */
    @Override
    public MerchantMember queryDataToMerchantMember(MerchantMember merchantMember) throws RuntimeException {
            //为了保险起见，再测检测reqList 是否有值
            if(merchantMember == null){
                LoggerEngine.debug("----【MerchantMemberServiceDaoImpl.queryDataToMerchantMember】查询数据出错 空" );
                throw new IllegalArgumentException("请求参数错误，merchantMember : 空");
            }

        LoggerEngine.debug("----【MerchantMemberServiceDaoImpl.queryDataToMerchantMember】查询数据入参 : " + JSONObject.toJSONString(merchantMember));


        MerchantMember newMerchantMember  = sqlSessionTemplate.selectOne("merchantMemberServiceDaoImpl.queryDataToMerchantMember",merchantMember);

        LoggerEngine.debug("----【MerchantMemberServiceDaoImpl.queryDataToMerchantMember】保存数据出参 :newMerchant " + JSONObject.toJSONString(newMerchantMember));

        return newMerchantMember;
    }

    /**
     * 保存过程数据
     * @param boMerchantMember 成员信息
     * @return
     * @throws RuntimeException
     */
    @Override
    public long saveDataToBoMerchantMember(BoMerchantMember boMerchantMember) throws RuntimeException {

        //为了保险起见，再测检测reqList 是否有值
        if(boMerchantMember == null){
            LoggerEngine.debug("----【MerchantMemberServiceDaoImpl.saveDataToBoMerchantMember】查询数据出错 空" );
            throw new IllegalArgumentException("请求参数错误，boMerchantMember : 空");
        }

        LoggerEngine.debug("----【MerchantMemberServiceDaoImpl.saveDataToBoMerchantMember】保存数据入参 : "
                + JSONObject.toJSONString(boMerchantMember));

        long rownum  = sqlSessionTemplate.insert("merchantMemberServiceDaoImpl.saveDataToBoMerchantMember",boMerchantMember);

        LoggerEngine.debug("----【MerchantMemberServiceDaoImpl.saveDataToBoMerchantMember】保存数据记录数 : " + rownum);
        return rownum;
    }

    /**
     * 根据过程数据 保存实例数据
     * @param boMerchantMember
     * @return
     * @throws RuntimeException
     */
    public long saveDataToMerchant(BoMerchantMember boMerchantMember) throws RuntimeException{

        //为了保险起见，再测检测reqList 是否有值
        if(boMerchantMember == null){
            LoggerEngine.debug("----【MerchantMemberServiceDaoImpl.saveDataToMerchant】查询数据出错 空" );
            throw new IllegalArgumentException("请求参数错误，boMerchantMember : 空");
        }

        LoggerEngine.debug("----【MerchantMemberServiceDaoImpl.saveDataToMerchant】保存数据入参 : "
                + JSONObject.toJSONString(boMerchantMember));

        long rownum  = sqlSessionTemplate.insert("merchantMemberServiceDaoImpl.saveDataToMerchant",boMerchantMember);

        LoggerEngine.debug("----【MerchantMemberServiceDaoImpl.saveDataToMerchant】保存数据记录数 : " + rownum);
        return rownum;
    }


    /**
     * 根据过程数据失效实例数据
     * @param boMerchantMember
     * @return
     * @throws RuntimeException
     */
    public long deleteDataToMerchant(BoMerchantMember boMerchantMember) throws RuntimeException{
        //为了保险起见，再测检测reqList 是否有值
        if(boMerchantMember == null){
            LoggerEngine.debug("----【MerchantMemberServiceDaoImpl.deleteDataToMerchant】查询数据出错 空" );
            throw new IllegalArgumentException("请求参数错误，boMerchantMember : 空");
        }

        LoggerEngine.debug("----【MerchantMemberServiceDaoImpl.deleteDataToMerchant】删除数据入参 : "
                + JSONObject.toJSONString(boMerchantMember));

        long rownum  = sqlSessionTemplate.update("merchantMemberServiceDaoImpl.deleteDataToMerchant",boMerchantMember);

        LoggerEngine.debug("----【MerchantMemberServiceDaoImpl.deleteDataToMerchant】删除数据记录数 : " + rownum);
        return rownum;

    }
}
