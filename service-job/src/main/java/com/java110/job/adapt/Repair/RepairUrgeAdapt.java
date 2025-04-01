package com.java110.job.adapt.Repair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.notice.NoticeStaffDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairSettingDto;
import com.java110.dto.repairEvent.RepairEventDto;
import com.java110.dto.system.Business;
import com.java110.intf.community.IRepairEventV1InnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.msgNotify.IMsgNotify;
import com.java110.job.msgNotify.MsgNotifyFactory;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 报修催单
 */
@Component(value = "repairUrgeAdapt")
public class RepairUrgeAdapt extends DatabusAdaptImpl {

    @Autowired
    private IRepairEventV1InnerServiceSMO repairEventV1InnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Override
    public void execute(Business business, List<Business> businesses) throws Exception {
        JSONObject data = business.getData();
        System.out.println("repairUrgeAdapt 收到日志：>>>>>>>>>>>>>" + data.toJSONString());
        Assert.hasKeyAndValue(data, "eventId", "未包含时间ID");
        RepairEventDto repairEventDto = new RepairEventDto();
        repairEventDto.setEventId(data.getString("eventId"));

        List<RepairEventDto> repairEventDtos = repairEventV1InnerServiceSMOImpl.queryRepairEvents(repairEventDto);
        if (ListUtil.isNull(repairEventDtos)) {
            return;
        }

        //获取报修id
        String repairId = repairEventDtos.get(0).getRepairId();
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(repairId);
        repairDto.setStatusCd("0");
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);

        Assert.listOnlyOne(repairDtos, "不存在这条报修信息");

        IMsgNotify msgNotify = null;
        if (RepairSettingDto.NOTIFY_WAY_SMS.equals(repairDtos.get(0).getNotifyWay())) {
            msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_ALI);
        } else if (RepairSettingDto.NOTIFY_WAY_WECHAT.equals(repairDtos.get(0).getNotifyWay())) {
            msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_WECHAT);
        } else {
            return;
        }

        msgNotify.sendStaffMsg(
                new NoticeStaffDto(repairDtos.get(0).getCommunityId(),
                        repairEventDtos.get(0).getStaffId(),
                        repairDtos.get(0).getRepairSettingTypeName() + "工单催单",
                        repairDtos.get(0).getRepairName())
        );
    }
}
