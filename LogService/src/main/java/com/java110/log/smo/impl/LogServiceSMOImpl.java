package com.java110.log.smo.impl;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.log.smo.ILogServiceSMO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单服务业务逻辑处理实现类
 * Created by wuxw on 2017/4/11.
 */
@Service("orderServiceSMOImpl")
@Transactional
public class LogServiceSMOImpl extends BaseServiceSMO implements ILogServiceSMO {
}
