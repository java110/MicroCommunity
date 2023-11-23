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
package com.java110.acct.cmd.invoice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.dao.IInvoiceEventV1ServiceDao;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.invoiceApply.InvoiceApplyDto;
import com.java110.dto.invoiceApplyItem.InvoiceApplyItemDto;
import com.java110.dto.invoiceEvent.InvoiceEventDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IInvoiceApplyItemV1InnerServiceSMO;
import com.java110.intf.acct.IInvoiceApplyV1InnerServiceSMO;
import com.java110.intf.acct.IInvoiceEventV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.invoiceApply.InvoiceApplyPo;
import com.java110.po.invoiceEvent.InvoiceEventPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 类表述：审核发票
 * 服务编码：invoiceApply.updateInvoiceApply
 * 请求路劲：/app/invoiceApply.UpdateInvoiceApply
 * add by 吴学文 at 2023-10-08 16:26:34 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "invoice.auditInvoiceApply")
public class AuditInvoiceApplyCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(AuditInvoiceApplyCmd.class);


    @Autowired
    private IInvoiceApplyV1InnerServiceSMO invoiceApplyV1InnerServiceSMOImpl;

    @Autowired
    private IInvoiceApplyItemV1InnerServiceSMO invoiceApplyItemV1InnerServiceSMOImpl;

    @Autowired
    private IInvoiceEventV1InnerServiceSMO invoiceEventV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "applyId", "applyId不能为空");
        Assert.hasKeyAndValue(reqJson, "state", "状态不能为空");
        Assert.hasKeyAndValue(reqJson, "remark", "说明不能为空");

        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");


        InvoiceApplyPo invoiceApplyPo = new InvoiceApplyPo();
        invoiceApplyPo.setApplyId(reqJson.getString("applyId"));
        //退费审核通过
        if ("1100".equals(reqJson.getString("state"))) {
            invoiceApplyPo.setState(InvoiceApplyDto.STATE_UPLOAD);
        }else{
            invoiceApplyPo.setState(InvoiceApplyDto.STATE_FAIL);
        }
        int flag = invoiceApplyV1InnerServiceSMOImpl.updateInvoiceApply(invoiceApplyPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        // todo 保存事件
        InvoiceEventPo invoiceEventPo = new InvoiceEventPo();
        invoiceEventPo.setApplyId(reqJson.getString("applyId"));
        invoiceEventPo.setCommunityId(reqJson.getString("communityId"));
        invoiceEventPo.setEventId(GenerateCodeFactory.getGeneratorId("11"));
        if ("1100".equals(reqJson.getString("state"))) {
            invoiceEventPo.setEventType(InvoiceEventDto.STATE_COMPLETE);
        }else{
            invoiceEventPo.setEventType(InvoiceEventDto.STATE_FAIL);
        }
        invoiceEventPo.setCreateUserId(userId);
        invoiceEventPo.setCreateUserName(userDtos.get(0).getName());
        invoiceEventPo.setRemark(reqJson.getString("remark"));
        invoiceEventV1InnerServiceSMOImpl.saveInvoiceEvent(invoiceEventPo);

        //todo 修改缴费明细开票状态为完成或者待开票
        InvoiceApplyItemDto invoiceApplyItemDto = new InvoiceApplyItemDto();
        invoiceApplyItemDto.setApplyId(invoiceApplyPo.getApplyId());
        invoiceApplyItemDto.setItemType(InvoiceApplyItemDto.ITEM_TYPE_FEE);
        List<InvoiceApplyItemDto> invoiceApplyItemDtos = invoiceApplyItemV1InnerServiceSMOImpl.queryInvoiceApplyItems(invoiceApplyItemDto);
        if(ListUtil.isNull(invoiceApplyItemDtos)){
            return;
        }

        for(InvoiceApplyItemDto tmpInvoiceApplyItemDto : invoiceApplyItemDtos){
            // todo 将缴费记录开票状态修改为D 开票中
            PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
            payFeeDetailPo.setDetailId(tmpInvoiceApplyItemDto.getItemObjId());
            payFeeDetailPo.setCommunityId(tmpInvoiceApplyItemDto.getCommunityId());
            if ("1100".equals(reqJson.getString("state"))) {
                payFeeDetailPo.setOpenInvoice("Y"); // todo 开票完成
            }else{
                payFeeDetailPo.setOpenInvoice("N"); // todo 带开票
            }

            payFeeDetailV1InnerServiceSMOImpl.updatePayFeeDetailNew(payFeeDetailPo);
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
