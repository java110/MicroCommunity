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
package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 类表述：保存
 * 服务编码：ownerCar.saveOwnerCar
 * 请求路劲：/app/ownerCar.SaveOwnerCar
 * add by 吴学文 at 2021-10-13 16:58:19 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "owner.editOwnerCar")
public class EditOwnerCarCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(EditOwnerCarCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "carNum", "请求报文中未包含carNum");
        Assert.jsonObjectHaveKey(reqJson, "carId", "请求报文中未包含carId");
        Assert.jsonObjectHaveKey(reqJson, "memberId", "请求报文中未包含memberId");
        Assert.jsonObjectHaveKey(reqJson, "carType", "请求报文中未包含carType");
        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setMemberId(reqJson.getString("memberId"));
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        ownerCarDto.setCarId(reqJson.getString("carId"));
        List<OwnerCarDto> ownerCarDtos = ownerCarV1InnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        Assert.listOnlyOne(ownerCarDtos, "未找到车辆信息");

        String psId = ownerCarDtos.get(0).getPsId();

        if (StringUtil.isEmpty(psId) || "-1".equals(psId)) {
            throw new IllegalArgumentException("车位已经被释放，不允许修改车辆信息");
        }

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        if (!reqJson.containsKey("leaseType")) {
            reqJson.put("leaseType", OwnerCarDto.LEASE_TYPE_MONTH);
        }

        if (!OwnerCarDto.LEASE_TYPE_MONTH.equals(reqJson.getString("leaseType"))) {
            reqJson.put("startTime", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_B));
            reqJson.put("endTime", "2037-01-01");
        }

        OwnerCarPo ownerCarPo = BeanConvertUtil.covertBean(reqJson, OwnerCarPo.class);
        int flag = ownerCarV1InnerServiceSMOImpl.updateOwnerCar(ownerCarPo);

        if (flag < 1) {
            throw new CmdException("修改数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
