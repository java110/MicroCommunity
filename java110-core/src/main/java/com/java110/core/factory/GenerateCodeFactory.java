package com.java110.core.factory;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.cache.MappingCache;
import com.java110.common.constant.MappingConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.GenerateCodeException;
import com.java110.common.exception.ResponseErrorException;
import com.java110.common.factory.ApplicationContextFactory;
import com.java110.common.util.Assert;
import com.java110.common.util.DateUtil;
import com.java110.feign.code.ICodeApi;
import org.springframework.web.client.RestTemplate;

import java.rmi.NoSuchObjectException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生成序列工具类
 * Created by wuxw on 2017/2/27.
 */
public class GenerateCodeFactory {

    private static final long ONE_STEP = 1000000;
    private static final Lock LOCK = new ReentrantLock();
    private static short lastCount = 1;
    private static int count = 0;
    private static final String first = "10";

    /**
     *
     * 只有在不调用服务生成ID时有用
     */
    private static Map<String,String> prefixMap = null;
    static {
        prefixMap = new HashMap<String,String>();
        //10+yyyymmdd+八位序列
        prefixMap.put("oId","10");
        //（20+yyyymmdd+八位序列）
        prefixMap.put("bId","20");
        //（11+yyyymmdd+八位序列）
        prefixMap.put("attrId","11");
        prefixMap.put("transactionId","1000001");
        prefixMap.put("pageTransactionId","1000002");
        prefixMap.put("dataFlowId","2000");
        prefixMap.put("userId","30");
        prefixMap.put("storeId","40");
        prefixMap.put("storePhotoId","41");
        prefixMap.put("storeCerdentialsId","42");
    }

    private static String PLATFORM_CODE = "0001";

    @SuppressWarnings("finally")
    public static String nextId(String idLength) {
        LOCK.lock();
        try {
            if (lastCount == ONE_STEP) {
                lastCount = 1;
            }
            count = lastCount++;
        } finally {
            LOCK.unlock();
            return String.format(idLength, count);
        }
    }

    public static String nextId(){
        return nextId("%06d");
    }

    /**
     * 获取交易流水ID
     *
     * @return
     */
    public static String getTransactionId() {

        //从内存中获取平台随机码

        return prefixMap.get("transactionId") + DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H) + nextId();
    }

    /**
     * 获取内部平台 交易流水
     * @return
     * @throws NoSuchObjectException
     */
    public static String getInnerTransactionId() throws Exception{
        return codeApi().generateCode(prefixMap.get("transactionId"));
    }

    /**
     * 获取交易流水ID
     *
     * @return
     */
    public static String getPageTransactionId() {

        //从内存中获取平台随机码

        return prefixMap.get("pageTransactionId") + DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H) + nextId();
    }

    public static String getOId() throws GenerateCodeException{
        if(!MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_NEED_INVOKE_GENERATE_ID))){
            return prefixMap.get("oId") + DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H) + nextId("%08d");
        }
        return getCode(prefixMap.get("oId"));
    }


    /**
     * 查询Code
     * @param prefix
     * @return
     * @throws GenerateCodeException
     */
    private static String getCode(String prefix) throws GenerateCodeException{
        //调用服务
        String code = "-1";
        try {
            String responseMessage = restTemplate().postForObject(MappingCache.getValue(MappingConstant.KEY_CODE_PATH),
                    createCodeRequestJson(getTransactionId(),prefix,prefix).toJSONString(), String.class);

            if(ResponseConstant.RESULT_CODE_ERROR.equals(responseMessage)){
                throw new ResponseErrorException(ResponseConstant.RESULT_CODE_ERROR, "生成oId编码失败");
            }
            Assert.jsonObjectHaveKey(responseMessage, "code", "编码生成系统 返回报文错误" + responseMessage);

            JSONObject resJson = JSONObject.parseObject(responseMessage);

            if (!ResponseConstant.RESULT_CODE_SUCCESS.equals(resJson.getString("code"))) {
                throw new ResponseErrorException(resJson.getString("code"), "生成oId编码失败 "
                        + resJson.getString("message"));
            }
            code = resJson.getString("id");
        }catch (Exception e){
            throw new GenerateCodeException(ResponseConstant.RESULT_CODE_ERROR,e.getMessage());
        }
        finally {
            return code;
        }

    }

    public static String getBId()  throws GenerateCodeException{
        if(!MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_NEED_INVOKE_GENERATE_ID))){
            return prefixMap.get("bId") + DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H) + nextId("%08d");
        }
        //调用服务
        return getCode(prefixMap.get("bId"));
    }

    public static String getAttrId()  throws GenerateCodeException{
        if(!MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_NEED_INVOKE_GENERATE_ID))){
            return prefixMap.get("attrId") + DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H) + nextId("%08d");
        }
        //调用服务
        return getCode(prefixMap.get("attrId"));
    }

    /**
     * 生成dataFlowId
     * @return
     * @throws GenerateCodeException
     */
    public static String getDataFlowId()  throws GenerateCodeException{

        return UUID.randomUUID().toString().replace("-","").toLowerCase();

    }

    public static String getUserId()  throws GenerateCodeException{
        if(!MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_NEED_INVOKE_GENERATE_ID))){
            return prefixMap.get("userId") +DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H)+ nextId("%06d");
        }
        //调用服务
        return getCode(prefixMap.get("userId"));
    }


    public static String getStoreId()  throws GenerateCodeException{
        if(!MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_NEED_INVOKE_GENERATE_ID))){
            return prefixMap.get("storeId") +DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H)+ nextId("%06d");
        }
        //调用服务
        return getCode(prefixMap.get("storeId"));
    }


    public static String getStorePhotoId()  throws GenerateCodeException{
        if(!MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_NEED_INVOKE_GENERATE_ID))){
            return prefixMap.get("storePhotoId") +DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H)+ nextId("%06d");
        }
        //调用服务
        return getCode(prefixMap.get("storePhotoId"));
    }


    public static String getStoreCerdentialsId()  throws GenerateCodeException{
        if(!MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_NEED_INVOKE_GENERATE_ID))){
            return prefixMap.get("storeCerdentialsId") +DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H)+ nextId("%06d");
        }
        //调用服务
        return getCode(prefixMap.get("storeCerdentialsId"));
    }

    /**
     * 获取restTemplate
     * @return
     * @throws NoSuchObjectException
     */
    private static RestTemplate restTemplate() throws NoSuchObjectException{

       Object bean = ApplicationContextFactory.getBean("restTemplate");

       if(bean == null){
           throw new NoSuchObjectException("没有找到restTemplate对象，请核实");
       }

       return (RestTemplate) bean;
    }

    /**
     * 获取codeApi
     * @return
     * @throws NoSuchObjectException
     */
    private static ICodeApi codeApi() throws NoSuchObjectException{

        Object bean = ApplicationContextFactory.getBean(ICodeApi.class.getName());

        if(bean == null){
            throw new NoSuchObjectException("codeApi，请核实");
        }

        return (ICodeApi) bean;
    }




    /**
     * ID生成请求报文
     * @param transactionId
     * @return
     */
    private static JSONObject createCodeRequestJson(String transactionId, String prefix, String name){
        JSONObject paramOut = JSONObject.parseObject("{}");
        paramOut.put("transactionId",transactionId);
        paramOut.put("prefix",prefix);
        paramOut.put("name",name);
        paramOut.put("requestTime",DateUtil.getNowDefault());
        return paramOut;
    }
}
