package com.java110.merchant.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.java110.common.constant.StateConstant;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.entity.merchant.BoMerchantMember;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantMember;
import com.java110.merchant.dao.IMerchantMemberServiceDao;
import com.java110.merchant.smo.IMerchantMemberServiceSMO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户成员
 * Created by wuxw on 2017/8/30.
 */
public class MerchantMemberServiceSMOImpl extends BaseServiceSMO implements IMerchantMemberServiceSMO {

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



    /**
     * 保存商户成员信息
     *
     * @param merchantInfoJson 入参为商户成员信息json传
     * @return
     */
    public String saveMerchantMember(String merchantInfoJson) throws Exception{

        JSONObject reqMerchantJSON = null;
        try {
            reqMerchantJSON = this.simpleValidateJSON(merchantInfoJson);
            //boMerchantMember增加Action (动作)
            if (reqMerchantJSON.containsKey("boMerchantMember")) {
                JSONObject boMerchantMember = reqMerchantJSON.getJSONObject("boMerchantMember");
                boMerchantMember.put("state", StateConstant.STATE_ADD);
            }
        } catch (RuntimeException e) {
            //返回异常信息
            return e.getMessage();
        }
        return soMerchantMemberService(reqMerchantJSON);
    }


    /**
     * 所有服务处理类
     * {
     *
     *     'boMerchantMember':[{}],
     *     'boMerchantMemberAttr':[{}]
     * }
     *
     * 返回报文：
     *
     * {'RESULT_CODE':'0000','RESULT_MSG':'成功','RESULT_INFO':{'merchantId':'7000123,718881991'}}
     * @param merchantInfoJson
     * @return
     */
    public String soMerchantMemberService(JSONObject merchantInfoJson) throws Exception{
        LoggerEngine.debug("商户成员服务操作客户入参：" + merchantInfoJson);
        JSONObject paramJson = new JSONObject();

        JSONObject resultInfo = null;

        //存放生成的merchantId 主键为 merchantId-1 71000010100
        Map merchantIdKey = new HashMap();

        if (merchantInfoJson == null){
            throw new IllegalArgumentException("soMerchantMemberService 入参 为空"+merchantInfoJson);
        }
        // 客户信息处理 处理boMerchantMember节点
        doProcessBoMerchantMember(merchantInfoJson,paramJson,merchantIdKey,resultInfo);

        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",resultInfo);

    }

    /**
     *
     * 请求报文为：
     *
     * {
     "data": [
     {
     "actionTypeCd": "C1",
     "boMerchantMember": [{},{}],
     "boMerchantMemberAttr": [{ }, {}]
     },
     {
     "actionTypeCd": "C1",
     "boMerchantMember": [{},{}],
     "boMerchantMemberAttr": [{ }, {}]
     }
     ]
     }

     返回报文 ：

     { 'RESULT_CODE': '0000', 'RESULT_MSG': '成功', 'RESULT_INFO': {'merchant':[{'oldMerchantId':'-1','merchantId':'12345678'},{'oldMerchantId':'-2','merchantId':'12345678'}]} }
     * @param mInfoJson
     * @return
     * @throws Exception
     */
    @Override
    public String soMerchantMemberServiceForOrderService(JSONObject mInfoJson) throws Exception {

        Assert.isNull(mInfoJson,"data","请求报文缺少 data 节点，请检查");

        JSONArray merchantInfos = mInfoJson.getJSONArray("data");

        Assert.isNull(merchantInfos,"请求报文中data节点，没有子节点，data子节点应该为JSONArray,merchantInfos="+merchantInfos);

        JSONObject merchantInfoJ = new JSONObject();
        JSONArray resultMerchantIdArray = new JSONArray();
        for(int merchantInfoIndex = 0 ;merchantInfoIndex < merchantInfos.size();merchantInfoIndex ++){
            JSONObject merchantInfoJson = merchantInfos.getJSONObject(merchantInfoIndex);
            String soMerchantMemberServiceResult = this.soMerchantMemberService(merchantInfoJson);
            JSONObject resultInfo = new JSONObject();

            if(!ProtocolUtil.validateReturnJson(soMerchantMemberServiceResult,resultInfo)){
                throw new RuntimeException("客户信息受理失败，原因为："+resultInfo.getString(ProtocolUtil.RESULT_MSG));
            }
            if(resultInfo.getJSONObject(ProtocolUtil.RESULT_INFO) != null
                    && resultInfo.getJSONObject(ProtocolUtil.RESULT_INFO).containsKey("merchantId")) {

                String merchantIds = merchantInfoJ.getString("merchantId");
//                merchantIds += "," + resultInfo.getJSONObject(ProtocolUtil.RESULT_INFO).getString("merchantId");

//                merchantIds = merchantIds.startsWith(",") && merchantIds.length()>1 ? merchantIds.substring(1,merchantIds.length()):merchantIds;
                //merchantInfoJ.put("merchantId", merchantIds);
                JSONArray boMerchantMembers = merchantInfoJson.getJSONArray("boMerchantMember");

                Object merchantIdObj = JSONPath.eval(merchantInfoJson,"$.boMerchantMember[merchantId < '0'][0].merchantId");
                if(StringUtils.isNotBlank(merchantIds) && !ObjectUtils.isEmpty(merchantIdObj)) {

                    String[] allNewMerchantIds = merchantIds.split(",");
                    JSONObject newMerchantIdJson = null;
                    for (String merchantId : allNewMerchantIds) {
                        newMerchantIdJson = new JSONObject();
                        newMerchantIdJson.put("oldMerchantId",merchantIdObj);
                        newMerchantIdJson.put("merchantId",merchantId);
                        resultMerchantIdArray.add(newMerchantIdJson);
                    }

                }
            }

        }

        merchantInfoJ.put("merchant",resultMerchantIdArray);

        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",merchantInfoJ);
    }

