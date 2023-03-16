package com.java110.acct.payment.adapt.spdb;

/**
 * @author z
 **/
public class SPDBNormalException extends Exception {
    public SPDBNormalException(String errMsg, Exception e) {
        super(errMsg, e);
    }
}
