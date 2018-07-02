package com.java110.center.listener;

import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlow;
import com.java110.event.app.order.Ordered;
import com.java110.event.center.event.InvokeBusinessSystemEvent;
import com.java110.event.center.listener.DataFlowListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户ID处理 侦听
 * Created by wuxw on 2018/7/2.
 */
//@Component
@Java110Listener(name="dealUserIdListener")
public class DealUserIdListener implements DataFlowListener<InvokeBusinessSystemEvent>,Ordered {

    private final static Logger logger = LoggerFactory.getLogger(DealUserIdListener.class);

    @Override
    public void soService(InvokeBusinessSystemEvent event) {
       DataFlow dataFlow = event.getDataFlow();
       if (dataFlow == null || dataFlow.getBusinesses() == null || dataFlow.getBusinesses().size() == 0){
           throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"用户ID处理侦听执行异常，没有可处理的business");
       }

       //判断是否存在 "serviceCode": "save.user.info", 业务，如果存在则处理
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
