package com.java110.job.util;

import com.java110.job.dao.IHcFtpFileDAO;
import com.java110.job.model.FtpTaskLogDetail;
import com.java110.job.smo.IHcFtpFileSMO;
import com.java110.job.smo.impl.HcFtpFileSMOImpl;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import javax.annotation.Resource;
import java.io.*;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.Callable;

/** 
 * FTP客户端 
 *  
 * @author
 * @version
 */  
public class FTPClientTemplate implements Callable<Map> {
    //---------------------------------------------------------------------  
    // Instance data  
    //---------------------------------------------------------------------  
    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(HcFtpFileSMOImpl.class);
    private static ThreadLocal<FTPClient> ftpClientThreadLocal = new ThreadLocal<FTPClient>();
    private String host;
    private int                    port;  
    private String username;
    private String password;
  
    private boolean                binaryTransfer       = true;  
    private boolean                passiveMode          = true;  
    private String encoding             = "UTF-8";
    private int                    clientTimeout        = 1000 * 900;  
    private int 				   tnum					=0;
    private long				   filelength			=0;									//要下载的文件的总大小
    private int					   downBlock			=8*1024;							//下载数据缓存区大小
    private long 				   threadDownSize		=0;									//线程下载数据的大小
    private String serverfilename;											//要下载的服务器文件名
    private String localfilename;											//本地保存的文件名
    private String transferflag;											//传输类型
    private Map threadpara;												//线程执行参数
    @Resource(name = "hcFtpFileDAOImpl")
    private IHcFtpFileDAO hcFtpFileDAOImpl;
    @Resource(name = "hcFtpFileSMOImpl")
    private IHcFtpFileSMO hcFtpFileSMOImpl;
    public static String SPECIAL_SINGLESPLIT_a="+*|";										//字符串分隔符特殊字符需要用加"\\"来转译
    //传输类型（“U"是上传      upload     ”D“是下载   download）
    public static class TransferType{
    	public static String upload="U";
    	public static String download="D";
    	public static String savedata="S";
    }
    /**
     * 字符集
     * @author superZ
     *
     */
    public static class Charset{
    	public static String CHARSET_UTF8 = "UTF-8";
    	public static String CHARSET_ZHS16GBK="ZHS16GBK";
    }
    //构造函数
	public FTPClientTemplate(){
		init();
	}
	public FTPClientTemplate(Map clientMap) {
		Map para=new HashMap();
		para.putAll(clientMap);
		this.setThreadpara(para);
		
		this.setHost((String)clientMap.get("ip"));
		this.setPort(Integer.valueOf(clientMap.get("port").toString()));
		this.setUsername(clientMap.get("username").toString());
		this.setPassword(clientMap.get("pwd").toString());
		this.setServerfilename((String)clientMap.get("serverfilename"));
		this.setLocalfilename((String)clientMap.get("localfilename"));
		if(clientMap.get("filelength")!=null)
			this.setFilelength(Long.valueOf(clientMap.get("filelength").toString()));
		if(clientMap.get("tnum")!=null)
			this.setTnum(Integer.valueOf(clientMap.get("tnum").toString()));
		if(clientMap.get("threadDownSize")!=null)
			this.setThreadDownSize(Long.valueOf(clientMap.get("threadDownSize").toString()));
		this.setTransferflag(clientMap.get("transferflag").toString());
		init();
	}
	public FTPClientTemplate(String _host, int _port, String _username, String _password, String _serverfilename, String _localfilename, int _filelength, int _tnum, int _threadDownSize) {
		this.setHost(_host);
		this.setPort(_port);
		this.setUsername(_username);
		this.setPassword(_password);
		this.setServerfilename(_serverfilename);
		this.setLocalfilename(_localfilename);
		this.setFilelength(_filelength);
		this.setTnum(_tnum);
		this.setThreadDownSize(_threadDownSize);
		init();
	}
	private void init(){

	}
	public Map call() throws Exception {
		Map result=null;
		if(TransferType.download.equals(transferflag)){
			result=moreThreadFtpDownload();
		}
		else if(TransferType.upload.equals(transferflag)){
			result=upload();
		}
		else if(TransferType.savedata.equals(transferflag)){
			result=saveFileData();
		}
		return result;
	}
    //---------------------------------------------------------------------  
    // private method  
    //---------------------------------------------------------------------  
    /** 
     * 返回一个FTPClient实例 
     *  
     * @throws Exception
     */  
    private FTPClient getFTPClient()throws Exception {
        if (ftpClientThreadLocal.get() != null && ftpClientThreadLocal.get().isConnected()) {  
            return ftpClientThreadLocal.get();  
        } else {  
            FTPClient ftpClient = new FTPClient(); //构造一个FtpClient实例
            ftpClient.setControlEncoding(encoding); //设置字符集  
      
            connect(ftpClient); //连接到ftp服务器  
      
            //设置为passive模式  
            if (passiveMode) {  
                ftpClient.enterLocalPassiveMode();  
                String replystr=ftpClient.getReplyString();
                ftpClient.sendCommand("PASV");
                replystr=ftpClient.getReplyString();
            }  
            setFileType(ftpClient); //设置文件传输类型  
      
            try {  
                ftpClient.setSoTimeout(clientTimeout);  
                ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            } catch (SocketException e) {
                throw new Exception("Set timeout error.", e);
            }  
            ftpClientThreadLocal.set(ftpClient);  
            return ftpClient;  
        }  
    }  
  
