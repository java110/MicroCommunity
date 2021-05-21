package com.java110.api.listener.meterWater;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.meterWater.IMeterWaterBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeMeterWaterConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveMeterWaterListener")
public class SaveMeterWaterListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IMeterWaterBMO meterWaterBMOImpl;

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

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "feeTypeCd", "请求报文中未包含费用类型");
        Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含费用项");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "preDegrees", "请求报文中未包含preDegrees");
        Assert.hasKeyAndValue(reqJson, "curDegrees", "请求报文中未包含curDegrees");
        Assert.hasKeyAndValue(reqJson, "preReadingTime", "请求报文中未包含preReadingTime");
        Assert.hasKeyAndValue(reqJson, "curReadingTime", "请求报文中未包含curReadingTime");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        if (FeeConfigDto.FEE_TYPE_CD_WATER.equals(reqJson.getString("feeTypeCd"))) {
            reqJson.put("meterType", "1010");
        } else if (FeeConfigDto.FEE_TYPE_CD_GAS.equals(reqJson.getString("feeTypeCd"))) {
            reqJson.put("meterType", "3030");
        } else {
            reqJson.put("meterType", "2020");
        }
        //获取抄表对象id(即房屋id)
        String objId = reqJson.getString("objId");
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(objId);
        List<RoomDto> roomList = roomInnerServiceSMOImpl.queryRooms(roomDto);
        Assert.listOnlyOne(roomList, "查询房屋信息错误！");
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
        if (waterRemarkList.contains(communityId) && meterType.equals("2020")) {
            reqJson.put("feeId", "-1");
            meterWaterBMOImpl.addMeterWater(reqJson, context);
        } else if (electricRemarkList.contains(communityId) && meterType.equals("1010")) {
            reqJson.put("feeId", "-1");
            meterWaterBMOImpl.addMeterWater(reqJson, context);
        } else {
            PayFeePo payFeePo = BeanConvertUtil.covertBean(reqJson, PayFeePo.class);
            payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            payFeePo.setIncomeObjId(reqJson.getString("storeId"));
            payFeePo.setAmount("-1");
            payFeePo.setStartTime(reqJson.getString("preReadingTime"));
            payFeePo.setEndTime(reqJson.getString("preReadingTime"));
            payFeePo.setPayerObjId(reqJson.getString("objId"));
            //payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            payFeePo.setPayerObjType(reqJson.getString("objType"));
            payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
            payFeePo.setState(FeeDto.STATE_DOING);
            payFeePo.setUserId(context.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
            super.insert(context, payFeePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(reqJson.getString("communityId"));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
            feeAttrPo.setValue(reqJson.getString("curReadingTime"));
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPo.setAttrId("-1");
            super.insert(context, feeAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
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
                feeAttrPo.setAttrId("-2");
                super.insert(context, feeAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(reqJson.getString("communityId"));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
                feeAttrPo.setValue(ownerDtos.get(0).getLink());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPo.setAttrId("-3");
                super.insert(context, feeAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(reqJson.getString("communityId"));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
                feeAttrPo.setValue(ownerDtos.get(0).getName());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPo.setAttrId("-4");
                super.insert(context, feeAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
            }
            reqJson.put("feeId", payFeePo.getFeeId());
            meterWaterBMOImpl.addMeterWater(reqJson, context);
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMeterWaterConstant.ADD_METERWATER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
