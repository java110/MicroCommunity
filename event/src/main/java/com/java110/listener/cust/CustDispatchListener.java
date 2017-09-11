package com.java110.listener.cust;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.java110.common.util.Assert;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.context.AppContext;
import com.java110.entity.order.BusiOrder;
import com.java110.event.cust.AppCustEvent;
import com.java110.event.AppListener;
import com.java110.event.order.Ordered;
import com.java110.feign.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 客户信息调度
 * Created by wuxw on 2017/4/14.
 */
@Component
public class CustDispatchListener implements AppListener<AppCustEvent> ,Ordered{

    @Autowired
    IUserService iUserService;

    private final static int order = Ordered.dafultValue+1;
    @Override
    public void soDataService(AppCustEvent event) {

        //这里写 客户信息处理逻辑

        AppContext context = event.getContext();

        JSONArray dataCustInfos = event.getData();

        JSONObject custInfoJson = new JSONObject();
        custInfoJson.put("data",dataCustInfos.toJSONString());

        String custInfo = custInfoJson.toJSONString();


        Assert.hasLength(custInfo,"没有需要处理的信息[custInfo="+custInfo+"]");

        //调用用户服务处理,正常返回 {'RESULT_CODE':'0000','RESULT_MSG':'成功','RESULT_INFO':{'cust':[{'oldCustId':'-1','custId':'12345678'},{'oldCustId':'-2','custId':'12345678'}]} }}
        String returnUser = iUserService.soUserServiceForOrderService(custInfo);

        JSONObject returnUserTmp = JSONObject.parseObject(returnUser);

        Assert.notNull(returnUserTmp,"用户服务没有相应，请检查服务是否正常，请求报文："+returnUser);
        //受理不成功
        if(!returnUserTmp.containsKey(ProtocolUtil.RESULT_CODE)
                || !ProtocolUtil.RETURN_MSG_SUCCESS.equals(returnUserTmp.getString(ProtocolUtil.RESULT_CODE))){
            throw new IllegalArgumentException("客户受理失败，失败原因：" + (returnUserTmp.containsKey(ProtocolUtil.RESULT_MSG)
                    ?"未知原因":returnUserTmp.getString(ProtocolUtil.RESULT_MSG)) + "请求报文："+returnUser);
        }

        //判断是否有新建客户的数据
        Object getNewCustId = JSONPath.eval(custInfoJson,"$.data.boCust[custId < '0'][0].custId");

        // 这里将 客户服务返回的 新生成的custId 回写到上下文对象中，方便于其他模块用
        if(!ObjectUtils.isEmpty(getNewCustId)) {
            //受理成功，目前不做任何处理
            //{'cust':[{'oldCustId':'-1','custId':'12345678'},{'oldCustId':'-2','custId':'12345678'}]} }
            JSONObject resultInfoJ = returnUserTmp.getJSONObject(ProtocolUtil.RESULT_INFO);

            Assert.isNull(resultInfoJ, "cust", "用户服务，成功时返回信息错误，没有返回新建成功的cust节点 ,returnUser = " + returnUser);

            JSONArray newCusts = resultInfoJ.getJSONArray("cust");

            for(int newCustIndex = 0 ; newCustIndex < newCusts.size(); newCustIndex++){
                JSONObject newCust =  newCusts.getJSONObject(newCustIndex);
                Assert.isNull(newCust,"custId","用户服务，成功时返回信息错误，没有返回新建成功的cust节点下的custId ,returnUser = " + newCust);

                context.setKeyId(AppContext.PREFIX_CUSTID,newCust.getString("oldCustId"),newCust.getString("custId"));
            }
        }

    }

    @Override
    public JSONObject queryDataInfo(AppCustEvent event) {

        AppContext context = event.getContext();

        BusiOrder busiOrder = (BusiOrder) context.getReqObj();

        String dataInfo = iUserService.queryUserInfoByOlId(JSONObject.toJSONString(busiOrder));

        JSONObject dataInfoTmp = JSONObject.parseObject(dataInfo);

        Assert.notNull(dataInfoTmp,"用户服务没有相应，请检查服务是否正常，请求报文："+dataInfoTmp);
        //受理不成功
        if(!dataInfoTmp.containsKey(ProtocolUtil.RESULT_CODE)
                || !ProtocolUtil.RETURN_MSG_SUCCESS.equals(dataInfoTmp.getString(ProtocolUtil.RESULT_CODE))){
            return JSONObject.parseObject("{'errorInfo':"+dataInfoTmp.getString(ProtocolUtil.RESULT_MSG)+"}");
        }
        return dataInfoTmp.getJSONObject(ProtocolUtil.RESULT_INFO);
    }

    @Override
    public JSONObject queryNeedDeleteDataInfo(AppCustEvent event) {
        return null;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
