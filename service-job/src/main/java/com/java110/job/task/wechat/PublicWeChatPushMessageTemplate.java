package com.java110.job.task.wechat;

import com.alibaba.fastjson.JSON;
import com.java110.core.factory.WechatFactory;
import com.java110.core.smo.fee.IFeeInnerServiceSMO;
import com.java110.core.smo.store.ISmallWeChatInnerServiceSMO;
import com.java110.core.smo.user.IOwnerAppUserInnerServiceSMO;
import com.java110.dto.fee.BillOweFeeDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.task.TaskDto;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @program: MicroCommunity
 * @description: 微信公众号主动推送信息
 * @author: zcc
 * @create: 2020-06-15 13:35
 **/
@Component
public class PublicWeChatPushMessageTemplate extends TaskSystemQuartz {

    private static Logger logger = LoggerFactory.getLogger(PublicWeChatPushMessageTemplate.class);

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    //模板id
    private static String DEFAULT_TEMPLATE_ID = "ZF4j_ug2XW-UGwW1F-Gi4M1-51lpiu-PM89Oa6oZv6w";


    @Override
    protected void process(TaskDto taskDto) throws Exception {
        logger.debug("开始执行微信模板信息推送" + taskDto.toString());

        String templateId = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.KEY_PROPERTY_FEE_TEMPLATE_ID);

        templateId = StringUtil.isEmpty(templateId) ? DEFAULT_TEMPLATE_ID : templateId;

        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);

        if (smallWeChatDtos.size() <= 0 || smallWeChatDto == null) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }

        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());

        if (accessToken == null || accessToken == "") {
            logger.info("推送微信模板,获取accessToken失败:{}", accessToken);
            return;
        }

        //根据小区id查询业主与公众号绑定信息
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setCommunityId(weChatDto.getObjId());
        ownerAppUserDto.setAppType(OwnerAppUserDto.APP_TYPE_WECHAT);
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (ownerAppUserDtos.size() <= 0 || ownerAppUserDtos == null) {
            logger.info("未查询到业主与微信公众号绑定关系");
            return;
        }

        List<String> memberIdList = new ArrayList<>(ownerAppUserDtos.size());
        for (OwnerAppUserDto appUserDto : ownerAppUserDtos) {
            memberIdList.add(appUserDto.getMemberId());
        }

        String[] memberIds = memberIdList.toArray(new String[memberIdList.size()]);
        //查询欠费信息
        BillOweFeeDto billOweFeeDto = new BillOweFeeDto();
        billOweFeeDto.setCommunityId(weChatDto.getObjId());
        billOweFeeDto.setOwnerIds(memberIds);
        billOweFeeDto.setState("1000");
        List<BillOweFeeDto> billOweFeeDtos = feeInnerServiceSMOImpl.queryBillOweFees(billOweFeeDto);

        String url = sendMsgUrl + accessToken;
        for (BillOweFeeDto fee : billOweFeeDtos) {
            for (OwnerAppUserDto appUserDto : ownerAppUserDtos) {
                if (fee.getOwnerId().equals(appUserDto.getMemberId())) {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fee.getFeeEndTime());
                    Calendar now = Calendar.getInstance();
                    now.setTime(date);
                    int year = now.get(Calendar.YEAR);
                    int month = now.get(Calendar.MONTH);
                    Data data = new Data();
                    PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
                    templateMessage.setTemplate_id(templateId);
                    templateMessage.setTouser(appUserDto.getOpenId());
                    data.setFirst(new Content("物业费缴费提醒"));
                    data.setKeyword1(new Content(fee.getPayerObjName()));
                    data.setKeyword2(new Content(year + "年-" + month + "月"));
                    data.setKeyword3(new Content(fee.getAmountOwed()));
                    data.setRemark(new Content("请您及时缴费,如有疑问请联系相关物业人员"));
                    templateMessage.setData(data);
                    logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
                    ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
                    logger.info("微信模板返回内容:{}", responseEntity);
                }
            }

        }
    }
}
