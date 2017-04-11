package com.java110.order.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.entity.order.OrderList;
import com.java110.feign.base.IPrimaryKeyService;
import com.java110.order.smo.IOrderServiceSMO;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单服务业务逻辑处理实现类
 * Created by wuxw on 2017/4/11.
 */
@Service("orderServiceSMOImpl")
public class OrderServiceSMOImpl extends BaseServiceSMO implements IOrderServiceSMO {

    @Autowired
    IPrimaryKeyService iPrimaryKeyService;

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




        return null;
    }

    public IPrimaryKeyService getiPrimaryKeyService() {
        return iPrimaryKeyService;
    }

    public void setiPrimaryKeyService(IPrimaryKeyService iPrimaryKeyService) {
        this.iPrimaryKeyService = iPrimaryKeyService;
    }
}
