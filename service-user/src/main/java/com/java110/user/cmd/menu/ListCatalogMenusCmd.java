/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.user.cmd.menu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.language.Language;
import com.java110.dto.menu.MenuGroupDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.intf.user.IMenuCatalogV1InnerServiceSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;


/**
 * 类表述：查询
 * 服务编码：menuCatalog.listMenuCatalog
 * 请求路劲：/app/menuCatalog.ListMenuCatalog
 * add by 吴学文 at 2022-02-26 10:12:36 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "menu.listCatalogMenus")
public class ListCatalogMenusCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListCatalogMenusCmd.class);
    @Autowired
    private IMenuCatalogV1InnerServiceSMO menuCatalogV1InnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        //super.validatePageInfo(reqJson);

        Assert.hasKeyAndValue(reqJson, "userId", "未包含用户信息");
        Assert.hasKeyAndValue(reqJson, "storeId", "未包含商户信息");
        //Assert.hasKeyAndValue(reqJson, "caId", "未包含目录信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        MenuGroupDto menuCatalogDto = BeanConvertUtil.covertBean(reqJson, MenuGroupDto.class);
        menuCatalogDto.setGroupType(MenuGroupDto.GROUP_TYPE_PC);
        //判断是否 为 开发或者运营，如果是开发或者运营时 取消 小区ID

        //查询store 信息
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(reqJson.getString("storeId"));
        List<StoreDto> storeDtos = storeInnerServiceSMOImpl.getStores(storeDto);

        Assert.listOnlyOne(storeDtos, "商户不存在");

        if (StoreDto.STORE_TYPE_ADMIN.equals(storeDtos.get(0).getStoreTypeCd()) || StoreDto.STORE_TYPE_DEV.equals(storeDtos.get(0).getStoreTypeCd())) {
            menuCatalogDto.setCommunityId("");
        }

        menuCatalogDto.setDomain(storeDtos.get(0).getStoreTypeCd());
        menuCatalogDto.setStoreType(storeDtos.get(0).getStoreTypeCd());

        List<Map> menus = menuCatalogV1InnerServiceSMOImpl.queryMenus(menuCatalogDto);
        String lang = cmdDataFlowContext.getReqHeaders().get(CommonConstant.JAVA110_LANG);
        if (!StringUtil.isEmpty(lang) && !CommonConstant.LANG_ZH_CN.equals(lang)) {
            Language language = ApplicationContextFactory.getBean(lang, Language.class);
            if (language != null) {
                menus = language.getMenuDto(menus);
            }

        }

        cmdDataFlowContext.setResponseEntity(refreshMenusInfo(menus));
    }


    private ResponseEntity<String> refreshMenusInfo(List<Map> menusList) {
        JSONArray tempMenus = new JSONArray();
        JSONObject tempMenu = null;
        for (Map tmpMenu : menusList) {
            JSONObject tMenu = JSONObject.parseObject(JSONObject.toJSONString(tmpMenu));
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
            childMenu.put("mId", tMenu.getString("mId"));
            childMenu.put("name", tMenu.getString("menuName"));
            childMenu.put("href", tMenu.getString("url"));
            childMenu.put("seq", tMenu.getString("menuSeq"));
            childMenu.put("isShow", tMenu.getString("isShow"));
            childMenu.put("description", tMenu.getString("description"));
            childs.add(childMenu);
        }


        return ResultVo.createResponseEntity(tempMenus);
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
