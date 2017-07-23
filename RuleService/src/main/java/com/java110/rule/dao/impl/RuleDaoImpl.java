package com.java110.rule.dao.impl;

import com.java110.core.base.dao.BaseServiceDao;
import com.java110.rule.dao.IRuleDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuxw on 2017/7/23.
 */

@Service("ruleDaoImpl")
@Transactional
public class RuleDaoImpl extends BaseServiceDao implements IRuleDao {
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
}
