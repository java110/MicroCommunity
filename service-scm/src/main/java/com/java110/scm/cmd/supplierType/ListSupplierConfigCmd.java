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
package com.java110.scm.cmd.supplierType;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.supplier.SupplierDto;
import com.java110.dto.supplier.SupplierKeyDto;
import com.java110.intf.scm.ISupplierConfigV1InnerServiceSMO;
import com.java110.intf.scm.ISupplierKeyV1InnerServiceSMO;
import com.java110.intf.scm.ISupplierV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.supplier.SupplierConfigDto;
import java.util.List;
import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：supplierConfig.listSupplierConfig
 * 请求路劲：/app/supplierConfig.ListSupplierConfig
 * add by 吴学文 at 2022-11-17 00:33:53 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "supplierType.listSupplierConfig")
public class ListSupplierConfigCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(ListSupplierConfigCmd.class);
    @Autowired
    private ISupplierConfigV1InnerServiceSMO supplierConfigV1InnerServiceSMOImpl;

    @Autowired
    private ISupplierKeyV1InnerServiceSMO supplierKeyV1InnerServiceSMOImpl;

    @Autowired
    private ISupplierV1InnerServiceSMO supplierV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson,"supplierId","未包含供应商");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

           SupplierConfigDto supplierConfigDto = BeanConvertUtil.covertBean(reqJson, SupplierConfigDto.class);

           int count = supplierConfigV1InnerServiceSMOImpl.querySupplierConfigsCount(supplierConfigDto);

           List<SupplierConfigDto> supplierConfigDtos = null;

           if (count > 0) {
               supplierConfigDtos = supplierConfigV1InnerServiceSMOImpl.querySupplierConfigs(supplierConfigDto);
           } else {
               supplierConfigDtos = getSupplierKey(supplierConfigDto);
           }

           ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, supplierConfigDtos);

           ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

           cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 查询
     * @param supplierConfigDto
     * @return
     */
    private List<SupplierConfigDto> getSupplierKey(SupplierConfigDto supplierConfigDto) {

        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setSupplierId(supplierConfigDto.getSupplierId());
        List<SupplierDto> supplierDtos = supplierV1InnerServiceSMOImpl.querySuppliers(supplierDto);

        Assert.listOnlyOne(supplierDtos,"未包含供应商");

        SupplierKeyDto supplierKeyDto = new SupplierKeyDto();
        supplierKeyDto.setBeanName(supplierDtos.get(0).getBeanName());
        List<SupplierKeyDto> supplierKeyDtos = supplierKeyV1InnerServiceSMOImpl.querySupplierKeys(supplierKeyDto);

        Assert.listOnlyOne(supplierDtos,"未包含供应商模板配置");

        List<SupplierConfigDto> supplierConfigDtos = new ArrayList<>();
        SupplierConfigDto supplierConfigDto1 = null;
        for(SupplierKeyDto supplierKeyDto1:supplierKeyDtos){
            supplierConfigDto1 = new SupplierConfigDto();
            supplierConfigDto1.setSupplierId(supplierConfigDto.getSupplierId());
            supplierConfigDto1.setColumnKey(supplierKeyDto1.getColumnKey());
            supplierConfigDto1.setColumnValue("");
            supplierConfigDto1.setName(supplierKeyDto1.getName());
            supplierConfigDto1.setRemark(supplierKeyDto1.getRemark());
            supplierConfigDtos.add(supplierConfigDto1);
        }
        return supplierConfigDtos;
    }
}
