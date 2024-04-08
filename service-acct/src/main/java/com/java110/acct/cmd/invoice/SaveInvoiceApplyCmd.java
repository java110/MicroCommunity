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
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountReceiptDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.invoiceApply.InvoiceApplyDto;
import com.java110.dto.invoiceApplyItem.InvoiceApplyItemDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.ownerInvoice.OwnerInvoiceDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IInvoiceApplyItemV1InnerServiceSMO;
import com.java110.intf.acct.IInvoiceApplyV1InnerServiceSMO;
import com.java110.intf.acct.IOwnerInvoiceV1InnerServiceSMO;
import com.java110.intf.fee.IAccountReceiptV1InnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.account.AccountReceiptPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.invoiceApply.InvoiceApplyPo;
import com.java110.po.invoiceApplyItem.InvoiceApplyItemPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 类表述：保存
 * 服务编码：invoiceApply.saveInvoiceApply
 * 请求路劲：/app/invoiceApply.SaveInvoiceApply
 * add by 吴学文 at 2023-10-08 16:26:34 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "invoice.saveInvoiceApply")
public class SaveInvoiceApplyCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveInvoiceApplyCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IInvoiceApplyV1InnerServiceSMO invoiceApplyV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerInvoiceV1InnerServiceSMO ownerInvoiceV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IInvoiceApplyItemV1InnerServiceSMO invoiceApplyItemV1InnerServiceSMOImpl;


    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IAccountReceiptV1InnerServiceSMO accountReceiptV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "oiId", "请求报文中未包含oiId");
        Assert.hasKeyAndValue(reqJson, "invoiceType", "请求报文中未包含invoiceType");
        Assert.hasKeyAndValue(reqJson, "ownerName", "请求报文中未包含ownerName");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        //todo 查询发票抬头补充 申请手机号

        OwnerInvoiceDto ownerInvoiceDto = new OwnerInvoiceDto();
        ownerInvoiceDto.setOiId(reqJson.getString("oiId"));
        ownerInvoiceDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerInvoiceDto> ownerInvoiceDtos = ownerInvoiceV1InnerServiceSMOImpl.queryOwnerInvoices(ownerInvoiceDto);

        Assert.listOnlyOne(ownerInvoiceDtos, "发票抬头不存在");

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerInvoiceDtos.get(0).getOwnerId());
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        if (ownerDtos == null || ownerDtos.isEmpty()) {
            throw new CmdException("业主不存在");
        }

        reqJson.put("applyTel", ownerDtos.get(0).getLink());

    }

    /**
     * {"invoiceAddress":"精神分裂水电费和水电费水电费","invoiceFlag":"ACCT","invoiceName":"张三丰","invoiceNum":"123585452156215",
     * "invoiceType":"1001","ownerId":"772023063013350054","ownerName":"qq2","detailIds":[],
     * "arIds":["112023091107560029","112023090897430042"],
     * "communityId":"2023052267100146"}
     *
     * @param event              事件对象
     * @param cmdDataFlowContext 数据上文对象
     * @param reqJson            请求报文
     * @throws CmdException
     */
    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");

        InvoiceApplyPo invoiceApplyPo = BeanConvertUtil.covertBean(reqJson, InvoiceApplyPo.class);
        invoiceApplyPo.setApplyId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));

        List<InvoiceApplyItemPo> invoiceApplyItemPos = new ArrayList<>();
        BigDecimal invoiceAmount = new BigDecimal("0.0");
        //todo 计算已缴费用
        invoiceAmount = computeFeeDetail(reqJson, invoiceApplyItemPos, invoiceAmount, invoiceApplyPo);

        //todo 计算账户费用
        invoiceAmount = computeAcctDetail(reqJson, invoiceApplyItemPos, invoiceAmount, invoiceApplyPo);

        if (invoiceApplyItemPos.isEmpty()) {
            throw new CmdException("未包含开票项");
        }

        invoiceApplyPo.setInvoiceAmount(invoiceAmount.doubleValue() + "");
        invoiceApplyPo.setCreateUserId(userId);
        invoiceApplyPo.setCreateUserName(userDtos.get(0).getName());
        invoiceApplyPo.setState(InvoiceApplyDto.STATE_WAIT);
        int flag = invoiceApplyV1InnerServiceSMOImpl.saveInvoiceApply(invoiceApplyPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }


        //todo 保存开票申请项
        invoiceApplyItemV1InnerServiceSMOImpl.saveInvoiceApplyItems(invoiceApplyItemPos);


        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }


    /**
     * 计算已缴费费用
     *
     * @param reqJson             "detailIds":[],
     * @param invoiceApplyItemPos
     * @return
     */
    private BigDecimal computeFeeDetail(JSONObject reqJson, List<InvoiceApplyItemPo> invoiceApplyItemPos, BigDecimal invoiceAmount, InvoiceApplyPo invoiceApplyPo) {

        if (!reqJson.containsKey("detailIds")) {
            return invoiceAmount;
        }

        JSONArray detailIds = reqJson.getJSONArray("detailIds");
        if (detailIds == null || detailIds.isEmpty()) {
            return invoiceAmount;
        }

        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setDetailIds(detailIds.toArray(new String[detailIds.size()]));
        feeDetailDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);

        if (feeDetailDtos == null || feeDetailDtos.isEmpty()) {
            return invoiceAmount;
        }
        InvoiceApplyItemPo invoiceApplyItemPo = null;
        for (FeeDetailDto tmpFeeDetailDto : feeDetailDtos) {
            invoiceAmount = invoiceAmount.add(new BigDecimal(tmpFeeDetailDto.getReceivedAmount()));
            invoiceApplyItemPo = new InvoiceApplyItemPo();
            invoiceApplyItemPo.setApplyId(invoiceApplyPo.getApplyId());
            invoiceApplyItemPo.setCommunityId(invoiceApplyPo.getCommunityId());
            invoiceApplyItemPo.setItemAmount(tmpFeeDetailDto.getReceivedAmount());
            invoiceApplyItemPo.setItemId(GenerateCodeFactory.getGeneratorId("11"));
            invoiceApplyItemPo.setItemName(tmpFeeDetailDto.getFeeName());
            invoiceApplyItemPo.setRemark(tmpFeeDetailDto.getFeeName()
                    + "("
                    + DateUtil.getFormatTimeStringB(tmpFeeDetailDto.getStartTime())
                    + "~"
                    + DateUtil.getFormatTimeStringB(tmpFeeDetailDto.getEndTime())
                    + ")");
            invoiceApplyItemPo.setPayTime(DateUtil.getFormatTimeStringA(tmpFeeDetailDto.getCreateTime()));
            invoiceApplyItemPo.setItemObjId(tmpFeeDetailDto.getDetailId());
            invoiceApplyItemPo.setItemType(InvoiceApplyItemDto.ITEM_TYPE_FEE);
            invoiceApplyItemPos.add(invoiceApplyItemPo);

            // todo 将缴费记录开票状态修改为D 开票中
            PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
            payFeeDetailPo.setDetailId(tmpFeeDetailDto.getDetailId());
            payFeeDetailPo.setCommunityId(tmpFeeDetailDto.getCommunityId());
            payFeeDetailPo.setOpenInvoice("D"); // todo 开票中
            payFeeDetailV1InnerServiceSMOImpl.updatePayFeeDetailNew(payFeeDetailPo);
        }

        return invoiceAmount;
    }


    /**
     * "arIds":["112023091107560029","112023090897430042"],
     *
     * @param reqJson
     * @param invoiceApplyItemPos
     * @param invoiceAmount
     * @param invoiceApplyPo
     * @return
     */
    private BigDecimal computeAcctDetail(JSONObject reqJson, List<InvoiceApplyItemPo> invoiceApplyItemPos, BigDecimal invoiceAmount, InvoiceApplyPo invoiceApplyPo) {
        if (!reqJson.containsKey("arIds")) {
            return invoiceAmount;
        }

        JSONArray arIds = reqJson.getJSONArray("arIds");
        if (arIds == null || arIds.isEmpty()) {
            return invoiceAmount;
        }


        AccountReceiptDto accountReceiptDto = new AccountReceiptDto();
        accountReceiptDto.setArIds(arIds.toArray(new String[arIds.size()]));
        accountReceiptDto.setCommunityId(reqJson.getString("communityId"));
        List<AccountReceiptDto> accountReceiptDtos = accountReceiptV1InnerServiceSMOImpl.queryAccountReceipts(accountReceiptDto);
        if (accountReceiptDtos == null || accountReceiptDtos.isEmpty()) {
            return invoiceAmount;
        }

        InvoiceApplyItemPo invoiceApplyItemPo = null;
        for (AccountReceiptDto tmpAccountReceiptDto : accountReceiptDtos) {
            invoiceAmount = invoiceAmount.add(new BigDecimal(tmpAccountReceiptDto.getReceivedAmount()));
            invoiceApplyItemPo = new InvoiceApplyItemPo();
            invoiceApplyItemPo.setApplyId(invoiceApplyPo.getApplyId());
            invoiceApplyItemPo.setCommunityId(invoiceApplyPo.getCommunityId());
            invoiceApplyItemPo.setItemAmount(tmpAccountReceiptDto.getReceivedAmount());
            invoiceApplyItemPo.setItemId(GenerateCodeFactory.getGeneratorId("11"));
            invoiceApplyItemPo.setItemName(tmpAccountReceiptDto.getOwnerName());
            invoiceApplyItemPo.setItemObjId(tmpAccountReceiptDto.getArId());
            invoiceApplyItemPo.setItemType(InvoiceApplyItemDto.ITEM_TYPE_ACCT);
            invoiceApplyItemPo.setPayTime(DateUtil.getFormatTimeStringA(tmpAccountReceiptDto.getCreateTime()));

            invoiceApplyItemPos.add(invoiceApplyItemPo);
        }


        return invoiceAmount;
    }
}
