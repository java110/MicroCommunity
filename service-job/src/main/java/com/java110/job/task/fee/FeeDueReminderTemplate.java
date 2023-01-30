package com.java110.job.task.fee;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.task.TaskDto;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author fqz
 * @Description 费用到期提醒
 * @date 2021-01-22 08:40
 */
@Component
public class FeeDueReminderTemplate extends TaskSystemQuartz {

    private static Logger logger = LoggerFactory.getLogger(FeeDueReminderTemplate.class);

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMO;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMO;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMO;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMO;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMO;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMO;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMO;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMO;

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键(黑名单)
    public static final String BLACKLIST = "blacklist";

    //键(费用提前到期提醒天数)
    public static final String FEE_REMINDER_DAYS = "feeDueReminderDays";

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    //短信模板域
    public final static String ALI_SMS_DOMAIN = "ALI_SMS";

    @Override
    protected void process(TaskDto taskDto) {
        logger.debug("开始执行微信模板信息推送" + taskDto.toString());
        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();
        //取出开关映射的备注值
        String remark = MappingCache.getRemark(DOMAIN_COMMON, BLACKLIST);
        String[] split = remark.split(",");
        //将数组转成list集合
        List<String> remarkList = Arrays.asList(split);
        for (CommunityDto communityDto : communityDtos) {
            if (remarkList.contains(communityDto.getCommunityId())) {
                continue;
            } else {
                try {
                    publishMsg(taskDto, communityDto);
                } catch (Exception e) {
                    logger.error("推送消息失败", e);
                }
            }
        }
    }

