package com.java110.rule.dao.impl;

import com.java110.utils.constant.RuleDomain;
import com.java110.utils.util.SerializeUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.rule.Rule;
import com.java110.entity.rule.RuleCondCfg;
import com.java110.entity.rule.RuleEntrance;
import com.java110.rule.dao.IRuleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by wuxw on 2017/7/23.
 */

@Service("ruleDaoImpl")
@Transactional
public class RuleDaoImpl extends BaseServiceDao implements IRuleDao {
    private final static Logger logger = LoggerFactory.getLogger(RuleDaoImpl.class);


    @Override
    public List<Map<String, String>> executeSql(String sql) throws Exception {
        return sqlSessionTemplate.selectList("ruleDaoImpl.executeSql",sql);
    }

    /**
     *
     * @param transactionId
     * @param paramIn
     * @param procName
     * @return
     * @throws Exception
     */
    @Override
    public String executeProc(String transactionId, String paramIn, String procName) throws Exception {

        Map<String,String> paramInMap = new HashMap<String,String>();
        paramInMap.put("transactionId",transactionId);

        paramInMap.put("procName",procName);

        paramInMap.put("paramIn",paramIn);

        long updateFlag = sqlSessionTemplate.update("ruleDaoImpl.executeProc",paramInMap);

        if (updateFlag > 0){
            return paramInMap.get("paramOut");
        }
        return null;
    }

    /**
     * 查询 rule_entrance表信息
     *
     * 这里逻辑，首先从ehCache 中获取数据，如果没有缓存，则校验redis中是否存在，如果不存在从数据库中查询，然后保存至redis中，
     * 自动缓存到ehcache 中（默认是10分钟）
     * @return
     * @throws Exception
     */
    @Override
    @Cacheable(key=RuleDomain.REDIS_KEY_RULE_ENTRANCE)
    public Map<String, RuleEntrance> getRuleEntranceMap() throws Exception {

        Jedis jedis = getJedis();

        Map map = new HashMap();

        List<RuleEntrance> list = null;

        if(jedis.exists(RuleDomain.REDIS_KEY_RULE_ENTRANCE.getBytes())){
            list =  SerializeUtil.unserializeList(jedis.get(RuleDomain.REDIS_KEY_RULE_ENTRANCE.getBytes()),RuleEntrance.class);
        }else{
            list = sqlSessionTemplate.selectList("ruleDaoImpl.getRuleEntranceMap");
            //将 数据缓存至redis中

            jedis.set(RuleDomain.REDIS_KEY_RULE_ENTRANCE.getBytes(),SerializeUtil.serializeList(list));
        }

        if (null != list && !list.isEmpty()) {
            RuleEntrance ruleEntrance = null;
            for (int ruleEntranceIndex = 0;ruleEntranceIndex< list.size();ruleEntranceIndex++) {
                ruleEntrance =  list.get(ruleEntranceIndex);
                map.put(String.valueOf(ruleEntranceIndex), ruleEntrance);
            }
        }
        return map;
    }

    /**
     * 初始化业务规则分组和编码映射关联关系信息
     * @return
     * @throws Exception
     */
    @Override
    @Cacheable(key=RuleDomain.REDIS_KEY_RULE_GROUP)
    public List getRuleGroupRelaList() throws Exception {
        List saopRuleGroupRelaList = new ArrayList();

        Jedis jedis = getJedis();

        if(jedis.exists(RuleDomain.REDIS_KEY_RULE_ENTRANCE.getBytes())){
            saopRuleGroupRelaList =  SerializeUtil.unserializeList(jedis.get(RuleDomain.REDIS_KEY_RULE_GROUP.getBytes()),Map.class);
        }else {
            //查询全部业务规则分组信息
            List allSaopRuleGroupInfoList = sqlSessionTemplate.selectList("ruleDaoImpl.querySaopRuleGroupMap");

            //业务分组和编码映射关系集合
            List allsaopRuleGroupRelaInfoList = sqlSessionTemplate.selectList("ruleDaoImpl.querySaopRuleGroupRelaMap");

            int allSaopRuleGroupInfoListSize = allSaopRuleGroupInfoList.size();
            int allsaopRuleGroupRelaInfoListSize = allsaopRuleGroupRelaInfoList.size();

            if (allSaopRuleGroupInfoListSize > 0 && allsaopRuleGroupRelaInfoListSize > 0) {
                for (int i = 0; i < allSaopRuleGroupInfoListSize; i++) {
                    //每个业务规则分组项
                    Map saopRuleGroupMap = (Map) allSaopRuleGroupInfoList.get(i);
                    if (null == saopRuleGroupMap || null == saopRuleGroupMap.get("groupId")) {
                        continue;
                    }

                    //当前规则分组编码
                    String curRuleGroupId = String.valueOf(saopRuleGroupMap.get("groupId"));

                    //当前业务规则分组下的规则编码集合
                    List ruleIdList = new ArrayList();
                    saopRuleGroupMap.put("ruleIdList", ruleIdList);

                    for (int j = 0; j < allsaopRuleGroupRelaInfoListSize; j++) {
                        Map saopRuleGroupRelaMap = (Map) allsaopRuleGroupRelaInfoList.get(j);
                        if (null == saopRuleGroupRelaMap || null == saopRuleGroupRelaMap.get("groupId")) {
                            continue;
                        }

                        //当前规则分组编码
                        String ruleRelaGroupId = String.valueOf(saopRuleGroupRelaMap.get("groupId"));

                        if (curRuleGroupId.equals(ruleRelaGroupId)) {
                            //获取规则编码
                            String ruleId = "";
                            if (null != saopRuleGroupRelaMap.get("rule_id")) {
                                ruleId = String.valueOf(saopRuleGroupRelaMap.get("rule_id"));
                            }

                            if (!StringUtils.isEmpty(ruleId)) {
                                ruleIdList.add(ruleId);
                            }
                        }
                    }
                }

                saopRuleGroupRelaList = allSaopRuleGroupInfoList;

                jedis.set(RuleDomain.REDIS_KEY_RULE_ENTRANCE.getBytes(),SerializeUtil.serializeList(saopRuleGroupRelaList));
            }
        }


        return saopRuleGroupRelaList;
    }

    @Override
    @Cacheable(key=RuleDomain.REDIS_KEY_RULE)
    public Map<String, Rule> getRuleMap() throws Exception {

        Jedis jedis = getJedis();

        List<Rule> ruleList = null;

        Map map = new HashMap();

        if(jedis.exists(RuleDomain.REDIS_KEY_RULE.getBytes())){
            ruleList =  SerializeUtil.unserializeList(jedis.get(RuleDomain.REDIS_KEY_RULE.getBytes()),Rule.class);
        }else{
            ruleList = sqlSessionTemplate.selectList("ruleDaoImpl.queryRule");
            for (Rule rule : ruleList) {
                String ruleId = rule.getRule_id();
                List<RuleCondCfg> ruleCondCfgList = sqlSessionTemplate.selectList("ruleDaoImpl.queryRuleCondCfg", ruleId);
                rule.setRuleCondCfgs(ruleCondCfgList);
            }
            //将 数据缓存至redis中

            jedis.set(RuleDomain.REDIS_KEY_RULE.getBytes(),SerializeUtil.serializeList(ruleList));
        }

        if (null != ruleList && !ruleList.isEmpty()) {
            Rule rule = null;
            for (Iterator i = ruleList.iterator(); i.hasNext();) {
                rule = (Rule) i.next();
                map.put(rule.getRule_id(), rule);
            }
        }

        return map;
    }
}
