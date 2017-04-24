package com.java110.order.jsonpath;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.java110.order.AppTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by wuxw on 2017/4/24.
 */
public class CustIdTest  extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CustIdTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( CustIdTest.class );
    }

    public void testCustId(){
        String jsonStr = " { \"actionTypeCd\": \"C1\", \"boCust\": [ " +
                "{ \"custId\": \"123\", \"name\": \"S\", \"email\": \"-52\", \"cellphone\": \"17797173942\", \"realName\": \"wuxw\", \"sex\": \"1\", \"password\": \"123456\", \"lanId\": \"863010\", \"custAdress\": \"青海省西宁市城中区格兰小镇\", \"custType\": \"1\", \"openId\": \"\", \"state\": \"ADD\" }, { \"custId\": \"-1\", \"name\": \"S\", \"email\": \"-52\", \"cellphone\": \"17797173942\", \"realName\": \"wuxw\", \"sex\": \"1\", \"password\": \"123456\", \"lanId\": \"863010\", \"custAdress\": \"青海省西宁市城中区格兰小镇\", \"custType\": \"1\", \"openId\": \"\", \"state\": \"DEL\" } ], \"boCustAttr\": [ { \"custId\": \"123\", \"prodId\": \"-1\", \"attrCd\": \"123344\", \"value\": \"1\", \"state\": \"ADD\" }, { \"custId\": \"123\", \"prodId\": \"-1\", \"attrCd\": \"123345\", \"value\": \"1\", \"state\": \"DEL\" } ] } ";
        JSONObject custInfo = JSONObject.parseObject(jsonStr);
        System.out.println(JSONPath.eval(custInfo,"$.boCust[custId < '0'][0].custId"));
    }
}
