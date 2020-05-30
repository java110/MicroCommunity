package com.java110.core.event.app.order;

/**
 * 顺序
 * Created by wuxw on 2017/4/14.
 */
public interface Ordered {

    /**
     * 默认值
     */
    public final static int dafultValue = 0;

    /**
     * 获取顺序,为了同一个事件需要多个侦听处理时，需要有前后顺序
     * @return
     */
    public int getOrder();
}
