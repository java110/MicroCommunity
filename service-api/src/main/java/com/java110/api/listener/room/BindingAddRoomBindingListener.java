package com.java110.api.listener.room;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.room.IRoomBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeAddRoomBindingConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("bindingAddRoomBindingListener")
public class BindingAddRoomBindingListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IRoomBMO roomBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        JSONArray infos = reqJson.getJSONArray("data");

        Assert.hasKeyByFlowData(infos, "addRoomView", "roomNum", "必填，请填写房屋编号");
        Assert.hasKeyByFlowData(infos, "addRoomView", "communityId", "必填，请填写房屋小区信息");
        Assert.hasKeyByFlowData(infos, "addRoomView", "layer", "必填，请填写房屋楼层");
        Assert.hasKeyByFlowData(infos, "addRoomView", "section", "必填，请填写房屋楼层");
        Assert.hasKeyByFlowData(infos, "addRoomView", "apartment", "必填，请选择房屋户型");
        Assert.hasKeyByFlowData(infos, "addRoomView", "builtUpArea", "必填，请填写房屋建筑面积");
        Assert.hasKeyByFlowData(infos, "addRoomView", "feeCoefficient", "必填，请填写房屋每平米单价");
        Assert.hasKeyByFlowData(infos, "addRoomView", "state", "必填，请选择房屋状态");

        JSONObject addRoomView = null;
        for (int roomIndex = 0; roomIndex < infos.size(); roomIndex++) {
            JSONObject _info = infos.getJSONObject(roomIndex);
            if (_info.containsKey("addRoomView") && _info.getString("flowComponent").equals("addRoomView")) {
                addRoomView = _info;
                break;
            }
        }

        if (addRoomView == null) {
            return;
        }

        Assert.judgeAttrValue(addRoomView);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        JSONArray infos = reqJson.getJSONArray("data");


        JSONObject viewFloorInfo = getObj(infos, "viewFloorInfo");
        JSONObject viewUnitInfo = getObj(infos, "viewUnitInfo");
        JSONObject addRoomView = getObj(infos, "addRoomView");
        if (!hasKey(viewFloorInfo, "floorId")) {
            viewFloorInfo.put("floorId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_floorId));
            viewFloorInfo.put("userId", context.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
            roomBMOImpl.addBusinessFloor(viewFloorInfo, context);
            roomBMOImpl.addCommunityMember(viewFloorInfo, context);
        }
        if (!hasKey(viewUnitInfo, "unitId")) {
            viewUnitInfo.put("unitId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_unitId));
            viewUnitInfo.put("userId", context.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
            viewUnitInfo.put("floorId", viewFloorInfo.getString("floorId"));
            roomBMOImpl.addBusinessUnit(viewUnitInfo, context);
        }
        if (!hasKey(addRoomView, "roomId")) {
            addRoomView.put("roomId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_roomId));
            addRoomView.put("userId", context.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
            addRoomView.put("unitId", viewUnitInfo.getString("unitId"));
            roomBMOImpl.addBusinessRoom(addRoomView, context);
            //处理房屋属性
            dealRoomAttr(addRoomView, context);
        }


        commit(context);

        JSONObject paramOutObj = new JSONObject();
        paramOutObj.put("floorId", viewFloorInfo.getString("floorId"));
        paramOutObj.put("unitId", viewUnitInfo.getString("unitId"));
        paramOutObj.put("roomId", addRoomView.getString("roomId"));
        ResponseEntity<String> responseEntity = null;
        if (context.getResponseEntity().getStatusCode() == HttpStatus.OK) {
            responseEntity = new ResponseEntity<String>(paramOutObj.toJSONString(), HttpStatus.OK);
        }
        context.setResponseEntity(responseEntity);
    }

    private void dealRoomAttr(JSONObject addRoomView, DataFlowContext context) {

        if (!addRoomView.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = addRoomView.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }


        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("roomId", addRoomView.getString("roomId"));
            roomBMOImpl.addRoomAttr(attr, context);
        }

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAddRoomBindingConstant.BINDING_ADDROOMBINDING;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    private boolean hasKey(JSONObject info, String key) {
        if (!info.containsKey(key)
                || StringUtil.isEmpty(info.getString(key))
                || info.getString(key).startsWith("-")) {
            return false;
        }
        return true;

    }

    private JSONObject getObj(JSONArray infos, String flowComponent) {

        JSONObject serviceInfo = null;

        for (int infoIndex = 0; infoIndex < infos.size(); infoIndex++) {

            Assert.hasKeyAndValue(infos.getJSONObject(infoIndex), "flowComponent", "未包含服务流程组件名称");

            if (flowComponent.equals(infos.getJSONObject(infoIndex).getString("flowComponent"))) {
                serviceInfo = infos.getJSONObject(infoIndex);
                Assert.notNull(serviceInfo, "未包含服务信息");
                return serviceInfo;
            }
        }

        throw new IllegalArgumentException("未找到组件编码为【" + flowComponent + "】数据");
    }


}
