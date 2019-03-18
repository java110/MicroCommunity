package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.cache.MappingCache;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.MappingConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.exception.SMOException;
import com.java110.common.util.DateUtil;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.common.util.StringUtil;
import com.java110.web.smo.IConsoleServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.service.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务服务类
 * Created by wuxw on 2018/4/28.
 */
@Service("consoleServiceSMOImpl")
public class ConsoleServiceSMOImpl extends LoggerEngine implements IConsoleServiceSMO {


    @Autowired
    private RestTemplate restTemplate;
    /**
     * 根据 管理员ID 查询菜单
     * @param manageId
     * @return
     */
    @Override
    public List<Map> getMenuItemsByManageId(String manageId) throws SMOException,IllegalArgumentException{
        Map paramIn = new HashMap();
        paramIn.put("manageId", manageId);
        paramIn.put("menuGroup", CommonConstant.MENU_GROUP_LEFT);
        paramIn.put(CommonConstant.ORDER_USER_ID,manageId);
        paramIn.put(ServiceCodeConstant.SERVICE_CODE,ServiceCodeConstant.SERVICE_CODE_QUERY_MENU_ITEM);
        paramIn.put(ServiceCodeConstant.SERVICE_CODE_NAME,ServiceCodeConstant.SERVICE_CODE_QUERY_MENU_ITEM_NAME);
        JSONObject businessObj = doExecute(paramIn);
        Assert.isNotNull(businessObj,"menus","接口返回错误，未包含menus节点");
        JSONArray menus = businessObj.getJSONArray("menus");
        return menus.toJavaList(Map.class);
    }

    /**
     * 用户登录
     * @param pd
     * @return
     * @throws SMOException
     */
    @Override
    public void login(PageData pd) throws SMOException {
        String userCode = pd.getParam().getString("userCode");
        String userPwd = pd.getParam().getString("userPwd");
        String pageSign = pd.getParam().getString("pageSign");

        Assert.hasText(userCode,"用户编码不能为空");
        Assert.hasText(userPwd,"用户密码不能为空");

        Map paramIn = new HashMap();
        paramIn.put("userCode", userCode);
        paramIn.put(CommonConstant.ORDER_USER_ID,CommonConstant.ORDER_DEFAULT_USER_ID);
        paramIn.put(ServiceCodeConstant.SERVICE_CODE,ServiceCodeConstant.SERVICE_CODE_QUERY_USER_LOGIN);
        //paramIn.put("userPwd", userPwd);
        JSONObject businessObj = doExecute(paramIn);

        Assert.isNotNull(businessObj,"user","查询模板 服务配置错误，返回报文中未包含user节点");

        JSONObject user = businessObj.getJSONObject("user");
        //String newPwd = AuthenticationFactory.md5UserPassword(userPwd);
        if(!AuthenticationFactory.md5UserPassword(userPwd).equals(user.getString("userPwd"))){
            throw new SMOException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"密码不正确");
        }
        String token = "";
        try {
            Map userMap = new HashMap();
            userMap.put(CommonConstant.LOGIN_USER_ID,user.getString("userId"));
            userMap.put(CommonConstant.LOGIN_USER_NAME,user.getString("userName"));
            token = AuthenticationFactory.createAndSaveToken(userMap);
            pd.setToken(token);
        }catch (Exception e){
            logger.error("登录异常：",e);
            throw new SMOException(ResponseConstant.RESULT_CODE_INNER_ERROR,"系统内部错误，请联系管理员");
        }

