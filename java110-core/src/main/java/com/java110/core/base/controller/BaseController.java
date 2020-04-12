package com.java110.core.base.controller;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.NoAuthorityException;

import com.java110.utils.util.StringUtil;
import com.java110.core.base.AppBase;
import com.java110.core.context.BusinessServiceDataFlow;
import com.java110.core.factory.DataFlowFactory;
import com.java110.core.context.PageData;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 所有控制类的父类，统一参数处理
 * 或公用逻辑处理
 * Created by wuxw on 2017/2/23.
 */
public class BaseController extends AppBase {


    /**
     * 检查用户登录
     *
     * @throws NoAuthorityException
     */
    protected void checkLogin(PageData pd) throws NoAuthorityException {
        if (StringUtil.isNullOrNone(pd.getUserId())) {
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "用户未登录，请登录！");
        }
    }


    /**
     * 将url参数写到header map中
     *
     * @param request
     */
    protected void initUrlParam(HttpServletRequest request, Map headers) {
        /*put real ip address*/

        Map readOnlyMap = request.getParameterMap();

        StringBuffer queryString = new StringBuffer(request.getRequestURL() != null ? request.getRequestURL() : "");

        if (readOnlyMap != null && !readOnlyMap.isEmpty()) {
            queryString.append("?");
            Set<String> keys = readOnlyMap.keySet();
            for (Iterator it = keys.iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                String[] value = (String[]) readOnlyMap.get(key);
//                String[] value = (String[]) readOnlyMap.get(key);
                if (value.length > 1) {
                    headers.put(key, value[0]);
                    for (int j = 0; j < value.length; j++) {
                        queryString.append(key);
                        queryString.append("=");
                        queryString.append(value[j]);
                        queryString.append("&");
                    }

                } else {
                    headers.put(key, value[0]);
                    queryString.append(key);
                    queryString.append("=");
                    queryString.append(value[0]);
                    queryString.append("&");
                }
            }
        }

        /*put requst url*/
        if (readOnlyMap != null && !readOnlyMap.isEmpty()) {
            headers.put("REQUEST_URL", queryString.toString().substring(0, queryString.toString().length() - 1));
        } else {
            headers.put("REQUEST_URL", queryString.toString());
        }

    }

    public static Map<String, String> getParameterStringMap(HttpServletRequest request) {
        Map<String, String[]> properties = request.getParameterMap();//把请求参数封装到Map<String, String[]>中
        Map<String, String> returnMap = new HashMap<String, String>();
        String name = "";
        String value = "";
        for (Map.Entry<String, String[]> entry : properties.entrySet()) {
            name = entry.getKey();
            String[] values = entry.getValue();
            if (null == values) {
                value = "";
            } else if (values.length > 1) {
                for (int i = 0; i < values.length; i++) { //用于请求参数中有多个相同名称
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = values[0];//用于请求参数中请求参数名唯一
            }
            returnMap.put(name, value);

        }
        return returnMap;
    }

    protected void initHeadParam(HttpServletRequest request, Map headers) {

        Enumeration reqHeaderEnum = request.getHeaderNames();

        while (reqHeaderEnum.hasMoreElements()) {
            String headerName = (String) reqHeaderEnum.nextElement();
            headers.put(headerName.toLowerCase(), request.getHeader(headerName));
        }

        headers.put("IP", getIpAddr(request));

        headers.put("hostName", request.getLocalName());
        headers.put("port", request.getLocalPort() + "");

        if(headers.containsKey("app-id")){
            headers.put("app_id",headers.get("app-id"));
        }

    }

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     */
    protected String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * 创建 PageData 对象
     *
     * @param request
     * @return
     * @throws IllegalArgumentException
     */
    protected PageData getPageData(HttpServletRequest request) {
        if (request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA) == null) {
            throw new IllegalArgumentException("请求参数错误");
        }
        return (PageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
    }

    /**
     * 查询菜单
     *
     * @param model
     * @param pd
     */
    protected void getMenus(Model model, PageData pd, List<Map> menuItems) {
        List<Map> removeMenuItems = new ArrayList<Map>();
        for (Map menuItem : menuItems) {
            if (!"-1".equals(menuItem.get("parentId")) && !"1".equals(menuItem.get("level"))) {
                Map parentMenuItem = this.getMenuItemFromList(menuItems, menuItem.get("parentId").toString());
                if (parentMenuItem == null) {
                    continue;
                }
                if (parentMenuItem.containsKey("subMenus")) {
                    List<Map> subMenus = (List<Map>) parentMenuItem.get("subMenus");
                    subMenus.add(menuItem);
                } else {
                    List<Map> subMenus = new ArrayList<Map>();
                    subMenus.add(menuItem);
                    parentMenuItem.put("subMenus", subMenus);
                }

                //removeMenuItems.add(menuItem);
            }
        }


        //bug 20180510 如果在一级菜单下面没有挂二级菜单报错问题处理
        ifNoSubMenusToRemove(menuItems, removeMenuItems);

        removeMap(menuItems, removeMenuItems);


        model.addAttribute("menus", menuItems);
    }


    private Map getMenuItemFromList(List<Map> menuItems, String parentId) {
        for (Map menuItem : menuItems) {
            if (menuItem.get("mId").toString().equals(parentId)) {
                return menuItem;
            }
        }
        return null;
    }

    /**
     * 删除map
     *
     * @param menuItems
     * @param removeMenuItems
     */
    private void removeMap(List<Map> menuItems, List<Map> removeMenuItems) {
        if (removeMenuItems == null || removeMenuItems.size() == 0) {
            return;
        }

        for (Map removeMenuItem : removeMenuItems) {
            menuItems.remove(removeMenuItem);
        }
    }

    private void ifNoSubMenusToRemove(List<Map> menuItems, List<Map> removeMenuItems) {
        for (Map menu : menuItems) {
            if (!menu.containsKey("subMenus")) {
                removeMenuItems.add(menu);
            }
        }
    }

    /**
     * 封装数据
     *
     * @param reqJson
     * @param headers
     * @return
     * @throws Exception
     */
    protected BusinessServiceDataFlow writeDataToDataFlowContext(String reqJson, Map<String, String> headers) throws Exception {
        BusinessServiceDataFlow businessServiceDataFlow = DataFlowFactory.newInstance(BusinessServiceDataFlow.class).builder(reqJson, headers);
        return businessServiceDataFlow;
    }

    protected void hasPrivilege(RestTemplate restTemplate, IPageData pd, String resource) {
        ResponseEntity<String> responseEntity = null;
        //没有用户的情况下不做权限判断
        if (StringUtil.isEmpty(pd.getUserId())) {
            return;
        }
        JSONObject paramIn = new JSONObject();
        paramIn.put("resource", resource);
        paramIn.put("userId", pd.getUserId());
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/basePrivilege.CheckUserHasResourceListener",
                HttpMethod.POST);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new UnsupportedOperationException("用户没有权限操作");
        }
    }

}
