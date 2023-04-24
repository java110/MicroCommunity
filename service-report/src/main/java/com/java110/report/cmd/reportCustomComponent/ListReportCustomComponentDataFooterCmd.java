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
package com.java110.report.cmd.reportCustomComponent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.PageDto;
import com.java110.dto.reportCustom.ReportCustomComponentDto;
import com.java110.dto.reportCustom.ReportCustomComponentFooterDto;
import com.java110.intf.report.IReportCustomComponentFooterV1InnerServiceSMO;
import com.java110.intf.report.IReportCustomComponentV1InnerServiceSMO;
import com.java110.service.smo.IQueryServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;


/**
 * 类表述：组件底部
 * 服务编码：reportCustomComponent.listReportCustomComponentFooter
 * 请求路劲：/app/reportCustomComponent.listReportCustomComponentFooter
 * add by 吴学文 at 2021-11-09 13:18:41 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "reportCustomComponent.listReportCustomComponentDataFooter")
public class ListReportCustomComponentDataFooterCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListReportCustomComponentDataFooterCmd.class);
    @Autowired
    private IReportCustomComponentV1InnerServiceSMO reportCustomComponentV1InnerServiceSMOImpl;
    @Autowired
    private IReportCustomComponentFooterV1InnerServiceSMO reportCustomComponentFooterV1InnerServiceSMOImpl;

    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "componentId", "未包含组件ID");
    }


    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        //查询组件是否存在
        ReportCustomComponentDto reportCustomComponentDto = new ReportCustomComponentDto();
        reportCustomComponentDto.setComponentId(reqJson.getString("componentId"));
        List<ReportCustomComponentDto> reportCustomComponentDtos = reportCustomComponentV1InnerServiceSMOImpl.queryReportCustomComponents(reportCustomComponentDto);
        Assert.listOnlyOne(reportCustomComponentDtos, "组件不存在，请联系开发人员");

        ReportCustomComponentFooterDto reportCustomComponentFooterDto = new ReportCustomComponentFooterDto();
        reportCustomComponentFooterDto.setComponentId(reqJson.getString("componentId"));
        List<ReportCustomComponentFooterDto> reportCustomComponentFooterDtos
                = reportCustomComponentFooterV1InnerServiceSMOImpl.queryReportCustomComponentFooters(reportCustomComponentFooterDto);

        // 没有配置信息
        if (reportCustomComponentFooterDtos == null || reportCustomComponentFooterDtos.size() < 1) {
            cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(new JSONObject()));
            return;
        }

        reportCustomComponentFooterDto = reportCustomComponentFooterDtos.get(0);

        if (ReportCustomComponentDto.QUERY_MODEL_SQL.equals(reportCustomComponentFooterDto.getQueryModel())) {
            doDealSql(reqJson, reportCustomComponentFooterDto, cmdDataFlowContext);
        } else if (ReportCustomComponentDto.QUERY_MODEL_JAVA.equals(reportCustomComponentFooterDto.getQueryModel())) {
            doDealJava(reqJson, reportCustomComponentFooterDto, cmdDataFlowContext);
        } else {
            throw new CmdException("组件实现方式不支持，请联系开发人员");
        }
    }

    private void doDealJava(JSONObject reqJson, ReportCustomComponentFooterDto reportCustomComponentFooterDto, ICmdDataFlowContext cmdDataFlowContext) {
        String sql = reportCustomComponentFooterDto.getComponentSql();

        int page = reqJson.getInteger("page");
        if (page != PageDto.DEFAULT_PAGE) {
            reqJson.put("page", (page - 1) * reqJson.getIntValue("row"));
        }
        JSONObject data = queryServiceSMOImpl.execJava(reqJson, sql);
        JSONArray dataTd = data.getJSONArray("td");
        JSONObject paramOut = null;
        if (dataTd != null && dataTd.size() > 0) {
            paramOut = dataTd.getJSONObject(0);
        } else {
            paramOut = new JSONObject();
        }
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(paramOut);
        cmdDataFlowContext.setResponseEntity(responseEntity);

    }

    private void doDealSql(JSONObject reqJson, ReportCustomComponentFooterDto reportCustomComponentFooterDto, ICmdDataFlowContext cmdDataFlowContext) {
        //校验是否传了 分页信息
        String sql = reportCustomComponentFooterDto.getComponentSql();
        long total = reqJson.getIntValue("row");
        int page = reqJson.getInteger("page");
        if (page != PageDto.DEFAULT_PAGE) {
            reqJson.put("page", (page - 1) * reqJson.getIntValue("row"));
        }
        JSONObject data = queryServiceSMOImpl.execQuerySql(reqJson, sql);
        JSONArray dataTd = data.getJSONArray("td");
        JSONObject paramOut = null;
        if (dataTd != null && dataTd.size() > 0) {
            paramOut = dataTd.getJSONObject(0);
        } else {
            paramOut = new JSONObject();
        }
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(paramOut);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }


}
