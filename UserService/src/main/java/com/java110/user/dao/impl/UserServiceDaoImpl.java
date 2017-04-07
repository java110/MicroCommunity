package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.user.dao.IUserServiceDao;
import com.java110.common.log.LoggerEngine;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.dao.BaseServiceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 用户服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */

/**
 * 用户信息实现工程
 * Created by wuxw on 2016/12/27.
 */
@Service("userServiceDaoImpl")
@Transactional
public class UserServiceDaoImpl extends BaseServiceDao implements IUserServiceDao {



    /**
     * 保存用户基本信息
     * 功能只用与保存用户处理
     * @param userInfo 用户基本信息
     * @return
     */
    @Override
    public String saveDataToBoCust(String userInfo) {

        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoCust】保存数据入参 : " + userInfo);
        Map<String,Object> userMap = null;
        String returnInfo = null;
        try {
            userMap = this.simpleValidateJSONReturnMap(userInfo);

            int saveFlag = sqlSessionTemplate.insert("UserServiceDAOImpl.saveDataToBoCust",userMap);

            if(saveFlag > 0){
                returnInfo = ProtocolUtil.createResultMsg(ResponseConstant.RESULT_CODE_SUCCESS,"成功",null);
            }else{
                returnInfo = ProtocolUtil.createResultMsg(ResponseConstant.RESULT_CODE_ERROR,"失败",null);
            }
        }catch(RuntimeException e){
            LoggerEngine.error("----【UserServiceDAOImpl.saveDataToBoCust】保存数据异常 : " ,e);
            return e.getMessage();
        }
        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoCust】保存数据出参 : " + returnInfo);
        return returnInfo;
    }

    /**
     * 保存用户属性（过程表）
     * 批量保存传入参数必须一个JSONArray to string
     * @param boCustAttr 用户属性
     * @return
     * @throws RuntimeException
     */
    @Override
    public String saveDataToBoCustAttr(String boCustAttr) throws RuntimeException {

        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoCustAttr】保存数据入参 : " + boCustAttr);
        List<Map> reqList = null;
        String returnInfo = null;
        try{
            reqList = this.simpleValidateJSONArrayReturnList(boCustAttr);
        }catch (RuntimeException e){
            return e.getMessage();
        }
        //为了保险起见，再测检测reqList 是否有值
        if(reqList == null || reqList.size() == 0){
            returnInfo = ProtocolUtil.createResultMsg(ResponseConstant.RESULT_CODE_PARAM_ERROR,"入参错误",null);
            LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoCustAttr】保存数据出错 : " + returnInfo);
            return returnInfo;
        }
        Map boCustAttrMap = null;
        int saveFlag = 0;
        for(int attrIndex = 0 ; attrIndex < reqList.size();attrIndex++){
            boCustAttrMap = reqList.get(attrIndex);
            saveFlag = sqlSessionTemplate.insert("UserServiceDAOImpl.saveDataToBoCustAttr",boCustAttrMap);
            if(saveFlag < 1){ //只要一个保存失败，抛异常回退
                LoggerEngine.error("----【UserServiceDAOImpl.saveDataToBoCustAttr】保存数据异常 : " + boCustAttrMap.toString());
                throw new RuntimeException(CommonConstant.SAVE_DATA_ERROR+"保存数据失败："+boCustAttrMap.toString());
            }
        }
        returnInfo = ProtocolUtil.createResultMsg(ResponseConstant.RESULT_CODE_SUCCESS,"成功",null);
        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoCustAttr】保存数据出参 : " + returnInfo);
        return returnInfo;
    }

    /**
     * 同时保存客户基本信息和客户属性
     * @param boCustInfo 用户信息
     * @return
     * @throws RuntimeException
     */
    @Override
    public String saveDataToBoCustAndBoCustAttr(String boCustInfo) throws RuntimeException {

        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoCustAndBoCustAttr】保存数据入参"+boCustInfo);
        JSONObject reqJson = null ;
        String returnInfo = null;
        //报文校验是否符合要求
        try{
            reqJson = this.simpleValidateJSON(boCustInfo);
        }catch (RuntimeException e){
            return e.getMessage();
        }
        //保存用户基本信息
        if(reqJson!= null && reqJson.containsKey("boCust")){
            String boCustJSON = reqJson.getJSONObject("boCust").toJSONString();
            returnInfo = this.saveDataToBoCust(boCustJSON);
            //解析返回内容
        }

        return null;
    }

    @Override
    public String saveDataToCust(String cust) throws RuntimeException {
        return null;
    }

    @Override
    public String saveDataToCustAttr(String custAttr) throws RuntimeException {
        return null;
    }

    @Override
    public String saveDataToCustAndCustAttr(String custInfo) throws RuntimeException {
        return null;
    }

    @Override
    public String updateDataToCust(String cust) throws RuntimeException {
        return null;
    }

    @Override
    public String updateDataToCustAttr(String custAttr) throws RuntimeException {
        return null;
    }

    @Override
    public String updateDataToCustAndCustAttr(String custInfo) throws RuntimeException {
        return null;
    }

    @Override
    public String queryDataToCust(String cust) throws RuntimeException {
        return null;
    }

    @Override
    public String queryDataToCustAttr(String custAttr) throws RuntimeException {
        return null;
    }

    @Override
    public String queryDataToCustAndCustAttr(String custInfo) throws RuntimeException {
        return null;
    }
}
