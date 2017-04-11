package com.java110.user.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.ProtocolUtil;
import com.java110.entity.user.BoCust;
import com.java110.entity.user.BoCustAttr;
import com.java110.feign.base.IPrimaryKeyService;
import com.java110.user.dao.IUserServiceDao;
import com.java110.user.smo.IUserServiceSMO;
import com.java110.core.base.smo.BaseServiceSMO;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务信息管理业务信息实现
 * Created by wuxw on 2017/4/5.
 */
@Service("userServiceSMOImpl")
public class UserServiceSMOImpl extends BaseServiceSMO implements IUserServiceSMO {

    @Autowired
    IPrimaryKeyService iPrimaryKeyService;

    @Autowired
    IUserServiceDao iUserServiceDao;

    //新增用户
    private final static String USER_ACTION_ADD = "ADD";

    //新增用户
    private final static String USER_ACTION_KIP = "KIP";

    //新增用户
    private final static String USER_ACTION_DEL = "DEL";

    /**
     * 保存用户信息
     *
     * @param userInfoJson 入参为用户信息json传
     * @return
     */
    public String saveUser(String userInfoJson) {

        JSONObject reqUserJSON = null;
        try {
            reqUserJSON = this.simpleValidateJSON(userInfoJson);
            //boCust增加Action (动作)
            if (reqUserJSON.containsKey("boCust")) {
                JSONObject boCust = reqUserJSON.getJSONObject("boCust");
                boCust.put("state", USER_ACTION_ADD);
            }
            //boCustAttr增加Action（动作）
            if (reqUserJSON.containsKey("boCustAttr")) {
                JSONArray boCustAttrs = reqUserJSON.getJSONArray("boCustAttr");

                for (int attrIndex = 0; attrIndex < boCustAttrs.size(); attrIndex++) {
                    JSONObject boCustAttr = boCustAttrs.getJSONObject(attrIndex);
                    boCustAttr.put("state", USER_ACTION_ADD);
                }
            }
        } catch (RuntimeException e) {
            //返回异常信息
            return e.getMessage();
        }
        return soUserService(reqUserJSON.toJSONString());
    }


    /**
     * 所有服务处理类
     *
     * @param userInfoJson
     * @return
     */
    public String soUserService(String userInfoJson) {
        LoggerEngine.debug("用户服务操作客户入参：" + userInfoJson);
        String resultUserInfo = null;
        JSONObject reqUserJSON = null;
        try {
            reqUserJSON = this.simpleValidateJSON(userInfoJson);
            //1.0规则校验，报文是否合法

            if(reqUserJSON.containsKey("boCust")){

            }

            if(reqUserJSON.containsKey("boCustAttr")){

            }

            //2.0
        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultUserInfo);
            return resultUserInfo;
        }
    }

    /**
     * {
     *     boCust:[{},{}]
     * }
     * 客户信心处理
     * @param boCusts
     * @return
     * @throws Exception
     */
    public String soBoCust(String boCusts) throws Exception{
        // 将 jsonArray 转为list<BoCust> 对象
        JSONObject jsonObject = JSONObject.parseObject(boCusts);

        List<BoCust> boCustList = JSONObject.parseArray(jsonObject.getJSONArray("boCust").toJSONString(), BoCust.class);

        //保存数据

        for(BoCust boCust : boCustList){
            int custId = NumberUtils.toInt(boCust.getBoId(),-1);
            //如果客户ID小于0 ，则自己生成客户ID,这个只有在有 主键生成服务时使用，否则为了防止出错，需要前段调用时需要生成custId
            if(custId < 0 ){
                JSONObject data = new JSONObject();
                data.put("type","CUST_ID");
                //要求接口返回 {"RESULT_CODE":"0000","RESULT_INFO":{"user_id":"7020170411000041"},"RESULT_MSG":"成功"}
                String custIdJSONStr = iPrimaryKeyService.queryPrimaryKey(data.toJSONString());
                JSONObject custIdJSONTmp = JSONObject.parseObject(custIdJSONStr);
                if(custIdJSONTmp.containsKey("RESULT_CODE")
                        && ProtocolUtil.RETURN_MSG_SUCCESS.equals(custIdJSONTmp.getString("RESULT_CODE"))
                        && custIdJSONTmp.containsKey("RESULT_INFO")){
                    //从接口生成custId
                    custId = NumberUtils.toInt(custIdJSONTmp.getJSONObject("RESULT_INFO").getString("CUST_ID"),-1);
                }
            }

            boCust.setCustId(custId+"");

            //保存数据至 bo_cust 表中
            int saveBoCustFlag = iUserServiceDao.saveDataToBoCust(boCust);

            if(saveBoCustFlag > 0){
                //建档 处理
            }

        }
        return "";
    }

    /**
     * 注意在调用这个接口时，相应的客户信息必须存在
     *
     *
     * 客户信息属性处理
     * 协议：
     *{
     *     boCustAttr:[{},{}]
     * }
     * @param boCustAttrs
     * @return
     * @throws Exception
     */
    @Override
    public String soBoCustAttr(String boCustAttrs) throws Exception {

        //这里可以加入基本客户信息是否存在的校验，暂时没有必要实现

        // 将 jsonArray 转为list<BoCust> 对象
        JSONObject jsonObject = JSONObject.parseObject(boCustAttrs);

        List<BoCustAttr> boCustAttrList = JSONObject.parseArray(jsonObject.getJSONArray("boCustAttr").toJSONString(), BoCustAttr.class);

        //保存数据

        for(BoCustAttr boCustAttr : boCustAttrList) {

            //保存数据至 bo_cust_attr 表中
            int saveBoCustFlag = iUserServiceDao.saveDataToBoCustAttr(boCustAttr);

            if (saveBoCustFlag > 0) {
                //建档 处理
            }
        }
            return "";
    }

    public IPrimaryKeyService getiPrimaryKeyService() {
        return iPrimaryKeyService;
    }

    public void setiPrimaryKeyService(IPrimaryKeyService iPrimaryKeyService) {
        this.iPrimaryKeyService = iPrimaryKeyService;
    }

    public IUserServiceDao getiUserServiceDao() {
        return iUserServiceDao;
    }

    public void setiUserServiceDao(IUserServiceDao iUserServiceDao) {
        this.iUserServiceDao = iUserServiceDao;
    }
}
