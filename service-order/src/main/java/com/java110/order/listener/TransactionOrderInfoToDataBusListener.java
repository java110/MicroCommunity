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

import com.java110.config.properties.code.Java110Properties;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.Environment;
import com.java110.core.context.IOrderDataFlowContext;
import com.java110.core.context.SecureInvocation;
import com.java110.core.event.app.order.Ordered;
import com.java110.core.event.center.event.InvokeFinishBusinessSystemEvent;
import com.java110.core.event.center.listener.DataFlowListener;
import com.java110.dto.businessDatabus.BusinessDatabusDto;
import com.java110.entity.order.Business;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.utils.cache.DatabusCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.DomainContant;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 用户ID处理 侦听
 * Created by wuxw on 2018/7/2.
 */

@Java110Listener(name = "transactionOrderInfoToDataBusListener")
public class TransactionOrderInfoToDataBusListener implements DataFlowListener<InvokeFinishBusinessSystemEvent>, Ordered {

    //databus 业务类型
    private static final String DATABUS_SWITCH = "DATABUS_SWITCH";
    private static final String DATABUS_SWITCH_ON = "ON"; // 开关打开

    private final static Logger logger = LoggerFactory.getLogger(TransactionOrderInfoToDataBusListener.class);

    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Autowired
    private Java110Properties java110Properties;

    @Override
    public void soService(InvokeFinishBusinessSystemEvent event) {

        IOrderDataFlowContext dataFlow = event.getDataFlow();
        if (!SecureInvocation.secure(this.getClass())
                || Environment.isOwnerPhone(java110Properties)
                || dataFlow == null
                || dataFlow.getBusinessList() == null
                || dataFlow.getBusinessList().size() == 0) {
            return;
        }

        String databusSwitch = MappingCache.getValue(DomainContant.COMMON_DOMAIN, DATABUS_SWITCH);

        if (!DATABUS_SWITCH_ON.equals(databusSwitch)) {
            return;
        }
        List<BusinessDatabusDto> databusDtos = DatabusCache.getDatabuss();

        if (!hasTypeCd(databusDtos, dataFlow.getBusinessList())) {
            return;
        }

        try {
            //同步databus
            dataBusInnerServiceSMOImpl.exchange(dataFlow.getBusinessList());
        } catch (Exception e) {
            logger.error("传输databus 失败", e);
        }
    }

    private boolean hasTypeCd(List<BusinessDatabusDto> databusDtos, List<Business> businesses) {

        for (BusinessDatabusDto databusDto : databusDtos) {
            for (Business business : businesses) {
                if (databusDto.getBusinessTypeCd().equals(business.getBusinessTypeCd())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
