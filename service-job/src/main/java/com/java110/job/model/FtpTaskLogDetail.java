package com.java110.job.model;

public class FtpTaskLogDetail {

	private long id;
	private long logid;
	private long taskid;
	private String state;
	private int tnum;
	private long begin;
	private long end;
	private long havedown;
	private String data;
	private String remark;
	private String serverfilename;
	private String localfilename;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getTnum() {
		return tnum;
	}
	public void setTnum(int tnum) {
		this.tnum = tnum;
	}
	public long getBegin() {
		return begin;
	}
	public void setBegin(long begin) {
		this.begin = begin;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public long getHavedown() {
		return havedown;
	}
	public void setHavedown(long havedown) {
		this.havedown = havedown;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	
	
}