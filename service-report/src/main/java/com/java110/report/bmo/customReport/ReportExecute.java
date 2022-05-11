package com.java110.report.bmo.customReport;

import com.alibaba.fastjson.JSONObject;
import com.java110.db.dao.IQueryServiceDAO;
import com.java110.service.context.DataQuery;

public interface ReportExecute {

    String execute(JSONObject params,IQueryServiceDAO queryServiceDAOImpl);
}
