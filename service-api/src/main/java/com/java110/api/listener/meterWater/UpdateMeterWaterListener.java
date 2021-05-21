package com.java110.api.listener.meterWater;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.meterWater.IMeterWaterBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.meterWater.MeterWaterDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IMeterWaterInnerServiceSMO;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeMeterWaterConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 保存水电费侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateMeterWaterListener")
public class UpdateMeterWaterListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IMeterWaterBMO meterWaterBMOImpl;

    @Autowired
    private IMeterWaterInnerServiceSMO meterWaterInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键(水费黑名单)
    public static final String WATER_BLACK_LIST = "WATER_BLACK_LIST";

    //键(电费黑名单)
    public static final String ELECTRIC_BLACK_LIST = "ELECTRIC_BLACK_LIST";

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "waterId", "waterId不能为空");
        Assert.hasKeyAndValue(reqJson, "preDegrees", "请求报文中未包含preDegrees");
        Assert.hasKeyAndValue(reqJson, "curDegrees", "请求报文中未包含curDegrees");
        Assert.hasKeyAndValue(reqJson, "preReadingTime", "请求报文中未包含preReadingTime");
        Assert.hasKeyAndValue(reqJson, "curReadingTime", "请求报文中未包含curReadingTime");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
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
            meterWaterBMOImpl.updateMeterWater(reqJson, context);
        } else if (electricRemarkList.contains(communityId) && meterType.equals("1010")) {
            meterWaterBMOImpl.updateMeterWater(reqJson, context);
        } else {
            PayFeePo payFeePo = new PayFeePo();
            payFeePo.setFeeId(meterWaterDtos.get(0).getFeeId());
            payFeePo.setCommunityId(meterWaterDtos.get(0).getCommunityId());
            payFeePo.setStartTime(reqJson.getString("preReadingTime"));
            //payFeePo.setEndTime(reqJson.getString("curReadingTime"));
            super.update(context, payFeePo, BusinessTypeConstant.BUSINESS_TYPE_ONLY_UPDATE_FEE_INFO);
            meterWaterBMOImpl.updateMeterWater(reqJson, context);
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMeterWaterConstant.UPDATE_METERWATER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
