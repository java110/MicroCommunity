package com.java110.dto.workflow;

import java.io.Serializable;

/**
 * @ClassName WorkflowModelDto
 * @Description TODO
 * @Author wuxw
 * @Date 2021/8/17 0:19
 * @Version 1.0
 * add by wuxw 2021/8/17
 **/
public class WorkflowModelDto implements Serializable {

    private String name;

    private String key;

    private String modelId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
}
