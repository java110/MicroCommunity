package com.java110.generator.util;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;




public class FileUtilBase {

	 public static void save(byte[] body, File file) throws IOException {
		 writeToFile(body, file);
	 }

	 public static void save(byte[] body, String fileName, String filePath)
	 throws IOException{
		 writeToFile(body, fileName, filePath);
	 }



	/***************************************************************************
	 * 删除指定目录结构
	 *
	 * @param directoryName
	 */
	protected static void deleteFileDirectory(String filePath) {
		/**
		 * 指定删除目录路径构造一个文件对象
		 *
		 */
		File file = new File(filePath);

		File[] fileList = file.listFiles();
		/**
		 * 初始化子目录路径
		 */
		String dirPath = null;

		if (fileList != null)
			for (int i = 0; i < fileList.length; i++) {
				/**
				 * 如果是文件就将其删除
				 */
				if (fileList[i].isFile())
					fileList[i].delete();
				/**
				 * 如果是目录,那么将些目录下所有文件删除后再将其目录删除,
				 */
				if (fileList[i].isDirectory()) {

					dirPath = fileList[i].getPath();
					// 递归删除指定目录下所有文件
					deleteFileDirectory(dirPath);
				}
			}
		/**
		 * 删除给定根目录
		 */
		file.delete();
	}

	/**
	 * 把文件读取到byte[]中
	 *
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	protected static byte[] getFileByte(String fileName, boolean isDelete)
			throws FileNotFoundException {
		FileInputStream fileInputStream = new FileInputStream(fileName);
		byte[] buffer = getFileByte(fileInputStream);
		if (isDelete) {
			new File(fileName).delete();
		}
		return buffer;
	}

	/**
	 * 将byte[]写入文件
	 *
	 * @param buffer
	 * @param file
	 * @throws IOException
	 */
	protected static File writeToFile(byte[] buffer, String fileName,
			String filePath) throws IOException {
		File dir = new File(filePath);

		if (!dir.exists()) {
			dir.mkdirs();
		}
		String abPath = filePath.concat(fileName);
		File file = new File(abPath);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(file);
		out.write(buffer);
		out.close();
		return file;
	}

	/**
	 * 将byte[]写入文件
	 *
	 * @param buffer
	 * @param file
	 * @throws IOException
	 */
	protected static File writeToFile(byte[] buffer, File file) throws IOException {

		FileOutputStream out = new FileOutputStream(file);
		out.write(buffer);
		out.close();
		return file;
	}

