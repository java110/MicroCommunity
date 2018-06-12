package com.java110.log.dao.impl;

import com.java110.common.log.LoggerEngine;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.order.BusiOrder;
import com.java110.entity.order.BusiOrderAttr;
import com.java110.entity.order.OrderList;
import com.java110.entity.order.OrderListAttr;
import com.java110.log.dao.logServiceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 日志服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */

@Service("logServiceDaoImpl")
@Transactional
public class LogServiceDaoImpl extends BaseServiceDao implements logServiceDao {
}
