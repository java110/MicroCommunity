package com.java110.core.base.smo;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.AppBase;
import com.java110.core.context.AppContext;
import com.java110.intf.code.IPrimaryKeyInnerServiceSMO;
import com.java110.utils.util.ProtocolUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 所有服务端的基类
 * 1、报文分装
 * 2、报文解析
 * Created by wuxw on 2017/2/28.
 */
public class BaseServiceSMO extends AppBase {

    private static final Logger logger = LoggerFactory.getLogger(BaseServiceSMO.class);

    /**
     * 主键生成
     *
     * @param iPrimaryKeyService 主键生成服务对象
     * @param type               主键类型 如 OL_ID , CUST_ID
     * @return
     * @throws Exception
     */
    protected String queryPrimaryKey(IPrimaryKeyInnerServiceSMO iPrimaryKeyService, String type) throws Exception {
        JSONObject data = new JSONObject();
        data.put("type", type);
        //生成的ID
        String targetId = "-1";
        //要求接口返回 {"RESULT_CODE":"0000","RESULT_INFO":{"user_id":"7020170411000041"},"RESULT_MSG":"成功"}
        String custIdJSONStr = iPrimaryKeyService.queryPrimaryKey(data.toJSONString());
        JSONObject custIdJSONTmp = JSONObject.parseObject(custIdJSONStr);
        if (custIdJSONTmp.containsKey("RESULT_CODE")
                && ProtocolUtil.RETURN_MSG_SUCCESS.equals(custIdJSONTmp.getString("RESULT_CODE"))
                && custIdJSONTmp.containsKey("RESULT_INFO")) {
            //从接口生成olId
            targetId = custIdJSONTmp.getJSONObject("RESULT_INFO").getString(type);
        }
        if ("-1".equals(targetId)) {
            throw new RuntimeException("调用主键生成服务服务失败，" + custIdJSONStr);
        }

        return targetId;
    }


    /**
     * 创建上下文对象
     *
     * @return
     */
    protected AppContext createApplicationContext() {
        return AppContext.newInstance();
    }


    /**
     * map 参数转 url get 参数 非空值转为get参数 空值忽略
     *
     * @param info map数据
     * @return url get 参数 带？
     */
    protected String mapToUrlParam(Map info) {
        String urlParam = "";
        if (info == null || info.isEmpty()) {
            return urlParam;
        }

        urlParam += "?";

        for (Object key : info.keySet()) {
            if (StringUtil.isNullOrNone(info.get(key))) {
                continue;
            }

            urlParam += (key + "=" + info.get(key) + "&");
        }

        urlParam = urlParam.endsWith("&") ? urlParam.substring(0, urlParam.length() - 1) : urlParam;

        return urlParam;
    }

    /**
     * @param urlparam 带分隔的url参数
     * @return
     */
    public static Map<String, String> urlToMap(String urlparam) {
        if (urlparam.indexOf("?") < 0) {
            return new HashMap<>();
        }
        urlparam = urlparam.substring(urlparam.indexOf("?")+1);
        Map<String, String> map = new HashMap<String, String>();
        String[] param = urlparam.split("&");
        for (String keyvalue : param) {
            String[] pair = keyvalue.split("=");
            if (pair.length == 2) {
                map.put(pair[0], pair[1]);
            }
        }
        return map;
    }

    public static void main(String[] args) {
        String url = "https://www.xx.com";
        System.out.println(url.substring(0,url.indexOf("?")));
    }
}
