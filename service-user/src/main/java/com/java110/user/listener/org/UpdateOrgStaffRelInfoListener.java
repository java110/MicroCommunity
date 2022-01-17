package com.java110.user.listener.org;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.org.OrgStaffRelPo;
import com.java110.user.dao.IOrgStaffRelServiceDao;
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
 * 修改组织员工关系信息 侦听
 * <p>
 * 处理节点
 * 1、businessOrgStaffRel:{} 组织员工关系基本信息节点
 * 2、businessOrgStaffRelAttr:[{}] 组织员工关系属性信息节点
 * 3、businessOrgStaffRelPhoto:[{}] 组织员工关系照片信息节点
 * 4、businessOrgStaffRelCerdentials:[{}] 组织员工关系证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateOrgStaffRelInfoListener")
@Transactional
public class UpdateOrgStaffRelInfoListener extends AbstractOrgStaffRelBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateOrgStaffRelInfoListener.class);
    @Autowired
    private IOrgStaffRelServiceDao orgStaffRelServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ORG_STAFF_REL;
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


        //处理 businessOrgStaffRel 节点
        if (data.containsKey(OrgStaffRelPo.class.getSimpleName())) {
            Object _obj = data.get(OrgStaffRelPo.class.getSimpleName());
            JSONArray businessOrgStaffRels = null;
            if (_obj instanceof JSONObject) {
                businessOrgStaffRels = new JSONArray();
                businessOrgStaffRels.add(_obj);
            } else {
                businessOrgStaffRels = (JSONArray) _obj;
            }
            //JSONObject businessOrgStaffRel = data.getJSONObject("businessOrgStaffRel");
            for (int _orgStaffRelIndex = 0; _orgStaffRelIndex < businessOrgStaffRels.size(); _orgStaffRelIndex++) {
                JSONObject businessOrgStaffRel = businessOrgStaffRels.getJSONObject(_orgStaffRelIndex);
                doBusinessOrgStaffRel(business, businessOrgStaffRel);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("relId", businessOrgStaffRel.getString("relId"));
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

        //组织员工关系信息
        List<Map> businessOrgStaffRelInfos = orgStaffRelServiceDaoImpl.getBusinessOrgStaffRelInfo(info);
        if (businessOrgStaffRelInfos != null && businessOrgStaffRelInfos.size() > 0) {
            for (int _orgStaffRelIndex = 0; _orgStaffRelIndex < businessOrgStaffRelInfos.size(); _orgStaffRelIndex++) {
                Map businessOrgStaffRelInfo = businessOrgStaffRelInfos.get(_orgStaffRelIndex);
                flushBusinessOrgStaffRelInfo(businessOrgStaffRelInfo, StatusConstant.STATUS_CD_VALID);
                orgStaffRelServiceDaoImpl.updateOrgStaffRelInfoInstance(businessOrgStaffRelInfo);
                if (businessOrgStaffRelInfo.size() == 1) {
                    dataFlowContext.addParamOut("relId", businessOrgStaffRelInfo.get("rel_id"));
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
        //组织员工关系信息
        List<Map> orgStaffRelInfo = orgStaffRelServiceDaoImpl.getOrgStaffRelInfo(info);
        if (orgStaffRelInfo != null && orgStaffRelInfo.size() > 0) {

            //组织员工关系信息
            List<Map> businessOrgStaffRelInfos = orgStaffRelServiceDaoImpl.getBusinessOrgStaffRelInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessOrgStaffRelInfos == null || businessOrgStaffRelInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（orgStaffRel），程序内部异常,请检查！ " + delInfo);
            }
            for (int _orgStaffRelIndex = 0; _orgStaffRelIndex < businessOrgStaffRelInfos.size(); _orgStaffRelIndex++) {
                Map businessOrgStaffRelInfo = businessOrgStaffRelInfos.get(_orgStaffRelIndex);
                flushBusinessOrgStaffRelInfo(businessOrgStaffRelInfo, StatusConstant.STATUS_CD_VALID);
                orgStaffRelServiceDaoImpl.updateOrgStaffRelInfoInstance(businessOrgStaffRelInfo);
            }
        }

    }


    /**
     * 处理 businessOrgStaffRel 节点
     *
     * @param business            总的数据节点
     * @param businessOrgStaffRel 组织员工关系节点
     */
    private void doBusinessOrgStaffRel(Business business, JSONObject businessOrgStaffRel) {

        Assert.jsonObjectHaveKey(businessOrgStaffRel, "relId", "businessOrgStaffRel 节点下没有包含 relId 节点");

        if (businessOrgStaffRel.getString("relId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "relId 错误，不能自动生成（必须已经存在的relId）" + businessOrgStaffRel);
        }
        //自动保存DEL
        autoSaveDelBusinessOrgStaffRel(business, businessOrgStaffRel);

        businessOrgStaffRel.put("bId", business.getbId());
        businessOrgStaffRel.put("operate", StatusConstant.OPERATE_ADD);
        //保存组织员工关系信息
        orgStaffRelServiceDaoImpl.saveBusinessOrgStaffRelInfo(businessOrgStaffRel);

    }


    public IOrgStaffRelServiceDao getOrgStaffRelServiceDaoImpl() {
        return orgStaffRelServiceDaoImpl;
    }

    public void setOrgStaffRelServiceDaoImpl(IOrgStaffRelServiceDao orgStaffRelServiceDaoImpl) {
        this.orgStaffRelServiceDaoImpl = orgStaffRelServiceDaoImpl;
    }


}
