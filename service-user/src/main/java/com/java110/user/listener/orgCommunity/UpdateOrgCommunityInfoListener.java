package com.java110.user.listener.orgCommunity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.community.OrgCommunityPo;
import com.java110.user.dao.IOrgCommunityServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改隶属小区信息 侦听
 * <p>
 * 处理节点
 * 1、businessOrgCommunity:{} 隶属小区基本信息节点
 * 2、businessOrgCommunityAttr:[{}] 隶属小区属性信息节点
 * 3、businessOrgCommunityPhoto:[{}] 隶属小区照片信息节点
 * 4、businessOrgCommunityCerdentials:[{}] 隶属小区证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateOrgCommunityInfoListener")
@Transactional
public class UpdateOrgCommunityInfoListener extends AbstractOrgCommunityBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateOrgCommunityInfoListener.class);
    @Autowired
    private IOrgCommunityServiceDao orgCommunityServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ORG_COMMUNITY;
    }

    /**
     * business过程
     *
     * @param dataFlowContext 上下文对象
     * @param business        业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");


        //处理 businessOrgCommunity 节点
        if (data.containsKey(OrgCommunityPo.class.getSimpleName())) {
            Object _obj = data.get(OrgCommunityPo.class.getSimpleName());
            JSONArray businessOrgCommunitys = null;
            if (_obj instanceof JSONObject) {
                businessOrgCommunitys = new JSONArray();
                businessOrgCommunitys.add(_obj);
            } else {
                businessOrgCommunitys = (JSONArray) _obj;
            }
            //JSONObject businessOrgCommunity = data.getJSONObject("businessOrgCommunity");
            for (int _orgCommunityIndex = 0; _orgCommunityIndex < businessOrgCommunitys.size(); _orgCommunityIndex++) {
                JSONObject businessOrgCommunity = businessOrgCommunitys.getJSONObject(_orgCommunityIndex);
                doBusinessOrgCommunity(business, businessOrgCommunity);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("orgCommunityId", businessOrgCommunity.getString("orgCommunityId"));
                }
            }

        }
    }


    /**
     * business to instance 过程
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);

        //隶属小区信息
        List<Map> businessOrgCommunityInfos = orgCommunityServiceDaoImpl.getBusinessOrgCommunityInfo(info);
        if (businessOrgCommunityInfos != null && businessOrgCommunityInfos.size() > 0) {
            for (int _orgCommunityIndex = 0; _orgCommunityIndex < businessOrgCommunityInfos.size(); _orgCommunityIndex++) {
                Map businessOrgCommunityInfo = businessOrgCommunityInfos.get(_orgCommunityIndex);
                flushBusinessOrgCommunityInfo(businessOrgCommunityInfo, StatusConstant.STATUS_CD_VALID);
                orgCommunityServiceDaoImpl.updateOrgCommunityInfoInstance(businessOrgCommunityInfo);
                if (businessOrgCommunityInfo.size() == 1) {
                    dataFlowContext.addParamOut("orgCommunityId", businessOrgCommunityInfo.get("org_community_id"));
                }
            }
        }

    }

    /**
     * 撤单
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {

        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //隶属小区信息
        List<Map> orgCommunityInfo = orgCommunityServiceDaoImpl.getOrgCommunityInfo(info);
        if (orgCommunityInfo != null && orgCommunityInfo.size() > 0) {

            //隶属小区信息
            List<Map> businessOrgCommunityInfos = orgCommunityServiceDaoImpl.getBusinessOrgCommunityInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessOrgCommunityInfos == null || businessOrgCommunityInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（orgCommunity），程序内部异常,请检查！ " + delInfo);
            }
            for (int _orgCommunityIndex = 0; _orgCommunityIndex < businessOrgCommunityInfos.size(); _orgCommunityIndex++) {
                Map businessOrgCommunityInfo = businessOrgCommunityInfos.get(_orgCommunityIndex);
                flushBusinessOrgCommunityInfo(businessOrgCommunityInfo, StatusConstant.STATUS_CD_VALID);
                orgCommunityServiceDaoImpl.updateOrgCommunityInfoInstance(businessOrgCommunityInfo);
            }
        }

    }


    /**
     * 处理 businessOrgCommunity 节点
     *
     * @param business             总的数据节点
     * @param businessOrgCommunity 隶属小区节点
     */
    private void doBusinessOrgCommunity(Business business, JSONObject businessOrgCommunity) {

        Assert.jsonObjectHaveKey(businessOrgCommunity, "orgCommunityId", "businessOrgCommunity 节点下没有包含 orgCommunityId 节点");

        if (businessOrgCommunity.getString("orgCommunityId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "orgCommunityId 错误，不能自动生成（必须已经存在的orgCommunityId）" + businessOrgCommunity);
        }
        //自动保存DEL
        autoSaveDelBusinessOrgCommunity(business, businessOrgCommunity);

        businessOrgCommunity.put("bId", business.getbId());
        businessOrgCommunity.put("operate", StatusConstant.OPERATE_ADD);
        //保存隶属小区信息
        orgCommunityServiceDaoImpl.saveBusinessOrgCommunityInfo(businessOrgCommunity);

    }


    public IOrgCommunityServiceDao getOrgCommunityServiceDaoImpl() {
        return orgCommunityServiceDaoImpl;
    }

    public void setOrgCommunityServiceDaoImpl(IOrgCommunityServiceDao orgCommunityServiceDaoImpl) {
        this.orgCommunityServiceDaoImpl = orgCommunityServiceDaoImpl;
    }


}
