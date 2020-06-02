package com.java110.log.common;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.user.BoCustAttr;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wuxw on 2017/4/29.
 */
public class PreBoCustAttr extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PreBoCustAttr( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( PreBoCustAttr.class );
    }


    public void testGetPreBoCustAttr(){
        List<BoCustAttr> boCustAttrs = new ArrayList<BoCustAttr>();
        BoCustAttr boCustAttr = null;
        for(int boCustAttrIndex = 0; boCustAttrIndex < 3; boCustAttrIndex++){
            boCustAttr = new BoCustAttr();

            boCustAttr.setCustId("1234456");
            boCustAttr.setBoId("1");
            boCustAttr.setAttrCd("BoCustAttr"+boCustAttrIndex);
            boCustAttr.setCreate_dt(new Date());
            boCustAttr.setState("ADD");
            boCustAttr.setValue(boCustAttrIndex+"");
            boCustAttrs.add(boCustAttr);
        }

        for(int boCustAttrIndex = 0; boCustAttrIndex < 5; boCustAttrIndex++){
            boCustAttr = new BoCustAttr();

            boCustAttr.setCustId("1234456");
            boCustAttr.setBoId("2");
            boCustAttr.setAttrCd("BoCustAttr"+boCustAttrIndex);
            boCustAttr.setCreate_dt(new Date());
            boCustAttr.setState("ADD");
            boCustAttr.setValue(boCustAttrIndex+"");
            boCustAttrs.add(boCustAttr);
        }

        for(int boCustAttrIndex = 0; boCustAttrIndex < 3; boCustAttrIndex++){
            boCustAttr = new BoCustAttr();

            boCustAttr.setCustId("1234456");
            boCustAttr.setBoId("3");
            boCustAttr.setAttrCd("BoCustAttr"+boCustAttrIndex);
            boCustAttr.setCreate_dt(new Date());
            boCustAttr.setState("ADD");
            boCustAttr.setValue(boCustAttrIndex+"");
            boCustAttrs.add(boCustAttr);
        }

        for (BoCustAttr boCustAttrTml : boCustAttrs){
            System.out.println(JSONObject.toJSON(boCustAttrTml));
        }

        System.out.println("-----------------------------处理前------------------------------------");

        boCustAttrs = getPreBoCustAttrs(boCustAttrs);

        for (BoCustAttr boCustAttrTml : boCustAttrs){
            System.out.println(JSONObject.toJSON(boCustAttrTml));
        }

        System.out.println("-----------------------------处理后------------------------------------");

    }

    /**
     * 获取上上一次的操作
     * @param boCustAttrs
     * @return
     */
    private List<BoCustAttr> getPreBoCustAttrs(List<BoCustAttr> boCustAttrs){

        String firstBoId = boCustAttrs.get(0).getBoId();
        String preBoId = "";
        List<BoCustAttr> preBoCustAttrs = new ArrayList<BoCustAttr>();
        for(BoCustAttr boCustAttr : boCustAttrs){
            if(!firstBoId.equals(boCustAttr.getBoId())){
                if(!preBoId.equals(boCustAttr.getBoId()) && !"".equals(preBoId)){
                    break;
                }
                preBoId = boCustAttr.getBoId();
                preBoCustAttrs.add(boCustAttr);
            }
        }
        return preBoCustAttrs;
    }

}
