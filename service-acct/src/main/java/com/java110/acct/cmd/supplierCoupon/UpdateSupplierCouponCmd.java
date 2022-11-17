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
package com.java110.acct.cmd.supplierCoupon;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.supplier.SupplierDto;
import com.java110.intf.acct.ISupplierCouponV1InnerServiceSMO;
import com.java110.intf.scm.ISupplierV1InnerServiceSMO;
import com.java110.po.supplierCoupon.SupplierCouponPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;



@Java110CmdDoc(title = "修改供应商优惠券",
        description = "用于外系统修改供应商优惠券",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/supplierCoupon.updateSupplierCoupon",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "supplierCoupon.saveSupplierCoupon"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "couponId", length = 30, remark = "优惠券ID"),
        @Java110ParamDoc(name = "supplierId", length = 30, remark = "供应商ID"),
        @Java110ParamDoc(name = "name", length = 64, remark = "名称"),
        @Java110ParamDoc(name = "businessKey", length = 64, remark = "优惠券业务ID，第三方ID"),
        @Java110ParamDoc(name = "valuePrice", length = 64, remark = "售价")
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="{\"couponId\":\"123123\",\"supplierId\":\"123123\",\"name\":\"2号楼\",\"businessKey\":\"123123\",\"valuePrice\":\"123123\"}",
        resBody="{'code':0,'msg':'成功'}"
)
/**
 * 类表述：更新
 * 服务编码：supplierCoupon.updateSupplierCoupon
 * 请求路劲：/app/supplierCoupon.UpdateSupplierCoupon
 * add by 吴学文 at 2022-11-17 21:33:45 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "supplierCoupon.updateSupplierCoupon")
public class UpdateSupplierCouponCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateSupplierCouponCmd.class);


    @Autowired
    private ISupplierCouponV1InnerServiceSMO supplierCouponV1InnerServiceSMOImpl;


    @Autowired
    private ISupplierV1InnerServiceSMO supplierV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "couponId", "couponId不能为空");
        Assert.hasKeyAndValue(reqJson, "supplierId", "supplierId不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setSupplierId(reqJson.getString("supplierId"));
        List<SupplierDto> supplierDtos = supplierV1InnerServiceSMOImpl.querySuppliers(supplierDto);

        Assert.listOnlyOne(supplierDtos,"请先添加供应商");

        SupplierCouponPo supplierCouponPo = BeanConvertUtil.covertBean(reqJson, SupplierCouponPo.class);
        supplierCouponPo.setSupplierName(supplierDtos.get(0).getSupplierName());
        int flag = supplierCouponV1InnerServiceSMOImpl.updateSupplierCoupon(supplierCouponPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
