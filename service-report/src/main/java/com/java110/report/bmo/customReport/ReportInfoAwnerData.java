package com.java110.report.bmo.customReport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.db.dao.IQueryServiceDAO;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
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

public class ReportInfoAwnerData implements ReportExecute {

    public JSONObject hasInTd(JSONArray tds, Map dataObj) {

        if (tds == null || tds.size() < 1) {
            return null;
        }

        for (int tdIndex = 0; tdIndex < tds.size(); tdIndex++) {
            if (tds.getJSONObject(tdIndex).getString("姓名").equals(dataObj.get("姓名"))
                    && tds.getJSONObject(tdIndex).getString("上报项目").equals(dataObj.get("上报项目"))
                    && tds.getJSONObject(tdIndex).getString("电话").equals(dataObj.get("电话"))
            ) {
                return tds.getJSONObject(tdIndex);
            }
        }


        return null;

    }

    public String execute(JSONObject params, IQueryServiceDAO queryServiceDAOImpl) {
        JSONObject paramOut = new JSONObject();

        List sqlParams = new ArrayList();
        String sql = "select rrs.`name` '上报项目',t.person_name '姓名',t.tel '电话',t.id_card '身份证号',DATE_FORMAT(t.create_time,'%Y-%m-%d %H:%i:%s') '上报时间',rist.title,riav.value_content,\n" +
                "\t\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t\t\t(select '业主' from building_owner bo where bo.link = t.tel and bo.status_cd = '0' and bo.community_id = t.community_id limit 1) '业主'\n" +
                "\t\t\t\t\t\t\t\t,\n" +
                "\t\t\t\t\t\t\t\t\t(select '员工' from u_user u \n" +
                "\t\t\t\t\t\t\t\tinner join s_store_user su on u.user_id = su.user_id and su.status_cd = '0'\n" +
                "\t\t\t\t\t\t\t\t\twhere u.tel = t.tel limit 1) '员工'\n" +
                "                from report_info_answer t\n" +
                "                left join report_info_setting rrs on t.setting_id = rrs.setting_id and rrs.status_cd = '0'\n" +
                "                left join report_info_answer_value riav on t.user_an_id = riav.user_an_id and riav.status_cd = '0'\n" +
                "                LEFT JOIN report_info_setting_title rist on riav.title_id = rist.title_id and rist.status_cd = '0'\n" +
                "                where t.status_cd = '0'\n" +
                "                and t.create_time > ?\n" +
                "                and t.create_time < ?";
        if (params.containsKey("startTime") && !StringUtils.isEmpty(params.getString("startTime"))) {
            sqlParams.add(params.get("startTime"));
            sqlParams.add(params.get("endTime"));
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String tomorrow = DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_B);
            sqlParams.add(DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_B));
            sqlParams.add(tomorrow);
        }
        if (params.containsKey("communityId") && !StringUtils.isEmpty(params.getString("communityId"))) {
            sql += "and t.`community_id` = ? \n";
            sqlParams.add(params.get("communityId"));
        }
        if (params.containsKey("personName") && !StringUtils.isEmpty(params.getString("personName"))) {
            sql += "and t.`person_name` like CONCAT('%',?,'%') \n";
            sqlParams.add(params.get("personName"));
        }
        if (params.containsKey("projectName") && !StringUtils.isEmpty(params.getString("projectName"))) {
            sql += "and rrs.`name` like CONCAT('%',?,'%') \n";
            sqlParams.add(params.get("projectName"));
        }

        sql += " order by t.create_time desc \n";

        List datas = queryServiceDAOImpl.executeSql(sql, sqlParams.toArray());

        System.out.println("datas=" + datas);

        if (datas == null || datas.size() < 1) {
            paramOut.put("toatl", 1);
            paramOut.put("data", new JSONArray());
            return paramOut.toJSONString();
        }

        JSONArray tds = new JSONArray();
        JSONObject td = null;
        for (int dataIndex = 0; dataIndex < datas.size(); dataIndex++) {
            Map dataObj = (Map) datas.get(dataIndex);
            td = hasInTd(tds, dataObj);
            if (td == null) {
                td = new JSONObject(true);
                td.put("上报项目", dataObj.get("上报项目"));
                td.put("姓名", dataObj.get("姓名"));
                td.put("电话", dataObj.get("电话"));
                td.put("身份证号", dataObj.get("身份证号"));
                td.put("上报时间", dataObj.get("上报时间"));
                if(!StringUtil.isNullOrNone(dataObj.get("业主"))){
                    td.put("身份", "业主");
                }else if(!StringUtil.isNullOrNone(dataObj.get("员工"))){
                    td.put("身份", "员工");
                }else{
                    td.put("身份", "访客");
                }
                tds.add(td);
            }
            td.put(dataObj.get("title").toString(), dataObj.get("value_content"));
        }

        paramOut.put("total", params.get("row"));
        paramOut.put("data", tds);

        return paramOut.toJSONString();
    }


}
