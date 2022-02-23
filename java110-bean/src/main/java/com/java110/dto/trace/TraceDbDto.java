package com.java110.dto.trace;

import java.io.Serializable;

public class TraceDbDto implements Serializable {
    private String dbSql;
    private String param;
    private String duration;

    public String getDbSql() {
        return dbSql;
    }

    public void setDbSql(String dbSql) {
        this.dbSql = dbSql;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
