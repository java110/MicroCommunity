package com.java110.vo.api;

/**
 * @ClassName ApiFeeVo
 * @Description TODO
 * @Author wuxw
 * @Date 2019/6/2 18:05
 * @Version 1.0
 * add by wuxw 2019/6/2
 **/
public class ApiParkingSpaceFeeVo extends ApiMainFeeVo {




   private String psId;
   private String num;
   private String typeCd;
   private String carNum;
   private String carBrand;
   private String carType;


    public String getPsId() {
        return psId;
    }

    public void setPsId(String psId) {
        this.psId = psId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
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
}
