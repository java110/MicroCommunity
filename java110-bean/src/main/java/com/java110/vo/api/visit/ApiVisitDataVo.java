package com.java110.vo.api.visit;

import java.io.Serializable;
import java.util.Date;

public class ApiVisitDataVo implements Serializable {

    private String vId;
    private String name;
    private String visitGender;
    private String visitGenderName;
    private String phoneNumber;
    private String visitTime;
    private String departureTime;
    private String visitCase;

    public String getVId() {
        return vId;
    }

    public void setVId(String vId) {
        this.vId = vId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVisitGender() {
        return visitGender;
    }

    public void setVisitGender(String visitGender) {
        this.visitGender = visitGender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getVisitCase() {
        return visitCase;
    }

    public void setVisitCase(String visitCase) {
        this.visitCase = visitCase;
    }

    public String getvId() {
        return vId;
    }

    public void setvId(String vId) {
        this.vId = vId;
    }

    public String getVisitGenderName() {
        return visitGenderName;
    }

    public void setVisitGenderName(String visitGenderName) {
        this.visitGenderName = visitGenderName;
    }
}
