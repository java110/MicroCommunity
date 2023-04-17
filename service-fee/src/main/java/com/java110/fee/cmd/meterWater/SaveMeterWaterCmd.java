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
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFeeBatch.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IMeterWaterV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.meterWater.MeterWaterPo;
import com.java110.po.payFeeBatch.PayFeeBatchPo;
import com.java110.utils.cache.MappingCache;
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
 * 类表述：保存
 * 服务编码：meterWater.saveMeterWater
 * 请求路劲：/app/meterWater.SaveMeterWater
 * add by 吴学文 at 2022-07-21 09:17:10 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "meterWater.saveMeterWater")
public class SaveMeterWaterCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveMeterWaterCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键(水费黑名单)
    public static final String WATER_BLACK_LIST = "WATER_BLACK_LIST";

    //键(电费黑名单)
    public static final String ELECTRIC_BLACK_LIST = "ELECTRIC_BLACK_LIST";

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IMeterWaterV1InnerServiceSMO meterWaterV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "feeTypeCd", "请求报文中未包含费用类型");
        Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含费用项");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "preDegrees", "请求报文中未包含preDegrees");
        Assert.hasKeyAndValue(reqJson, "curDegrees", "请求报文中未包含curDegrees");
        Assert.hasKeyAndValue(reqJson, "preReadingTime", "请求报文中未包含preReadingTime");
        Assert.hasKeyAndValue(reqJson, "curReadingTime", "请求报文中未包含curReadingTime");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "meterType", "请求报文中未包含抄表类型");

        if(reqJson.getDoubleValue("curDegrees") < reqJson.getDoubleValue("preDegrees")){
            throw new CmdException("当前读数小于上期读数");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String objId = reqJson.getString("objId");
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(objId);
        List<RoomDto> roomList = roomInnerServiceSMOImpl.queryRooms(roomDto);
        Assert.listOnlyOne(roomList, "查询房屋信息错误！");
        //生成批次
        generatorBatch(reqJson);
        //获取抄表对象所属小区id
        String communityId = roomList.get(0).getCommunityId();
        //获取表类型
        String meterType = reqJson.getString("meterType");
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
        if (waterRemarkList.contains(communityId)
                && FeeConfigDto.FEE_TYPE_CD_METER.equals(reqJson.getString("feeTypeCd"))) {
            reqJson.put("feeId", "-1");
            addMeterWater(reqJson);
        } else if (electricRemarkList.contains(communityId)
                && FeeConfigDto.FEE_TYPE_CD_WATER.equals(reqJson.getString("feeTypeCd"))) {
            reqJson.put("feeId", "-1");
            addMeterWater(reqJson);
        } else {
            PayFeePo payFeePo = BeanConvertUtil.covertBean(reqJson, PayFeePo.class);
            payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            payFeePo.setIncomeObjId(reqJson.getString("storeId"));
            payFeePo.setAmount("-1");
            payFeePo.setStartTime(reqJson.getString("preReadingTime"));
            payFeePo.setEndTime(reqJson.getString("preReadingTime"));
            payFeePo.setPayerObjId(reqJson.getString("objId"));
            //payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            payFeePo.setbId("-1");
            payFeePo.setPayerObjType(reqJson.getString("objType"));
            payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
            payFeePo.setState(FeeDto.STATE_DOING);
            payFeePo.setBatchId(reqJson.getString("batchId"));
            payFeePo.setUserId("-1");
            int flag = payFeeV1InnerServiceSMOImpl.savePayFee(payFeePo);
            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(reqJson.getString("communityId"));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
            feeAttrPo.setValue(reqJson.getString("curReadingTime"));
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            flag = feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setCommunityId(reqJson.getString("communityId"));
            ownerDto.setRoomId(reqJson.getString("objId"));
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);

            if (ownerDtos != null && ownerDtos.size() > 0) {
                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(reqJson.getString("communityId"));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_ID);
                feeAttrPo.setValue(ownerDtos.get(0).getOwnerId());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                flag = feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
                if (flag < 1) {
                    throw new CmdException("保存数据失败");
                }

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(reqJson.getString("communityId"));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
                feeAttrPo.setValue(ownerDtos.get(0).getLink());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                flag = feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
                if (flag < 1) {
                    throw new CmdException("保存数据失败");
                }

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(reqJson.getString("communityId"));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
                feeAttrPo.setValue(ownerDtos.get(0).getName());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                flag = feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
                if (flag < 1) {
                    throw new CmdException("保存数据失败");
                }
            }
            reqJson.put("feeId", payFeePo.getFeeId());
            addMeterWater(reqJson);
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addMeterWater(JSONObject paramInJson) {
        MeterWaterPo meterWaterPo = BeanConvertUtil.covertBean(paramInJson, MeterWaterPo.class);
        if (StringUtil.isEmpty(meterWaterPo.getbId())) {
            meterWaterPo.setbId("-1");
        }
        meterWaterPo.setWaterId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));

        int flag = meterWaterV1InnerServiceSMOImpl.saveMeterWater(meterWaterPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
    }


    /**
     * 生成批次号
     *
     * @param reqJson
     */
    private void generatorBatch(JSONObject reqJson) {
        PayFeeBatchPo payFeeBatchPo = new PayFeeBatchPo();
        payFeeBatchPo.setBatchId(GenerateCodeFactory.getGeneratorId("12"));
        payFeeBatchPo.setCommunityId(reqJson.getString("communityId"));
        payFeeBatchPo.setCreateUserId(reqJson.getString("userId"));
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");
        payFeeBatchPo.setCreateUserName(userDtos.get(0).getUserName());
        payFeeBatchPo.setState(PayFeeBatchDto.STATE_NORMAL);
        payFeeBatchPo.setMsg("正常");
        int flag = payFeeBatchV1InnerServiceSMOImpl.savePayFeeBatch(payFeeBatchPo);

        if (flag < 1) {
            throw new IllegalArgumentException("生成批次失败");
        }

        reqJson.put("batchId", payFeeBatchPo.getBatchId());
    }

}
