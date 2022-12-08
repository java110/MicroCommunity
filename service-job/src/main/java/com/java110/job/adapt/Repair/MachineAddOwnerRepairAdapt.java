package com.java110.job.adapt.Repair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import com.java110.dto.user.UserDto;
import com.java110.entity.order.Business;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.owner.RepairPoolPo;
import com.java110.utils.cache.MappingCache;
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
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private IPrivilegeInnerServiceSMO privilegeInnerServiceSMO;

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMO;

    @Autowired
    private RestTemplate outRestTemplate;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

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
        sendMessage(paramIn, communityDtos.get(0));
    }

    /**
     * 报修登记给员工推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void sendMessage(JSONObject paramIn, CommunityDto communityDto) {
        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityDto.getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityDto.getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_WORK_ORDER_REMIND_TEMPLATE);
        List<SmallWechatAttrDto> smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.info("未配置微信公众号消息模板");
            return;
        }
        String templateId = smallWechatAttrDtos.get(0).getValue();
        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());
        if (StringUtil.isEmpty(accessToken)) {
            logger.info("推送微信模板,获取accessToken失败:{}", accessToken);
            return;
        }
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
        String url = sendMsgUrl + accessToken;
        List<String> userIds = new ArrayList<>();
        for (UserDto userDto : userDtos) {
            if (userIds.contains(userDto.getUserId())) {
                continue;
            }
            userIds.add(userDto.getUserId());
            //根据 userId 查询到openId
            StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
            staffAppAuthDto.setStaffId(userDto.getUserId());
            staffAppAuthDto.setAppType("WECHAT");
            List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMO.queryStaffAppAuths(staffAppAuthDto);
            if (staffAppAuthDtos == null || staffAppAuthDtos.size() < 1) {
                continue;
            }
            String openId = staffAppAuthDtos.get(0).getOpenId();
            Data data = new Data();
            PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
            templateMessage.setTemplate_id(templateId);
            templateMessage.setTouser(openId);
            data.setFirst(new Content("业主" + paramIn.getString("repairName") + "提交了新的报修工单，报修信息如下："));
            data.setKeyword1(new Content(paramIn.getString("repairTypeName")));
            if (communityDto.getName().equals(paramIn.getString("repairObjName"))) {
                data.setKeyword2(new Content(paramIn.getString("repairObjName")));
            } else {
                data.setKeyword2(new Content(communityDto.getName() + paramIn.getString("repairObjName")));
            }
            data.setKeyword3(new Content(paramIn.getString("context")));
            data.setRemark(new Content("请您及时确认信息，并安排相关人员进行处理，感谢您的使用！"));
            templateMessage.setData(data);
            //获取员工公众号地址
            String wechatUrl = MappingCache.getValue("STAFF_WECHAT_URL");
            templateMessage.setUrl(wechatUrl);
            logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
            ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
            logger.info("微信模板返回内容:{}", responseEntity);
        }
    }
}
