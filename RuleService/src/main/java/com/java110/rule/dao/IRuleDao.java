package com.java110.rule.dao;

import com.java110.entity.rule.Rule;
import com.java110.entity.rule.RuleEntrance;

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


    /**
     *  查询Rule_Entrance
     * @return
     * @throws Exception
     */
    public Map<String,RuleEntrance> getRuleEntranceMap() throws Exception;


    /**
     * 查询规则组关系信息
     * @return
     * @throws Exception
     */
    public  List getRuleGroupRelaList() throws Exception;


    /**
     * 查询规则信息
     * @return
     * @throws Exception
     */
    public Map<String, Rule> getRuleMap() throws Exception;
}
