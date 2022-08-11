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
package com.java110.api.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.payment.*;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 支付 处理类
 */
@RestController
@RequestMapping(path = "/app/payment")
public class PaymentController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private IToPaySMO toPaySMOImpl;

    @Autowired
    private IToPayOweFeeSMO toPayOweFeeSMOImpl;

    @Autowired
    private IRentingToPaySMO rentingToPaySMOImpl;

    @Autowired
    private IToPayTempCarInoutSMO toPayTempCarInoutSMOImpl;

    @Autowired
    private IToNotifySMO toNotifySMOImpl;

    @Autowired
    private IRentingToNotifySMO rentingToNotifySMOImpl;

    @Autowired
    private IOweFeeToNotifySMO oweFeeToNotifySMOImpl;

    @Autowired
    private ITempCarFeeToNotifySMO tempCarFeeToNotifySMOImpl;

    @Autowired
    private IToQrPayOweFeeSMO toQrPayOweFeeSMOImpl;

    @Autowired
    private IToPayInGoOutSMO toPayInGoOutSMOImpl;

    @Autowired
    private IToPayBackCitySMO toPayBackCitySMOImpl;

    @Autowired
    private IToPayTempCarFeeSMO toPayTempCarFeeSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    /**
     * <p>统一下单入口</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/toPay", method = RequestMethod.POST)
    public ResponseEntity<String> toPay(@RequestBody String postInfo, HttpServletRequest request) {
        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
        /*IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);*/
        String appId = request.getHeader("APP_ID");
        if (StringUtil.isEmpty(appId)) {
            appId = request.getHeader("APP-ID");
        }

        JSONObject param = JSONObject.parseObject(postInfo);
        UserDto userDto = new UserDto();
        userDto.setUserId(pd.getUserId());
        List<UserDto> users = userInnerServiceSMOImpl.getUsers(userDto);
        Assert.listOnlyOne(users, "查询用户信息错误！");

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), users.get(0).getName(), pd.getToken(), postInfo,
                "", "", "", pd.getSessionId(), appId, param.getString("payerObjId"), param.getString("payerObjType"),
                param.getString("endTime"));
        return toPaySMOImpl.toPay(newPd);
    }

    /**
     * <p>统一下单入口</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/toOweFeePay", method = RequestMethod.POST)
    public ResponseEntity<String> toOweFeePay(@RequestBody String postInfo, HttpServletRequest request) {
        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
        /*IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);*/
        String appId = request.getHeader("APP_ID");
        if (StringUtil.isEmpty(appId)) {
            appId = request.getHeader("APP-ID");
        }

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(), postInfo,
                "", "", "", pd.getSessionId(),
                appId, pd.getHeaders());
        return toPayOweFeeSMOImpl.toPay(newPd);
    }

    /**
     * <p>统一下单入口</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/toPayTempCarFee", method = RequestMethod.POST)
    public ResponseEntity<String> toPayTempCarFee(@RequestBody String postInfo, HttpServletRequest request) {
        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
        /*IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);*/
        String appId = request.getHeader("APP_ID");
        if (StringUtil.isEmpty(appId)) {
            appId = request.getHeader("APP-ID");
        }

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(), postInfo,
                "", "", "", pd.getSessionId(),
                appId, pd.getHeaders());
        return toPayTempCarFeeSMOImpl.toPay(newPd);
    }

    /**
     * <p>统一下单入口</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/toQrInGoOutPay", method = RequestMethod.POST)
    public ResponseEntity<String> toQrInGoOutPay(@RequestBody String postInfo, HttpServletRequest request) {
        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
        /*IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);*/
        String appId = request.getHeader("APP_ID");
        if (StringUtil.isEmpty(appId)) {
            appId = request.getHeader("APP-ID");
        }

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(), postInfo,
                "", "", "", pd.getSessionId(),
                appId);
        return toPayInGoOutSMOImpl.toPay(newPd);
    }

    /**
     * <p>统一下单入口</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/toQrBackCityPay", method = RequestMethod.POST)
    public ResponseEntity<String> toQrBackCityPay(@RequestBody String postInfo, HttpServletRequest request) {
        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
        /*IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);*/
        String appId = request.getHeader("APP_ID");
        if (StringUtil.isEmpty(appId)) {
            appId = request.getHeader("APP-ID");
        }

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(), postInfo,
                "", "", "", pd.getSessionId(),
                appId);
        return toPayBackCitySMOImpl.toPay(newPd);
    }

    /**
     * <p>统一下单入口</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/toPayTempCarInout", method = RequestMethod.POST)
    public ResponseEntity<String> toPayTempCarInout(@RequestBody String postInfo, HttpServletRequest request) {
        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
        /*IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);*/
        String appId = request.getHeader("APP_ID");
        if (StringUtil.isEmpty(appId)) {
            appId = request.getHeader("APP-ID");
        }
        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(), postInfo,
                "", "", "", pd.getSessionId(),
                appId);
        return toPayTempCarInoutSMOImpl.toPay(newPd);
    }


    /**
     * <p>支付回调Api</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/notify", method = RequestMethod.POST)
    public ResponseEntity<String> notify(@RequestBody String postInfo, HttpServletRequest request) {

        logger.debug("微信支付回调报文" + postInfo);

        return toNotifySMOImpl.toNotify(postInfo, request);
    }

    /**
     * <p>支付回调Api</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/notifyChinaUms", method = RequestMethod.POST)
    public ResponseEntity<String> notifyChinaUms(HttpServletRequest request) {
        JSONObject paramIn = new JSONObject();
        for (String key : request.getParameterMap().keySet()) {
            paramIn.put(key, request.getParameter(key));
            logger.debug("银联回调报文form" + key + ":: " + request.getParameter(key));
        }
        logger.debug("微信支付回调报文" + paramIn.toJSONString());

        /*接收参数*/
        Map<String, String> params = getRequestParams(request);
        System.out.println("params:" + params);
        String sign = params.get("sign");
        System.out.println(sign);


        String preStr = buildSignString(params);
        paramIn.put("preSign", preStr);
        paramIn.put("sign", sign);
        //判断签名是否相等

        // 收到通知后记得返回SUCCESS
        return toNotifySMOImpl.toNotify(paramIn.toJSONString(), request);


    }


    /**
     * <p>出租统一下单入口</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/rentingToPay", method = RequestMethod.POST)
    public ResponseEntity<String> rentingToPay(@RequestBody String postInfo, HttpServletRequest request) {
        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
        String appId = request.getHeader("APP_ID");
        if (StringUtil.isEmpty(appId)) {
            appId = request.getHeader("APP-ID");
        }

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(), postInfo,
                "", "", "", pd.getSessionId(),
                appId);
        return rentingToPaySMOImpl.toPay(newPd);
    }

    /**
     * <p>欠费银联回调</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/oweFeeNotifyChinaUms", method = RequestMethod.POST)
    public ResponseEntity<String> oweFeeNotifyChinaUms(HttpServletRequest request) {
        JSONObject paramIn = new JSONObject();
        for (String key : request.getParameterMap().keySet()) {
            paramIn.put(key, request.getParameter(key));
        }
        logger.debug("微信支付回调报文" + paramIn.toJSONString());
        /*接收参数*/
        Map<String, String> params = getRequestParams(request);
        System.out.println("params:" + params);
        String sign = params.get("sign");
        System.out.println(sign);


        String preStr = buildSignString(params);
        paramIn.put("preSign", preStr);
        paramIn.put("sign", sign);

        return oweFeeToNotifySMOImpl.toNotify(paramIn.toJSONString(), request);
    }

    /**
     * <p>出租统一下单入口</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/oweFeeNotify", method = RequestMethod.POST)
    public ResponseEntity<String> oweFeeNotify(@RequestBody String postInfo, HttpServletRequest request) {
        logger.debug("微信支付回调报文" + postInfo);

        return oweFeeToNotifySMOImpl.toNotify(postInfo, request);
    }

    /**
     * <p>出租统一下单入口</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/tempCarFeeNotifyUrl", method = RequestMethod.POST)
    public ResponseEntity<String> tempCarFeeNotifyUrl(@RequestBody String postInfo, HttpServletRequest request) {
        logger.debug("微信支付回调报文" + postInfo);

        return tempCarFeeToNotifySMOImpl.toNotify(postInfo, request);
    }

    /**
     * <p>支付回调Api</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/rentingNotify", method = RequestMethod.POST)
    public ResponseEntity<String> rentingNotify(@RequestBody String postInfo, HttpServletRequest request) {

        logger.debug("微信支付回调报文" + postInfo);

        return rentingToNotifySMOImpl.toNotify(postInfo, request);


    }

    /**
     * <p>出租统一下单入口</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/toQrOweFeePay", method = RequestMethod.POST)
    public ResponseEntity<String> toQrOweFeePay(@RequestBody String postInfo, HttpServletRequest request) {
        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
        String appId = request.getHeader("APP_ID");
        if (StringUtil.isEmpty(appId)) {
            appId = request.getHeader("APP-ID");
        }

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(), postInfo,
                "", "", "", pd.getSessionId(),
                appId);
        return toQrPayOweFeeSMOImpl.toPay(newPd);
    }

    // 构建签名字符串
    public static String buildSignString(Map<String, String> params) {

        // params.put("Zm","test_test");

        if (params == null || params.size() == 0) {
            return "";
        }

        List<String> keys = new ArrayList<>(params.size());

        for (String key : params.keySet()) {
            if ("sign".equals(key))
                continue;
            if ("wId".equals(key))
                continue;
            if (StringUtils.isEmpty(params.get(key)))
                continue;
            keys.add(key);
        }
        //System.out.println(listToString(keys));

        Collections.sort(keys);

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                buf.append(key + "=" + value);
            } else {
                buf.append(key + "=" + value + "&");
            }
        }

        return buf.toString();
    }


}
