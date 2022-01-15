package com.java110.job.Api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.job.dao.IHcFtpFileDAO;
import com.java110.job.quartz.TaskSystemJob;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.util.ValidatorUtils;
import org.quartz.*;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author wuxw7 add by 20170103
 * 		   shiyj update by 2019.08.29
 *
 */
@Service
public class HcFtpToFileSystemConfigAction {

	private static final Logger logger = LoggerFactory.getLogger(HcFtpToFileSystemConfigAction.class);

	private static final String defaultCronExpression = "0 * * * * ?";// 每分钟执行一次

	private static final String prefixJobName = "HcFtpToSystem_"; // job
	private static final String triggerNames = "HcFtpToData_"; // job
	// 名称前缀，防止和其他的job名称产生冲突

	private static final String RUNFLAG_START = "1";

	private static final String RUNFLAG_STOP = "0";

	@Autowired
	private IHcFtpFileDAO iHcFtpFileDAO;
	@Autowired
	private Scheduler scheduler;

	// 每页数据条数
	private static int pageSize = 20;

	public JSONObject resultMsg;

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 查询配置的需要下载任务的列表
	 *
	 * @return
	 */
	public JSONObject queryFtpItems(HttpServletRequest request) {

		String curPage = request.getParameter("curPage");
		curPage = curPage == null || "".equals(curPage) ? "1" : curPage;

		// 查询在用状态时的下载任务列表
		Map info = new HashMap();
		info.put("curPage", (Integer.parseInt(curPage)-1)*pageSize);
		info.put("pageSize", pageSize*Integer.parseInt(curPage));
		Map resultInfo = iHcFtpFileDAO.queryFtpItems(info);

		// 获取总数据数
		int dataCount = resultInfo.get("ITEMSCOUNT") == null ? 0 : Integer.parseInt(resultInfo.get("ITEMSCOUNT").toString());
		// 计算页数
		if (dataCount % pageSize == 0) {
			dataCount /= pageSize;
		} else {
			dataCount = dataCount / pageSize + 1;
		}

		// 获取数据
		List<Map> ftpItems = resultInfo.get("DATA") == null ? null : (List) resultInfo.get("DATA");

		// {"total":10,"rows":[{},{}]}

		JSONObject data = new JSONObject();
		data.put("total", dataCount);
		data.put("currentPage", curPage);
		if (ftpItems != null && ftpItems.size() > 0) {
			JSONArray rows = new JSONArray();
			for (int itemIndex = 0; itemIndex < ftpItems.size(); itemIndex++) {

				// 处理时间显示和界面显示传输类型
				Map ftpItemMap = ftpItems.get(itemIndex);
				ftpItemMap.put("U_OR_D_NAME", ftpItemMap.get("U_OR_D"));// 暂且写死，最终还是读取配置
				ftpItemMap.put("CREATE_DATE", df.format(ftpItemMap.get("CREATE_DATE")));// 暂且写死，最终还是读取配置
				rows.add(JSONObject.parseObject(JSONObject.toJSONString(ftpItems.get(itemIndex))));
			}
			data.put("rows", rows);
			resultMsg = data;
			return data;
		}
		data.put("rows", "[]");
		resultMsg = data;
		return data;
	}

	/**
	 * 增加Ftp配置
	 *
	 * @return
	 */
	public JSONObject addFtpItem(HttpServletRequest request) {

		// 请求参数
		String ftpItemJson = request.getParameter("ftpItemJson");
		JSONObject ftpItemJsonObj = null;
		try {
			// 校验格式是否正确
			ftpItemJsonObj = JSONObject.parseObject(ftpItemJson);
		} catch (Exception e) {
			logger.error("传入参数格式不正确：" + ftpItemJson, e);
			resultMsg = createResultMsg("1999", "传入参数格式不正确：" + ftpItemJson, "");
			return resultMsg;
		}

		// 将ftpItemJson装为Map保存操作
		Map paramIn = JSONObject.parseObject(ftpItemJsonObj.getJSONObject("taskInfo").toJSONString(), Map.class);

		// 数据规范性校验
		Object dealClassObj = null;
		//目前写死他
		if ("DT".equals(paramIn.get("uOrD").toString())) {
			 dealClassObj = "downloadFileFromFtpToTable";
		}else{
			resultMsg = this.createResultMsg("1999", "对应模板不存在，请联系管理员", "");
			return resultMsg;
		}
		// Object dealClassObj = "provInner.DownloadFileFromFtpToTFS";
		if (dealClassObj == null) {
			resultMsg = this.createResultMsg("1999", "对应模板不存在，请联系管理员", "");
			return resultMsg;
		}
		String dealClass = dealClassObj.toString();

		String taskId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_HCJOBId);
		// 保存数据
		paramIn.put("taskId", taskId);
		paramIn.put("dealClass", dealClass);

