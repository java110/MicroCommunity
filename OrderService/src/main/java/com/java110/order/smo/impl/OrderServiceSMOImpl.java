package com.java110.order.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.ProtocolUtil;
import com.java110.config.properties.EventProperties;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.context.AppContext;
import com.java110.core.event.AppEventPublishing;
import com.java110.entity.order.BusiOrder;
import com.java110.entity.order.BusiOrderAttr;
import com.java110.entity.order.OrderList;
import com.java110.entity.order.OrderListAttr;
import com.java110.feign.base.IPrimaryKeyService;
import com.java110.order.dao.IOrderServiceDao;
import com.java110.order.smo.IOrderServiceSMO;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.java110.common.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 根据购物车ID 或者 外部系统ID 或者 custId 或者 channelId 查询订单信息
     * @param orderList
     * @return
     */
    @Override
    public String queryOrderInfo(OrderList orderList) throws Exception{


        List<OrderList> orderLists = iOrderServiceDao.queryOrderListAndAttr(orderList);
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
        orderListTmpO.put("orderLists",orderListsArray);

        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"查询成功",orderListTmpO);
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

            /*
            try {
                //发布事件
                AppEventPublishing.multicastEvent(actionTypeCd,orderInfo.toJSONString(), data.toJSONString(),orderListTmp.getString("asyn"));
            }catch (Exception e){
                //这里补偿事物
                throw e;
            }*/

        }

        //创建上下文对象
        AppContext context = createApplicationContext();

        prepareContext(context, datasTmp);

        //发布事件
        AppEventPublishing.multicastEvent(context,datasTmp,orderListTmp.getString("asyn"));

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

        if(!busiOrderTmps.getJSONObject(0).containsKey("oldBoId")){
           String actionTypeCds = busiOrderTmps.getJSONObject(0).getString("actionTypeCd");
            deleteOrderByActionTypeCd(orderListTmp.getString("oldOlId"),actionTypeCds.split(","));
            return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS,"成功",JSONObject.parseObject(JSONObject.toJSONString(orderList)));
        }



        return null;
    }

    /**
     * 根据 订单动作 作废
     * @param oldOlId 作废的购物车
     * @param actionTypeCd busi_order action_type_cd 类型来作废订单
     * @throws Exception
     */
    private void deleteOrderByActionTypeCd(String oldOlId,String ...actionTypeCd) throws Exception{
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

        busiOrderTmp.setActionTypeCd(actionTypeCds);

        List<BusiOrder> busiOrders =  iOrderServiceDao.queryBusiOrderAndAttr(busiOrderTmp);

        Assert.isNull(busiOrders,"没有找到需要作废的订单，[oldOdId="+oldOlId+",actionTypeCd = "+actionTypeCd+"]");


    }

    private void prepareContext(AppContext context,Map<String,JSONArray> datasTmp){
        Assert.isNull(context,"创建上下对象失败");

        //这里将整个订单的data 信息存入 上下文对象中，以防后期使用无法获取

        context.coverData(datasTmp);

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
