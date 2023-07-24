package com.java110;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }


    @Test
    public void parseInteger(){
        String communityId = "702019051443120001";
        String str = String.format("%0"+24+"d", 991);
        System.out.println(str);
    }
}