    /** 
     * 设置文件传输类型 
     *  
     * @throws Exception
     * @throws IOException
     */  
    private void setFileType(FTPClient ftpClient)throws Exception {
        try {  
            if (binaryTransfer) {  
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            } else {  
                ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
            }  
        } catch (IOException e) {
            throw new Exception("Could not to set file type.", e);
        }  catch (Exception e) {
            throw new Exception("Could not to set file type.", e);
        }  
    }  
  
    /** 
     * 连接到ftp服务器 
     *  
     * @param ftpClient 
     * @return 连接成功返回true，否则返回false 
     * @throws Exception
     */  
    private boolean connect(FTPClient ftpClient)throws Exception {
        try {  
            ftpClient.connect(host, port);  
  
            // 连接后检测返回码来校验连接是否成功  
            int reply = ftpClient.getReplyCode();  
  
            if (FTPReply.isPositiveCompletion(reply)) {
                //登陆到ftp服务器  
                if (ftpClient.login(username, password)) {  
                    setFileType(ftpClient);  
                    return true;  
                }  
            } else {  
                ftpClient.disconnect();  
                throw new Exception("FTP server refused connection.");
            }  
        } catch (IOException e) {
            if (ftpClient.isConnected()) {  
                try {  
                    ftpClient.disconnect(); //断开连接  
                } catch (IOException e1) {
                    throw new Exception("Could not disconnect from server.", e1);
                }  
  
            }  
            throw new Exception("Could not connect to server.", e);
        }  
        return false;  
    }  
  
  
    //---------------------------------------------------------------------  
    // public method  
    //---------------------------------------------------------------------  
    /** 
     * 断开ftp连接 
     *  
     * @throws Exception
     */  
    public void disconnect() throws Exception {
        try {  
            FTPClient ftpClient = getFTPClient();
//            ftpClient.logout();  
            if (ftpClient.isConnected()) {  
                ftpClient.disconnect();  
                ftpClient = null;  
            }  
        } catch (IOException e) {
            throw new Exception("Could not disconnect from server.", e);
        }  
    }  
      
    public boolean mkdir(String pathname) throws Exception {
        return mkdir(pathname, null);  
    }  
      
    /** 
     * 在ftp服务器端创建目录（不支持一次创建多级目录） 
     *  
     * 该方法执行完后将自动关闭当前连接 
     *  
     * @param pathname 
     * @return 
     * @throws Exception
     */  
    public boolean mkdir(String pathname, String workingDirectory) throws Exception {
        return mkdir(pathname, workingDirectory, true);  
    }  
      
    /** 
     * 在ftp服务器端创建目录（不支持一次创建多级目录） 
     *  
     * @param pathname 
     * @param autoClose 是否自动关闭当前连接 
     * @return 
     * @throws Exception
     */  
    public boolean mkdir(String pathname, String workingDirectory, boolean autoClose) throws Exception {
        try {  
            getFTPClient().changeWorkingDirectory(workingDirectory);  
            return getFTPClient().makeDirectory(pathname);  
        } catch (IOException e) {
            throw new Exception("Could not mkdir.", e);
        } finally {  
            if (autoClose) {  
                disconnect(); //断开连接  
            }  
        }  
    }  
    