    private void publishMsg(TaskDto taskDto, CommunityDto communityDto) {
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
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_EXPIRE_TEMPLATE);
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
        FeeDto feeDto = new FeeDto();
        //小区id
        feeDto.setCommunityId(communityDto.getCommunityId());
        //有效数据
        feeDto.setStatusCd("0");
        //收费中状态
        feeDto.setState("2008001");
        //获取当前时间
        Date date = new Date();
        feeDto.setNowDate(date);
        int page;
        int row = 50;
        int count = feeInnerServiceSMO.queryFeesCount(feeDto);
        if (count < 0) {
            return;
        }
        double num = (double) count / (double) row;
        double ceil = Math.ceil(num);
        int size = (int) ceil;
        for (int i = 0; i < size; i++) {
            page = i + 1;
            feeDto.setPage(page);
            feeDto.setRow(row);
            List<FeeDto> feeDtos = feeInnerServiceSMO.queryFees(feeDto);
            //取出开关映射的值(提前提醒天数)
            String day = MappingCache.getValue(DOMAIN_COMMON, FEE_REMINDER_DAYS);
            if (StringUtil.isEmpty(day)) {
                return;
            }
            for (FeeDto tmpFeeDto : feeDtos) {
                //费用到期时间
                Date endTime = tmpFeeDto.getEndTime();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                long time1 = cal.getTimeInMillis();
                cal.setTime(endTime);
                long time2 = cal.getTimeInMillis();
                double between_days = (double) (time2 - time1) / (double) (1000 * 3600 * 24);
                double days = Math.ceil(between_days);
                int feeDay = (int) days;
                int msgTime = Integer.parseInt(day);
                int smallTime = 0;
                if (msgTime > 1) {
                    smallTime = msgTime - 1;
                }
                if (feeDay <= msgTime && feeDay > smallTime) {
                    try {
                        doSentWechat(tmpFeeDto, templateId, accessToken);
                    } catch (Exception e) {
                        logger.error("通知异常", e);
                    }
                } else {
                    continue;
                }
            }
        }
    }

    private void doSentWechat(FeeDto feeDto, String templateId, String accessToken) {
        //获取付费对象类型(3333表示房屋；6666表示车辆)
        String payerObjType = feeDto.getPayerObjType();
        CommunityDto communityDto = new CommunityDto();
        communityDto.setStatusCd("0");
        communityDto.setCommunityId(feeDto.getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(communityDto);
        //获取小区名
        String communityName = communityDtos.get(0).getName();
        String ownerId = "";
        String location = "";
        String feeName = "";
        String msgLocation = "";
        if (payerObjType.equals("3333")) {   //房屋
            //付款方id(即房屋id)
            String payerObjId = feeDto.getPayerObjId();
            RoomDto roomDto = new RoomDto();
            roomDto.setStatusCd("0");
            roomDto.setCommunityId(feeDto.getCommunityId());
            roomDto.setRoomId(payerObjId);
            //查询房屋信息
            List<RoomDto> roomDtos = roomInnerServiceSMO.queryRooms(roomDto);
            //房屋号
            String roomNum = roomDtos.get(0).getRoomNum();
            //单元
            String unitNum = roomDtos.get(0).getUnitNum();
            //楼栋
            String floorNum = roomDtos.get(0).getFloorNum();
            //位置
            location = floorNum + "栋" + unitNum + "单元" + roomNum + "室";
            msgLocation = floorNum + "-" + unitNum + "-" + roomNum;
            feeName = "物业费";
            OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
            //有效数据
            ownerRoomRelDto.setStatusCd("0");
            ownerRoomRelDto.setRoomId(payerObjId);
            //查询房屋业主关系表
            List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMO.queryOwnerRoomRels(ownerRoomRelDto);
            //获取业主id
            ownerId = ownerRoomRelDtos.get(0).getOwnerId();
        } else if (payerObjType.equals("6666")) {   //车辆
            //付款方id(即车辆id)
            String payerObjId = feeDto.getPayerObjId();
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            //有效数据
            ownerCarDto.setStatusCd("0");
            //车辆id
            ownerCarDto.setCarId(payerObjId);
            //查询业主车辆表
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMO.queryOwnerCars(ownerCarDto);
            //车位id
            String psId = ownerCarDtos.get(0).getPsId();
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setStatusCd("0");
            parkingSpaceDto.setPsId(psId);
            parkingSpaceDto.setCommunityId(feeDto.getCommunityId());
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMO.queryParkingSpaces(parkingSpaceDto);
            //获取车位编号
            String num = parkingSpaceDtos.get(0).getNum();
            //获取停车场编号
            String areaNum = parkingSpaceDtos.get(0).getAreaNum();
            location = areaNum + "号停车场" + num + "号车位";
            msgLocation = areaNum + "-" + num;
            feeName = "车位费";
            ownerId = ownerCarDtos.get(0).getOwnerId();
        }
        //获取到期时间
        Date endTime = feeDto.getEndTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String finishTime = format.format(endTime);
        OwnerDto ownerDto = new OwnerDto();
        //有效数据
        ownerDto.setStatusCd("0");
        //业主id
        ownerDto.setOwnerId(ownerId);
        //1001 业主本人 1002 家庭成员
        ownerDto.setOwnerTypeCd("1001");
        //查询业主信息表
        List<OwnerDto> ownerDtos = ownerInnerServiceSMO.queryOwners(ownerDto);
        if (ownerDtos == null || ownerDtos.size() < 1) {
            return;
        }
        //获取业主姓名
        String name = ownerDtos.get(0).getName();
        //获取成员id
        String memberId = ownerDtos.get(0).getMemberId();
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        //有效数据
        ownerAppUserDto.setStatusCd("0");
        //成员id
        ownerAppUserDto.setMemberId(memberId);
        //绑定业主手机端类型
        ownerAppUserDto.setAppType("WECHAT");
        //查询绑定业主
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMO.queryOwnerAppUsers(ownerAppUserDto);
        //用户绑定推送微信公众号信息
        if (ownerAppUserDtos.size() > 0) {
            //获取openId
            String openId = ownerAppUserDtos.get(0).getOpenId();
            String url = sendMsgUrl + accessToken;
            Data data = new Data();
            PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
            templateMessage.setTemplate_id(templateId);
            templateMessage.setTouser(openId);
            data.setFirst(new Content("尊敬的业主" + name + "，您的" + feeName + "即将到期，详细信息如下："));
            data.setKeyword1(new Content(name));
            data.setKeyword2(new Content(communityName + location));
            data.setKeyword3(new Content(finishTime));
            data.setRemark(new Content("请您及时续费，以免影响您的正常使用！"));
            templateMessage.setData(data);
            //获取业主公众号地址
            String wechatUrl = UrlCache.getOwnerUrl();
            templateMessage.setUrl(wechatUrl);
            logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
            ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
            logger.info("微信模板返回内容:{}", responseEntity);
        } else {    //用户没绑定就发送短信
            //获取用户手机号
            String link = ownerDtos.get(0).getLink();
            DefaultProfile profile = DefaultProfile.getProfile(MappingCache.getValue(ALI_SMS_DOMAIN, "region"),
                    MappingCache.getValue(ALI_SMS_DOMAIN, "accessKeyId"),
                    MappingCache.getValue(ALI_SMS_DOMAIN, "accessSecret"));
            IAcsClient client = new DefaultAcsClient(profile);
            CommonRequest request = new CommonRequest();
            request.setSysMethod(MethodType.POST);
            request.setSysDomain("dysmsapi.aliyuncs.com");
            request.setSysVersion("2017-05-25");
            request.setSysAction("SendSms");
            request.putQueryParameter("RegionId", MappingCache.getValue(ALI_SMS_DOMAIN, "region"));
            request.putQueryParameter("PhoneNumbers", link);
            request.putQueryParameter("SignName", MappingCache.getValue(ALI_SMS_DOMAIN, "signName"));
            //获取模板编码(SMS_210079733为缴费成功提示模板编码)
            String feeDueReminderCode = MappingCache.getValue(ALI_SMS_DOMAIN, "FEE_DUE_REMINDER_CODE");
            String substring = "";
            if (!StringUtil.isEmpty(feeDueReminderCode)) {
                substring = feeDueReminderCode.substring(0, 4);
            }
            if (substring.equals("SMS_")) {
                request.putQueryParameter("TemplateCode", feeDueReminderCode);
            }
            request.putQueryParameter("TemplateParam", "{\"user\":" + "\"" + name + "\"" + "," + "\"community\":" + "\"" + communityName + "\"" + "," + "\"house\":" + "\"" + msgLocation + "\"" + "," + "\"feeType\":" + "\"" + feeName + "\"" + "," + "\"time\":" + "\"" + finishTime + "\"" + "}");
            try {
                CommonResponse response = client.getCommonResponse(request);
                logger.debug("发送短信：{}", response.getData());
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
    }
}
