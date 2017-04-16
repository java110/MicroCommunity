package com.java110.order.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.event.AppCustEvent;
import com.java110.core.event.AppListener;
import com.java110.core.event.Ordered;
import com.java110.feign.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 客户信息调度
 * Created by wuxw on 2017/4/14.
 */
@Component
public class CustDispatchListener implements AppListener<AppCustEvent> ,Ordered{

    @Autowired
    IUserService iUserService;

    private final static int order = Ordered.dafultValue+1;
    @Override
    public void onJava110Event(AppCustEvent event) {

        //这里写 客户信息处理逻辑

        String custInfo = event.getCustInfo();

        Assert.hasLength(custInfo,"没有需要处理的信息[custInfo="+custInfo+"]");

        //调用用户服务处理
        String returnUser = iUserService.soUserService(custInfo);

        JSONObject returnUserTmp = JSONObject.parseObject(returnUser);

        Assert.notNull(returnUserTmp,"用户服务没有相应，请检查服务是否正常，请求报文："+returnUser);
        //受理不成功
        if(!returnUserTmp.containsKey("RESULT_CODE")
                || !ProtocolUtil.RETURN_MSG_SUCCESS.equals(returnUserTmp.getString("RESULT_CODE"))){
            throw new IllegalArgumentException("客户受理失败，失败原因：" + (returnUserTmp.containsKey("RESULT_MSG")
                    ?"未知原因":returnUserTmp.getString("RESULT_MSG")) + "请求报文："+returnUser);
        }
        //受理成功，目前不做任何处理


    }

    @Override
    public int getOrder() {
        return order;
    }
}
