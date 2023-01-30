/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.store.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.BusinessServiceDataFlow;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.po.store.StorePo;
import com.java110.store.bmo.store.IUpdateStoreStateBMO;
import com.java110.store.smo.IStoreServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.InitConfigDataException;
import com.java110.utils.exception.InitDataFlowContextException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 商户对外接口类
 * 主要包含 商户相关接口信息
 * Created by wuxw on 2018/5/14.
 */
@RestController
public class StoreApi extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(StoreApi.class);

    @Autowired
    private IUpdateStoreStateBMO updateStoreStateBMOImpl;

    @Autowired
    IStoreServiceSMO storeServiceSMOImpl;

    @RequestMapping(path = "/storeApi/service", method = RequestMethod.GET)
    public String serviceGet(HttpServletRequest request) {
        return DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_ERROR, "不支持Get方法请求").toJSONString();
    }

    /**
     * 商户服务统一处理接口
     *
     * @param orderInfo
     * @param request
     * @return
     */
    @RequestMapping(path = "/storeApi/service", method = RequestMethod.POST)
    public String servicePost(@RequestBody String orderInfo, HttpServletRequest request) {
        BusinessServiceDataFlow businessServiceDataFlow = null;
        JSONObject responseJson = null;
        try {
            Map<String, String> headers = new HashMap<String, String>();
            getRequestInfo(request, headers);
            //预校验
            preValiateOrderInfo(orderInfo);
            businessServiceDataFlow = this.writeDataToDataFlowContext(orderInfo, headers);
            responseJson = storeServiceSMOImpl.service(businessServiceDataFlow);
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
            logger.debug("storeApi 请求报文{},返回报文：{}", orderInfo, responseJson.toJSONString());
        }
        
        return responseJson.toJSONString();
    }

    /**
     * 这里预校验，请求报文中不能有 dataFlowId
     *
     * @param orderInfo
     */
    private void preValiateOrderInfo(String orderInfo) {
       /* if(JSONObject.parseObject(orderInfo).getJSONObject("orders").containsKey("dataFlowId")){
            throw new BusinessException(ResponseConstant.RESULT_CODE_ERROR,"报文中不能存在dataFlowId节点");
        }*/
    }

    /**
     * 获取请求信息
     *
     * @param request
     * @param headers
     * @throws RuntimeException
     */
    private void getRequestInfo(HttpServletRequest request, Map headers) throws Exception {
        try {
            super.initHeadParam(request, headers);
            super.initUrlParam(request, headers);
        } catch (Exception e) {
            logger.error("加载头信息失败", e);
            throw new InitConfigDataException(ResponseConstant.RESULT_PARAM_ERROR, "加载头信息失败");
        }
    }

    public IStoreServiceSMO getStoreServiceSMOImpl() {
        return storeServiceSMOImpl;
    }

    public void setStoreServiceSMOImpl(IStoreServiceSMO storeServiceSMOImpl) {
        this.storeServiceSMOImpl = storeServiceSMOImpl;
    }

    /**
     * 修改商户状态
     *
     * @param reqJson 请求对象
     * @return
     */
    @RequestMapping(value = "/storeApi/updateStoreState", method = RequestMethod.POST)
    public ResponseEntity<String> updateStoreState(@RequestBody JSONObject reqJson) {

        String env = MappingCache.getValue(MappingConstant.ENV_DOMAIN,"HC_ENV");

        if (!"PROD".equals(env)) {
            return ResultVo.error("演示环境，不允许操作");
        }

        Assert.hasKeyAndValue(reqJson, "storeId", "未包含商户信息");
        Assert.hasKeyAndValue(reqJson, "state", "未包含商户状态");

        StorePo storePo = new StorePo();
        storePo.setStoreId(reqJson.getString("storeId"));
        storePo.setState(reqJson.getString("state"));
        return updateStoreStateBMOImpl.update(storePo);
    }
}
