package com.java110.job.adapt.hcGov.visit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.machine.CarBlackWhiteDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.ownerCarAttr.OwnerCarAttrDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.visit.VisitDto;
import com.java110.entity.order.Business;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IParkingAreaInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IVisitInnerServiceSMO;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.po.owner.VisitPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
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
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IIotSendAsyn hcOwnerVisitAsynImpl;

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarAttrInnerServiceSMO ownerCarAttrInnerServiceSMOImpl;

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
    private ICarBlackWhiteV1InnerServiceSMO carBlackWhiteV1InnerServiceSMOImpl;

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
            if (businessVisit.containsKey("state") && !StringUtil.isEmpty(businessVisit.getString("state")) && businessVisit.getString("state").equals("0")) {
                publishMsg(business, businessVisit);
            }
            doAddVisit(business, businessVisit);
        }
    }

    /**
     * 登记成功给有审核权限的员工推送消息
     *
     * @param business
     * @param businessVisit
     */
    private void publishMsg(Business business, JSONObject businessVisit) {
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
                String wechatUrl = MappingCache.getValue("STAFF_WECHAT_URL");
                templateMessage.setUrl(wechatUrl);
                logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
                ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
                logger.info("微信模板返回内容:{}", responseEntity);
            }
        }
    }

    /**
     * 审核通过预约车下发
     *
     * @param business
     * @param businessVisit
     */
    private void doAddVisit(Business business, JSONObject businessVisit) {
        VisitPo visitPo = BeanConvertUtil.covertBean(businessVisit, VisitPo.class);
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(visitPo.getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listNotNull(communityDtos, "未包含小区信息");

        if (!StringUtil.isEmpty(visitPo.getState()) && visitPo.getState().equals("1")) { //状态为审核通过
            VisitDto visitDto = new VisitDto();
            visitDto.setvId(visitPo.getvId());
            List<VisitDto> visits = visitInnerServiceSMOImpl.queryVisits(visitDto);
            Assert.listOnlyOne(visits, "访客信息不存在或存在多条");
            if (!StringUtil.isEmpty(visits.get(0).getPsId())) {
                //查询停车位
                ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
                parkingSpaceDto.setPsId(visits.get(0).getPsId());
                List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
                Assert.listOnlyOne(parkingSpaceDtos, "访客登记适配器,查询停车位错误！");
                //查询停车场
                ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
                parkingAreaDto.setPaId(parkingSpaceDtos.get(0).getPaId());
                List<ParkingAreaDto> parkingAreaDtos = parkingAreaInnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
                Assert.listOnlyOne(parkingAreaDtos, "查询停车场错误！");

                //查询车辆信息
                OwnerCarDto ownerCarDto = new OwnerCarDto();
                ownerCarDto.setCarNum(visitPo.getCarNum());
                ownerCarDto.setCommunityId(visitPo.getCommunityId());
                List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
                Assert.listOnlyOne(ownerCarDtos, "未找到车辆,或存在多条！");

                //查询属性
                OwnerCarAttrDto ownerCarAttrDto = new OwnerCarAttrDto();
                ownerCarAttrDto.setCarId(ownerCarDtos.get(0).getCarId());
                ownerCarAttrDto.setCommunityId(ownerCarDtos.get(0).getCommunityId());
                List<OwnerCarAttrDto> parkingAreaAttrDtos = ownerCarAttrInnerServiceSMOImpl.queryOwnerCarAttrs(ownerCarAttrDto);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                JSONObject body = new JSONObject();
                body.put("vId", visits.get(0).getvId());
                body.put("vName", visits.get(0).getvName());
                body.put("visitGender", visits.get(0).getVisitGender());
                body.put("phoneNumber", visits.get(0).getPhoneNumber());
                body.put("userId", visits.get(0).getUserId());
                body.put("communityId", visits.get(0).getCommunityId());
                body.put("ownerId", visits.get(0).getOwnerId());
                body.put("visitCase", visits.get(0).getVisitCase());
                body.put("visitTime", df.format(ownerCarDtos.get(0).getStartTime()));
                body.put("departureTime", df.format(ownerCarDtos.get(0).getEndTime()));
                body.put("carNum", visits.get(0).getCarNum());
                body.put("entourage", visits.get(0).getEntourage());
                body.put("reasonType", visits.get(0).getReasonType());
                body.put("state", visits.get(0).getState());
                body.put("stateRemark", visits.get(0).getStateRemark());
                body.put("stateName", visits.get(0).getStateName());
                body.put("paId", parkingAreaDtos.get(0).getPaId());
                body.put("psId", parkingSpaceDtos.get(0).getPsId());
                body.put("extCarId", ownerCarDtos.get(0).getCarId());
                body.put("attrs", parkingAreaAttrDtos);
                body.put("extCommunityId", visits.get(0).getCommunityId());
                body.put("leaseType", ownerCarDtos.get(0).getLeaseType());
                CarBlackWhiteDto carBlackWhiteDto = new CarBlackWhiteDto();
                carBlackWhiteDto.setCarNum(visits.get(0).getCarNum());
                carBlackWhiteDto.setBlackWhite("2222"); //1111 黑名单 2222 白名单
                carBlackWhiteDto.setStartTime(visitPo.getVisitTime());
                carBlackWhiteDto.setEndTime(visitPo.getFreeTime());
                carBlackWhiteDto.setCommunityId(visitPo.getCommunityId());
                List<CarBlackWhiteDto> carBlackWhiteDtos = carBlackWhiteV1InnerServiceSMOImpl.queryCarBlackWhites(carBlackWhiteDto);
                Assert.listOnlyOne(carBlackWhiteDtos, "查询白名单错误！");
                body.put("extBwId", carBlackWhiteDtos.get(0).getBwId());
                hcOwnerVisitAsynImpl.addVisit(body);
            }
        }
    }
}
