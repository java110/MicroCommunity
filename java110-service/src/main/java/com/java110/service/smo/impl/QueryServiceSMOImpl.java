package com.java110.service.smo.impl;

import bsh.Interpreter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.java110.common.cache.ServiceSqlCache;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.BusinessException;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.common.util.StringUtil;
import com.java110.entity.service.DataQuery;
import com.java110.entity.service.ServiceSql;
import com.java110.service.dao.IQueryServiceDAO;
import com.java110.service.smo.IQueryServiceSMO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by wuxw on 2018/4/19.
 */
@Service("queryServiceSMOImpl")
@Transactional
public class QueryServiceSMOImpl extends LoggerEngine implements IQueryServiceSMO {


    private static Logger logger = LoggerFactory.getLogger(QueryServiceSMOImpl.class);


    @Autowired
    private IQueryServiceDAO queryServiceDAOImpl;

    @Override
    public void commonQueryService(DataQuery dataQuery) throws BusinessException {
        //查询缓存查询 对应处理的ServiceSql
        ResponseEntity<String> responseEntity = null;
        try {
            ServiceSql currentServiceSql = ServiceSqlCache.getServiceSql(dataQuery.getServiceCode());
            if (currentServiceSql == null) {
                throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "未提供该服务 serviceCode = " + dataQuery.getServiceCode());
            }
            if ("".equals(currentServiceSql.getQueryModel())) {
                throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "配置服务 serviceCode = " + dataQuery.getServiceCode() + " 错误，未配置QueryModel,请联系管理员");
            }
            //请求参数校验
            List<String> sysParams = currentServiceSql.getParamList();
            for (String param : sysParams) {
                if (!dataQuery.getRequestParams().containsKey(param)) {
                    //2019-04-10 这里修改为不抛出异常而是写为空字符串
                    //throw new BusinessException(ResponseConstant.RESULT_PARAM_ERROR,"请求参数错误，请求报文中未包含参数 " + param + " 信息");
                    dataQuery.getRequestParams().put(param, "");
                }
            }
            dataQuery.setServiceSql(currentServiceSql);
            if (CommonConstant.QUERY_MODEL_SQL.equals(currentServiceSql.getQueryModel())) {
                doExecuteSql(dataQuery);
            } else if (CommonConstant.QUERY_MODE_JAVA.equals(currentServiceSql.getQueryModel())) {
                doExecuteJava(dataQuery);
            } else {
                doExecuteProc(dataQuery);
            }
            responseEntity = new ResponseEntity<String>(dataQuery.getResponseInfo().toJSONString(), HttpStatus.OK);
        } catch (BusinessException e) {
            logger.error("公用查询异常：", e);
            /*dataQuery.setResponseInfo(DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_PARAM_ERROR,
                    e.getMessage()));*/
            responseEntity = new ResponseEntity<String>("请求发生异常，" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            dataQuery.setResponseEntity(responseEntity);
        }


    }

    @Override
    public void commonDoService(DataQuery dataQuery) throws BusinessException {
        //查询缓存查询 对应处理的ServiceSql
        try {
            ServiceSql currentServiceSql = ServiceSqlCache.getServiceSql(dataQuery.getServiceCode());
            if (currentServiceSql == null) {
                throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "未提供该服务 serviceCode = " + dataQuery.getServiceCode());
            }
            if ("".equals(currentServiceSql.getQueryModel())) {
                throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "配置服务 serviceCode = " + dataQuery.getServiceCode() + " 错误，未配置QueryModel,请联系管理员");
            }
            dataQuery.setServiceSql(currentServiceSql);
            if (CommonConstant.QUERY_MODEL_SQL.equals(currentServiceSql.getQueryModel())) {
                doExecuteUpdateSql(dataQuery);
                return;
            } else if (CommonConstant.QUERY_MODE_JAVA.equals(currentServiceSql.getQueryModel())) {
                doExecuteJava(dataQuery);
                return;
            }
            doExecuteUpdateProc(dataQuery);
        } catch (BusinessException e) {
            logger.error("公用查询异常：", e);
            dataQuery.setResponseInfo(DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_PARAM_ERROR,
                    e.getMessage()));
        }

    }

    /**
     * {"PARAM:"{
     * "param1": "$.a.#A#Object",
     * "param2": "$.a.b.A#B#Array",
     * "param3": "$.a.b.c.A.B#C#Array"
     * },"TEMPLATE":"{}"
     * }
     * 执行sql
     *
     * @param dataQuery
     */
    private void doExecuteUpdateSql(DataQuery dataQuery) throws BusinessException {
        JSONObject business = null;
        try {
            JSONObject params = dataQuery.getRequestParams();
            JSONObject sqlObj = JSONObject.parseObject(dataQuery.getServiceSql().getSql());
            JSONObject templateObj = JSONObject.parseObject(dataQuery.getServiceSql().getTemplate());
            business = JSONObject.parseObject(templateObj.getString("TEMPLATE"));
            List<Object> currentParams = new ArrayList<Object>();
            String currentSql = "";
            for (String key : sqlObj.keySet()) {
                currentSql = sqlObj.getString(key);
                String[] sqls = currentSql.split("#");
                String currentSqlNew = "";
                for (int sqlIndex = 0; sqlIndex < sqls.length; sqlIndex++) {
                    if (sqlIndex % 2 == 0) {
                        currentSqlNew += sqls[sqlIndex];
                        continue;
                    }
                    currentSqlNew += "?";
                    currentParams.add(params.get(sqls[sqlIndex]) instanceof Integer ? params.getInteger(sqls[sqlIndex]) : "" + params.getString(sqls[sqlIndex]) + "");
                    //currentSqlNew += params.get(sqls[sqlIndex]) instanceof Integer ? params.getInteger(sqls[sqlIndex]) : "'" + params.getString(sqls[sqlIndex]) + "'";
                }

                int flag = queryServiceDAOImpl.updateSql(currentSqlNew, currentParams.toArray());

                if (flag < 1) {
                    throw new BusinessException(ResponseConstant.RESULT_PARAM_ERROR, "数据交互失败");
                }
            }

        } catch (Exception e) {
            logger.error("数据交互异常：", e);
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "数据交互异常。。。");
        }

        dataQuery.setResponseInfo(DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_SUCCESS,
                "成功", business));
    }

    /**
     * 执行java脚本
     *
     * @param dataQuery
     * @throws BusinessException
     */
    private void doExecuteJava(DataQuery dataQuery) throws BusinessException {
        try {
            JSONObject params = dataQuery.getRequestParams();
            String javaCode = dataQuery.getServiceSql().getJavaScript();

            Interpreter interpreter = new Interpreter();
            interpreter.eval(javaCode);
            String param = "";
            for (String key : params.keySet()) {
                param += (params.getString(key) + ",");
            }

            if (param.endsWith(",")) {
                param = param.substring(0, param.length() - 1);
            }

            dataQuery.setResponseInfo(DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_SUCCESS,
                    "成功", JSONObject.parseObject(interpreter.eval("execute(" + param + ")").toString())));
        } catch (Exception e) {
            logger.error("数据交互异常：", e);
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "数据交互异常。。。");
        }
    }

    /**
     * {"PARAM:"{
     * "param1": "$.a.#A#Object",
     * "param2": "$.a.b.A#B#Array",
     * "param3": "$.a.b.c.A.B#C#Array"
     * },"TEMPLATE":"{}"
     * }
     * 执行sql
     *
     * @param dataQuery
     */
    private void doExecuteSql(DataQuery dataQuery) throws BusinessException {

        JSONObject templateObj = JSONObject.parseObject(dataQuery.getServiceSql().getTemplate());
        JSONObject templateParams = templateObj.getJSONObject("PARAM");
        JSONObject business = JSONObject.parseObject(templateObj.getString("TEMPLATE"));
        String template = "";
        String[] values = null;
        JSONObject currentJsonObj = null;
        JSONArray currentJsonArr = null;
        for (String key : templateParams.keySet()) {
            template = templateParams.getString(key);

            values = judgeResponseTemplate(template);

            Object o = JSONPath.eval(business, values[0]);

            dataQuery.setTemplateKey(key);
            if (o instanceof JSONObject) {
                currentJsonObj = (JSONObject) o;
                doJsonObject(currentJsonObj, dataQuery, values);
            } else if (o instanceof JSONArray) {
                currentJsonArr = (JSONArray) o;
                doJsonArray(currentJsonArr, dataQuery, values);
            } else {
                throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "template 配置 不正确，value 值 和 TEMPLATE 配置不一致");
            }
        }

        dataQuery.setResponseInfo(DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_SUCCESS,
                "成功", business));
    }

    /**
     * 处理 jsonObject
     *
     * @param obj
     * @param dataQuery
     * @param values
     */
    private void doJsonObject(JSONObject obj, DataQuery dataQuery, String[] values) {
        try {
            JSONObject params = dataQuery.getRequestParams();
            JSONObject sqlObj = JSONObject.parseObject(dataQuery.getServiceSql().getSql());
            List<Object> currentParams = new ArrayList<Object>();

            String currentSql = sqlObj.getString(dataQuery.getTemplateKey());
            String[] sqls = currentSql.split("#");
            String currentSqlNew = "";
            for (int sqlIndex = 0; sqlIndex < sqls.length; sqlIndex++) {
                if (sqlIndex % 2 == 0) {
                    currentSqlNew += sqls[sqlIndex];
                    continue;
                }
                if (sqls[sqlIndex].startsWith("PARENT_")) {
                    if (obj.isEmpty()) {
                        currentSqlNew += "?";
                        currentParams.add("''");
                        continue;
                    }
                    for (String key : obj.keySet()) {
                        if (sqls[sqlIndex].substring("PARENT_".length()).equals(key)) {
                            /*currentSqlNew += obj.get(key) instanceof Integer
                                    ? obj.getInteger(key) : "'" + obj.getString(key) + "'";*/
                            currentSqlNew += "?";
                            currentParams.add(obj.get(key) instanceof Integer
                                    ? obj.getInteger(key) : "" + obj.getString(key) + "");
                            continue;
                        }
                    }
                } else {
                    currentSqlNew += "?";
                    Object param = params.getString(sqls[sqlIndex]);
                    if (params.get(sqls[sqlIndex]) instanceof Integer) {
                        param = params.getInteger(sqls[sqlIndex]);
                    }
                    //这里对 page 和 rows 特殊处理 ，目前没有想到其他的办法
                    if (StringUtils.isNumeric(param.toString()) && "page,rows".contains(sqls[sqlIndex])) {
                        param = Integer.parseInt(param.toString());
                    }
                    currentParams.add(param);
                    //currentSqlNew += params.get(sqls[sqlIndex]) instanceof Integer ? params.getInteger(sqls[sqlIndex]) : "'" + params.getString(sqls[sqlIndex]) + "'";
                }
            }

            List<Map<String, Object>> results = queryServiceDAOImpl.executeSql(currentSqlNew, currentParams.toArray());

            if (results == null || results.size() == 0) {
                if (StringUtil.isNullOrNone(values[1])) {
                    return;
                }
                obj.put(values[1], values[2].equals("Object") ? new JSONObject() : new JSONArray());
                return;
            }
            if (values[2].equals("Object")) {
                if (StringUtil.isNullOrNone(values[1])) {
                    obj.putAll(JSONObject.parseObject(JSONObject.toJSONString(results.get(0))));
                    return;
                }
                obj.put(values[1], JSONObject.parseObject(JSONObject.toJSONString(results.get(0))));
            } else if (values[2].equals("Array")) {
                if (StringUtil.isNullOrNone(values[1])) {
                    JSONArray datas = JSONArray.parseArray(JSONArray.toJSONString(results));
                    for (int dataIndex = 0; dataIndex < datas.size(); dataIndex++) {
                        obj.putAll(datas.getJSONObject(dataIndex));
                    }
                    return;
                }
                obj.put(values[1], JSONArray.parseArray(JSONArray.toJSONString(results)));
            }
        } catch (Exception e) {
            logger.error("数据交互异常：", e);
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "数据交互异常。。。");
        }
    }

    /**
     * 处理JSONArray
     *
     * @param objs
     * @param dataQuery
     * @param values
     */
    private void doJsonArray(JSONArray objs, DataQuery dataQuery, String[] values) {

        for (int objIndex = 0; objIndex < objs.size(); objIndex++) {
            doJsonObject(objs.getJSONObject(objIndex), dataQuery, values);
        }

    }

    /**
     * 执行存储
     *
     * @param dataQuery
     */
    private void doExecuteUpdateProc(DataQuery dataQuery) {
        Map info = new TreeMap();
        info.put("procName", dataQuery.getServiceSql().getProc());
        JSONObject params = dataQuery.getRequestParams();
        info.putAll(params);

        String jsonStr = queryServiceDAOImpl.updateProc(info);

        if (!Assert.isJsonObject(jsonStr)) {
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "存储过程 procName = " + dataQuery.getServiceSql().getProc() + " 返回结果不是Json格式");
        }

        dataQuery.setResponseInfo(DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_SUCCESS,
                "成功", JSONObject.parseObject(jsonStr)));
    }


    /**
     * 校验 返回模板
     *
     * @param template
     * @return
     * @throws BusinessException
     */
    private String[] judgeResponseTemplate(String template) throws BusinessException {


        if (!template.startsWith("$.")) {
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "template 配置 不正确，value 必须以$.开头");
        }

        String[] values = template.split("#");

        if (values == null || values.length != 3) {
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "template 配置 不正确，value 必须有两个#号");
        }

        if (StringUtil.isNullOrNone(values[1]) && !"$.##Object".equals(template) && !"$.##Array".equals(template)) {
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "template 配置 不正确，目前只支持 $.##Object 和 $.##Array ");
        }

        return values;
    }


    /**
     * 执行存储
     *
     * @param dataQuery
     */
    private void doExecuteProc(DataQuery dataQuery) {
        Map info = new TreeMap();
        info.put("procName", dataQuery.getServiceSql().getProc());
        JSONObject params = dataQuery.getRequestParams();
        info.putAll(params);

        String jsonStr = queryServiceDAOImpl.executeProc(info);

        if (!Assert.isJsonObject(jsonStr)) {
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "存储过程 procName = " + dataQuery.getServiceSql().getProc() + " 返回结果不是Json格式");
        }

        dataQuery.setResponseInfo(DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_SUCCESS,
                "成功", JSONObject.parseObject(jsonStr)));
    }


    public IQueryServiceDAO getQueryServiceDAOImpl() {
        return queryServiceDAOImpl;
    }

    public void setQueryServiceDAOImpl(IQueryServiceDAO queryServiceDAOImpl) {
        this.queryServiceDAOImpl = queryServiceDAOImpl;
    }
}
