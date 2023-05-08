package com.java110.community.cmd.parkingArea;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.IParkingAreaAttrV1InnerServiceSMO;
import com.java110.intf.community.IParkingAreaV1InnerServiceSMO;
import com.java110.po.parking.ParkingAreaPo;
import com.java110.po.parkingAreaAttr.ParkingAreaAttrPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "parkingArea.updateParkingArea")
public class UpdateParkingAreaCmd extends Cmd {

    @Autowired
    private IParkingAreaV1InnerServiceSMO parkingAreaV1InnerServiceSMOImpl;

    @Autowired
    private IParkingAreaAttrV1InnerServiceSMO parkingAreaAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "paId", "停车场ID不能为空");
        Assert.hasKeyAndValue(reqJson, "num", "必填，请填写停车场编号");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择停车场类型");

        Assert.judgeAttrValue(reqJson);
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        JSONObject businessParkingArea = new JSONObject();
        businessParkingArea.putAll(reqJson);
        ParkingAreaPo parkingAreaPo = BeanConvertUtil.covertBean(businessParkingArea, ParkingAreaPo.class);
        int flag = parkingAreaV1InnerServiceSMOImpl.updateParkingArea(parkingAreaPo);
        if (flag < 1) {
            throw new CmdException("保存停车场失败");
        }
        if (!reqJson.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (attrs == null || attrs.size() < 1) {
            return;
        }

        JSONObject attr = null;
        ParkingAreaAttrPo parkingAreaAttrPo = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("communityId", reqJson.getString("communityId"));
            if (!attr.containsKey("attrId") || attr.getString("attrId").startsWith("-") || StringUtil.isEmpty(attr.getString("attrId"))) {
                attr.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                parkingAreaAttrPo = BeanConvertUtil.covertBean(attr, ParkingAreaAttrPo.class);
                parkingAreaAttrPo.setPaId(parkingAreaPo.getPaId());
                flag = parkingAreaAttrV1InnerServiceSMOImpl.saveParkingAreaAttr(parkingAreaAttrPo);
                if (flag < 1) {
                    throw new CmdException("保存停车场失败");
                }
                continue;
            }
            parkingAreaAttrPo = BeanConvertUtil.covertBean(attr, ParkingAreaAttrPo.class);
            flag = parkingAreaAttrV1InnerServiceSMOImpl.updateParkingAreaAttr(parkingAreaAttrPo);
            if (flag < 1) {
                throw new CmdException("保存停车场失败");
            }
        }
    }
}
