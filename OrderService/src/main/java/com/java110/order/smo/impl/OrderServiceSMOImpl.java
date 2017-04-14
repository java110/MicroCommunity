package com.java110.order.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.ProtocolUtil;
import com.java110.config.properties.EventProperties;
import com.java110.core.base.smo.BaseServiceSMO;
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
     * 订单调度
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

        for(int busiOrderTmpsIndex = 0 ; busiOrderTmpsIndex < busiOrderTmps.size() ; busiOrderTmpsIndex++){
            JSONObject busiOrderJson = busiOrderTmps.getJSONObject(busiOrderTmpsIndex);
            if (!busiOrderJson.containsKey("busiObj")){
                throw new IllegalArgumentException("请求报文中busiOrder 节点中没有对应的 busiObj 节点，请检查"+busiOrderJson);
            }

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
            if (!busiOrderJson.containsKey("data")){
                throw new IllegalArgumentException("请求报文中busiOrder 节点中没有对应的 data 节点，请检查"+busiOrderJson);
            }

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

            String orderDispathListener = eventProperties.getOrderDispatchListener();

            if(StringUtils.isBlank(orderDispathListener)){
                throw new RuntimeException("订单调度引擎没有配置，请在event.properties 文件中配置");
            }

            String[] dispathListeners = orderDispathListener.split("\\.");

        }




        return null;
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
