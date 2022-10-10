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
package com.java110.acct.cmd.parkingCoupon;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.parkingCoupon.ParkingCouponDto;
import com.java110.intf.acct.IParkingCouponV1InnerServiceSMO;
import com.java110.intf.community.IParkingAreaV1InnerServiceSMO;
import com.java110.po.parkingCoupon.ParkingCouponPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 类表述：更新
 * 服务编码：parkingCoupon.updateParkingCoupon
 * 请求路劲：/app/parkingCoupon.UpdateParkingCoupon
 * add by 吴学文 at 2022-10-10 13:27:02 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "parkingCoupon.updateParkingCoupon")
public class UpdateParkingCouponCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateParkingCouponCmd.class);


    @Autowired
    private IParkingCouponV1InnerServiceSMO parkingCouponV1InnerServiceSMOImpl;

    @Autowired
    private IParkingAreaV1InnerServiceSMO parkingAreaV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "couponId", "couponId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
        if(ParkingCouponDto.TYPE_CD_FREE.equals(reqJson.getString("typeCd"))){
            reqJson.put("value","9999");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ParkingCouponPo parkingCouponPo = BeanConvertUtil.covertBean(reqJson, ParkingCouponPo.class);

        if(StringUtil.isEmpty(parkingCouponPo.getPaId())) {
            ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
            parkingAreaDto.setPaId(parkingCouponPo.getPaId());
            List<ParkingAreaDto> parkingAreaDtos = parkingAreaV1InnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
            Assert.listOnlyOne(parkingAreaDtos, "停车场不存在");
            parkingCouponPo.setPaName(parkingAreaDtos.get(0).getNum());
        }

        int flag = parkingCouponV1InnerServiceSMOImpl.updateParkingCoupon(parkingCouponPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
