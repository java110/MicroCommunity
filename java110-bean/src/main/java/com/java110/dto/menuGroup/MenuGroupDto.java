package com.java110.dto.menuGroup;

import com.java110.dto.PageDto;

import java.io.Serializable;

public class MenuGroupDto extends PageDto implements Serializable {

    private String gId;
    private String name;
    private String icon;
    private String label;
    private String seq;
    private String description;
    private String statusCd;

    public String getGId() {
        return gId;
    }

    public void setGId(String gId) {
        this.gId = gId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
