package com.java110.po.dict;

public class DictPo {

    private int id;
    private String statusCd;
    private String name;
    private String description;
    private String tableName;
    private String tableColumns;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(String tableColumns) {
        this.tableColumns = tableColumns;
    }
}
