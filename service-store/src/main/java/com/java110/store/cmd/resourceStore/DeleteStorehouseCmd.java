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
package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.community.IResourceStoreServiceSMO;
import com.java110.intf.store.IStorehouseV1InnerServiceSMO;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.storehouse.StorehousePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

/**
 * 类表述：删除
 * 服务编码：storehouse.deleteStorehouse
 * 请求路劲：/app/storehouse.DeleteStorehouse
 * add by 吴学文 at 2022-08-08 15:47:06 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "resourceStore.deleteStorehouse")
public class DeleteStorehouseCmd extends Cmd {
  private static Logger logger = LoggerFactory.getLogger(DeleteStorehouseCmd.class);

    @Autowired
    private IStorehouseV1InnerServiceSMO storehouseV1InnerServiceSMOImpl;


    @Autowired
    private IResourceStoreServiceSMO resourceStoreServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "shId", "shId不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //获取仓库id
        String shId = reqJson.getString("shId");
        ResourceStorePo resourceStorePo = new ResourceStorePo();
        resourceStorePo.setShId(shId);
        int page = resourceStoreServiceSMOImpl.getResourceStoresCount(resourceStorePo);
        if (page < 1) {
            StorehousePo storehousePo = BeanConvertUtil.covertBean(reqJson, StorehousePo.class);
            int flag = storehouseV1InnerServiceSMOImpl.deleteStorehouse(storehousePo);
            if (flag < 1) {
                throw new CmdException("删除数据失败");
            }
        } else {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "该仓库包含物品，不能直接删除！");
            context.setResponseEntity(responseEntity);
            return;
        }
        context.setResponseEntity(ResultVo.success());
    }
}
