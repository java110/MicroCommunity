package com.java110.dto.integral;

import java.io.Serializable;

public class DeductionIntegralDto implements Serializable {

    private int integral;

    private double money;

    public DeductionIntegralDto(int integral, double money) {
        this.integral = integral;
        this.money = money;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
