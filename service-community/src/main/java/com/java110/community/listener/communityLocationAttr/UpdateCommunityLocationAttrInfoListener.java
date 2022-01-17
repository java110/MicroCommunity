package com.java110.community.listener.communityLocationAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.ICommunityLocationAttrServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.communityLocationAttr.CommunityLocationAttrPo;
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
 * 修改位置属性信息 侦听
 * <p>
 * 处理节点
 * 1、businessCommunityLocationAttr:{} 位置属性基本信息节点
 * 2、businessCommunityLocationAttrAttr:[{}] 位置属性属性信息节点
 * 3、businessCommunityLocationAttrPhoto:[{}] 位置属性照片信息节点
 * 4、businessCommunityLocationAttrCerdentials:[{}] 位置属性证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateCommunityLocationAttrInfoListener")
@Transactional
public class UpdateCommunityLocationAttrInfoListener extends AbstractCommunityLocationAttrBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateCommunityLocationAttrInfoListener.class);
    @Autowired
    private ICommunityLocationAttrServiceDao communityLocationAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_LOCATION_ATTR;
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


        //处理 businessCommunityLocationAttr 节点
        if (data.containsKey(CommunityLocationAttrPo.class.getSimpleName())) {
            Object _obj = data.get(CommunityLocationAttrPo.class.getSimpleName());
            JSONArray businessCommunityLocationAttrs = null;
            if (_obj instanceof JSONObject) {
                businessCommunityLocationAttrs = new JSONArray();
                businessCommunityLocationAttrs.add(_obj);
            } else {
                businessCommunityLocationAttrs = (JSONArray) _obj;
            }
            //JSONObject businessCommunityLocationAttr = data.getJSONObject(CommunityLocationAttrPo.class.getSimpleName());
            for (int _communityLocationAttrIndex = 0; _communityLocationAttrIndex < businessCommunityLocationAttrs.size(); _communityLocationAttrIndex++) {
                JSONObject businessCommunityLocationAttr = businessCommunityLocationAttrs.getJSONObject(_communityLocationAttrIndex);
                doBusinessCommunityLocationAttr(business, businessCommunityLocationAttr);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessCommunityLocationAttr.getString("attrId"));
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

        //位置属性信息
        List<Map> businessCommunityLocationAttrInfos = communityLocationAttrServiceDaoImpl.getBusinessCommunityLocationAttrInfo(info);
        if (businessCommunityLocationAttrInfos != null && businessCommunityLocationAttrInfos.size() > 0) {
            for (int _communityLocationAttrIndex = 0; _communityLocationAttrIndex < businessCommunityLocationAttrInfos.size(); _communityLocationAttrIndex++) {
                Map businessCommunityLocationAttrInfo = businessCommunityLocationAttrInfos.get(_communityLocationAttrIndex);
                flushBusinessCommunityLocationAttrInfo(businessCommunityLocationAttrInfo, StatusConstant.STATUS_CD_VALID);
                communityLocationAttrServiceDaoImpl.updateCommunityLocationAttrInfoInstance(businessCommunityLocationAttrInfo);
                if (businessCommunityLocationAttrInfo.size() == 1) {
                    dataFlowContext.addParamOut("attrId", businessCommunityLocationAttrInfo.get("attr_id"));
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
        //位置属性信息
        List<Map> communityLocationAttrInfo = communityLocationAttrServiceDaoImpl.getCommunityLocationAttrInfo(info);
        if (communityLocationAttrInfo != null && communityLocationAttrInfo.size() > 0) {

            //位置属性信息
            List<Map> businessCommunityLocationAttrInfos = communityLocationAttrServiceDaoImpl.getBusinessCommunityLocationAttrInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessCommunityLocationAttrInfos == null || businessCommunityLocationAttrInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（communityLocationAttr），程序内部异常,请检查！ " + delInfo);
            }
            for (int _communityLocationAttrIndex = 0; _communityLocationAttrIndex < businessCommunityLocationAttrInfos.size(); _communityLocationAttrIndex++) {
                Map businessCommunityLocationAttrInfo = businessCommunityLocationAttrInfos.get(_communityLocationAttrIndex);
                flushBusinessCommunityLocationAttrInfo(businessCommunityLocationAttrInfo, StatusConstant.STATUS_CD_VALID);
                communityLocationAttrServiceDaoImpl.updateCommunityLocationAttrInfoInstance(businessCommunityLocationAttrInfo);
            }
        }

    }


    /**
     * 处理 businessCommunityLocationAttr 节点
     *
     * @param business                      总的数据节点
     * @param businessCommunityLocationAttr 位置属性节点
     */
    private void doBusinessCommunityLocationAttr(Business business, JSONObject businessCommunityLocationAttr) {

        Assert.jsonObjectHaveKey(businessCommunityLocationAttr, "attrId", "businessCommunityLocationAttr 节点下没有包含 attrId 节点");

        if (businessCommunityLocationAttr.getString("attrId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "attrId 错误，不能自动生成（必须已经存在的attrId）" + businessCommunityLocationAttr);
        }
        //自动保存DEL
        autoSaveDelBusinessCommunityLocationAttr(business, businessCommunityLocationAttr);

        businessCommunityLocationAttr.put("bId", business.getbId());
        businessCommunityLocationAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存位置属性信息
        communityLocationAttrServiceDaoImpl.saveBusinessCommunityLocationAttrInfo(businessCommunityLocationAttr);

    }


    @Override
    public ICommunityLocationAttrServiceDao getCommunityLocationAttrServiceDaoImpl() {
        return communityLocationAttrServiceDaoImpl;
    }

    public void setCommunityLocationAttrServiceDaoImpl(ICommunityLocationAttrServiceDao communityLocationAttrServiceDaoImpl) {
        this.communityLocationAttrServiceDaoImpl = communityLocationAttrServiceDaoImpl;
    }


}
