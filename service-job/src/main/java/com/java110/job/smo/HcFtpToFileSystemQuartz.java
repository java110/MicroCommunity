package com.java110.job.smo;
import com.java110.utils.constant.RuleDomain;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.job.dao.IHcFtpFileDAO;

import com.java110.job.model.FtpTaskLog;
import com.java110.job.model.FtpTaskLogDetail;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author
 *
 */
public abstract class HcFtpToFileSystemQuartz{

    protected static final Logger logger = LoggerFactory.getLogger(HcFtpToFileSystemQuartz.class);
    @Autowired
    private IHcFtpFileDAO iHcFtpFileDAO;
    @Autowired
    private IHcFtpFileSMO iHcFtpFileSMO;

    /*private IPrvncDumpSMO prvncDumpSMO;*/
    // 运行状态，R：正在执行 T：等待运行 TD1:文件下载失败 TD2:文件内容保存失败 TU1:数据文件生成失败 TU2:数据文件上传失败
    private static final String TASK_STATE_R = "R";// 正在运行
    private static final String TASK_STATE_T = "T";// 等待运行
    private static final String TASK_STATE_E1 = "E1";// 执行事前过程失败
    private static final String TASK_STATE_E2 = "E2";// 处理数据失败
    private static final String TASK_STATE_E3 = "E3";// 执行事后过程失败

    public void initTask() {

        // 将所有的任务状态改为等待运行状态
        Map paramIn = new HashMap();
        paramIn.put("oldRunState", "R");
        paramIn.put("runState", "T");
        int updateFtpItemRunStateFlag = iHcFtpFileDAO.updateFtpItemRunState(paramIn);
        if (updateFtpItemRunStateFlag < 1) {
            logger.error("--【PrvncFtpToFileSystemQuartz.initTask】,没有需要更新的内容（没有下载一半后停止应用的情况）", paramIn);
        }
    }

    /**
     * 启动任务
     *
     * @param ftpItemConfigInfo
     */
    public  void startFtpTask(Map ftpItemConfigInfo) throws Exception {

        // 这么做是为了，单线程调用，防止多线程导致数据重复处理
        if (!ftpItemConfigInfo.containsKey("RUN_STATE") || "R".equals(ftpItemConfigInfo.get("RUN_STATE"))) {
            return;
        }

        long taskId = Long.parseLong(ftpItemConfigInfo.get("TASKID").toString());

        if (logger.isDebugEnabled()) {
            logger.debug("---【PrvncFtpToFileSystemQuartz.startFtpTask】：任务【" + taskId + "】开始运行！", taskId);
        }

        // 保存任务执行主要日志信息
        //获取LOGID 默认生成规则为tadkid去掉年月日之前的值+66
        String id = ftpItemConfigInfo.get("TASKID").toString();
        id = id.substring(10,id.length());
        long logid = Long.parseLong (id+"22");
        ftpItemConfigInfo.put("logid",logid);
        long taskLogID = insertTaskInfo(ftpItemConfigInfo);

        ftpItemConfigInfo.put("logid", taskLogID);
        ftpItemConfigInfo.put("taskid", taskId);
        ftpItemConfigInfo.put("threadrunstate", TASK_STATE_R);
        ftpItemConfigInfo.put("tnum", 1);
        // 修改任务状态为正在执行状态
        updateTaskState(taskId, TASK_STATE_R);
        // 方法调用是否成功，S成功（默认），E表示失败（在方法中失败时，需要修改）
        ftpItemConfigInfo.put("PRE_METHOD_FLAG", "S");
        try {
            // 1.0空方法，让子类去实现
            prepare(ftpItemConfigInfo);

            // 2.0调用事前过程
            if (ftpItemConfigInfo.containsKey("PREFLAG") && "0".equals(ftpItemConfigInfo.get("PREFLAG"))) {
                callPreFunction(ftpItemConfigInfo);
            }

            if (ftpItemConfigInfo.containsKey("PRE_METHOD_FLAG") && "E".equals(ftpItemConfigInfo.get("PRE_METHOD_FLAG"))) {
                // 此时调用事前过程失败，直接返回 查询标识为E，更新日志
                udpateTaskLog(ftpItemConfigInfo);
                updateTaskState(taskId, TASK_STATE_E1);
                return;
            }

            // 3.0核心业务处理逻辑，需要子类去实现
            process(ftpItemConfigInfo);

            if (ftpItemConfigInfo.containsKey("PRE_METHOD_FLAG") && "E".equals(ftpItemConfigInfo.get("PRE_METHOD_FLAG"))) {
                // 程序处理失败，直接返回
                ftpItemConfigInfo.put("threadrunstate", TASK_STATE_E2);
                updateTaskState(taskId, TASK_STATE_E2);
                udpateTaskLog(ftpItemConfigInfo);
                saveTaskLogDetail(ftpItemConfigInfo);// 保存detail
                return;
            }
            // 记录详细日志
            ftpItemConfigInfo.put("threadrunstate", "T");
            saveTaskLogDetail(ftpItemConfigInfo);

            // 4.0调用事后过程
            if (ftpItemConfigInfo.containsKey("AFTERFLAG") && "0".equals(ftpItemConfigInfo.get("AFTERFLAG"))) {
                callAfterFunction(ftpItemConfigInfo);
            }
            if (ftpItemConfigInfo.containsKey("PRE_METHOD_FLAG") && "E".equals(ftpItemConfigInfo.get("PRE_METHOD_FLAG"))) {
                // 此时调用事前过程失败，直接返回 查询标识为E，更新日志
                udpateTaskLog(ftpItemConfigInfo);
                updateTaskState(taskId, TASK_STATE_E3);
                return;
            }
            // 5.0空方法，让子类去实现
            post(ftpItemConfigInfo);
        } catch (Exception ex) {
            ftpItemConfigInfo.put("threadrunstate", TASK_STATE_E2);
            udpateTaskLog(ftpItemConfigInfo);
            ftpItemConfigInfo.put("threadrunstate", TASK_STATE_E2);
            ftpItemConfigInfo.put("remark", ex);
            saveTaskLogDetail(ftpItemConfigInfo);
            updateTaskState(taskId, TASK_STATE_E2);
            // 接续向外抛出去
            logger.error("处理出现问题：", ex);
            return;
        }

        // 修改任务状态为执行完毕状态
        updateTaskState(taskId, TASK_STATE_T);
        ftpItemConfigInfo.put("threadrunstate", TASK_STATE_T);
        udpateTaskLog(ftpItemConfigInfo);

        // 发送任务运行结果通知短信给相关人员 **暂时不调用短信
        if (!TASK_STATE_T.equals(ftpItemConfigInfo.get("RUN_STATE").toString())) {
            /*sendErrLogPhoneMsg(ftpItemConfigInfo, taskLogID);*/
        }

    }

