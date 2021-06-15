package com.java110.core.event.center;

import com.java110.core.event.app.order.Ordered;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 侦听排序类
 * Created by wuxw on 2018/4/17.
 */
public class DataFlowListenerOrderComparator implements Comparator<Object> {

    public static final DataFlowListenerOrderComparator INSTANCE = new DataFlowListenerOrderComparator();



    public static void sort(List<?> list) {
        if (list.size() > 1) {
            Collections.sort(list, INSTANCE);
        }
    }

    @Override
    public int compare(Object o1, Object o2) {
        return doCompare(o1,o2);
    }

    private Integer getOrder(Object obj){
        return (obj instanceof Ordered ? ((Ordered) obj).getOrder() : null);
    }

    private int doCompare(Object o1, Object o2) {
        boolean p1 = (o1 instanceof Ordered);
        boolean p2 = (o2 instanceof Ordered);
        if (p1 && !p2) {
            return -1;
        }
        else if (p2 && !p1) {
            return 1;
        }

        int i1 = getOrder(o1);
        int i2 = getOrder(o2);
        return (i1 < i2) ? -1 : (i1 > i2) ? 1 : 0;
    }
}
