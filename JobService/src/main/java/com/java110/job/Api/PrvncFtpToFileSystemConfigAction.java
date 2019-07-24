package com.java110.job.Api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.util.SpringBeanInvoker;
import com.java110.job.common.CustomizedPropertyPlaceholderConfigurer;
import com.java110.job.dao.IPrvncFtpFileDAO;
import com.java110.job.task.PrvncFtpToFileSystemJob;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.util.ValidatorUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将ftp上的文件保存到支持的文件系统
 * 
 * @author wuxw7 add by 20170103
 * 
 */
@RestController
public class PrvncFtpToFileSystemConfigAction{

	private static final Logger logger = LoggerFactory.getLogger(PrvncFtpToFileSystemConfigAction.class);

	private static final String defaultCronExpression = "0 * * * * ?";// 每分钟执行一次

	private static final String prefixJobName = "PrvncFtpToSystem_"; // job
	// 名称前缀，防止和其他的job名称产生冲突

	private static final String RUNFLAG_START = "1";

	private static final String RUNFLAG_STOP = "0";
	@Autowired
	private IPrvncFtpFileDAO iprvncFtpFileDAO;
	// 每页数据条数
	private static int pageSize = 20;

	public JSONObject resultMsg;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 查询配置的需要下载任务的列表
	 * 
	 * @return
	 */
	@RequestMapping(path = "/FtpToFileSystem/queryFtpItems" , method = RequestMethod.POST)
	public String queryFtpItems(@RequestBody String queryftpinfo,HttpServletRequest request) {

		JSONObject queryftp = JSONObject.parseObject(queryftpinfo);
		String curPage = queryftp.getString("curPage");
		curPage = curPage == null || "".equals(curPage) ? "1" : curPage;

		// 查询在用状态时的下载任务列表
		Map info = new HashMap();
		info.put("curPage", curPage);
		info.put("pageSize", pageSize);
		Map resultInfo = getPrvncFtpFileDAO().queryFtpItems(info);

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
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for (int itemIndex = 0; itemIndex < ftpItems.size(); itemIndex++) {

				// 处理时间显示和界面显示传输类型
				Map ftpItemMap = ftpItems.get(itemIndex);
				ftpItemMap.put("U_OR_D_NAME", "task.tamplete.name." + ftpItemMap.get("U_OR_D"));// 暂且写死，最终还是读取配置
				ftpItemMap.put("CREATE_DATE", df.format(ftpItemMap.get("CREATE_DATE")));// 暂且写死，最终还是读取配置
				rows.add(JSONObject.parseObject(JSONObject.toJSONString(ftpItems.get(itemIndex))));
			}
			data.put("rows", rows);
			resultMsg = data;
			return data.toString();
		}
		data.put("rows", "[]");
		resultMsg = data;
		return data.toString();
	}

	/**
	 * 增加Ftp配置
	 * 
	 * @return
	 */
	@RequestMapping(path = "/FtpToFileSystem/addFtpItem" , method = RequestMethod.POST)
	public String addFtpItem(@RequestBody String addFtpIteminfo,HttpServletRequest request) {
		JSONObject addFtpItem = JSONObject.parseObject(addFtpIteminfo);
		// 请求参数
		String ftpItemJson = addFtpItem.getString("ftpItemJson");
		JSONObject ftpItemJsonObj = null;
		try {
			// 校验格式是否正确
			ftpItemJsonObj = JSONObject.parseObject(ftpItemJson);
		} catch (Exception e) {
			logger.error("传入参数格式不正确：" + ftpItemJson, e);
			resultMsg = createResultMsg("1999", "传入参数格式不正确：" + ftpItemJson, "");
			return "addFtpItem";
		}

		// 将ftpItemJson装为Map保存操作
		Map paramIn = JSONObject.parseObject(ftpItemJsonObj.getJSONObject("taskInfo").toJSONString(), Map.class);

		// 数据规范性校验

		// 在prvncCrm.properties 文件中获取对应处理类
		Object dealClassObj = CustomizedPropertyPlaceholderConfigurer.getContextProperty("task.deal.class." + paramIn.get("uOrD"));
		// Object dealClassObj = "provInner.DownloadFileFromFtpToTFS";
		if (dealClassObj == null) {
			resultMsg = this.createResultMsg("1999", "对应模板不存在，请联系管理员", "");
			return "addFtpItem";
		}
		String dealClass = dealClassObj.toString();

		long taskId = getPrvncFtpFileDAO().newCreateTaskId();
		// 保存数据
		paramIn.put("taskId", taskId);
		paramIn.put("dealClass", dealClass);

		int addFtpItemFlag = getPrvncFtpFileDAO().addFtpItem(paramIn);

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
			int addFtpItemAttrFlag = getPrvncFtpFileDAO().addFtpItemAttrs(taskAttrsList);
			if (addFtpItemAttrFlag > 0) {
				resultMsg = this.createResultMsg("0000", "成功", ftpItemJson);
			} else {
				resultMsg = this.createResultMsg("1999", "保存属性失败", "");
			}
			return resultMsg.toString();
		}
		resultMsg = this.createResultMsg("1999", "保存数据失败", "");
		return resultMsg.toString();
	}

	/**
	 * 编辑Ftp配置，编辑时将会修改所有的ftpItem 信息，所以传递是所有字段需要传递全
	 * 
	 * @return
	 */
	@RequestMapping(path = "/FtpToFileSystem/editFtpItem",method = RequestMethod.POST)
	public String editFtpItem(@RequestBody String editFtpItem,HttpServletRequest request) {
		JSONObject editftpitem =  JSONObject.parseObject(editFtpItem);
		// 请求参数为{"taskId":"12","taskName":"经办人照片同步处理","ftpUserName":"weblogic",.....}
		String ftpItemJson = editftpitem.getString("ftpItemJson");
		JSONObject ftpItemJsonObj = null;
		try {
			// 校验格式是否正确
			ftpItemJsonObj = JSONObject.parseObject(ftpItemJson);
		} catch (Exception e) {
			logger.error("传入参数格式不正确：" + ftpItemJson, e);
			resultMsg = createResultMsg("1999", "传入参数格式不正确：" + ftpItemJson, "");
			return "editFtpItem";
		}

		// 将ftpItemJson装为Map保存操作
		Map paramIn = JSONObject.parseObject(ftpItemJsonObj.getJSONObject("taskInfo").toJSONString(), Map.class);

		// 在prvncCrm.properties 文件中获取对应处理类
		Object dealClassObj = CustomizedPropertyPlaceholderConfigurer.getContextProperty("task.deal.class." + paramIn.get("uOrD"));
		// Object dealClassObj = "provInner.DownloadFileFromFtpToTFS";
		if (dealClassObj == null) {
			resultMsg = this.createResultMsg("1999", "对应模板不存在，请联系管理员", "");
			return "editFtpItem";
		}
		String dealClass = dealClassObj.toString();

		paramIn.put("dealClass", dealClass);

		// 根据taskId 查询记录是否存在，如果不存在直接返回失败
		Map ftpItem = getPrvncFtpFileDAO().queryFtpItemByTaskId(paramIn);
		// 判断是否有对应的数据
		if (ftpItem != null && ftpItem.containsKey("TASKID")) {
			// 更新数据
			int updateFtpItemFlag = getPrvncFtpFileDAO().updateFtpItemByTaskId(paramIn);
			if (updateFtpItemFlag > 0) {
				// 首先先删除
				getPrvncFtpFileDAO().deleteFtpItemAttrsbyTaskId(paramIn);
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
				int addFtpItemAttrFlag = getPrvncFtpFileDAO().addFtpItemAttrs(taskAttrsList);
				if (addFtpItemAttrFlag > 0) {
					resultMsg = this.createResultMsg("0000", "成功", ftpItemJson);
				} else {
					resultMsg = this.createResultMsg("1999", "更新属性失败", "");
				}
				return "editFtpItem";
			}
			resultMsg = this.createResultMsg("1999", "修改的数据不存在或修改失败", "");
			return "editFtpItem";
		}
		resultMsg = this.createResultMsg("1999", "未找到对应的数据更新失败【" + paramIn.get("taskId") + "】", "");
		return "editFtpItem";
	}

	/**
	 * 删除ftp配置
	 * 
	 * @return
	 */
	@RequestMapping(path = "/FtpToFileSystem/deleteFtpItem",method = RequestMethod.POST)
	public String deleteFtpItem(@RequestBody String deleteFtpItemInfo,HttpServletRequest request) {

		JSONObject delteftpinfo = JSONObject.parseObject(deleteFtpItemInfo);
		// 请求参数为{"tasks":[{"taskId":1},{"taskId":2}],"state":"DELETE"}
		String ftpItemJson = delteftpinfo.getString("ftpItemJson");
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
			return "deleteFtpItem";
		}

		// 传入报文不为空
		if (paramIn == null || !paramIn.containsKey("tasks") || !paramIn.containsKey("state")) {

			resultMsg = createResultMsg("1999", "传入参数格式不正确(必须包含tasks 和  state节点)：" + ftpItemJson, "");
			return "deleteFtpItem";
		}

		// 校验当前是否为启动侦听
		if (!"DELETE".equals(paramIn.get("state"))) {

			resultMsg = createResultMsg("1999", "传入参数格式不正确(state的值必须是DELETE)：" + ftpItemJson, "");
			return "deleteFtpItem";
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
		int updateFtpItemFlag = getPrvncFtpFileDAO().deleteFtpItemByTaskId(paramInfo);
		if (updateFtpItemFlag > 0) {
			resultMsg = this.createResultMsg("0000", "成功", ftpItemJson);
			return "deleteFtpItem";
		}

		resultMsg = this.createResultMsg("1999", "删除数据已经不存在，或删除失败", "");

		return "deleteFtpItem";
	}

	/**
	 * 根据taskId 获取 ftp配置信息
	 * 
	 * @return
	 */

	@RequestMapping(path = "/FtpToFileSystem/queryFtpItemByTaskId" , method = RequestMethod.POST)
	public String queryFtpItemByTaskId(@RequestBody String queryFtpItemByTaskId,HttpServletRequest request) {
		JSONObject queryFtpItemByTask = JSONObject.parseObject(queryFtpItemByTaskId);
		// 请求参数
		String ftpItemJson = queryFtpItemByTask.getString("ftpItemJson");
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
		Map ftpItem = getPrvncFtpFileDAO().queryFtpItemByTaskId(paramIn);
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

	@RequestMapping(path = "/FtpToFileSystem/questTaskTample" , method = RequestMethod.POST)
	public String questTaskTample(@RequestBody String questTaskTample,HttpServletRequest request) {
		JSONObject questtasktample = JSONObject.parseObject(questTaskTample);
		// 请求参数
		String ftpItemJson = questtasktample.getString("ftpItemJson");
		// 请求参数为{"taskId":"12"}
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
			return "questTaskTample";
		}

		String tample = paramIn.getString("uOrD");

		Map info = new HashMap();
		info.put("domain", tample);

		List<Map> itemSpecs = getPrvncFtpFileDAO().queryItemSpec(info);
		String taskItems = JSONObject.toJSONString(itemSpecs);

		resultMsg = this.createResultMsg("0000", "成功", "{\"U_OR_D\":\"" + tample + "\",\"TASK_ITEMS\":" + taskItems + "}");
		return "questTaskTample";
	}

	/**
	 * 根据TaskId 获取任务属性
	 * 
	 * @return
	 */
	@RequestMapping(path = "/FtpToFileSystem/queryTaskAttrs" , method = RequestMethod.POST)
	public String queryTaskAttrs(@RequestBody String queryTaskAttrsInfo,HttpServletRequest request) {

		JSONObject querytaskattrs = JSONObject.parseObject(queryTaskAttrsInfo);

		// 请求参数为{"taskId":"12"}
		String ftpItemJson = querytaskattrs.getString("ftpItemJson");
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
			return "questTaskTample";
		}

		long taskId = paramIn.getLong("taskId");

		Map info = new HashMap();
		info.put("taskId", taskId);

		List<Map> itemAttrs = getPrvncFtpFileDAO().queryFtpItemAttrsByTaskId(info);
		String itemsAttrs = JSONObject.toJSONString(itemAttrs);

		resultMsg = this.createResultMsg("0000", "成功", "{\"TASK_ATTRS\":" + itemsAttrs + "}");
		return "queryTaskAttrs";
	}

	/**
	 * 启动侦听（多节点启动）
	 * 
	 * @return
	 */
	@RequestMapping(path = "/FtpToFileSystem/startJob",method = RequestMethod.POST)
	public String startJob(@RequestBody String startInfo , HttpServletRequest request) {

		JSONObject startinfo = JSONObject.parseObject(startInfo);
		// 请求参数为{"tasks":[{"taskId":1},{"taskId":2}],"state":"START"}
		String ftpItemJson = startinfo.getString("ftpItemJson");
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
			return "startJob";
		}

		// 传入报文不为空
		if (paramIn == null || !paramIn.containsKey("tasks") || !paramIn.containsKey("state")) {

			resultMsg = createResultMsg("1999", "传入参数格式不正确(必须包含tasks 和  state节点)：" + ftpItemJson, "");
			return "startJob";
		}

		// 校验当前是否为启动侦听
		if (!"START".equals(paramIn.get("state"))) {

			resultMsg = createResultMsg("1999", "传入参数格式不正确(state的值必须是START)：" + ftpItemJson, "");
			return "startJob";
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

		List<Map> doFtpItems = getPrvncFtpFileDAO().queryFtpItemsByTaskIds(info);

		// 获取Spring调度器
		Scheduler scheduler = (Scheduler) SpringBeanInvoker.getBean("schedulerFactoryBean");
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

				String jobName = prefixJobName + taskId;

				String triggerName = prefixJobName + taskId;

				JobDetail jobDetail = scheduler.getJobDetail(jobName, PrvncFtpToFileSystemJob.JOB_GROUP_NAME);
				// 说明这个没有启动，则需要重新启动，如果启动着不做处理
				if (jobDetail == null) {
					// 任务名称
					String taskCfgName = (String) doFtpItem.get("TASKNAME");

					JobDetail warnJob = new JobDetail(jobName, PrvncFtpToFileSystemJob.JOB_GROUP_NAME, PrvncFtpToFileSystemJob.class);

					warnJob.getJobDataMap().put(PrvncFtpToFileSystemJob.JOB_DATA_CONFIG_NAME, taskCfgName);

					warnJob.getJobDataMap().put(PrvncFtpToFileSystemJob.JOB_DATA_TASK_ID, taskId);

					CronTrigger warnTrigger = new CronTrigger(triggerName, triggerName, cronExpression);

					// 错过执行后，立即执行
					warnTrigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);

					scheduler.scheduleJob(warnJob, warnTrigger);

					// 修改数据状态，将任务数据状态改为运行状态

					Map updateTaskInfo = new HashMap();

					updateTaskInfo.put("taskId", taskId);
					updateTaskInfo.put("runFlag", RUNFLAG_START);
					// 这里更新状态没有成功的，只是在后台打印日志，再前台不进行展示
					int updateTaskStateFlag = getPrvncFtpFileDAO().updateFtpItemByTaskId(updateTaskInfo);
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
			return "startJob";
		}

		if (logger.isDebugEnabled()) {
			logger.debug("---【PrvncFtpToFileSystemConfigAction.startJob】出参为：" + resultMsg, resultMsg);
		}
		return "startJob";
	}

	/**
	 * 停止侦听
	 * 
	 * @return
	 */
	@RequestMapping(path = "/FtpToFileSystem/stopJob",method = RequestMethod.POST)
	public String stopJob(@RequestBody String stopjobInfo,HttpServletRequest request) {

		JSONObject stopjob = JSONObject.parseObject(stopjobInfo);
		// 请求参数为{"tasks":[{"taskId":1},{"taskId":2}],"state":"STOP"}
		String ftpItemJson = stopjob.getString("ftpItemJson");
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
			return "stopJob";
		}

		// 传入报文不为空
		if (paramIn == null || !paramIn.containsKey("tasks") || !paramIn.containsKey("state")) {

			resultMsg = createResultMsg("1999", "传入参数格式不正确(必须包含tasks 和  state节点)：" + ftpItemJson, "");
			return "stopJob";
		}

		// 校验当前是否为启动侦听
		if (!"STOP".equals(paramIn.get("state"))) {

			resultMsg = createResultMsg("1999", "传入参数格式不正确(state的值必须是START)：" + ftpItemJson, "");
			return "stopJob";
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

		List<Map> doFtpItems = getPrvncFtpFileDAO().queryFtpItemsByTaskIds(info);

		// 获取Spring调度器
		Scheduler scheduler = (Scheduler) SpringBeanInvoker.getBean("schedulerFactoryBean");

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
				scheduler.deleteJob(jobName, PrvncFtpToFileSystemJob.JOB_GROUP_NAME);

				// 修改数据状态，将任务数据状态改为运行状态

				Map updateTaskInfo = new HashMap();

				updateTaskInfo.put("taskId", taskId);
				updateTaskInfo.put("runFlag", RUNFLAG_STOP);
				// 这里更新状态没有成功的，只是在后台打印日志，再前台不进行展示
				int updateTaskStateFlag = getPrvncFtpFileDAO().updateFtpItemByTaskId(updateTaskInfo);
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
			return "stopJob";
		}

		if (logger.isDebugEnabled()) {
			logger.debug("---【PrvncFtpToFileSystemConfigAction.startJob】出参为：" + resultMsg, resultMsg);
		}
		return "stopJob";
	}

	/**
	 * 根据任务名称或任务ID模糊查询
	 * 
	 * @return
	 */
	@RequestMapping(path = "/FtpToFileSystem/searchTaskByNameOrId" , method = RequestMethod.POST)
	public String searchTaskByNameOrId(@RequestBody String searchTaskByNameOrIdInfo,HttpServletRequest request) {
		JSONObject searchTaskByNameOrIdObj = JSONObject.parseObject(searchTaskByNameOrIdInfo);
		String ftpItemJson = searchTaskByNameOrIdObj.getString("ftpItemJson");
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
			Map ftpItem = getPrvncFtpFileDAO().queryFtpItemByTaskId(paramIn);
			if (ftpItem != null && ftpItem.containsKey("FTP_ITEM_ATTRS")) {
				ftpItem.remove("FTP_ITEM_ATTRS");// 前台暂时用不到，所以这里将属性移除

				ftpItems = new ArrayList<Map>();
				ftpItems.add(ftpItem);
			}
		} else {
			ftpItems = getPrvncFtpFileDAO().searchFtpItemByTaskName(paramIn);
		}

		JSONArray rows = new JSONArray();
		if (ftpItems != null && ftpItems.size() > 0) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for (Map ftpItemMap : ftpItems) {

				// 处理时间显示和界面显示传输类型
				ftpItemMap.put("U_OR_D_NAME", CustomizedPropertyPlaceholderConfigurer.getContextProperty("task.tamplete.name." + ftpItemMap.get("U_OR_D")));// 暂且写死，最终还是读取配置
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

	public IPrvncFtpFileDAO getPrvncFtpFileDAO() {
		if (this.iprvncFtpFileDAO == null) {
			this.iprvncFtpFileDAO = ((IPrvncFtpFileDAO) SpringBeanInvoker.getBean("provInner.PrvncFtpFileDAO"));
		}
		return iprvncFtpFileDAO;
	}

}
