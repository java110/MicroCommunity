package com.java110.user.listener.org;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.org.OrgPo;
import com.java110.user.dao.IOrgServiceDao;
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
 * 修改组织信息 侦听
 * <p>
 * 处理节点
 * 1、businessOrg:{} 组织基本信息节点
 * 2、businessOrgAttr:[{}] 组织属性信息节点
 * 3、businessOrgPhoto:[{}] 组织照片信息节点
 * 4、businessOrgCerdentials:[{}] 组织证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateOrgInfoListener")
@Transactional
public class UpdateOrgInfoListener extends AbstractOrgBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateOrgInfoListener.class);
    @Autowired
    private IOrgServiceDao orgServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ORG;
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


        //处理 businessOrg 节点
        if (data.containsKey(OrgPo.class.getSimpleName())) {
            Object _obj = data.get(OrgPo.class.getSimpleName());
            JSONArray businessOrgs = null;
            if (_obj instanceof JSONObject) {
                businessOrgs = new JSONArray();
                businessOrgs.add(_obj);
            } else {
                businessOrgs = (JSONArray) _obj;
            }
            //JSONObject businessOrg = data.getJSONObject("businessOrg");
            for (int _orgIndex = 0; _orgIndex < businessOrgs.size(); _orgIndex++) {
                JSONObject businessOrg = businessOrgs.getJSONObject(_orgIndex);
                doBusinessOrg(business, businessOrg);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("orgId", businessOrg.getString("orgId"));
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

        //组织信息
        List<Map> businessOrgInfos = orgServiceDaoImpl.getBusinessOrgInfo(info);
        if (businessOrgInfos != null && businessOrgInfos.size() > 0) {
            for (int _orgIndex = 0; _orgIndex < businessOrgInfos.size(); _orgIndex++) {
                Map businessOrgInfo = businessOrgInfos.get(_orgIndex);
                flushBusinessOrgInfo(businessOrgInfo, StatusConstant.STATUS_CD_VALID);
                orgServiceDaoImpl.updateOrgInfoInstance(businessOrgInfo);
                if (businessOrgInfo.size() == 1) {
                    dataFlowContext.addParamOut("orgId", businessOrgInfo.get("org_id"));
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
        //组织信息
        List<Map> orgInfo = orgServiceDaoImpl.getOrgInfo(info);
        if (orgInfo != null && orgInfo.size() > 0) {

            //组织信息
            List<Map> businessOrgInfos = orgServiceDaoImpl.getBusinessOrgInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessOrgInfos == null || businessOrgInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（org），程序内部异常,请检查！ " + delInfo);
            }
            for (int _orgIndex = 0; _orgIndex < businessOrgInfos.size(); _orgIndex++) {
                Map businessOrgInfo = businessOrgInfos.get(_orgIndex);
                flushBusinessOrgInfo(businessOrgInfo, StatusConstant.STATUS_CD_VALID);
                orgServiceDaoImpl.updateOrgInfoInstance(businessOrgInfo);
            }
        }

    }


    /**
     * 处理 businessOrg 节点
     *
     * @param business    总的数据节点
     * @param businessOrg 组织节点
     */
    private void doBusinessOrg(Business business, JSONObject businessOrg) {

        Assert.jsonObjectHaveKey(businessOrg, "orgId", "businessOrg 节点下没有包含 orgId 节点");

        if (businessOrg.getString("orgId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "orgId 错误，不能自动生成（必须已经存在的orgId）" + businessOrg);
        }
        //自动保存DEL
        autoSaveDelBusinessOrg(business, businessOrg);

        businessOrg.put("bId", business.getbId());
        businessOrg.put("operate", StatusConstant.OPERATE_ADD);
        //保存组织信息
        orgServiceDaoImpl.saveBusinessOrgInfo(businessOrg);

    }


    public IOrgServiceDao getOrgServiceDaoImpl() {
        return orgServiceDaoImpl;
    }

    public void setOrgServiceDaoImpl(IOrgServiceDao orgServiceDaoImpl) {
        this.orgServiceDaoImpl = orgServiceDaoImpl;
    }


}