		int addFtpItemFlag = iHcFtpFileDAO.addFtpItem(paramIn);

		if (addFtpItemFlag > 0) {
			// #taskId#,#itemSpecId#,#value#
			// 保存属性信息
			JSONArray taskAttrs = ftpItemJsonObj.getJSONArray("taskAttrs");
			List<Map> taskAttrsList = new ArrayList<Map>();
			for (int taskAttrIndex = 0; taskAttrIndex < taskAttrs.size(); taskAttrIndex++) {
				JSONObject taskAttr = taskAttrs.getJSONObject(taskAttrIndex);
				Map taskAttrMap = new HashMap();
				taskAttrMap.put("taskId", taskId);
				taskAttrMap.put("itemSpecId", taskAttr.get("itemSpecId"));
				taskAttrMap.put("value", taskAttr.get("value"));
				taskAttrsList.add(taskAttrMap);
			}
			int addFtpItemAttrFlag = iHcFtpFileDAO.addFtpItemAttrs(taskAttrsList);
			if (addFtpItemAttrFlag > 0) {
				resultMsg = this.createResultMsg("0000", "成功", ftpItemJson);
			} else {
				resultMsg = this.createResultMsg("1999", "保存属性失败", "");
			}
			return resultMsg;
		}
		resultMsg = this.createResultMsg("1999", "保存数据失败", "");
		return resultMsg;
	}

	/**
	 * 编辑Ftp配置，编辑时将会修改所有的ftpItem 信息，所以传递是所有字段需要传递全
	 *
	 * @return
	 */
	public JSONObject editFtpItem(HttpServletRequest request) {

		// 请求参数为{"taskId":"12","taskName":"经办人照片同步处理","ftpUserName":"weblogic",.....}
		String ftpItemJson = request.getParameter("ftpItemJson");
		JSONObject ftpItemJsonObj = null;
		try {
			// 校验格式是否正确
			ftpItemJsonObj = JSONObject.parseObject(ftpItemJson);
		} catch (Exception e) {
			logger.error("传入参数格式不正确：" + ftpItemJson, e);
			resultMsg = createResultMsg("1999", "传入参数格式不正确：" + ftpItemJson, "");
			return resultMsg;
		}

		// 将ftpItemJson装为Map保存操作
		Map paramIn = JSONObject.parseObject(ftpItemJsonObj.getJSONObject("taskInfo").toJSONString(), Map.class);


		Object dealClassObj = null;
		//目前写死他
		if ("DT".equals(paramIn.get("uOrD").toString())) {
			dealClassObj = "downloadFileFromFtpToTable";
		}else{
			resultMsg = this.createResultMsg("1999", "对应模板不存在，请联系管理员", "");
			return resultMsg;
		}
		// Object dealClassObj = "provInner.DownloadFileFromFtpToTFS";
		if (dealClassObj == null) {
			resultMsg = this.createResultMsg("1999", "对应模板不存在，请联系管理员", "");
			return resultMsg;
		}
		String dealClass = dealClassObj.toString();

		paramIn.put("dealClass", dealClass);

		// 根据taskId 查询记录是否存在，如果不存在直接返回失败
		Map ftpItem = iHcFtpFileDAO.queryFtpItemByTaskId(paramIn);
		// 判断是否有对应的数据
		if (ftpItem != null && ftpItem.containsKey("TASKID")) {
			// 更新数据
			int updateFtpItemFlag = iHcFtpFileDAO.updateFtpItemByTaskId(paramIn);
			if (updateFtpItemFlag > 0) {
				// 首先先删除
				iHcFtpFileDAO.deleteFtpItemAttrsbyTaskId(paramIn);
				// 保存属性信息
				JSONArray taskAttrs = ftpItemJsonObj.getJSONArray("taskAttrs");
				List<Map> taskAttrsList = new ArrayList<Map>();
				for (int taskAttrIndex = 0; taskAttrIndex < taskAttrs.size(); taskAttrIndex++) {
					JSONObject taskAttr = taskAttrs.getJSONObject(taskAttrIndex);
					Map taskAttrMap = new HashMap();
					taskAttrMap.put("taskId", paramIn.get("taskId"));
					taskAttrMap.put("itemSpecId", taskAttr.get("itemSpecId"));
					taskAttrMap.put("value", taskAttr.get("value"));
					taskAttrsList.add(taskAttrMap);
				}
				int addFtpItemAttrFlag = iHcFtpFileDAO.addFtpItemAttrs(taskAttrsList);
				if (addFtpItemAttrFlag > 0) {
					resultMsg = this.createResultMsg("0000", "成功", ftpItemJson);
				} else {
					resultMsg = this.createResultMsg("1999", "更新属性失败", "");
				}
				return resultMsg;
			}
			resultMsg = this.createResultMsg("1999", "修改的数据不存在或修改失败", "");
			return resultMsg;
		}
		resultMsg = this.createResultMsg("1999", "未找到对应的数据更新失败【" + paramIn.get("taskId") + "】", "");
		return resultMsg;
	}

	/**
	 * 删除ftp配置
	 *
	 * @return
	 */
	public JSONObject deleteFtpItem(HttpServletRequest request) {

		// 请求参数为{"tasks":[{"taskId":1},{"taskId":2}],"state":"DELETE"}
		String ftpItemJson = request.getParameter("ftpItemJson");
		if (logger.isDebugEnabled()) {
			logger.debug("---【PrvncFtpToFileSystemConfigAction.deleteFtpItem】入参为：" + ftpItemJson, ftpItemJson);
		}

		JSONObject paramIn = null;

		try {
			// 校验格式是否正确
			paramIn = JSONObject.parseObject(ftpItemJson);
		} catch (Exception e) {
			logger.error("传入参数格式不正确：" + ftpItemJson, e);
			resultMsg = createResultMsg("1999", "传入参数格式不正确：" + ftpItemJson + e, "");
			return resultMsg;
		}

		// 传入报文不为空
		if (paramIn == null || !paramIn.containsKey("tasks") || !paramIn.containsKey("state")) {

			resultMsg = createResultMsg("1999", "传入参数格式不正确(必须包含tasks 和  state节点)：" + ftpItemJson, "");
			return resultMsg;
		}

		// 校验当前是否为启动侦听
		if (!"DELETE".equals(paramIn.get("state"))) {

			resultMsg = createResultMsg("1999", "传入参数格式不正确(state的值必须是DELETE)：" + ftpItemJson, "");
			return resultMsg;
		}

		// 查询需要操作的任务
		JSONArray taskInfos = paramIn.getJSONArray("tasks");
		String taskIds = "";

		for (int taskIndex = 0; taskIndex < taskInfos.size(); taskIndex++) {
			taskIds += (taskInfos.getJSONObject(taskIndex).getString("taskId") + ",");
		}

		if (taskIds.length() > 0) {
			taskIds = taskIds.substring(0, taskIds.length() - 1);
		}
		// 将ftpItemJson装为Map保存操作
		Map paramInfo = new HashMap();
		paramInfo.put("taskIds", taskIds.split(","));
		// 更新数据
		int updateFtpItemFlag = iHcFtpFileDAO.deleteFtpItemByTaskId(paramInfo);
		if (updateFtpItemFlag > 0) {
			resultMsg = this.createResultMsg("0000", "成功", ftpItemJson);
			return resultMsg;
		}

		resultMsg = this.createResultMsg("1999", "删除数据已经不存在，或删除失败", "");

		return resultMsg;
	}

	/**
	 * 根据taskId 获取 ftp配置信息
	 *
	 * @return
	 */
	public String queryFtpItemByTaskId(HttpServletRequest request) {

		// 请求参数为{"taskId":"12"}
		String ftpItemJson = request.getParameter("ftpItemJson");

		if (logger.isDebugEnabled()) {
			logger.debug("---【PrvncFtpToFileSystemConfigAction.queryFtpItemByTaskId】入参为：" + ftpItemJson, ftpItemJson);
		}
		try {

			// 校验格式是否正确
			JSONObject.parseObject(ftpItemJson);
		} catch (Exception e) {
			logger.error("传入参数格式不正确：" + ftpItemJson, e);
			resultMsg = createResultMsg("1999", "传入参数格式不正确：" + ftpItemJson, "");
			return "queryFtpItemByTaskId";
		}

		// 将ftpItemJson装为Map保存操作
		Map paramIn = JSONObject.parseObject(ftpItemJson, Map.class);

		// 根据taskId 查询记录是否存在，如果不存在直接返回失败
		Map ftpItem = iHcFtpFileDAO.queryFtpItemByTaskId(paramIn);
		// 判断是否有对应的数据
		if (ftpItem != null && ftpItem.containsKey("TASKID")) {
			// 更新数据
			this.createResultMsg("0000", "成功", JSONObject.toJSONString(ftpItem));
			return "queryFtpItemByTaskId";
		}
		resultMsg = this.createResultMsg("1999", "删除数据已经不存在，或删除失败", "");

		return "queryFtpItemByTaskId";
	}

	/**
	 * 查询任务模板 ftpItemJson:{'uOrD':'U'}
	 *
	 * @return
	 */
	public JSONObject questTaskTample(HttpServletRequest request) {

		// 请求参数为{"taskId":"12"}
		String ftpItemJson = request.getParameter("ftpItemJson");

		if (logger.isDebugEnabled()) {
			logger.debug("---【PrvncFtpToFileSystemConfigAction.queryFtpItemByTaskId】入参为：" + ftpItemJson, ftpItemJson);
		}
		JSONObject paramIn = null;
		try {
			// 校验格式是否正确
			paramIn = JSONObject.parseObject(ftpItemJson);
		} catch (Exception e) {
			logger.error("传入参数格式不正确：" + ftpItemJson, e);
			resultMsg = createResultMsg("1999", "传入参数格式不正确：" + ftpItemJson, "");
			return resultMsg;
		}

		String tample = paramIn.getString("uOrD");

		Map info = new HashMap();
		info.put("domain", tample);

		List<Map> itemSpecs = iHcFtpFileDAO.queryItemSpec(info);
		String taskItems = JSONObject.toJSONString(itemSpecs);

		resultMsg = this.createResultMsg("0000", "成功", "{\"U_OR_D\":\"" + tample + "\",\"TASK_ITEMS\":" + taskItems + "}");
		return resultMsg;
	}

	/**
	 * 根据TaskId 获取任务属性
	 *
	 * @return
	 */
	public JSONObject queryTaskAttrs(HttpServletRequest request) {

		// 请求参数为{"taskId":"12"}
		String ftpItemJson = request.getParameter("ftpItemJson");
		if (logger.isDebugEnabled()) {
			logger.debug("---【PrvncFtpToFileSystemConfigAction.queryTaskAttrs】入参为：" + ftpItemJson, ftpItemJson);
		}
		JSONObject paramIn = null;
		try {
			// 校验格式是否正确
			paramIn = JSONObject.parseObject(ftpItemJson);
		} catch (Exception e) {
			logger.error("传入参数格式不正确：" + ftpItemJson, e);
			resultMsg = createResultMsg("1999", "传入参数格式不正确：" + ftpItemJson, "");
			return resultMsg;
		}

		long taskId = paramIn.getLong("taskId");

		Map info = new HashMap();
		info.put("taskId", taskId);

		List<Map> itemAttrs = iHcFtpFileDAO.queryFtpItemAttrsByTaskId(info);
		String itemsAttrs = JSONObject.toJSONString(itemAttrs);

		resultMsg = this.createResultMsg("0000", "成功", "{\"TASK_ATTRS\":" + itemsAttrs + "}");
		return resultMsg;
	}

	/**
	 * 启动侦听（多节点启动）
	 *
	 * @return
	 */
	public JSONObject startJob(HttpServletRequest request) {

		// 请求参数为{"tasks":[{"taskId":1},{"taskId":2}],"state":"START"}
		String ftpItemJson = request.getParameter("ftpItemJson");
		if (logger.isDebugEnabled()) {
			logger.debug("---【PrvncFtpToFileSystemConfigAction.startJob】入参为：" + ftpItemJson, ftpItemJson);
		}
		JSONObject paramIn = null;
		try {
			// 校验格式是否正确
			paramIn = JSONObject.parseObject(ftpItemJson);
		} catch (Exception e) {
			logger.error("传入参数格式不正确：" + ftpItemJson, e);
			resultMsg = createResultMsg("1999", "传入参数格式不正确：" + ftpItemJson, "");
			return resultMsg;
		}

		// 传入报文不为空
		if (paramIn == null || !paramIn.containsKey("tasks") || !paramIn.containsKey("state")) {

			resultMsg = createResultMsg("1999", "传入参数格式不正确(必须包含tasks 和  state节点)：" + ftpItemJson, "");
			return resultMsg;
		}

		// 校验当前是否为启动侦听
		if (!"START".equals(paramIn.get("state"))) {

			resultMsg = createResultMsg("1999", "传入参数格式不正确(state的值必须是START)：" + ftpItemJson, "");
			return resultMsg;
		}

		// 查询需要操作的任务
		JSONArray taskInfos = paramIn.getJSONArray("tasks");

		String taskIds = "";

		for (int taskIndex = 0; taskIndex < taskInfos.size(); taskIndex++) {
			taskIds += (taskInfos.getJSONObject(taskIndex).getString("taskId") + ",");
		}

		if (taskIds.length() > 0) {
			taskIds = taskIds.substring(0, taskIds.length() - 1);
		}

		Map info = new HashMap();

		info.put("taskIds", taskIds.split(","));

		List<Map> doFtpItems = iHcFtpFileDAO.queryFtpItemsByTaskIds(info);

		int linstenCount = 0;
		int updateTaskStateFailCount = 0;
		try {
			for (int doIndex = 0; doIndex < doFtpItems.size(); doIndex++) {
				Map doFtpItem = doFtpItems.get(doIndex);

				// 侦听运行状态
				String runState = doFtpItem.get("RUN_STATE") == null ? "1" : doFtpItem.get("RUN_STATE").toString();// 如果为空，为了操作正常，默认为1，也就是启动状态，在后面再关闭一次

				// 获取taskId
				String taskId = doFtpItem.get("TASKID").toString();// 这个就不用三目判断了，因为如果为空，则直接抛出异常，正常情况下不会出现空的情况

				// 获取定时时间
				String cronExpression = doFtpItem.get("TASKCRON") == null ? defaultCronExpression : doFtpItem.get("TASKCRON").toString();// 如果没有配置则，每一分运行一次
				// 设置触发时间点
				CronScheduleBuilder cronScheduleBuilder =CronScheduleBuilder.cronSchedule(cronExpression);

				String jobName = prefixJobName + taskId;

				String triggerName = triggerNames + taskId;

				//设置任务名称
				JobKey jobKey = new JobKey(jobName, TaskSystemJob.JOB_GROUP_NAME);
				JobDetail jobDetail = scheduler.getJobDetail(jobKey);

				if (jobDetail == null) {
					// 任务名称
					String taskCfgName = (String) doFtpItem.get("TASKNAME");
					//构建job信息
					JobDetail warnJob = JobBuilder.newJob(TaskSystemJob.class).withIdentity(jobName, TaskSystemJob.JOB_GROUP_NAME).withDescription("任务启动").build();

					warnJob.getJobDataMap().put(TaskSystemJob.JOB_DATA_CONFIG_NAME, taskCfgName);

					warnJob.getJobDataMap().put(TaskSystemJob.JOB_DATA_TASK_ID, taskId);

					// 触发时间点
					CronTrigger warnTrigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerName+"_group").withSchedule(cronScheduleBuilder).build();

					// 错过执行后，立即执行
					//warnTrigger(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
					//交由Scheduler安排触发
					scheduler.scheduleJob(warnJob, warnTrigger);

					// 修改数据状态，将任务数据状态改为运行状态

					Map updateTaskInfo = new HashMap();

					updateTaskInfo.put("taskId", taskId);
					updateTaskInfo.put("runFlag", RUNFLAG_START);
					// 这里更新状态没有成功的，只是在后台打印日志，再前台不进行展示
					int updateTaskStateFlag = iHcFtpFileDAO.updateFtpItemByTaskId(updateTaskInfo);
					if (updateTaskStateFlag < 1) {
						logger.error("---侦听【" + taskId + "】启动成功，但是更新任务状态失败,请关注！！！", info);
						updateTaskStateFailCount++;
					}

				}
				linstenCount++;
			}
			String resultMsgStr = "侦听启动成功，启动目标侦听个数为【" + doFtpItems.size() + "】，成功启动个数为【" + linstenCount + "】";
			if (updateTaskStateFailCount > 0) {
				resultMsgStr += ("，有【" + updateTaskStateFailCount + "】任务启动成功，更新数据失败");
			}
			resultMsg = this.createResultMsg("0000", resultMsgStr, taskIds);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("调度器启动出错：" + ftpItemJson, e);
			resultMsg = createResultMsg("1999", "调度器启动出错：" + e, "");
			return resultMsg;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("---【PrvncFtpToFileSystemConfigAction.startJob】出参为：" + resultMsg, resultMsg);
		}
		return resultMsg;
	}

	/**
	 * 停止侦听
	 *
	 * @return
	 */
	public JSONObject stopJob(HttpServletRequest request) {

		// 请求参数为{"tasks":[{"taskId":1},{"taskId":2}],"state":"STOP"}
		String ftpItemJson = request.getParameter("ftpItemJson");
		if (logger.isDebugEnabled()) {
			logger.debug("---【PrvncFtpToFileSystemConfigAction.stopJob】入参为：" + ftpItemJson, ftpItemJson);
		}
		JSONObject paramIn = null;
		try {
			// 校验格式是否正确
			paramIn = JSONObject.parseObject(ftpItemJson);
		} catch (Exception e) {
			logger.error("传入参数格式不正确：" + ftpItemJson, e);
			resultMsg = createResultMsg("1999", "传入参数格式不正确：" + ftpItemJson, "");
			return resultMsg;
		}

		// 传入报文不为空
		if (paramIn == null || !paramIn.containsKey("tasks") || !paramIn.containsKey("state")) {

			resultMsg = createResultMsg("1999", "传入参数格式不正确(必须包含tasks 和  state节点)：" + ftpItemJson, "");
			return resultMsg;
		}

		// 校验当前是否为启动侦听
		if (!"STOP".equals(paramIn.get("state"))) {

			resultMsg = createResultMsg("1999", "传入参数格式不正确(state的值必须是START)：" + ftpItemJson, "");
			return resultMsg;
		}

		// 查询需要操作的任务
		JSONArray taskInfos = paramIn.getJSONArray("tasks");

		String taskIds = "";

		for (int taskIndex = 0; taskIndex < taskInfos.size(); taskIndex++) {
			taskIds += (taskInfos.getJSONObject(taskIndex).getString("taskId") + ",");
		}

		if (taskIds.length() > 0) {
			taskIds = taskIds.substring(0, taskIds.length() - 1);
		}

		Map info = new HashMap();

		info.put("taskIds", taskIds.split(","));

		List<Map> doFtpItems = iHcFtpFileDAO.queryFtpItemsByTaskIds(info);

		int linstenCount = 0;
		int updateTaskStateFailCount = 0;
		try {
			for (Map doFtpItem : doFtpItems) {

				// 获取taskId
				String taskId = doFtpItem.get("TASKID").toString();// 这个就不用三目判断了，因为如果为空，则直接抛出异常，正常情况下不会出现空的情况

				// 获取定时时间
				String cronExpression = doFtpItem.get("TASKCRON") == null ? defaultCronExpression : doFtpItem.get("TASKCRON").toString();// 如果没有配置则，每一分运行一次

				String jobName = prefixJobName + taskId;

				String triggerName = prefixJobName + taskId;

				TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TaskSystemJob.JOB_GROUP_NAME);
				// 停止触发器
				scheduler.pauseTrigger(triggerKey);
				// 移除触发器
				scheduler.unscheduleJob(triggerKey);

				JobKey jobKey = new JobKey(jobName, TaskSystemJob.JOB_GROUP_NAME);
				// 删除任务
				scheduler.deleteJob(jobKey);
				// 修改数据状态，将任务数据状态改为运行状态

				Map updateTaskInfo = new HashMap();

				updateTaskInfo.put("taskId", taskId);
				updateTaskInfo.put("runFlag", RUNFLAG_STOP);
				// 这里更新状态没有成功的，只是在后台打印日志，再前台不进行展示
				int updateTaskStateFlag = iHcFtpFileDAO.updateFtpItemByTaskId(updateTaskInfo);
				if (updateTaskStateFlag < 1) {
					logger.error("---侦听【" + taskId + "】停止成功，但是更新任务状态失败,请关注！！！", info);
					updateTaskStateFailCount++;
				}
				linstenCount++;

				String resultMsgStr = "侦听停止成功，停止目标侦听个数为【" + doFtpItems.size() + "】，成功停止个数为【" + linstenCount + "】";
				if (updateTaskStateFailCount > 0) {
					resultMsgStr += ("，有【" + updateTaskStateFailCount + "】任务停止成功，更新数据失败");
				}
				resultMsg = this.createResultMsg("0000", resultMsgStr, taskIds);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("调度器停止出错：" + ftpItemJson, e);
			resultMsg = createResultMsg("1999", "调度器停止出错：" + e, "");
			return resultMsg;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("---【PrvncFtpToFileSystemConfigAction.startJob】出参为：" + resultMsg, resultMsg);
		}
		return resultMsg;
	}

	/**
	 * 根据任务名称或任务ID模糊查询
	 *
	 * @return
	 */
	public String searchTaskByNameOrId(HttpServletRequest request) {

		String ftpItemJson = request.getParameter("ftpItemJson");
		JSONObject ftpItemJsonObj = null;
		try {
			// 校验格式是否正确
			// {"taskName":"经办人照片同步处理"}
			ftpItemJsonObj = JSONObject.parseObject(ftpItemJson);
		} catch (Exception e) {
			logger.error("传入参数格式不正确：" + ftpItemJson, e);
			resultMsg = createResultMsg("1999", "传入参数格式不正确：" + ftpItemJson, "");
			return "addFtpItem";
		}

		// 将ftpItemJson装为Map保存操作
		Map paramIn = JSONObject.parseObject(ftpItemJsonObj.toJSONString(), Map.class);

		String taskNameOrTaskId = paramIn.get("taskName") == null ? "1" : paramIn.get("taskName").toString();

		taskNameOrTaskId = ValidatorUtils.getValueAsString(paramIn, "taskName");

		// 规则校验
		JSONObject data = new JSONObject();
		data.put("total", 1); // 搜索不进行分页处理
		data.put("currentPage", 1);
		List<Map> ftpItems = null;
		// 说明是taskId
		if (GenericValidator.isInt(taskNameOrTaskId) || GenericValidator.isLong(taskNameOrTaskId)) {
			// 根据taskId 查询记录
			paramIn.put("taskId", taskNameOrTaskId);
			Map ftpItem = iHcFtpFileDAO.queryFtpItemByTaskId(paramIn);
			if (ftpItem != null && ftpItem.containsKey("FTP_ITEM_ATTRS")) {
				ftpItem.remove("FTP_ITEM_ATTRS");// 前台暂时用不到，所以这里将属性移除

				ftpItems = new ArrayList<Map>();
				ftpItems.add(ftpItem);
			}
		} else {
			ftpItems = iHcFtpFileDAO.searchFtpItemByTaskName(paramIn);
		}

		JSONArray rows = new JSONArray();
		if (ftpItems != null && ftpItems.size() > 0) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for (Map ftpItemMap : ftpItems) {

				// 处理时间显示和界面显示传输类型
				ftpItemMap.put("U_OR_D_NAME", ftpItemMap.get("U_OR_D"));// 暂且写死，最终还是读取配置
				ftpItemMap.put("CREATE_DATE", df.format(ftpItemMap.get("CREATE_DATE")));// 暂且写死，最终还是读取配置
				rows.add(JSONObject.parseObject(JSONObject.toJSONString(ftpItemMap)));
			}
		}
		data.put("rows", rows);
		resultMsg = data;
		return "searchTaskByNameOrId";
	}

	/**
	 * 创建公用输出
	 *
	 * @return
	 */
	private JSONObject createResultMsg(String resultCode, String resultMsg, String resultInfo) {
		JSONObject data = new JSONObject();
		data.put("RESULT_CODE", resultCode);
		data.put("RESULT_MSG", resultMsg);
		data.put("RESULT_INFO", resultInfo);
		return data;
	}

}
