package com.java110.user.listener.junkRequirement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.junkRequirement.JunkRequirementPo;
import com.java110.user.dao.IJunkRequirementServiceDao;
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
 * 修改旧货市场信息 侦听
 * <p>
 * 处理节点
 * 1、businessJunkRequirement:{} 旧货市场基本信息节点
 * 2、businessJunkRequirementAttr:[{}] 旧货市场属性信息节点
 * 3、businessJunkRequirementPhoto:[{}] 旧货市场照片信息节点
 * 4、businessJunkRequirementCerdentials:[{}] 旧货市场证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateJunkRequirementInfoListener")
@Transactional
public class UpdateJunkRequirementInfoListener extends AbstractJunkRequirementBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateJunkRequirementInfoListener.class);
    @Autowired
    private IJunkRequirementServiceDao junkRequirementServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_JUNK_REQUIREMENT;
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


        //处理 businessJunkRequirement 节点
        if (data.containsKey(JunkRequirementPo.class.getSimpleName())) {
            Object _obj = data.get(JunkRequirementPo.class.getSimpleName());
            JSONArray businessJunkRequirements = null;
            if (_obj instanceof JSONObject) {
                businessJunkRequirements = new JSONArray();
                businessJunkRequirements.add(_obj);
            } else {
                businessJunkRequirements = (JSONArray) _obj;
            }
            //JSONObject businessJunkRequirement = data.getJSONObject("businessJunkRequirement");
            for (int _junkRequirementIndex = 0; _junkRequirementIndex < businessJunkRequirements.size(); _junkRequirementIndex++) {
                JSONObject businessJunkRequirement = businessJunkRequirements.getJSONObject(_junkRequirementIndex);
                doBusinessJunkRequirement(business, businessJunkRequirement);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("junkRequirementId", businessJunkRequirement.getString("junkRequirementId"));
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

        //旧货市场信息
        List<Map> businessJunkRequirementInfos = junkRequirementServiceDaoImpl.getBusinessJunkRequirementInfo(info);
        if (businessJunkRequirementInfos != null && businessJunkRequirementInfos.size() > 0) {
            for (int _junkRequirementIndex = 0; _junkRequirementIndex < businessJunkRequirementInfos.size(); _junkRequirementIndex++) {
                Map businessJunkRequirementInfo = businessJunkRequirementInfos.get(_junkRequirementIndex);
                flushBusinessJunkRequirementInfo(businessJunkRequirementInfo, StatusConstant.STATUS_CD_VALID);
                junkRequirementServiceDaoImpl.updateJunkRequirementInfoInstance(businessJunkRequirementInfo);
                if (businessJunkRequirementInfo.size() == 1) {
                    dataFlowContext.addParamOut("junkRequirementId", businessJunkRequirementInfo.get("junk_requirement_id"));
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
        //旧货市场信息
        List<Map> junkRequirementInfo = junkRequirementServiceDaoImpl.getJunkRequirementInfo(info);
        if (junkRequirementInfo != null && junkRequirementInfo.size() > 0) {

            //旧货市场信息
            List<Map> businessJunkRequirementInfos = junkRequirementServiceDaoImpl.getBusinessJunkRequirementInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessJunkRequirementInfos == null || businessJunkRequirementInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（junkRequirement），程序内部异常,请检查！ " + delInfo);
            }
            for (int _junkRequirementIndex = 0; _junkRequirementIndex < businessJunkRequirementInfos.size(); _junkRequirementIndex++) {
                Map businessJunkRequirementInfo = businessJunkRequirementInfos.get(_junkRequirementIndex);
                flushBusinessJunkRequirementInfo(businessJunkRequirementInfo, StatusConstant.STATUS_CD_VALID);
                junkRequirementServiceDaoImpl.updateJunkRequirementInfoInstance(businessJunkRequirementInfo);
            }
        }

    }


    /**
     * 处理 businessJunkRequirement 节点
     *
     * @param business                总的数据节点
     * @param businessJunkRequirement 旧货市场节点
     */
    private void doBusinessJunkRequirement(Business business, JSONObject businessJunkRequirement) {

        Assert.jsonObjectHaveKey(businessJunkRequirement, "junkRequirementId", "businessJunkRequirement 节点下没有包含 junkRequirementId 节点");

        if (businessJunkRequirement.getString("junkRequirementId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "junkRequirementId 错误，不能自动生成（必须已经存在的junkRequirementId）" + businessJunkRequirement);
        }
        //自动保存DEL
        autoSaveDelBusinessJunkRequirement(business, businessJunkRequirement);

        businessJunkRequirement.put("bId", business.getbId());
        businessJunkRequirement.put("operate", StatusConstant.OPERATE_ADD);
        //保存旧货市场信息
        junkRequirementServiceDaoImpl.saveBusinessJunkRequirementInfo(businessJunkRequirement);

    }


    public IJunkRequirementServiceDao getJunkRequirementServiceDaoImpl() {
        return junkRequirementServiceDaoImpl;
    }

    public void setJunkRequirementServiceDaoImpl(IJunkRequirementServiceDao junkRequirementServiceDaoImpl) {
        this.junkRequirementServiceDaoImpl = junkRequirementServiceDaoImpl;
    }


}
