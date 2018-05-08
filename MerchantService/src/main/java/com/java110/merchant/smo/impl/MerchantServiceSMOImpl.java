package com.java110.merchant.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.java110.common.constant.StateConstant;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;
import com.java110.entity.order.BusiOrder;
import com.java110.feign.base.IPrimaryKeyService;
import com.java110.merchant.dao.IMerchantServiceDao;
import com.java110.merchant.smo.IMerchantServiceSMO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * 用户服务信息管理业务信息实现
 * Created by wuxw on 2017/4/5.
 */
@Service("merchantServiceSMOImpl")
@Transactional
public class MerchantServiceSMOImpl extends BaseServiceSMO implements IMerchantServiceSMO {

    @Autowired
    IPrimaryKeyService iPrimaryKeyService;

    @Autowired
    IMerchantServiceDao iMerchantServiceDao;

    //新增用户
    private final static String MERCHANT_ACTION_ADD = "ADD";

    //新增用户
    private final static String MERCHANT_ACTION_KIP = "KIP";

    //新增用户
    private final static String MERCHANT_ACTION_DEL = "DEL";

    /**
     * 保存用户信息
     *
     * @param merchantInfoJson 入参为用户信息json传
     * @return
     */
    public String saveMerchant(String merchantInfoJson) throws Exception{

        JSONObject reqMerchantJSON = null;
        try {
            reqMerchantJSON = this.simpleValidateJSON(merchantInfoJson);
            //boMerchant增加Action (动作)
            if (reqMerchantJSON.containsKey("boMerchant")) {
                JSONObject boMerchant = reqMerchantJSON.getJSONObject("boMerchant");
                boMerchant.put("state", MERCHANT_ACTION_ADD);
            }
            //boMerchantAttr增加Action（动作）
            if (reqMerchantJSON.containsKey("boMerchantAttr")) {
                JSONArray boMerchantAttrs = reqMerchantJSON.getJSONArray("boMerchantAttr");

                for (int attrIndex = 0; attrIndex < boMerchantAttrs.size(); attrIndex++) {
                    JSONObject boMerchantAttr = boMerchantAttrs.getJSONObject(attrIndex);
                    boMerchantAttr.put("state", MERCHANT_ACTION_ADD);
                }
            }
        } catch (RuntimeException e) {
            //返回异常信息
            return e.getMessage();
        }
        return soMerchantService(reqMerchantJSON);
    }


