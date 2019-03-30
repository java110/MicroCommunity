package com.java110.entity.order;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2019/3/30.
 */
public class BusinessTest extends TestCase {

    public void testCompareTo() throws Exception {
        Business business1 = new Business();
        business1.setSeq(1);

        Business business2 = new Business();
        business2.setSeq(2);

        Business business3 = new Business();
        business3.setSeq(3);

        List<Business> businesses = new ArrayList<Business>();
        businesses.add(business1);
        businesses.add(business3);
        businesses.add(business2);
        System.out.println("--------------------排序前");
        for(Business business :businesses){
            System.out.println(business.getSeq());
        }

        System.out.println("--------------------排序后");

        Collections.sort(businesses);
        for(Business business :businesses){
            System.out.println(business.getSeq());
        }
    }

}