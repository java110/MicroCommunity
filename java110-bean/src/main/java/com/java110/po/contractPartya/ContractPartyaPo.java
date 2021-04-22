package com.java110.po.contractPartya;

import java.io.Serializable;

public class ContractPartyaPo implements Serializable {

    private String partyA;
    private String aLink;
    private String aContacts;
    private String partyaId;
    private String statusCd = "0";
    private String storeId;


    public String getPartyA() {
        return partyA;
    }

    public void setPartyA(String partyA) {
        this.partyA = partyA;
    }

    public String getaLink() {
        return aLink;
    }

    public void setaLink(String aLink) {
        this.aLink = aLink;
    }

    public String getaContacts() {
        return aContacts;
    }

    public void setaContacts(String aContacts) {
        this.aContacts = aContacts;
    }

    public String getPartyaId() {
        return partyaId;
    }

    public void setPartyaId(String partyaId) {
        this.partyaId = partyaId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
