package com.java110.core.event.service;

import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.entity.center.Business;

/**
 * BusinessServiceDataFlowListener 抽象类
 * Created by wuxw on 2018/7/3.
 */
public abstract class AbstractBusinessServiceDataFlowListener implements BusinessServiceDataFlowListener{

    @Override
    public void soService(BusinessServiceDataFlowEvent event) {
        //这里处理业务逻辑数据
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        doSaveStoreInfo(dataFlowContext);
    }

    /**
     * 修改商户信息
     * 主要保存 businessStore，businessStoreAttr，businessStorePhoto，businessStoreCerdentials信息
     * @param dataFlowContext 数据流对象
     */
    private void doSaveStoreInfo(DataFlowContext dataFlowContext){
        String businessType = dataFlowContext.getOrder().getBusinessType();
        Business business = dataFlowContext.getCurrentBusiness();
        // Instance 过程
        if(StatusConstant.REQUEST_BUSINESS_TYPE_INSTANCE.equals(businessType)){
            doBusinessToInstance(dataFlowContext,business);
        }else if(StatusConstant.REQUEST_BUSINESS_TYPE_BUSINESS.equals(businessType)){ // Business过程
            doSaveBusiness(dataFlowContext,business);
        }else if(StatusConstant.REQUEST_BUSINESS_TYPE_DELETE.equals(businessType)){ //撤单过程
            doRecover(dataFlowContext,business);
        }

        dataFlowContext.setResJson(DataTransactionFactory.createBusinessResponseJson(dataFlowContext, ResponseConstant.RESULT_CODE_SUCCESS,"成功",
                dataFlowContext.getParamOut()));
    }


    /**
     * 保存数据至 business表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    protected abstract void doSaveBusiness(DataFlowContext dataFlowContext,Business business);

    /**
     * 将business 数据 同步到 business
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    protected abstract void doBusinessToInstance(DataFlowContext dataFlowContext,Business business);

    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    protected abstract void doRecover(DataFlowContext dataFlowContext,Business business);
}
