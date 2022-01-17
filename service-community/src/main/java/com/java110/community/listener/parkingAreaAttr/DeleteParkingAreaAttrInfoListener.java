package com.java110.community.listener.parkingAreaAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.parkingAreaAttr.ParkingAreaAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.community.dao.IParkingAreaAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除单元属性信息 侦听
 *
 * 处理节点
 * 1、businessParkingAreaAttr:{} 单元属性基本信息节点
 * 2、businessParkingAreaAttrAttr:[{}] 单元属性属性信息节点
 * 3、businessParkingAreaAttrPhoto:[{}] 单元属性照片信息节点
 * 4、businessParkingAreaAttrCerdentials:[{}] 单元属性证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteParkingAreaAttrInfoListener")
@Transactional
public class DeleteParkingAreaAttrInfoListener extends AbstractParkingAreaAttrBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteParkingAreaAttrInfoListener.class);
    @Autowired
    IParkingAreaAttrServiceDao parkingAreaAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_PARKING_AREA_ATTR;
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

            //处理 businessParkingAreaAttr 节点
            if(data.containsKey(ParkingAreaAttrPo.class.getSimpleName())){
                Object _obj = data.get(ParkingAreaAttrPo.class.getSimpleName());
                JSONArray businessParkingAreaAttrs = null;
                if(_obj instanceof JSONObject){
                    businessParkingAreaAttrs = new JSONArray();
                    businessParkingAreaAttrs.add(_obj);
                }else {
                    businessParkingAreaAttrs = (JSONArray)_obj;
                }
                //JSONObject businessParkingAreaAttr = data.getJSONObject(ParkingAreaAttrPo.class.getSimpleName());
                for (int _parkingAreaAttrIndex = 0; _parkingAreaAttrIndex < businessParkingAreaAttrs.size();_parkingAreaAttrIndex++) {
                    JSONObject businessParkingAreaAttr = businessParkingAreaAttrs.getJSONObject(_parkingAreaAttrIndex);
                    doBusinessParkingAreaAttr(business, businessParkingAreaAttr);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("attrId", businessParkingAreaAttr.getString("attrId"));
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

        //单元属性信息
        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_DEL);

        //单元属性信息
        List<Map> businessParkingAreaAttrInfos = parkingAreaAttrServiceDaoImpl.getBusinessParkingAreaAttrInfo(info);
        if( businessParkingAreaAttrInfos != null && businessParkingAreaAttrInfos.size() >0) {
            for (int _parkingAreaAttrIndex = 0; _parkingAreaAttrIndex < businessParkingAreaAttrInfos.size();_parkingAreaAttrIndex++) {
                Map businessParkingAreaAttrInfo = businessParkingAreaAttrInfos.get(_parkingAreaAttrIndex);
                flushBusinessParkingAreaAttrInfo(businessParkingAreaAttrInfo,StatusConstant.STATUS_CD_INVALID);
                parkingAreaAttrServiceDaoImpl.updateParkingAreaAttrInfoInstance(businessParkingAreaAttrInfo);
                dataFlowContext.addParamOut("attrId",businessParkingAreaAttrInfo.get("attr_id"));
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
        //单元属性信息
        List<Map> parkingAreaAttrInfo = parkingAreaAttrServiceDaoImpl.getParkingAreaAttrInfo(info);
        if(parkingAreaAttrInfo != null && parkingAreaAttrInfo.size() > 0){

            //单元属性信息
            List<Map> businessParkingAreaAttrInfos = parkingAreaAttrServiceDaoImpl.getBusinessParkingAreaAttrInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessParkingAreaAttrInfos == null ||  businessParkingAreaAttrInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（parkingAreaAttr），程序内部异常,请检查！ "+delInfo);
            }
            for (int _parkingAreaAttrIndex = 0; _parkingAreaAttrIndex < businessParkingAreaAttrInfos.size();_parkingAreaAttrIndex++) {
                Map businessParkingAreaAttrInfo = businessParkingAreaAttrInfos.get(_parkingAreaAttrIndex);
                flushBusinessParkingAreaAttrInfo(businessParkingAreaAttrInfo,StatusConstant.STATUS_CD_VALID);
                parkingAreaAttrServiceDaoImpl.updateParkingAreaAttrInfoInstance(businessParkingAreaAttrInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"attrId 错误，不能自动生成（必须已经存在的attrId）"+businessParkingAreaAttr);
        }
        //自动插入DEL
        autoSaveDelBusinessParkingAreaAttr(business,businessParkingAreaAttr);
    }
    @Override
    public IParkingAreaAttrServiceDao getParkingAreaAttrServiceDaoImpl() {
        return parkingAreaAttrServiceDaoImpl;
    }

    public void setParkingAreaAttrServiceDaoImpl(IParkingAreaAttrServiceDao parkingAreaAttrServiceDaoImpl) {
        this.parkingAreaAttrServiceDaoImpl = parkingAreaAttrServiceDaoImpl;
    }
}
