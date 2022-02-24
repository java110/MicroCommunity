
package com.java110.community.listener.parkingSpace;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IParkingSpaceServiceDao;
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
 * 保存 停车位信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveParkingSpaceInfoListener")
@Transactional
public class SaveParkingSpaceInfoListener extends AbstractParkingSpaceBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveParkingSpaceInfoListener.class);

    @Autowired
    private IParkingSpaceServiceDao parkingSpaceServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PARKING_SPACE;
    }

    /**
     * 保存停车位信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessParkingSpace 节点
        if (data.containsKey(ParkingSpacePo.class.getSimpleName())) {
            Object bObj = data.get(ParkingSpacePo.class.getSimpleName());
            JSONArray businessParkingSpaces = null;
            if (bObj instanceof JSONObject) {
                businessParkingSpaces = new JSONArray();
                businessParkingSpaces.add(bObj);
            } else {
                businessParkingSpaces = (JSONArray) bObj;
            }
            //JSONObject businessParkingSpace = data.getJSONObject("businessParkingSpace");
            for (int bParkingSpaceIndex = 0; bParkingSpaceIndex < businessParkingSpaces.size(); bParkingSpaceIndex++) {
                JSONObject businessParkingSpace = businessParkingSpaces.getJSONObject(bParkingSpaceIndex);
                doBusinessParkingSpace(business, businessParkingSpace);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("psId", businessParkingSpace.getString("psId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
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

        //停车位信息
        List<Map> businessParkingSpaceInfo = parkingSpaceServiceDaoImpl.getBusinessParkingSpaceInfo(info);
        if (businessParkingSpaceInfo != null && businessParkingSpaceInfo.size() > 0) {
            reFreshShareColumn(info, businessParkingSpaceInfo.get(0));
            parkingSpaceServiceDaoImpl.saveParkingSpaceInfoInstance(info);
            if (businessParkingSpaceInfo.size() == 1) {
                dataFlowContext.addParamOut("psId", businessParkingSpaceInfo.get(0).get("ps_id"));
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
        Map paramIn = new HashMap();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        //停车位信息
        List<Map> parkingSpaceInfo = parkingSpaceServiceDaoImpl.getParkingSpaceInfo(info);
        if (parkingSpaceInfo != null && parkingSpaceInfo.size() > 0) {
            reFreshShareColumn(paramIn, parkingSpaceInfo.get(0));
            parkingSpaceServiceDaoImpl.updateParkingSpaceInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessParkingSpace 节点
     *
     * @param business             总的数据节点
     * @param businessParkingSpace 停车位节点
     */
    private void doBusinessParkingSpace(Business business, JSONObject businessParkingSpace) {

        Assert.jsonObjectHaveKey(businessParkingSpace, "psId", "businessParkingSpace 节点下没有包含 psId 节点");

        if (businessParkingSpace.getString("psId").startsWith("-")) {
            //刷新缓存
            //flushParkingSpaceId(business.getDatas());

            businessParkingSpace.put("psId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_psId));

        }

        businessParkingSpace.put("bId", business.getbId());
        businessParkingSpace.put("operate", StatusConstant.OPERATE_ADD);
        //保存停车位信息
        parkingSpaceServiceDaoImpl.saveBusinessParkingSpaceInfo(businessParkingSpace);

    }

    public IParkingSpaceServiceDao getParkingSpaceServiceDaoImpl() {
        return parkingSpaceServiceDaoImpl;
    }

    public void setParkingSpaceServiceDaoImpl(IParkingSpaceServiceDao parkingSpaceServiceDaoImpl) {
        this.parkingSpaceServiceDaoImpl = parkingSpaceServiceDaoImpl;
    }
}