    /**
     * 所有服务处理类
     * {
     *
     *     'boMerchant':[{}],
     *     'boMerchantAttr':[{}]
     * }
     *
     * 返回报文：
     *
     * {'RESULT_CODE':'0000','RESULT_MSG':'成功','RESULT_INFO':{'merchantId':'7000123,718881991'}}
     * @param merchantInfoJson
     * @return
     */
    public String soMerchantService(JSONObject merchantInfoJson) throws Exception{
        LoggerEngine.debug("用户服务操作客户入参：" + merchantInfoJson);
        JSONObject paramJson = new JSONObject();

        JSONObject resultInfo = null;

        //存放生成的merchantId 主键为 merchantId-1 71000010100
        Map merchantIdKey = new HashMap();

        if (merchantInfoJson == null){
            throw new IllegalArgumentException("soMerchantService 入参 为空"+merchantInfoJson);
        }
         // 客户信息处理 处理boMerchant节点
        doProcessBoMerchant(merchantInfoJson,paramJson,merchantIdKey,resultInfo);

        //客户属性信息处理 处理boMerchantAttr节点
        doProcessBoMerchantAttr(merchantInfoJson,paramJson,merchantIdKey,resultInfo);

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
     "boMerchant": [{},{}],
     "boMerchantAttr": [{ }, {}]
     },
     {
     "actionTypeCd": "C1",
     "boMerchant": [{},{}],
     "boMerchantAttr": [{ }, {}]
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
    public String soMerchantServiceForOrderService(JSONObject mInfoJson) throws Exception {

        Assert.isNotNull(mInfoJson,"data","请求报文缺少 data 节点，请检查");

        JSONArray merchantInfos = mInfoJson.getJSONArray("data");

        Assert.isNull(merchantInfos,"请求报文中data节点，没有子节点，data子节点应该为JSONArray,merchantInfos="+merchantInfos);

        JSONObject merchantInfoJ = new JSONObject();
        JSONArray resultMerchantIdArray = new JSONArray();
        for(int merchantInfoIndex = 0 ;merchantInfoIndex < merchantInfos.size();merchantInfoIndex ++){
            JSONObject merchantInfoJson = merchantInfos.getJSONObject(merchantInfoIndex);
            String soMerchantServiceResult = this.soMerchantService(merchantInfoJson);
            JSONObject resultInfo = new JSONObject();

            if(!ProtocolUtil.validateReturnJson(soMerchantServiceResult,resultInfo)){
                throw new RuntimeException("客户信息受理失败，原因为："+resultInfo.getString(ProtocolUtil.RESULT_MSG));
            }
            if(resultInfo.getJSONObject(ProtocolUtil.RESULT_INFO) != null
                    && resultInfo.getJSONObject(ProtocolUtil.RESULT_INFO).containsKey("merchantId")) {

                String merchantIds = merchantInfoJ.getString("merchantId");
//                merchantIds += "," + resultInfo.getJSONObject(ProtocolUtil.RESULT_INFO).getString("merchantId");

//                merchantIds = merchantIds.startsWith(",") && merchantIds.length()>1 ? merchantIds.substring(1,merchantIds.length()):merchantIds;
                //merchantInfoJ.put("merchantId", merchantIds);
                JSONArray boMerchants = merchantInfoJson.getJSONArray("boMerchant");

                Object merchantIdObj = JSONPath.eval(merchantInfoJson,"$.boMerchant[merchantId < '0'][0].merchantId");
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
     *     boMerchant:[{},{}]
     * }
     * 客户信心处理
     *
     *
     * @param boMerchants
     * @return 成功 会带上处理客户的客户ID
     * {'RESULT_CODE':'0000','RESULT_MSG':'成功','RESULT_INFO':{'merchantId':'7000123,718881991'}}
     * @throws Exception
     */
    public String soBoMerchant(String boMerchants) throws Exception{
        return soBoMerchant(boMerchants,null);
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
     * @param boMerchants 客户信息
     * @param merchantIdKey merchantIdKeymap
     * @return
     * @throws Exception
     */
    public String soBoMerchant(String boMerchants,Map merchantIdKey) throws Exception{
        // 将 jsonArray 转为list<BoMerchant> 对象
        JSONObject jsonObject = JSONObject.parseObject(boMerchants);

        JSONObject resultInfo = new JSONObject();

        String merchantIds = "";

        List<BoMerchant> boMerchantList = JSONObject.parseArray(jsonObject.getJSONArray("boMerchant").toJSONString(), BoMerchant.class);

        Collections.sort(boMerchantList);
        //保存数据

        for(BoMerchant boMerchant : boMerchantList){
//        for(int boMerchantIndex = 0 ; boMerchantIndex < boMerchantList.size();boMerchantIndex++){
//            BoMerchant boMerchant = boMerchantList.get(boMerchantIndex);
            String merchantId = boMerchant.getBoId();
            //如果客户ID小于0 ，则自己生成客户ID,这个只有在有 主键生成服务时使用，否则为了防止出错，需要前段调用时需要生成merchantId
            if(StringUtils.isBlank(merchantId) || merchantId.startsWith("-") ){
                /*JSONObject data = new JSONObject();
                data.put("type","CUST_ID");
                //要求接口返回 {"RESULT_CODE":"0000","RESULT_INFO":{"merchant_id":"7020170411000041"},"RESULT_MSG":"成功"}
                String merchantIdJSONStr = iPrimaryKeyService.queryPrimaryKey(data.toJSONString());
                JSONObject merchantIdJSONTmp = JSONObject.parseObject(merchantIdJSONStr);
                if(merchantIdJSONTmp.containsKey("RESULT_CODE")
                        && ProtocolUtil.RETURN_MSG_SUCCESS.equals(merchantIdJSONTmp.getString("RESULT_CODE"))
                        && merchantIdJSONTmp.containsKey("RESULT_INFO")){
                    //从接口生成merchantId
                    merchantId = NumberUtils.toInt(merchantIdJSONTmp.getJSONObject("RESULT_INFO").getString("CUST_ID"),-1);
                }*/

                merchantId = this.queryPrimaryKey(iPrimaryKeyService,"CUST_ID");

                //将 新生成的merchantId保存至 map中 merchantId-1 merchantId-2 主键方式存入
                if(merchantIdKey != null){
                    merchantIdKey.put("merchantId"+boMerchant.getMerchantId(),merchantId);
                }
            }

            boMerchant.setMerchantId(merchantId);

            //保存数据至 bo_merchant 表中
            int saveBoMerchantFlag = iMerchantServiceDao.saveDataToBoMerchant(boMerchant);

            if(saveBoMerchantFlag < 1){ // 如果没有保存成功，抛出异常，有事物 回退事物
                throw new RuntimeException("保存过程[bo_merchant]数据失败,印象记录数为"+saveBoMerchantFlag+"，boMerchant : "+boMerchant);
            }
            //建档 处理 实例数据
            int saveMerchantFlag = 0;
            if("ADD".equals(boMerchant.getState())){
               /* List<BoMerchant> boMerchantsTmp = boMerchantList;
                //在增加之间首先要判断是否有相应删的动作
//                for(BoMerchant boMerchantTmp : boMerchantsTmp){
                for(int boMerchantTmpIndex = boMerchantIndex+1;boMerchantTmpIndex < boMerchantsTmp.size();boMerchantTmpIndex++){
                    BoMerchant boMerchantTmp = boMerchantsTmp.get(boMerchantTmpIndex);
                    if(StringUtils.isNotBlank(boMerchant.getMerchantId())
                            && boMerchant.getMerchantId().equals(boMerchantTmp.getMerchantId())
                            &&"DEL".equals(boMerchantTmp.getState())){
                        //先调用删除客户信息
                        saveMerchantFlag = iMerchantServiceDao.deleteDataToMerchant(boMerchant.convert());

                        if(saveMerchantFlag < 1){
                            throw new RuntimeException("删除实例[merchant]数据失败，影响记录数为"+saveMerchantFlag+", merchant : "+boMerchant.convert());
                        }

                        //则把已经删除过的从list中删除，以防重复删除
                        boMerchantList.remove(boMerchantTmp);

                    }
                }*/

                saveMerchantFlag  = iMerchantServiceDao.saveDataToMerchant(boMerchant.convert());
            }else if("DEL".equals(boMerchant.getState())){
                saveMerchantFlag = iMerchantServiceDao.deleteDataToMerchant(boMerchant.convert());
            }else if("KIP".equals(boMerchant.getState())){
                //按理这里到不了，KIP表示实例数据不变，所以这里默认写成1 认为是成功
                saveMerchantFlag = 1;
            }else{
                //这里单独抛出异常，不走下面统一异常抛出，是为了说明更具体点
                throw new RuntimeException("入参错误boMerchant 的 state 目前只支持 [ADD,DEL,KIP] , boMerchant : " +boMerchant);
            }


            if(saveMerchantFlag < 1){
                throw new RuntimeException("保存实例[merchant]数据失败，影响记录数为"+saveMerchantFlag+", merchant : "+boMerchant.convert());
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
     * 注意在调用这个接口时，相应的客户信息必须存在
     *
     *
     * 客户信息属性处理
     * 协议：
     *{
     *     boMerchantAttr:[{},{}]
     * }
     * @param boMerchantAttrs
     * @return
     * @throws Exception
     */
    @Override
    public String soBoMerchantAttr(String boMerchantAttrs) throws Exception {

        //这里可以加入基本客户信息是否存在的校验，暂时没有必要实现

        // 将 jsonArray 转为list<BoMerchant> 对象
        JSONObject jsonObject = JSONObject.parseObject(boMerchantAttrs);

        List<BoMerchantAttr> boMerchantAttrList = JSONObject.parseArray(jsonObject.getJSONArray("boMerchantAttr").toJSONString(), BoMerchantAttr.class);

        //先拍个序 先处理DEL 再处理ADD
        Collections.sort(boMerchantAttrList);
        //保存数据

        for(BoMerchantAttr boMerchantAttr : boMerchantAttrList) {

            //保存数据至 bo_merchant_attr 表中
            int saveBoMerchantAttrFlag = iMerchantServiceDao.saveDataToBoMerchantAttr(boMerchantAttr);

            if(saveBoMerchantAttrFlag < 1){ // 如果没有保存成功，抛出异常，有事物 回退事物
                throw new RuntimeException("保存过程[bo_merchant_attr]数据失败,印象记录数为"+saveBoMerchantAttrFlag+"，boMerchantAttr : "+boMerchantAttr);
            }

            //建档 处理 实例数据
            int saveMerchantAttrFlag = 0;
            if("ADD".equals(boMerchantAttr.getState())){
                saveMerchantAttrFlag  = iMerchantServiceDao.saveDataToMerchantAttr(boMerchantAttr.convert());
            }else if("DEL".equals(boMerchantAttr.getState())){
                saveMerchantAttrFlag = iMerchantServiceDao.deleteDataToMerchantAttr(boMerchantAttr.convert());
            }else if("KIP".equals(boMerchantAttr.getState())){
                //按理这里到不了，KIP表示实例数据不变，所以这里默认写成1 认为是成功
                saveMerchantAttrFlag = 1;
            }else{
                //这里单独抛出异常，不走下面统一异常抛出，是为了说明更具体点
                throw new RuntimeException("入参错误boMerchantAttr 的 state 目前只支持 [ADD,DEL,KIP] , boMerchant : " +boMerchantAttr);
            }

            if(saveMerchantAttrFlag < 1){
                throw new RuntimeException("保存实例[merchant_attr]数据失败，影响记录数为"+saveMerchantAttrFlag+", merchant : "+boMerchantAttr.convert());
            }
        }

        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",null);
    }

    /**
     * 作废客户信息
     * [{},{},{}]
     *
     * @param datas
     * @return
     * @throws Exception
     */
    @Override
    public String soDeleteMerchantInfo(JSONArray datas) throws Exception {

        Assert.isNull(datas,"传入的data节点下没有任何内容");

        for(int boIdIndex = 0 ; boIdIndex < datas.size(); boIdIndex++){
            JSONObject data = datas.getJSONObject(boIdIndex);

            Assert.isNotNull(data,"boId","当前节点中没有包含boId节点格式错误"+data);

            // 复原Merchant
            doDeleteBoMerchant(data);

            // 复原MerchantAttr
            doDeleteBoMerchantAttr(data);

        }


        return null;
    }


    /**
     * 查询客户信息
     * 包括 基本信息merchant 和 属性信息 merchantAttr
     * @param merchant
     * @return
     * @throws Exception
     */
    public String queryMerchant(Merchant merchant) throws Exception{
        LoggerEngine.debug("客户信息查询入参：" + merchant);
        if(merchant == null || StringUtils.isBlank(merchant.getMerchantId()) ){
            throw new IllegalArgumentException("客户信息查询入参为空，merchantId 为空 "+merchant);
        }
        Merchant newMerchant = iMerchantServiceDao.queryDataToMerchant(merchant);

        if(newMerchant == null){
            return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"未找到用户信息",null);
        }

        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",JSONObject.parseObject(JSONObject.toJSONString(newMerchant)));
    }

    /**
     * 根据购物车信息查询
     * @param busiOrder 订单项信息
     * @return
     * @throws Exception
     */
    @Override
    public String queryMerchantInfoByOlId(String busiOrder) throws Exception {
        return doQueryMerchantInfoByOlId(busiOrder,false);
    }

    /**
     * 根据购物车信息查询 需要作废的发起的报文
     * @param busiOrder 订单项信息
     * @return
     * @throws Exception
     */
    @Override
    public String queryNeedDeleteMerchantInfoByOlId(String busiOrder) throws Exception {
        return doQueryMerchantInfoByOlId(busiOrder,true);
    }

    /**
     * 处理boMerchant 节点
     * @throws Exception
     */
    public void doProcessBoMerchant(JSONObject merchantInfoJson,JSONObject paramJson,Map merchantIdKey, JSONObject resultInfo) throws Exception{

        if(merchantInfoJson.containsKey("boMerchant")){
            JSONArray boMerchants = merchantInfoJson.getJSONArray("boMerchant");
            JSONObject boMerchantObj = new JSONObject();
            boMerchantObj.put("boMerchant",boMerchants);
            String returnSaveBoMerchant = this.soBoMerchant(boMerchantObj.toJSONString(),merchantIdKey);

            if(!ProtocolUtil.validateReturnJson(returnSaveBoMerchant,paramJson)){

                throw new RuntimeException("保存 bo_merchant 失败："+boMerchantObj+(paramJson != null
                        && paramJson.containsKey("RESULT_MSG")?paramJson.getString("RESULT_MSG"):"未知异常"));
            }

            resultInfo = paramJson.getJSONObject("RESULT_INFO");
        }
    }

    /**
     * 处理boMerchantAttr 节点
     * @param merchantInfoJson
     * @param paramJson
     * @param merchantIdKey
     * @param resultInfo
     */
    public void doProcessBoMerchantAttr(JSONObject merchantInfoJson,JSONObject paramJson,Map merchantIdKey, JSONObject resultInfo) throws Exception{
        if(merchantInfoJson.containsKey("boMerchantAttr")){

            JSONArray boMerchantAttrs = merchantInfoJson.getJSONArray("boMerchantAttr");
            //首先对merchantId 进行处理
            if(merchantIdKey != null && merchantIdKey.size() > 0 ){
                for(int boMerchantAttrIndex = 0 ; boMerchantAttrIndex < boMerchantAttrs.size();boMerchantAttrIndex++){
                    JSONObject boMerchantAttr = boMerchantAttrs.getJSONObject(boMerchantAttrIndex);
                    boMerchantAttr.put("merchantId",merchantIdKey.get("merchantId"+boMerchantAttr.getString("merchantId")));
                }
            }
            JSONObject boMerchantAttrObj = new JSONObject();
            boMerchantAttrObj.put("boMerchantAttr",boMerchantAttrs);
            String returnSaveBoMerchantAttr = soBoMerchantAttr(boMerchantAttrObj.toJSONString());

            if(!ProtocolUtil.validateReturnJson(returnSaveBoMerchantAttr,paramJson)){

                throw new RuntimeException("保存 bo_merchant 失败："+boMerchantAttrObj+(paramJson != null
                        && paramJson.containsKey("RESULT_MSG")?paramJson.getString("RESULT_MSG"):"未知异常"));
            }
        }
    }

    /**
     * 作废 boMerchant 信息
     * @param data
     * @throws Exception
     */
    public void doDeleteBoMerchant(JSONObject data) throws Exception{


        Merchant deleteMerchant = null;
        //根据boId 查询bo_merchant 表，是否有数据，没数据直接返回
        BoMerchant boMerchant = new BoMerchant();

        boMerchant.setBoId(data.getString("boId"));

       List<BoMerchant> boMerchants =  iMerchantServiceDao.queryBoMerchant(boMerchant);

       //Assert.isOne(boMerchants,"在表bo_merchant中未找到boId 为["+data.getString("boId")+"]的数据 或有多条数据，请检查");
        if(boMerchants == null || boMerchants.size() < 1){
            LoggerEngine.error("当前没有查到数为 "+data+"请检查数据");
            return;
        }
       //在过程表中补一条作废的数据，然后根据boId的动作对实例数据进行处理

        boMerchant.setMerchantId(boMerchants.get(0).getMerchantId());
        boMerchant.setBoId("");
        //查询出所有merchantId 一样的数据
        List<BoMerchant> boMerchantAll =  iMerchantServiceDao.queryBoMerchant(boMerchant);

        Assert.isNull(boMerchantAll,"当前没有查到merchantId 为 "+boMerchants.get(0).getMerchantId()+"请检查数据");

        boMerchant = boMerchants.get(0);

        BoMerchant newBoMerchant = new BoMerchant();
        newBoMerchant.setBoId(data.getString("newBoId"));
        newBoMerchant.setMerchantId(boMerchant.getMerchantId());
        newBoMerchant.setState("DEL");
        int saveBoMerchantFlag = iMerchantServiceDao.saveDataToBoMerchant(newBoMerchant);

        if(saveBoMerchantFlag < 1){
            throw new RuntimeException("向bo_merchant表中保存数据失败，boMerchant="+JSONObject.toJSONString(newBoMerchant));
        }

        //首先删除实例数据
        deleteMerchant = new Merchant();
        deleteMerchant.setMerchantId(boMerchant.getMerchantId());
        if(iMerchantServiceDao.deleteDataToMerchant(deleteMerchant) < 1){
            throw new RuntimeException("删除merchant实例数据失败"+JSONObject.toJSONString(deleteMerchant));
        }
        //如果有多条数据，则恢复 前一条数据信息，这边存在bug 如果上一条的数据没有分装以前数据的情况下会有问题，
        // 所以我们的原则是再更新或删除数据时一定要在过程表中保存完整是实例数据信息
        if(boMerchantAll.size() > 1){
            Merchant oldMerchant = boMerchantAll.get(1).convert();
            if(iMerchantServiceDao.saveDataToMerchant(oldMerchant)<1 ){
                throw new RuntimeException("merchant 表恢复老数据信息失败，merchant 为："+JSONObject.toJSONString(oldMerchant));
            }
        }
    }

    /**
     * 删除 bo_merchant_attr
     * @param data
     * @throws Exception
     */
    public void doDeleteBoMerchantAttr(JSONObject data) throws Exception{

        BoMerchantAttr boMerchantAttrTmp = new BoMerchantAttr();

        boMerchantAttrTmp.setBoId(data.getString("boId"));

        List<BoMerchantAttr> boMerchantAttrs = iMerchantServiceDao.queryBoMerchantAttr(boMerchantAttrTmp);

        if(boMerchantAttrs == null || boMerchantAttrs.size() < 1){
            LoggerEngine.error("当前没有查到数为 "+data+"请检查数据");
            return;
        }

        boMerchantAttrTmp.setBoId("");
        boMerchantAttrTmp.setMerchantId(boMerchantAttrs.get(0).getMerchantId());

        List<BoMerchantAttr> boMerchantAttrsTmps = iMerchantServiceDao.queryBoMerchantAttr(boMerchantAttrTmp);

        Assert.isNull(boMerchantAttrsTmps,"当前没有查到merchantId 为 "+boMerchantAttrs.get(0).getMerchantId()+"请检查数据");

        //获取上一次所有的属性

        List<BoMerchantAttr> preBoMerchantAttrTmps = getPreBoMerchantAttrs(boMerchantAttrsTmps);

        //保存过程表
        for(BoMerchantAttr boMerchantAttr : boMerchantAttrs){
            boMerchantAttr.setBoId("newBoId");
            boMerchantAttr.setState("DEL");
            if(iMerchantServiceDao.saveDataToBoMerchantAttr(boMerchantAttr) < 1){
                throw new RuntimeException("保存数据失败，保存数据为boMerchantAttr = "+ JSONObject.toJSONString(boMerchantAttr));
            }
        }

        //删除实例数据 这里思路是，删除实例数据中数据，将上一次ADD数据重新写一遍
        MerchantAttr merchantAttrTmp = new MerchantAttr();
        merchantAttrTmp.setMerchantId(boMerchantAttrs.get(0).getMerchantId());
        if(iMerchantServiceDao.deleteDataToMerchantAttr(merchantAttrTmp) < 1){
            throw new RuntimeException("删除MerchantAttr 实例数据失败,数据为："+JSONObject.toJSONString(merchantAttrTmp));
        }

        for(BoMerchantAttr boMerchantAttr : preBoMerchantAttrTmps){
            if("ADD".equals(boMerchantAttr.getState())){
                if(iMerchantServiceDao.deleteDataToMerchantAttr(boMerchantAttr.convert()) < 1){
                    throw new  RuntimeException("复原原始数据失败，数据为：" + JSONObject.toJSONString(boMerchantAttr));
                }
            }
        }

    }

    /**
     * 获取上上一次的操作
     * @param boMerchantAttrs
     * @return
     */
    private List<BoMerchantAttr> getPreBoMerchantAttrs(List<BoMerchantAttr> boMerchantAttrs){

        String firstBoId = boMerchantAttrs.get(0).getBoId();
        String preBoId = "";
        List<BoMerchantAttr> preBoMerchantAttrs = new ArrayList<BoMerchantAttr>();
        for(BoMerchantAttr boMerchantAttr : boMerchantAttrs){
            if(!firstBoId.equals(boMerchantAttr.getBoId())){
                if(!preBoId.equals(boMerchantAttr.getBoId()) && !"".equals(preBoId)){
                    break;
                }
                preBoId = boMerchantAttr.getBoId();
                preBoMerchantAttrs.add(boMerchantAttr);
            }
        }
        return preBoMerchantAttrs;
    }


    /**
     * 查询客户订单信息
     * @param busiOrderStr 订单项信息
     * @param isNeedDelete 是否是撤单报文 true 查询撤单报文 false
     * @return
     * @throws Exception
     */
    private String doQueryMerchantInfoByOlId(String busiOrderStr,Boolean isNeedDelete) throws Exception{
        BusiOrder busiOrder = JSONObject.parseObject(busiOrderStr, BusiOrder.class);

        if(busiOrder == null || "".equals(busiOrder.getOlId())){
            throw new IllegalArgumentException("商户信息查询入参为空，olId 为空 "+busiOrderStr);
        }

        Merchant merchant = new Merchant();
        merchant.setVersionId(busiOrder.getOlId());
        //根据版本ID查询实例数据
        Merchant newMerchant = iMerchantServiceDao.queryDataToMerchant(merchant);
        JSONObject returnJson = JSONObject.parseObject("{'data':{}}");
        if(newMerchant == null){
            return returnJson.toJSONString();
        }

        BoMerchant boMerchant = new BoMerchant();

        boMerchant.setBoId(busiOrder.getBoId());
        boMerchant.setMerchantId(newMerchant.getMerchantId());
        boMerchant.setVersionId(busiOrder.getOlId());

        List<BoMerchant> boMerchants =  iMerchantServiceDao.queryBoMerchant(boMerchant);


        //一般情况下没有这种情况存在，除非 人工 改了数据，或没按流程完成数据处理
        if(boMerchants == null || boMerchants.size() == 0){
            return returnJson.toJSONString();
        }


        JSONArray boMerchantArray = new JSONArray();
        //单纯的删除 和单纯 增加
        for(int boMerchantIndex = 0 ; boMerchantIndex < boMerchants.size();boMerchantIndex++) {
            BoMerchant newBoMerchant = boMerchants.get(boMerchantIndex);
            if(isNeedDelete) {
                if (StateConstant.STATE_DEL.equals(newBoMerchant.getState())) {
                    newBoMerchant.setBoId("");
                    newBoMerchant.setState(StateConstant.STATE_ADD);
                } else if (StateConstant.STATE_ADD.equals(newBoMerchant.getState())) {
                    newBoMerchant.setState(StateConstant.STATE_DEL);
                } else {
                    newBoMerchant.setState(StateConstant.STATE_KIP);
                }
            }
            boMerchantArray.add(newBoMerchant);
        }
        returnJson.getJSONObject("data").put("boMerchant",JSONObject.toJSONString(boMerchantArray));


        //属性处理
        MerchantAttr oldMerchantAttr = new MerchantAttr();
        oldMerchantAttr.setMerchantId(newMerchant.getMerchantId());
        oldMerchantAttr.setVersionId(busiOrder.getOlId());
        List<MerchantAttr> custAttrs = iMerchantServiceDao.queryDataToMerchantAttr(oldMerchantAttr);
        if(custAttrs == null || custAttrs.size() == 0){
            return returnJson.toJSONString();
        }
        /**
         * 查询客户查询的过程数据
         */
        BoMerchantAttr boMerchantAttr = new BoMerchantAttr();
        boMerchantAttr.setMerchantId(newMerchant.getMerchantId());
        boMerchantAttr.setVersionId(busiOrder.getOlId());
        List<BoMerchantAttr> boMerchantAttrs = iMerchantServiceDao.queryBoMerchantAttr(boMerchantAttr);


        //一般情况下没有这种情况存在，除非 人工 改了数据，或没按流程完成数据处理
        if(boMerchantAttrs == null || boMerchantAttrs.size() == 0){
            return returnJson.toJSONString();
        }


        JSONArray boMerchantAttrArray = new JSONArray();
        //单纯的删除 和单纯 增加
        for(BoMerchantAttr newBoMerchantAttr : boMerchantAttrs) {
            if(isNeedDelete) {
                if (StateConstant.STATE_DEL.equals(newBoMerchantAttr.getState())) {
                    newBoMerchantAttr.setBoId("");
                    newBoMerchantAttr.setState(StateConstant.STATE_ADD);
                } else if (StateConstant.STATE_ADD.equals(newBoMerchantAttr.getState())) {
                    newBoMerchantAttr.setState(StateConstant.STATE_DEL);
                } else {
                    newBoMerchantAttr.setState(StateConstant.STATE_KIP);
                }
            }
            boMerchantAttrArray.add(newBoMerchantAttr);
        }

        returnJson.getJSONObject("data").put("boMerchantAttr",JSONObject.toJSONString(boMerchantAttrArray));

        return returnJson.toJSONString();

    }

    public IPrimaryKeyService getiPrimaryKeyService() {
        return iPrimaryKeyService;
    }

    public void setiPrimaryKeyService(IPrimaryKeyService iPrimaryKeyService) {
        this.iPrimaryKeyService = iPrimaryKeyService;
    }

    public IMerchantServiceDao getiMerchantServiceDao() {
        return iMerchantServiceDao;
    }

    public void setiMerchantServiceDao(IMerchantServiceDao iMerchantServiceDao) {
        this.iMerchantServiceDao = iMerchantServiceDao;
    }
}
