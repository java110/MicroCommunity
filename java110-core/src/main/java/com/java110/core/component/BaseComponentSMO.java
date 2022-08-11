package com.java110.core.component;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;

/**
 * Created by wuxw on 2019/3/22.
 */
public class BaseComponentSMO extends BaseServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(BaseComponentSMO.class);

    protected static final int MAX_ROW = 200;

    /**
     * 调用组件
     *
     * @param componentCode   组件编码
     * @param componentMethod 组件方法
     * @param pd
     * @return
     */
    protected ResponseEntity<String> invokeComponent(String componentCode, String componentMethod, IPageData pd) {

        logger.debug("开始调用组件：{}", pd.toString());

        ResponseEntity<String> responseEntity = null;

        Object componentInstance = ApplicationContextFactory.getBean(componentCode);

        Assert.notNull(componentInstance, "未找到组件对应的处理类，请确认 " + componentCode);
        try {

            Method cMethod = componentInstance.getClass().getDeclaredMethod(componentMethod, IPageData.class);

            Assert.notNull(cMethod, "未找到组件对应处理类的方法，请确认 " + componentCode + "方法：" + componentMethod);

            logger.debug("组件编码{}，组件方法{}，pd 为{}", componentCode, componentMethod, pd.toString());

            responseEntity = (ResponseEntity<String>) cMethod.invoke(componentInstance, pd);
        } catch (Exception e) {
            logger.error("调用组件失败：", e);
            responseEntity = new ResponseEntity<String>("调用组件" + componentCode + ",组件方法" + componentMethod + "失败：" + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
        }
        return responseEntity;
    }

    /**
     * 根据 请求路径 判断用户是否有权限操作
     *
     * @param pd
     * @param restTemplate
     */
    protected void checkUserHasPrivilege(IPageData pd, RestTemplate restTemplate) {
        //pd.get
    }

    /**
     * 根据 请求路径 判断用户是否有权限操作
     *
     * @param pd
     * @param restTemplate
     */
    protected void checkUserHasPrivilege(IPageData pd, RestTemplate restTemplate, String privilege) {
        //pd.get
    }

    /**
     * 分页信息校验
     *
     * @param pd 页面数据封装
     */
    protected void validatePageInfo(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "row", "请求报文中未包含row节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "page", "请求报文中未包含page节点");
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.isInteger(paramIn.getString("row"), "row必须为数字");
        Assert.isInteger(paramIn.getString("page"), "page必须为数字");
    }
}
