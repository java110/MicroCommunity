package com.java110.common.listener.attendanceClassesAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAttendanceClassesAttrServiceDao;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考勤班组属性 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractAttendanceClassesAttrBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractAttendanceClassesAttrBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IAttendanceClassesAttrServiceDao getAttendanceClassesAttrServiceDaoImpl();

    /**
     * 刷新 businessAttendanceClassesAttrInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessAttendanceClassesAttrInfo
     */
    protected void flushBusinessAttendanceClassesAttrInfo(Map businessAttendanceClassesAttrInfo, String statusCd) {
        businessAttendanceClassesAttrInfo.put("newBId", businessAttendanceClassesAttrInfo.get("b_id"));
        businessAttendanceClassesAttrInfo.put("classesId", businessAttendanceClassesAttrInfo.get("classes_id"));
        businessAttendanceClassesAttrInfo.put("attrId", businessAttendanceClassesAttrInfo.get("attr_id"));
        businessAttendanceClassesAttrInfo.put("operate", businessAttendanceClassesAttrInfo.get("operate"));
        businessAttendanceClassesAttrInfo.put("specCd", businessAttendanceClassesAttrInfo.get("spec_cd"));
        businessAttendanceClassesAttrInfo.put("storeId", businessAttendanceClassesAttrInfo.get("store_id"));
        businessAttendanceClassesAttrInfo.put("value", businessAttendanceClassesAttrInfo.get("value"));
        businessAttendanceClassesAttrInfo.remove("bId");
        businessAttendanceClassesAttrInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessAttendanceClassesAttr 考勤班组属性信息
     */
    protected void autoSaveDelBusinessAttendanceClassesAttr(Business business, JSONObject businessAttendanceClassesAttr) {
//自动插入DEL
        Map info = new HashMap();
        info.put("attrId", businessAttendanceClassesAttr.getString("attrId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentAttendanceClassesAttrInfos = getAttendanceClassesAttrServiceDaoImpl().getAttendanceClassesAttrInfo(info);
        if (currentAttendanceClassesAttrInfos == null || currentAttendanceClassesAttrInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentAttendanceClassesAttrInfo = currentAttendanceClassesAttrInfos.get(0);

        currentAttendanceClassesAttrInfo.put("bId", business.getbId());

        currentAttendanceClassesAttrInfo.put("classesId", currentAttendanceClassesAttrInfo.get("classes_id"));
        currentAttendanceClassesAttrInfo.put("attrId", currentAttendanceClassesAttrInfo.get("attr_id"));
        currentAttendanceClassesAttrInfo.put("operate", currentAttendanceClassesAttrInfo.get("operate"));
        currentAttendanceClassesAttrInfo.put("specCd", currentAttendanceClassesAttrInfo.get("spec_cd"));
        currentAttendanceClassesAttrInfo.put("storeId", currentAttendanceClassesAttrInfo.get("store_id"));
        currentAttendanceClassesAttrInfo.put("value", currentAttendanceClassesAttrInfo.get("value"));


        currentAttendanceClassesAttrInfo.put("operate", StatusConstant.OPERATE_DEL);
        getAttendanceClassesAttrServiceDaoImpl().saveBusinessAttendanceClassesAttrInfo(currentAttendanceClassesAttrInfo);
        for (Object key : currentAttendanceClassesAttrInfo.keySet()) {
            if (businessAttendanceClassesAttr.get(key) == null) {
                businessAttendanceClassesAttr.put(key.toString(), currentAttendanceClassesAttrInfo.get(key));
            }
        }
    }


}
