package com.java110.core.context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.entity.center.DataFlowLinksCost;
import com.java110.entity.center.DataFlowLog;
import org.springframework.http.ResponseEntity;

import java.util.*;

/**
 * 数据流上下文
 * Created by wuxw on 2018/5/18.
 */
public abstract class AbstractOrderDataFlowContext extends AbstractDataFlowContextPlus implements IOrderDataFlowContext{

    protected AbstractOrderDataFlowContext(){}

    protected AbstractOrderDataFlowContext(Date startDate, String code){}

    /**
     * 构建 对象信息
     * @param reqInfo
     * @param headerAll
     * @return
     * @throws Exception
     */
    public  <T> T builder(String reqInfo, Map<String,String> headerAll) throws Exception{
        //预处理
        preBuilder(reqInfo, headerAll);
        //调用builder
        T dataFlowContext = (T)doBuilder(reqInfo, headerAll);
        //后处理
        afterBuilder((IOrderDataFlowContext) dataFlowContext);
        return dataFlowContext;
    }


    /**
     * 预处理
     * @param reqInfo
     * @param headerAll
     */
    protected void preBuilder(String reqInfo, Map<String,String> headerAll) {

    }

    /**
     * 构建对象
     * @param reqInfo
     * @param headerAll
     * @return
     * @throws Exception
     */
    public abstract IOrderDataFlowContext doBuilder(String reqInfo, Map<String,String> headerAll) throws Exception;

    protected void afterBuilder(IOrderDataFlowContext dataFlowContext){

    }

}
