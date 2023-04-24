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
package com.java110.common.cmd.machine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.machine.IMachineOpenDoorBMO;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.machine.CarBlackWhiteDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.parking.ParkingBoxAreaDto;
import com.java110.dto.parkingCoupon.ParkingCouponCarDto;
import com.java110.dto.tempCarFeeConfig.TempCarPayOrderDto;
import com.java110.intf.acct.IParkingCouponCarOrderV1InnerServiceSMO;
import com.java110.intf.acct.IParkingCouponCarV1InnerServiceSMO;
import com.java110.intf.common.ICarInoutV1InnerServiceSMO;
import com.java110.intf.community.IParkingBoxAreaV1InnerServiceSMO;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.intf.user.ICarBlackWhiteV1InnerServiceSMO;
import com.java110.po.parkingCouponCar.ParkingCouponCarPo;
import com.java110.po.parkingCouponCarOrder.ParkingCouponCarOrderPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述：保存
 * 服务编码：machine.customCarInOutCmd
 * 请求路劲：/app/machine.customCarInOutCmd
 * add by 吴学文 at 2021-09-18 13:35:13 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "machine.customCarInOutCmd")
public class CustomCarInOutCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(CustomCarInOutCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IMachineOpenDoorBMO machineOpenDoorBMOImpl;


    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Autowired
    private IParkingBoxAreaV1InnerServiceSMO parkingBoxAreaV1InnerServiceSMOImpl;

    @Autowired
    private ICarInoutV1InnerServiceSMO carInoutV1InnerServiceSMOImpl;

    @Autowired
    private ICarBlackWhiteV1InnerServiceSMO carBlackWhiteV1InnerServiceSMOImpl;

    @Autowired
    private IParkingCouponCarV1InnerServiceSMO parkingCouponCarV1InnerServiceSMOImpl;

    @Autowired
    private IParkingCouponCarOrderV1InnerServiceSMO parkingCouponCarOrderV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "machineId", "请求报文中未包含设备信息");
        Assert.hasKeyAndValue(reqJson, "carNum", "请求报文中未包含车牌号");
        Assert.hasKeyAndValue(reqJson, "type", "请求报文中未包含类型");

        CarInoutDto carInoutDto = new CarInoutDto();
        carInoutDto.setCarNum(reqJson.getString("carNum"));
        carInoutDto.setStates(new String[]{
                CarInoutDto.STATE_IN,
                CarInoutDto.STATE_REPAY
        });
        int count = carInoutV1InnerServiceSMOImpl.queryCarInoutsCount(carInoutDto);

        //出场
        if(!"1101".equals(reqJson.getString("type"))) {
            Assert.hasKeyAndValue(reqJson,"payType","未包含支付方式");
            Assert.hasKeyAndValue(reqJson,"amount","未包含支付金额");
            if(count < 1){
                throw new CmdException("车辆未入场");
            }
        }else{
            if(count > 0){
                throw new CmdException("车辆已经在场，请先出场");
            }
            //进场时 判断是否为黑名单
            CarBlackWhiteDto carBlackWhiteDto = new CarBlackWhiteDto();
            carBlackWhiteDto.setCarNum(reqJson.getString("carNum"));
            count =  carBlackWhiteV1InnerServiceSMOImpl.queryCarBlackWhitesCount(carBlackWhiteDto);
            if(count > 0){
                throw new CmdException("黑名单车辆禁止入场");
            }
        }



    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        //出场时 先 补充费用信息
        if(!"1101".equals(reqJson.getString("type"))) {

            ParkingBoxAreaDto parkingBoxAreaDto = new ParkingBoxAreaDto();
            parkingBoxAreaDto.setBoxId(reqJson.getString("boxId"));
            parkingBoxAreaDto.setDefaultArea(ParkingBoxAreaDto.DEFAULT_AREA_TRUE);
            List<ParkingBoxAreaDto> parkingBoxAreaDtos = parkingBoxAreaV1InnerServiceSMOImpl.queryParkingBoxAreas(parkingBoxAreaDto);

            if(parkingBoxAreaDtos == null  || parkingBoxAreaDtos.size()< 1){
                throw new CmdException("未包含停车场信息");
            }
            TempCarPayOrderDto tempCarPayOrderDto = new TempCarPayOrderDto();
            tempCarPayOrderDto.setCarNum(reqJson.getString("carNum"));
            tempCarPayOrderDto.setPaId(parkingBoxAreaDtos.get(0).getPaId());
            ResultVo resultVo = dataBusInnerServiceSMOImpl.getTempCarFeeOrder(tempCarPayOrderDto);
            if(resultVo.getCode() != ResultVo.CODE_OK){
                throw new CmdException(resultVo.getMsg());
            }
            String data = JSONObject.toJSONString(resultVo.getData());
            JSONObject orderInfo = JSONObject.parseObject(data);
            //JSONObject orderInfo = JSONObject.parseObject(resultVo.getData().toString());

             tempCarPayOrderDto = new TempCarPayOrderDto();
            tempCarPayOrderDto.setCarNum(reqJson.getString("carNum"));
            tempCarPayOrderDto.setPaId(parkingBoxAreaDtos.get(0).getPaId());
            tempCarPayOrderDto.setOrderId(orderInfo.getString("orderId"));
            tempCarPayOrderDto.setAmount(Double.parseDouble(reqJson.getString("amount")));
            tempCarPayOrderDto.setPayCharge(Double.parseDouble(reqJson.getString("payCharge")));
            tempCarPayOrderDto.setPayType(reqJson.getString("payType"));
            //处理优惠券
            dealParkingCouponCar(reqJson,tempCarPayOrderDto);
            //tempCarPayOrderDto.setMachineId(reqJson.getString("machineId"));
             resultVo = dataBusInnerServiceSMOImpl.notifyTempCarFeeOrder(tempCarPayOrderDto);
            if(resultVo.getCode() != ResultVo.CODE_OK){
                throw new CmdException(resultVo.getMsg());
            }
        }
        ResponseEntity<String> responseEntity = machineOpenDoorBMOImpl.customCarInOut(reqJson);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void dealParkingCouponCar(JSONObject reqJson,TempCarPayOrderDto tempCarPayOrderDto) {
        //处理停车劵

        if(!reqJson.containsKey("pccIds")) {
            return ;
        }

        JSONArray pccIds = reqJson.getJSONArray("pccIds");
        ParkingCouponCarPo parkingCouponCarPo = null;
        ParkingCouponCarOrderPo parkingCouponCarOrderPo = null;
        String pccId = "";
        List<String> tmpPccIds = new ArrayList<>();
        for(int pccIndex = 0;pccIndex <  pccIds.size();pccIndex++){
            pccId = pccIds.getString(pccIndex);
            tmpPccIds.add(pccId);
            parkingCouponCarPo = new ParkingCouponCarPo();
            parkingCouponCarPo.setPccId(pccId);
            parkingCouponCarPo.setState(ParkingCouponCarDto.STATE_FINISH);
            parkingCouponCarV1InnerServiceSMOImpl.updateParkingCouponCar(parkingCouponCarPo);

            parkingCouponCarOrderPo = new ParkingCouponCarOrderPo();
            parkingCouponCarOrderPo.setOrderId(GenerateCodeFactory.getGeneratorId("11"));
            parkingCouponCarOrderPo.setCarNum(reqJson.getString("carNum"));
            parkingCouponCarOrderPo.setCarOutId("-1");
            parkingCouponCarOrderPo.setCommunityId(reqJson.getString("communityId"));
            parkingCouponCarOrderPo.setMachineId(StringUtil.isEmpty(reqJson.getString("machineId"))?"-1":reqJson.getString("machineId"));
            parkingCouponCarOrderPo.setMachineName("未知");
            parkingCouponCarOrderPo.setPaId(reqJson.getString("paId"));
            parkingCouponCarOrderPo.setPccId(pccId);
            parkingCouponCarOrderPo.setRemark("pc端支付停车劵抵扣");

            parkingCouponCarOrderV1InnerServiceSMOImpl.saveParkingCouponCarOrder(parkingCouponCarOrderPo);
        }

        tempCarPayOrderDto.setPccIds(tmpPccIds.toArray(new String[tmpPccIds.size()]));
    }
}
