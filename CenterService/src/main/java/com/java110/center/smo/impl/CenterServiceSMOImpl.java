package com.java110.center.smo.impl;

import com.java110.center.smo.ICenterServiceSMO;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.factory.DataFlowFactory;
import com.java110.common.util.DateUtil;
import com.java110.common.util.ResponseTemplateUtil;
import com.java110.entity.center.DataFlow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 中心服务处理类
 * Created by wuxw on 2018/4/13.
 */
@Service("centerServiceSMOImpl")
@Transactional
public class CenterServiceSMOImpl implements ICenterServiceSMO {


    @Override
    public String service(String reqJson, HttpServletRequest request) {

        DataFlow dataFlow = null;

        try{
            //1.0 创建消息流
            dataFlow = DataFlowFactory.newInstance().builder(reqJson, request);

        }catch (Exception e){
            return ResponseTemplateUtil.createOrderResponseJson(dataFlow == null
                            ?ResponseConstant.NO_TRANSACTION_ID
                            :dataFlow.getTransactionId(),
                    ResponseConstant.NO_NEED_SIGN,ResponseConstant.RESULT_CODE_INNER_ERROR,"内部异常了："+e.getMessage()+e.getLocalizedMessage());
        }finally {
            //这里记录日志
            Date endDate = DateUtil.getCurrentDate();
            if(dataFlow == null){ //说明异常了
                return null;
            }
            dataFlow.setEndDate(endDate);
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow,"service","业务处理总耗时",dataFlow.getStartDate(),dataFlow.getEndDate());

            //这里保存耗时，以及日志

            return ResponseTemplateUtil.createCommonResponseJson(dataFlow);
        }

    }
}
