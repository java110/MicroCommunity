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
package com.java110.job.adapt.hcIot;

import com.java110.entity.order.Business;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.IDatabusAdapt;
import com.java110.job.adapt.hcIot.asyn.ITransactionMachineAsyn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * HC iot 设备同步适配器
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "machineTransactionIotAdapt")
public class MachineTransactionIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private ITransactionMachineAsyn transactionMachineAsynImpl;

    @Override
    public void execute(Business business, List<Business> businesses) {

    }
}
