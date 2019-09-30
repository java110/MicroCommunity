package com.java110.comment.listener;

import com.java110.comment.dao.ICommentServiceDao;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 商户 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractCommentBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener{

    protected final static Logger logger = LoggerFactory.getLogger(AbstractCommentBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     * @return
     */
    public abstract ICommentServiceDao getCommentServiceDaoImpl();

}
