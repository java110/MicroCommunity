package com.java110.community.listener.floorAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.floorAttr.FloorAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.community.dao.IFloorAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除考勤班组属性信息 侦听
 *
 * 处理节点
 * 1、businessFloorAttr:{} 考勤班组属性基本信息节点
 * 2、businessFloorAttrAttr:[{}] 考勤班组属性属性信息节点
 * 3、businessFloorAttrPhoto:[{}] 考勤班组属性照片信息节点
 * 4、businessFloorAttrCerdentials:[{}] 考勤班组属性证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteFloorAttrInfoListener")
@Transactional
public class DeleteFloorAttrInfoListener extends AbstractFloorAttrBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteFloorAttrInfoListener.class);
    @Autowired
    IFloorAttrServiceDao floorAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_FLOOR_ATTR_INFO;
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

            //处理 businessFloorAttr 节点
            if(data.containsKey(FloorAttrPo.class.getSimpleName())){
                Object _obj = data.get(FloorAttrPo.class.getSimpleName());
                JSONArray businessFloorAttrs = null;
                if(_obj instanceof JSONObject){
                    businessFloorAttrs = new JSONArray();
                    businessFloorAttrs.add(_obj);
                }else {
                    businessFloorAttrs = (JSONArray)_obj;
                }
                //JSONObject businessFloorAttr = data.getJSONObject(FloorAttrPo.class.getSimpleName());
                for (int _floorAttrIndex = 0; _floorAttrIndex < businessFloorAttrs.size();_floorAttrIndex++) {
                    JSONObject businessFloorAttr = businessFloorAttrs.getJSONObject(_floorAttrIndex);
                    doBusinessFloorAttr(business, businessFloorAttr);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("attrId", businessFloorAttr.getString("attrId"));
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

        //考勤班组属性信息
        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_DEL);

        //考勤班组属性信息
        List<Map> businessFloorAttrInfos = floorAttrServiceDaoImpl.getBusinessFloorAttrInfo(info);
        if( businessFloorAttrInfos != null && businessFloorAttrInfos.size() >0) {
            for (int _floorAttrIndex = 0; _floorAttrIndex < businessFloorAttrInfos.size();_floorAttrIndex++) {
                Map businessFloorAttrInfo = businessFloorAttrInfos.get(_floorAttrIndex);
                flushBusinessFloorAttrInfo(businessFloorAttrInfo,StatusConstant.STATUS_CD_INVALID);
                floorAttrServiceDaoImpl.updateFloorAttrInfoInstance(businessFloorAttrInfo);
                dataFlowContext.addParamOut("attrId",businessFloorAttrInfo.get("attr_id"));
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
        //考勤班组属性信息
        List<Map> floorAttrInfo = floorAttrServiceDaoImpl.getFloorAttrInfo(info);
        if(floorAttrInfo != null && floorAttrInfo.size() > 0){

            //考勤班组属性信息
            List<Map> businessFloorAttrInfos = floorAttrServiceDaoImpl.getBusinessFloorAttrInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessFloorAttrInfos == null ||  businessFloorAttrInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（floorAttr），程序内部异常,请检查！ "+delInfo);
            }
            for (int _floorAttrIndex = 0; _floorAttrIndex < businessFloorAttrInfos.size();_floorAttrIndex++) {
                Map businessFloorAttrInfo = businessFloorAttrInfos.get(_floorAttrIndex);
                flushBusinessFloorAttrInfo(businessFloorAttrInfo,StatusConstant.STATUS_CD_VALID);
                floorAttrServiceDaoImpl.updateFloorAttrInfoInstance(businessFloorAttrInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"attrId 错误，不能自动生成（必须已经存在的attrId）"+businessFloorAttr);
        }
        //自动插入DEL
        autoSaveDelBusinessFloorAttr(business,businessFloorAttr);
    }
    @Override
    public IFloorAttrServiceDao getFloorAttrServiceDaoImpl() {
        return floorAttrServiceDaoImpl;
    }

    public void setFloorAttrServiceDaoImpl(IFloorAttrServiceDao floorAttrServiceDaoImpl) {
        this.floorAttrServiceDaoImpl = floorAttrServiceDaoImpl;
    }
}
