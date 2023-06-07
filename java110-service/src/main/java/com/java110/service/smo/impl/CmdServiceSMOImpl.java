package com.java110.service.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.context.CmdDataFlow;
import com.java110.core.context.DataFlow;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.ServiceCmdEventPublishing;
import com.java110.core.factory.DataFlowFactory;
import com.java110.core.smo.ISaveTransactionLogSMO;
import com.java110.core.trace.Java110TraceLog;
import com.java110.entity.center.DataFlowLinksCost;
import com.java110.service.smo.ICmdServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.KafkaConstant;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.BusinessException;
import com.java110.utils.exception.SMOException;
import com.java110.utils.kafka.KafkaFactory;
import com.java110.utils.log.LoggerEngine;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * cmd服务处理类
 * Created by wuxw on 2018/4/13.
 */
@Service("cmdServiceSMOImpl")
public class CmdServiceSMOImpl extends LoggerEngine implements ICmdServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(ICmdServiceSMO.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private ISaveTransactionLogSMO saveTransactionLogSMOImpl;


    /**
     * cmd 服务调度
     *
     * @param reqJson 请求报文json
     * @param headers
     * @return
     * @throws SMOException
     */
    @Override
    @Java110TraceLog
    public ResponseEntity<String> cmd(String reqJson, Map<String, String> headers) throws Exception {

        ICmdDataFlowContext cmdDataFlowContext = null;

        Date startDate = DateUtil.getCurrentDate();

        ResponseEntity<String> responseEntity = null;

        //todo 1.0 创建数据流 appId serviceCode
        cmdDataFlowContext = DataFlowFactory.newInstance(CmdDataFlow.class).builder(reqJson, headers);


        //todo 2.0 调用下游系统
        invokeBusinessSystem(cmdDataFlowContext);

        responseEntity = cmdDataFlowContext.getResponseEntity();

        Date endDate = DateUtil.getCurrentDate();

        if (responseEntity == null) {
            responseEntity = ResultVo.success();
        }
        return responseEntity;
    }


    /**
     * 2.0 调用下游系统
     *
     * @param cmdDataFlowContext
     * @throws BusinessException
     */
    private void invokeBusinessSystem(ICmdDataFlowContext cmdDataFlowContext) throws Exception {
        //todo 发布 cmd 事件
        ServiceCmdEventPublishing.multicastEvent(cmdDataFlowContext);
    }


    /**
     * 保存日志信息
     *
     * @param requestJson
     */
    private void saveLogMessage(String requestJson, String responseJson) {

        try {
            if (MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH,MappingConstant.KEY_LOG_ON_OFF))) {
                JSONObject log = new JSONObject();
                log.put("request", requestJson);
                log.put("response", responseJson);
                KafkaFactory.sendKafkaMessage(KafkaConstant.TOPIC_LOG_NAME, "", log.toJSONString());
            }
        } catch (Exception e) {
            logger.error("报错日志出错了，", e);
        }
    }

    /**
     * 保存耗时信息
     *
     * @param cmdDataFlowContext
     */
    private void saveCostTimeLogMessage(DataFlow cmdDataFlowContext) {
        try {
            if (MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH,MappingConstant.KEY_COST_TIME_ON_OFF))) {
                List<DataFlowLinksCost> cmdDataFlowContextLinksCosts = cmdDataFlowContext.getLinksCostDates();
                JSONObject costDate = new JSONObject();
                JSONArray costDates = new JSONArray();
                JSONObject newObj = null;
                for (DataFlowLinksCost cmdDataFlowContextLinksCost : cmdDataFlowContextLinksCosts) {
                    newObj = JSONObject.parseObject(JSONObject.toJSONString(cmdDataFlowContextLinksCost));
                    newObj.put(" cmdDataFlowContextId", cmdDataFlowContext.getDataFlowId());
                    newObj.put("transactionId", cmdDataFlowContext.getTransactionId());
                    costDates.add(newObj);
                }
                costDate.put("costDates", costDates);

                KafkaFactory.sendKafkaMessage(KafkaConstant.TOPIC_COST_TIME_LOG_NAME, "", costDate.toJSONString());
            }
        } catch (Exception e) {
            logger.error("报错日志出错了，", e);
        }
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
