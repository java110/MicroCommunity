package com.java110.job.adapt.Repair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.community.CommunityMemberDto;
import com.java110.dto.complaintType.ComplaintTypeDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairSettingDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.dto.wechat.SmallWechatAttrDto;
import com.java110.dto.user.StaffAppAuthDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.system.Business;
import com.java110.dto.wechat.Content;
import com.java110.dto.wechat.Data;
import com.java110.dto.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.msgNotify.IMsgNotify;
import com.java110.job.msgNotify.MsgNotifyFactory;
import com.java110.po.owner.RepairPoolPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 登记报修通知适配器
 *
 * @author fqz
 * @date 2020-12-11  18:54
 */
@Component(value = "machineAddOwnerRepairAdapt")
public class MachineAddOwnerRepairAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(MachineAddOwnerRepairAdapt.class);

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMO;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMO;

    @Autowired
    private IPrivilegeInnerServiceSMO privilegeInnerServiceSMO;

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMO;


    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessOwnerRepairs = new JSONArray();
        System.out.println("收到日志：>>>>>>>>>>>>>" + data.toJSONString());
        if (data.containsKey(RepairPoolPo.class.getSimpleName())) {
            Object bObj = data.get(RepairPoolPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessOwnerRepairs.add(bObj);
            } else if (bObj instanceof List) {
                businessOwnerRepairs = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessOwnerRepairs = (JSONArray) bObj;
            }
        } else {
            if (data instanceof JSONObject) {
                businessOwnerRepairs.add(data);
            }
        }
        //JSONObject businessOwnerCar = data.getJSONObject("businessOwnerCar");
        for (int bOwnerRepairIndex = 0; bOwnerRepairIndex < businessOwnerRepairs.size(); bOwnerRepairIndex++) {
            JSONObject businessOwnerRepair = businessOwnerRepairs.getJSONObject(bOwnerRepairIndex);
            doDealOwnerRepair(business, businessOwnerRepair);
        }
    }

    private void doDealOwnerRepair(Business business, JSONObject businessOwnerRepair) {
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(businessOwnerRepair.getString("repairId"));
//        repairDto.setStatusCd("0");
        List<RepairDto> repairDtos = repairInnerServiceSMO.queryRepairs(repairDto);
        //获取报修类型
        String repairTypeName = repairDtos.get(0).getRepairTypeName();
        //获取小区id
        String communityId = repairDtos.get(0).getCommunityId();
        //获取位置信息
        String repairObjName = repairDtos.get(0).getRepairObjName();
        //获取报修内容
        String context = repairDtos.get(0).getContext();
        //获取业主姓名
        String repairName = repairDtos.get(0).getRepairName();
        //查询小区信息
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(communityId);
        List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(communityDto);
        JSONObject paramIn = new JSONObject();
        paramIn.put("repairTypeName", repairTypeName);
        paramIn.put("communityId", communityId);
        paramIn.put("repairObjName", repairObjName);
        paramIn.put("context", context);
        paramIn.put("repairName", repairName);
        paramIn.put("repairId", repairDtos.get(0).getRepairId());
        sendMessage(paramIn, communityDtos.get(0), repairDtos.get(0));
    }

    /**
     * 报修登记给员工推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void sendMessage(JSONObject paramIn, CommunityDto communityDto, RepairDto repairDto) {

        //查询小区物业公司
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(communityDto.getCommunityId());
        communityMemberDto.setAuditStatusCd(CommunityMemberDto.AUDIT_STATUS_NORMAL);
        communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMO.getCommunityMembers(communityMemberDto);
        Assert.listOnlyOne(communityMemberDtos, "小区没有 物业公司");
        // 根据特定权限查询 有该权限的 员工
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/wechatRepairRegistration");
        basePrivilegeDto.setStoreId(communityMemberDtos.get(0).getMemberId());
        basePrivilegeDto.setCommunityId(communityMemberDtos.get(0).getCommunityId());
        List<UserDto> userDtos = privilegeInnerServiceSMO.queryPrivilegeUsers(basePrivilegeDto);
        List<String> userIds = new ArrayList<>();
        IMsgNotify msgNotify = null;
        if (RepairSettingDto.NOTIFY_WAY_SMS.equals(repairDto.getNotifyWay())) {
            msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_ALI);
        } else if (RepairSettingDto.NOTIFY_WAY_WECHAT.equals(repairDto.getNotifyWay())) {
            msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_WECHAT);
        } else {
            return;
        }
        for (UserDto userDto : userDtos) {
            if (userIds.contains(userDto.getUserId())) {
                continue;
            }
            JSONObject content = new JSONObject();
            content.put("repairTypeName", paramIn.getString("repairTypeName"));
            if (communityDto.getName().equals(paramIn.getString("repairObjName"))) {
                content.put("repairObjName", paramIn.getString("repairObjName"));
            } else {
                content.put("repairObjName", communityDto.getName() + paramIn.getString("repairObjName"));
            }
            content.put("repairName", paramIn.getString("repairName"));
            content.put("context", paramIn.getString("context"));
            content.put("repairId", paramIn.getString("repairId"));
            String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN, "STAFF_WECHAT_URL");
            content.put("url", wechatUrl);

            try {
                msgNotify.sendAddOwnerRepairMsg(communityDto.getCommunityId(), userDto.getUserId(), content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
