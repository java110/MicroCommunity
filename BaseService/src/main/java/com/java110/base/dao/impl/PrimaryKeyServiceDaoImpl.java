package com.java110.base.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.base.dao.IPrimaryKeyServiceDao;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.dao.BaseServiceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 用户服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */

/**
 * 用户信息实现工程
 * Created by wuxw on 2016/12/27.
 */
@Service("primaryKeyServiceDaoImpl")
@Transactional
public class PrimaryKeyServiceDaoImpl extends BaseServiceDao implements IPrimaryKeyServiceDao {



    /**
     * 根据主键name查询主键生成ID
     * @param primaryKey 主键name信息
     * @return
     */
    @Override
    public Map queryPrimaryKey(Map primaryKey) {

        LoggerEngine.debug("----【PrimaryKeyServiceDaoImpl.queryPrimaryKey】入参 : " + primaryKey);
        return sqlSessionTemplate.selectOne("primaryKeyServiceDaoImpl.queryPrimaryKey",primaryKey);
    }
}
