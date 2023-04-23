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
package com.java110.common.cmd.carInoutPayment;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.carInoutPayment.CarInoutPaymentDto;
import com.java110.dto.parking.ParkingBoxAreaDto;
import com.java110.intf.common.ICarInoutPaymentV1InnerServiceSMO;
import com.java110.intf.community.IParkingBoxAreaV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 类表述：查询
 * 服务编码：carInoutPayment.listCarInoutPayment
 * 请求路劲：/app/carInoutPayment.ListCarInoutPayment
 * add by 吴学文 at 2021-10-14 15:03:12 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "carInoutPayment.listCarInoutPayment")
public class ListCarInoutPaymentCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListCarInoutPaymentCmd.class);
    @Autowired
    private ICarInoutPaymentV1InnerServiceSMO carInoutPaymentV1InnerServiceSMOImpl;
    @Autowired
    private IParkingBoxAreaV1InnerServiceSMO parkingBoxAreaV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        CarInoutPaymentDto carInoutPaymentDto = BeanConvertUtil.covertBean(reqJson, CarInoutPaymentDto.class);
        if(reqJson.containsKey("boxId")) {
            carInoutPaymentDto.setPaIds(getPaIds(reqJson));
        }else{
            carInoutPaymentDto.setPaId(reqJson.getString("paId"));
        }

        if(!StringUtil.isEmpty(carInoutPaymentDto.getEndTime())){
            Date endTime = DateUtil.getDateFromStringB(carInoutPaymentDto.getEndTime());
            carInoutPaymentDto.setEndTime(DateUtil.getAddDayStringB(endTime,1));
        }

        int count = carInoutPaymentV1InnerServiceSMOImpl.queryCarInoutPaymentsCount(carInoutPaymentDto);

        List<CarInoutPaymentDto> carInoutPaymentDtos = null;

        if (count > 0) {
            carInoutPaymentDtos = carInoutPaymentV1InnerServiceSMOImpl.queryCarInoutPayments(carInoutPaymentDto);
        } else {
            carInoutPaymentDtos = new ArrayList<>();
        }

        if(carInoutPaymentDtos != null && carInoutPaymentDtos.size()>0){
            List<CarInoutPaymentDto> tempCarInoutPaymentDtos = carInoutPaymentV1InnerServiceSMOImpl.queryCarInoutPaymentMarjor(carInoutPaymentDto);
            for(CarInoutPaymentDto carInoutPaymentDto1: carInoutPaymentDtos){
                carInoutPaymentDto1.setPayChargeTotal(tempCarInoutPaymentDtos.get(0).getPayChargeTotal());
                carInoutPaymentDto1.setRealChargeTotal(tempCarInoutPaymentDtos.get(0).getRealChargeTotal());
            }
        }else{
            for(CarInoutPaymentDto carInoutPaymentDto1: carInoutPaymentDtos){
                carInoutPaymentDto1.setPayChargeTotal("0");
                carInoutPaymentDto1.setRealChargeTotal("0");
            }
        }


        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, carInoutPaymentDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }


    private String[] getPaIds(JSONObject reqJson) {
        if (reqJson.containsKey("boxId") && !StringUtil.isEmpty(reqJson.getString("boxId"))) {
            ParkingBoxAreaDto parkingBoxAreaDto = new ParkingBoxAreaDto();
            parkingBoxAreaDto.setBoxId(reqJson.getString("boxId"));
            parkingBoxAreaDto.setCommunityId(reqJson.getString("communityId"));
            List<ParkingBoxAreaDto> parkingBoxAreaDtos = parkingBoxAreaV1InnerServiceSMOImpl.queryParkingBoxAreas(parkingBoxAreaDto);

            if (parkingBoxAreaDtos == null || parkingBoxAreaDtos.size() < 1) {
                throw new CmdException("未查到停车场信息");
            }
            List<String> paIds = new ArrayList<>();
            for (ParkingBoxAreaDto parkingBoxAreaDto1 : parkingBoxAreaDtos) {
                paIds.add(parkingBoxAreaDto1.getPaId());
            }
            String[] paIdss = paIds.toArray(new String[paIds.size()]);
            return paIdss;
        }
        return null;
    }
}