    // 如果有事前存过需要调用，则先调用存过
    public void callPreFunction(Map taskInfo) {

        if (taskInfo.containsKey("PREFUNCTION") && taskInfo.get("PREFUNCTION") != null && !"".equals(taskInfo.get("PREFUNCTION"))) {
            try {
                iHcFtpFileSMO.saveDbFunction(taskInfo.get("PREFUNCTION").toString());
                taskInfo.put("threadrunstate", "T");
                taskInfo.put("remark", "调用事前存过结束");
                saveTaskLogDetail(taskInfo);
            } catch (Exception ex) {
                logger.error("调用事前存过失败:", ex);
                taskInfo.put("threadrunstate", "E1");
                taskInfo.put("remark", "调用事前存过失败" + ex);
                taskInfo.put("PRE_METHOD_FLAG", "E");
                saveTaskLogDetail(taskInfo);
            }
        }
    }

    /**
     * 主要业务处理（上传下载）,让子类去实现
     *
     * @param ftpItemConfigInfo
     */
    protected abstract void process(Map ftpItemConfigInfo) throws Exception;

    // 如果有事后存过需要调用，则调用存过
    public void callAfterFunction(Map taskInfo) {

        if (taskInfo.containsKey("AFTERFUNCTION") && taskInfo.get("AFTERFUNCTION") != null && !"".equals(taskInfo.get("AFTERFUNCTION"))) {
            try {
                taskInfo.put("functionname", taskInfo.get("AFTERFUNCTION"));
                // taskInfo 参数param需要在process方法中需要自己写入
                iHcFtpFileSMO.saveDbFunctionWithParam(taskInfo);
                taskInfo.put("threadrunstate", "T");
                taskInfo.put("remark", "调用事后存过结束");
                saveTaskLogDetail(taskInfo);
            } catch (Exception ex) {
                ex.printStackTrace();
                taskInfo.put("threadrunstate", "E3");
                taskInfo.put("remark", "调用事后存过失败" + ex);
                taskInfo.put("PRE_METHOD_FLAG", "E");
                saveTaskLogDetail(taskInfo);
            }
        }
    }

