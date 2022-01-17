package com.java110.common.listener.attendanceClassesAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.attendanceClassesAttr.AttendanceClassesAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.common.dao.IAttendanceClassesAttrServiceDao;
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
@Java110Listener("saveAttendanceClassesAttrInfoListener")
@Transactional
public class SaveAttendanceClassesAttrInfoListener extends AbstractAttendanceClassesAttrBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveAttendanceClassesAttrInfoListener.class);

    @Autowired
    private IAttendanceClassesAttrServiceDao attendanceClassesAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_ATTENDANCE_CLASSES_ATTR;
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

        //处理 businessAttendanceClassesAttr 节点
        if(data.containsKey(AttendanceClassesAttrPo.class.getSimpleName())){
            Object bObj = data.get(AttendanceClassesAttrPo.class.getSimpleName());
            JSONArray businessAttendanceClassesAttrs = null;
            if(bObj instanceof JSONObject){
                businessAttendanceClassesAttrs = new JSONArray();
                businessAttendanceClassesAttrs.add(bObj);
            }else {
                businessAttendanceClassesAttrs = (JSONArray)bObj;
            }
            //JSONObject businessAttendanceClassesAttr = data.getJSONObject(AttendanceClassesAttrPo.class.getSimpleName());
            for (int bAttendanceClassesAttrIndex = 0; bAttendanceClassesAttrIndex < businessAttendanceClassesAttrs.size();bAttendanceClassesAttrIndex++) {
                JSONObject businessAttendanceClassesAttr = businessAttendanceClassesAttrs.getJSONObject(bAttendanceClassesAttrIndex);
                doBusinessAttendanceClassesAttr(business, businessAttendanceClassesAttr);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessAttendanceClassesAttr.getString("attrId"));
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
        List<Map> businessAttendanceClassesAttrInfo = attendanceClassesAttrServiceDaoImpl.getBusinessAttendanceClassesAttrInfo(info);
        if( businessAttendanceClassesAttrInfo != null && businessAttendanceClassesAttrInfo.size() >0) {
            reFreshShareColumn(info, businessAttendanceClassesAttrInfo.get(0));
            attendanceClassesAttrServiceDaoImpl.saveAttendanceClassesAttrInfoInstance(info);
            if(businessAttendanceClassesAttrInfo.size() == 1) {
                dataFlowContext.addParamOut("attrId", businessAttendanceClassesAttrInfo.get(0).get("attr_id"));
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

        if (info.containsKey("storeId")) {
            return;
        }

        if (!businessInfo.containsKey("store_id")) {
            return;
        }

        info.put("storeId", businessInfo.get("store_id"));
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
        List<Map> attendanceClassesAttrInfo = attendanceClassesAttrServiceDaoImpl.getAttendanceClassesAttrInfo(info);
        if(attendanceClassesAttrInfo != null && attendanceClassesAttrInfo.size() > 0){
            reFreshShareColumn(paramIn, attendanceClassesAttrInfo.get(0));
            attendanceClassesAttrServiceDaoImpl.updateAttendanceClassesAttrInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessAttendanceClassesAttr 节点
     * @param business 总的数据节点
     * @param businessAttendanceClassesAttr 考勤班组属性节点
     */
    private void doBusinessAttendanceClassesAttr(Business business,JSONObject businessAttendanceClassesAttr){

        Assert.jsonObjectHaveKey(businessAttendanceClassesAttr,"attrId","businessAttendanceClassesAttr 节点下没有包含 attrId 节点");

        if(businessAttendanceClassesAttr.getString("attrId").startsWith("-")){
            //刷新缓存
            //flushAttendanceClassesAttrId(business.getDatas());

            businessAttendanceClassesAttr.put("attrId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));

        }

        businessAttendanceClassesAttr.put("bId",business.getbId());
        businessAttendanceClassesAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存考勤班组属性信息
        attendanceClassesAttrServiceDaoImpl.saveBusinessAttendanceClassesAttrInfo(businessAttendanceClassesAttr);

    }
    @Override
    public IAttendanceClassesAttrServiceDao getAttendanceClassesAttrServiceDaoImpl() {
        return attendanceClassesAttrServiceDaoImpl;
    }

    public void setAttendanceClassesAttrServiceDaoImpl(IAttendanceClassesAttrServiceDao attendanceClassesAttrServiceDaoImpl) {
        this.attendanceClassesAttrServiceDaoImpl = attendanceClassesAttrServiceDaoImpl;
    }
}
