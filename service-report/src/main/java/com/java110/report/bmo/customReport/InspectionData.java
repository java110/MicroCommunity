package com.java110.report.bmo.customReport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.db.dao.IQueryServiceDAO;
import com.java110.utils.util.DateUtil;
import org.apache.commons.lang.StringUtils;

import java.util.*;


/**
 * select t.inspection_name '巡检点',t.point_obj_name '位置',ips.staff_name '员工',
 * (select count(1) from inspection_task it
 * INNER JOIN inspection_task_detail itd on it.task_id = itd.task_id and itd.status_cd = '0'
 * where it.inspection_plan_id = ip.inspection_plan_id
 * and itd.inspection_id = t.inspection_id and it.plan_user_id = ips.staff_id
 * and itd.act_user_id is not null
 * ) '已巡检',
 * (select count(1) from inspection_task it
 * INNER JOIN inspection_task_detail itd on it.task_id = itd.task_id and itd.status_cd = '0'
 * where it.inspection_plan_id = ip.inspection_plan_id
 * and itd.inspection_id = t.inspection_id and it.plan_user_id = ips.staff_id
 * and itd.act_user_id is null
 * ) '未巡检',
 * (select itd.description from inspection_task it
 * INNER JOIN inspection_task_detail itd on it.task_id = itd.task_id and itd.status_cd = '0'
 * where it.inspection_plan_id = ip.inspection_plan_id
 * and itd.inspection_id = t.inspection_id and it.plan_user_id = ips.staff_id
 * and itd.act_user_id is not null
 * limit 1
 * ) '状态'
 * from inspection_point t
 * left join inspection_route_point_rel irpr on t.inspection_id = irpr.inspection_id and irpr.status_cd = '0'
 * left join inspection_plan ip on ip.inspection_route_id = irpr.inspection_route_id and ip.status_cd = '0'
 * left join inspection_plan_staff ips on ip.inspection_plan_id = ips.inspection_plan_id and ips.status_cd = '0'
 * <p>
 * where ips.staff_name is not null
 * and t.community_id = #communityId#
 * and t.status_cd = '0'
 * <if test="startTime != null and startTime != ''">
 * and ip.create_time &gt; #startTime#
 * </if>
 * <if test="endTime != null and endTime != ''">
 * and ip.create_time &lt; #endTime#
 * </if>
 * <p>
 * group by t.inspection_name,t.point_obj_name,ips.staff_name
 * order by t.inspection_name
 */

public class InspectionData implements ReportExecute {

    public JSONObject hasInTd(JSONArray tds, Map dataObj) {

        if (tds == null || tds.size() < 1) {
            return null;
        }

        for (int tdIndex = 0; tdIndex < tds.size(); tdIndex++) {
            if (tds.getJSONObject(tdIndex).getString("巡检点").equals(dataObj.get("巡检点"))) {
                return tds.getJSONObject(tdIndex);
            }
        }


        return null;

    }

    public String execute(JSONObject params, IQueryServiceDAO queryServiceDAOImpl) {
        JSONObject paramOut = new JSONObject();

        List sqlParams = new ArrayList();
        String sql = "select t.inspection_name '巡检点',\n" +
                "t.point_obj_name '位置',\n" +
                "ips.staff_name '员工',\n" +
                "(select count(1) from inspection_task it\n" +
                "INNER JOIN inspection_task_detail itd on it.task_id = itd.task_id and itd.status_cd = '0'\n" +
                "where  itd.inspection_id = t.inspection_id and it.plan_user_id = ips.staff_id\n" +
                "and itd.act_user_id is not null\n" +
                "and it.create_time > ?\n" +
                "and it.create_time < ?\n" +
                ") '已巡检',\n" +
                "(select count(1) from inspection_task it\n" +
                "INNER JOIN inspection_task_detail itd on it.task_id = itd.task_id and itd.status_cd = '0'\n" +
                "where  itd.inspection_id = t.inspection_id and it.plan_user_id = ips.staff_id\n" +
                "and itd.act_user_id is null\n" +
                "and it.create_time > ?\n" +
                "and it.create_time < ?\n" +
                ") '未巡检',\n" +
                "(select itd.description from inspection_task it\n" +
                "INNER JOIN inspection_task_detail itd on it.task_id = itd.task_id and itd.status_cd = '0'\n" +
                "where  itd.inspection_id = t.inspection_id and it.plan_user_id = ips.staff_id\n" +
                " and itd.act_user_id is not null\n" +
                " and it.create_time > ?\n" +
                " and it.create_time < ?\n" +
                " limit 1\n" +
                " ) '状态'\n" +
                "  from inspection_point t\n" +
                " left join inspection_route_point_rel irpr on t.inspection_id = irpr.inspection_id and irpr.status_cd = '0'\n" +
                " left join inspection_plan ip on ip.inspection_route_id = irpr.inspection_route_id and ip.status_cd = '0'\n" +
                " left join inspection_plan_staff ips on ip.inspection_plan_id = ips.inspection_plan_id and ips.status_cd = '0'\n" +
                " where ips.staff_name is not null\n" +
                " and t.status_cd = '0'\n" ;
        if (params.containsKey("startTime") && !StringUtils.isEmpty(params.getString("startTime"))) {
            sqlParams.add(params.get("startTime"));
            sqlParams.add(params.get("endTime"));
            sqlParams.add(params.get("startTime"));
            sqlParams.add(params.get("endTime"));
            sqlParams.add(params.get("startTime"));
            sqlParams.add(params.get("endTime"));
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String tomorrow = DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_B);
            sqlParams.add(DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_B));
            sqlParams.add(tomorrow);
            sqlParams.add(DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_B));
            sqlParams.add(tomorrow);
            sqlParams.add(DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_B));
            sqlParams.add(tomorrow);
        }
        if (params.containsKey("communityId") && !StringUtils.isEmpty(params.getString("communityId"))) {
            sql += "and t.`community_id` = ? ";
            sqlParams.add(params.get("communityId"));
        }

        sql += "group by t.inspection_name,t.point_obj_name,ips.staff_name,t.inspection_id,ips.staff_id\n" +
                "order by t.inspection_name";


        List datas = queryServiceDAOImpl.executeSql(sql, sqlParams.toArray());

        System.out.println("datas=" + datas);

        if (datas == null || datas.size() < 1) {
            paramOut.put("total",1);
            paramOut.put("data",new JSONArray());
            return paramOut.toJSONString();
        }

        JSONArray tds = new JSONArray();
        JSONObject td = null;
        for (int dataIndex = 0; dataIndex < datas.size(); dataIndex++) {
            Map dataObj = (Map) datas.get(dataIndex);
            td = hasInTd(tds, dataObj);

            if (td == null) {
                td = new JSONObject(true);
                td.put("巡检点", dataObj.get("巡检点"));
                tds.add(td);
            }

            td.put(dataObj.get("员工").toString(), dataObj.get("已巡检") + "/" + dataObj.get("未巡检"));
            td.put(dataObj.get("员工").toString() + "巡检状态", dataObj.get("状态"));
        }

        paramOut.put("total", params.get("row"));
        paramOut.put("data", tds);

        return paramOut.toJSONString();
    }


}
