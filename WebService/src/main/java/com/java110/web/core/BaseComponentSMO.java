package com.java110.web.core;

import com.java110.common.constant.CommonConstant;
import com.java110.common.factory.ApplicationContextFactory;
import com.java110.common.util.Assert;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.context.IPageData;
import com.java110.web.smo.impl.LoginServiceSMOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;

/**
 * Created by wuxw on 2019/3/22.
 */
public class BaseComponentSMO extends BaseServiceSMO {

    private final static Logger logger = LoggerFactory.getLogger(BaseComponentSMO.class);

    /**
     * 调用组件
     * @param componentCode 组件编码
     * @param componentMethod 组件方法
     * @param pd
     * @return
     */
    protected ResponseEntity<String> invokeComponent(String componentCode,String componentMethod,IPageData pd){

        logger.debug("开始调用组件：{}",pd.toString());

        ResponseEntity<String> responseEntity = null;

        Object componentInstance = ApplicationContextFactory.getBean(componentCode);

        Assert.notNull(componentInstance,"未找到组件对应的处理类，请确认 "+componentCode);
        try {

            Method cMethod = componentInstance.getClass().getDeclaredMethod(componentMethod, IPageData.class);

            Assert.notNull(cMethod, "未找到组件对应处理类的方法，请确认 " + componentCode + "方法：" + componentMethod);

            logger.debug("组件编码{}，组件方法{}，pd 为{}", componentCode, componentMethod, pd.toString());

            responseEntity = (ResponseEntity<String>) cMethod.invoke(componentInstance, pd);
        }catch (Exception e){
            logger.error("调用组件失败：",e);
            responseEntity = new ResponseEntity<String>("调用组件"+componentCode+",组件方法"+componentMethod+"失败："+e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            return responseEntity;
        }
    }
}
