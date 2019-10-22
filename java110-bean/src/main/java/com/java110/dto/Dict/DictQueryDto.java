package com.java110.dto.Dict;

import java.io.Serializable;

/**
 * <br>
 * Created by hu ping on 10/22/2019
 * <p>
 */
public class DictQueryDto implements Serializable {

    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
