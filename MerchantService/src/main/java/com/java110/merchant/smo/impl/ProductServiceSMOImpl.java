package com.java110.merchant.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.java110.common.constant.StateConstant;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.entity.order.BusiOrder;
import com.java110.entity.product.BoProduct;
import com.java110.entity.product.BoProductAttr;
import com.java110.entity.product.Product;
import com.java110.entity.product.ProductAttr;
import com.java110.feign.base.IPrimaryKeyService;
import com.java110.merchant.dao.IProductServiceDao;
import com.java110.merchant.smo.IProductServiceSMO;
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
@Service("productServiceSMOImpl")
@Transactional
public class ProductServiceSMOImpl extends BaseServiceSMO implements IProductServiceSMO {

    @Autowired
    IPrimaryKeyService iPrimaryKeyService;

    @Autowired
    IProductServiceDao iProductServiceDao;

    //新增用户
    private final static String MERCHANT_ACTION_ADD = "ADD";

    //新增用户
    private final static String MERCHANT_ACTION_KIP = "KIP";

    //新增用户
    private final static String MERCHANT_ACTION_DEL = "DEL";

    /**
     * 保存用户信息
     *
     * @param productInfoJson 入参为用户信息json传
     * @return
     */
    public String saveProduct(String productInfoJson) throws Exception{

        JSONObject reqProductJSON = null;
        try {
            reqProductJSON = this.simpleValidateJSON(productInfoJson);
            //boProduct增加Action (动作)
            if (reqProductJSON.containsKey("boProduct")) {
                JSONObject boProduct = reqProductJSON.getJSONObject("boProduct");
                boProduct.put("state", MERCHANT_ACTION_ADD);
            }
            //boProductAttr增加Action（动作）
            if (reqProductJSON.containsKey("boProductAttr")) {
                JSONArray boProductAttrs = reqProductJSON.getJSONArray("boProductAttr");

                for (int attrIndex = 0; attrIndex < boProductAttrs.size(); attrIndex++) {
                    JSONObject boProductAttr = boProductAttrs.getJSONObject(attrIndex);
                    boProductAttr.put("state", MERCHANT_ACTION_ADD);
                }
            }
        } catch (RuntimeException e) {
            //返回异常信息
            return e.getMessage();
        }
        return soProductService(reqProductJSON);
    }


