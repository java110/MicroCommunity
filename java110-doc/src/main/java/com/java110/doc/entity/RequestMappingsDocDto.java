package com.java110.doc.entity;

import java.io.Serializable;

public class RequestMappingsDocDto implements Serializable, Comparable<RequestMappingsDocDto> {

    private String name;

    private String resource;

    private int seq;

    private String url;

    private String startWay;


    public RequestMappingsDocDto() {
    }

    public RequestMappingsDocDto(String name, String resource, int seq, String url, String startWay) {
        this.name = name;
        this.resource = resource;
        this.seq = seq;
        this.url = url;
        this.startWay = startWay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStartWay() {
        return startWay;
    }

    public void setStartWay(String startWay) {
        this.startWay = startWay;
    }

    @Override
    public int compareTo(RequestMappingsDocDto o) {
        return this.getSeq() - o.getSeq();
    }
}
