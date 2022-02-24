package com.java110.community.listener.floorAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.floorAttr.FloorAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IFloorAttrServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 考勤班组属性信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveFloorAttrInfoListener")
@Transactional
public class SaveFloorAttrInfoListener extends AbstractFloorAttrBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveFloorAttrInfoListener.class);

    @Autowired
    private IFloorAttrServiceDao floorAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_FLOOR_ATTR_INFO;
    }

    /**
     * 保存考勤班组属性信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessFloorAttr 节点
        if(data.containsKey(FloorAttrPo.class.getSimpleName())){
            Object bObj = data.get(FloorAttrPo.class.getSimpleName());
            JSONArray businessFloorAttrs = null;
            if(bObj instanceof JSONObject){
                businessFloorAttrs = new JSONArray();
                businessFloorAttrs.add(bObj);
            }else {
                businessFloorAttrs = (JSONArray)bObj;
            }
            //JSONObject businessFloorAttr = data.getJSONObject(FloorAttrPo.class.getSimpleName());
            for (int bFloorAttrIndex = 0; bFloorAttrIndex < businessFloorAttrs.size();bFloorAttrIndex++) {
                JSONObject businessFloorAttr = businessFloorAttrs.getJSONObject(bFloorAttrIndex);
                doBusinessFloorAttr(business, businessFloorAttr);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessFloorAttr.getString("attrId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //考勤班组属性信息
        List<Map> businessFloorAttrInfo = floorAttrServiceDaoImpl.getBusinessFloorAttrInfo(info);
        if( businessFloorAttrInfo != null && businessFloorAttrInfo.size() >0) {
            reFreshShareColumn(info, businessFloorAttrInfo.get(0));
            floorAttrServiceDaoImpl.saveFloorAttrInfoInstance(info);
            if(businessFloorAttrInfo.size() == 1) {
                dataFlowContext.addParamOut("attrId", businessFloorAttrInfo.get(0).get("attr_id"));
            }
        }
    }


    /**
     * 刷 分片字段
     *
     * @param info         查询对象
     * @param businessInfo 小区ID
     */
    private void reFreshShareColumn(Map info, Map businessInfo) {

        if (info.containsKey("communityId")) {
            return;
        }

        if (!businessInfo.containsKey("community_id")) {
            return;
        }

        info.put("communityId", businessInfo.get("community_id"));
    }
    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        //考勤班组属性信息
        List<Map> floorAttrInfo = floorAttrServiceDaoImpl.getFloorAttrInfo(info);
        if(floorAttrInfo != null && floorAttrInfo.size() > 0){
            reFreshShareColumn(paramIn, floorAttrInfo.get(0));
            floorAttrServiceDaoImpl.updateFloorAttrInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessFloorAttr 节点
     * @param business 总的数据节点
     * @param businessFloorAttr 考勤班组属性节点
     */
    private void doBusinessFloorAttr(Business business,JSONObject businessFloorAttr){

        Assert.jsonObjectHaveKey(businessFloorAttr,"attrId","businessFloorAttr 节点下没有包含 attrId 节点");

        if(businessFloorAttr.getString("attrId").startsWith("-")){
            //刷新缓存
            //flushFloorAttrId(business.getDatas());

            businessFloorAttr.put("attrId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));

        }

        businessFloorAttr.put("bId",business.getbId());
        businessFloorAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存考勤班组属性信息
        floorAttrServiceDaoImpl.saveBusinessFloorAttrInfo(businessFloorAttr);

    }
    @Override
    public IFloorAttrServiceDao getFloorAttrServiceDaoImpl() {
        return floorAttrServiceDaoImpl;
    }

    public void setFloorAttrServiceDaoImpl(IFloorAttrServiceDao floorAttrServiceDaoImpl) {
        this.floorAttrServiceDaoImpl = floorAttrServiceDaoImpl;
    }
}
