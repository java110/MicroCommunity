package com.java110.listener.task;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 从Ftp文件系统下载文件存到TFS文件系统
 * 
 * 
 * @author wuxw7 2016-01-04
 * 
 */
public class DownloadFileFromFtpToTFS extends FtpToFileSystemQuartz {

	private static final String ITEM_SPEC_CD_10001 = "10001";// FTP地址
	private static final String ITEM_SPEC_CD_10002 = "10002";// FTP端口号
	private static final String ITEM_SPEC_CD_10003 = "10003";// FTP账号
	private static final String ITEM_SPEC_CD_10004 = "10004";// FTP密码
	private static final String ITEM_SPEC_CD_10005 = "10005";// FTP路径
	private static final String ITEM_SPEC_CD_10006 = "10006";// TFS地址

	private final int DEFAULT_THREAD_NUM = 1; // 默认线程，当在界面上不配置或配置不正确时采用默认线程

	// private static final String ITEM_SPEC_CD_10007 = "10007";//FTP地址

	// 调用是需要注入
	//private TfsManager tfsManager;

	private String tfsManager;

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
		String tfsPath = null;
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
			if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10001.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				ftpIp = ftpItemAttr.get("VALUE") == null ? "" : ftpItemAttr.get("VALUE").toString();
			} else if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10002.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				ftpPort = ftpItemAttr.get("VALUE") == null ? 21 : Integer.parseInt(ftpItemAttr.get("VALUE").toString());
			} else if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10003.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				ftpUsername = ftpItemAttr.get("VALUE") == null ? "" : ftpItemAttr.get("VALUE").toString();
			} else if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10004.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				ftpPassword = ftpItemAttr.get("VALUE") == null ? "" : ftpItemAttr.get("VALUE").toString();
			} else if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10005.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				ftpPath = ftpItemAttr.get("VALUE") == null ? "" : ftpItemAttr.get("VALUE").toString();
			} else if (ftpItemAttr.containsKey("ITEM_SPEC_ID") && ITEM_SPEC_CD_10006.equals(ftpItemAttr.get("ITEM_SPEC_ID").toString())) {
				tfsPath = ftpItemAttr.get("VALUE") == null ? "" : ftpItemAttr.get("VALUE").toString();
			}
		}

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
	 * @throws Exception
	 */
	private void anyFilesDownload(Map ftpItemConfigInfo, String taskId, FTPClientTemplate ftpClientTemplate, String ftpPath, List<String> fileNames) throws Exception {
		// 这种需要列出所有文件，根据名称匹配
		String param = "";

		String downLoadFailFileNames = "";// 下载失败的fileName
		String downLoadSuccessFileNames = "";// 下载成功的fileName
		int tNum = ftpItemConfigInfo.get("TNUM") == null ? DEFAULT_THREAD_NUM : (StringUtils.isNumeric(ftpItemConfigInfo.get("TNUM").toString()) ? Integer
				.parseInt(ftpItemConfigInfo.get("TNUM").toString()) : DEFAULT_THREAD_NUM);
		ThreadPoolExecutor downloadFilePools = null;
		try {
			// 创建线程池
			downloadFilePools = (ThreadPoolExecutor) Executors.newCachedThreadPool();

			//线程返回值存储
			List<Future<Map>> threadR = new ArrayList<Future<Map>>();

			// 文件数
			int filesNum = fileNames.size();

			// 每个线程上的文件数
			int fileNamesNumPerThread = filesNum / tNum;

			int surplusFileNames = filesNum % tNum;

			// 保存每个线程的文件
			List<List<String>> tnumFileNames = new ArrayList<List<String>>();

			// 每个线程上的文件名称
			List<String> fileNamesPerThread = null;
			// 说明文件数小于线程数，此时每个线程处理一个文件，总共文件数个线程
			if (fileNamesNumPerThread == 0) {
				for (String fName : fileNames) {
					fileNamesPerThread = new ArrayList<String>();
					fileNamesPerThread.add(fName);
					tnumFileNames.add(fileNamesPerThread);
				}
			} else {
				// 处理每个线程文件名
				for (int fileNameIndex = 0; fileNameIndex < fileNames.size();) {
					
					
					// 将余数文件加入至每个线程
					if (fileNameIndex >= (fileNames.size() - surplusFileNames)) {
						tnumFileNames.get(fileNameIndex%tNum).add(fileNames.get(fileNameIndex));
						fileNameIndex++;
						continue;
					}
					
//					if (fileNameIndex == (fileNames.size() - surplusFileNames)) {		
//						fileNameIndex++;
//						continue;
//					}
					
					fileNamesPerThread = new ArrayList<String>();
					for (int fileNamesPerThreadIndex = 0; fileNamesPerThreadIndex < fileNamesNumPerThread; fileNamesPerThreadIndex++) {
						fileNamesPerThread.add(fileNames.get(fileNameIndex + fileNamesPerThreadIndex));	
					}
					fileNameIndex += fileNamesNumPerThread;
					tnumFileNames.add(fileNamesPerThread);
				}
			}
			// 这里 循环每一个线程，处理每个线程上的文件
			for (List<String> fileNamesPerThreadNumTmp : tnumFileNames) {
				DownloadFileFromFtpToFTSThread downloadFileFromFtpToFTSThread = new DownloadFileFromFtpToFTSThread(fileNamesPerThreadNumTmp, taskId, this.getListenerServiceDao(),
						ftpPath, ftpClientTemplate, ftpItemConfigInfo, tfsManager);
				Future<Map> runresult = (Future<Map>) downloadFilePools.submit(downloadFileFromFtpToFTSThread);
				threadR.add(runresult);
			}
			do {
				// 等待下载完成
				Thread.sleep(1000);
			} while (downloadFilePools.getCompletedTaskCount() < threadR.size());

			// 对线程返回值处理
			for (Future<Map> future : threadR) {

				Map resultThreadInfo = future.get();
				param += (resultThreadInfo != null ? resultThreadInfo.get("param") : "");
				downLoadSuccessFileNames += (resultThreadInfo != null ? resultThreadInfo.get("downLoadSuccessFileNames") : "");
				downLoadFailFileNames += (resultThreadInfo != null ? resultThreadInfo.get("downLoadFailFileNames") : "");
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
		} finally {
			// 关闭线程池
			if (downloadFilePools != null)
				downloadFilePools.shutdown();
		}
		return;
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

	/*public TfsManager getTfsManager() {
		return tfsManager;
	}

	public void setTfsManager(TfsManager tfsManager) {
		this.tfsManager = tfsManager;
	}*/

}
