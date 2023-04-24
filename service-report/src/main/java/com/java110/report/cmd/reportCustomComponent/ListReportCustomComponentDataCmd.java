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
import com.java110.core.log.LoggerFactory;
import com.java110.dto.PageDto;
import com.java110.dto.reportCustom.ReportCustomComponentDto;
import com.java110.intf.report.IReportCustomComponentV1InnerServiceSMO;
import com.java110.service.smo.IQueryServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


/**
 * 类表述：查询
 * 服务编码：reportCustomComponent.listReportCustomComponentData
 * 请求路劲：/app/reportCustomComponent.listReportCustomComponentData
 * add by 吴学文 at 2021-11-09 13:18:41 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "reportCustomComponent.listReportCustomComponentData")
public class ListReportCustomComponentDataCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListReportCustomComponentDataCmd.class);

    @Autowired
    private IReportCustomComponentV1InnerServiceSMO reportCustomComponentV1InnerServiceSMOImpl;


    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "componentId", "未包含组件ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");
        reqJson.put("storeId", storeId);

        //查询组件是否存在
        ReportCustomComponentDto reportCustomComponentDto = new ReportCustomComponentDto();
        reportCustomComponentDto.setComponentId(reqJson.getString("componentId"));
        List<ReportCustomComponentDto> reportCustomComponentDtos = reportCustomComponentV1InnerServiceSMOImpl.queryReportCustomComponents(reportCustomComponentDto);
        Assert.listOnlyOne(reportCustomComponentDtos, "组件不存在，请联系开发人员");

        reportCustomComponentDto = reportCustomComponentDtos.get(0);

        if (ReportCustomComponentDto.QUERY_MODEL_SQL.equals(reportCustomComponentDto.getQueryModel())) {
            doDealSql(reqJson, reportCustomComponentDto, cmdDataFlowContext);
        } else if (ReportCustomComponentDto.QUERY_MODEL_JAVA.equals(reportCustomComponentDto.getQueryModel())) {
            doDealJava(reqJson, reportCustomComponentDto, cmdDataFlowContext);
        } else {
            throw new CmdException("组件实现方式不支持，请联系开发人员");
        }
    }

    private void doDealJava(JSONObject reqJson, ReportCustomComponentDto reportCustomComponentDto, ICmdDataFlowContext cmdDataFlowContext) {
        //校验是否传了 分页信息
        String javaScript = reportCustomComponentDto.getJavaScript();
        int page = reqJson.getInteger("page");
        if (page != PageDto.DEFAULT_PAGE) {
            reqJson.put("page", (page - 1) * reqJson.getIntValue("row"));
        }
        JSONObject data = queryServiceSMOImpl.execJava(reqJson, javaScript);
        long total = data.getLong("total");
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) total / (double) reqJson.getInteger("row")), total, data);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void doDealSql(JSONObject reqJson, ReportCustomComponentDto reportCustomComponentDto, ICmdDataFlowContext cmdDataFlowContext) {
        //校验是否传了 分页信息
        String sql = reportCustomComponentDto.getComponentSql();
        long total = reqJson.getIntValue("row");
        if (sql.trim().contains("test=\"count")) { // 如果包含 count(1) 求总数
            JSONObject reqJsonCount = new JSONObject();
            for (String key : reqJson.keySet()) {
                if ("row".equals(key) || "page".equals(key)) {
                    continue;
                }
                reqJsonCount.put(key, reqJson.get(key));
            }
            reqJsonCount.put("count", "1");
            JSONObject data = queryServiceSMOImpl.execQuerySql(reqJsonCount, sql);
            total = data.getJSONArray("td").getJSONObject(0).getIntValue("total");
        }
        reqJson.put("count", "0");
        int page = reqJson.getInteger("page");
        if (page != PageDto.DEFAULT_PAGE) {
            reqJson.put("page", (page - 1) * reqJson.getIntValue("row"));
        }
        JSONObject data = queryServiceSMOImpl.execQuerySql(reqJson, sql);
        if (!sql.trim().contains("test=\"count")) {
            total = JSONArray.parseArray(data.getString("td")).size();
        }
        if (!StringUtil.isEmpty(sql) && !sql.contains("limit #page#,#row#")) {
            sql = sql + " limit #page#,#row#";
            data = queryServiceSMOImpl.execQuerySql(reqJson, sql);
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) total / (double) reqJson.getInteger("row")), total, data);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
