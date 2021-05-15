package com.java110.job.smo;

import com.java110.job.util.FTPClientTemplate;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * 
 * @author
 * 
 */
@Component
public class DownloadFileFromFtpToTable extends HcFtpToFileSystemQuartz {

	private static final String ITEM_SPEC_CD_10011 = "10011";// FTP地址
	private static final String ITEM_SPEC_CD_10012 = "10012";// FTP端口号
	private static final String ITEM_SPEC_CD_10013 = "10013";// FTP账号
	private static final String ITEM_SPEC_CD_10014 = "10014";// FTP密码
	private static final String ITEM_SPEC_CD_10015 = "10015";// FTP路径
	private static final String ITEM_SPEC_CD_10016 = "10016";// 本地路径
	private static final String ITEM_SPEC_CD_10007 = "10007";// 文件头
	private static final String ITEM_SPEC_CD_10008 = "10008";// 分隔符
	private static final String ITEM_SPEC_CD_10009 = "10009";// 总记录数
	private static final String ITEM_SPEC_CD_10010 = "10010";// 处理脚本

	/**
	 * 如果运行失败时，需要在ftpItemConfigInfo 中PRE_METHOD_FLAG 值改成E，remark 备注错误原因
	 */
	@Override
	protected void process(Map ftpItemConfigInfo) throws Exception {
		// TODO Auto-generated method stub

		String taskId = ftpItemConfigInfo.get("TASKID").toString();
		FTPClientTemplate ftpClientTemplate = null;
		// 1.0 读取配置，包括ftp 服务器信息，和tfs相关配置，根据taskId 关联信息
		if (!ftpItemConfigInfo.containsKey("FTP_ITEM_ATTRS") || ftpItemConfigInfo.get("FTP_ITEM_ATTRS") == null) {

			ftpItemConfigInfo.put("PRE_METHOD_FLAG", "E");
			ftpItemConfigInfo.put("remark", "当前ftp任务【" + taskId + "】没有配置属性，在从Ftp文件系统下载文件存到TFS文件系统模板中必须配置属性");
			return;
		}
		List<Map> ftpItemAttrs = (List<Map>) ftpItemConfigInfo.get("FTP_ITEM_ATTRS");
		// FTP_ITEM_ATTRS
		String ftpIp = null;
		int ftpPort = 21;
		String ftpUsername = null;
		String ftpPassword = null;
		String ftpPath = null;
		String titleflag = null;
		String sign = null;
		String linecountflag = null;
		String dbsql = null; // 处理脚本
		String localPath = "";// 本地文件保存路径
		int tnum = ftpItemConfigInfo.get("TNUM") == null ? 1 : Integer.parseInt(ftpItemConfigInfo.get("TNUM").toString());

		// 如果没有配置默认获取指定目录下的所有文件
		String fileName = ftpItemConfigInfo.get("FILE_NAME") == null ? "" : ftpItemConfigInfo.get("FILE_NAME").toString();

		// 文件名如果为空，直接返回再不处理
		if ("".equals(fileName)) {
			ftpItemConfigInfo.put("PRE_METHOD_FLAG", "E");
			ftpItemConfigInfo.put("remark", "当前ftp任务【" + taskId + "】没有配置文件名，在从Ftp文件系统下载文件存到TFS文件系统模板中必须配置文件名");
			return;
		}
		// 获取属性表中的数据
		for (Map ftpItemAttr : ftpItemAttrs) {
			if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10011.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				ftpIp = ftpItemAttr.get("VALUE") == null ? "" : ftpItemAttr.get("VALUE").toString();
			} else if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10012.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				ftpPort = ftpItemAttr.get("VALUE") == null ? 21 : Integer.parseInt(ftpItemAttr.get("VALUE").toString());
			} else if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10013.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				ftpUsername = ftpItemAttr.get("VALUE") == null ? "" : ftpItemAttr.get("VALUE").toString();
			} else if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10014.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				ftpPassword = ftpItemAttr.get("VALUE") == null ? "" : ftpItemAttr.get("VALUE").toString();
			} else if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10015.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				ftpPath = ftpItemAttr.get("VALUE") == null ? "" : ftpItemAttr.get("VALUE").toString();
			} else if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10007.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				titleflag = ftpItemAttr.get("VALUE") == null ? "" : ftpItemAttr.get("VALUE").toString();
			} else if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10008.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				sign = ftpItemAttr.get("VALUE") == null ? "" : ftpItemAttr.get("VALUE").toString();
			} else if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10009.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				linecountflag = ftpItemAttr.get("VALUE") == null ? "" : ftpItemAttr.get("VALUE").toString();
			} else if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10010.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				dbsql = ftpItemAttr.get("VALUE") == null ? "" : ftpItemAttr.get("VALUE").toString();
			} else if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10016.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				localPath = ftpItemAttr.get("VALUE") == null ? "" : ftpItemAttr.get("VALUE").toString();
			}
		}

		// 将 contentSpiltChar totalCount dealSql fileTop属性回写到

		ftpItemConfigInfo.put("sign", sign);
		ftpItemConfigInfo.put("linecountflag", linecountflag);
		ftpItemConfigInfo.put("dbsql", dbsql);
		ftpItemConfigInfo.put("titleflag", titleflag);
		ftpItemConfigInfo.put("localPath", localPath);
		ftpItemConfigInfo.put("port", ftpPort);
		ftpItemConfigInfo.put("username", ftpUsername);
		ftpItemConfigInfo.put("pwd", ftpPassword);
		ftpItemConfigInfo.put("ip", ftpIp);

		// 初始化FTPClientTemplate
		ftpClientTemplate = new FTPClientTemplate(ftpIp, ftpPort, ftpUsername, ftpPassword, ftpPath, null, 0, tnum, 100);

		if (!ftpPath.endsWith("/")) {
			ftpPath += "/";
		}

		// 处理文件名
		List<String> fileNames = dealFileName(fileName);

		// **** 则不支持select出来文件名是list 的情况，如果是list 默认处理第一个
		// 校验文件名是中是否存在****（4个），表示通配符，如果不存在就是确定唯一文件名
		fileName = fileNames.get(0);
		if (fileName != null && fileName.contains("****")) {

			String[] reFileNames = ftpClientTemplate.listNames(ftpPath + fileName, true);
			List<String> fNames = new ArrayList<String>();
			for (int reFileNamesIndex = 0; reFileNamesIndex < reFileNames.length; reFileNamesIndex++) {
				if (reFileNames[reFileNamesIndex] != null && !"".equals(reFileNames[reFileNamesIndex])) {
					fNames.add(reFileNames[reFileNamesIndex].replace(ftpPath, ""));
				}
			}
			// 多文件处理
			anyFilesDownload(ftpItemConfigInfo, taskId, ftpClientTemplate, ftpPath, fNames);
			return;
		}
		// 单文件支持文件名为list,如果是list，则处理list所有文件
		// 单文件处理
		anyFilesDownload(ftpItemConfigInfo, taskId, ftpClientTemplate, ftpPath, fileNames);

	}

	/**
	 * 文件名是中是存在****,多文件下载处理
	 * 
	 * @param ftpItemConfigInfo
	 * @param taskId
	 * @param ftpClientTemplate
	 * @param ftpPath
	 * @param
	 * @param
	 * @param
	 * @throws Exception
	 */
	private void anyFilesDownload(Map ftpItemConfigInfo, String taskId, FTPClientTemplate ftpClientTemplate, String ftpPath, List<String> fileNames) throws Exception {
		// 这种需要列出所有文件，根据名称匹配
		String param = "";
		// 获取FTP上的文件
		List<Map> needDownloadFiles = null;
		String downLoadFailFileNames = "";// 下载失败的fileName
		String downLoadSuccessFileNames = "";// 下载失败的fileName
		for (String fileName : fileNames) {
			if (fileName.length() > 0) {
				// 查询数据库，那写文件还没有下载
				Map logInfo = new HashMap();
				logInfo.put("fileNames", fileName);
				logInfo.put("taskId", taskId);
				needDownloadFiles = this.getPrvncFtpFileDAO().queryFileNamesWithOutFtpLog(logInfo);
			}

			if (needDownloadFiles == null || needDownloadFiles.size() < 1) {
				continue;
			}
			ftpItemConfigInfo.put("newFileName", fileName);

			// 保存文件至table
			Map resultInfo = downLoadFileToTable(ftpPath + fileName, ftpClientTemplate, ftpItemConfigInfo);

			Map remoteFileInfo = new HashMap();

			if (resultInfo.containsKey("SAVE_FILE_FLAG") && "S".equals(resultInfo.get("SAVE_FILE_FLAG"))) {
				param += (fileName + "@@");
				// 将下载成功的文件名需要保存至表中，防止以后重复下载
				remoteFileInfo.put("taskId", taskId);
				remoteFileInfo.put("fileName", fileName);
				saveDownLoadSuccessFile(remoteFileInfo);
				// 记录成功时的文件名
				downLoadSuccessFileNames += (fileName + ",");
			} else {
				// 记录下载失败的文件名
				downLoadFailFileNames += (fileName + ",");
			}
			// }
		}
		// 做这个校验主要为了，如果一个都没有成功，就不去调事后过程
		if ("".equals(param)) {
			ftpItemConfigInfo.put("PRE_METHOD_FLAG", "E");
			ftpItemConfigInfo.put("remark", "当前ftp任务【" + taskId + "】没有可下载的文件，或下载文件时失败");
			return;
		}
		ftpItemConfigInfo.put("PRE_METHOD_FLAG", "S");
		ftpItemConfigInfo.put("remark", "当前ftp任务【" + taskId + "】下载成功{" + downLoadSuccessFileNames + "},失败的{" + downLoadFailFileNames + "}");
		ftpItemConfigInfo.put("param", param);
		return;
	}

	/**
	 * 下载并保存文件至表中。
	 * 
	 * @param remoteFileInfo
	 */
	private void saveDownLoadSuccessFile(Map remoteFileInfo) {
		// TODO Auto-generated method stub
		int addDownloadFlag = this.getPrvncFtpFileDAO().addDownloadFileName(remoteFileInfo);
		if (addDownloadFlag < 1) {
			logger.error("---【DownloadFileFromFtpToTFS.saveDownLoadSuccessFile】保存下载文件名失败", remoteFileInfo);
		}
	}

	/**
	 * 下载文件并且存至tfs文件系统
	 * 
	 * @param remoteFileNameTmp
	 * @param
	 * @return
	 */
	private Map downLoadFileToTable(String remoteFileNameTmp, FTPClientTemplate ftpClientTemplate, Map ftpItemConfigInfo) {
		Map resultInfo = new HashMap();
		String tfsReturnFileName = null;
		long block = 10 * 1024;// 默认
		if (ftpClientTemplate == null) {
			resultInfo.put("SAVE_FILE_FLAG", "E");
			return resultInfo;
		}
		String localPathName = ftpItemConfigInfo.get("localPath").toString().endsWith("/") ? ftpItemConfigInfo.get("localPath").toString()
				+ ftpItemConfigInfo.get("newFileName").toString() : ftpItemConfigInfo.get("localPath").toString() + "/" + ftpItemConfigInfo.get("newFileName").toString();
		ftpItemConfigInfo.put("localfilename", localPathName);// 本地带路径的文件名回写，后面读文件时使用
		try {
			File file = new File(localPathName);
			RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");// 建立随机访问
			FTPClient ftpClient = new FTPClient();
			ftpClient.connect(ftpClientTemplate.getHost(), ftpClientTemplate.getPort());
			if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				if (!ftpClient.login(ftpClientTemplate.getUsername(), ftpClientTemplate.getPassword())) {
					resultInfo.put("SAVE_FILE_FLAG", "E");
					resultInfo.put("remark", "登录失败,用户名【" + ftpClientTemplate.getUsername() + "】密码【" + ftpClientTemplate.getPassword() + "】");
					return resultInfo;
				}
			}
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // 二进制
			ftpClient.enterLocalPassiveMode(); // 被动模式
			ftpClient.sendCommand("PASV");
			ftpClient.sendCommand("SIZE " + remoteFileNameTmp + "\r\n");
			String replystr = ftpClient.getReplyString();
			String[] replystrL = replystr.split(" ");
			long filelen = 0;
			if (Integer.valueOf(replystrL[0]) == 213) {
				filelen = Long.valueOf(replystrL[1].trim());
			} else {
				resultInfo.put("SAVE_FILE_FLAG", "E");
				resultInfo.put("remark", "无法获取要下载的文件的大小!");
				return resultInfo;
			}
			accessFile.setLength(filelen);
			accessFile.close();
			ftpClient.disconnect();

			int tnum = Integer.valueOf(ftpItemConfigInfo.get("TNUM").toString());
			block = (filelen + tnum - 1) / tnum;// 每个线程下载的快大小
			ThreadPoolExecutor cachedThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
			List<Future<Map>> threadR = new ArrayList<Future<Map>>();
			for (int i = 0; i < tnum; i++) {
				logger.debug("发起线程：" + i);
				// 保存线程日志
				ftpItemConfigInfo.put("threadrunstate", "R");
				ftpItemConfigInfo.put("remark", "开始下载文件");
				ftpItemConfigInfo.put("data", "文件名：" + remoteFileNameTmp);
				long start = i * block;
				long end = (i + 1) * block - 1;
				ftpItemConfigInfo.put("begin", start);
				ftpItemConfigInfo.put("end", end);
				saveTaskLogDetail(ftpItemConfigInfo);
				Map para = new HashMap();
				para.putAll(ftpItemConfigInfo);
				para.put("serverfilename", remoteFileNameTmp);
				para.put("filelength", filelen);
				para.put("tnum", i + 0);
				para.put("threadDownSize", block);
				para.put("transferflag", FTPClientTemplate.TransferType.download);
				FTPClientTemplate dumpThread = new FTPClientTemplate(para);
				Future<Map> runresult = cachedThreadPool.submit(dumpThread);
				threadR.add(runresult);
			}

			do {
				// 等待下载完成
				Thread.sleep(1000);
			} while (cachedThreadPool.getCompletedTaskCount() < threadR.size());

			saveDownFileData(ftpItemConfigInfo);
			// 下载已经完成，多线程保存数据至表中

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("保存文件失败：", e);
			resultInfo.put("SAVE_FILE_FLAG", "E");
			resultInfo.put("remark", "保存文件失败：" + e);
			return resultInfo;
		}
		resultInfo.put("SAVE_FILE_FLAG", "S");
		return resultInfo;

	}

	@Override
	protected void post(Map ftpItemConfigInfo) {
		// TODO Auto-generated method stub
		if (ftpItemConfigInfo != null && ftpItemConfigInfo.containsValue("AFTERFLAG") && "0".equals(ftpItemConfigInfo.get("AFTERFLAG"))
				&& ftpItemConfigInfo.containsKey("AFTERFUNCTION") && ftpItemConfigInfo.get("AFTERFUNCTION") != null && !"".equals(ftpItemConfigInfo.get("AFTERFUNCTION"))) {
			// 这个时候确定已经进入了事后过程
			if (ftpItemConfigInfo.containsKey("retVal") && !"0000".equals(ftpItemConfigInfo.get("retVal"))) {
				ftpItemConfigInfo.put("threadrunstate", "E3");
				ftpItemConfigInfo.put("remark", ftpItemConfigInfo.get("retVal"));
				saveTaskLogDetail(ftpItemConfigInfo);

			}

		}
	}

	/**
	 * 保存下载的文件里的内容到配置的数据表中，多线程同时保存
	 */
	public void saveDownFileData(Map taskInfo) {
		// 先分配每个线程处理的起止位置
		List contthr = contSaveThreadContInfo(taskInfo);
		// 启动多个线程，保存文件内容
		if (contthr != null) {
			try {
				int tnum = Integer.valueOf(taskInfo.get("TNUM").toString());
				ThreadPoolExecutor cachedThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
				List<Future<Map>> threadR = new ArrayList<Future<Map>>();
				for (int i = 0; i < tnum; i++) {
					logger.debug("发起线程：" + i);
					Map tmp = (Map) contthr.get(i);
					Map para = new HashMap();
					para.putAll(taskInfo);
					para.putAll(tmp);
					para.put("transferflag", FTPClientTemplate.TransferType.savedata);
					FTPClientTemplate dumpThread = new FTPClientTemplate(para);
					Future<Map> runresult = cachedThreadPool.submit(dumpThread);
					threadR.add(runresult);
				}
				do {
					// 等待保存数据
					Thread.sleep(1000);
				} while (cachedThreadPool.getCompletedTaskCount() < threadR.size());
				taskInfo.put("SAVE_FILE_FLAG", "S");
				logger.debug("文件内容保存完毕！");
			} catch (Exception ex) {
				logger.error("保存文件失败", ex);
				taskInfo.put("SAVE_FILE_FLAG", "E");
				taskInfo.put("remark", "保存文件失败" + ex);
			} catch (Throwable ex) {
				logger.error("保存文件失败", ex);
				taskInfo.put("SAVE_FILE_FLAG", "E");
				taskInfo.put("remark", "保存文件失败" + ex);
			}
		}
	}

	/**
	 * 分配每个线程处理的起止位置
	 */
	public static List<Map<String,Long>> contSaveThreadContInfo(Map taskInfo) {
		List<Map<String,Long>> contlist = new ArrayList<Map<String,Long>>();
		try {
			RandomAccessFile raf = new RandomAccessFile(taskInfo.get("localfilename").toString(), "r");
			int tnum = Integer.valueOf(taskInfo.get("TNUM").toString());

			long filelen = raf.length();
			if (filelen != 0L) {
				long block = (filelen + tnum - 1) / tnum;// 每个线程下载的快大小
				long begin = 0;
				// 修复在保存数据时的线程数太大时，无法去除titleflag 行的问题处理
				if ("0".equals(taskInfo.get("linecountflag"))) {
					String temp = raf.readLine();
					begin += temp.length();
				}
				if ("0".equals(taskInfo.get("titleflag"))) {
					String temp = raf.readLine();
					begin += temp.length();
				}
				for (int i = 0; i < tnum; i++) {
					if (i == tnum - 1) {
						Map<String,Long> tmp = new HashMap<String,Long>();
						tmp.put("begin", begin);
						tmp.put("end", filelen - 1);
						contlist.add(tmp);
						break;
					}
					long end = (i + 1) * block - 1;
					// 处理如果有总数行和文件头行时，线程数大于数据行数，映入的问题
					if (end < begin) {
						begin = 0;
						end = begin;
						Map<String,Long> tmp = new HashMap<String,Long>();
						tmp.put("begin", begin);
						tmp.put("end", end);
						contlist.add(tmp);
						begin = end + 1;
						continue;
					}
					while (end > 0) {
						raf.seek(end);
						if (raf.readByte() == '\n') {
							Map<String,Long> tmp = new HashMap<String,Long>();
							tmp.put("begin", begin);
							tmp.put("end", end);
							contlist.add(tmp);
							begin = end + 1;
							break;
						}
						end--;
					}
				}
			}
			raf.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return contlist;
	}

}
