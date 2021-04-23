package com.java110.job.adapt.Repair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
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
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 退单(结单)通知适配器
 *
 * @author fqz
 * @date 2021-01-18 10:22
 */
@Component(value = "machineReturnRepairAdapt")
public class MachineReturnRepairAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(MachineReturnRepairAdapt.class);

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMO;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMO;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMO;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMO;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IPrivilegeInnerServiceSMO privilegeInnerServiceSMO;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMO;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    public void execute(Business business, List<Business> businesses) {
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setbId(business.getbId());
        repairUserDto.setStatusCd("0");
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
        //获取状态
        String state = repairUserDtos.get(0).getState();
        //获取报修id
        String repairId = repairUserDtos.get(0).getRepairId();
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(repairId);
        repairDto.setStatusCd("0");
        List<RepairDto> repairDtos = repairInnerServiceSMO.queryRepairs(repairDto);
        //获取报修人姓名
        String repairName = repairDtos.get(0).getRepairName();
        //获取报修内容
        String context = repairDtos.get(0).getContext();
        //获取报修类型名
        String repairTypeName = repairDtos.get(0).getRepairTypeName();
        //报修地址
        String repairObjName = repairDtos.get(0).getRepairObjName();
        //获取小区id
        String communityId = repairUserDtos.get(0).getCommunityId();
        //查询小区信息
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(communityId);
        List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(communityDto);
        JSONObject paramIn = new JSONObject();
        if (state.equals("10003")) {   //退单状态
            //退单人
            String staffName = repairUserDtos.get(0).getStaffName();
            //退单备注
            String returnContext = repairUserDtos.get(0).getContext();
            paramIn.put("repairTypeName", repairTypeName);
            paramIn.put("repairObjName", repairObjName);
            paramIn.put("staffName", staffName);
            paramIn.put("context", context);
            paramIn.put("returnContext", returnContext);
            sendReturnMessage(paramIn, communityDtos.get(0));
        } else if (state.equals("10002")) {     //结单
            //获取用户id
            String preStaffId = repairUserDtos.get(0).getPreStaffId();
            //获取维修师傅姓名
            String staffName = repairUserDtos.get(0).getStaffName();
            //获取报修对象id
            String repairObjId = repairDtos.get(0).getRepairObjId();
            //获取结单日期
            Date endTime = repairUserDtos.get(0).getEndTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(endTime);
            paramIn.put("repairName", repairName);
            paramIn.put("repairObjName", repairObjName);
            paramIn.put("context", context);
            paramIn.put("staffName", staffName);
            paramIn.put("time", time);
            paramIn.put("repairObjId", repairObjId);
            paramIn.put("preStaffId", preStaffId);
            publishReturnMsg(paramIn, communityDtos.get(0));
        }
    }

    /**
     * 退单给管理员推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void sendReturnMessage(JSONObject paramIn, CommunityDto communityDto) {
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
        // 根据特定权限查询 有该权限的 员工
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/wechatRepairRegistration");
        List<UserDto> userDtos = privilegeInnerServiceSMO.queryPrivilegeUsers(basePrivilegeDto);
        String url = sendMsgUrl + accessToken;
        for (UserDto userDto : userDtos) {
            //根据 userId 查询到openId
            StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
            staffAppAuthDto.setStaffId(userDto.getUserId());
            staffAppAuthDto.setAppType("WECHAT");
            List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMO.queryStaffAppAuths(staffAppAuthDto);
            String openId = staffAppAuthDtos.get(0).getOpenId();
            Data data = new Data();
            PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
            templateMessage.setTemplate_id(templateId);
            templateMessage.setTouser(openId);
            data.setFirst(new Content(paramIn.getString("staffName") + "进行维修工单退单操作，请及时处理，信息如下："));
            data.setKeyword1(new Content(paramIn.getString("repairTypeName")));
            if (communityDto.getName().equals(paramIn.getString("repairObjName"))) {
                data.setKeyword2(new Content(paramIn.getString("repairObjName")));
            } else {
                data.setKeyword2(new Content(communityDto.getName() + paramIn.getString("repairObjName")));
            }
            data.setKeyword3(new Content(paramIn.getString("context")));
            data.setRemark(new Content(paramIn.getString("returnContext")));
            templateMessage.setData(data);
            String wechatUrl = MappingCache.getValue("OWNER_WECHAT_URL");
            templateMessage.setUrl(wechatUrl);
            logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
            ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
            logger.info("微信模板返回内容:{}", responseEntity);
        }
    }

    /**
     * 结单给管理员推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void publishReturnMsg(JSONObject paramIn, CommunityDto communityDto) {
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
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_WORK_ORDER_END_TEMPLATE);
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
        //获取用户id
        String preStaffId = paramIn.getString("preStaffId");
        if (!StringUtil.isEmpty(preStaffId)) {
            OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
            ownerAppUserDto.setUserId(preStaffId);
            ownerAppUserDto.setAppType("WECHAT");
            //查询绑定业主
            List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMO.queryOwnerAppUsers(ownerAppUserDto);
            //获取openId
            String openId = ownerAppUserDtos.get(0).getOpenId();
            String url = sendMsgUrl + accessToken;
            Data data = new Data();
            PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
            templateMessage.setTemplate_id(templateId);
            templateMessage.setTouser(openId);
            data.setFirst(new Content("尊敬的" + paramIn.getString("repairName") + "先生/女士，您的报修问题，已经为您处理完毕。"));
            if (communityDto.getName().equals(paramIn.getString("repairObjName"))) {
                data.setKeyword1(new Content(paramIn.getString("repairObjName")));
            } else {
                data.setKeyword1(new Content(communityDto.getName() + paramIn.getString("repairObjName")));
            }
            data.setKeyword2(new Content(paramIn.getString("context")));
            data.setKeyword3(new Content(paramIn.getString("staffName")));
            data.setKeyword4(new Content(paramIn.getString("time")));
            data.setRemark(new Content("请点击查看详情，对我们的工作进行评价，以便提供更优质的服务，感谢您的配合和使用，祝您生活愉快，阖家欢乐！"));
            templateMessage.setData(data);
            String wechatUrl = MappingCache.getValue("OWNER_WECHAT_URL");
            templateMessage.setUrl(wechatUrl);
            logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
            ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
            logger.info("微信模板返回内容:{}", responseEntity);
        }
    }
}
