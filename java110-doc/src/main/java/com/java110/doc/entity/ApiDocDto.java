package com.java110.doc.entity;

import java.io.Serializable;

public class ApiDocDto implements Serializable {

   private String title;
    /**
     * description api
     * @return
     */
    private String description;

    /**
     *  api  version
     * @return
     */
    private  String version;

    private String company;

    public ApiDocDto() {
    }

    public ApiDocDto(String title, String description, String version, String company) {
        this.title = title;
        this.description = description;
        this.version = version;
        this.company = company;
    }

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
