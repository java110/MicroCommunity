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
package com.java110.dev.cmd.menuGroupCatalog;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.menu.MenuCatalogDto;
import com.java110.intf.user.IMenuCatalogV1InnerServiceSMO;
import com.java110.intf.user.IMenuGroupCatalogV1InnerServiceSMO;
import com.java110.po.menuGroupCatalog.MenuGroupCatalogPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：menuGroupCatalog.saveMenuGroupCatalog
 * 请求路劲：/app/menuGroupCatalog.SaveMenuGroupCatalog
 * add by 吴学文 at 2022-02-26 10:18:54 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "menuGroupCatalog.saveMenuGroupCatalog")
public class SaveMenuGroupCatalogCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveMenuGroupCatalogCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IMenuGroupCatalogV1InnerServiceSMO menuGroupCatalogV1InnerServiceSMOImpl;

    @Autowired
    private IMenuCatalogV1InnerServiceSMO menuCatalogV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "caId", "请求报文中未包含caId");
        Assert.hasKeyAndValue(reqJson, "gId", "请求报文中未包含gId");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        //查询 catalog
        MenuCatalogDto menuCatalogDto = new MenuCatalogDto();
        menuCatalogDto.setCaId(reqJson.getString("caId"));
        List<MenuCatalogDto> menuCatalogDtos = menuCatalogV1InnerServiceSMOImpl.queryMenuCatalogs(menuCatalogDto);

        Assert.listOnlyOne(menuCatalogDtos, "菜单目录不存在");

        MenuGroupCatalogPo menuGroupCatalogPo = BeanConvertUtil.covertBean(reqJson, MenuGroupCatalogPo.class);
        menuGroupCatalogPo.setGcId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        menuGroupCatalogPo.setStoreType(menuCatalogDtos.get(0).getStoreType());
        int flag = menuGroupCatalogV1InnerServiceSMOImpl.saveMenuGroupCatalog(menuGroupCatalogPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
