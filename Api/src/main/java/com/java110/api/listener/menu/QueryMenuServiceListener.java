package com.java110.api.listener.menu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.DataFlowFactory;
import com.java110.entity.center.AppService;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import org.springframework.http.*;

import java.util.Map;

/**
 * 根据用户ID查询菜单信息
 * Created by Administrator on 2019/4/1.
 */
@Java110Listener("queryMenuServiceListener")
public class QueryMenuServiceListener extends AbstractServiceApiDataFlowListener {
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_MENU_INFO;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    /**
     * 业务处理
     *
     * @param event
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        //get 方式下 请求参数会转化到header 中
        Map<String, String> requestHeaders = dataFlowContext.getRequestHeaders();
        Assert.hasKey(requestHeaders, "userId", "请求信息中未包含userId信息");

        String userId = requestHeaders.get("userId");
        //根据用户查询 商户类
        String domain = queryStoreTypeCd(dataFlowContext, userId);
        domain = StringUtil.isEmpty(domain) ? "-1" : domain;
        ResponseEntity responseEntity = null;
        String requestUrl = service.getUrl() + "?userId=" + userId + "&domain=" + domain;
        dataFlowContext.getRequestHeaders().put("REQUEST_URL", requestUrl);
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(), ServiceCodeConstant.SERVICE_CODE_QUERY_MENU_INFO);
        HttpEntity<String> httpEntity = new HttpEntity<String>("", header);
        doRequest(dataFlowContext, service, httpEntity);
        responseEntity = dataFlowContext.getResponseEntity();
        //如果调用后端失败了 则直接返回
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return;
        }

        JSONObject resultObj = JSONObject.parseObject(responseEntity.getBody().toString());

        if (!resultObj.containsKey("menus")) {
            return;
        }

        dataFlowContext.setResponseEntity(refreshMenusInfo(resultObj.getJSONArray("menus")));

    }

    /**
     * 根据用户ID查询商户信息
     *
     * @param dataFlowContext
     * @param userId
     * @return
     */
    private String queryStoreTypeCd(DataFlowContext dataFlowContext, String userId) {
        ResponseEntity responseEntity = null;
        AppService appService = DataFlowFactory.getService(dataFlowContext.getAppId(), ServiceCodeConstant.SERVICE_CODE_QUERY_STORE_BYUSER);
        if (appService == null) {
            responseEntity = new ResponseEntity<String>("当前没有权限访问" + ServiceCodeConstant.SERVICE_CODE_QUERY_USER_LOGIN, HttpStatus.UNAUTHORIZED);
            dataFlowContext.setResponseEntity(responseEntity);
            return "";
        }
        String requestUrl = appService.getUrl() + "?userId=" + userId;
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(), ServiceCodeConstant.SERVICE_CODE_QUERY_STORE_BYUSER);
        HttpEntity<String> httpEntity = new HttpEntity<String>("", header);
        doRequest(dataFlowContext, appService, httpEntity);
        responseEntity = dataFlowContext.getResponseEntity();

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            dataFlowContext.setResponseEntity(responseEntity);
        }

        return JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeTypeCd");
    }

    /**
     * 刷新菜单信息
     * 将 数据 [{
     * "gId": "800201904001",
     * "menuDescription": "添加员工",
     * "menuGroupSeq": 1,
     * "menuSeq": 1,
     * "icon": "fa-desktop",
     * "mId": "700201904001",
     * "menuName": "添加员工",
     * "pId": "500201904001",
     * "menuGroupName": "员工管理",
     * "label": "",
     * "menuGroupDescription": "员工管理",
     * "url": "/"
     * }],
     * 转为：
     * "[{'id':1,'icon':'fa-desktop','name':'我的菜单','label':'HOT','childs':[" +
     * "{'name':'子菜单','href':'http://www.baidu.com'}]}," +
     * "{'id':2,'icon':'fa-flask','name':'我的菜单','childs':[],'href':'/doc'}," +
     * "{'id':3,'icon':'fa-globe','name':'我的菜单','childs':[{'name':'子菜单','href':'http://www.baidu.com'}]}" +
     * "]";
     *
     * @param menusList 菜单列表
     * @return
     */
    private ResponseEntity<String> refreshMenusInfo(JSONArray menusList) {
        JSONArray tempMenus = new JSONArray();
        JSONObject tempMenu = null;
        for (int menuIndex = 0; menuIndex < menusList.size(); menuIndex++) {
            JSONObject tMenu = menusList.getJSONObject(menuIndex);
            tempMenu = this.getMenuFromMenus(tempMenus, tMenu.getString("gId"));
            if (tempMenu == null) {
                tempMenu = new JSONObject();
                tempMenu.put("id", tMenu.getString("gId"));
                tempMenu.put("icon", tMenu.getString("icon"));
                tempMenu.put("name", tMenu.getString("menuGroupName"));
                tempMenu.put("label", tMenu.getString("label"));
                tempMenu.put("seq", tMenu.getString("menuGroupSeq"));
                tempMenu.put("childs", new JSONArray());
                tempMenus.add(tempMenu);
            }
            //获取孩子菜单
            JSONArray childs = tempMenu.getJSONArray("childs");
            JSONObject childMenu = new JSONObject();
            childMenu.put("name", tMenu.getString("menuName"));
            childMenu.put("href", tMenu.getString("url"));
            childMenu.put("seq", tMenu.getString("menuSeq"));
            childMenu.put("isShow", tMenu.getString("isShow"));
            childs.add(childMenu);
        }
        return new ResponseEntity<String>(tempMenus.toJSONString(), HttpStatus.OK);
    }

    /**
     * 在菜单列表查询菜单
     *
     * @param gId
     * @return
     */
    private JSONObject getMenuFromMenus(JSONArray tempMenus, String gId) {
        for (int tempIndex = 0; tempIndex < tempMenus.size(); tempIndex++) {
            if (tempMenus.getJSONObject(tempIndex).getString("id").equals(gId)) {
                return tempMenus.getJSONObject(tempIndex);
            }
        }

        return null;
    }
}