    /** *//**  
     * 递归创建远程服务器目录  
     * @param pathname 远程服务器文件绝对路径
     * @return 目录创建是否成功  
     * @throws IOException
     */  
    public boolean mkdirMore(String pathname) throws Exception {
//        UploadStatus status = UploadStatus.Create_Directory_Success;   
        String directory = pathname;
        if(!directory.startsWith("/")){   
        	directory ="/"+directory;   
        } 
        if(pathname.lastIndexOf("/")==pathname.length()-1){
        	directory = pathname.substring(0,pathname.lastIndexOf("/")); 
        }
        FTPClient ftpClient=getFTPClient();
        if(!directory.equalsIgnoreCase("/")&&!ftpClient.changeWorkingDirectory(new String(directory.getBytes("GBK"),"iso-8859-1"))){
            //如果远程目录不存在，则递归创建远程服务器目录   
        	 if(!directory.endsWith("/")){  
             	directory=directory+"/";
             } 
        	int start=0;
            int end =directory.indexOf("/",start+1);   
            while(true){   
                String subDirectory = new String(pathname.substring(start,end).getBytes("GBK"),"iso-8859-1");
                if(!ftpClient.changeWorkingDirectory(subDirectory)){   
                    if(ftpClient.makeDirectory(subDirectory)){   
                        ftpClient.changeWorkingDirectory(subDirectory);   
                    }else {   
//                        System.out.println("创建目录失败");  
                        return true;
                    }   
                }   
                   
                start = end + 1;   
                end = directory.indexOf("/",start);   
                //检查所有目录是否创建完毕   
                if(end <= start){   
                    break;   
                }   
            }   
        }   
        return true;   
    }   
    /** 
     * 上传一个本地文件到远程指定文件 
     *  
     * @param remoteAbsoluteFile 远程文件名(包括完整路径) 
     * @param localAbsoluteFile 本地文件名(包括完整路径) 
     * @return 成功时，返回true，失败返回false 
     * @throws Exception
     */  
    public boolean put(String remoteAbsoluteFile, String localAbsoluteFile) throws Exception {
        return put(remoteAbsoluteFile, localAbsoluteFile, true);  
    }  
  
    /** 
     * 上传一个本地文件到远程指定文件 
     *  
     * @param remoteAbsoluteFile 远程文件名(包括完整路径) 
     * @param localAbsoluteFile 本地文件名(包括完整路径) 
     * @param autoClose 是否自动关闭当前连接 
     * @return 成功时，返回true，失败返回false 
     * @throws Exception
     */  
    public boolean put(String remoteAbsoluteFile, String localAbsoluteFile, boolean autoClose) throws Exception {
        InputStream input = null;
        try {  
            //处理传输  
            input = new FileInputStream(localAbsoluteFile);
            getFTPClient().storeFile(remoteAbsoluteFile, input);  
            logger.debug("put " + localAbsoluteFile);  
            return true;  
        } catch (FileNotFoundException e) {
            throw new Exception("local file not found.", e);
        } catch (IOException e) {
            throw new Exception("Could not put file to server.", e);
        } finally {  
            try {  
                if (input != null) {  
                    input.close();  
                }  
            } catch (Exception e) {
                throw new Exception("Couldn't close FileInputStream.", e);
            }  
            if (autoClose) {  
                disconnect(); //断开连接  
            }  
        }  
    }  
    
