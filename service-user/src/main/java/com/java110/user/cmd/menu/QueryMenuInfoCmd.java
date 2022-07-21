package com.java110.user.cmd.menu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.store.StoreDto;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.service.context.DataQuery;
import com.java110.service.smo.IQueryServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "query.menu.info")
public class QueryMenuInfoCmd extends Cmd {

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = context.getReqHeaders().get("user-id");
        //String storeId = context.getReqHeaders().get("store-id");

        if(StringUtil.isEmpty(userId)){
            userId = reqJson.getString("userId");
        }

        Assert.hasLength(userId, "未包含用户");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String userId = context.getReqHeaders().get("user-id");
        String storeId = context.getReqHeaders().get("store-id");

        String groupType = "P_WEB";
        if(reqJson.containsKey("groupType") && !StringUtil.isEmpty(reqJson.getString("groupType"))){
            groupType = reqJson.getString("groupType");
        }

        if(StringUtil.isEmpty(userId)){
            userId = reqJson.getString("userId");
        }
        String domain = "";
        if(!reqJson.containsKey("domain") || StringUtil.isEmpty(reqJson.getString("domain"))) {

            StoreDto storeDto = new StoreDto();
            storeDto.setStoreId(storeId);
            storeDto.setPage(1);
            storeDto.setRow(1);
            List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

            Assert.listOnlyOne(storeDtos, "商户不存在");
            domain = storeDtos.get(0).getStoreTypeCd();
        }else{
            domain = reqJson.getString("domain");
        }

        DataQuery dataQuery = new DataQuery();
        dataQuery.setServiceCode("query.menu.info");
        JSONObject param = new JSONObject();
        param.put("userId", userId);
        param.put("domain", domain);
        param.put("groupType", groupType);
        dataQuery.setRequestParams(param);
        queryServiceSMOImpl.commonQueryService(dataQuery);
        ResponseEntity<String> privilegeGroup = dataQuery.getResponseEntity();
        if (privilegeGroup.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(privilegeGroup);
            return ;
        }
        JSONObject resultObj = JSONObject.parseObject(privilegeGroup.getBody().toString());



        if (!resultObj.containsKey("menus")) {
            return;
        }

        context.setResponseEntity(refreshMenusInfo(resultObj.getJSONArray("menus")));

    }

    /**
     * 刷新菜单信息
     * 将 数据 [{
     * "gId": "800201904001",
     * "menuDescription": "添加员工",
     * "menuGroupSeq": 1,
     * "menuSeq": 1,
     * "icon": "fa-desktop",
     * "mId": "700201904001",
     * "menuName": "添加员工",
     * "pId": "500201904001",
     * "menuGroupName": "员工管理",
     * "label": "",
     * "menuGroupDescription": "员工管理",
     * "url": "/"
     * }],
     * 转为：
     * "[{'id':1,'icon':'fa-desktop','name':'我的菜单','label':'HOT','childs':[" +
     * "{'name':'子菜单','href':'http://www.baidu.com'}]}," +
     * "{'id':2,'icon':'fa-flask','name':'我的菜单','childs':[],'href':'/doc'}," +
     * "{'id':3,'icon':'fa-globe','name':'我的菜单','childs':[{'name':'子菜单','href':'http://www.baidu.com'}]}" +
     * "]";
     *
     * @param menusList 菜单列表
     * @return
     */
    private ResponseEntity<String> refreshMenusInfo(JSONArray menusList) {
        JSONArray tempMenus = new JSONArray();
        JSONObject tempMenu = null;
        for (int menuIndex = 0; menuIndex < menusList.size(); menuIndex++) {
            JSONObject tMenu = menusList.getJSONObject(menuIndex);
            tempMenu = this.getMenuFromMenus(tempMenus, tMenu.getString("gId"));
            if (tempMenu == null) {
                tempMenu = new JSONObject();
                tempMenu.put("id", tMenu.getString("gId"));
                tempMenu.put("icon", tMenu.getString("icon"));
                tempMenu.put("name", tMenu.getString("menuGroupName"));
                tempMenu.put("label", tMenu.getString("label"));
                tempMenu.put("seq", tMenu.getString("menuGroupSeq"));
                tempMenu.put("childs", new JSONArray());
                tempMenus.add(tempMenu);
            }
            //获取孩子菜单
            JSONArray childs = tempMenu.getJSONArray("childs");
            JSONObject childMenu = new JSONObject();
            childMenu.put("name", tMenu.getString("menuName"));
            childMenu.put("href", tMenu.getString("url"));
            childMenu.put("seq", tMenu.getString("menuSeq"));
            childMenu.put("isShow", tMenu.getString("isShow"));
            childMenu.put("description", tMenu.getString("description"));
            childs.add(childMenu);
        }
        return new ResponseEntity<String>(tempMenus.toJSONString(), HttpStatus.OK);
    }

    /**
     * 在菜单列表查询菜单
     *
     * @param gId
     * @return
     */
    private JSONObject getMenuFromMenus(JSONArray tempMenus, String gId) {
        for (int tempIndex = 0; tempIndex < tempMenus.size(); tempIndex++) {
            if (tempMenus.getJSONObject(tempIndex).getString("id").equals(gId)) {
                return tempMenus.getJSONObject(tempIndex);
            }
        }

        return null;
    }
}
