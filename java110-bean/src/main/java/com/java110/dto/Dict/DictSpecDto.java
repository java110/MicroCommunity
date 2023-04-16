package com.java110.dto.Dict;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 物品类型数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class DictSpecDto extends PageDto implements Serializable {

    private String specId;
private String tableColumns;
private String specName;
private String tableName;


    private Date createTime;

    private String statusCd = "0";


    public String getSpecId() {
        return specId;
    }
public void setSpecId(String specId) {
        this.specId = specId;
    }
public String getTableColumns() {
        return tableColumns;
    }
public void setTableColumns(String tableColumns) {
        this.tableColumns = tableColumns;
    }
public String getSpecName() {
        return specName;
    }
public void setSpecName(String specName) {
        this.specName = specName;
    }
public String getTableName() {
        return tableName;
    }
public void setTableName(String tableName) {
        this.tableName = tableName;
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
}
