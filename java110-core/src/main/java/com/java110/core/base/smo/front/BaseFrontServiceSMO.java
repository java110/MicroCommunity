package com.java110.core.base.smo.front;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @ClassName BaseFrontServiceSMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/6/15 21:22
 * @Version 1.0
 * add by wuxw 2020/6/15
 **/
public class BaseFrontServiceSMO extends BaseServiceSMO {

    private static final String URL_API = ServiceConstant.SERVICE_API_URL + "/api/";
    //日志
    private static Logger logger = LoggerFactory.getLogger(BaseFrontServiceSMO.class);

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 查询
     *
     * @param pd          页面对象
     * @param param       传入对象
     * @param serviceCode 服务编码
     * @param t           返回类
     * @param <T>
     * @return
     */
    protected <T> T getForApi(IPageData pd, T param, String serviceCode, Class<T> t) {

        List<T> list = getForApis(pd, param, serviceCode, t);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 查询
     *
     * @param pd          页面对象
     * @param param       传入对象
     * @param serviceCode 服务编码
     * @param t           返回类
     * @param <T>
     * @return
     */
    protected <T> T postForApi(IPageData pd, T param, String serviceCode, Class<T> t) {
        return postForApi(pd, param, serviceCode, t);
    }

    /**
     * 查询
     *
     * @param pd          页面对象
     * @param param       传入对象
     * @param serviceCode 服务编码
     * @param t           返回类
     * @param <T>
     * @return
     */
    protected <T> List<T> postForApis(IPageData pd, T param, String serviceCode, Class<T> t) {

        String url = URL_API;


        ResponseEntity<String> responseEntity = callCenterService(restTemplate, pd, JSONObject.toJSONString(param), url, HttpMethod.POST);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new SMOException("调用" + serviceCode + "失败，" + responseEntity.getBody());
        }

        JSONObject resultVo = JSONObject.parseObject(responseEntity.getBody());

        if (ResultVo.CODE_MACHINE_OK != resultVo.getInteger("code")) {
            throw new SMOException(resultVo.getString("msg"));
        }

        Object bObj = resultVo.get("data");
        JSONArray datas = null;
        if (bObj instanceof JSONObject) {
            datas = new JSONArray();
            datas.add(bObj);
        } else {
            datas = (JSONArray) bObj;
        }
        String jsonStr = JSONObject.toJSONString(datas);

        List<T> list = JSONObject.parseArray(jsonStr, t);
        return list;
    }

    /**
     * 查询
     *
     * @param pd          页面对象
     * @param param       传入对象
     * @param serviceCode 服务编码
     * @param t           返回类
     * @param <T>
     * @return
     */
    protected <T> List<T> getForApis(IPageData pd, T param, String serviceCode, Class<T> t) {

        String url = URL_API;
        if (param != null) {
            url += mapToUrlParam(BeanConvertUtil.beanCovertMap(param));
        }

        ResponseEntity<String> responseEntity = callCenterService(restTemplate, pd, "", url, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new SMOException("调用" + serviceCode + "失败，" + responseEntity.getBody());
        }

        JSONObject resultVo = JSONObject.parseObject(responseEntity.getBody());

        if (!"0".equals(resultVo.getString("code"))) {
            throw new SMOException(resultVo.getString("msg"));
        }

        Object bObj = resultVo.get("data");
        JSONArray datas = null;
        if (bObj instanceof JSONObject) {
            datas = new JSONArray();
            datas.add(bObj);
        } else {
            datas = (JSONArray) bObj;
        }
        String jsonStr = JSONObject.toJSONString(datas);

        List<T> list = JSONObject.parseArray(jsonStr, t);
        return list;
    }


}
