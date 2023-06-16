package com.java110.community.listener.visit;

import com.java110.community.dao.IVisitServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.system.AppBusiness;
import com.java110.utils.constant.BusinessTypeConstant;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@Java110Listener("examineVisitInfoListener")
@Transactional
public class ExamineVisitInfoListener extends AbstractVisitBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(ExamineVisitInfoListener.class);

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, AppBusiness business) {

    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, AppBusiness business) {

    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, AppBusiness business) {

    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_EXAMINE_VISIT;
    }

    @Override
    public IVisitServiceDao getVisitServiceDaoImpl() {
        return null;
    }
}
