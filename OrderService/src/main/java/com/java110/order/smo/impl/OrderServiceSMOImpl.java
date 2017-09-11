package com.java110.order.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ActionTypeConstant;
import com.java110.common.constant.AttrCdConstant;
import com.java110.common.constant.CommonConstant;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.ProtocolUtil;
import com.java110.config.properties.EventProperties;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.context.AppContext;
import com.java110.event.AppEventPublishing;
import com.java110.entity.order.BusiOrder;
import com.java110.entity.order.BusiOrderAttr;
import com.java110.entity.order.OrderList;
import com.java110.entity.order.OrderListAttr;
import com.java110.feign.base.IPrimaryKeyService;
import com.java110.order.dao.IOrderServiceDao;
import com.java110.order.mq.DeleteOrderInfoProducer;
import com.java110.order.smo.IOrderServiceSMO;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.java110.common.util.Assert;

import java.util.*;

/**
 * 订单服务业务逻辑处理实现类
 * Created by wuxw on 2017/4/11.
 */
@Service("orderServiceSMOImpl")
@Transactional
public class OrderServiceSMOImpl extends BaseServiceSMO implements IOrderServiceSMO {

    @Autowired
    IPrimaryKeyService iPrimaryKeyService;

    @Autowired
    IOrderServiceDao iOrderServiceDao;

    @Autowired
    EventProperties eventProperties;

    @Autowired
    DeleteOrderInfoProducer deleteOrderInfoProducer;

    /**
     * 根据购物车ID 或者 外部系统ID 或者 custId 或者 channelId 查询订单信息
     *
     * 返回报文格式如下：
     *
     * {

     "orderList": [{

     "orderListAttrs": [{···}],

     "busiOrder": [

     {

     "data": {

     "boCust": [{···}],

     "boCustAttr": [ {···}]

     },

     "busiObj": {····},

     "busiOrderAttrs": [{···}]

     }

     ],

     "orderListInfo": {···}

     }]
     }

     * @param orderList
     * @return
     */
    @Override
    public String queryOrderInfo(OrderList orderList) throws Exception{
        return queryOrderInfo(orderList,true);
    }

