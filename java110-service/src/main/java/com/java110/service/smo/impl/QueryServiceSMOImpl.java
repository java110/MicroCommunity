package com.java110.service.smo.impl;

import bsh.Interpreter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.parser.Feature;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.db.dao.IQueryServiceDAO;
import com.java110.dto.logSystemError.LogSystemErrorDto;
import com.java110.entity.service.ServiceSql;
import com.java110.po.logSystemError.LogSystemErrorPo;
import com.java110.service.context.DataQuery;
import com.java110.service.smo.IQueryServiceSMO;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.cache.ServiceSqlCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.BusinessException;
import com.java110.utils.log.LoggerEngine;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ExceptionUtil;
import com.java110.utils.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.ibatis.ognl.OgnlException;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
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

    @Autowired(required = false)
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

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

    @Override
    public ResponseEntity<String> fallBack(String fallBackInfo) throws BusinessException {

        JSONArray params = JSONArray.parseArray(fallBackInfo);
        String sql = "";
        for (int paramIndex = 0; paramIndex < params.size(); paramIndex++) {
            try {
                JSONObject param = params.getJSONObject(paramIndex);
                sql =  param.getString("fallBackSql");
                if (StringUtil.isEmpty(sql)) {
                    return new ResponseEntity<String>("未包含sql信息", HttpStatus.BAD_REQUEST);
                }
                int flag = queryServiceDAOImpl.updateSql(sql, null);
            }catch (Exception e){
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_JOB);
                logSystemErrorPo.setMsg(sql+ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
            }

        }
        return new ResponseEntity<String>("回退成功", HttpStatus.OK);

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
            //JSONObject params = dataQuery.getRequestParams();
            String javaCode = dataQuery.getServiceSql().getJavaScript();

            Interpreter interpreter = new Interpreter();
            interpreter.eval(javaCode);
            interpreter.set("dataQuery", dataQuery);
            dataQuery.setResponseInfo(DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_SUCCESS,
                    "成功", JSONObject.parseObject(interpreter.eval("execute(dataQuery)").toString())));
        } catch (Exception e) {
            logger.error("数据交互异常：", e);
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "数据交互异常," + e.getMessage());
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
            //处理 if 判断
            logger.debug("dealSqlIf开始处理sql中的<if>节点 " + currentSql + " 入参:" + params.toJSONString());
            currentSql = dealSqlIf(currentSql, params);
            logger.debug("dealSqlIf处理完成sql中的<if>节点 " + currentSql + " 入参:" + params.toJSONString());


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
                    if (StringUtils.isNumeric(param.toString()) && "page,rows,row".contains(sqls[sqlIndex])) {
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
     * 执行java脚本
     *
     * @param javaCode
     * @throws BusinessException
     */
    public JSONObject execJava(JSONObject params, String javaCode) throws BusinessException {
        try {
            //JSONObject params = dataQuery.getRequestParams();
            List<String> columns = new ArrayList<>();
            Interpreter interpreter = new Interpreter();
            interpreter.eval(javaCode);
            interpreter.set("params", params);
            interpreter.set("queryServiceDAOImpl",queryServiceDAOImpl);
            JSONObject results = JSONObject.parseObject(interpreter.eval("execute(params,queryServiceDAOImpl)").toString(), Feature.OrderedField);

            JSONArray data = null;
            if (results == null || results.size() < 1) {
                data = new JSONArray();
            } else {
                data = results.getJSONArray("data");
            }

            JSONArray th = new JSONArray();
            if(data.size()>0) {
                for (String key : data.getJSONObject(0).keySet()) {
                    th.add(key);
                }
            }
            JSONObject paramOut = new JSONObject();
            paramOut.put("th", th);
            paramOut.put("td", data);
            paramOut.put("total",results.getString("total"));

            return paramOut;
        } catch (Exception e) {
            logger.error("数据交互异常：", e);
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "数据交互异常," + e.getMessage());
        }
    }

    @Override
    public JSONObject execQuerySql(JSONObject params, String currentSql) throws BusinessException {
        List<Map<String, Object>> results = null;
        List<String> columns = new ArrayList<>();
        try {
            List<Object> currentParams = new ArrayList<Object>();
            //处理 if 判断
            logger.debug("dealSqlIf开始处理sql中的<if>节点 " + currentSql + " 入参:" + params.toJSONString());
            currentSql = dealSqlIf(currentSql, params);
            logger.debug("dealSqlIf处理完成sql中的<if>节点 " + currentSql + " 入参:" + params.toJSONString());
            String[] sqls = currentSql.split("#");
            String currentSqlNew = "";
            for (int sqlIndex = 0; sqlIndex < sqls.length; sqlIndex++) {
                if (sqlIndex % 2 == 0) {
                    currentSqlNew += sqls[sqlIndex];
                    continue;
                }
                currentSqlNew += "?";
                Object param = params.getString(sqls[sqlIndex]);
                if (params.get(sqls[sqlIndex]) instanceof Integer) {
                    param = params.getInteger(sqls[sqlIndex]);
                }
                //这里对 page 和 rows 特殊处理 ，目前没有想到其他的办法
                if (StringUtils.isNumeric(param.toString()) && "page,rows,row".contains(sqls[sqlIndex])) {
                    param = Integer.parseInt(param.toString());
                }
                currentParams.add(param);
            }
            results = queryServiceDAOImpl.executeSql(currentSqlNew, currentParams.toArray(), columns);
        } catch (Exception e) {
            logger.error("解析sql 异常"+currentSql, e);
            throw new BusinessException("1999", e.getLocalizedMessage());
        }
        JSONArray data = null;
        if (results == null || results.size() < 1) {
            data = new JSONArray();
        } else {
            data = JSONArray.parseArray(JSONArray.toJSONString(results));
        }
        JSONArray th = null;
        if (columns.size() < 1) {
            th = new JSONArray();
        } else {
            th = JSONArray.parseArray(JSONArray.toJSONString(columns));
        }


        JSONObject paramOut = new JSONObject();
        paramOut.put("th", th);
        paramOut.put("td", data);
        return paramOut;
    }

    /**
     * 处理SQL语句
     *
     * @param oldSql select * from s_a a
     *               where <if test="name != null && name != ''">
     *               a.name = #name#
     *               </if>
     * @return
     */
    public String dealSqlIf(String oldSql, JSONObject requestParams) throws DocumentException, OgnlException {
        StringBuffer newSql = new StringBuffer();
        String tmpSql = "";
        Boolean conditionResult = false;
        // 未包含 条件语句
        if (!oldSql.contains("<if")) {
            return oldSql;
        }
        String[] oSqls = oldSql.split("</if>");
        for (String oSql : oSqls) {
            logger.debug("处理if 节点，当前处理的oSql=" + oSql + "总的oSqls = " + oSqls);
            if (StringUtil.isNullOrNone(oSql) || !oSql.contains("<if")) {
                newSql.append(oSql);
                continue;
            }
            if (!oSql.startsWith("<if")) {
                newSql.append(oSql.substring(0, oSql.indexOf("<if")));
            }

            tmpSql = oSql.substring(oSql.indexOf("<if")) + "</if>";

            Element root = DocumentHelper.parseText(tmpSql).getRootElement();

            String condition = root.attribute("test").getValue();

            Object condObj = Ognl.parseExpression(condition);

            OgnlContext context = new OgnlContext(null,null,new DefaultMemberAccess(true));

            Object value = Ognl.getValue(condObj,context, requestParams);

            if (value instanceof Boolean) {
                conditionResult = (Boolean) value;
            } else {
                throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "配置错误，if语句配置错误 " + condition);
            }

            if (conditionResult) {
                newSql.append(root.getText());
            }


        }


        return newSql.toString().replace("&gt;", ">").replace("&lt;", "<");

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
