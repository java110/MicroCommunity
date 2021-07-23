package com.java110.job.task.wechat;

import com.alibaba.fastjson.JSON;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.feeCollectionOrder.FeeCollectionOrderDto;
import com.java110.dto.logSystemError.LogSystemErrorDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.reportOweFee.ReportOweFeeDto;
import com.java110.dto.reportOweFee.ReportOweFeeItemDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.task.TaskDto;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.Miniprogram;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.fee.IFeeCollectionOrderInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.logSystemError.LogSystemErrorPo;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ExceptionUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: MicroCommunity
 * @description: 费用催缴信息模板
 * @author: zcc
 * @create: 2020-06-15 13:35
 **/
@Component
public class FeeCollectionPushMessageTemplate extends TaskSystemQuartz {

    private static Logger logger = LoggerFactory.getLogger(FeeCollectionPushMessageTemplate.class);

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IFeeCollectionOrderInnerServiceSMO feeCollectionOrderInnerServiceSMOImpl;


    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    //模板id
    private static String DEFAULT_TEMPLATE_ID = "ZF4j_ug2XW-UGwW1F-Gi4M1-51lpiu-PM89Oa6oZv6w";

    private static final int DEFAULT_FEE_COUNT = 200;


    @Override
    protected void process(TaskDto taskDto) {
        logger.debug("开始执行微信模板信息推送" + taskDto.toString());

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            try {
                publishMsg(taskDto, communityDto);
            } catch (Exception e) {
                logger.error("推送消息失败", e);
            }
        }
    }

    private void publishMsg(TaskDto taskDto, CommunityDto communityDto) throws Exception {

        FeeCollectionOrderDto feeCollectionOrderDto = new FeeCollectionOrderDto();
        feeCollectionOrderDto.setCommunityId(communityDto.getCommunityId());
        feeCollectionOrderDto.setState(FeeCollectionOrderDto.STATE_WAIT);
        List<FeeCollectionOrderDto> feeCollectionOrderDtos = feeCollectionOrderInnerServiceSMOImpl.queryFeeCollectionOrders(feeCollectionOrderDto);

        if (feeCollectionOrderDtos == null || feeCollectionOrderDtos.size() < 1) {
            return;
        }

        ReportOweFeeDto reportOweFeeDto = new ReportOweFeeDto();
        reportOweFeeDto.setCommunityId(communityDto.getCommunityId());
        reportOweFeeDto.setHasOweFee("Y");
        long oweCount = reportOweFeeInnerServiceSMOImpl.queryReportOweFeesCount(reportOweFeeDto);
        if (oweCount == 0) {
            return;
        }

        double maxPage = Math.ceil(oweCount / DEFAULT_FEE_COUNT);

        for (int roomIndex = 0; roomIndex < maxPage; roomIndex++) {
            try {
                doReportOweFeePushMessage(roomIndex * DEFAULT_FEE_COUNT, (roomIndex + 1) * DEFAULT_FEE_COUNT, communityDto, feeCollectionOrderDtos.get(0));
            } catch (Exception e) {
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_OWE_FEE);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("欠费推送失败" + communityDto.getCommunityId(), e);
            }
        }
    }

    private void doReportOweFeePushMessage(int page, int row, CommunityDto communityDto, FeeCollectionOrderDto feeCollectionOrderDto) {
        ReportOweFeeDto reportOweFeeDto = new ReportOweFeeDto();
        reportOweFeeDto.setCommunityId(communityDto.getCommunityId());
        reportOweFeeDto.setHasOweFee("Y");
        reportOweFeeDto.setPage(page);
        reportOweFeeDto.setRow(row);
        List<ReportOweFeeDto> reportOweFeeDtos = reportOweFeeInnerServiceSMOImpl.queryReportOweFees(reportOweFeeDto);
        refreshReportOwe(reportOweFeeDtos);

        pushMessage(reportOweFeeDtos, feeCollectionOrderDto);
    }

    private void pushMessage(List<ReportOweFeeDto> reportOweFeeDtos, FeeCollectionOrderDto feeCollectionOrderDto) {
        for (ReportOweFeeDto reportOweFeeDo : reportOweFeeDtos) {
            try {
                doPushMessage(reportOweFeeDo, feeCollectionOrderDto);
            } catch (Exception e) {
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_OWE_FEE);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("欠费推送失败" + feeCollectionOrderDto.getCollectionName(), e);
            }
        }
    }

    /**
     * 推送消息
     *
     * @param reportOweFeeDo
     * @param feeCollectionOrderDto
     */
    private void doPushMessage(ReportOweFeeDto reportOweFeeDo, FeeCollectionOrderDto feeCollectionOrderDto) {

        switch (feeCollectionOrderDto.getCollectionWay()) {
            case FeeCollectionOrderDto.COLLECTION_WAY_SMS:
                doSendSms(reportOweFeeDo, feeCollectionOrderDto);
                break;
            case FeeCollectionOrderDto.COLLECTION_WAY_WECHAT:
                doSendWechat(reportOweFeeDo, feeCollectionOrderDto);
                break;
            case FeeCollectionOrderDto.COLLECTION_WAY_WECHAT_SMS:
                doSendWechatOrSms(reportOweFeeDo, feeCollectionOrderDto);
                break;
        }

    }

    /**
     * 微信或者 短信推送
     *
     * @param reportOweFeeDo
     * @param feeCollectionOrderDto
     */
    private void doSendWechatOrSms(ReportOweFeeDto reportOweFeeDo, FeeCollectionOrderDto feeCollectionOrderDto) {
        Map paramInfo = getOwnerAppUserDto(feeCollectionOrderDto.getCommunityId(), reportOweFeeDo.getOwnerId());
        if (paramInfo == null) {
            doSendSms(reportOweFeeDo, feeCollectionOrderDto);
            return;
        }
        doSendWechat(reportOweFeeDo, feeCollectionOrderDto, paramInfo);
    }

    /**
     * 微信方式推送
     *
     * @param reportOweFeeDo
     * @param feeCollectionOrderDto
     */
    private void doSendWechat(ReportOweFeeDto reportOweFeeDo, FeeCollectionOrderDto feeCollectionOrderDto) {
        Map paramInfo = getOwnerAppUserDto(feeCollectionOrderDto.getCommunityId(), reportOweFeeDo.getOwnerId());
        if (paramInfo == null) {
            return;
        }
        doSendWechat(reportOweFeeDo, feeCollectionOrderDto, paramInfo);
    }

    /**
     * 微信方式推送
     *
     * @param reportOweFeeDo
     * @param feeCollectionOrderDto
     */
    private void doSendWechat(ReportOweFeeDto reportOweFeeDo, FeeCollectionOrderDto feeCollectionOrderDto, Map paramInfo) {
        if (paramInfo == null) {
            return;
        }
        String templateId = paramInfo.get("templateId").toString();
        String url = paramInfo.get("url").toString();
        String oweRoomUrl = paramInfo.get("oweCarUrl").toString();
        String oweCarUrl = paramInfo.get("oweCarUrl").toString();
        SmallWeChatDto weChatDto = (SmallWeChatDto)paramInfo.get("weChatDto");
        Miniprogram miniprogram = paramInfo.get("oweCarUrl") == null ? null : (Miniprogram) paramInfo.get("oweCarUrl");
        List<OwnerAppUserDto> ownerAppUserDtos = (List<OwnerAppUserDto>) paramInfo.get("ownerAppUserDtos");

        List<ReportOweFeeItemDto> itemDtos = reportOweFeeDo.getItems();
        String oweUrl = "";
        for (ReportOweFeeItemDto itemDto : itemDtos) {
            oweUrl = FeeDto.PAYER_OBJ_TYPE_ROOM.equals(reportOweFeeDo.getPayerObjType()) ? oweRoomUrl : oweCarUrl;
            for (OwnerAppUserDto appUserDto : ownerAppUserDtos) {
                try {

                    Data data = new Data();
                    PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
                    templateMessage.setTemplate_id(templateId);
                    templateMessage.setTouser(appUserDto.getOpenId());
                    /*data.setFirst(new Content("物业费缴费提醒"));*/
                    data.setFirst(new Content(itemDto.getFeeName() + "提醒"));
                    data.setKeyword1(new Content(itemDto.getPayerObjName()));
                    data.setKeyword2(new Content(itemDto.getAmountOwed()));
                    data.setKeyword3(new Content(
                            DateUtil.getFormatTimeString(itemDto.getStartTime(), DateUtil.DATE_FORMATE_STRING_B)
                                    + "至"
                                    + DateUtil.getFormatTimeString(itemDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_B)));
                    data.setRemark(new Content("请您及时缴费,如有疑问请联系相关物业人员"));
                    if (!StringUtil.isEmpty(oweUrl)) {
                        if (miniprogram == null) {
                            templateMessage.setUrl(oweUrl + itemDto.getPayerObjId() + "&wAppId=" + weChatDto.getAppId());
                        } else {
                            miniprogram.setPagepath(oweUrl.split("@@")[1] + itemDto.getPayerObjId() + "&wAppId=" + weChatDto.getAppId());
                            templateMessage.setMiniprogram(miniprogram);
                        }
                    }
                    templateMessage.setData(data);
                    logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
                    ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
                    logger.info("微信模板返回内容:{}", responseEntity);
                } catch (Exception e) {
                    LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                    logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                    logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_OWE_FEE);
                    logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                    saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                    logger.error("欠费推送失败" + feeCollectionOrderDto.getCollectionName(), e);
                }
            }
        }
    }

    /**
     * 短信方式推送
     *
     * @param reportOweFeeDo
     * @param feeCollectionOrderDto
     */
    private void doSendSms(ReportOweFeeDto reportOweFeeDo, FeeCollectionOrderDto feeCollectionOrderDto) {

    }

    private Map getOwnerAppUserDto(String communityId, String memberId) {
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityId);
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);

        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return null;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);


        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityId);
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_OWE_FEE_TEMPLATE);
        List<SmallWechatAttrDto> smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);

        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.info("未配置微信公众号消息模板");
            return null;
        }

        String templateId = smallWechatAttrDtos.get(0).getValue();

        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());

        if (StringUtil.isEmpty(accessToken)) {
            logger.info("推送微信模板,获取accessToken失败:{}", accessToken);
            return null;
        }

        //根据小区id查询业主与公众号绑定信息
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setCommunityId(weChatDto.getObjId());
        ownerAppUserDto.setAppType(OwnerAppUserDto.APP_TYPE_WECHAT);
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
            return null;
        }

        String sendTemplate = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.SEND_TEMPLATE_URL);
        if (StringUtil.isEmpty(sendTemplate)) {
            sendTemplate = sendMsgUrl;
        }
        String url = sendTemplate + accessToken;

        String oweRoomUrl = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.OWE_FEE_PAGE);
        String oweCarUrl = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.OWE_CAR_FEE_PAGE);
        Miniprogram miniprogram = null;
        if (oweRoomUrl.contains("@@")) {
            miniprogram = new Miniprogram();
            miniprogram.setAppid(oweRoomUrl.split("@@")[0]);
        }
        //车辆费用
        if (oweCarUrl.contains("@@")) {
            miniprogram = new Miniprogram();
            miniprogram.setAppid(oweCarUrl.split("@@")[0]);
        }

        Map paramInfo = new HashMap();
        paramInfo.put("templateId", templateId);
        paramInfo.put("url", url);
        paramInfo.put("miniprogram", miniprogram);
        paramInfo.put("oweCarUrl", oweCarUrl);
        paramInfo.put("oweRoomUrl", oweRoomUrl);
        paramInfo.put("ownerAppUserDtos", ownerAppUserDtos);
        paramInfo.put("weChatDto", weChatDto);
        return paramInfo;
    }

    private void refreshReportOwe(List<ReportOweFeeDto> oldReportOweFeeDtos) {
        List<String> payObjIds = new ArrayList<>();

        if (oldReportOweFeeDtos == null || oldReportOweFeeDtos.size() < 1) {
            return;
        }

        for (ReportOweFeeDto reportOweFeeDto : oldReportOweFeeDtos) {
            payObjIds.add(reportOweFeeDto.getPayerObjId());
        }
        ReportOweFeeDto reportOweFeeDto = new ReportOweFeeDto();
        reportOweFeeDto.setPayerObjIds(payObjIds.toArray(new String[payObjIds.size()]));
        List<ReportOweFeeDto> allReportOweFeeDtos = reportOweFeeInnerServiceSMOImpl.queryReportAllOweFees(reportOweFeeDto);
        List<ReportOweFeeDto> reportOweFeeDtos = new ArrayList<>();
        for (ReportOweFeeDto reportOweFee : allReportOweFeeDtos) {
            ReportOweFeeDto reportOwe = new ReportOweFeeDto();
            reportOwe.setOweId(reportOweFee.getOweId());
            reportOweFee.setPayerObjType(reportOweFee.getPayerObjType());
            if (reportOweFee.getPayerObjType().equals("3333")) {
                List<ReportOweFeeDto> reportOweFees = reportOweFeeInnerServiceSMOImpl.queryReportAllOweFeesByRoom(reportOwe);
                reportOweFee.setOweId(reportOweFees.get(0).getOweId());
                reportOweFee.setOwnerName(reportOweFees.get(0).getOwnerName());
            } else if (reportOweFee.getPayerObjType().equals("6666")) {
                List<ReportOweFeeDto> reportOweFees = reportOweFeeInnerServiceSMOImpl.queryReportAllOweFeesByCar(reportOwe);
                reportOweFee.setOweId(reportOweFees.get(0).getOweId());
                reportOweFee.setOwnerName(reportOweFees.get(0).getOwnerName());
            }
            reportOweFeeDtos.add(reportOweFee);
        }
        for (ReportOweFeeDto tmpReportOweFeeDto : oldReportOweFeeDtos) {
            dealItem(tmpReportOweFeeDto, reportOweFeeDtos);
        }

    }

    private void dealItem(ReportOweFeeDto oldReportOweFeeDto, List<ReportOweFeeDto> allReportOweFeeDtos) {
        List<ReportOweFeeItemDto> items = new ArrayList<>();
        if (allReportOweFeeDtos == null || allReportOweFeeDtos.size() < 1) {
            return;
        }

        ReportOweFeeItemDto reportOweFeeItemDto = null;
        for (ReportOweFeeDto reportOweFeeDto : allReportOweFeeDtos) {
            if (!oldReportOweFeeDto.getPayerObjId().equals(reportOweFeeDto.getPayerObjId())) {
                continue;
            }
            reportOweFeeItemDto = hasItem(items, reportOweFeeDto.getConfigId());
            if (reportOweFeeItemDto == null) {
                reportOweFeeItemDto = new ReportOweFeeItemDto();
                reportOweFeeItemDto.setConfigId(reportOweFeeDto.getConfigId());
                reportOweFeeItemDto.setFeeName(reportOweFeeDto.getFeeName());
                reportOweFeeItemDto.setAmountOwed(reportOweFeeDto.getAmountOwed());
                reportOweFeeItemDto.setPayerObjId(reportOweFeeDto.getPayerObjId());
                reportOweFeeItemDto.setPayerObjName(reportOweFeeDto.getPayerObjName());
                reportOweFeeItemDto.setConfigName(reportOweFeeDto.getConfigName());
                try {
                    reportOweFeeItemDto.setStartTime(DateUtil.getDateFromString(reportOweFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
                    reportOweFeeItemDto.setEndTime(DateUtil.getDateFromString(reportOweFeeDto.getDeadlineTime(), DateUtil.DATE_FORMATE_STRING_A));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                items.add(reportOweFeeItemDto);
            } else {
                BigDecimal oldAmount = new BigDecimal(Double.parseDouble(reportOweFeeItemDto.getAmountOwed()));
                oldAmount = oldAmount.add(new BigDecimal(Double.parseDouble(reportOweFeeDto.getAmountOwed()))).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                reportOweFeeItemDto.setAmountOwed(oldAmount.doubleValue() + "");
            }
            if (!StringUtil.isEmpty(reportOweFeeDto.getOwnerName()) && StringUtil.isEmpty(oldReportOweFeeDto.getOwnerName())) {
                oldReportOweFeeDto.setOwnerName(reportOweFeeDto.getOwnerName());
            }
            oldReportOweFeeDto.setUpdateTime(reportOweFeeDto.getUpdateTime());
            oldReportOweFeeDto.setConfigName(reportOweFeeDto.getConfigName());
        }

        //计算总金额
        BigDecimal totalAmount = new BigDecimal(0);
        Date startTime = null;
        Date endTime = null;
        for (ReportOweFeeItemDto tempReportOweFeeItemDto : items) {
            if (startTime == null) {
                startTime = tempReportOweFeeItemDto.getStartTime();
            }
            if (startTime.getTime() > tempReportOweFeeItemDto.getStartTime().getTime()) {
                startTime = tempReportOweFeeItemDto.getStartTime();
            }
            if (endTime == null) {
                endTime = tempReportOweFeeItemDto.getEndTime();
            }
            if (endTime.getTime() < tempReportOweFeeItemDto.getEndTime().getTime()) {
                endTime = tempReportOweFeeItemDto.getEndTime();
            }
            totalAmount = totalAmount.add(new BigDecimal(Double.parseDouble(tempReportOweFeeItemDto.getAmountOwed()))).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }
        oldReportOweFeeDto.setEndTime(DateUtil.getFormatTimeString(startTime, DateUtil.DATE_FORMATE_STRING_A));
        oldReportOweFeeDto.setDeadlineTime(DateUtil.getFormatTimeString(endTime, DateUtil.DATE_FORMATE_STRING_A));
        oldReportOweFeeDto.setItems(items);
        oldReportOweFeeDto.setPayerObjName(items.get(0).getPayerObjName());
        oldReportOweFeeDto.setAmountOwed(totalAmount.doubleValue() + "");

    }

    private ReportOweFeeItemDto hasItem(List<ReportOweFeeItemDto> reportOweFeeItemDtos, String configId) {
        if (reportOweFeeItemDtos == null || reportOweFeeItemDtos.size() < 1) {
            return null;
        }
        for (ReportOweFeeItemDto reportOweFeeItemDto : reportOweFeeItemDtos) {
            if (reportOweFeeItemDto.getConfigId().equals(configId)) {
                return reportOweFeeItemDto;
            }
        }

        return null;
    }
}