    /**
     * 根据购物车ID 或者 外部系统ID 或者 custId 或者 channelId 查询订单信息
     *
     * 返回报文格式如下：
     *
     * {

         "orderList": [{

         "orderListAttrs": [{···}],

         "busiOrder": [

             {

                 "data": {

                 "boCust": [{···}],

                 "boCustAttr": [ {···}]

                 },

                 "busiObj": {····},

                 "busiOrderAttrs": [{···}]

             }

         ],

         "orderListInfo": {···}

         }]
     }

     * @param orderList
     * @param isQueryDataInfo 是 查询data节点 否不查询data节点
     * @return
     */
    @Override
    public String queryOrderInfo(OrderList orderList,Boolean isQueryDataInfo) throws Exception{


        /*List<OrderList> orderLists = iOrderServiceDao.queryOrderListAndAttr(orderList);
        //
        JSONArray orderListsArray = new JSONArray();
        for (OrderList orderListTmp : orderLists){
            //
            BusiOrder busiOrderTmp = new BusiOrder();
            busiOrderTmp.setBoId(orderListTmp.getOlId());

            List<BusiOrder> busiOrders = iOrderServiceDao.queryBusiOrderAndAttr(busiOrderTmp);

            JSONObject orderListJSON = JSONObject.parseObject(JSONObject.toJSONString(orderListTmp));

            orderListJSON.put("busiOrders",JSONObject.parseArray(JSONObject.toJSONString(busiOrders)));

            orderListsArray.add(orderListJSON);
        }

        JSONObject orderListTmpO = new JSONObject();
        orderListTmpO.put("orderList",orderListsArray);*/

        List<OrderList> orderLists = iOrderServiceDao.queryOrderListAndAttr(orderList);
        //多个购物车封装
        JSONArray orderListsArray = new JSONArray();

        JSONObject orderListTmpJson = null;

        for(OrderList orderListTmp : orderLists){
            orderListTmpJson = new JSONObject();
            //封装orderListAttrs
            orderListTmpJson.put("orderListAttrs",JSONArray.parseArray(JSONObject.toJSONString(orderListTmp.getOrderListAttrs())));

            orderListTmpJson.put("orderListInfo",JSONObject.parseObject(JSONObject.toJSONString(orderListTmp)).remove("orderListAttrs"));


            BusiOrder busiOrderTmp = new BusiOrder();
            busiOrderTmp.setBoId(orderListTmp.getOlId());

            List<BusiOrder> busiOrders = iOrderServiceDao.queryBusiOrderAndAttr(busiOrderTmp);

            //封装busiObj
            JSONArray busiOrderTmpArray = new JSONArray();

            /**
             * [

             {

             "data": {

             "boCust": [{···}],

             "boCustAttr": [ {···}]

             },

             "busiObj": {····},

             "busiOrderAttrs": [{···}]

             }
             */
            JSONObject busiOrderTmpJson = null;
            for(BusiOrder busiOrderTmp1 : busiOrders){
                busiOrderTmpJson = new JSONObject();

                //封装busiOrderAttrs

                busiOrderTmpJson.put("busiOrderAttrs",JSONArray.parseArray(JSONObject.toJSONString(busiOrderTmp1.getBusiOrderAttrs())));


                //封装busiObj

                busiOrderTmpJson.put("busiObj",JSONObject.parseObject(JSONObject.toJSONString(busiOrderTmp1)).remove("busiOrderAttrs"));

                //封装data 节点
                if(isQueryDataInfo){

                }


                busiOrderTmpArray.add(busiOrderTmpJson);
            }


            //分装busiOrder

            orderListTmpJson.put("busiOrder",busiOrderTmpArray);

        }
        orderListsArray.add(orderListTmpJson);

        JSONObject orderListJson = new JSONObject();
        orderListJson.put("orderList",orderListsArray);


        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"查询成功",orderListJson);
    }

    /**
     * 订单调度
     *
     * orderListInfo 中字段 asyn 如果为 A 表示 异步处理订单，其他表同步处理订单
     * @param orderInfo 订单信息
     * @return 订单处理接口
     * @throws Exception
     */
    @Override
    public String orderDispatch(JSONObject orderInfo) throws Exception {

        //1.0 购物车信息校验处理,走订单受理必须要有购物车信息和订单项信息
        if(!orderInfo.containsKey("orderListInfo") || !orderInfo.containsKey("busiOrder")){
            throw  new IllegalArgumentException("请求报文中没有购物车相关信息[orderListInfo]或订单项相关信息[busiOrder],请检查报文："+orderInfo);
        }

        JSONObject orderListTmp = orderInfo.getJSONObject("orderListInfo");

        OrderList orderList = JSONObject.parseObject(orderListTmp.toJSONString(),OrderList.class);

        String olId = orderList.getOlId();
        //生成olId
        if(StringUtils.isBlank(olId) || olId.startsWith("-") ){
            olId = this.queryPrimaryKey(iPrimaryKeyService,"OL_ID");
            orderList.setOlId(olId);
            orderListTmp.put("olId",olId);
        }

        //这里保存购物车

        int saveOrderListFlag = iOrderServiceDao.saveDataToBoOrderList(orderList);
        if (saveOrderListFlag < 1){
            throw  new RuntimeException("保存购物车信息失败"+orderListTmp);
        }

        //保存购物车属性

        if(orderInfo.containsKey("orderListAttrs")){
            JSONArray orderListAttrs = orderInfo.getJSONArray("orderListAttrs");

            List<OrderListAttr> orderListAttrsTmp = JSONObject.parseArray(orderListAttrs.toJSONString(), OrderListAttr.class);

            for(OrderListAttr orderListAttr : orderListAttrsTmp){
                orderListAttr.setOlId(olId);
                saveOrderListFlag = iOrderServiceDao.saveDataToOrderListAttr(orderListAttr);
                if(saveOrderListFlag < 1){
                    throw new RuntimeException("保存购物车属性信息失败"+JSONObject.toJSONString(orderListAttr));
                }
            }
        }


        //获取 订单项
        JSONArray busiOrderTmps = orderInfo.getJSONArray("busiOrder");

        //存放busiOrder 的data节点

        Map<String,JSONArray> datasTmp = new HashMap<String, JSONArray>();

        for(int busiOrderTmpsIndex = 0 ; busiOrderTmpsIndex < busiOrderTmps.size() ; busiOrderTmpsIndex++){
            JSONObject busiOrderJson = busiOrderTmps.getJSONObject(busiOrderTmpsIndex);
            /*if (!busiOrderJson.containsKey("busiObj")){
                throw new IllegalArgumentException("请求报文中busiOrder 节点中没有对应的 busiObj 节点，请检查"+busiOrderJson);
            }*/

            Assert.isNull(busiOrderJson,"busiObj","请求报文中busiOrder 节点中没有对应的 busiObj 节点，请检查"+busiOrderJson);

            BusiOrder busiOrderObj = JSONObject.parseObject(busiOrderJson.getJSONObject("busiObj").toJSONString(),BusiOrder.class);

            String boId = busiOrderObj.getBoId();
            //生成 订单项ID
            if(StringUtils.isBlank(boId) || boId.startsWith("-")){
                boId = this.queryPrimaryKey(iPrimaryKeyService,"BO_ID");
            }
            busiOrderObj.setOlId(olId);
            //修改boId
            busiOrderObj.setBoId(boId);

            //这里保存订单项 busiOrder
            int saveBusiOrderFlag = iOrderServiceDao.saveDataToBusiOrder(busiOrderObj);
            if(saveBusiOrderFlag < 1){
                throw new RuntimeException("保存订单项信息失败"+JSONObject.toJSONString(busiOrderObj));
            }

            //如果有busiOrderAttrs 节点 每个节点添加 boId
            if(busiOrderJson.containsKey("busiOrderAttrs")){
                List<BusiOrderAttr> busiOrderAttrsTmp = JSONObject.parseArray(busiOrderJson.getJSONArray("busiOrderAttrs").toJSONString(),
                        BusiOrderAttr.class);

                for (BusiOrderAttr busiOrderAttrTmp : busiOrderAttrsTmp){
                    busiOrderAttrTmp.setBoId(boId);
                    //这里保存订单项属性
                    saveBusiOrderFlag =  iOrderServiceDao.saveDataToBusiOrderAttr(busiOrderAttrTmp);
                    if(saveBusiOrderFlag < 1){
                        throw new RuntimeException("保存订单项信息属性失败"+JSONObject.toJSONString(busiOrderAttrTmp));
                    }
                }
            }
            //修改data节点下的boId，一般是没有这个值，所以直接新加就行了，不许判断是否已-开头
           /* if (!busiOrderJson.containsKey("data")){
                throw new IllegalArgumentException("请求报文中busiOrder 节点中没有对应的 data 节点，请检查"+busiOrderJson);
            }*/

            Assert.isNull(busiOrderJson,"data","请求报文中busiOrder 节点中没有对应的 data 节点，请检查"+busiOrderJson);

            //处理data 节点
            JSONObject data = busiOrderJson.getJSONObject("data");

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Object valueObj = entry.getValue();
                if (valueObj instanceof JSONObject){
                    ((JSONObject)valueObj).put("boId",boId);
                }else if(valueObj instanceof JSONArray){
                    JSONArray valueArray = (JSONArray)valueObj;
                    for(int valueIndex = 0 ; valueIndex < valueArray.size(); valueIndex++){
                        valueArray.getJSONObject(valueIndex).put("boId",boId);
                    }
                }
            }

            LoggerEngine.debug("处理后的data节点 ：" + data.toString());

            //根据busiOrder 的  actionTypeCd 注册那个服务去处理
            String actionTypeCd = busiOrderObj.getActionTypeCd();

            JSONArray dataJsonTmp = null;
            if(!datasTmp.containsKey(actionTypeCd)){
                dataJsonTmp = new JSONArray();
            }else{
                dataJsonTmp = datasTmp.get(actionTypeCd);
            }
            data.put("actionTypeCd",actionTypeCd);
            dataJsonTmp.add(data);

            datasTmp.put(actionTypeCd,dataJsonTmp);

        }

        //创建上下文对象
        AppContext context = createApplicationContext();

        prepareContext(context, datasTmp);
       /* try {*/
            //发布事件
            AppEventPublishing.multicastEvent(context,datasTmp,orderListTmp.getString("asyn"));
       /* }catch (Exception e){
            //这里补偿事物,这里发布广播
            compensateTransactional(datasTmp);
            throw e;
        }*/
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",JSONObject.parseObject(JSONObject.toJSONString(orderList)));
    }

    /**
     * 作废订单
     * 根据业务动作作废
     * 请求协议：
     * {
     *     "orderList":{
     *     "transactionId": "1000000200201704113137002690",
             "channelId": "700212896",
             "remarks": "",
             "custId": "701008023904",
             "statusCd": "S",
             "reqTime": "20170411163709",
             "extSystemId": "310013698777",
             "olTypeCd": "15",
     *        "oldOlId":"123456789",
     *        "asyn":"S"
     *     },
     *     "busiOrder":[{
     *         "actionTypeCd":"ALL"
     *     }]
     * }
     *
     * ，
     *
     * 根据 订单项ID作废
     *
     * {
     *     "orderList":{
     *         "transactionId": "1000000200201704113137002690",
     "channelId": "700212896",
     "remarks": "",
     "custId": "701008023904",
     "statusCd": "S",
     "reqTime": "20170411163709",
     "extSystemId": "310013698777",
     "olTypeCd": "15",
     "asyn":"A"
     *     },
     *     "busiOrder":[{
     *          "oldBoId":"123456789"
     *     },
     *     {
     *          "oldBoId":"123456799"
     *     }]
     * }
     * @param orderInfo
     * @return
     * @throws Exception
     */
    @Override
    public String deleteOrder(JSONObject orderInfo) throws Exception {
        //1.0 购物车信息校验处理,走订单受理必须要有购物车信息和订单项信息
        if(!orderInfo.containsKey("orderList") || !orderInfo.containsKey("busiOrder")){
            throw  new IllegalArgumentException("请求报文中没有购物车相关信息[orderList]或订单项相关信息[busiOrder],请检查报文："+orderInfo);
        }

        JSONObject orderListTmp = orderInfo.getJSONObject("orderList");
        OrderList orderList = JSONObject.parseObject(orderListTmp.toJSONString(),OrderList.class);

        String olId = orderList.getOlId();
        //生成olId
        if(StringUtils.isBlank(olId) || olId.startsWith("-") ){
            olId = this.queryPrimaryKey(iPrimaryKeyService,"OL_ID");
            orderList.setOlId(olId);
        }

        //这里保存购物车

        int saveOrderListFlag = iOrderServiceDao.saveDataToBoOrderList(orderList);
        if (saveOrderListFlag < 1){
            throw  new RuntimeException("作废订单时保存购物车信息失败"+orderListTmp);
        }

        JSONArray busiOrderTmps = orderInfo.getJSONArray("busiOrder");

        /**
         * 根据actionTypeCd 作废
         */
        Assert.isNull(busiOrderTmps,"入参错误，没有busiOrder 节点，或没有子节点");

        List<Map<String,String>> needDeleteBoIds = new ArrayList<Map<String,String>>();

        if(busiOrderTmps.getJSONObject(0).containsKey("actionTypeCd")){
           String actionTypeCds = busiOrderTmps.getJSONObject(0).getString("actionTypeCd");
            getNeeddeleteOrderByActionTypeCd(orderListTmp.getString("oldOlId"),needDeleteBoIds,actionTypeCds.split(","));
            //return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",JSONObject.parseObject(JSONObject.toJSONString(orderList)));
        }else if(busiOrderTmps.getJSONObject(0).containsKey("oldBoId")){
            Map<String,String> oldBoIdMap = null;
            for(int busiOrderIndex = 0; busiOrderIndex< busiOrderTmps.size();busiOrderIndex++){
                oldBoIdMap = new HashMap<String, String>();
                oldBoIdMap.put("actionTypeCd","");
                oldBoIdMap.put("boId",busiOrderTmps.getJSONObject(busiOrderIndex).getString("oldBoId"));
                oldBoIdMap.put("olId","");
                needDeleteBoIds.add(oldBoIdMap);
            }
        }else {
            throw new IllegalArgumentException("当前系统只支持busiOrder 节点下第一个节点包含 actionTypeCd节点和 oldOlId节点的报文："+orderInfo);
        }

        //数据分装
        Map<String,JSONArray> datasTmp = new HashMap<String, JSONArray>();
        //添加数据至 busi_order,这里生成新的boId 将需要作废的boId信息写入值busi_order_attr 中单独注册一个属性信息

        for(Map<String,String> needDeleteBoIdMap : needDeleteBoIds){
            BusiOrder busiOrder = new BusiOrder();

            String newBoId = this.queryPrimaryKey(iPrimaryKeyService,"BO_ID");

            busiOrder.setOlId(olId);
            //重新生成 boId
            busiOrder.setBoId(newBoId);
            //设置撤单，作废订单动作
            busiOrder.setActionTypeCd(ActionTypeConstant.ACTION_TYPE_CANCEL_ORDER);

            busiOrder.setRemark("撤单，作废订单处理，作废订单为"+needDeleteBoIdMap.get("boId"));

            //这里保存订单项 busiOrder
            int saveBusiOrderFlag = iOrderServiceDao.saveDataToBusiOrder(busiOrder);
            if(saveBusiOrderFlag < 1){
                throw new RuntimeException("撤单，作废订单失败，保存订单项信息失败"+JSONObject.toJSONString(busiOrder));
            }

            //将需要作废的订单boId 写入值 busi_order_attr 中 属性为： 10000001

            BusiOrderAttr busiOrderAttr = new BusiOrderAttr();
            busiOrderAttr.setBoId(newBoId);
            busiOrderAttr.setAttrCd(AttrCdConstant.BUSI_ORDER_ATTR_10000001);
            busiOrderAttr.setValue(needDeleteBoIdMap.get("boId"));
            needDeleteBoIdMap.put("newBoId",newBoId);

            saveBusiOrderFlag =  iOrderServiceDao.saveDataToBusiOrderAttr(busiOrderAttr);
            if(saveBusiOrderFlag < 1){
                throw new RuntimeException("撤单，作废订单失败,保存订单项信息属性失败"+JSONObject.toJSONString(busiOrderAttr));
            }

            //封装数据
            processDeleteOrderByActionTypeCd(needDeleteBoIdMap,datasTmp);

        }

        //这里补充 order_list_attr中 编码为 10000002 的数据，要作废 订单购物车信息，真正作废单子是以 busi_order_attr 中的boId 为主
        Assert.hasSize(datasTmp,"当前没有可作废的订单，请核实");
        //由于撤单，作废订单我们只支持一个购物车操作
        Set keys = datasTmp.keySet();
        Object key =  keys.toArray()[0];
        Assert.isNull(datasTmp.get(key).getJSONObject(0),"olId","数据错误，需要作废的订单的第一个节点为空，或不包含olId节点，请核查"+datasTmp);
        String oldOlId = datasTmp.get(key).getJSONObject(0).getString("olId");


        OrderListAttr orderListAttr = new OrderListAttr();
        orderListAttr.setOlId(olId);
        orderListAttr.setAttrCd(AttrCdConstant.ORDER_LIST_ATTR_10000002);
        orderListAttr.setValue(oldOlId);
        saveOrderListFlag = iOrderServiceDao.saveDataToOrderListAttr(orderListAttr);
        if(saveOrderListFlag < 1){
            throw new RuntimeException("保存购物车属性信息失败"+JSONObject.toJSONString(orderListAttr));
        }




        //创建上下文对象
        AppContext context = createApplicationContext();

        prepareContext(context, datasTmp);
        try {
            //发布事件
            AppEventPublishing.multicastEvent(context,datasTmp,orderListTmp.getString("asyn"));
        }catch (Exception e){
            //这里补偿事物
            compensateTransactional(datasTmp);
            throw e;
        }
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",JSONObject.parseObject(JSONObject.toJSONString(orderList)));

    }

    /**
     * 撤单处理 add by wuxw 2017-09-10 22:35
     * 修改以前逻辑，根据olId 去目标系统查询需要查询撤单订单组装报文
     * @param orderInfo
     * @throws Exception
     */
    public void soDeleteOrder(JSONObject orderInfo) throws Exception{

        //1.0 购物车信息校验处理,走订单受理必须要有购物车信息和订单项信息
        if(!orderInfo.containsKey("orderListInfo") || !orderInfo.containsKey("busiOrder")){
            return;
        }

        JSONObject orderListTmp = orderInfo.getJSONObject("orderListInfo");

        OrderList orderList = JSONObject.parseObject(orderListTmp.toJSONString(),OrderList.class);

        String olId = orderList.getOlId();
        //生成olId
        if(StringUtils.isBlank(olId) || olId.startsWith("-") ){
           return ;
        }

        // 查询购物车信息，订单项信息
        String oldOrderInfo = this.queryOrderInfo(orderList,false);

        JSONObject oldOrderInfoJson = ProtocolUtil.getObject(oldOrderInfo,JSONObject.class);

        oldOrderInfoJson.getJSONArray("orderLists");
        //重新发起撤单订单
        orderDispatch(null);
    }

    /**
     * 根据 订单动作 作废
     * @param oldOlId 作废的购物车
     * @param actionTypeCd busi_order action_type_cd 类型来作废订单
     * @throws Exception
     */
    private void getNeeddeleteOrderByActionTypeCd(String oldOlId,List<Map<String,String>> needDeleteBoIds,String ...actionTypeCd) throws Exception{
        //根据oldOdId actionTypeCd 获取订单项
        BusiOrder busiOrderTmp = new BusiOrder();
        busiOrderTmp.setOlId(oldOlId);
        String actionTypeCds= "";
        // 'C1','A1','M1',
        for(String ac : actionTypeCd){
            actionTypeCds += ("'"+ac+"',");
        }

        // 'C1','A1','M1'
        actionTypeCds = actionTypeCds.endsWith(",")?actionTypeCds.substring(0,actionTypeCds.length()-1):actionTypeCds;

        //如果为 ALL 作废整个订单数据，这里直接传为空 根据olId 处理
        if("ALL".equals(actionTypeCds)){

            actionTypeCds = "";
        }

        busiOrderTmp.setActionTypeCd(actionTypeCds);

        /**
         * 已经生成的订单项信息
         */
        List<BusiOrder> oldBusiOrders =  iOrderServiceDao.queryBusiOrderAndAttr(busiOrderTmp);

        Assert.isNull(oldBusiOrders,"没有找到需要作废的订单，[oldOdId="+oldOlId+",actionTypeCd = "+actionTypeCd+"]");

        //作废老订单信息
        Map<String,String> oldBoIdMap = null;
        for(BusiOrder oldBusiOrder : oldBusiOrders){
            oldBoIdMap = new HashMap<String, String>();
            oldBoIdMap.put("actionTypeCd",oldBusiOrder.getActionTypeCd());
            oldBoIdMap.put("boId",oldBusiOrder.getBoId());
            oldBoIdMap.put("olId",oldBusiOrder.getOlId());
            needDeleteBoIds.add(oldBoIdMap);
        }
    }

    private void prepareContext(AppContext context,Map<String,JSONArray> datasTmp){
        Assert.isNull(context,"创建上下对象失败");

        //这里将整个订单的data 信息存入 上下文对象中，以防后期使用无法获取

        context.coverData(datasTmp);

    }


    /**
     *
     * oldBoIdMap.put("actionTypeCd","");
     *
     *  oldBoIdMap.put("boId",busiOrderTmps.getJSONObject(busiOrderIndex).getString("oldBoId"));
     *  oldBoIdMap.put("olId","");
     * @param needDeleteBoIdMap
     *
     */
    private void processDeleteOrderByActionTypeCd(Map<String,String> needDeleteBoIdMap,Map<String,JSONArray> datasTmp){

        Assert.isNull(datasTmp,"processDeleteOrderByActionTypeCd 方法的参数 datasTmp 为空，");

        // 如果这两个中有一个为空，则从库中查询
        if(StringUtils.isBlank(needDeleteBoIdMap.get("newBoId")) || StringUtils.isBlank(needDeleteBoIdMap.get("actionTypeCd"))){
            BusiOrder busiOrderTmp = new BusiOrder();
            busiOrderTmp.setBoId(needDeleteBoIdMap.get("boId"));
            //这里只有一条其他，否则抛出异常
            List<BusiOrder> oldBusiOrders =  iOrderServiceDao.queryBusiOrderAndAttr(busiOrderTmp);

            if(oldBusiOrders == null || oldBusiOrders.size() != 1){
                throw new IllegalArgumentException("当前[boId="+needDeleteBoIdMap.get("boId")+"] 数据在busi_order表中不存在，请处理，很有可能是入参错误");
            }

            //回写数据

            needDeleteBoIdMap.put("olId",oldBusiOrders.get(0).getOlId());
            needDeleteBoIdMap.put("actionTypeCd",oldBusiOrders.get(0).getActionTypeCd()+ CommonConstant.SUFFIX_DELETE_ORDER);
        }

        String actionTypeCd = needDeleteBoIdMap.get("actionTypeCd");

        JSONArray dataJsonTmp = null;
        if(!datasTmp.containsKey(actionTypeCd)){
            dataJsonTmp = new JSONArray();
        }else{
            dataJsonTmp = datasTmp.get(actionTypeCd);
        }
        dataJsonTmp.add(JSONObject.parseObject(JSONObject.toJSONString(needDeleteBoIdMap)));
        datasTmp.put(actionTypeCd,dataJsonTmp);

        //deleteOrderInfoProducer.send(datasTmp.toString());
    }


    /**
     * 补偿事物，这里不用手工作废 购物车信息，事物会自动回退掉，这里这需要手工给其他的系统发布事物回退
     *
     * { 'data': [

     {
     'boId': '222222',
     'actionTypeCd': 'C1',
     'oldBoId':'11111'
     },
     {
     'boId': '222222',
     'actionTypeCd': 'M1',
     'oldBoId':'11111'
     },
     {
     'boId': '222222',
     'actionTypeCd': 'C1',
     'oldBoId':'11111'
     }
     ]
     }
     * @param datasTmp {C1={'data':[{}]}}
     */
    private void compensateTransactional(Map<String,JSONArray> datasTmp){

        Set<String> keys = datasTmp.keySet();

        JSONArray compensateDatas = new JSONArray();

        JSONObject compensateBoId = null;
        for(String key : keys){
            JSONArray datas = datasTmp.get(key);

            for(int dataIndex = 0 ; dataIndex <datas.size() ; dataIndex++){
                compensateBoId.put("boId",datas.getJSONObject(dataIndex).getString("boId"));
                compensateBoId.put("actionTypeCd",key);
                compensateDatas.add(compensateBoId);
            }
        }

        JSONObject compensateData = new JSONObject();

        compensateData.put("data",compensateDatas);

        deleteOrderInfoProducer.send(datasTmp.toString());
    }

    public IPrimaryKeyService getiPrimaryKeyService() {
        return iPrimaryKeyService;
    }

    public void setiPrimaryKeyService(IPrimaryKeyService iPrimaryKeyService) {
        this.iPrimaryKeyService = iPrimaryKeyService;
    }

    public IOrderServiceDao getiOrderServiceDao() {
        return iOrderServiceDao;
    }

    public void setiOrderServiceDao(IOrderServiceDao iOrderServiceDao) {
        this.iOrderServiceDao = iOrderServiceDao;
    }

    public EventProperties getEventProperties() {
        return eventProperties;
    }

    public void setEventProperties(EventProperties eventProperties) {
        this.eventProperties = eventProperties;
    }
}
