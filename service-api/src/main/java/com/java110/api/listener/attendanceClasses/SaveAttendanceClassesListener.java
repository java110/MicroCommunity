package com.java110.api.listener.attendanceClasses;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.attendanceClasses.IAttendanceClassesBMO;
import com.java110.api.bmo.attendanceClassesAttr.IAttendanceClassesAttrBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeAttendanceClassesConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveAttendanceClassesListener")
public class SaveAttendanceClassesListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IAttendanceClassesBMO attendanceClassesBMOImpl;

    @Autowired
    private IAttendanceClassesAttrBMO attendanceClassesAttrBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "classesName", "请求报文中未包含classesName");
        Assert.hasKeyAndValue(reqJson, "timeOffset", "请求报文中未包含timeOffset");
        Assert.hasKeyAndValue(reqJson, "clockCount", "请求报文中未包含clockCount");
        Assert.hasKeyAndValue(reqJson, "clockType", "请求报文中未包含clockType");
        Assert.hasKeyAndValue(reqJson, "clockTypeValue", "请求报文中未包含clockTypeValue");
        Assert.hasKeyAndValue(reqJson, "lateOffset", "请求报文中未包含lateOffset");
        Assert.hasKeyAndValue(reqJson, "leaveOffset", "请求报文中未包含leaveOffset");
        Assert.hasKeyAndValue(reqJson, "classesObjType", "请求报文中未包含classesObjType");
        Assert.hasKeyAndValue(reqJson, "classesObjId", "请求报文中未包含classesObjId");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        attendanceClassesBMOImpl.addAttendanceClasses(reqJson, context);

        //处理房屋属性
        dealAttr(reqJson, context);
    }

    private void dealAttr(JSONObject reqJson, DataFlowContext context) {

        if (!reqJson.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (attrs == null || attrs.size() < 1) {
            return;
        }


        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("classesId", reqJson.getString("classesId"));
            attr.put("storeId", reqJson.getString("storeId"));
            attendanceClassesAttrBMOImpl.addAttendanceClassesAttr(attr, context);
        }

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAttendanceClassesConstant.ADD_ATTENDANCECLASSES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
