package com.java110.job.kafka;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.BusinessServiceDataFlow;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.dto.community.CommunityAttrDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.reportData.ReportDataDto;
import com.java110.dto.reportData.ReportDataHeaderDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.job.adapt.hcGov.HcGovConstant;
import com.java110.job.adapt.hcGov.IReportReturnDataAdapt;
import com.java110.job.adapt.hcGov.asyn.BaseHcGovSendAsyn;
import com.java110.job.smo.IJobServiceSMO;
import com.java110.utils.constant.KafkaConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.InitConfigDataException;
import com.java110.utils.exception.InitDataFlowContextException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.kafka.KafkaFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * kafka侦听
 * Created by wuxw on 2018/4/15.
 */
public class JobServiceKafka extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(JobServiceKafka.class);

    public static final String KAFKA_RETURN = "_RETURN";

    @Autowired
    private IJobServiceSMO jobServiceSMOImpl;
    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;
    @Autowired
    private BaseHcGovSendAsyn baseHcGovSendAsynImpl;

    @KafkaListener(topics = {"jobServiceTopic"})
    public void listen(ConsumerRecord<?, ?> record) {
        logger.info("kafka的key: " + record.key());
        logger.info("kafka的value: " + record.value().toString());
        String orderInfo = record.value().toString();
        BusinessServiceDataFlow businessServiceDataFlow = null;
        JSONObject responseJson = null;
        try {
            Map<String, String> headers = new HashMap<String, String>();
            //预校验
            preValiateOrderInfo(orderInfo);
            businessServiceDataFlow = this.writeDataToDataFlowContext(orderInfo, headers);
            //responseJson = jobServiceSMOImpl.service(businessServiceDataFlow);
        } catch (InitDataFlowContextException e) {
            logger.error("请求报文错误,初始化 BusinessServiceDataFlow失败" + orderInfo, e);
            responseJson = DataTransactionFactory.createNoBusinessTypeBusinessResponseJson(orderInfo, ResponseConstant.RESULT_PARAM_ERROR, e.getMessage(), null);
        } catch (InitConfigDataException e) {
            logger.error("请求报文错误,加载配置信息失败" + orderInfo, e);
            responseJson = DataTransactionFactory.createNoBusinessTypeBusinessResponseJson(orderInfo, ResponseConstant.RESULT_PARAM_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            logger.error("请求订单异常", e);
            responseJson = DataTransactionFactory.createBusinessResponseJson(businessServiceDataFlow, ResponseConstant.RESULT_CODE_ERROR, e.getMessage() + e,
                    null);
        } finally {
            logger.debug("当前请求报文：" + orderInfo + ", 当前返回报文：" + responseJson.toJSONString());
            //只有business 和 instance 过程才做通知消息
            if (!StatusConstant.REQUEST_BUSINESS_TYPE_BUSINESS.equals(responseJson.getString("businessType"))
                    && !StatusConstant.REQUEST_BUSINESS_TYPE_INSTANCE.equals(responseJson.getString("businessType"))) {
                return;
            }
            try {
                KafkaFactory.sendKafkaMessage(KafkaConstant.TOPIC_NOTIFY_CENTER_SERVICE_NAME, "", responseJson.toJSONString());
            } catch (Exception e) {
                logger.error("用户服务通知centerService失败" + responseJson, e);
                //这里保存异常信息
            }
        }
    }

    @KafkaListener(topics = {"${kafka.hcGovTopic}"})
    public void hcGovListen(ConsumerRecord<?, ?> record) {

        String orderInfo = record.value().toString();
        logger.debug("hcGovkafka 接收到数据", orderInfo);
        doListen(orderInfo);

    }

    private void doListen(String orderInfo) {
        JSONObject reqJson = JSONObject.parseObject(orderInfo);
        ReportDataDto reportDataDto = null;
        JSONObject header = reqJson.getJSONObject("header");

        String extCommunityId = header.getString("extCommunityId");

        try {
            //获得小区对象
            String secure = getExtCommunityCode(extCommunityId);
            //报文合规性校验
            preValiateOrderInfo(orderInfo);
            //构建对象
            reportDataDto = freshReportDataDto(orderInfo);
            //签名认证
            AuthenticationFactory.authReportDataSign(reportDataDto, secure);
            //适配器
            IReportReturnDataAdapt reportDataAdapt = ApplicationContextFactory.getBean(reportDataDto.getReportDataHeaderDto().getServiceCode() + KAFKA_RETURN, IReportReturnDataAdapt.class);
            if (reportDataAdapt == null) {
                throw new IllegalArgumentException("serviceCode 错误 请检查");
            }
            //业务处理
            reportDataAdapt.reportReturn(reportDataDto, extCommunityId);

        } catch (Exception e) {
            logger.error("政务回写失败", e);
        }finally {
            //回写日志表
            baseHcGovSendAsynImpl.updateHcGovLog(reqJson);
        }

    }

    /**
     * 获取小区密钥
     *
     * @param extCommunityId
     * @return
     */
    private String getExtCommunityCode(String extCommunityId) {

        CommunityAttrDto communityAttrDto = new CommunityAttrDto();
        communityAttrDto.setSpecCd(CommunityAttrDto.SPEC_CD_GOV);
        communityAttrDto.setValue(extCommunityId);
        List<CommunityAttrDto> tmpCommunityAttrDtos = communityInnerServiceSMOImpl.getCommunityAttrs(communityAttrDto);
        if (tmpCommunityAttrDtos == null || tmpCommunityAttrDtos.size() < 1) {
            return HcGovConstant.COMMUNITY_SECURE;
        }
        communityAttrDto = new CommunityAttrDto();
        communityAttrDto.setSpecCd(CommunityAttrDto.SPEC_CD_GOV_SECURE);
        communityAttrDto.setCommunityId(tmpCommunityAttrDtos.get(0).getCommunityId());
        tmpCommunityAttrDtos = communityInnerServiceSMOImpl.getCommunityAttrs(communityAttrDto);
        if (tmpCommunityAttrDtos == null || tmpCommunityAttrDtos.size() < 1) {
            return HcGovConstant.COMMUNITY_SECURE;
        }
        return tmpCommunityAttrDtos.get(0).getValue();
    }

    /**
     * 构建对象
     *
     * @param orderInfo
     * @return
     */
    private ReportDataDto freshReportDataDto(String orderInfo) {
        ReportDataDto reportDataDto = new ReportDataDto();
        JSONObject reqJson = JSONObject.parseObject(orderInfo);
        ReportDataHeaderDto reportDataHeaderDto = BeanConvertUtil.covertBean(reqJson.getJSONObject("header"), ReportDataHeaderDto.class);
        reportDataDto.setReportDataHeaderDto(reportDataHeaderDto);
        reportDataDto.setReportDataBodyDto(reqJson.getJSONObject("body"));

        return reportDataDto;
    }

    /**
     * 这里预校验，请求报文中不能有 dataFlowId
     *
     * @param orderInfo
     */
    private void preValiateOrderInfo(String orderInfo) {
        JSONObject reqJson = JSONObject.parseObject(orderInfo);
        Assert.hasKeyAndValue(reqJson, "header", "请求报文中未包含header");
        Assert.hasKeyAndValue(reqJson, "body", "请求报文中未包含body");
        JSONObject header = reqJson.getJSONObject("header");
        Assert.hasKeyAndValue(header, "serviceCode", "请求报文中未包含serviceCode");
        Assert.hasKeyAndValue(header, "sign", "请求报文中未包含sign");
        Assert.hasKeyAndValue(header, "resTime", "请求报文中未包含reqTime");
        Assert.hasKeyAndValue(header, "code", "请求报文中未包含reqTime");
        Assert.hasKeyAndValue(header, "msg", "请求报文中未包含reqTime");
    }
}
