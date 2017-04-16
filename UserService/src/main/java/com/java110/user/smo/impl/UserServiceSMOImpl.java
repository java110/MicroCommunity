package com.java110.user.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.ProtocolUtil;
import com.java110.entity.user.BoCust;
import com.java110.entity.user.BoCustAttr;
import com.java110.entity.user.Cust;
import com.java110.feign.base.IPrimaryKeyService;
import com.java110.user.dao.IUserServiceDao;
import com.java110.user.smo.IUserServiceSMO;
import com.java110.core.base.smo.BaseServiceSMO;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务信息管理业务信息实现
 * Created by wuxw on 2017/4/5.
 */
@Service("userServiceSMOImpl")
@Transactional
public class UserServiceSMOImpl extends BaseServiceSMO implements IUserServiceSMO {

    @Autowired
    IPrimaryKeyService iPrimaryKeyService;

    @Autowired
    IUserServiceDao iUserServiceDao;

    //新增用户
    private final static String USER_ACTION_ADD = "ADD";

    //新增用户
    private final static String USER_ACTION_KIP = "KIP";

    //新增用户
    private final static String USER_ACTION_DEL = "DEL";

    /**
     * 保存用户信息
     *
     * @param userInfoJson 入参为用户信息json传
     * @return
     */
    public String saveUser(String userInfoJson) throws Exception{

        JSONObject reqUserJSON = null;
        try {
            reqUserJSON = this.simpleValidateJSON(userInfoJson);
            //boCust增加Action (动作)
            if (reqUserJSON.containsKey("boCust")) {
                JSONObject boCust = reqUserJSON.getJSONObject("boCust");
                boCust.put("state", USER_ACTION_ADD);
            }
            //boCustAttr增加Action（动作）
            if (reqUserJSON.containsKey("boCustAttr")) {
                JSONArray boCustAttrs = reqUserJSON.getJSONArray("boCustAttr");

                for (int attrIndex = 0; attrIndex < boCustAttrs.size(); attrIndex++) {
                    JSONObject boCustAttr = boCustAttrs.getJSONObject(attrIndex);
                    boCustAttr.put("state", USER_ACTION_ADD);
                }
            }
        } catch (RuntimeException e) {
            //返回异常信息
            return e.getMessage();
        }
        return soUserService(reqUserJSON);
    }


    /**
     * 所有服务处理类
     *
     * @param userInfoJson
     * @return
     */
    public String soUserService(JSONObject userInfoJson) throws Exception{
        LoggerEngine.debug("用户服务操作客户入参：" + userInfoJson);
        JSONObject paramJson = new JSONObject();

        JSONObject resultInfo = null;

        //存放生成的custId 主键为 custId-1 71000010100
        Map custIdKey = new HashMap();

        if (userInfoJson == null){
            throw new IllegalArgumentException("soUserService 入参 为空"+userInfoJson);
        }
         // 客户信息处理
            if(userInfoJson.containsKey("boCust")){
                JSONArray boCusts = userInfoJson.getJSONArray("boCust");
                JSONObject boCustObj = new JSONObject();
                boCustObj.put("boCust",boCusts);
                String returnSaveBoCust = this.soBoCust(boCustObj.toJSONString(),custIdKey);

                if(!ProtocolUtil.validateReturnJson(returnSaveBoCust,paramJson)){

                    throw new RuntimeException("保存 bo_cust 失败："+boCustObj+(paramJson != null
                            && paramJson.containsKey("RESULT_MSG")?paramJson.getString("RESULT_MSG"):"未知异常"));
                }

                resultInfo = paramJson.getJSONObject("RESULT_INFO");
            }

            //客户属性信息处理
            if(userInfoJson.containsKey("boCustAttr")){

                JSONArray boCustAttrs = userInfoJson.getJSONArray("boCustAttr");
                //首先对custId 进行处理
                if(custIdKey != null && custIdKey.size() > 0 ){
                    for(int boCustAttrIndex = 0 ; boCustAttrIndex < boCustAttrs.size();boCustAttrIndex++){
                       JSONObject boCustAttr = boCustAttrs.getJSONObject(boCustAttrIndex);
                       boCustAttr.put("custId",custIdKey.get("custId"+boCustAttr.getString("custId")));
                    }
                }
                JSONObject boCustAttrObj = new JSONObject();
                boCustAttrObj.put("boCustAttr",boCustAttrs);
                String returnSaveBoCustAttr = soBoCustAttr(boCustAttrObj.toJSONString());

                if(!ProtocolUtil.validateReturnJson(returnSaveBoCustAttr,paramJson)){

                    throw new RuntimeException("保存 bo_cust 失败："+boCustAttrObj+(paramJson != null
                            && paramJson.containsKey("RESULT_MSG")?paramJson.getString("RESULT_MSG"):"未知异常"));
                }
            }

        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",resultInfo);

    }

