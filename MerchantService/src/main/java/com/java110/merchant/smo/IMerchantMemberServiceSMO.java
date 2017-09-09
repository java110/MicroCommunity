package com.java110.merchant.smo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.java110.common.constant.StateConstant;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.common.util.ProtocolUtil;
import com.java110.entity.merchant.BoMerchantMember;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantMember;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuxw on 2017/8/30.
 */
public interface IMerchantMemberServiceSMO {

    /**
     * 查询商户成员
     * 包括 基本信息merchant 和 属性信息 merchantAttr
     * @param merchantMember
     * @return
     * @throws Exception
     */
    public String queryMerchantMember(MerchantMember merchantMember) throws Exception;



    /**
     * 保存商户成员信息
     *
     * @param merchantInfoJson 入参为商户成员信息json传
     * @return
     */
    public String saveMerchantMember(String merchantInfoJson) throws Exception;


    /**
     * 所有服务处理类
     * {
     *
     *     'boMerchantMember':[{}],
     *     'boMerchantMemberAttr':[{}]
     * }
     *
     * 返回报文：
     *
     * {'RESULT_CODE':'0000','RESULT_MSG':'成功','RESULT_INFO':{'merchantId':'7000123,718881991'}}
     * @param merchantInfoJson
     * @return
     */
    public String soMerchantMemberService(JSONObject merchantInfoJson) throws Exception;

    /**
     *
     * 请求报文为：
     *
     * {
     "data": [
     {
     "actionTypeCd": "C1",
     "boMerchantMember": [{},{}],
     "boMerchantMemberAttr": [{ }, {}]
     },
     {
     "actionTypeCd": "C1",
     "boMerchantMember": [{},{}],
     "boMerchantMemberAttr": [{ }, {}]
     }
     ]
     }

     返回报文 ：

     { 'RESULT_CODE': '0000', 'RESULT_MSG': '成功', 'RESULT_INFO': {'merchant':[{'oldMerchantId':'-1','merchantId':'12345678'},{'oldMerchantId':'-2','merchantId':'12345678'}]} }
     * @param mInfoJson
     * @return
     * @throws Exception
     */
    public String soMerchantMemberServiceForOrderService(JSONObject mInfoJson) throws Exception ;

    /**
     * {
     *     boMerchantMember:[{},{}]
     * }
     * 客户信心处理
     *
     *
     * @param boMerchantMembers
     * @return 成功 会带上处理客户的客户ID
     * {'RESULT_CODE':'0000','RESULT_MSG':'成功','RESULT_INFO':{'merchantId':'7000123,718881991'}}
     * @throws Exception
     */
    public String soBoMerchantMember(String boMerchantMembers) throws Exception;
    /**
     * 将生成的merchantId 封装在map中返回
     * ...
     * merchantIdKey.put("merchantId-1","710020404040");
     *
     * ...
     *
     * key 为 merchantId 加原前的值
     *
     * merchantIdKey 如果为空不做处理
     * @param boMerchantMembers 客户信息
     * @param merchantIdKey merchantIdKeymap
     * @return
     * @throws Exception
     */
    public String soBoMerchantMember(String boMerchantMembers,Map merchantIdKey) throws Exception;


    /**
     * 查询需要作废的订单信息
     * @param data
     * @return
     * @throws Exception
     */
    public String queryNeedDeleteData(JSONObject data) throws Exception;






}
