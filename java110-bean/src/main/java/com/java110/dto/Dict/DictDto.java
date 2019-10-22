package com.java110.dto.Dict;

import java.io.Serializable;

/**
 * <br>
 * Created by hu ping on 10/22/2019
 * <p>
 */
public class DictDto implements Serializable {

    private String statusCd;
    private String name;
    private String description;

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
