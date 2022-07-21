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
package com.java110.fee.cmd.meterWater;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.meterWater.MeterWaterDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IMeterWaterInnerServiceSMO;
import com.java110.intf.fee.IMeterWaterV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.po.fee.PayFeePo;
import com.java110.po.meterWater.MeterWaterPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 类表述：更新
 * 服务编码：meterWater.updateMeterWater
 * 请求路劲：/app/meterWater.UpdateMeterWater
 * add by 吴学文 at 2022-07-21 09:17:10 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "meterWater.updateMeterWater")
public class UpdateMeterWaterCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(UpdateMeterWaterCmd.class);


    @Autowired
    private IMeterWaterV1InnerServiceSMO meterWaterV1InnerServiceSMOImpl;


    @Autowired
    private IMeterWaterInnerServiceSMO meterWaterInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键(水费黑名单)
    public static final String WATER_BLACK_LIST = "WATER_BLACK_LIST";

    //键(电费黑名单)
    public static final String ELECTRIC_BLACK_LIST = "ELECTRIC_BLACK_LIST";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "waterId", "waterId不能为空");
        Assert.hasKeyAndValue(reqJson, "preDegrees", "请求报文中未包含preDegrees");
        Assert.hasKeyAndValue(reqJson, "curDegrees", "请求报文中未包含curDegrees");
        Assert.hasKeyAndValue(reqJson, "preReadingTime", "请求报文中未包含preReadingTime");
        Assert.hasKeyAndValue(reqJson, "curReadingTime", "请求报文中未包含curReadingTime");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        MeterWaterDto meterWaterDto = new MeterWaterDto();
        meterWaterDto.setWaterId(reqJson.getString("waterId"));
        meterWaterDto.setCommunityId(reqJson.getString("communityId"));
        List<MeterWaterDto> meterWaterDtos = meterWaterInnerServiceSMOImpl.queryMeterWaters(meterWaterDto);
        Assert.listOnlyOne(meterWaterDtos, "数据异常未找到费用信息");
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(meterWaterDtos.get(0).getObjId());
        List<RoomDto> roomList = roomInnerServiceSMOImpl.queryRooms(roomDto);
        Assert.listOnlyOne(roomList, "查询房屋信息错误！");
        //获取抄表对象所属小区id
        String communityId = roomList.get(0).getCommunityId();
        //获取抄表类型
        String meterType = meterWaterDtos.get(0).getMeterType();
        //取出开关映射的备注值(水费黑名单)
        String waterRemark = MappingCache.getRemark(DOMAIN_COMMON, WATER_BLACK_LIST);
        List<String> waterRemarkList = new ArrayList<>();
        if (!StringUtil.isEmpty(waterRemark)) {
            String[] waterSplit = waterRemark.split(",");
            //将数组转成list集合(水费黑名单集合)
            waterRemarkList = Arrays.asList(waterSplit);
        }
        //取出开关映射的备注值(电费黑名单)
        String electricRemark = MappingCache.getRemark(DOMAIN_COMMON, ELECTRIC_BLACK_LIST);
        List<String> electricRemarkList = new ArrayList<>();
        if (!StringUtil.isEmpty(electricRemark)) {
            String[] electricSplit = electricRemark.split(",");
            //将数组转成list集合(电费黑名单集合)
            electricRemarkList = Arrays.asList(electricSplit);
        }
        //如果是水费，且在水费黑名单就直接生成水费记录，不生成费用
        if (waterRemarkList.contains(communityId) && meterType.equals("2020")) {
            updateMeterWater(reqJson);
        } else if (electricRemarkList.contains(communityId) && meterType.equals("1010")) {
            updateMeterWater(reqJson);
        } else {
            PayFeePo payFeePo = new PayFeePo();
            payFeePo.setFeeId(meterWaterDtos.get(0).getFeeId());
            payFeePo.setCommunityId(meterWaterDtos.get(0).getCommunityId());
            payFeePo.setStartTime(reqJson.getString("preReadingTime"));
            //payFeePo.setEndTime(reqJson.getString("curReadingTime"));
            int flag = payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
            if (flag < 1) {
                throw new CmdException("更新数据失败");
            }
            updateMeterWater(reqJson);
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void updateMeterWater(JSONObject paramInJson) {
        MeterWaterPo meterWaterPo = BeanConvertUtil.covertBean(paramInJson, MeterWaterPo.class);
        int flag = meterWaterV1InnerServiceSMOImpl.updateMeterWater(meterWaterPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
    }
}
