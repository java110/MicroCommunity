package com.java110.service.dao.impl;

import com.java110.common.util.StringUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.service.ServiceSql;
import com.java110.service.dao.IQueryServiceDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by wuxw on 2018/4/20.
 */
@Service("queryServiceDAOImpl")
@Transactional
public class QueryServiceDAOImpl extends BaseServiceDao implements IQueryServiceDAO {
    @Override
    public List<Map> executeSql(String sql) {
        logger.debug("----【queryServiceDAOImpl.executeSql】入参 : "+sql);
        return sqlSessionTemplate.selectList("queryServiceDAOImpl.executeSql",sql);
    }

    @Override
    public String executeProc(Map<String,Object> paramsInfo) {
        String paramsInfoStr = "";
        for (String key : paramsInfo.keySet()){
            if("procName".equals(key)){
                paramsInfoStr += (paramsInfo.get("procName") + "(");
            }else{
                if(StringUtil.isNullOrNone(paramsInfo.get(key))){
                    paramsInfoStr += "'',";
                }else{
                    paramsInfoStr += "'"+paramsInfo.get(key)+"',";
                }
            }
        }

        paramsInfo.put("paramsInfo",paramsInfoStr);

        sqlSessionTemplate.selectOne("queryServiceDAOImpl.executeProc",paramsInfo);

        return paramsInfo.get("resMsg") ==null ?"" :paramsInfo.get("resMsg").toString();
    }

    /**
     * 查询 ServiceSql
     * @return
     */
    @Override
    public List<ServiceSql> qureyServiceSqlAll() {
        return sqlSessionTemplate.selectList("queryServiceDAOImpl.qureyServiceSqlAll");
    }
}