    /** *//**  
     * 上传文件到FTP服务器，支持断点续传  
     *  localAbsoluteFile 本地文件名称，绝对路径
     *  remoteAbsoluteFile 远程文件路径，使用/home/directory1/subdirectory/file.ext或是 http://www.guihua.org /subdirectory/file.ext
     * 							按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
     * @return 上传结果
     * @throws IOException
     */  
    public Map upload() throws Exception {
    	FTPClient ftpClient =null;
    	Map result=new HashMap();

    	String flag="1";
        try {
        	ftpClient =getFTPClient();
            File f = new File(localfilename);
            long localSize = f.length();  
            threadpara.put("filesize", localSize);
            //对远程目录的处理   
            if(serverfilename.contains("/")){   
                //创建服务器远程目录结构，创建失败直接返回   
                if(!mkdirMore(serverfilename.substring(0,serverfilename.lastIndexOf("/")+1))){  
                	threadpara.put("flag","0");
                	return threadpara;
                }   
            }   
            //检查远程是否存在文件   
            FTPFile[] files = ftpClient.listFiles(new String(serverfilename.getBytes("GBK"),"iso-8859-1"));
            if(files.length == 1){   
                long remoteSize = files[0].getSize();   
                if(remoteSize>=localSize){
                	threadpara.put("flag","1");
                	return threadpara;
                }
                //尝试移动文件内读取指针,实现断点续传   
                result = uploadFile(serverfilename, f,remoteSize);   
                   
                //如果断点续传没有成功，则删除服务器上文件，重新上传   
                if("0".equals(result.get("flag").toString())){   
                    if(!ftpClient.deleteFile(serverfilename)){   
                        return result;   
                    }   
                    result = uploadFile(serverfilename, f,  0);   
                }   
            }else {   
                result = uploadFile(serverfilename, new File(localfilename),  0);
            }   
        } catch (Exception e1) {
//        	result=result;
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			throw e1;
		}
        threadpara.put("flag",result.get("flag"));
    	return threadpara;
    }   
    
