package com.java110.community.listener.parkingSpaceAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.parkingSpaceAttr.ParkingSpaceAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IParkingSpaceAttrServiceDao;
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
 * 保存 车位属性信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveParkingSpaceAttrInfoListener")
@Transactional
public class SaveParkingSpaceAttrInfoListener extends AbstractParkingSpaceAttrBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveParkingSpaceAttrInfoListener.class);

    @Autowired
    private IParkingSpaceAttrServiceDao parkingSpaceAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PARKING_SPACE_ATTR;
    }

    /**
     * 保存车位属性信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessParkingSpaceAttr 节点
        if(data.containsKey(ParkingSpaceAttrPo.class.getSimpleName())){
            Object bObj = data.get(ParkingSpaceAttrPo.class.getSimpleName());
            JSONArray businessParkingSpaceAttrs = null;
            if(bObj instanceof JSONObject){
                businessParkingSpaceAttrs = new JSONArray();
                businessParkingSpaceAttrs.add(bObj);
            }else {
                businessParkingSpaceAttrs = (JSONArray)bObj;
            }
            //JSONObject businessParkingSpaceAttr = data.getJSONObject(ParkingSpaceAttrPo.class.getSimpleName());
            for (int bParkingSpaceAttrIndex = 0; bParkingSpaceAttrIndex < businessParkingSpaceAttrs.size();bParkingSpaceAttrIndex++) {
                JSONObject businessParkingSpaceAttr = businessParkingSpaceAttrs.getJSONObject(bParkingSpaceAttrIndex);
                doBusinessParkingSpaceAttr(business, businessParkingSpaceAttr);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessParkingSpaceAttr.getString("attrId"));
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

        //车位属性信息
        List<Map> businessParkingSpaceAttrInfo = parkingSpaceAttrServiceDaoImpl.getBusinessParkingSpaceAttrInfo(info);
        if( businessParkingSpaceAttrInfo != null && businessParkingSpaceAttrInfo.size() >0) {
            reFreshShareColumn(info, businessParkingSpaceAttrInfo.get(0));
            parkingSpaceAttrServiceDaoImpl.saveParkingSpaceAttrInfoInstance(info);
            if(businessParkingSpaceAttrInfo.size() == 1) {
                dataFlowContext.addParamOut("attrId", businessParkingSpaceAttrInfo.get(0).get("attr_id"));
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
        //车位属性信息
        List<Map> parkingSpaceAttrInfo = parkingSpaceAttrServiceDaoImpl.getParkingSpaceAttrInfo(info);
        if(parkingSpaceAttrInfo != null && parkingSpaceAttrInfo.size() > 0){
            reFreshShareColumn(paramIn, parkingSpaceAttrInfo.get(0));
            parkingSpaceAttrServiceDaoImpl.updateParkingSpaceAttrInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessParkingSpaceAttr 节点
     * @param business 总的数据节点
     * @param businessParkingSpaceAttr 车位属性节点
     */
    private void doBusinessParkingSpaceAttr(Business business,JSONObject businessParkingSpaceAttr){

        Assert.jsonObjectHaveKey(businessParkingSpaceAttr,"attrId","businessParkingSpaceAttr 节点下没有包含 attrId 节点");

        if(businessParkingSpaceAttr.getString("attrId").startsWith("-")){
            //刷新缓存
            //flushParkingSpaceAttrId(business.getDatas());

            businessParkingSpaceAttr.put("attrId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));

        }

        businessParkingSpaceAttr.put("bId",business.getbId());
        businessParkingSpaceAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存车位属性信息
        parkingSpaceAttrServiceDaoImpl.saveBusinessParkingSpaceAttrInfo(businessParkingSpaceAttr);

    }
    @Override
    public IParkingSpaceAttrServiceDao getParkingSpaceAttrServiceDaoImpl() {
        return parkingSpaceAttrServiceDaoImpl;
    }

    public void setParkingSpaceAttrServiceDaoImpl(IParkingSpaceAttrServiceDao parkingSpaceAttrServiceDaoImpl) {
        this.parkingSpaceAttrServiceDaoImpl = parkingSpaceAttrServiceDaoImpl;
    }
}