        //封装成功信息
        pd.setResJson(DataTransactionFactory.pageResponseJson(pd.getTransactionId(),ResponseConstant.RESULT_CODE_SUCCESS,"登录成功 ",null));

    }


    /**
     * 查询模板信息
     * @param pd
     * @throws SMOException
     */
    @Override
    public void getTemplateCol(PageData pd) throws SMOException{
        String templateCode = pd.getParam().getString("templateCode");

        Assert.hasText(templateCode,"模板编码不能为空");

        Map paramIn = new HashMap();
        paramIn.put("templateCode", templateCode);
        paramIn.put(CommonConstant.ORDER_USER_ID,pd.getUserId());
        paramIn.put(ServiceCodeConstant.SERVICE_CODE,ServiceCodeConstant.SERVICE_CODE_QUERY_CONSOLE_TEMPLATE_COL);
        paramIn.put(ServiceCodeConstant.SERVICE_CODE_NAME,ServiceCodeConstant.SERVICE_CODE_QUERY_CONSOLE_TEMPLATE_COL_NAME);
        //paramIn.put("userPwd", userPwd);
        JSONObject businessObj = doExecute(paramIn);

        Assert.isNotNull(businessObj,"template","查询模板 服务配置错误，返回报文中未包含template节点");

        removeButtonName(businessObj);

        JSONObject templateObj = new JSONObject();

        paramColModelToJson(businessObj.getJSONArray("template"));

        templateObj.put("template",businessObj.getJSONArray("template"));

        pd.setResJson(DataTransactionFactory.pageResponseJson(pd.getTransactionId(),ResponseConstant.RESULT_CODE_SUCCESS,"查询成功 ",templateObj));

    }

    /**
     *  colModel 字段处理
     * @param jsonArray
     * @throws SMOException
     */
    private void paramColModelToJson(JSONArray jsonArray) throws SMOException{
        if(jsonArray == null || jsonArray.size() == 0){
            return ;
        }
        try {
            for(int arrIndex = 0; arrIndex < jsonArray.size();arrIndex ++){
                JSONObject tObj = jsonArray.getJSONObject(arrIndex);
                tObj.put("colModel",JSONObject.parse(tObj.getString("colModel")));
            }
        }catch (Exception e){
            logger.error("c_template_col 表 colModel 配置错误",e);
            throw new SMOException(ResponseConstant.RESULT_CODE_CONFIG_ERROR,"colModel 配置错误");
        }
    }

    /**
     * 删除BUTTON 字段 值
     * @param jsonObject
     */
    private void removeButtonName(JSONObject jsonObject){
        JSONArray template = jsonObject.getJSONArray("template");
        for(int colIndex = 0 ; colIndex < template.size() ; colIndex ++){
            if(CommonConstant.TEMPLATE_COLUMN_NAME_BUTTON.equals(template.getJSONObject(colIndex).getString("colName"))){
                template.getJSONObject(colIndex).put("colName","");
            }
        }
    }

    private JSONObject doExecute(Map<String,Object> paramIn) {
        //获取组件
        String appId = MappingCache.getValue(MappingConstant.KEY_CONSOLE_SERVICE_APP_ID);

        Assert.hasLength(appId, "组件不能为空");

        String apiServiceUrl = MappingCache.getValue(MappingConstant.KEY_API_SERVICE_URL);

        Assert.hasLength(apiServiceUrl, "api服务器地址没有配置");

        String securityCode = MappingCache.getValue(MappingConstant.KEY_CONSOLE_SECURITY_CODE);
        Assert.hasLength(securityCode, "签名秘钥没有配置");

        String serviceCode = paramIn.get(ServiceCodeConstant.SERVICE_CODE).toString();
        if(paramIn.containsKey(ServiceCodeConstant.SERVICE_CODE)){
            paramIn.remove(ServiceCodeConstant.SERVICE_CODE);
        }
        if(paramIn.containsKey(ServiceCodeConstant.SERVICE_CODE_NAME)){
            paramIn.remove(ServiceCodeConstant.SERVICE_CODE_NAME);
        }

        if(paramIn.containsKey(CommonConstant.ORDER_USER_ID)){
            paramIn.remove(CommonConstant.ORDER_USER_ID);
        }
        String params = "?";
        for(String key : paramIn.keySet()){
            params += (key +"=" + paramIn.get(key)+"&");
        }
        params = params.lastIndexOf("&") >-1 ? params.substring(0,params.length()-1):params;
        apiServiceUrl = apiServiceUrl.replace("#serviceCode#",serviceCode) + params;
        ResponseEntity<String> responseEntity = null;
        if (MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_CONSOLE_SERVICE_SECURITY_ON_OFF))) {
            try {
//                requestBody = DataTransactionFactory.encrypt(requestBody, 2048);
//                //调用查询菜单信息
//                HttpHeaders header = new HttpHeaders();
//                header.add(CommonConstant.ENCRYPT, MappingConstant.VALUE_ON);
//                header.add(CommonConstant.ENCRYPT_KEY_SIZE, "2048");
//                HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, header);
//                responseMsg = restTemplate.postForObject(centerServiceUrl, httpEntity, String.class);
//                responseMsg = DataTransactionFactory.decrypt(responseMsg, 2048);
            }catch (Exception e){
                logger.error("调用接口失败",e);
                throw new SMOException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"调用接口失败"+e);
            }
        } else {
            HttpHeaders header = new HttpHeaders();
            header.add(CommonConstant.HTTP_APP_ID, appId);
            header.add(CommonConstant.HTTP_TRANSACTION_ID, GenerateCodeFactory.getTransactionId());
            header.add(CommonConstant.HTTP_REQ_TIME, DateUtil.getNowDefault());
            header.add(CommonConstant.HTTP_SIGN, "");
            HttpEntity<String> httpEntity = new HttpEntity<String>(header);
            try {
                responseEntity = restTemplate.exchange(apiServiceUrl, HttpMethod.GET, httpEntity, String.class);
            }catch (HttpClientErrorException e){
                //responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(),e.getStatusCode());

                return null;
            }
        }

        Assert.isJsonObject(responseEntity.getBody(),"下游系统返回报文不是有效的json格式");

        return JSONObject.parseObject(responseEntity.getBody());
    }


    /**
     * 获取模板
     * @param pd
     * @return
     * @throws SMOException
     */
    public void getTemplate(PageData pd) throws SMOException{
        String templateCode = pd.getParam().getString("templateCode");

        Assert.hasText(templateCode,"模板编码不能为空");

        Map paramIn = new HashMap();
        paramIn.put("templateCode", templateCode);
        paramIn.put(CommonConstant.ORDER_USER_ID,pd.getUserId());
        paramIn.put(ServiceCodeConstant.SERVICE_CODE,ServiceCodeConstant.SERVICE_CODE_QUERY_CONSOLE_TEMPLATE);
        paramIn.put(ServiceCodeConstant.SERVICE_CODE_NAME,ServiceCodeConstant.SERVICE_CODE_QUERY_CONSOLE_TEMPLATE_NAME);
        //paramIn.put("userPwd", userPwd);
        JSONObject businessObj = doExecute(paramIn);

        Assert.isNotNull(businessObj,"template","查询模板 服务配置错误，返回报文中未包含template节点");


        JSONObject templateObj = new JSONObject();
        templateObj.put("template",businessObj.getJSONObject("template"));
        pd.setData(templateObj);
        pd.setResJson(DataTransactionFactory.pageResponseJson(pd.getTransactionId(),ResponseConstant.RESULT_CODE_SUCCESS,"查询成功 ",templateObj));

    }

    /**
     * 查询模板数据
     * @param pd
     * @throws SMOException
     */
    public void getTemplateData(PageData pd) throws SMOException{

        //查询模板信息
        getTemplate(pd);
        JSONObject template = pd.getData().getJSONObject("template");
        Assert.isInteger(pd.getParam().getString("rows"),"rows 字段不能为空,并且为整数");
        Assert.isInteger(pd.getParam().getString("page"),"page 字段不能为空,并且为整数");
        int rows = Integer.parseInt(pd.getParam().getString("rows"));
        int page = Integer.parseInt(pd.getParam().getString("page"));
        String sord = pd.getParam().getString("sord");
        String templateUrl = template.getString("templateUrl");
        if(StringUtil.isNullOrNone(templateUrl) || !templateUrl.contains(CommonConstant.TEMPLATE_URL_LIST)){
            throw new SMOException(ResponseConstant.RESULT_CODE_CONFIG_ERROR,"配置错误，模板中为配置查询数据的地址");
        }

        Map paramIn = new HashMap();

        paramIn.put("rows", rows);
        paramIn.put("page", (page-1)*rows);
        paramIn.put("sord", sord);
        paramIn.put("userId", pd.getUserId());
        paramIn.put(CommonConstant.ORDER_USER_ID,pd.getUserId());
        paramIn.put(ServiceCodeConstant.SERVICE_CODE,getServiceCode(templateUrl,CommonConstant.TEMPLATE_URL_LIST));
        paramIn.put(ServiceCodeConstant.SERVICE_CODE_NAME,"数据查询");
        //paramIn.put("userPwd", userPwd);
        JSONObject businessObj = doExecute(paramIn);

        pd.setResJson(businessObj);
    }


    /**
     * 刷新缓存
     * @param pd
     * @throws SMOException
     */
    public void flushCache(PageData pd) throws SMOException{
        //查询单条缓存信息
        queryCacheOne(pd);

        JSONObject cacheObj = pd.getData().getJSONObject("cache");

        String serviceCode = cacheObj.getString("serviceCode");

        String param = cacheObj.getString("param");
        if(!Assert.isJsonObject(param)){
            throw new SMOException(ResponseConstant.RESULT_CODE_CONFIG_ERROR,serviceCode+"缓存配置param错误，不是有效的json格式");
        }
        Map paramIn = new HashMap();
        paramIn.putAll(JSONObject.parseObject(param));
        paramIn.put(CommonConstant.ORDER_USER_ID,pd.getUserId());
        paramIn.put(ServiceCodeConstant.SERVICE_CODE,serviceCode);
        paramIn.put(ServiceCodeConstant.SERVICE_CODE_NAME,"刷新缓存");
        //paramIn.put("userPwd", userPwd);
        JSONObject businessObj = doExecute(paramIn);

        Assert.notEmpty(businessObj,"刷新缓存失败，请联系管理员");

        pd.setResJson(DataTransactionFactory.pageResponseJson(pd.getTransactionId(),ResponseConstant.RESULT_CODE_SUCCESS,"查询成功 ",null));

    }

    /**
     * 查询缓存信息
     * @param pd
     */
    public void queryCacheOne(PageData pd) throws  SMOException{
        String cacheCode = pd.getParam().getString("cacheCode");

        Assert.hasText(cacheCode,"缓存编码不能为空");

        Map paramIn = new HashMap();
        paramIn.put("cacheCode", cacheCode);
        paramIn.put(CommonConstant.ORDER_USER_ID,pd.getUserId());
        paramIn.put(ServiceCodeConstant.SERVICE_CODE,ServiceCodeConstant.SERVICE_CODE_QUERY_CONSOLE_CACHE);
        paramIn.put(ServiceCodeConstant.SERVICE_CODE_NAME,ServiceCodeConstant.SERVICE_CODE_QUERY_CONSOLE_CACHE_NAME);
        //paramIn.put("userPwd", userPwd);
        JSONObject businessObj = doExecute(paramIn);

        Assert.isNotNull(businessObj,"cache","查询单条缓存信息 配置错误，返回报文中未包含cache节点");


        JSONObject templateObj = new JSONObject();
        templateObj.put("cache",businessObj.getJSONObject("cache"));
        pd.setData(templateObj);
        pd.setResJson(DataTransactionFactory.pageResponseJson(pd.getTransactionId(),ResponseConstant.RESULT_CODE_SUCCESS,"查询成功 ",templateObj));

    }

    /**
     * 编辑模板数据
     * @param pd
     * @throws SMOException
     */
    public void editTemplateData(PageData pd) throws SMOException{

        //查询模板信息
        getTemplate(pd);
        JSONObject template = pd.getData().getJSONObject("template");
        String oper = pd.getParam().getString("oper");
        Assert.hasLength(oper,"oper 字段不能为空");
        String templateUrl = template.getString("templateUrl");
        if(StringUtil.isNullOrNone(templateUrl)){
            throw new SMOException(ResponseConstant.RESULT_CODE_CONFIG_ERROR,"配置错误，模板中url 没有被配置");
        }
        String serviceCode = "";
        if(CommonConstant.TEMPLATE_OPER_ADD.equals(oper)){
            serviceCode = CommonConstant.TEMPLATE_URL_INSERT;
        }else if(CommonConstant.TEMPLATE_OPER_EDIT.equals(oper)){
            serviceCode = CommonConstant.TEMPLATE_URL_UPDATE;
        }else if(CommonConstant.TEMPLATE_OPER_DEL.equals(oper)){
            serviceCode = CommonConstant.TEMPLATE_URL_DELETE;
        }else{
            throw new SMOException(ResponseConstant.RESULT_PARAM_ERROR,"入参错误oper 只能为 add edit del 中的一种");
        }
        Map paramIn = new HashMap();
        paramIn.putAll(pd.getParam());
        paramIn.put("userId", pd.getUserId());
        paramIn.put(CommonConstant.ORDER_USER_ID,pd.getUserId());
        paramIn.put(ServiceCodeConstant.SERVICE_CODE,getServiceCode(templateUrl,serviceCode));
        paramIn.put(ServiceCodeConstant.SERVICE_CODE_NAME,"数据操作");
        //paramIn.put("userPwd", userPwd);
        JSONObject businessObj = doExecute(paramIn);

        pd.setResJson(businessObj);
    }

    /**
     * 获取serviceCode
     * @param templateUrl
     * @param model
     * @return
     * @throws SMOException
     */
    private String getServiceCode(String templateUrl,String model) throws SMOException{
        String [] tUrls = templateUrl.split(CommonConstant.TEMPLATE_URL_SPILT);
        for(String url : tUrls){
            if(url.contains(model)){
                return url.substring(model.length());
            }
        }

        throw new SMOException(ResponseConstant.RESULT_CODE_CONFIG_ERROR,"配置错误，模板中为配置["+model+"]数据的地址");
    }



    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