    /**
     * 修改任务状态
     *
     * @return
     */
    private void updateTaskState(long taskId, String state) {
        Map info = new HashMap();

        info.put("taskId", taskId);
        info.put("runState", state);
        int updateFtpItemFlag = iHcFtpFileDAO.updateFtpItemByTaskId(info);
        // 这里只是后台提示，不进行日志保存
        if (updateFtpItemFlag < 1) {
            logger.error("---【PrvncFtpToFileSystemQuartz.updateTaskState】修改任务【" + taskId + "】的状态失败", info);
        }
    }

    /**
     * 修改任务执行日志的状态
     */
    private void udpateTaskLog(Map taskInfo) {
        FtpTaskLog loginfo = new FtpTaskLog();
        loginfo.setLogid(Long.valueOf(taskInfo.get("logid").toString()));
        loginfo.setState(taskInfo.get("threadrunstate").toString());
        iHcFtpFileSMO.updateTaskRunLog(loginfo);
    }

    /**
     * 保存任务执行的详细日志
     */
    protected void saveTaskLogDetail(Map taskInfo) {
        FtpTaskLogDetail logdetail = new FtpTaskLogDetail();
        logdetail.setId(Long.valueOf(taskInfo.get("logid").toString()+"66"));
        logdetail.setLogid(Long.valueOf(taskInfo.get("logid").toString()));
        logdetail.setTaskid(Long.valueOf(taskInfo.get("taskid").toString()));
        logdetail.setState((String) taskInfo.get("threadrunstate"));
        logdetail.setTnum(Integer.valueOf(taskInfo.get("tnum").toString()));
        if (taskInfo.get("begin") != null) {
            logdetail.setBegin(Long.valueOf(taskInfo.get("begin").toString()));
        }
        if (taskInfo.get("end") != null) {
            logdetail.setEnd(Long.valueOf(taskInfo.get("end").toString()));
        }
        if (taskInfo.get("havedown") != null) {
            logdetail.setHavedown(Long.valueOf(taskInfo.get("havedown").toString()));
        }
        logdetail.setRemark(taskInfo.get("remark") == null ? "" : (taskInfo.get("remark").toString().trim().length() > 2000 ? taskInfo.get("remark").toString().trim().substring(0,
                1600) : taskInfo.get("remark").toString().trim()));
        logdetail.setData(taskInfo.get("data") == null ? "" : taskInfo.get("data").toString());
        logdetail.setServerfilename(taskInfo.get("serverfilename") == null ? "" : taskInfo.get("serverfilename").toString());
        logdetail.setLocalfilename(taskInfo.get("localfilename") == null ? "" : taskInfo.get("localfilename").toString());
        int logdetailid = iHcFtpFileSMO.saveTaskRunDetailLog(logdetail);
        taskInfo.put("logdetailid", logdetailid);
    }

    // /**
    // * 修改任务执行的详细日志的状态
    // */
    // private void updateTaskLogDetail(Map taskInfo){
    // FtpTaskLogDetail logdetail=new FtpTaskLogDetail();//
    // logdetail.setId(Long.valueOf(taskInfo.get("logdetailid").toString()));
    // logdetail.setState(taskInfo.get("threadrunstate").toString());
    // logdetail.setRemark((String)taskInfo.get("remark"));
    // logdetail.setData((String)taskInfo.get("data"));
    // if(taskInfo.get("downedlength")!=null)
    // logdetail.setHavedown(Long.valueOf(taskInfo.get("downedlength").toString()));
    // prvncFtpFileSMO.updateTaskRunDetailLog(logdetail);
    // }

    /**
     * 生成任务执行日志
     */
    private long insertTaskInfo(Map taskInfo) {
        FtpTaskLog loginfo = new FtpTaskLog();
        loginfo.setTaskid(Long.valueOf(taskInfo.get("TASKID").toString()));
        loginfo.setState("R");
        loginfo.setServerfilename("");// taskInfo.get("serverfilename").toString()
        loginfo.setLocalfilename("");// taskInfo.get("localfilename").toString()
        loginfo.setUord(taskInfo.get("U_OR_D").toString());
        return iHcFtpFileSMO.saveTaskRunLog(loginfo);
    }

