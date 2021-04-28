package com.java110.entity.assetImport;

/**
 * 车位导入
 *
 * add by wuxw 2019-09-24
 */
public class ImportParkingSpace {

    private String paNum;

    private String psNum;

    private String typeCd;

    private double area;

    private ImportOwner importOwner;

    private String carNum;

    private String carBrand;

    private String carType;

    private String carColor;

    private String sellOrHire;

    private String startTime;

    private String endTime;

    public String getPsNum() {
        return psNum;
    }

    public void setPsNum(String psNum) {
        this.psNum = psNum;
    }

    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public ImportOwner getImportOwner() {
        return importOwner;
    }

    public void setImportOwner(ImportOwner importOwner) {
        this.importOwner = importOwner;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getSellOrHire() {
        return sellOrHire;
    }

    public void setSellOrHire(String sellOrHire) {
        this.sellOrHire = sellOrHire;
    }

    public String getPaNum() {
        return paNum;
    }

    public void setPaNum(String paNum) {
        this.paNum = paNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