	/**
	 * 把URL中的数据读取到byte[]中
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	protected static byte[] getFileByte(URL url) throws IOException {
		if (url != null) {
			return getFileByte(url.openStream());
		} else {
			return null;
		}
	}

	/**
	 * 从IS中获取byte[]
	 *
	 * @param in
	 * @return
	 */
	protected static byte[] getFileByte(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
		try {
			copy(in, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	protected static void copy(InputStream in, OutputStream out)
			throws IOException {
		try {
			byte[] buffer = new byte[4096];
			int nrOfBytes = -1;
			while ((nrOfBytes = in.read(buffer)) != -1) {
				out.write(buffer, 0, nrOfBytes);
			}
			out.flush();
		} catch (IOException e) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
			}
		}
	}
	/**
	 * 截取路径文件名，包括扩展名
	 * @param fileName
	 * @return
	 */
	public static String getFileNameSuff(String fileName) {
		return fileName.substring(fileName.lastIndexOf("/")+1);
	}
	/**
	 * 以路径最后斜杠位置加1开始取,取到扩展名。不包括扩展名
	 * @param fileName
	 * @return
	 */
	public static String getFileName(String fileName) {
		int pos = fileName.lastIndexOf(".");
		if(pos==-1){
			return fileName;
		}
		return fileName.substring(fileName.lastIndexOf("/")+1,pos);

	}
	/**
	 * 复制文件
	 *
	 * @param f1
	 *            当前文件流
	 * @param f2
	 *            目标文件流
	 * @return
	 * @throws Exception
	 */
	public static long copyfile(File f1, File f2) throws Exception {
		mkdirs(f2.getParent());
		if (f2.exists() && f2.isFile()) {
			f2.delete();
		}
		System.out.println("添加："+f2.getAbsolutePath());
		long time = new Date().getTime();
		int length = 2097152;
		FileInputStream in = new FileInputStream(f1);
		FileOutputStream out = new FileOutputStream(f2);
		FileChannel inC = in.getChannel();
		FileChannel outC = out.getChannel();
		ByteBuffer b = null;
		while (true) {
			if (inC.position() == inC.size()) {
				inC.close();
				outC.close();
				return new Date().getTime() - time;
			}
			if ((inC.size() - inC.position()) < length) {
				length = (int) (inC.size() - inC.position());
			} else
				length = 2097152;
			b = ByteBuffer.allocateDirect(length);
			inC.read(b);
			b.flip();
			outC.write(b);
			outC.force(false);
		}

	}
	public static long copyfile(String filePath1, String filePath2) throws Exception {
		File f1=new File(filePath1);
		File f2=new File(filePath2);
		return copyfile(f1,f2);

	}


	/**
	 *  创建文件夹及父节点
	 * @param file
	 */
	public static void mkdirs(File file){
		if(file.isFile()){
			file=new File(file.getParent());
		}
		if(!file.exists()){
			file.mkdirs();
        }
	}
	/**
	 *  创建文件夹及父节点
	 * @param file
	 */
	public static void mkdirs(String filePath){
		File file = new File(filePath);
		mkdirs(file);
	}
	/**
	 *  先创建父节点，再创建文件
	 * @param file
	 * @throws IOException
	 */
	public static void createrFile(File file) throws IOException{
		mkdirs(file.getParent()); // 判断父节点是否存在
		if(!file.exists()){
			file.createNewFile();
		}
	}

	/**
	 * 把字节写入文件中
	 * @param buffer
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static File writeNewFile(byte[] buffer, File file) throws IOException{
		return writeToFile(buffer, file);
	}

	/**
	 * 删除文件
	 * @param file File 对象
	 */
	public static void deleteFile(File file) {
		System.out.println("删除文件："+file.getAbsolutePath());
		file.delete();
	}
	/**
	 * 删除文件
	 * @param filePath
	 */
	public static void deleteFile(String filePath) {
		deleteFile(new File(filePath));
	}
	/**
	 *  创建父节点目录，
	 *  判断删除文件2并复制文件1到文件2
	 * @param f1 当前文件流
	 * @param f2 目标文件流
	 * @return
	 * @throws Exception
	 */
	public static void deleteAndCopyFile(File file1, File file2) throws Exception {
		mkdirs(file2.getParent());
		if (file2.exists() && file2.isFile()) {
			file2.delete();
		}
		if(file1.exists() && file1.isFile()){
			copyfile(file1, file2);
			file1.delete();
		}
	}

	/**
	 *  创建父节点目录，
	 *  判断删除文件2并复制文件1到文件2
	 *
	 * @param f1
	 *            当前文件流
	 * @param f2
	 *            目标文件流
	 * @return
	 * @throws Exception
	 */
	public static void deleteAndCopyFile(String file1Path, String file2Path) throws Exception {
		File file1 = new File(file1Path);
		File file2 = new File(file2Path);
		deleteAndCopyFile(file1, file2);
	}
	/**
	 * 获得文件的扩展名 含 标点符号
	 * @param fileName
	 * @return
	 */
	public static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		if(pos+1<fileName.length()){
			return fileName.substring(pos);
		}
		return "";
	}
	/**
	 * 获得文件的扩展名  不含标点符号
	 * @param fileName
	 * @return
	 */
	public static String getFileType(String fileName) {
		int pos = fileName.lastIndexOf(".");
		if(pos+1<fileName.length()){
			return fileName.substring(pos+1);
		}
		return "";
	}
	/**
	 * 根据路径获得文件内容
	 * @param fileName
	 * @return
	 */
	public static String getFileContent(String filePath) {
		File file = new File(filePath);
		return getFileContent(file);
	}
	/**
	 * 根据File对象获得文件内容
	 * @param fileName
	 * @return
	 */
	public static String getFileContent(File file) {
		String htmlCode = "";
		try {
			String encoding = "UTF-8";
			if (file.isFile() && file.exists()) { // 判断文件是否存在

				BufferedReader bufferedReader = new BufferedReader(new UnicodeReader(new FileInputStream(file), encoding));
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					htmlCode += lineTxt;
				}
				bufferedReader.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return htmlCode;
	}
	public static void upload(String filePath, String saveAsFileName,File upload) throws Exception {
		if (upload != null) {

			if (!filePath.equals("")) {
				File file = new File(filePath);
				if (!file.exists()) {
					file.mkdirs();
				}
			}
			filePath =filePath.concat(saveAsFileName);
			File imageFile = new File(filePath);
			deleteAndCopyFile(upload, imageFile);
		}
	}




	/**
	 * 文件字节大小转换文字描述
	 * @param fileName
	 * @return
	 */
	public static String convertfilesize(long filesize)
	{
		String strunit="bytes";
		String straftercomma="";
		int intdivisor=1;
		if(filesize>=1024*1024)
		{
			strunit = "mb";
			intdivisor=1024*1024;
		}
		else if(filesize>=1024)
		{
			strunit = "kb";
			intdivisor=1024;
		}
		if(intdivisor==1)
			return filesize + " " + strunit;
		straftercomma = "" + 100 * (filesize % intdivisor) / intdivisor ;
		if(straftercomma=="")
			straftercomma=".0";
		return filesize / intdivisor + "." + straftercomma + " " + strunit;
	}

}
