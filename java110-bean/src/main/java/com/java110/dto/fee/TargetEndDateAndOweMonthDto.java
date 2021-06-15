package com.java110.dto.fee;

import java.util.Date;

public class TargetEndDateAndOweMonthDto {

    private Date targetEndDate;

    double oweMonth;


    public Date getTargetEndDate() {
        return targetEndDate;
    }

    public void setTargetEndDate(Date targetEndDate) {
        this.targetEndDate = targetEndDate;
    }

    public double getOweMonth() {
        return oweMonth;
    }

    public void setOweMonth(double oweMonth) {
        this.oweMonth = oweMonth;
    }
}
