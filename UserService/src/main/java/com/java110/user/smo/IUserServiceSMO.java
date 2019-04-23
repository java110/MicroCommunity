package com.java110.user.smo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.exception.SMOException;
import com.java110.core.context.BusinessServiceDataFlow;
import com.java110.entity.user.Cust;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 *
 * 用户信息管理，服务
 * Created by wuxw on 2017/4/5.
 */
public interface IUserServiceSMO {



    public JSONObject service(BusinessServiceDataFlow businessServiceDataFlow) throws SMOException;


}
