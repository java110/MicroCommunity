package com.java110.vo.api.inspectionTaskDetail;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiInspectionTaskDetailVo extends MorePageVo implements Serializable {
    List<ApiInspectionTaskDetailDataVo> inspectionTaskDetails;
    private int code = 0;
    private String msg ="成功";


    public List<ApiInspectionTaskDetailDataVo> getInspectionTaskDetails() {
        return inspectionTaskDetails;
    }

    public void setInspectionTaskDetails(List<ApiInspectionTaskDetailDataVo> inspectionTaskDetails) {
        this.inspectionTaskDetails = inspectionTaskDetails;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