    /** *//**  
     * 上传文件到服务器,新上传和断点续传  
     * @param remoteFile 远程文件名，在上传之前已经将服务器工作目录做了改变  
     * @param localFile 本地文件 File句柄，绝对路径  
     * @param beginindex 需要显示的处理进度步进值
     *  ftpClient FTPClient 引用
     * @return
     * @throws IOException
     */  
    public Map uploadFile(String remoteFile, File localFile, long beginindex) throws Exception {
        RandomAccessFile accessFile =null;// 建立随机访问
		OutputStream out = null;
		FTPClient ftpClient = null;
		Map result=new HashMap();
		result.put("flag", "1");
        try {
        	ftpClient =getFTPClient();
        	out = ftpClient.appendFileStream(new String(remoteFile.getBytes("GBK"),"iso-8859-1"));
        	accessFile = new RandomAccessFile(localFile, "rwd");
			ftpClient = getFTPClient();
			//断点续传   
	        if(beginindex>0){   
	            ftpClient.setRestartOffset(beginindex);   
	            accessFile.seek(beginindex);   
	        }   
	        byte[] bytes = new byte[1024];   
	        int c;   
	        while((c = accessFile.read(bytes))!= -1){   
	            out.write(bytes,0,c);   
	        }  
        } catch (IOException e) {
    		result.put("flag", "0");
			logger.debug(" not upload !!! ");
			logger.debug("uploadFileFrom IOException : {}", e);
			throw e;
		} catch (Exception e1) {
			result.put("flag", "0");
			e1.printStackTrace();
			throw e1;
		}finally {
			try {
				out.flush();  
				accessFile.close();
				out.close();
				boolean ftpresult =ftpClient.completePendingCommand();   
				disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
        return result;   
    }  
    /**
     * 从指定位移开始读取文件内容，并保存到数据表中，并在指定的位移处结束
     * @return
     */
    public Map saveFileData(){
    	Map result=new HashMap();
    	String flag="S";
    	try
		{
    		String insertsql=threadpara.get("dbsql").toString();
    		//取要保存的表的字段名
    		String colnames=insertsql.substring(insertsql.indexOf("(")+1, insertsql.lastIndexOf(")"));
    		//取取要保存的表名
    		String tablename=insertsql.substring(0,insertsql.indexOf("("));
    		tablename=tablename.trim();
    		tablename=tablename.substring(tablename.lastIndexOf(" "), tablename.length());
    		//查字段的数据类型
    		List<Map> collist=hcFtpFileDAOImpl.queryTableColInfo(tablename.trim(), colnames.trim());
			RandomAccessFile raf = new RandomAccessFile(localfilename, "r");
			long filelen=raf.length();
			if (filelen != 0L) {  
				long begin = Long.valueOf(threadpara.get("begin").toString());
				long end = Long.valueOf(threadpara.get("end").toString());
				raf.seek(begin);
				if(begin==0){
					if("0".equals(threadpara.get("linecountflag").toString())){
						String temp = raf.readLine();
					}
					if("0".equals(threadpara.get("titleflag").toString())){
						String temp = raf.readLine();
					}
				}
				while (raf.getFilePointer() < end) {
					String temp = raf.readLine();
					String line = new String(temp.getBytes("ISO-8859-1"),"UTF-8");
					//保存取到的内容
					try{
						saveStringToData(line,insertsql,colnames,collist);
			    	}
			    	catch(Exception ex){
			    		ex.printStackTrace();
			    		flag="E";
			    		//如果保存失败，保存日志，继续保存下面的内容
			    		String err=ex.toString();
			    		if(err.length()>2000){
							err=err.substring(2000);
						}
			    		threadpara.put("state", "TD2");
			    		threadpara.put("remark", err);
			    		threadpara.put("savelinedata", line);
			    		insertTaskLogDetailInfo();
			    	}
				}
			}  
			raf.close(); 
		}
		catch(Exception ex){
			ex.printStackTrace();
    		flag="E";
		} 
		result.put("flag", flag);
    	return result; 
    }

	/**
	 * 生成任务执行日志
	 */
	private void insertTaskLogDetailInfo(){
		FtpTaskLogDetail logdetail=new FtpTaskLogDetail();
		logdetail.setLogid(Long.valueOf(threadpara.get("logid").toString()));
		logdetail.setTaskid(Long.valueOf(threadpara.get("taskid").toString()));
		logdetail.setState((String)threadpara.get("state"));
		logdetail.setData((String)threadpara.get("savelinedata"));
		logdetail.setTnum(Integer.valueOf(threadpara.get("tnum").toString()));
		if(threadpara.get("begin")!=null){
			logdetail.setBegin(Long.valueOf(threadpara.get("begin").toString()));
		}
		if(threadpara.get("end")!=null){
			logdetail.setEnd(Long.valueOf(threadpara.get("end").toString()));
		}
		if(threadpara.get("havedown")!=null){
			logdetail.setHavedown(Long.valueOf(threadpara.get("havedown").toString()));
		}
		logdetail.setRemark((String)threadpara.get("remark"));
		logdetail.setServerfilename((String)threadpara.get("serverfilename"));
		logdetail.setLocalfilename((String)threadpara.get("localfilename"));
        hcFtpFileSMOImpl.saveTaskRunDetailLog(logdetail);
	}
    public void saveStringToData(String data, String insertsql, String colnames, List<Map> collist){
		//拼接数据保存的SQL语句，日期型的值要格式化下
		String[] datarows=data.split(threadpara.get("sign").toString());
		//要保存的数据
		String cols[]=colnames.split(",");
		//如果配置的时候配置的表列数大于数据数的话，直接补充 -1
		if(datarows != null && datarows.length < cols.length ){
			List<String> datarowsLists = new ArrayList<String>();
			List<String> datarowsList = Arrays.asList(datarows);
			datarowsLists.addAll(datarowsList);
			for(int index = 0 ; index < (cols.length - datarows.length);index++){		
				datarowsLists.add("-1");	
			}
			datarows = (String[]) datarowsLists.toArray(new String[0]);
		}
		insertsql=getInsertTableSQL(colnames,collist,datarows,insertsql);
		//执行SQL语句，保存数据
        hcFtpFileSMOImpl.insertFileData2Table(insertsql);
    }
    /**
     * 拼接数据保存的SQL语句，日期型的值要格式化下
     */
    private String getInsertTableSQL(String colnames, List<Map> collist, String[] datarows, String insertsql){
    	String sql=insertsql.trim() +" values(";
    	String cols[]=colnames.split(",");
    	for(int i=0;i<cols.length;i++){
    		String name=cols[i];
    		String value=datarows[i];
    		for(Map map:collist){
    			if(name.toUpperCase().equals(map.get("COLUMN_NAME").toString())){
    				if("DATE".equals(map.get("DATA_TYPE").toString())){
    					value="to_date('"+value+"','yyyy-mm-dd hh24:mi:ss')";
    				}
    				else if("NUMBER".equals(map.get("DATA_TYPE").toString())){
    					value=value;
    				}
    				else{
    					value="'"+value+"'";
    				}
    				break;
    			}
    		}
    		sql=sql+value+",";
    	}
    	sql=sql.substring(0, sql.length()-1)+")";
    	return sql;
    }
	/**
	 * 重命名本地文件
	 * @param fileFrom
	 * @param fileTo
	 * @return
	 */
	public static boolean reNameTo(File fileFrom, File fileTo) {
		return fileFrom.renameTo(fileTo);
	}
	/**
	 * 根据文件路径加载File对象
	 * @param path
	 * @return
	 */
	public static File getFileByPath(String path){
		return new File(path);
	}
    /** 
     * 下载一个远程文件到本地的指定文件 
     *  
     * @param remoteAbsoluteFile 远程文件名(包括完整路径) 
     * @param localAbsoluteFile 本地文件名(包括完整路径) 
     * @return 成功时，返回true，失败返回false 
     * @throws Exception
     */  
    public boolean get(String remoteAbsoluteFile, String localAbsoluteFile) throws Exception {
        return get(remoteAbsoluteFile, localAbsoluteFile, true);  
    }  
  
    /** 
     * 下载一个远程文件到本地的指定文件 
     *  
     * @param remoteAbsoluteFile 远程文件名(包括完整路径) 
     * @param localAbsoluteFile 本地文件名(包括完整路径) 
     * @param autoClose 是否自动关闭当前连接 
     *  
     * @return 成功时，返回true，失败返回false 
     * @throws Exception
     */  
    public boolean get(String remoteAbsoluteFile, String localAbsoluteFile, boolean autoClose) throws Exception {
        OutputStream output = null;
        try {  
            output = new FileOutputStream(localAbsoluteFile);
            return get(remoteAbsoluteFile, output, autoClose);  
        } catch (FileNotFoundException e) {
            throw new Exception("local file not found.", e);
        } finally {  
            try {  
                if (output != null) {  
                    output.close();  
                }  
            } catch (IOException e) {
                throw new Exception("Couldn't close FileOutputStream.", e);
            }  
        }  
    }  
  
    /** 
     * 下载一个远程文件到指定的流 处理完后记得关闭流 
     *  
     * @param remoteAbsoluteFile 
     * @param output 
     * @return 
     * @throws Exception
     */  
    public boolean get(String remoteAbsoluteFile, OutputStream output) throws Exception {
        return get(remoteAbsoluteFile, output, true);  
    }  
  
    /** 
     * 下载一个远程文件到指定的流 处理完后记得关闭流 
     *  
     * @param remoteAbsoluteFile 
     * @param output 
     * @param
     * @return 
     * @throws Exception
     */  
    public boolean get(String remoteAbsoluteFile, OutputStream output, boolean autoClose) throws Exception {
        try {  
            FTPClient ftpClient = getFTPClient();
            // 处理传输  
            return ftpClient.retrieveFile(remoteAbsoluteFile, output);  
        } catch (IOException e) {
            throw new Exception("Couldn't get file from server.", e);
        } finally {  
            if (autoClose) {  
                disconnect(); //关闭链接  
            }  
        }  
    }  
  
    /** 
     * 从ftp服务器上删除一个文件 
     * 该方法将自动关闭当前连接 
     *  
     * @param delFile 
     * @return 
     * @throws Exception
     */  
    public boolean delete(String delFile) throws Exception {
        return delete(delFile, true);  
    }  
      
    /** 
     * 从ftp服务器上删除一个文件 
     *  
     * @param delFile 
     * @param autoClose 是否自动关闭当前连接 
     *  
     * @return 
     * @throws Exception
     */  
    public boolean delete(String delFile, boolean autoClose) throws Exception {
        try {  
            getFTPClient().deleteFile(delFile);  
            return true;  
        } catch (IOException e) {
            throw new Exception("Couldn't delete file from server.", e);
        } finally {  
            if (autoClose) {  
                disconnect(); //关闭链接  
            }  
        }  
    }  
      
    /** 
     * 批量删除 
     * 该方法将自动关闭当前连接 
     *  
     * @param delFiles 
     * @return 
     * @throws Exception
     */  
    public boolean delete(String[] delFiles) throws Exception {
        return delete(delFiles, true);  
    }  
  
    /** 
     * 批量删除 
     *  
     * @param delFiles 
     * @param autoClose 是否自动关闭当前连接 
     *  
     * @return 
     * @throws Exception
     */  
    public boolean delete(String[] delFiles, boolean autoClose) throws Exception {
        try {  
            FTPClient ftpClient = getFTPClient();
            for (String s : delFiles) {
                ftpClient.deleteFile(s);  
            }  
            return true;  
        } catch (IOException e) {
            throw new Exception("Couldn't delete file from server.", e);
        } finally {  
            if (autoClose) {  
                disconnect(); //关闭链接  
            }  
        }  
    }  
  
    /** 
     * 列出远程默认目录下所有的文件 
     *  
     * @return 远程默认目录下所有文件名的列表，目录不存在或者目录下没有文件时返回0长度的数组 
     * @throws Exception
     */  
    public String[] listNames() throws Exception {
        return listNames(null, true);  
    }  
      
    public String[] listNames(boolean autoClose) throws Exception {
        return listNames(null, autoClose);  
    }  
  
    /** 
     * 列出远程目录下所有的文件 
     *  
     * @param remotePath 远程目录名 
     * @param autoClose 是否自动关闭当前连接 
     *  
     * @return 远程目录下所有文件名的列表，目录不存在或者目录下没有文件时返回0长度的数组 
     * @throws Exception
     */  
    public String[] listNames(String remotePath, boolean autoClose) throws Exception {
        try {  
            String[] listNames = getFTPClient().listNames(remotePath);
            return listNames;  
        } catch (IOException e) {
            throw new Exception("列出远程目录下所有的文件时出现异常", e);
        } finally {  
            if (autoClose) {  
                disconnect(); //关闭链接  
            }  
        }  
    }  
    public Map moreThreadFtpUpload(){
    	Map result=new HashMap();
    	String flag="1";
    	File file = new File(localfilename);
		RandomAccessFile accessFile =null;// 建立随机访问
		OutputStream out = null;
		FTPClient ftpClient = null;
		try {
			long start = tnum * threadDownSize;
			long end = (tnum + 1) * threadDownSize-1;
			if(end>=filelength){
				end=filelength-1;
			}
			logger.debug("线程:" + tnum +"从"+start+"开始上传到"+end+ "结束，共需上传:"+(end-start+1));
			accessFile = new RandomAccessFile(file, "rwd");
			ftpClient = getFTPClient();
			ftpClient.setRestartOffset(start);
			out = ftpClient.storeFileStream(new String(
					serverfilename.getBytes("GBK"),
					FTP.DEFAULT_CONTROL_ENCODING));
//			out = ftpClient.appendFileStream(new String(
//					serverfilename.getBytes("GBK"),
//					FTP.DEFAULT_CONTROL_ENCODING)); 
			accessFile.seek(start);   
			byte[] data = new byte[downBlock];
			int len = 0;
			long downedlength = 0;
			while ((len =accessFile.read(data)) != -1 && downedlength < threadDownSize) {
				downedlength = downedlength + len;
				out.write(data,0,len);   
				logger.debug("线程:" + tnum + "已上传:"+downedlength);
				if(downedlength==threadDownSize){
					break;
				}
				if(downedlength+len>threadDownSize){
					data = new byte[Integer.valueOf(Long.toString(threadDownSize-downedlength))];
				}
			}
			logger.debug("线程:" + tnum + "上传完成!共上传"+downedlength);
		} catch (IOException e) {
			flag = "0";
			logger.debug(" not upload !!! ");
			logger.debug("uploadFileFrom IOException : {}", e);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			flag="0";
			e1.printStackTrace();
		}finally {
			try {
//				ftpClient.logout();
//				ftpClient.disconnect();
				out.flush();  
				accessFile.close();
				out.close();
				boolean ftpresult =ftpClient.completePendingCommand();   
				disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	result.put("flag",flag);
    	result.put("threadnum", tnum);
    	return result;
    }
    public Map moreThreadFtpDownload(){
    	Map result=new HashMap();
    	String flag="1";
		// 要写入的文件
		File file = new File(localfilename);
		RandomAccessFile accessFile =null;// 建立随机访问
		InputStream in = null;
		FTPClient ftpClient = null;
		int downedlength = 0;
		try {
			long start = tnum * threadDownSize;
			long end = (tnum + 1) * threadDownSize-1;
			if(end>=filelength){
				end=filelength-1;
			}
			logger.debug("线程:" + tnum +"从"+start+"开始下载到"+end+ "结束，共需下载:"+(end-start+1));
			ftpClient = getFTPClient();
			accessFile = new RandomAccessFile(file, "rwd");
			ftpClient.setRestartOffset(start);
			in = ftpClient.retrieveFileStream(new String(
					serverfilename.getBytes("GBK"),
					FTP.DEFAULT_CONTROL_ENCODING));
			byte[] data = new byte[downBlock];
			int len = 0;
			accessFile.seek(start);
			while ((len = in.read(data)) != -1 && downedlength < threadDownSize) {
				downedlength = downedlength + len;
				accessFile.write(data, 0, len);
				logger.debug("线程:" + tnum + "已下载:"+downedlength);
				if(downedlength==threadDownSize){
					break;
				}
				if(downedlength+len>threadDownSize){
					data = new byte[Integer.valueOf(Long.toString(threadDownSize-downedlength))];
				}
			}
			logger.debug("线程:" + tnum + "下载完成!共下载"+downedlength);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			flag="0";
			logger.error("下载线程异常", e1);
		}  catch (Exception e1) {
			// TODO Auto-generated catch block
			flag="0";
			logger.error("下载线程异常", e1);
		} finally {
			try {
//				ftpClient.logout();
//				ftpClient.disconnect();
				accessFile.close();
				in.close();
				disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
//    	result.put("threadnum", tnum);
    	threadpara.put("downedlength",downedlength);
    	threadpara.put("flag",flag);
    	return threadpara;
    }

    public long getFilelength() {
		return filelength;
	}
	public void setFilelength(long filelength) {
		this.filelength = filelength;
	}
	public String getHost() {
        return host;  
    }  
  
    public void setHost(String host) {
        this.host = host;  
    }  
  
    public int getPort() {  
        return port;  
    }  
  
    public void setPort(int port) {  
        this.port = port;  
    }  
  
    public String getUsername() {
        return username;  
    }  
  
    public void setUsername(String username) {
        this.username = username;  
    }  
  
    public String getPassword() {
        return password;  
    }  
  
    public void setPassword(String password) {
        this.password = password;  
    }  
  
    public boolean isBinaryTransfer() {  
        return binaryTransfer;  
    }  
  
    public void setBinaryTransfer(boolean binaryTransfer) {  
        this.binaryTransfer = binaryTransfer;  
    }  
  
    public boolean isPassiveMode() {  
        return passiveMode;  
    }  
    public void setPassiveMode(boolean passiveMode) {  
        this.passiveMode = passiveMode;  
    }  
    public String getEncoding() {
        return encoding;  
    }  
    public void setEncoding(String encoding) {
        this.encoding = encoding;  
    }  
    public int getClientTimeout() {  
        return clientTimeout;  
    }  
    public void setClientTimeout(int clientTimeout) {  
        this.clientTimeout = clientTimeout;  
    }  
	public int getTnum() {
		return tnum;
	}
	public void setTnum(int tnum) {
		this.tnum = tnum;
	}
	public int getDownBlock() {
		return downBlock;
	}
	public void setDownBlock(int downBlock) {
		this.downBlock = downBlock;
	}
	public long getThreadDownSize() {
		return threadDownSize;
	}
	public void setThreadDownSize(long threadDownSize) {
		this.threadDownSize = threadDownSize;
	}
	public String getServerfilename() {
		return serverfilename;
	}
	public void setServerfilename(String serverfilename) {
		this.serverfilename = serverfilename;
	}
	public String getLocalfilename() {
		return localfilename;
	}
	public void setLocalfilename(String localfilename) {
		this.localfilename = localfilename;
	}
	
	public String getTransferflag() {
		return transferflag;
	}
	public void setTransferflag(String transferflag) {
		this.transferflag = transferflag;
	}
	public Map getThreadpara() {
		return threadpara;
	}
	public void setThreadpara(Map threadpara) {
		this.threadpara = threadpara;
	}
	
}