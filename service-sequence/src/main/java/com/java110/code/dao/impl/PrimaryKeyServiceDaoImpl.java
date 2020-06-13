package com.java110.code.dao.impl;

import com.java110.code.dao.IPrimaryKeyServiceDao;
import com.java110.utils.log.LoggerEngine;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    protected final static Logger logger = LoggerFactory.getLogger(PrimaryKeyServiceDaoImpl.class);

    /**
     * 根据主键name查询主键生成ID
     *
     * @param primaryKey 主键name信息
     * @return
     */
    @Override
    public Map queryPrimaryKey(Map primaryKey) {

        LoggerEngine.debug("----【PrimaryKeyServiceDaoImpl.queryPrimaryKey】入参 : " + primaryKey);
        return sqlSessionTemplate.selectOne("primaryKeyServiceDaoImpl.queryPrimaryKey", primaryKey);
    }
}
