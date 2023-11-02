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

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.file.FileRelDto;
import com.java110.intf.acct.IInvoiceApplyV1InnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.invoiceApply.InvoiceApplyDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：invoiceApply.listInvoiceApply
 * 请求路劲：/app/invoiceApply.ListInvoiceApply
 * add by 吴学文 at 2023-10-08 16:26:34 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "invoice.listInvoiceApply")
public class ListInvoiceApplyCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListInvoiceApplyCmd.class);
    @Autowired
    private IInvoiceApplyV1InnerServiceSMO invoiceApplyV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        InvoiceApplyDto invoiceApplyDto = BeanConvertUtil.covertBean(reqJson, InvoiceApplyDto.class);

        int count = invoiceApplyV1InnerServiceSMOImpl.queryInvoiceApplysCount(invoiceApplyDto);

        List<InvoiceApplyDto> invoiceApplyDtos = null;

        if (count > 0) {
            invoiceApplyDtos = invoiceApplyV1InnerServiceSMOImpl.queryInvoiceApplys(invoiceApplyDto);
        } else {
            invoiceApplyDtos = new ArrayList<>();
        }

        //todo 查询电子发票
        queryInvoicePhoto(invoiceApplyDtos);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, invoiceApplyDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void queryInvoicePhoto(List<InvoiceApplyDto> invoiceApplyDtos) {

        if (invoiceApplyDtos == null || invoiceApplyDtos.size() != 1) {
            return;
        }

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(invoiceApplyDtos.get(0).getApplyId());
        fileRelDto.setRelTypeCd(FileRelDto.REL_TYPE_CE_INVOICE); //业主照片
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        //String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN, "IMG_PATH");

        if (fileRelDtos != null && !fileRelDtos.isEmpty()) {
            List<String> urls = new ArrayList<>();
            for (FileRelDto fileRel : fileRelDtos) {
                urls.add(fileRel.getFileRealName());
            }
            invoiceApplyDtos.get(0).setUrls(urls);
        }
    }
}