    /**
     * 如果任务运行有异常，则发送警告短信给配置的手机号码
     */
    private void sendErrLogPhoneMsg(Map taskInfo, long taskLogID) {
        Map msginfo = new HashMap();
        String phone = (String) taskInfo.get("errphone");
        if (phone != null && !"".equals(phone)) {
            String[] phonelist = phone.split(",");
            for (int i = 0; i < phonelist.length; i++) {
                msginfo.put("taskid", taskInfo.get("taskid"));
                msginfo.put("phone", phonelist[i]);
                msginfo.put("msg", "通用FTP数据文件传接任务：" + (String) taskInfo.get("taskname") + "运行提示");

                DateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                String detail = "任务已于" + df.format(new Date()) + "运行完毕。运行过程中出现异常，详情请登录系统查看！";
                msginfo.put("detail", detail);
                /*prvncDumpSMO.saveTaskErrInfoPhoneMsg(msginfo);*/
            }
        }
    }

    /**
     * 处理文件名，校验文件名是中是否存在****（4个），表示通配符，如果不存在就是确定唯一文件名
     * 文件名支持日期型的如CRM_########001.txt 程序处理后是 CRM_20170105001.txt 文件名支持sql 语句生成的
     * 文件名支持通配符的如863_****.txt 程序下载所有以863_开头的文件 863_****001.txt
     * 以863_开头,以001结尾，****001.txt 以001结尾的
     *
     * @param fileName
     * @return
     */
    protected List<String> dealFileName(String fileName) {
        // TODO Auto-generated method stub
        List<String> results = new ArrayList<String>();
        String result = "";
        // 文件中使用的日期
        if (StringUtils.contains(fileName, RuleDomain.REPLAY_TYPE_F)) {
            result = StringUtil.replace(fileName, RuleDomain.REPLAY_TYPE_F, DateUtil.getFormatTimeString(new Date(), "yyyyMMddHHmm"));
        } else if (StringUtils.contains(fileName, RuleDomain.REPLAY_TYPE_E)) {
            result = StringUtil.replace(fileName, RuleDomain.REPLAY_TYPE_E, DateUtil.getFormatTimeString(new Date(), "yyyyMMddHH"));
        } else if (StringUtils.contains(fileName, RuleDomain.REPLAY_TYPE_A)) {
            result = StringUtil.replace(fileName, RuleDomain.REPLAY_TYPE_A, DateUtil.getFormatTimeString(new Date(), "yyyyMMdd"));
        } else if (StringUtils.contains(fileName == null ? "" : fileName.toLowerCase(), RuleDomain.REPLAY_TYPE_SQL)) {
            // 后期改造，文件名如果配置的是sql的话，以sql查询文件名
            List<String> fileNames = this.getPrvncFtpFileDAO().execConfigSql(fileName);
            // if (fileNames != null && fileNames.size() > 0) {
            // result = fileNames.get(0);
            // }
            return fileNames;
        } else {
            result = fileName;
        }
        results.add(result);
        return results;
    }

    /**
     * 空方法，如果在事前过程处理前，还需要做一定的处理，需要子类重写这个方法，实现业务逻辑
     *
     * @param ftpItemConfigInfo
     */
    protected void prepare(Map ftpItemConfigInfo) {

    }

    /**
     * 空方法，如果在事后过程处理完后，还需要做一定的处理，需要子类重写这个方法，实现业务逻辑
     *
     * @param ftpItemConfigInfo
     */
    protected void post(Map ftpItemConfigInfo) {

    }

    public IHcFtpFileDAO getPrvncFtpFileDAO() {
        return iHcFtpFileDAO;
    }

    public void setPrvncFtpFileDAO(IHcFtpFileDAO prvncFtpFileDAO) {
        this.iHcFtpFileDAO = prvncFtpFileDAO;
    }

    public IHcFtpFileSMO getPrvncFtpFileSMO() {
        return iHcFtpFileSMO;
    }

    public void setPrvncFtpFileSMO(IHcFtpFileSMO prvncFtpFileSMO) {
        this.iHcFtpFileSMO = prvncFtpFileSMO;
    }

	/*public IPrvncDumpSMO getPrvncDumpSMO() {
		return prvncDumpSMO;
	}

	public void setPrvncDumpSMO(IPrvncDumpSMO prvncDumpSMO) {
		this.prvncDumpSMO = prvncDumpSMO;
	}
*/
}