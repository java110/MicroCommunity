package com.java110.db.dao.impl;

import com.java110.core.base.dao.BaseServiceDao;
import com.java110.db.dao.IQueryServiceDAO;
import com.java110.entity.order.ServiceBusiness;
import com.java110.entity.service.ServiceSql;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by wuxw on 2018/4/20.
 */
@Service("queryServiceDAOImpl")
@Transactional
public class QueryServiceDAOImpl extends BaseServiceDao implements IQueryServiceDAO {

    private final static Logger logger = LoggerFactory.getLogger(QueryServiceDAOImpl.class);

    /**
     * 防止sql注入 改造成直接用prepareStatement 预处理sql
     *
     * @param sql
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> executeSql(String sql, Object[] params) {
        return executeSql(sql, params, null);
    }

    /**
     * 防止sql注入 改造成直接用prepareStatement 预处理sql
     *
     * @param sql
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> executeSql(String sql, Object[] params, List<String> columns) {
        logger.debug("----【queryServiceDAOImpl.executeSql】入参 : " + sql + " params= " + params);
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        try {
            conn = sqlSessionTemplate.getConnection();
            ps = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            rs = ps.executeQuery();
            //精髓的地方就在这里，类ResultSet有getMetaData()会返回数据的列和对应的值的信息，然后我们将列名和对应的值作为map的键值存入map对象之中...
            ResultSetMetaData rsmd = rs.getMetaData();
            if (columns != null) {
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    columns.add(rsmd.getColumnLabel(i+1));
                }
            }
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    String col_name = rsmd.getColumnLabel(i + 1);
                    Object col_value = rs.getObject(col_name);
                    if (col_value == null) {
                        col_value = "";
                    }
                    map.put(col_name, col_value);
                }
                mapList.add(map);
            }
            return mapList;
        } catch (SQLException e) {
            logger.error("执行sql异常：" + sql + params, e);
            return null;
        } finally {
            try {
                //conn.close();
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //return sqlSessionTemplate.selectList("queryServiceDAOImpl.executeSql",sql);
    }

    /**
     * 防止sql注入 改造成直接用prepareStatement 预处理sql
     *
     * @param sql
     * @param params
     * @return
     */
    public int updateSql(String sql, Object[] params) {
        logger.debug("----【queryServiceDAOImpl.updateSql】入参 : " + sql + " params= " + params);
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlSessionTemplate.getConnection();
            ps = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            return ps.executeUpdate();
            //精髓的地方就在这里，类ResultSet有getMetaData()会返回数据的列和对应的值的信息，然后我们将列名和对应的值作为map的键值存入map对象之中...
        } catch (SQLException e) {
            logger.error("执行sql异常：" + sql + params, e);
            return 0;
        } finally {
            try {
                //conn.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //return sqlSessionTemplate.update("queryServiceDAOImpl.updateSql",sql);
    }

    @Override
    public String executeProc(Map<String, Object> paramsInfo) {
        String paramsInfoStr = "";
        for (String key : paramsInfo.keySet()) {
            if ("procName".equals(key)) {
                paramsInfoStr += (paramsInfo.get("procName") + "(");
            } else {
                if (StringUtil.isNullOrNone(paramsInfo.get(key))) {
                    paramsInfoStr += "'',";
                } else {
                    paramsInfoStr += "'" + paramsInfo.get(key) + "',";
                }
            }
        }

        paramsInfo.put("paramsInfo", paramsInfoStr);

        sqlSessionTemplate.selectOne("queryServiceDAOImpl.executeProc", paramsInfo);

        return paramsInfo.get("resMsg") == null ? "" : paramsInfo.get("resMsg").toString();
    }

    @Override
    public String updateProc(Map<String, Object> paramsInfo) {
        String paramsInfoStr = "";
        for (String key : paramsInfo.keySet()) {
            if ("procName".equals(key)) {
                paramsInfoStr += (paramsInfo.get("procName") + "(");
            } else {
                if (StringUtil.isNullOrNone(paramsInfo.get(key))) {
                    paramsInfoStr += "'',";
                } else {
                    paramsInfoStr += "'" + paramsInfo.get(key) + "',";
                }
            }
        }

        paramsInfo.put("paramsInfo", paramsInfoStr);

        sqlSessionTemplate.update("queryServiceDAOImpl.updateProc", paramsInfo);

        return paramsInfo.get("resMsg") == null ? "" : paramsInfo.get("resMsg").toString();
    }

    /**
     * 查询 ServiceSql
     *
     * @return
     */
    @Override
    public List<ServiceSql> qureyServiceSqlAll() {
        return sqlSessionTemplate.selectList("queryServiceDAOImpl.qureyServiceSqlAll");
    }


    /**
     * 查询服务业务信息
     *
     * @return
     */
    public List<ServiceBusiness> qureyServiceBusiness() {
        return sqlSessionTemplate.selectList("queryServiceDAOImpl.queryServiceBusiness");
    }
}