    /**
     * {
     *     boCust:[{},{}]
     * }
     * 客户信心处理
     *
     *
     * @param boCusts
     * @return 成功 会带上处理客户的客户ID
     * {'RESULT_CODE':'0000','RESULT_MSG':'成功','RESULT_INFO':{'custId':'7000123,718881991'}}
     * @throws Exception
     */
    public String soBoCust(String boCusts) throws Exception{
        return soBoCust(boCusts,null);
    }

    /**
     * 将生成的custId 封装在map中返回
     * ...
     * custIdKey.put("custId-1","710020404040");
     *
     * ...
     *
     * key 为 custId 加原前的值
     *
     * custIdKey 如果为空不做处理
     * @param boCusts 客户信息
     * @param custIdKey custIdKeymap
     * @return
     * @throws Exception
     */
    public String soBoCust(String boCusts,Map custIdKey) throws Exception{
        // 将 jsonArray 转为list<BoCust> 对象
        JSONObject jsonObject = JSONObject.parseObject(boCusts);

        JSONObject resultInfo = new JSONObject();

        String custIds = "";

        List<BoCust> boCustList = JSONObject.parseArray(jsonObject.getJSONArray("boCust").toJSONString(), BoCust.class);

        Collections.sort(boCustList);
        //保存数据

        for(BoCust boCust : boCustList){
//        for(int boCustIndex = 0 ; boCustIndex < boCustList.size();boCustIndex++){
//            BoCust boCust = boCustList.get(boCustIndex);
            String custId = boCust.getBoId();
            //如果客户ID小于0 ，则自己生成客户ID,这个只有在有 主键生成服务时使用，否则为了防止出错，需要前段调用时需要生成custId
            if(StringUtils.isBlank(custId) || custId.startsWith("-") ){
                /*JSONObject data = new JSONObject();
                data.put("type","CUST_ID");
                //要求接口返回 {"RESULT_CODE":"0000","RESULT_INFO":{"user_id":"7020170411000041"},"RESULT_MSG":"成功"}
                String custIdJSONStr = iPrimaryKeyService.queryPrimaryKey(data.toJSONString());
                JSONObject custIdJSONTmp = JSONObject.parseObject(custIdJSONStr);
                if(custIdJSONTmp.containsKey("RESULT_CODE")
                        && ProtocolUtil.RETURN_MSG_SUCCESS.equals(custIdJSONTmp.getString("RESULT_CODE"))
                        && custIdJSONTmp.containsKey("RESULT_INFO")){
                    //从接口生成custId
                    custId = NumberUtils.toInt(custIdJSONTmp.getJSONObject("RESULT_INFO").getString("CUST_ID"),-1);
                }*/

                custId = this.queryPrimaryKey(iPrimaryKeyService,"CUST_ID");

                //将 新生成的custId保存至 map中 custId-1 custId-2 主键方式存入
                if(custIdKey != null){
                    custIdKey.put("custId"+boCust.getCustId(),custId);
                }
            }

            boCust.setCustId(custId);

            //保存数据至 bo_cust 表中
            int saveBoCustFlag = iUserServiceDao.saveDataToBoCust(boCust);

            if(saveBoCustFlag < 1){ // 如果没有保存成功，抛出异常，有事物 回退事物
                throw new RuntimeException("保存过程[bo_cust]数据失败,印象记录数为"+saveBoCustFlag+"，boCust : "+boCust);
            }
            //建档 处理 实例数据
            int saveCustFlag = 0;
            if("ADD".equals(boCust.getState())){
               /* List<BoCust> boCustsTmp = boCustList;
                //在增加之间首先要判断是否有相应删的动作
//                for(BoCust boCustTmp : boCustsTmp){
                for(int boCustTmpIndex = boCustIndex+1;boCustTmpIndex < boCustsTmp.size();boCustTmpIndex++){
                    BoCust boCustTmp = boCustsTmp.get(boCustTmpIndex);
                    if(StringUtils.isNotBlank(boCust.getCustId())
                            && boCust.getCustId().equals(boCustTmp.getCustId())
                            &&"DEL".equals(boCustTmp.getState())){
                        //先调用删除客户信息
                        saveCustFlag = iUserServiceDao.deleteDataToCust(boCust.convert());

                        if(saveCustFlag < 1){
                            throw new RuntimeException("删除实例[cust]数据失败，影响记录数为"+saveCustFlag+", cust : "+boCust.convert());
                        }

                        //则把已经删除过的从list中删除，以防重复删除
                        boCustList.remove(boCustTmp);

                    }
                }*/

                saveCustFlag  = iUserServiceDao.saveDataToCust(boCust.convert());
            }else if("DEL".equals(boCust.getState())){
                saveCustFlag = iUserServiceDao.deleteDataToCust(boCust.convert());
            }else if("KIP".equals(boCust.getState())){
                //按理这里到不了，KIP表示实例数据不变，所以这里默认写成1 认为是成功
                saveCustFlag = 1;
            }else{
                //这里单独抛出异常，不走下面统一异常抛出，是为了说明更具体点
                throw new RuntimeException("入参错误boCust 的 state 目前只支持 [ADD,DEL,KIP] , boCust : " +boCust);
            }


            if(saveCustFlag < 1){
                throw new RuntimeException("保存实例[cust]数据失败，影响记录数为"+saveCustFlag+", cust : "+boCust.convert());
            }

            custIds +=","+custId;

        }

        //去除第一个逗号
        if (custIds.length()>0){
            custIds = custIds.substring(1);
        }

        resultInfo.put("custId",custIds);

        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",resultInfo);
    }

