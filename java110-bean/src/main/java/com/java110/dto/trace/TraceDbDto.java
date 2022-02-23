package com.java110.dto.trace;

import java.io.Serializable;

public class TraceDbDto implements Serializable {
    private String sql;
    private String params;
    private long duration;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
