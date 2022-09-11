/**
 * Copyright 2017-2020 吴学文 and java110 team.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.job.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.businessDatabus.CustomBusinessDatabusDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.tempCarFeeConfig.TempCarPayOrderDto;
import com.java110.entity.order.Business;
import com.java110.po.machine.MachineRecordPo;
import com.java110.vo.ResultVo;

import java.util.List;

/**
 * databus 适配器
 * <p>
 * add by wuxw 2020-12-07
 */
public interface IDatabusAdapt {

    /**
     * 业务处理
     *
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    public void execute(Business business, List<Business> businesses) throws Exception;

    /**
     * 开门接口
     *
     * @param paramIn 业务信息
     */
    ResultVo openDoor(JSONObject paramIn);
    /**
     * 开门接口
     *
     * @param paramIn 业务信息
     */
    ResultVo closeDoor(JSONObject paramIn);


    ResultVo getQRcode(JSONObject reqJson);
    /**
     * 重启设备
     *
     * @param reqJson {
     *                "machineCode":""
     * }
     */
    ResultVo restartMachine(JSONObject reqJson);

    ResultVo reSendToIot(JSONObject reqJson);

    ResultVo getTempCarFeeOrder(TempCarPayOrderDto tempCarPayOrderDto);

    ResultVo notifyTempCarFeeOrder(TempCarPayOrderDto tempCarPayOrderDto);


    /**
     * 手工 送数据
     * @param customBusinessDatabusDto
     */
    void customExchange(CustomBusinessDatabusDto customBusinessDatabusDto);

    ResultVo customCarInOut(JSONObject reqJson);

    ResultVo payVideo(MachineDto machineDto);

    ResultVo heartbeatVideo(JSONObject reqJson);

    ResultVo updateCarInoutCarNum(CarInoutDto carInoutDto);

    ResultVo getManualOpenDoorLogs(JSONObject reqJson);
}