    /**
     * 注意在调用这个接口时，相应的客户信息必须存在
     *
     *
     * 客户信息属性处理
     * 协议：
     *{
     *     boCustAttr:[{},{}]
     * }
     * @param boCustAttrs
     * @return
     * @throws Exception
     */
    @Override
    public String soBoCustAttr(String boCustAttrs) throws Exception {

        //这里可以加入基本客户信息是否存在的校验，暂时没有必要实现

        // 将 jsonArray 转为list<BoCust> 对象
        JSONObject jsonObject = JSONObject.parseObject(boCustAttrs);

        List<BoCustAttr> boCustAttrList = JSONObject.parseArray(jsonObject.getJSONArray("boCustAttr").toJSONString(), BoCustAttr.class);

        //先拍个序 先处理DEL 再处理ADD
        Collections.sort(boCustAttrList);
        //保存数据

        for(BoCustAttr boCustAttr : boCustAttrList) {

            //保存数据至 bo_cust_attr 表中
            int saveBoCustAttrFlag = iUserServiceDao.saveDataToBoCustAttr(boCustAttr);

            if(saveBoCustAttrFlag < 1){ // 如果没有保存成功，抛出异常，有事物 回退事物
                throw new RuntimeException("保存过程[bo_cust_attr]数据失败,印象记录数为"+saveBoCustAttrFlag+"，boCustAttr : "+boCustAttr);
            }

            //建档 处理 实例数据
            int saveCustAttrFlag = 0;
            if("ADD".equals(boCustAttr.getState())){
                saveCustAttrFlag  = iUserServiceDao.saveDataToCustAttr(boCustAttr.convert());
            }else if("DEL".equals(boCustAttr.getState())){
                saveCustAttrFlag = iUserServiceDao.deleteDataToCustAttr(boCustAttr.convert());
            }else if("KIP".equals(boCustAttr.getState())){
                //按理这里到不了，KIP表示实例数据不变，所以这里默认写成1 认为是成功
                saveCustAttrFlag = 1;
            }else{
                //这里单独抛出异常，不走下面统一异常抛出，是为了说明更具体点
                throw new RuntimeException("入参错误boCustAttr 的 state 目前只支持 [ADD,DEL,KIP] , boCust : " +boCustAttr);
            }

            if(saveCustAttrFlag < 1){
                throw new RuntimeException("保存实例[cust_attr]数据失败，影响记录数为"+saveCustAttrFlag+", cust : "+boCustAttr.convert());
            }
        }

        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",null);
    }


    /**
     * 查询客户信息
     * 包括 基本信息cust 和 属性信息 custAttr
     * @param cust
     * @return
     * @throws Exception
     */
    public String queryCust(Cust cust) throws Exception{
        LoggerEngine.debug("客户信息查询入参：" + cust);
        if(cust == null || StringUtils.isBlank(cust.getCustId()) ){
            throw new IllegalArgumentException("客户信息查询入参为空，custId 为空 "+cust);
        }
        Cust newCust = iUserServiceDao.queryDataToCust(cust);

        if(newCust == null){
            return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"未找到用户信息",null);
        }

        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",JSONObject.parseObject(JSONObject.toJSONString(newCust)));
    }

    public IPrimaryKeyService getiPrimaryKeyService() {
        return iPrimaryKeyService;
    }

    public void setiPrimaryKeyService(IPrimaryKeyService iPrimaryKeyService) {
        this.iPrimaryKeyService = iPrimaryKeyService;
    }

    public IUserServiceDao getiUserServiceDao() {
        return iUserServiceDao;
    }

    public void setiUserServiceDao(IUserServiceDao iUserServiceDao) {
        this.iUserServiceDao = iUserServiceDao;
    }
}
