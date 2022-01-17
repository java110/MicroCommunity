package com.java110.community.listener.parkingSpaceAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.parkingSpaceAttr.ParkingSpaceAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.community.dao.IParkingSpaceAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除车位属性信息 侦听
 *
 * 处理节点
 * 1、businessParkingSpaceAttr:{} 车位属性基本信息节点
 * 2、businessParkingSpaceAttrAttr:[{}] 车位属性属性信息节点
 * 3、businessParkingSpaceAttrPhoto:[{}] 车位属性照片信息节点
 * 4、businessParkingSpaceAttrCerdentials:[{}] 车位属性证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteParkingSpaceAttrInfoListener")
@Transactional
public class DeleteParkingSpaceAttrInfoListener extends AbstractParkingSpaceAttrBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteParkingSpaceAttrInfoListener.class);
    @Autowired
    IParkingSpaceAttrServiceDao parkingSpaceAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_PARKING_SPACE_ATTR;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

            //处理 businessParkingSpaceAttr 节点
            if(data.containsKey(ParkingSpaceAttrPo.class.getSimpleName())){
                Object _obj = data.get(ParkingSpaceAttrPo.class.getSimpleName());
                JSONArray businessParkingSpaceAttrs = null;
                if(_obj instanceof JSONObject){
                    businessParkingSpaceAttrs = new JSONArray();
                    businessParkingSpaceAttrs.add(_obj);
                }else {
                    businessParkingSpaceAttrs = (JSONArray)_obj;
                }
                //JSONObject businessParkingSpaceAttr = data.getJSONObject(ParkingSpaceAttrPo.class.getSimpleName());
                for (int _parkingSpaceAttrIndex = 0; _parkingSpaceAttrIndex < businessParkingSpaceAttrs.size();_parkingSpaceAttrIndex++) {
                    JSONObject businessParkingSpaceAttr = businessParkingSpaceAttrs.getJSONObject(_parkingSpaceAttrIndex);
                    doBusinessParkingSpaceAttr(business, businessParkingSpaceAttr);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("attrId", businessParkingSpaceAttr.getString("attrId"));
                    }
                }

        }


    }

    /**
     * 删除 instance数据
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");

        //车位属性信息
        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_DEL);

        //车位属性信息
        List<Map> businessParkingSpaceAttrInfos = parkingSpaceAttrServiceDaoImpl.getBusinessParkingSpaceAttrInfo(info);
        if( businessParkingSpaceAttrInfos != null && businessParkingSpaceAttrInfos.size() >0) {
            for (int _parkingSpaceAttrIndex = 0; _parkingSpaceAttrIndex < businessParkingSpaceAttrInfos.size();_parkingSpaceAttrIndex++) {
                Map businessParkingSpaceAttrInfo = businessParkingSpaceAttrInfos.get(_parkingSpaceAttrIndex);
                flushBusinessParkingSpaceAttrInfo(businessParkingSpaceAttrInfo,StatusConstant.STATUS_CD_INVALID);
                parkingSpaceAttrServiceDaoImpl.updateParkingSpaceAttrInfoInstance(businessParkingSpaceAttrInfo);
                dataFlowContext.addParamOut("attrId",businessParkingSpaceAttrInfo.get("attr_id"));
            }
        }

    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //车位属性信息
        List<Map> parkingSpaceAttrInfo = parkingSpaceAttrServiceDaoImpl.getParkingSpaceAttrInfo(info);
        if(parkingSpaceAttrInfo != null && parkingSpaceAttrInfo.size() > 0){

            //车位属性信息
            List<Map> businessParkingSpaceAttrInfos = parkingSpaceAttrServiceDaoImpl.getBusinessParkingSpaceAttrInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessParkingSpaceAttrInfos == null ||  businessParkingSpaceAttrInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（parkingSpaceAttr），程序内部异常,请检查！ "+delInfo);
            }
            for (int _parkingSpaceAttrIndex = 0; _parkingSpaceAttrIndex < businessParkingSpaceAttrInfos.size();_parkingSpaceAttrIndex++) {
                Map businessParkingSpaceAttrInfo = businessParkingSpaceAttrInfos.get(_parkingSpaceAttrIndex);
                flushBusinessParkingSpaceAttrInfo(businessParkingSpaceAttrInfo,StatusConstant.STATUS_CD_VALID);
                parkingSpaceAttrServiceDaoImpl.updateParkingSpaceAttrInfoInstance(businessParkingSpaceAttrInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"attrId 错误，不能自动生成（必须已经存在的attrId）"+businessParkingSpaceAttr);
        }
        //自动插入DEL
        autoSaveDelBusinessParkingSpaceAttr(business,businessParkingSpaceAttr);
    }
    @Override
    public IParkingSpaceAttrServiceDao getParkingSpaceAttrServiceDaoImpl() {
        return parkingSpaceAttrServiceDaoImpl;
    }

    public void setParkingSpaceAttrServiceDaoImpl(IParkingSpaceAttrServiceDao parkingSpaceAttrServiceDaoImpl) {
        this.parkingSpaceAttrServiceDaoImpl = parkingSpaceAttrServiceDaoImpl;
    }
}
