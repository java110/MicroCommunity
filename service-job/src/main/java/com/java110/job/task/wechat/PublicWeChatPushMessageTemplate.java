package com.java110.job.task.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.BillOweFeeDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.oweFeeCallable.OweFeeCallableDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.reportFee.ReportOweFeeDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.dto.wechat.SmallWechatAttrDto;
import com.java110.dto.task.TaskDto;
import com.java110.dto.wechat.Content;
import com.java110.dto.wechat.Data;
import com.java110.dto.wechat.Miniprogram;
import com.java110.dto.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IOweFeeCallableV1InnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.job.msgNotify.MsgNotifyFactory;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.oweFeeCallable.OweFeeCallablePo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
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
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IOweFeeCallableV1InnerServiceSMO oweFeeCallableV1InnerServiceSMOImpl;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    //模板id
    private static String DEFAULT_TEMPLATE_ID = "ZF4j_ug2XW-UGwW1F-Gi4M1-51lpiu-PM89Oa6oZv6w";


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

        ReportOweFeeDto reportOweFeeDto = new ReportOweFeeDto();
        reportOweFeeDto.setCommunityId(communityDto.getCommunityId());
        reportOweFeeDto.setHasOweFee("Y");

        List<ReportOweFeeDto> reportOweFeeDtos = reportOweFeeInnerServiceSMOImpl.queryReportAllOweFees(reportOweFeeDto);

        if (reportOweFeeDtos == null || reportOweFeeDtos.size() < 1) {
            return;
        }

        OweFeeCallablePo oweFeeCallablePo = null;
        List<OweFeeCallablePo> oweFeeCallablePos = new ArrayList<>();
        String notifyWay = MappingCache.getValue(MappingConstant.ENV_DOMAIN, "DEFAULT_MSG_NOTIFY_WAY");
        if (StringUtil.isEmpty(notifyWay) || MsgNotifyFactory.NOTIFY_WAY_WECHAT.equals(notifyWay)) {
            notifyWay = OweFeeCallableDto.CALLABLE_WAY_WECHAT;
        } else {
            notifyWay = OweFeeCallableDto.CALLABLE_WAY_SMS;
        }

        for (ReportOweFeeDto fee : reportOweFeeDtos) {
            oweFeeCallablePo = new OweFeeCallablePo();

            oweFeeCallablePo.setAmountdOwed(reportOweFeeDto.getAmountOwed());
            oweFeeCallablePo.setCallableWay(notifyWay);
            oweFeeCallablePo.setOfcId(GenerateCodeFactory.getGeneratorId("11"));
            oweFeeCallablePo.setFeeId(reportOweFeeDto.getFeeId());
            oweFeeCallablePo.setFeeName(reportOweFeeDto.getFeeName());
            oweFeeCallablePo.setCommunityId(communityDto.getCommunityId());
            oweFeeCallablePo.setConfigId(reportOweFeeDto.getConfigId());
            oweFeeCallablePo.setOwnerId(reportOweFeeDto.getOwnerId());
            oweFeeCallablePo.setOwnerName(reportOweFeeDto.getOwnerName());
            oweFeeCallablePo.setPayerObjId(fee.getPayerObjId());
            oweFeeCallablePo.setPayerObjName(reportOweFeeDto.getPayerObjName());
            oweFeeCallablePo.setPayerObjType(reportOweFeeDto.getPayerObjType());
            oweFeeCallablePo.setRemark("系统自动催缴");
            oweFeeCallablePo.setStaffId("-1");
            oweFeeCallablePo.setStaffName("系统自动催缴");
            oweFeeCallablePo.setState(OweFeeCallableDto.STATE_WAIT);
            oweFeeCallablePo.setStartTime(reportOweFeeDto.getEndTime());
            oweFeeCallablePo.setEndTime(reportOweFeeDto.getDeadlineTime());
            oweFeeCallablePos.add(oweFeeCallablePo);

        }

        if (oweFeeCallablePos.size() < 1) {
            return;
        }

        int flag = oweFeeCallableV1InnerServiceSMOImpl.saveOweFeeCallables(oweFeeCallablePos);
        if (flag < 1) {
            throw new IllegalArgumentException("保存催缴记录失败");
        }
        String oweUrl = "";
        JSONObject content = null;
        String oweRoomUrl = UrlCache.getOwnerUrl() + MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.OWE_FEE_PAGE);
        String oweCarUrl = UrlCache.getOwnerUrl() + MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.OWE_CAR_FEE_PAGE);

        OweFeeCallablePo updateOweFeeCallablePo = null;
        OwnerAppUserDto ownerAppUserDto = null;
        for (OweFeeCallablePo tmpOweFeeCallablePo : oweFeeCallablePos) {
            if (StringUtil.isEmpty(tmpOweFeeCallablePo.getOwnerId()) || tmpOweFeeCallablePo.getOwnerId().startsWith("-")) {
                updateOweFeeCallablePo = new OweFeeCallablePo();
                updateOweFeeCallablePo.setOfcId(tmpOweFeeCallablePo.getOfcId());
                updateOweFeeCallablePo.setCommunityId(tmpOweFeeCallablePo.getCommunityId());
                updateOweFeeCallablePo.setState(OweFeeCallableDto.STATE_FAIL);
                updateOweFeeCallablePo.setRemark(tmpOweFeeCallablePo.getRemark() + "-业主不存在");
                oweFeeCallableV1InnerServiceSMOImpl.updateOweFeeCallable(updateOweFeeCallablePo);
                continue;
            }
            ownerAppUserDto = new OwnerAppUserDto();
            ownerAppUserDto.setMemberId(tmpOweFeeCallablePo.getOwnerId());
            ownerAppUserDto.setCommunityId(tmpOweFeeCallablePo.getCommunityId());
            ownerAppUserDto.setAppType(OwnerAppUserDto.APP_TYPE_WECHAT);
            List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
            if(ownerAppUserDtos == null || ownerAppUserDtos.size() < 1){
                updateOweFeeCallablePo = new OweFeeCallablePo();
                updateOweFeeCallablePo.setOfcId(tmpOweFeeCallablePo.getOfcId());
                updateOweFeeCallablePo.setCommunityId(tmpOweFeeCallablePo.getCommunityId());
                updateOweFeeCallablePo.setState(OweFeeCallableDto.STATE_FAIL);
                updateOweFeeCallablePo.setRemark(tmpOweFeeCallablePo.getRemark() + "-业主未绑定");
                oweFeeCallableV1InnerServiceSMOImpl.updateOweFeeCallable(updateOweFeeCallablePo);
                continue;
            }

            oweUrl = FeeDto.PAYER_OBJ_TYPE_ROOM.equals(tmpOweFeeCallablePo.getPayerObjType()) ? oweRoomUrl : oweCarUrl;
            content = new JSONObject();
            content.put("feeTypeName", tmpOweFeeCallablePo.getFeeName());
            content.put("payerObjName", tmpOweFeeCallablePo.getPayerObjName());
            content.put("billAmountOwed", tmpOweFeeCallablePo.getAmountdOwed());
            content.put("date", DateUtil.dateTimeToDate(tmpOweFeeCallablePo.getStartTime()) + "~" + DateUtil.dateTimeToDate(tmpOweFeeCallablePo.getEndTime()));
            content.put("url", oweUrl);
            ResultVo resultVo = MsgNotifyFactory.sendOweFeeMsg(communityDto.getCommunityId(), ownerAppUserDtos.get(0).getUserId(), content);
            updateOweFeeCallablePo = new OweFeeCallablePo();
            updateOweFeeCallablePo.setOfcId(tmpOweFeeCallablePo.getOfcId());
            updateOweFeeCallablePo.setCommunityId(tmpOweFeeCallablePo.getCommunityId());
            if (resultVo.getCode() != ResultVo.CODE_OK) {
                updateOweFeeCallablePo.setState(OweFeeCallableDto.STATE_FAIL);
                updateOweFeeCallablePo.setRemark(tmpOweFeeCallablePo.getRemark() + "-" + resultVo.getMsg());
            } else {
                updateOweFeeCallablePo.setState(OweFeeCallableDto.STATE_COMPLETE);
            }
            oweFeeCallableV1InnerServiceSMOImpl.updateOweFeeCallable(updateOweFeeCallablePo);
        }
    }
}
