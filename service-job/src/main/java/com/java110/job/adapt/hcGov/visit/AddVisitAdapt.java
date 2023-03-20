package com.java110.job.adapt.hcGov.visit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.visit.VisitDto;
import com.java110.dto.visitSetting.VisitSettingDto;
import com.java110.entity.order.Business;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.community.*;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.owner.VisitPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 访客登记适配器
 *
 * @author fqz
 * @date 2022-04-11
 */
@Component(value = "addVisitAdapt")
public class AddVisitAdapt extends DatabusAdaptImpl {

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMOImpl;

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
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMO;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMO;

    @Autowired
    private IVisitSettingV1InnerServiceSMO visitSettingV1InnerServiceSMOImpl;

    private static Logger logger = LoggerFactory.getLogger(AddVisitAdapt.class);

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    /**
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessVisits = new JSONArray();
        if (data.containsKey(VisitPo.class.getSimpleName())) {
            Object bObj = data.get(VisitPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessVisits.add(bObj);
            } else if (bObj instanceof List) {
                businessVisits = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessVisits = (JSONArray) bObj;
            }
        } else {
            if (data instanceof JSONObject) {
                businessVisits.add(data);
            }
        }
        for (int bVisitIndex = 0; bVisitIndex < businessVisits.size(); bVisitIndex++) {
            JSONObject businessVisit = businessVisits.getJSONObject(bVisitIndex);
            if (businessVisit.containsKey("carNum") && !StringUtil.isEmpty(businessVisit.getString("carNum"))) { //有车辆
                publishMsg(business, businessVisit); //访客登记给有预约车审核权限的员工推送消息
            }
        }
    }

    /**
     * 登记成功给有审核权限的员工推送消息
     *
     * @param business
     * @param businessVisit
     */
    private void publishMsg(Business business, JSONObject businessVisit) {
        VisitDto visitDto = new VisitDto();
        visitDto.setvId(businessVisit.getString("vId"));
        //查询访客信息
        List<VisitDto> visitDtos = visitInnerServiceSMOImpl.queryVisits(visitDto);
        if (!StringUtil.isEmpty(visitDtos.get(0).getState()) && visitDtos.get(0).getState().equals("1")) { //访客审核状态 0表示未审核；1表示审核通过；2表示审核拒绝；3表示审核中
            return;
        }
        //查询访客配置信息
        VisitSettingDto visitSettingDto = new VisitSettingDto();
        visitSettingDto.setCommunityId(businessVisit.getString("communityId"));
        List<VisitSettingDto> visitSettingDtos = visitSettingV1InnerServiceSMOImpl.queryVisitSettings(visitSettingDto);
        if (visitSettingDtos == null || visitSettingDtos.size() != 1 || StringUtil.isEmpty(visitSettingDtos.get(0).getCarNumWay())
                || !visitSettingDtos.get(0).getCarNumWay().equals("Y") || StringUtil.isEmpty(visitSettingDtos.get(0).getAuditWay())
                || visitSettingDtos.get(0).getAuditWay().equals("Y")) { //访客配置信息不存在，或者访客配置信息里面的车辆不同步，或者访客配置访客记录需要流程人审核，就不推送信息
            return;
        }
        //车辆待审核状态，给有审核权限的员工推送消息
        if (!StringUtil.isEmpty(visitDtos.get(0).getCarState()) &&
                (visitDtos.get(0).getCarState().equals("0") || visitDtos.get(0).getCarState().equals("3"))) { //车辆审核状态 0表示未审核；1表示审核通过；2表示审核拒绝；3审核中
            VisitPo visitPo = BeanConvertUtil.covertBean(businessVisit, VisitPo.class);
            //查询小区信息
            CommunityDto communityDto = new CommunityDto();
            communityDto.setCommunityId(visitPo.getCommunityId());
            List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(communityDto);
            Assert.listOnlyOne(communityDtos, "查询小区错误！");
            //查询公众号配置
            SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setWeChatType("1100");
            smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
            smallWeChatDto.setObjId(visitPo.getCommunityId());
            List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
            if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
                logger.info("未配置微信公众号信息,定时任务执行结束");
                return;
            }
            SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
            SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
            smallWechatAttrDto.setCommunityId(visitPo.getCommunityId());
            smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
            smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_PROCESS_TEMPLATE);
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
            // 根据特定权限查询 有该权限的 员工(预约车审核权限)
            BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
            basePrivilegeDto.setResource("/reviewVisitJurisdiction");
            List<UserDto> userDtos = privilegeInnerServiceSMO.queryPrivilegeUsers(basePrivilegeDto);
            String url = sendMsgUrl + accessToken;
            //查询业主信息
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setOwnerId(visitPo.getOwnerId());
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
            Assert.listOnlyOne(ownerDtos, "查询业主信息错误！");
            for (UserDto userDto : userDtos) {
                //根据 userId 查询到openId
                StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
                staffAppAuthDto.setStaffId(userDto.getUserId());
                staffAppAuthDto.setAppType("WECHAT");
                List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMO.queryStaffAppAuths(staffAppAuthDto);
                if (staffAppAuthDtos != null && staffAppAuthDtos.size() > 0) {
                    String openId = staffAppAuthDtos.get(0).getOpenId();
                    Data data = new Data();
                    PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
                    templateMessage.setTemplate_id(templateId);
                    templateMessage.setTouser(openId);
                    data.setFirst(new Content("访客预约车辆，登记信息如下："));
                    data.setKeyword1(new Content(visitPo.getvId()));
                    data.setKeyword2(new Content("访客预约车辆-审核"));
                    data.setKeyword3(new Content(ownerDtos.get(0).getName()));
                    data.setKeyword4(new Content(ownerDtos.get(0).getName() + "提交的访客预约车辆-" + visitPo.getCarNum() + "，需要进行车辆审核。"));
                    data.setKeyword5(new Content("待审核"));
                    data.setRemark(new Content("请及时处理！"));
                    templateMessage.setData(data);
                    //获取员工公众号地址
                    String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN, "STAFF_WECHAT_URL");
                    templateMessage.setUrl(wechatUrl);
                    logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
                    ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
                    logger.info("微信模板返回内容:{}", responseEntity);
                }
            }
        } else if (!StringUtil.isEmpty(visitDtos.get(0).getCarState()) && (visitDtos.get(0).getCarState().equals("1") || visitDtos.get(0).getCarState().equals("2"))) { //车辆不需要审核，此时给业主发信息
            sendMessage(visitDtos); //给业主推送消息
        }
    }

    /**
     * 给业主推送消息
     *
     * @param visitDtos
     */
    private void sendMessage(List<VisitDto> visitDtos) {
        //查询小区信息
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(visitDtos.get(0).getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(communityDto);
        Assert.listOnlyOne(communityDtos, "查询小区错误！");
        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(visitDtos.get(0).getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(visitDtos.get(0).getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_ROOM_STATE_TEMPLATE);
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
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(visitDtos.get(0).getOwnerId());
        //1001 业主本人 1002 家庭成员
        ownerDto.setOwnerTypeCd("1001");
        //查询业主
        List<OwnerDto> ownerDtos = ownerInnerServiceSMO.queryOwners(ownerDto);
        Assert.listOnlyOne(ownerDtos, "业主不存在！");
        //获得成员id
        String memberId = ownerDtos.get(0).getMemberId();
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(memberId);
        ownerAppUserDto.setAppType("WECHAT");
        //查询绑定业主
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMO.queryOwnerAppUsers(ownerAppUserDto);
        if (ownerAppUserDtos.size() > 0) {
            //获取openId
            String openId = ownerAppUserDtos.get(0).getOpenId();
            String url = sendMsgUrl + accessToken;
            Data data = new Data();
            PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
            templateMessage.setTemplate_id(templateId);
            templateMessage.setTouser(openId);
            data.setFirst(new Content("您提交的访客预约车辆-" + visitDtos.get(0).getCarNum() + "，申请结果如下："));
            data.setKeyword1(new Content("访客预约车辆-审核"));
            if (!StringUtil.isEmpty(visitDtos.get(0).getCarState()) && visitDtos.get(0).getCarState().equals("1")) { //审核通过
                data.setKeyword2(new Content("审核通过"));
                //查询停车位
                ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
                parkingSpaceDto.setPsId(visitDtos.get(0).getPsId());
                List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
                Assert.listOnlyOne(parkingSpaceDtos, "查询停车位错误！");
                //查询停车场
                ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
                parkingAreaDto.setPaId(parkingSpaceDtos.get(0).getPaId());
                List<ParkingAreaDto> parkingAreaDtos = parkingAreaInnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
                Assert.listOnlyOne(parkingAreaDtos, "查询停车场错误！");
                data.setKeyword3(new Content("请将车辆(" + visitDtos.get(0).getCarNum() + ")停放于" +
                        parkingAreaDtos.get(0).getNum() + "-" + parkingSpaceDtos.get(0).getNum() + "号停车位，" + "停车免费时间截至到" + visitDtos.get(0).getFreeTime()));
                data.setRemark(new Content("访客预约车辆审核已通过，请您合理安排出行！"));
            } else { //审核不通过
                data.setKeyword2(new Content("审核不通过"));
                data.setKeyword3(new Content(visitDtos.get(0).getStateRemark()));
                data.setRemark(new Content("访客预约车辆审核未通过，请您合理安排出行！"));
            }
            templateMessage.setData(data);
            //获取业主公众号地址
            String wechatUrl = UrlCache.getOwnerUrl();
            templateMessage.setUrl(wechatUrl);
            logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
            ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
            logger.info("微信模板返回内容:{}", responseEntity);
        }
    }
}
