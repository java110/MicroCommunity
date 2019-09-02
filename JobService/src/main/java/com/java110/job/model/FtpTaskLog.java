package com.java110.job.model;

public class FtpTaskLog {
	private long logid;
	private long taskid;
	private String uord;
	private String state;
	private String serverfilename;
	private String localfilename;
	private long filelength;
	public long getLogid() {
		return logid;
	}
	public void setLogid(long logid) {
		this.logid = logid;
	}
	public long getTaskid() {
		return taskid;
	}
	public void setTaskid(long taskid) {
		this.taskid = taskid;
	}
	public String getUord() {
		return uord;
	}
	public void setUord(String uord) {
		this.uord = uord;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
	public long getFilelength() {
		return filelength;
	}
	public void setFilelength(long filelength) {
		this.filelength = filelength;
	}
	
	
}