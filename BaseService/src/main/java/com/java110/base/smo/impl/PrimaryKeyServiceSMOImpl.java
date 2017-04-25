package com.java110.base.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.base.dao.IPrimaryKeyServiceDao;
import com.java110.base.smo.IPrimaryKeyServiceSMO;
import com.java110.common.log.LoggerEngine;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.entity.user.BoCust;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户服务信息管理业务信息实现
 * Created by wuxw on 2017/4/5.
 */
@Service("PrimaryKeyServiceSMOImpl")
public class PrimaryKeyServiceSMOImpl extends BaseServiceSMO implements IPrimaryKeyServiceSMO {


    @Autowired
    IPrimaryKeyServiceDao iPrimaryKeyServiceDao;

    /**
     * 根据sequence 表中name 查询ID
     *
     * @param primaryKeyInfo name信息封装
     * @return
     */
    public JSONObject queryPrimaryKey(JSONObject primaryKeyInfo) throws Exception {
        Map paramIn = JSONObject.toJavaObject(primaryKeyInfo, Map.class);
        Map primaryKey = iPrimaryKeyServiceDao.queryPrimaryKey(paramIn);
        JSONObject returnPrimaryKey = new JSONObject();
        if (primaryKey != null && primaryKey.containsKey("targetId")) {
            returnPrimaryKey.put("targetId", primaryKey.get("targetId"));
        } else {
            //如果没定义相应name的键序列，直接返回-1 表示 自己系统需要自己生成
            returnPrimaryKey.put("targetId", "-1");
        }
        return returnPrimaryKey;
    }


}
