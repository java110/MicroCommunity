package com.java110.community.listener.floor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.floor.FloorPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IFloorServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改小区楼信息 侦听
 * <p>
 * 处理节点
 * 1、businessFloor:{} 小区楼基本信息节点
 * 2、businessFloorAttr:[{}] 小区楼属性信息节点
 * 3、businessFloorPhoto:[{}] 小区楼照片信息节点
 * 4、businessFloorCerdentials:[{}] 小区楼证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateFloorInfoListener")
@Transactional
public class UpdateFloorInfoListener extends AbstractFloorBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateFloorInfoListener.class);
    @Autowired
    IFloorServiceDao floorServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FLOOR_INFO;
    }

    /**
     * business过程
     *
     * @param dataFlowContext
     * @param business
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessFloor 节点
        if (data.containsKey(FloorPo.class.getSimpleName())) {
            Object _obj = data.get(FloorPo.class.getSimpleName());
            JSONArray businessFloors = null;
            if (_obj instanceof JSONObject) {
                businessFloors = new JSONArray();
                businessFloors.add(_obj);
            } else {
                businessFloors = (JSONArray) _obj;
            }
            //JSONObject businessFloor = data.getJSONObject("businessFloor");
            for (int _floorIndex = 0; _floorIndex < businessFloors.size(); _floorIndex++) {
                JSONObject businessFloor = businessFloors.getJSONObject(_floorIndex);
                doBusinessFloor(business, businessFloor);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("floorId", businessFloor.getString("floorId"));
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

        //小区楼信息
        List<Map> businessFloorInfos = floorServiceDaoImpl.getBusinessFloorInfo(info);
        if (businessFloorInfos != null && businessFloorInfos.size() > 0) {
            for (int _floorIndex = 0; _floorIndex < businessFloorInfos.size(); _floorIndex++) {
                Map businessFloorInfo = businessFloorInfos.get(_floorIndex);
                flushBusinessFloorInfo(businessFloorInfo, StatusConstant.STATUS_CD_VALID);
                floorServiceDaoImpl.updateFloorInfoInstance(businessFloorInfo);
                if (businessFloorInfo.size() == 1) {
                    dataFlowContext.addParamOut("floorId", businessFloorInfo.get("floor_id"));
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
        //小区楼信息
        List<Map> floorInfo = floorServiceDaoImpl.getFloorInfo(info);
        if (floorInfo != null && floorInfo.size() > 0) {

            //小区楼信息
            List<Map> businessFloorInfos = floorServiceDaoImpl.getBusinessFloorInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessFloorInfos == null || businessFloorInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（floor），程序内部异常,请检查！ " + delInfo);
            }
            for (int _floorIndex = 0; _floorIndex < businessFloorInfos.size(); _floorIndex++) {
                Map businessFloorInfo = businessFloorInfos.get(_floorIndex);
                flushBusinessFloorInfo(businessFloorInfo, StatusConstant.STATUS_CD_VALID);
                floorServiceDaoImpl.updateFloorInfoInstance(businessFloorInfo);
            }
        }

    }


    /**
     * 处理 businessFloor 节点
     *
     * @param business      总的数据节点
     * @param businessFloor 小区楼节点
     */
    private void doBusinessFloor(Business business, JSONObject businessFloor) {

        Assert.jsonObjectHaveKey(businessFloor, "floorId", "businessFloor 节点下没有包含 floorId 节点");

        if (businessFloor.getString("floorId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "floorId 错误，不能自动生成（必须已经存在的floorId）" + businessFloor);
        }
        //自动保存DEL
        autoSaveDelBusinessFloor(business, businessFloor);

        businessFloor.put("bId", business.getbId());
        businessFloor.put("operate", StatusConstant.OPERATE_ADD);
        //保存小区楼信息
        floorServiceDaoImpl.saveBusinessFloorInfo(businessFloor);

    }


    public IFloorServiceDao getFloorServiceDaoImpl() {
        return floorServiceDaoImpl;
    }

    public void setFloorServiceDaoImpl(IFloorServiceDao floorServiceDaoImpl) {
        this.floorServiceDaoImpl = floorServiceDaoImpl;
    }


}
