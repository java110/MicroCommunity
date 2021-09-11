package com.java110.api.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.ICompanyServiceSMO;
import com.java110.po.store.StoreAttrPo;
import com.java110.po.store.StoreCerdentialPo;
import com.java110.po.store.StorePo;
import com.java110.utils.constant.AttrCdConstant;
import com.java110.utils.constant.CredentialsConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 初始化公司实现类
 * Created by Administrator on 2019/3/28.
 */
@Service("companyServiceSMOImpl")
public class CompanyServiceSMOImpl extends DefaultAbstractComponentSMO implements ICompanyServiceSMO {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 查询 商户类别
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> getStoreType(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        Assert.hasLength(pd.getUserId(), "用户还未登录请先登录");

        responseEntity = this.callCenterService(restTemplate, pd, "", "query.store.type?type=all", HttpMethod.GET);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Assert.jsonObjectHaveKey(responseEntity.getBody(), "storeType", "查询中心服务异常，不是有效json或未包含storeType节点");
            //将storeType 返回出去
            responseEntity = new ResponseEntity<String>(JSONObject.parseObject(responseEntity.getBody()).getJSONArray("storeType").toJSONString(), HttpStatus.OK);
        }
        return responseEntity;
    }

    /**
     * 保存公司信息
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> saveCompanyInfo(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        Assert.hasLength(pd.getUserId(), "用户还未登录请先登录");

        validateCompanyInfo(pd.getReqData());


        JSONObject reqJson = JSONObject.parseObject("{\""+StorePo.class.getSimpleName()+"\":{}}");

        JSONObject paramJson = JSONObject.parseObject(pd.getReqData());

        //基本信息
        JSONObject businessStore = reqJson.getJSONObject(StorePo.class.getSimpleName());
        businessStore.put("userId", pd.getUserId());
        businessStore.put("name", paramJson.getString("name"));
        businessStore.put("address", paramJson.getString("areaAddress") + paramJson.getString("address"));
        businessStore.put("tel", paramJson.getString("tel"));
        businessStore.put("storeTypeCd", paramJson.getString("storeTypeCd"));
        businessStore.put("nearbyLandmarks", paramJson.getString("nearbyLandmarks"));

        JSONArray businessStoreAttr = new JSONArray();

        JSONObject attr = new JSONObject();
        attr.put("specCd", AttrCdConstant.SPEC_CD_STORE_CORPORATION);
        attr.put("value", paramJson.getString("corporation"));
        businessStoreAttr.add(attr);

        attr = new JSONObject();
        attr.put("specCd", AttrCdConstant.SPEC_CD_STORE_REGISTEREDCAPITAL);
        attr.put("value", paramJson.getString("registeredCapital"));
        businessStoreAttr.add(attr);

        attr = new JSONObject();
        attr.put("specCd", AttrCdConstant.SPEC_CD_STORE_FOUNDINGTIME);
        attr.put("value", paramJson.getString("foundingTime"));
        businessStoreAttr.add(attr);

        attr = new JSONObject();
        attr.put("specCd", AttrCdConstant.SPEC_CD_STORE_REGISTRATIONAUTHORITY);
        attr.put("value", paramJson.getString("registrationAuthority"));
        businessStoreAttr.add(attr);

        attr = new JSONObject();
        attr.put("specCd", AttrCdConstant.SPEC_CD_STORE_SCOPE);
        attr.put("value", paramJson.getString("scope"));
        businessStoreAttr.add(attr);
        reqJson.put(StoreAttrPo.class.getSimpleName(), businessStoreAttr);

        JSONArray businessStoreCerdentials = new JSONArray();
        JSONObject cerdentials = new JSONObject();

        cerdentials.put("credentialsCd", CredentialsConstant.LICENCE);
        cerdentials.put("value", paramJson.getString("value"));
        cerdentials.put("validityPeriod", paramJson.getString("validityPeriod"));
        cerdentials.put("positivePhoto", "");
        cerdentials.put("negativePhoto", "");
        businessStoreCerdentials.add(cerdentials);

        reqJson.put(StoreCerdentialPo.class.getSimpleName(), businessStoreCerdentials);

        //reqJson.put(StorePo.class.getSimpleName(), businessStore);


        responseEntity = this.callCenterService(restTemplate, pd, reqJson.toJSONString(), "save.store.info", HttpMethod.POST);

       /* if(responseEntity.getStatusCode() != HttpStatus.OK){
            return responseEntity;
        }

        JSONObject resStoreInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        //将现用户添加为商户管理员
        JSONObject staffInfo = new JSONObject();
        staffInfo.put("userId",pd.getUserId());
        staffInfo.put("storeId",resStoreInfo.getString("storeId"));
        responseEntity = this.callCenterService(restTemplate,pd,staffInfo.toJSONString(), ServiceConstant.SERVICE_API_URL+"/api/user.staff.add", HttpMethod.POST);
*/

        return responseEntity;
    }

    /**
     * 查询 所有省市
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> getAreas(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        Assert.hasLength(pd.getUserId(), "用户还未登录请先登录");
        JSONObject param = JSONObject.parseObject(pd.getReqData());

        responseEntity = this.callCenterService(restTemplate, pd, "", "area.listAreas" + mapToUrlParam(param), HttpMethod.GET);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Assert.jsonObjectHaveKey(responseEntity.getBody(), "areas", "查询中心服务异常，不是有效json或未包含areas节点");
            //将areas 返回出去
            responseEntity = new ResponseEntity<String>(JSONObject.parseObject(responseEntity.getBody()).getJSONArray("areas").toJSONString(), HttpStatus.OK);
        }
        return responseEntity;
    }


    /**
     * 校验公司信息
     *
     * @param param
     */
    private void validateCompanyInfo(String param) {

    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
