/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.order.listener;

import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.IOrderDataFlowContext;
import com.java110.core.event.app.order.Ordered;
import com.java110.core.event.center.event.InvokeFinishBusinessSystemEvent;
import com.java110.core.event.center.listener.DataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户ID处理 侦听
 * Created by wuxw on 2018/7/2.
 */

@Java110Listener(name = "transactionOrderInfoToKafkaListener")
public class TransactionOrderInfoToKafkaListener implements DataFlowListener<InvokeFinishBusinessSystemEvent>, Ordered {

    private final static Logger logger = LoggerFactory.getLogger(TransactionOrderInfoToKafkaListener.class);

    @Override
    public void soService(InvokeFinishBusinessSystemEvent event) {
        IOrderDataFlowContext dataFlow = event.getDataFlow();
        if (dataFlow == null || dataFlow.getBusinessList() == null || dataFlow.getBusinessList().size() == 0) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "用户ID处理侦听执行异常，没有可处理的business");
        }

        //判断是否存在 "serviceCode": "save.user.info", 业务，如果存在则处理
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
