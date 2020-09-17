package com.java110.front.controller;

import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.front.smo.payment.*;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
    private IRentingToPaySMO rentingToPaySMOImpl;

    @Autowired
    private IToPayTempCarInoutSMO toPayTempCarInoutSMOImpl;

    @Autowired
    private IToNotifySMO toNotifySMOImpl;

    @Autowired
    private IRentingToNotifySMO rentingToNotifySMOImpl;



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

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(), postInfo,
                "", "", "", pd.getSessionId(),
                appId);
        return toPaySMOImpl.toPay(newPd);
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

}
