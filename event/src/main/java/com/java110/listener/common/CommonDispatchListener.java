package com.java110.listener.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.util.Assert;
import com.java110.common.util.ProtocolUtil;
import com.java110.common.util.StringUtil;
import com.java110.core.context.AppContext;
import com.java110.entity.order.BusiOrder;
import com.java110.event.AppListener;
import com.java110.event.common.AppCommonEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 公用调度侦听
 *
 *
 * Created by wuxw on 2017/9/15.
 */
@Component
public class CommonDispatchListener implements AppListener<AppCommonEvent> {


    /**
     * 保存服务信息，一般启动时加载
     */
    private final static Map<String,String> services = new HashMap<String,String>();

    private final static String QUEYR_DATA = "1";//查询数据，完整数据

    private final static String QUERY_NEED_DELETE_DATA = "2"; //查询需要作废的数据报文


    /**
     * 添加服务，一般启动时加载，不会有并发问题
     * @param boActionType
     * @param service
     */
    public static void addService(String boActionType,String service){
        services.put(boActionType,service);
    }

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 数据受理
     * @param event the event to respond to
     */
    @Override
    public void soDataService(AppCommonEvent event) {
        AppContext context = event.getContext();
        JSONArray dataInfos = event.getData();

        JSONObject infoJson = new JSONObject();
        infoJson.put("data",dataInfos.toJSONString());


        String bo_action_type  = context.getBo_action_type();

        String service_url = services.get(bo_action_type);

        if(StringUtil.isEmpty(service_url) || !service_url.contains("@@")){
            throw new IllegalArgumentException("服务配置错误，["+bo_action_type+"]不存在 ,配置格式为 A::B@@C@@D");
        }

        String returnObj = restTemplate.postForObject(service_url.split("@@")[0],null,String.class,infoJson);

        JSONObject returnObjTmp = JSONObject.parseObject(returnObj);

        Assert.notNull(returnObjTmp,"用户服务没有相应，请检查服务是否正常，请求报文："+returnObj);
        //受理不成功
        if(!returnObjTmp.containsKey(ProtocolUtil.RESULT_CODE)
                || !ProtocolUtil.RETURN_MSG_SUCCESS.equals(returnObjTmp.getString(ProtocolUtil.RESULT_CODE))){
            throw new IllegalArgumentException(service_url+"受理失败，失败原因：" + (returnObjTmp.containsKey(ProtocolUtil.RESULT_MSG)
                    ?"未知原因":returnObjTmp.getString(ProtocolUtil.RESULT_MSG)) + "请求报文："+returnObj);
        }
    }

    @Override
    public JSONObject queryDataInfo(AppCommonEvent event) {
        return queryCommonDataInfo(event,QUEYR_DATA);
    }

    @Override
    public JSONObject queryNeedDeleteDataInfo(AppCommonEvent event){
        return queryCommonDataInfo(event,QUERY_NEED_DELETE_DATA);
    }

    /**
     * 公用查询
     * @param event
     * @param queryFlag
     * @return
     * @throws Exception
     */
    private JSONObject queryCommonDataInfo(AppCommonEvent event,String queryFlag) {

        AppContext context = event.getContext();

        BusiOrder busiOrder = (BusiOrder) context.getReqObj();

        String bo_action_type  = context.getBo_action_type();

        String service_url = services.get(bo_action_type);

        if(StringUtil.isEmpty(service_url) || !service_url.contains("@@")){
            throw new IllegalArgumentException("服务配置错误，["+bo_action_type+"]不存在 ,配置格式为 A::B@@C@@D");
        }

        String[] urls = service_url.split("@@");

        if(urls.length != 3){
            throw new IllegalArgumentException("服务配置错误，["+bo_action_type+"] 配置为"+service_url+"，应该配置格式为 A::B@@C@@D");
        }

        service_url = QUEYR_DATA.equals(queryFlag) ? urls[1] : urls[2];

        String returnObj = restTemplate.postForObject(service_url,null,String.class,JSONObject.toJSONString(busiOrder));

        JSONObject returnObjTmp = JSONObject.parseObject(returnObj);

        Assert.notNull(returnObjTmp,"用户服务没有相应，请检查服务是否正常，请求报文："+returnObjTmp);
        //受理不成功
        if(!returnObjTmp.containsKey(ProtocolUtil.RESULT_CODE)
                || !ProtocolUtil.RETURN_MSG_SUCCESS.equals(returnObjTmp.getString(ProtocolUtil.RESULT_CODE))){
            return JSONObject.parseObject("{'errorInfo':"+returnObjTmp.getString(ProtocolUtil.RESULT_MSG)+"}");
        }
        return returnObjTmp.getJSONObject(ProtocolUtil.RESULT_INFO);

    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
