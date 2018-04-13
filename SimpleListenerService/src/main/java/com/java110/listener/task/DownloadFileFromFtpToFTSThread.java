package com.java110.listener.task;

import com.java110.common.log.LoggerEngine;
import com.java110.listener.dao.IListenerServiceDao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 从ftp下载文件保存至TFS 文件系统
 * 
 * 首先传入文件名 初始化类
 * 
 * 在call 中抒写业务处理逻辑
 * 
 * @author wuxw
 * 
 */
public class DownloadFileFromFtpToFTSThread extends LoggerEngine implements Callable<Map> {


	// 需要现在文件名称
	private List<String> fileNames;

	private String taskId;

	private IListenerServiceDao listenerServiceDao;

	private String ftpPath;

	private FTPClientTemplate ftpClientTemplate;

	private Map ftpItemConfigInfo;

	//private TfsManager tfsManager;
	private String  tfsManager;

	public DownloadFileFromFtpToFTSThread(List<String> fileNames, String taskId, IListenerServiceDao listenerServiceDao, String ftpPath, FTPClientTemplate ftpClientTemplate,
			Map ftpItemConfigInfo, String  tfsManager) {
		this.fileNames = fileNames;
		this.taskId = taskId;
		this.listenerServiceDao = listenerServiceDao;
		this.ftpPath = ftpPath;
		this.ftpClientTemplate = ftpClientTemplate;
		this.ftpItemConfigInfo = ftpItemConfigInfo;
		this.tfsManager = tfsManager;
	}

	@Override
	public Map call() throws Exception {
		// TODO Auto-generated method stub
		// 这种需要列出所有文件，根据名称匹配
		String param = "";
		// 获取FTP上的文件
		List<Map> needDownloadFiles = null;
		String downLoadFailFileNames = "";// 下载失败的fileName
		String downLoadSuccessFileNames = "";// 下载失败的fileName

		Map result = new HashMap();

		for (String fileName : fileNames) {
			if (fileName.length() > 0) { // 查询数据库，那写文件还没有下载
				Map logInfo = new HashMap();
				logInfo.put("fileNames", fileName);
				logInfo.put("taskId", taskId);
				needDownloadFiles = listenerServiceDao.queryFileNamesWithOutFtpLog(logInfo);
			}

			if (needDownloadFiles == null || needDownloadFiles.size() < 1) {
				continue;
			}

			// 保存文件至tfs
			Map resultInfo = downLoadFileToTFS(ftpPath + fileName, ftpClientTemplate, ftpItemConfigInfo);

			Map remoteFileInfo = new HashMap();

			if (resultInfo.containsKey("SAVE_FILE_FLAG") && "S".equals(resultInfo.get("SAVE_FILE_FLAG"))) {
				param += (fileName + "##" + resultInfo.get("TFS_RETURN_FILENAME") + "@@"); // 将下载成功的文件名需要保存至表中，防止以后重复下载
				remoteFileInfo.put("taskId", taskId);
				remoteFileInfo.put("fileName", fileName);
				remoteFileInfo.put("tfsFileName", resultInfo.get("TFS_RETURN_FILENAME"));
				saveDownLoadSuccessFile(remoteFileInfo); // 记录成功时的文件名
				downLoadSuccessFileNames += (fileName + ",");
			} else { // 记录下载失败的文件名
				downLoadFailFileNames += (fileName + ",");
			} //
		}
		result.put("param", param);
		result.put("downLoadSuccessFileNames", downLoadSuccessFileNames);
		result.put("downLoadFailFileNames", downLoadFailFileNames);
		return result;
	}

	/**
	 * 下载文件并且存至tfs文件系统
	 *
	 * @param remoteFileNameTmp
	 * @param ftpClientTemplate
	 * @return
	 */
	private Map downLoadFileToTFS(String remoteFileNameTmp, FTPClientTemplate ftpClientTemplate, Map ftpItemConfigInfo) {
		Map resultInfo = new HashMap();
		String tfsReturnFileName = null;
		if (ftpClientTemplate == null) {
			resultInfo.put("SAVE_FILE_FLAG", "E");
			return resultInfo;
		}
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		String suffix = remoteFileNameTmp.substring(remoteFileNameTmp.lastIndexOf("."));
		try {
			if (ftpClientTemplate.get(remoteFileNameTmp, output)) {
				//tfsReturnFileName = tfsManager.saveFile(output.toByteArray(), null, suffix);

				if (tfsReturnFileName == null) {
					// 保存文件失败
					resultInfo.put("SAVE_FILE_FLAG", "E");
					resultInfo.put("remark", "从ftp报文文件至TFS失败【" + remoteFileNameTmp + "】");
					logger.error("---【DownloadFileFromFtpToFTSThread.downLoadFileToTFS】从ftp报文文件至TFS失败", remoteFileNameTmp);
					return resultInfo;
				}
				// 保存tfs返回的文件名
				resultInfo.put("TFS_RETURN_FILENAME", tfsReturnFileName);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("保存文件失败：", e);
			resultInfo.put("SAVE_FILE_FLAG", "E");
			resultInfo.put("remark", e);
			return resultInfo;
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("关闭流失败", e);
					resultInfo.put("SAVE_FILE_FLAG", "E");
					resultInfo.put("remark", e);
					return resultInfo;
				}
			}
		}
		resultInfo.put("SAVE_FILE_FLAG", "S");
		resultInfo.put("TFS_RETURN_FILENAME", tfsReturnFileName);
		return resultInfo;

	}

	/**
	 * 下载并保存文件至表中。
	 *
	 * @param remoteFileInfo
	 */
	private void saveDownLoadSuccessFile(Map remoteFileInfo) throws Exception {
		// TODO Auto-generated method stub
		int addDownloadFlag = listenerServiceDao.addDownloadFileName(remoteFileInfo);
		if (addDownloadFlag < 1) {
			logger.error("---【DownloadFileFromFtpToFTSThread.saveDownLoadSuccessFile】保存下载文件名失败", remoteFileInfo);
		}
	}
}