    /**
     * {
     *     boMerchantMember:[{},{}]
     * }
     * 客户信心处理
     *
     *
     * @param boMerchantMembers
     * @return 成功 会带上处理客户的客户ID
     * {'RESULT_CODE':'0000','RESULT_MSG':'成功','RESULT_INFO':{'merchantId':'7000123,718881991'}}
     * @throws Exception
     */
    public String soBoMerchantMember(String boMerchantMembers) throws Exception{
        return soBoMerchantMember(boMerchantMembers,null);
    }

    /**
     * 将生成的merchantId 封装在map中返回
     * ...
     * merchantIdKey.put("merchantId-1","710020404040");
     *
     * ...
     *
     * key 为 merchantId 加原前的值
     *
     * merchantIdKey 如果为空不做处理
     * @param boMerchantMembers 客户信息
     * @param merchantIdKey merchantIdKeymap
     * @return
     * @throws Exception
     */
    public String soBoMerchantMember(String boMerchantMembers,Map merchantIdKey) throws Exception{
        // 将 jsonArray 转为list<BoMerchantMember> 对象
        JSONObject jsonObject = JSONObject.parseObject(boMerchantMembers);

        JSONObject resultInfo = new JSONObject();

        String merchantIds = "";

        List<BoMerchantMember> boMerchantMemberList = JSONObject.parseArray(jsonObject.getJSONArray("boMerchantMember").toJSONString(), BoMerchantMember.class);

        //保存数据

        for(BoMerchantMember boMerchantMember : boMerchantMemberList){

            String merchantId = boMerchantMember.getMerchantId();


            //保存数据至 bo_merchant 表中
            long saveBoMerchantMemberFlag = iMerchantMemberServiceDao.saveDataToBoMerchantMember(boMerchantMember);

            if(saveBoMerchantMemberFlag < 1){ // 如果没有保存成功，抛出异常，有事物 回退事物
                throw new RuntimeException("保存过程[bo_merchant]数据失败,影响记录数为"+saveBoMerchantMemberFlag+"，boMerchantMember : "+boMerchantMember);
            }
            //建档 处理 实例数据
            long saveMerchantFlag = 0;
            if(StateConstant.STATE_ADD.equals(boMerchantMember.getState())){
                saveMerchantFlag  = iMerchantMemberServiceDao.saveDataToMerchant(boMerchantMember);
            }else if(StateConstant.STATE_DEL.equals(boMerchantMember.getState())){
                saveMerchantFlag = iMerchantMemberServiceDao.deleteDataToMerchant(boMerchantMember);
            }else if(StateConstant.STATE_KIP.equals(boMerchantMember.getState())){
                //按理这里到不了，KIP表示实例数据不变，所以这里默认写成1 认为是成功
                saveMerchantFlag = 1;
            }else{
                //这里单独抛出异常，不走下面统一异常抛出，是为了说明更具体点
                throw new RuntimeException("入参错误boMerchantMember 的 state 目前只支持 [ADD,DEL,KIP] , boMerchantMember : " +boMerchantMember);
            }


            if(saveMerchantFlag < 1){
                throw new RuntimeException("保存实例[merchant]数据失败，影响记录数为"+saveMerchantFlag+", merchant : "+boMerchantMember);
            }

            merchantIds +=","+merchantId;

        }

        //去除第一个逗号
        if (merchantIds.length()>0){
            merchantIds = merchantIds.substring(1);
        }

        resultInfo.put("merchantId",merchantIds);

        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",resultInfo);
    }

    /**
     * 处理boMerchantMember 节点
     * @throws Exception
     */
    public void doProcessBoMerchantMember(JSONObject merchantInfoJson,JSONObject paramJson,Map merchantIdKey, JSONObject resultInfo) throws Exception{

        if(merchantInfoJson.containsKey("boMerchantMember")){
            JSONArray boMerchantMembers = merchantInfoJson.getJSONArray("boMerchantMember");
            JSONObject boMerchantMemberObj = new JSONObject();
            boMerchantMemberObj.put("boMerchantMember",boMerchantMembers);
            String returnSaveBoMerchantMember = this.soBoMerchantMember(boMerchantMemberObj.toJSONString(),merchantIdKey);

            if(!ProtocolUtil.validateReturnJson(returnSaveBoMerchantMember,paramJson)){

                throw new RuntimeException("保存 bo_merchant 失败："+boMerchantMemberObj+(paramJson != null
                        && paramJson.containsKey("RESULT_MSG")?paramJson.getString("RESULT_MSG"):"未知异常"));
            }

            resultInfo = paramJson.getJSONObject("RESULT_INFO");
        }
    }


    public IMerchantMemberServiceDao getiMerchantMemberServiceDao() {
        return iMerchantMemberServiceDao;
    }

    public void setiMerchantMemberServiceDao(IMerchantMemberServiceDao iMerchantMemberServiceDao) {
        this.iMerchantMemberServiceDao = iMerchantMemberServiceDao;
    }
}
