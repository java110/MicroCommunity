package com.java110.utils.exception;

import java.util.Map;

public class TaskException extends RuntimeException {
	private Map errInfoMap;
	public TaskException(Map errInfoMap, Throwable cause) {
		super(cause);
		this.errInfoMap = errInfoMap;
	}
	public Map getErrInfoMap() {
		return errInfoMap;
	}
	public void setErrInfoMap(Map errInfoMap) {
		this.errInfoMap = errInfoMap;
	}
	
}