package com.java110.common.listener.attendanceClasses;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.attendanceClasses.AttendanceClassesPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.common.dao.IAttendanceClassesServiceDao;
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
 * 保存 考勤班次信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveAttendanceClassesInfoListener")
@Transactional
public class SaveAttendanceClassesInfoListener extends AbstractAttendanceClassesBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveAttendanceClassesInfoListener.class);

    @Autowired
    private IAttendanceClassesServiceDao attendanceClassesServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_ATTENDANCE_CLASSES;
    }

    /**
     * 保存考勤班次信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessAttendanceClasses 节点
        if(data.containsKey(AttendanceClassesPo.class.getSimpleName())){
            Object bObj = data.get(AttendanceClassesPo.class.getSimpleName());
            JSONArray businessAttendanceClassess = null;
            if(bObj instanceof JSONObject){
                businessAttendanceClassess = new JSONArray();
                businessAttendanceClassess.add(bObj);
            }else {
                businessAttendanceClassess = (JSONArray)bObj;
            }
            //JSONObject businessAttendanceClasses = data.getJSONObject(AttendanceClassesPo.class.getSimpleName());
            for (int bAttendanceClassesIndex = 0; bAttendanceClassesIndex < businessAttendanceClassess.size();bAttendanceClassesIndex++) {
                JSONObject businessAttendanceClasses = businessAttendanceClassess.getJSONObject(bAttendanceClassesIndex);
                doBusinessAttendanceClasses(business, businessAttendanceClasses);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("classesId", businessAttendanceClasses.getString("classesId"));
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

        //考勤班次信息
        List<Map> businessAttendanceClassesInfo = attendanceClassesServiceDaoImpl.getBusinessAttendanceClassesInfo(info);
        if( businessAttendanceClassesInfo != null && businessAttendanceClassesInfo.size() >0) {
            reFreshShareColumn(info, businessAttendanceClassesInfo.get(0));
            attendanceClassesServiceDaoImpl.saveAttendanceClassesInfoInstance(info);
            if(businessAttendanceClassesInfo.size() == 1) {
                dataFlowContext.addParamOut("classesId", businessAttendanceClassesInfo.get(0).get("classes_id"));
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

        if (info.containsKey("store_id")) {
            return;
        }

        if (!businessInfo.containsKey("storeId")) {
            return;
        }

        info.put("store_id", businessInfo.get("storeId"));
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
        //考勤班次信息
        List<Map> attendanceClassesInfo = attendanceClassesServiceDaoImpl.getAttendanceClassesInfo(info);
        if(attendanceClassesInfo != null && attendanceClassesInfo.size() > 0){
            reFreshShareColumn(paramIn, attendanceClassesInfo.get(0));
            attendanceClassesServiceDaoImpl.updateAttendanceClassesInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessAttendanceClasses 节点
     * @param business 总的数据节点
     * @param businessAttendanceClasses 考勤班次节点
     */
    private void doBusinessAttendanceClasses(Business business,JSONObject businessAttendanceClasses){

        Assert.jsonObjectHaveKey(businessAttendanceClasses,"classesId","businessAttendanceClasses 节点下没有包含 classesId 节点");

        if(businessAttendanceClasses.getString("classesId").startsWith("-")){
            //刷新缓存
            //flushAttendanceClassesId(business.getDatas());

            businessAttendanceClasses.put("classesId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_classesId));

        }

        businessAttendanceClasses.put("bId",business.getbId());
        businessAttendanceClasses.put("operate", StatusConstant.OPERATE_ADD);
        //保存考勤班次信息
        attendanceClassesServiceDaoImpl.saveBusinessAttendanceClassesInfo(businessAttendanceClasses);

    }
    @Override
    public IAttendanceClassesServiceDao getAttendanceClassesServiceDaoImpl() {
        return attendanceClassesServiceDaoImpl;
    }

    public void setAttendanceClassesServiceDaoImpl(IAttendanceClassesServiceDao attendanceClassesServiceDaoImpl) {
        this.attendanceClassesServiceDaoImpl = attendanceClassesServiceDaoImpl;
    }
}
