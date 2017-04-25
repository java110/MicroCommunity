package com.java110.order.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.java110.common.constant.CommonConstant;
import com.java110.common.util.Assert;
import com.java110.common.util.ProtocolUtil;
import com.java110.config.properties.OrderProperties;
import com.java110.core.context.AppContext;
import com.java110.core.event.AppCustEvent;
import com.java110.core.event.AppDeleteCustEvent;
import com.java110.core.event.AppListener;
import com.java110.core.event.Ordered;
import com.java110.feign.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 客户信息调度，主要用客户信息作废用，撤单用
 *
 *
 * Created by wuxw on 2017/4/14.
 */
@Component
public class DeleteCustDispatchListener implements AppListener<AppDeleteCustEvent> ,Ordered{

    @Autowired
    IUserService iUserService;

    @Autowired
    OrderProperties orderProperties;

    private final static int order = Ordered.dafultValue+2;
    @Override
    public void onJava110Event(AppDeleteCustEvent event) {

        //这里写 客户信息处理逻辑

       // AppContext context = event.getContext();

        JSONArray dataCustInfos = event.getCustData();

        JSONObject custInfoJson = new JSONObject();
        custInfoJson.put("data",dataCustInfos.toJSONString());

        String custInfo = custInfoJson.toJSONString();


        Assert.hasLength(custInfo,"没有需要处理的信息[custInfo="+custInfo+"]");

        /**
         * 同步处理
         */
        if(CommonConstant.PROCESS_ORDER_SYNCHRONOUS.equals(orderProperties.getDeleteOrderAsyn())){
            processSynchronous(custInfo);
        }else{
            //异步消息队里处理
            processAsynchronous(custInfo);
        }


    }

    /**
     * 同步方式处理
     * @param custInfo
     */
    private void processSynchronous(String custInfo){
        //调用用户服务处理,正常返回 {'RESULT_CODE':'0000','RESULT_MSG':'成功','RESULT_INFO':{}}
        String returnUser = iUserService.soUserServiceForOrderService(custInfo);

        JSONObject returnUserTmp = JSONObject.parseObject(returnUser);

        Assert.notNull(returnUserTmp,"用户服务没有相应，请检查服务是否正常，请求报文："+returnUser);
        //受理不成功
        if(!returnUserTmp.containsKey(ProtocolUtil.RESULT_CODE)
                || !ProtocolUtil.RETURN_MSG_SUCCESS.equals(returnUserTmp.getString(ProtocolUtil.RESULT_CODE))){
            throw new IllegalArgumentException("客户受理失败，失败原因：" + (returnUserTmp.containsKey(ProtocolUtil.RESULT_MSG)
                    ?"未知原因":returnUserTmp.getString(ProtocolUtil.RESULT_MSG)) + "请求报文："+returnUser);
        }
    }

    /**
     * 异步方式处理，主要是通过消息队列处理，缺点是对结果无法预知
     * @param custInfo
     */
    private void processAsynchronous(String custInfo){

    }

    @Override
    public int getOrder() {
        return order;
    }
}
