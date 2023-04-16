package com.java110.dto.chargeMachine;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 充电桩厂家数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ChargeMachineFactoryDto extends PageDto implements Serializable {

    private String factoryId;
    private String factoryName;
    private String remark;
    private String beanImpl;


    private Date createTime;

    private String statusCd = "0";

    private List<ChargeMachineFactorySpecDto> specs;


    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBeanImpl() {
        return beanImpl;
    }

    public void setBeanImpl(String beanImpl) {
        this.beanImpl = beanImpl;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public List<ChargeMachineFactorySpecDto> getSpecs() {
        return specs;
    }

    public void setSpecs(List<ChargeMachineFactorySpecDto> specs) {
        this.specs = specs;
    }
}
