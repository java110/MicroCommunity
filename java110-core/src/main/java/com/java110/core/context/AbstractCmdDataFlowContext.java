package com.java110.core.context;

import java.util.Date;
import java.util.Map;

/**
 * 数据流上下文
 * Created by wuxw on 2018/5/18.
 */
public abstract class AbstractCmdDataFlowContext extends AbstractDataFlowContextPlus implements ICmdDataFlowContext{

    protected AbstractCmdDataFlowContext(){}

    protected AbstractCmdDataFlowContext(Date startDate, String code) {

    }




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
        afterBuilder((ICmdDataFlowContext) dataFlowContext);
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
    public abstract ICmdDataFlowContext doBuilder(String reqInfo, Map<String,String> headerAll) throws Exception;

    protected void afterBuilder(ICmdDataFlowContext dataFlowContext){

    }

}
