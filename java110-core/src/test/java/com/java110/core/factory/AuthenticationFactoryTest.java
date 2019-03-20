package com.java110.core.factory;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wuxw on 2019/3/20.
 */
public class AuthenticationFactoryTest {
    @Test
    public void passwdMd5() throws Exception {
        System.out.println(AuthenticationFactory.passwdMd5("admin"));
    }

}