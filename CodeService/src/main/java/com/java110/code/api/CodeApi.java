package com.java110.code.api;

import com.java110.code.smo.IPrimaryKeyServiceSMO;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.util.Assert;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.CodeDataFlow;
import com.java110.core.factory.DataFlowFactory;
import com.java110.core.factory.DataTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ID 生成
 * Created by wuxw on 2018/6/3.
 */
@RestController
public class CodeApi extends BaseController {

    @Autowired
    IPrimaryKeyServiceSMO primaryKeyServiceSMOImpl;

    @RequestMapping(path = "/codeApi/generate",method= RequestMethod.GET)
    public String generateGet(HttpServletRequest request) {
        return DataTransactionFactory.createCodeResponseJson(ResponseConstant.NO_TRANSACTION_ID,"-1",
                ResponseConstant.RESULT_CODE_ERROR,"不支持Get方法请求").toJSONString();
    }

    @RequestMapping(path = "/codeApi/generate",method= RequestMethod.POST)
    public String generatePost(@RequestBody String orderInfo,HttpServletRequest request) {
        Map<String, String> headers = new HashMap<String, String>();
        try {
            getRequestInfo(request, headers);
            //预校验
            preValiateOrderInfo(orderInfo,headers);
            CodeDataFlow dataFlow = DataFlowFactory.newInstance(CodeDataFlow.class).builder(orderInfo,null);
            primaryKeyServiceSMOImpl.generateCode(dataFlow);
            return dataFlow.getResJson().toJSONString();
        }catch (Exception e){
            logger.error("请求订单异常",e);
            return DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_ERROR,e.getMessage()+e).toJSONString();
        }
    }

    /**
     * 获取请求信息
     * @param request
     * @param headers
     * @throws RuntimeException
     */
    private void getRequestInfo(HttpServletRequest request,Map headers) throws Exception{
        try{
            super.initHeadParam(request,headers);
            super.initUrlParam(request,headers);
        }catch (Exception e){
            logger.error("加载头信息失败",e);
            throw e;
        }
    }

    /**
     * 这里预校验，请求报文中不能有 dataFlowId
     * @param orderInfo
     */
    private void preValiateOrderInfo(String orderInfo,Map<String, String> headers) {

        Assert.jsonObjectHaveKey(orderInfo,"prefix","没有包含prefix");

        Assert.jsonObjectHaveKey(orderInfo,"transactionId","没有包含transactionId");

    }


    public IPrimaryKeyServiceSMO getPrimaryKeyServiceSMOImpl() {
        return primaryKeyServiceSMOImpl;
    }

    public void setPrimaryKeyServiceSMOImpl(IPrimaryKeyServiceSMO primaryKeyServiceSMOImpl) {
        this.primaryKeyServiceSMOImpl = primaryKeyServiceSMOImpl;
    }
}
