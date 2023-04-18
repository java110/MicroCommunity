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

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.language.Language;
import com.java110.dto.menu.MenuCatalogDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


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
@Java110Cmd(serviceCode = "menu.listCatalog")
public class ListCatalogCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListCatalogCmd.class);
    @Autowired
    private IMenuCatalogV1InnerServiceSMO menuCatalogV1InnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        if(!reqJson.containsKey("storeId") || StringUtil.isEmpty(reqJson.getString("storeId"))){
            reqJson.put("storeId",cmdDataFlowContext.getReqHeaders().get("store-id"));
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        MenuCatalogDto menuCatalogDto = BeanConvertUtil.covertBean(reqJson, MenuCatalogDto.class);

        //查询store 信息
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(reqJson.getString("storeId"));
        List<StoreDto> storeDtos = storeInnerServiceSMOImpl.getStores(storeDto);

        Assert.listOnlyOne(storeDtos, "商户不存在");

        menuCatalogDto.setStoreType(storeDtos.get(0).getStoreTypeCd());

        int count = menuCatalogV1InnerServiceSMOImpl.queryMenuCatalogsCount(menuCatalogDto);

        List<MenuCatalogDto> menuCatalogDtos = null;

        if (count > 0) {
            menuCatalogDtos = menuCatalogV1InnerServiceSMOImpl.queryMenuCatalogs(menuCatalogDto);
        } else {
            menuCatalogDtos = new ArrayList<>();
        }

        String lang = cmdDataFlowContext.getReqHeaders().get(CommonConstant.JAVA110_LANG);
        if (!StringUtil.isEmpty(lang) && !CommonConstant.LANG_ZH_CN.equals(lang)) {
            Language language = ApplicationContextFactory.getBean(lang, Language.class);
            if (language != null) {
                menuCatalogDtos = language.getMenuCatalog(menuCatalogDtos);
            }

        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, menuCatalogDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
