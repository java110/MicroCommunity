package com.java110.entity.order;

/**
 * @author wux
 * @create 2019-02-05 下午1:50
 * @desc Business扩展类
 **/
public class BusinessPlus extends BaseOrder{

    private String invokeModel;

    private long seq;

    public String getInvokeModel() {
        return invokeModel;
    }

    public void setInvokeModel(String invokeModel) {
        this.invokeModel = invokeModel;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }
}
