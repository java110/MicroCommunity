package com.java110.comment.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.comment.dao.ICommentServiceDao;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 商户 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractCommentBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener{


    /**
     * 获取 DAO工具类
     * @return
     */
    public abstract ICommentServiceDao getCommentServiceDaoImpl();

}
