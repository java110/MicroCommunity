package com.java110.utils.constant;

/**
 * 过程数据状态常量
 * Created by wuxw on 2017/5/20.
 */
public class StateConstant {

    private StateConstant() {

    }

    //未审核
    public static final String NO_AUDIT = "1000";


    //审核同意
    public static final String AGREE_AUDIT = "1100";

    //审核拒绝
    public static final String REJECT_AUDIT = "1200";


    /**
     * 未派单
     */
    public static final String REPAIR_NO_DISPATCH = "1000";

    /**
     * 拍单处理中
     */
    public static final String REPAIR_DISPATCHING = "1100";

    /**
     * 拍单处理完成
     */
    public static final String REPAIR_DISPATCH_FINISH = "1200";

    //未结单
    public static final String STAFF_NO_FINISH_ORDER = "10001";

    //结单
    public static final String STAFF_FINISHED_ORDER = "10002";

    //退单
    public static final String STAFF_BACK_ORDER = "10003";


}
