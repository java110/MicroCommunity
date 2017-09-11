package com.java110.order.type;



/**
 * Created by wuxw on 2017/4/14.
 */
public class CustDispatchListener implements AppListener<AppCustEvent> ,Ordered{

    private final static int order = Ordered.dafultValue+1;
    @Override
    public void onJava110Event(AppCustEvent event) {

        //这里写 客户信息处理逻辑
    }

    @Override
    public int getOrder() {
        return order;
    }
}
