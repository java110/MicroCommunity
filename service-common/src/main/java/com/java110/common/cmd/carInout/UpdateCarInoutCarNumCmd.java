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
package com.java110.common.cmd.carInout;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.machine.CarInoutDto;
import com.java110.intf.common.ICarInoutDetailV1InnerServiceSMO;
import com.java110.intf.common.ICarInoutV1InnerServiceSMO;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.po.car.CarInoutDetailPo;
import com.java110.po.car.CarInoutPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 类表述：更新
 * 服务编码：carInout.updateCarInout
 * 请求路劲：/app/carInout.UpdateCarInout
 * add by 吴学文 at 2021-10-13 14:45:52 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "carInout.updateCarInoutNum")
public class UpdateCarInoutCarNumCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateCarInoutCarNumCmd.class);


    @Autowired
    private ICarInoutV1InnerServiceSMO carInoutV1InnerServiceSMOImpl;

    @Autowired
    private ICarInoutDetailV1InnerServiceSMO carInoutDetailV1InnerServiceSMOImpl;

    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "inoutId", "inoutId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
        Assert.hasKeyAndValue(reqJson, "carNum", "车牌号不能为空");

        CarInoutDto carInoutDto = new CarInoutDto();
        carInoutDto.setCarNum(reqJson.getString("carNum"));
        carInoutDto.setCommunityId(reqJson.getString("communityId"));
        carInoutDto.setStates(new String[]{CarInoutDto.STATE_IN,CarInoutDto.STATE_REPAY});
        List<CarInoutDto> carInoutDtos = carInoutV1InnerServiceSMOImpl.queryCarInouts(carInoutDto);

       if(carInoutDtos != null && carInoutDtos.size() > 0){
           throw new CmdException("车牌已进场");
       }

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        CarInoutDto carInoutDto = new CarInoutDto();
        carInoutDto.setInoutId(reqJson.getString("inoutId"));
        carInoutDto.setStates(new String[]{CarInoutDto.STATE_IN,CarInoutDto.STATE_REPAY});
        List<CarInoutDto> carInoutDtos = carInoutV1InnerServiceSMOImpl.queryCarInouts(carInoutDto);

        Assert.listOnlyOne(carInoutDtos,"进出明细不存在");

        carInoutDto = carInoutDtos.get(0);
        carInoutDto.setOldCarNum(carInoutDto.getCarNum());
        carInoutDto.setCarNum(reqJson.getString("carNum"));

        //调用物理网 修改 车牌号
        dataBusInnerServiceSMOImpl.updateCarInoutCarNum(carInoutDto);

        CarInoutPo carInoutPo = new CarInoutPo();
        carInoutPo.setInoutId(reqJson.getString("inoutId"));
        carInoutPo.setCarNum(reqJson.getString("carNum"));
        int flag = carInoutV1InnerServiceSMOImpl.updateCarInout(carInoutPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        CarInoutDetailPo carInoutDetailPo = new CarInoutDetailPo();
        carInoutDetailPo.setInoutId(reqJson.getString("inoutId"));
        carInoutDetailPo.setCarNum(reqJson.getString("carNum"));

        flag =  carInoutDetailV1InnerServiceSMOImpl.updateCarInoutDetail(carInoutDetailPo);
        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }



        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