    /**
     * 所有服务处理类
     * {
     *
     *     'boProduct':[{}],
     *     'boProductAttr':[{}]
     * }
     *
     * 返回报文：
     *
     * {'RESULT_CODE':'0000','RESULT_MSG':'成功','RESULT_INFO':{'productId':'7000123,718881991'}}
     * @param productInfoJson
     * @return
     */
    public String soProductService(JSONObject productInfoJson) throws Exception{
        LoggerEngine.debug("用户服务操作客户入参：" + productInfoJson);
        JSONObject paramJson = new JSONObject();

        JSONObject resultInfo = null;

        //存放生成的productId 主键为 productId-1 71000010100
        Map productIdKey = new HashMap();

        if (productInfoJson == null){
            throw new IllegalArgumentException("soProductService 入参 为空"+productInfoJson);
        }
         // 客户信息处理 处理boProduct节点
        doProcessBoProduct(productInfoJson,paramJson,productIdKey,resultInfo);

        //客户属性信息处理 处理boProductAttr节点
        doProcessBoProductAttr(productInfoJson,paramJson,productIdKey,resultInfo);

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
     "boProduct": [{},{}],
     "boProductAttr": [{ }, {}]
     },
     {
     "actionTypeCd": "C1",
     "boProduct": [{},{}],
     "boProductAttr": [{ }, {}]
     }
     ]
     }

     返回报文 ：

     { 'RESULT_CODE': '0000', 'RESULT_MSG': '成功', 'RESULT_INFO': {'product':[{'oldProductId':'-1','productId':'12345678'},{'oldProductId':'-2','productId':'12345678'}]} }
     * @param mInfoJson
     * @return
     * @throws Exception
     */
    @Override
    public String soProductServiceForOrderService(JSONObject mInfoJson) throws Exception {

        Assert.isNotNull(mInfoJson,"data","请求报文缺少 data 节点，请检查");

        JSONArray productInfos = mInfoJson.getJSONArray("data");

        Assert.isNull(productInfos,"请求报文中data节点，没有子节点，data子节点应该为JSONArray,productInfos="+productInfos);

        JSONObject productInfoJ = new JSONObject();
        JSONArray resultProductIdArray = new JSONArray();
        for(int productInfoIndex = 0 ;productInfoIndex < productInfos.size();productInfoIndex ++){
            JSONObject productInfoJson = productInfos.getJSONObject(productInfoIndex);
            String soProductServiceResult = this.soProductService(productInfoJson);
            JSONObject resultInfo = new JSONObject();

            if(!ProtocolUtil.validateReturnJson(soProductServiceResult,resultInfo)){
                throw new RuntimeException("客户信息受理失败，原因为："+resultInfo.getString(ProtocolUtil.RESULT_MSG));
            }
            if(resultInfo.getJSONObject(ProtocolUtil.RESULT_INFO) != null
                    && resultInfo.getJSONObject(ProtocolUtil.RESULT_INFO).containsKey("productId")) {

                String productIds = productInfoJ.getString("productId");
//                productIds += "," + resultInfo.getJSONObject(ProtocolUtil.RESULT_INFO).getString("productId");

//                productIds = productIds.startsWith(",") && productIds.length()>1 ? productIds.substring(1,productIds.length()):productIds;
                //productInfoJ.put("productId", productIds);
                JSONArray boProducts = productInfoJson.getJSONArray("boProduct");

                Object productIdObj = JSONPath.eval(productInfoJson,"$.boProduct[productId < '0'][0].productId");
                if(StringUtils.isNotBlank(productIds) && !ObjectUtils.isEmpty(productIdObj)) {

                    String[] allNewProductIds = productIds.split(",");
                    JSONObject newProductIdJson = null;
                    for (String productId : allNewProductIds) {
                        newProductIdJson = new JSONObject();
                        newProductIdJson.put("oldProductId",productIdObj);
                        newProductIdJson.put("productId",productId);
                        resultProductIdArray.add(newProductIdJson);
                    }

                }
            }

        }

        productInfoJ.put("product",resultProductIdArray);

        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",productInfoJ);
    }

    /**
     * {
     *     boProduct:[{},{}]
     * }
     * 客户信心处理
     *
     *
     * @param boProducts
     * @return 成功 会带上处理客户的客户ID
     * {'RESULT_CODE':'0000','RESULT_MSG':'成功','RESULT_INFO':{'productId':'7000123,718881991'}}
     * @throws Exception
     */
    public String soBoProduct(String boProducts) throws Exception{
        return soBoProduct(boProducts,null);
    }

    /**
     * 将生成的productId 封装在map中返回
     * ...
     * productIdKey.put("productId-1","710020404040");
     *
     * ...
     *
     * key 为 productId 加原前的值
     *
     * productIdKey 如果为空不做处理
     * @param boProducts 客户信息
     * @param productIdKey productIdKeymap
     * @return
     * @throws Exception
     */
    public String soBoProduct(String boProducts,Map productIdKey) throws Exception{
        // 将 jsonArray 转为list<BoProduct> 对象
        JSONObject jsonObject = JSONObject.parseObject(boProducts);

        JSONObject resultInfo = new JSONObject();

        String productIds = "";

        List<BoProduct> boProductList = JSONObject.parseArray(jsonObject.getJSONArray("boProduct").toJSONString(), BoProduct.class);

        Collections.sort(boProductList);
        //保存数据

        for(BoProduct boProduct : boProductList){
//        for(int boProductIndex = 0 ; boProductIndex < boProductList.size();boProductIndex++){
//            BoProduct boProduct = boProductList.get(boProductIndex);
            String productId = boProduct.getBoId();
            //如果客户ID小于0 ，则自己生成客户ID,这个只有在有 主键生成服务时使用，否则为了防止出错，需要前段调用时需要生成productId
            if(StringUtils.isBlank(productId) || productId.startsWith("-") ){
                /*JSONObject data = new JSONObject();
                data.put("type","CUST_ID");
                //要求接口返回 {"RESULT_CODE":"0000","RESULT_INFO":{"product_id":"7020170411000041"},"RESULT_MSG":"成功"}
                String productIdJSONStr = iPrimaryKeyService.queryPrimaryKey(data.toJSONString());
                JSONObject productIdJSONTmp = JSONObject.parseObject(productIdJSONStr);
                if(productIdJSONTmp.containsKey("RESULT_CODE")
                        && ProtocolUtil.RETURN_MSG_SUCCESS.equals(productIdJSONTmp.getString("RESULT_CODE"))
                        && productIdJSONTmp.containsKey("RESULT_INFO")){
                    //从接口生成productId
                    productId = NumberUtils.toInt(productIdJSONTmp.getJSONObject("RESULT_INFO").getString("CUST_ID"),-1);
                }*/

                productId = this.queryPrimaryKey(iPrimaryKeyService,"CUST_ID");

                //将 新生成的productId保存至 map中 productId-1 productId-2 主键方式存入
                if(productIdKey != null){
                    productIdKey.put("productId"+boProduct.getProductId(),productId);
                }
            }

            boProduct.setProductId(productId);

            //保存数据至 bo_product 表中
            int saveBoProductFlag = iProductServiceDao.saveDataToBoProduct(boProduct);

            if(saveBoProductFlag < 1){ // 如果没有保存成功，抛出异常，有事物 回退事物
                throw new RuntimeException("保存过程[bo_product]数据失败,印象记录数为"+saveBoProductFlag+"，boProduct : "+boProduct);
            }
            //建档 处理 实例数据
            int saveProductFlag = 0;
            if("ADD".equals(boProduct.getState())){
               /* List<BoProduct> boProductsTmp = boProductList;
                //在增加之间首先要判断是否有相应删的动作
//                for(BoProduct boProductTmp : boProductsTmp){
                for(int boProductTmpIndex = boProductIndex+1;boProductTmpIndex < boProductsTmp.size();boProductTmpIndex++){
                    BoProduct boProductTmp = boProductsTmp.get(boProductTmpIndex);
                    if(StringUtils.isNotBlank(boProduct.getProductId())
                            && boProduct.getProductId().equals(boProductTmp.getProductId())
                            &&"DEL".equals(boProductTmp.getState())){
                        //先调用删除客户信息
                        saveProductFlag = iProductServiceDao.deleteDataToProduct(boProduct.convert());

                        if(saveProductFlag < 1){
                            throw new RuntimeException("删除实例[product]数据失败，影响记录数为"+saveProductFlag+", product : "+boProduct.convert());
                        }

                        //则把已经删除过的从list中删除，以防重复删除
                        boProductList.remove(boProductTmp);

                    }
                }*/

                saveProductFlag  = iProductServiceDao.saveDataToProduct(boProduct.convert());
            }else if("DEL".equals(boProduct.getState())){
                saveProductFlag = iProductServiceDao.deleteDataToProduct(boProduct.convert());
            }else if("KIP".equals(boProduct.getState())){
                //按理这里到不了，KIP表示实例数据不变，所以这里默认写成1 认为是成功
                saveProductFlag = 1;
            }else{
                //这里单独抛出异常，不走下面统一异常抛出，是为了说明更具体点
                throw new RuntimeException("入参错误boProduct 的 state 目前只支持 [ADD,DEL,KIP] , boProduct : " +boProduct);
            }


            if(saveProductFlag < 1){
                throw new RuntimeException("保存实例[product]数据失败，影响记录数为"+saveProductFlag+", product : "+boProduct.convert());
            }

            productIds +=","+productId;

        }

        //去除第一个逗号
        if (productIds.length()>0){
            productIds = productIds.substring(1);
        }

        resultInfo.put("productId",productIds);

        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",resultInfo);
    }

    /**
     * 注意在调用这个接口时，相应的客户信息必须存在
     *
     *
     * 客户信息属性处理
     * 协议：
     *{
     *     boProductAttr:[{},{}]
     * }
     * @param boProductAttrs
     * @return
     * @throws Exception
     */
    @Override
    public String soBoProductAttr(String boProductAttrs) throws Exception {

        //这里可以加入基本客户信息是否存在的校验，暂时没有必要实现

        // 将 jsonArray 转为list<BoProduct> 对象
        JSONObject jsonObject = JSONObject.parseObject(boProductAttrs);

        List<BoProductAttr> boProductAttrList = JSONObject.parseArray(jsonObject.getJSONArray("boProductAttr").toJSONString(), BoProductAttr.class);

        //先拍个序 先处理DEL 再处理ADD
        Collections.sort(boProductAttrList);
        //保存数据

        for(BoProductAttr boProductAttr : boProductAttrList) {

            //保存数据至 bo_product_attr 表中
            int saveBoProductAttrFlag = iProductServiceDao.saveDataToBoProductAttr(boProductAttr);

            if(saveBoProductAttrFlag < 1){ // 如果没有保存成功，抛出异常，有事物 回退事物
                throw new RuntimeException("保存过程[bo_product_attr]数据失败,印象记录数为"+saveBoProductAttrFlag+"，boProductAttr : "+boProductAttr);
            }

            //建档 处理 实例数据
            int saveProductAttrFlag = 0;
            if("ADD".equals(boProductAttr.getState())){
                saveProductAttrFlag  = iProductServiceDao.saveDataToProductAttr(boProductAttr.convert());
            }else if("DEL".equals(boProductAttr.getState())){
                saveProductAttrFlag = iProductServiceDao.deleteDataToProductAttr(boProductAttr.convert());
            }else if("KIP".equals(boProductAttr.getState())){
                //按理这里到不了，KIP表示实例数据不变，所以这里默认写成1 认为是成功
                saveProductAttrFlag = 1;
            }else{
                //这里单独抛出异常，不走下面统一异常抛出，是为了说明更具体点
                throw new RuntimeException("入参错误boProductAttr 的 state 目前只支持 [ADD,DEL,KIP] , boProduct : " +boProductAttr);
            }

            if(saveProductAttrFlag < 1){
                throw new RuntimeException("保存实例[product_attr]数据失败，影响记录数为"+saveProductAttrFlag+", product : "+boProductAttr.convert());
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
    public String soDeleteProductInfo(JSONArray datas) throws Exception {

        Assert.isNull(datas,"传入的data节点下没有任何内容");

        for(int boIdIndex = 0 ; boIdIndex < datas.size(); boIdIndex++){
            JSONObject data = datas.getJSONObject(boIdIndex);

            Assert.isNotNull(data,"boId","当前节点中没有包含boId节点格式错误"+data);

            // 复原Product
            doDeleteBoProduct(data);

            // 复原ProductAttr
            doDeleteBoProductAttr(data);

        }


        return null;
    }


    /**
     * 查询客户信息
     * 包括 基本信息product 和 属性信息 productAttr
     * @param product
     * @return
     * @throws Exception
     */
    public String queryProduct(Product product) throws Exception{
        LoggerEngine.debug("客户信息查询入参：" + product);
        if(product == null || StringUtils.isBlank(product.getProductId()) ){
            throw new IllegalArgumentException("客户信息查询入参为空，productId 为空 "+product);
        }
        Product newProduct = iProductServiceDao.queryDataToProduct(product);

        if(newProduct == null){
            return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"未找到用户信息",null);
        }

        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",JSONObject.parseObject(JSONObject.toJSONString(newProduct)));
    }

    /**
     *根据olID查询用户信息
     * @param busiOrder
     * @return
     * @throws Exception
     */
    @Override
    public String queryProductInfoByOlId(String busiOrder) throws Exception {
        return doQueryProductInfoByOlId(busiOrder,false);
    }

    /**
     * 根据购物车信息查询 需要作废的发起的报文
     * @param busiOrder
     * @return
     * @throws Exception
     */
    @Override
    public String queryNeedDeleteProductInfoByOlId(String busiOrder) throws Exception {
        return doQueryProductInfoByOlId(busiOrder,true);
    }

    /**
     * 处理boProduct 节点
     * @throws Exception
     */
    public void doProcessBoProduct(JSONObject productInfoJson,JSONObject paramJson,Map productIdKey, JSONObject resultInfo) throws Exception{

        if(productInfoJson.containsKey("boProduct")){
            JSONArray boProducts = productInfoJson.getJSONArray("boProduct");
            JSONObject boProductObj = new JSONObject();
            boProductObj.put("boProduct",boProducts);
            String returnSaveBoProduct = this.soBoProduct(boProductObj.toJSONString(),productIdKey);

            if(!ProtocolUtil.validateReturnJson(returnSaveBoProduct,paramJson)){

                throw new RuntimeException("保存 bo_product 失败："+boProductObj+(paramJson != null
                        && paramJson.containsKey("RESULT_MSG")?paramJson.getString("RESULT_MSG"):"未知异常"));
            }

            resultInfo = paramJson.getJSONObject("RESULT_INFO");
        }
    }

    /**
     * 处理boProductAttr 节点
     * @param productInfoJson
     * @param paramJson
     * @param productIdKey
     * @param resultInfo
     */
    public void doProcessBoProductAttr(JSONObject productInfoJson,JSONObject paramJson,Map productIdKey, JSONObject resultInfo) throws Exception{
        if(productInfoJson.containsKey("boProductAttr")){

            JSONArray boProductAttrs = productInfoJson.getJSONArray("boProductAttr");
            //首先对productId 进行处理
            if(productIdKey != null && productIdKey.size() > 0 ){
                for(int boProductAttrIndex = 0 ; boProductAttrIndex < boProductAttrs.size();boProductAttrIndex++){
                    JSONObject boProductAttr = boProductAttrs.getJSONObject(boProductAttrIndex);
                    boProductAttr.put("productId",productIdKey.get("productId"+boProductAttr.getString("productId")));
                }
            }
            JSONObject boProductAttrObj = new JSONObject();
            boProductAttrObj.put("boProductAttr",boProductAttrs);
            String returnSaveBoProductAttr = soBoProductAttr(boProductAttrObj.toJSONString());

            if(!ProtocolUtil.validateReturnJson(returnSaveBoProductAttr,paramJson)){

                throw new RuntimeException("保存 bo_product 失败："+boProductAttrObj+(paramJson != null
                        && paramJson.containsKey("RESULT_MSG")?paramJson.getString("RESULT_MSG"):"未知异常"));
            }
        }
    }

    /**
     * 作废 boProduct 信息
     * @param data
     * @throws Exception
     */
    public void doDeleteBoProduct(JSONObject data) throws Exception{


        Product deleteProduct = null;
        //根据boId 查询bo_product 表，是否有数据，没数据直接返回
        BoProduct boProduct = new BoProduct();

        boProduct.setBoId(data.getString("boId"));

       List<BoProduct> boProducts =  iProductServiceDao.queryBoProduct(boProduct);

       //Assert.isOne(boProducts,"在表bo_product中未找到boId 为["+data.getString("boId")+"]的数据 或有多条数据，请检查");
        if(boProducts == null || boProducts.size() < 1){
            LoggerEngine.error("当前没有查到数为 "+data+"请检查数据");
            return;
        }
       //在过程表中补一条作废的数据，然后根据boId的动作对实例数据进行处理

        boProduct.setProductId(boProducts.get(0).getProductId());
        boProduct.setBoId("");
        //查询出所有productId 一样的数据
        List<BoProduct> boProductAll =  iProductServiceDao.queryBoProduct(boProduct);

        Assert.isNull(boProductAll,"当前没有查到productId 为 "+boProducts.get(0).getProductId()+"请检查数据");

        boProduct = boProducts.get(0);

        BoProduct newBoProduct = new BoProduct();
        newBoProduct.setBoId(data.getString("newBoId"));
        newBoProduct.setProductId(boProduct.getProductId());
        newBoProduct.setState("DEL");
        int saveBoProductFlag = iProductServiceDao.saveDataToBoProduct(newBoProduct);

        if(saveBoProductFlag < 1){
            throw new RuntimeException("向bo_product表中保存数据失败，boProduct="+JSONObject.toJSONString(newBoProduct));
        }

        //首先删除实例数据
        deleteProduct = new Product();
        deleteProduct.setProductId(boProduct.getProductId());
        if(iProductServiceDao.deleteDataToProduct(deleteProduct) < 1){
            throw new RuntimeException("删除product实例数据失败"+JSONObject.toJSONString(deleteProduct));
        }
        //如果有多条数据，则恢复 前一条数据信息，这边存在bug 如果上一条的数据没有分装以前数据的情况下会有问题，
        // 所以我们的原则是再更新或删除数据时一定要在过程表中保存完整是实例数据信息
        if(boProductAll.size() > 1){
            Product oldProduct = boProductAll.get(1).convert();
            if(iProductServiceDao.saveDataToProduct(oldProduct)<1 ){
                throw new RuntimeException("product 表恢复老数据信息失败，product 为："+JSONObject.toJSONString(oldProduct));
            }
        }
    }

    /**
     * 删除 bo_product_attr
     * @param data
     * @throws Exception
     */
    public void doDeleteBoProductAttr(JSONObject data) throws Exception{

        BoProductAttr boProductAttrTmp = new BoProductAttr();

        boProductAttrTmp.setBoId(data.getString("boId"));

        List<BoProductAttr> boProductAttrs = iProductServiceDao.queryBoProductAttr(boProductAttrTmp);

        if(boProductAttrs == null || boProductAttrs.size() < 1){
            LoggerEngine.error("当前没有查到数为 "+data+"请检查数据");
            return;
        }

        boProductAttrTmp.setBoId("");
        boProductAttrTmp.setProductId(boProductAttrs.get(0).getProductId());

        List<BoProductAttr> boProductAttrsTmps = iProductServiceDao.queryBoProductAttr(boProductAttrTmp);

        Assert.isNull(boProductAttrsTmps,"当前没有查到productId 为 "+boProductAttrs.get(0).getProductId()+"请检查数据");

        //获取上一次所有的属性

        List<BoProductAttr> preBoProductAttrTmps = getPreBoProductAttrs(boProductAttrsTmps);

        //保存过程表
        for(BoProductAttr boProductAttr : boProductAttrs){
            boProductAttr.setBoId("newBoId");
            boProductAttr.setState("DEL");
            if(iProductServiceDao.saveDataToBoProductAttr(boProductAttr) < 1){
                throw new RuntimeException("保存数据失败，保存数据为boProductAttr = "+ JSONObject.toJSONString(boProductAttr));
            }
        }

        //删除实例数据 这里思路是，删除实例数据中数据，将上一次ADD数据重新写一遍
        ProductAttr productAttrTmp = new ProductAttr();
        productAttrTmp.setProductId(boProductAttrs.get(0).getProductId());
        if(iProductServiceDao.deleteDataToProductAttr(productAttrTmp) < 1){
            throw new RuntimeException("删除ProductAttr 实例数据失败,数据为："+JSONObject.toJSONString(productAttrTmp));
        }

        for(BoProductAttr boProductAttr : preBoProductAttrTmps){
            if("ADD".equals(boProductAttr.getState())){
                if(iProductServiceDao.deleteDataToProductAttr(boProductAttr.convert()) < 1){
                    throw new  RuntimeException("复原原始数据失败，数据为：" + JSONObject.toJSONString(boProductAttr));
                }
            }
        }

    }

    /**
     * 获取上上一次的操作
     * @param boProductAttrs
     * @return
     */
    private List<BoProductAttr> getPreBoProductAttrs(List<BoProductAttr> boProductAttrs){

        String firstBoId = boProductAttrs.get(0).getBoId();
        String preBoId = "";
        List<BoProductAttr> preBoProductAttrs = new ArrayList<BoProductAttr>();
        for(BoProductAttr boProductAttr : boProductAttrs){
            if(!firstBoId.equals(boProductAttr.getBoId())){
                if(!preBoId.equals(boProductAttr.getBoId()) && !"".equals(preBoId)){
                    break;
                }
                preBoId = boProductAttr.getBoId();
                preBoProductAttrs.add(boProductAttr);
            }
        }
        return preBoProductAttrs;
    }

    /**
     * 查询客户订单信息
     * @param busiOrderStr 订单项信息
     * @param isNeedDelete 是否是撤单报文 true 查询撤单报文 false
     * @return
     * @throws Exception
     */
    private String doQueryProductInfoByOlId(String busiOrderStr,Boolean isNeedDelete) throws Exception{
        BusiOrder busiOrder = JSONObject.parseObject(busiOrderStr, BusiOrder.class);

        if(busiOrder == null || "".equals(busiOrder.getOlId())){
            throw new IllegalArgumentException("产品信息查询入参为空，olId 为空 "+busiOrderStr);
        }

        Product cust = new Product();
        cust.setVersionId(busiOrder.getOlId());
        //根据版本ID查询实例数据
        Product newProduct = iProductServiceDao.queryDataToProduct(cust);
        JSONObject returnJson = JSONObject.parseObject("{'data':{}}");
        if(newProduct == null){
            return returnJson.toJSONString();
        }

        BoProduct boProduct = new BoProduct();

        boProduct.setBoId(busiOrder.getBoId());
        boProduct.setProductId(newProduct.getProductId());
        boProduct.setVersionId(busiOrder.getOlId());

        List<BoProduct> boProducts =  iProductServiceDao.queryBoProduct(boProduct);


        //一般情况下没有这种情况存在，除非 人工 改了数据，或没按流程完成数据处理
        if(boProducts == null || boProducts.size() == 0){
            return returnJson.toJSONString();
        }


        JSONArray boProductArray = new JSONArray();
        //单纯的删除 和单纯 增加
        for(int boProductIndex = 0 ; boProductIndex < boProducts.size();boProductIndex++) {
            BoProduct newBoProduct = boProducts.get(boProductIndex);
            if(isNeedDelete) {
                if (StateConstant.STATE_DEL.equals(newBoProduct.getState())) {
                    newBoProduct.setBoId("");
                    newBoProduct.setState(StateConstant.STATE_ADD);
                } else if (StateConstant.STATE_ADD.equals(newBoProduct.getState())) {
                    newBoProduct.setState(StateConstant.STATE_DEL);
                } else {
                    newBoProduct.setState(StateConstant.STATE_KIP);
                }
            }
            boProductArray.add(newBoProduct);
        }
        returnJson.getJSONObject("data").put("boProduct",JSONObject.toJSONString(boProductArray));


        //属性处理
        ProductAttr oldProductAttr = new ProductAttr();
        oldProductAttr.setProductId(newProduct.getProductId());
        oldProductAttr.setVersionId(busiOrder.getOlId());
        List<ProductAttr> custAttrs = iProductServiceDao.queryDataToProductAttr(oldProductAttr);
        if(custAttrs == null || custAttrs.size() == 0){
            return returnJson.toJSONString();
        }
        /**
         * 查询客户查询的过程数据
         */
        BoProductAttr boProductAttr = new BoProductAttr();
        boProductAttr.setProductId(newProduct.getProductId());
        boProductAttr.setVersionId(busiOrder.getOlId());
        List<BoProductAttr> boProductAttrs = iProductServiceDao.queryBoProductAttr(boProductAttr);


        //一般情况下没有这种情况存在，除非 人工 改了数据，或没按流程完成数据处理
        if(boProductAttrs == null || boProductAttrs.size() == 0){
            return returnJson.toJSONString();
        }


        JSONArray boProductAttrArray = new JSONArray();
        //单纯的删除 和单纯 增加
        for(BoProductAttr newBoProductAttr : boProductAttrs) {
            if(isNeedDelete) {
                if (StateConstant.STATE_DEL.equals(newBoProductAttr.getState())) {
                    newBoProductAttr.setBoId("");
                    newBoProductAttr.setState(StateConstant.STATE_ADD);
                } else if (StateConstant.STATE_ADD.equals(newBoProductAttr.getState())) {
                    newBoProductAttr.setState(StateConstant.STATE_DEL);
                } else {
                    newBoProductAttr.setState(StateConstant.STATE_KIP);
                }
            }
            boProductAttrArray.add(newBoProductAttr);
        }

        returnJson.getJSONObject("data").put("boProductAttr",JSONObject.toJSONString(boProductAttrArray));

        return returnJson.toJSONString();

    }

    public IPrimaryKeyService getiPrimaryKeyService() {
        return iPrimaryKeyService;
    }

    public void setiPrimaryKeyService(IPrimaryKeyService iPrimaryKeyService) {
        this.iPrimaryKeyService = iPrimaryKeyService;
    }

    public IProductServiceDao getiProductServiceDao() {
        return iProductServiceDao;
    }

    public void setiProductServiceDao(IProductServiceDao iProductServiceDao) {
        this.iProductServiceDao = iProductServiceDao;
    }
}
