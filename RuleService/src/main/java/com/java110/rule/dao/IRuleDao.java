package com.java110.rule.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by wuxw on 2017/7/23.
 */
public interface IRuleDao {


    /**
     * 执行 sql
     * @param sql
     * @return
     * @throws Exception
     */
    public List<Map<String,String>> executeSql(String sql) throws Exception;


    public String executeProc(String transactionId,String paramIn,String procName) throws Exception;
}
