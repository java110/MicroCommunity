package com.java110.doc.entity;

import java.io.Serializable;

public class CmdDocDto implements Serializable, Comparable<CmdDocDto> {

    /**
     * api title
     *
     * @return
     */
    private String title;

    /**
     * description api
     *
     * @return
     */
    private String description;


    /**
     * api  version
     *
     * @return
     */
    private String version;

    /**
     * http method
     *
     * @return
     */
    private String httpMethod;

    private int seq;

    /**
     * request url
     *
     * @return
     */
    private String url;

    private String resource;

    private String author;

    private String cmdClass;

    private String serviceCode;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCmdClass() {
        return cmdClass;
    }

    public void setCmdClass(String cmdClass) {
        this.cmdClass = cmdClass;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public int compareTo(CmdDocDto o) {
        return this.getSeq() - o.getSeq();
    }
}
