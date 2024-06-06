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
package com.java110.community.cmd.parkingSpaceApply;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityMemberDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.parking.ParkingSpaceApplyDto;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.intf.community.IParkingSpaceApplyV1InnerServiceSMO;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.po.parking.ParkingSpaceApplyPo;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类表述：更新
 * 服务编码：parkingSpaceApply.updateParkingSpaceApply
 * 请求路劲：/app/parkingSpaceApply.UpdateParkingSpaceApply
 * add by 吴学文 at 2021-10-18 13:00:02 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "parkingSpaceApply.auditParkingSpaceApply")
public class AuditParkingSpaceApplyCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(AuditParkingSpaceApplyCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IParkingSpaceApplyV1InnerServiceSMO parkingSpaceApplyV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeNewV1InnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "applyId", "applyId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String userId = cmdDataFlowContext.getReqHeaders().get(CommonConstant.USER_ID);
        ParkingSpaceApplyPo parkingSpaceApplyPo = BeanConvertUtil.covertBean(reqJson, ParkingSpaceApplyPo.class);
        //审核失败
        if (ParkingSpaceApplyDto.STATE_FAIL.equals(parkingSpaceApplyPo.getState())) {
            parkingSpaceApplyPo.setPsId("");
            parkingSpaceApplyPo.setConfigId("");
            int flag = parkingSpaceApplyV1InnerServiceSMOImpl.updateParkingSpaceApply(parkingSpaceApplyPo);
            if (flag < 1) {
                throw new CmdException("更新数据失败");
            }
            return;
        }
        parkingSpaceApplyPo.setFeeId("-1");
        parkingSpaceApplyPo.setState(ParkingSpaceApplyDto.STATE_SUCCESS);
        int flag = parkingSpaceApplyV1InnerServiceSMOImpl.updateParkingSpaceApply(parkingSpaceApplyPo);
        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
        ParkingSpaceApplyDto parkingSpaceApplyDto = new ParkingSpaceApplyDto();
        parkingSpaceApplyDto.setApplyId(parkingSpaceApplyPo.getApplyId());
        List<ParkingSpaceApplyDto> parkingSpaceApplyDtos = parkingSpaceApplyV1InnerServiceSMOImpl.queryParkingSpaceApplys(parkingSpaceApplyDto);
        if (ListUtil.isNull(parkingSpaceApplyDtos)) {
            throw new CmdException("未查询到申请单，请联系管理员");
        }
        ParkingSpaceApplyDto parkingSpaceApply = parkingSpaceApplyDtos.get(0);
        //2、审核的时，判断车辆是否在owner_car中有，有就跳过。  没有的话写入owner_car，--都要写入pay_fee。
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarNum(parkingSpaceApplyPo.getCarNum());
        ownerCarDto.setCommunityId(parkingSpaceApplyPo.getCommunityId());
        ownerCarDto.setLeaseType(OwnerCarDto.LEASE_TYPE_MONTH);
        List<OwnerCarDto> ownerCarDtos = ownerCarV1InnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        String catId = "";
        OwnerCarPo ownerCarPo = new OwnerCarPo();
        if (ListUtil.isNull(ownerCarDtos)) {
            ownerCarPo.setCarId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            ownerCarPo.setOwnerId(parkingSpaceApply.getApplyPersonId());
            ownerCarPo.setbId("-1");
            ownerCarPo.setCarNum(parkingSpaceApply.getCarNum());
            ownerCarPo.setCarColor(parkingSpaceApply.getCarColor());
            ownerCarPo.setCarBrand(parkingSpaceApply.getCarBrand());
            ownerCarPo.setCarType(parkingSpaceApply.getCarType());
            ownerCarPo.setCarTypeCd("1001");
            ownerCarPo.setCommunityId(parkingSpaceApply.getCommunityId());
            ownerCarPo.setMemberId(ownerCarPo.getCarId());
            ownerCarPo.setStartTime(parkingSpaceApply.getStartTime());
            ownerCarPo.setEndTime(parkingSpaceApply.getEndTime());
            ownerCarPo.setPsId(parkingSpaceApply.getPsId());
            ownerCarPo.setState("1001");
            ownerCarPo.setUserId(userId);
            ownerCarPo.setRemark("车位申请，系统自动写入");
            ownerCarPo.setLeaseType(OwnerCarDto.LEASE_TYPE_MONTH);
            flag = ownerCarV1InnerServiceSMOImpl.saveOwnerCar(ownerCarPo);
            if (flag < 1) {
                throw new CmdException("更新数据失败");
            }
            catId = ownerCarPo.getCarId();
        } else {
            catId = ownerCarDtos.get(0).getCarId();
        }
        // 将车位状态 修改为已出租状态
        ParkingSpacePo parkingSpacePo = new ParkingSpacePo();
        parkingSpacePo.setPsId(parkingSpaceApply.getPsId());
        parkingSpacePo.setState(ParkingSpaceDto.STATE_HIRE);
        flag = parkingSpaceV1InnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
        if (flag < 1) {
            throw new CmdException("更新车位状态失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
