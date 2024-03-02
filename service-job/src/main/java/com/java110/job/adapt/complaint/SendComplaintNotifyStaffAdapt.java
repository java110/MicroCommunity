package com.java110.job.adapt.complaint;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.dto.complaintType.ComplaintTypeDto;
import com.java110.dto.complaintTypeUser.ComplaintTypeUserDto;
import com.java110.dto.system.Business;
import com.java110.intf.store.IComplaintTypeUserV1InnerServiceSMO;
import com.java110.intf.store.IComplaintTypeV1InnerServiceSMO;
import com.java110.intf.store.IComplaintV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.Repair.MachineReturnRepairAdapt;
import com.java110.job.msgNotify.IMsgNotify;
import com.java110.job.msgNotify.MsgNotifyFactory;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 投诉单推送消息给员工
 */

@Component(value = "sendComplaintNotifyStaffAdapt")
public class SendComplaintNotifyStaffAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(SendComplaintNotifyStaffAdapt.class);


    @Autowired
    private IComplaintV1InnerServiceSMO complaintV1InnerServiceSMOImpl;

    @Autowired
    private IComplaintTypeV1InnerServiceSMO complaintTypeV1InnerServiceSMOImpl;

    @Autowired
    private IComplaintTypeUserV1InnerServiceSMO complaintTypeUserV1InnerServiceSMOImpl;


    @Override
    public void execute(Business business, List<Business> businesses) throws Exception {
        JSONObject data = business.getData();

        String complaintId = data.getString("complaintId");

        if (StringUtil.isEmpty(complaintId)) {
            return;
        }


        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setComplaintId(complaintId);
        List<ComplaintDto> complaintDtos = complaintV1InnerServiceSMOImpl.queryComplaints(complaintDto);

        if (ListUtil.isNull(complaintDtos)) {
            return;
        }

        ComplaintTypeDto complaintTypeDto = new ComplaintTypeDto();
        complaintTypeDto.setTypeCd(complaintDtos.get(0).getTypeCd());
        List<ComplaintTypeDto> complaintTypeDtos = complaintTypeV1InnerServiceSMOImpl.queryComplaintTypes(complaintTypeDto);

        if(ListUtil.isNull(complaintTypeDtos)){
            return;
        }

        ComplaintTypeUserDto complaintTypeUserDto = new ComplaintTypeUserDto();
        complaintTypeUserDto.setTypeCd(complaintDtos.get(0).getTypeCd());
        List<ComplaintTypeUserDto> complaintTypeUserDtos = complaintTypeUserV1InnerServiceSMOImpl.queryComplaintTypeUsers(complaintTypeUserDto);

        if (ListUtil.isNull(complaintTypeUserDtos)) {
            return;
        }

        JSONObject content = new JSONObject();
        content.put("complaintName", complaintDtos.get(0).getComplaintName());
        content.put("orderId", complaintId);

        String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN, "STAFF_WECHAT_URL");
        content.put("url", wechatUrl);
        IMsgNotify msgNotify = null;
        if(ComplaintTypeDto.NOTIFY_WAY_SMS.equals(complaintTypeDtos.get(0).getNotifyWay())) {
            msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_ALI);
        }else if(ComplaintTypeDto.NOTIFY_WAY_WECHAT.equals(complaintTypeDtos.get(0).getNotifyWay())){
            msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_WECHAT);
        }else{
            return;
        }

        for(ComplaintTypeUserDto tmpComplaintTypeUserDto:complaintTypeUserDtos) {
             msgNotify.sendComplaintMsg(tmpComplaintTypeUserDto.getCommunityId(), tmpComplaintTypeUserDto.getStaffId(), content);
        }

    }

}
