package com.java110.db;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class,
                Object.class})
})

public class Java110MybatisInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(Java110MybatisInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }

        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        //获取sql语句
        String sql = showSql(configuration, boundSql);

        switch (sqlCommandType) {
            case INSERT:
                dealInsertSql(mappedStatement, parameter,sql);
                break;
            case UPDATE:
                dealUpdateSql(mappedStatement, parameter,sql);
                break;
            case DELETE:
                dealDeleteSql(mappedStatement, parameter,sql);
                break;
        }
        return invocation.proceed();
    }

    /**
     * 处理删除sql
     *
     * @param mappedStatement
     * @param parameter
     */
    private void dealDeleteSql(MappedStatement mappedStatement, Object parameter,String sql) {
    }

    /**
     * 处理修改 sql
     *
     * @param mappedStatement
     * @param parameter
     */
    private void dealUpdateSql(MappedStatement mappedStatement, Object parameter,String sql) {
    }

    /**
     * 处理insert 语句
     *
     * @param mappedStatement
     * @param parameter
     */
    private void dealInsertSql(MappedStatement mappedStatement, Object parameter,String sql) {
        String deleteSql = "delete from ";



    }

    @Override
    public Object plugin(Object target) {

        return Plugin.wrap(target, this);

    }

    @Override
    public void setProperties(Properties properties) {

    }

    public String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));

            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }

    private String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
//	            System.out.println(value);
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return value;
    }
}
