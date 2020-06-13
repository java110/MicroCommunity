package com.java110.generator.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.generator.smo.IPrimaryKeyServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.ResponseErrorException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.CodeDataFlow;
import com.java110.core.factory.DataFlowFactory;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.core.smo.code.ICodeApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ID 生成
 * Created by wuxw on 2018/6/3.
 */
@RestController
public class CodeApi extends BaseController implements ICodeApi {

    protected static Logger logger = LoggerFactory.getLogger(CodeApi.class);


    @Autowired
    IPrimaryKeyServiceSMO primaryKeyServiceSMOImpl;

    /**
     * 生成编码服务器 不支持Get方法请求
     * @param request http 请求对象
     * @return 不支持Get方法请求
     */
    @Deprecated
    @RequestMapping(path = "/codeApi/generate", method = RequestMethod.GET)
    public String generateGet(HttpServletRequest request) {
        return DataTransactionFactory.createCodeResponseJson(ResponseConstant.NO_TRANSACTION_ID, "-1",
                ResponseConstant.RESULT_CODE_ERROR, "不支持Get方法请求").toJSONString();
    }

    /**
     * 生成编码服务器 不支持Get方法请求
     * @param orderInfo 请求信息
     * @param request http 请求对象
     * @return 不支持Get方法请求
     */
    @RequestMapping(path = "/codeApi/generate", method = RequestMethod.POST)
    public String generatePost(@RequestBody String orderInfo, HttpServletRequest request) {
        Map<String, String> headers = new HashMap<String, String>();
        try {
            getRequestInfo(request, headers);
            //预校验
            preValidateOrderInfo(orderInfo, headers);
            CodeDataFlow dataFlow = DataFlowFactory.newInstance(CodeDataFlow.class).builder(orderInfo, null);
            primaryKeyServiceSMOImpl.generateCode(dataFlow);
            return dataFlow.getResJson().toJSONString();
        } catch (Exception e) {
            logger.error("请求订单异常", e);
            return DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_ERROR, e.getMessage() + e).toJSONString();
        }
    }

    /**
     * 获取请求信息
     *
     * @param request 请求信息封装
     * @param headers 请求头信息
     * @throws Exception 处理数据失败会返回Exception异常
     */
    private void getRequestInfo(HttpServletRequest request, Map headers) throws Exception {
        try {
            super.initHeadParam(request, headers);
            super.initUrlParam(request, headers);
        } catch (Exception e) {
            logger.error("加载头信息失败", e);
            throw e;
        }
    }

    /**
     * 这里预校验，请求报文中不能有 dataFlowId
     *
     * @param orderInfo 请求信息封装
     * @param headers  请求头信息
     */
    private void preValidateOrderInfo(String orderInfo, Map<String, String> headers) {

        Assert.jsonObjectHaveKey(orderInfo, "prefix", "没有包含prefix");

        Assert.jsonObjectHaveKey(orderInfo, "transactionId", "没有包含transactionId");

    }


    public IPrimaryKeyServiceSMO getPrimaryKeyServiceSMOImpl() {
        return primaryKeyServiceSMOImpl;
    }

    public void setPrimaryKeyServiceSMOImpl(IPrimaryKeyServiceSMO primaryKeyServiceSMOImpl) {
        this.primaryKeyServiceSMOImpl = primaryKeyServiceSMOImpl;
    }

    /**
     * 生成 编码
     *
     * @param prefix 前缀
     * @return
     */
    @Override
    @RequestMapping(value = "/codeApi/generateCode", method = RequestMethod.POST)
    public String generateCode(@RequestParam("prefix") String prefix) {

        try {
            JSONObject requestInfo = new JSONObject();

            //封装符合构建CodeDataFlow对象的JSON对象参数
            builderRequestInfo(prefix, requestInfo);

            CodeDataFlow dataFlow = DataFlowFactory.newInstance(CodeDataFlow.class).builder(requestInfo.toJSONString(), null);

            //生成编码
            primaryKeyServiceSMOImpl.generateCode(dataFlow);

            if (!ResponseConstant.RESULT_CODE_SUCCESS.equals(dataFlow.getResJson().getString("code"))) {
                throw new ResponseErrorException(ResponseConstant.RESULT_CODE_ERROR, "生成编码失败 "
                        + dataFlow.getResJson().getString("message"));
            }

            return dataFlow.getResJson().getString("id");
        } catch (Exception e) {
            logger.error("请求订单异常", e);
            return ResponseConstant.RESULT_CODE_ERROR;
        }
    }

    /**
     * 封装符合构建CodeDataFlow对象的JSON对象参数
     *
     * @param prefix      前缀
     * @param requestInfo 构建的请求JSON对象
     */
    private void builderRequestInfo(String prefix, JSONObject requestInfo) {
        requestInfo.put("transactionId", UUID.randomUUID().toString().replace("-", ""));
        requestInfo.put("prefix", prefix);
        requestInfo.put("requestTime", DateUtil.getNowDefault());
    }
}
