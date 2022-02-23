package com.java110.dto.trace;

import java.io.Serializable;
import java.util.List;

public class TraceDto implements Serializable {
    private String id;
    private String traceId;
    private String parentSpanId;
    private String name;
    private long timestamp;

    private List<TraceAnnotationsDto> annotations;
    private List<TraceDbDto> dbs;

    private TraceParamDto param;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getParentSpanId() {
        return parentSpanId;
    }

    public void setParentSpanId(String parentSpanId) {
        this.parentSpanId = parentSpanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<TraceAnnotationsDto> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<TraceAnnotationsDto> annotations) {
        this.annotations = annotations;
    }

    public TraceParamDto getParam() {
        return param;
    }

    public void setParam(TraceParamDto param) {
        this.param = param;
    }

    public List<TraceDbDto> getDbs() {
        return dbs;
    }

    public void setDbs(List<TraceDbDto> dbs) {
        this.dbs = dbs;
    }
}
