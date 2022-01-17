package com.java110.community.listener.parkingAreaAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.parkingAreaAttr.ParkingAreaAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IParkingAreaAttrServiceDao;
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
 * 保存 单元属性信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveParkingAreaAttrInfoListener")
@Transactional
public class SaveParkingAreaAttrInfoListener extends AbstractParkingAreaAttrBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveParkingAreaAttrInfoListener.class);

    @Autowired
    private IParkingAreaAttrServiceDao parkingAreaAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PARKING_AREA_ATTR;
    }

    /**
     * 保存单元属性信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessParkingAreaAttr 节点
        if(data.containsKey(ParkingAreaAttrPo.class.getSimpleName())){
            Object bObj = data.get(ParkingAreaAttrPo.class.getSimpleName());
            JSONArray businessParkingAreaAttrs = null;
            if(bObj instanceof JSONObject){
                businessParkingAreaAttrs = new JSONArray();
                businessParkingAreaAttrs.add(bObj);
            }else {
                businessParkingAreaAttrs = (JSONArray)bObj;
            }
            //JSONObject businessParkingAreaAttr = data.getJSONObject(ParkingAreaAttrPo.class.getSimpleName());
            for (int bParkingAreaAttrIndex = 0; bParkingAreaAttrIndex < businessParkingAreaAttrs.size();bParkingAreaAttrIndex++) {
                JSONObject businessParkingAreaAttr = businessParkingAreaAttrs.getJSONObject(bParkingAreaAttrIndex);
                doBusinessParkingAreaAttr(business, businessParkingAreaAttr);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessParkingAreaAttr.getString("attrId"));
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

        //单元属性信息
        List<Map> businessParkingAreaAttrInfo = parkingAreaAttrServiceDaoImpl.getBusinessParkingAreaAttrInfo(info);
        if( businessParkingAreaAttrInfo != null && businessParkingAreaAttrInfo.size() >0) {
            reFreshShareColumn(info, businessParkingAreaAttrInfo.get(0));
            parkingAreaAttrServiceDaoImpl.saveParkingAreaAttrInfoInstance(info);
            if(businessParkingAreaAttrInfo.size() == 1) {
                dataFlowContext.addParamOut("attrId", businessParkingAreaAttrInfo.get(0).get("attr_id"));
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
        //单元属性信息
        List<Map> parkingAreaAttrInfo = parkingAreaAttrServiceDaoImpl.getParkingAreaAttrInfo(info);
        if(parkingAreaAttrInfo != null && parkingAreaAttrInfo.size() > 0){
            reFreshShareColumn(paramIn, parkingAreaAttrInfo.get(0));
            parkingAreaAttrServiceDaoImpl.updateParkingAreaAttrInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessParkingAreaAttr 节点
     * @param business 总的数据节点
     * @param businessParkingAreaAttr 单元属性节点
     */
    private void doBusinessParkingAreaAttr(Business business,JSONObject businessParkingAreaAttr){

        Assert.jsonObjectHaveKey(businessParkingAreaAttr,"attrId","businessParkingAreaAttr 节点下没有包含 attrId 节点");

        if(businessParkingAreaAttr.getString("attrId").startsWith("-")){
            //刷新缓存
            //flushParkingAreaAttrId(business.getDatas());

            businessParkingAreaAttr.put("attrId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));

        }

        businessParkingAreaAttr.put("bId",business.getbId());
        businessParkingAreaAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存单元属性信息
        parkingAreaAttrServiceDaoImpl.saveBusinessParkingAreaAttrInfo(businessParkingAreaAttr);

    }
    @Override
    public IParkingAreaAttrServiceDao getParkingAreaAttrServiceDaoImpl() {
        return parkingAreaAttrServiceDaoImpl;
    }

    public void setParkingAreaAttrServiceDaoImpl(IParkingAreaAttrServiceDao parkingAreaAttrServiceDaoImpl) {
        this.parkingAreaAttrServiceDaoImpl = parkingAreaAttrServiceDaoImpl;
    }
}
