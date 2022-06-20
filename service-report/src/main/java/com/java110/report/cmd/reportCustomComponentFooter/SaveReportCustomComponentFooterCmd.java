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
package com.java110.report.cmd.reportCustomComponentFooter;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.report.IReportCustomComponentFooterV1InnerServiceSMO;
import com.java110.po.reportCustomComponentFooter.ReportCustomComponentFooterPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类表述：保存
 * 服务编码：reportCustomComponentFooter.saveReportCustomComponentFooter
 * 请求路劲：/app/reportCustomComponentFooter.SaveReportCustomComponentFooter
 * add by 吴学文 at 2021-11-14 01:32:10 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "reportCustomComponentFooter.saveReportCustomComponentFooter")
public class SaveReportCustomComponentFooterCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveReportCustomComponentFooterCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IReportCustomComponentFooterV1InnerServiceSMO reportCustomComponentFooterV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "componentId", "请求报文中未包含componentId");
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ReportCustomComponentFooterPo reportCustomComponentFooterPo = BeanConvertUtil.covertBean(reqJson, ReportCustomComponentFooterPo.class);
        reportCustomComponentFooterPo.setFooterId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        try {
            if (!StringUtil.isEmpty(reportCustomComponentFooterPo.getComponentSql())) {
                reportCustomComponentFooterPo.setComponentSql(Base64Convert.byteToBase64(reportCustomComponentFooterPo.getComponentSql().getBytes("UTF-8")));
            }
            if (!StringUtil.isEmpty(reportCustomComponentFooterPo.getJavaScript())) {
                reportCustomComponentFooterPo.setJavaScript(Base64Convert.byteToBase64(reportCustomComponentFooterPo.getJavaScript().getBytes("UTF-8")));
            }
        } catch (Exception e) {
            logger.error("编码失败", e);
        }
        int flag = reportCustomComponentFooterV1InnerServiceSMOImpl.saveReportCustomComponentFooter(reportCustomComponentFooterPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
