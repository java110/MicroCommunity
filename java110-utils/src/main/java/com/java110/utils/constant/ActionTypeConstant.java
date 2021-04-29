package com.java110.utils.constant;

/**
 * 订单动作定义类
 * Created by wuxw on 2017/4/24.
 */
public final class ActionTypeConstant {

    private ActionTypeConstant() {
    }

    /**
     * 添加客户信息
     */
    public static final String ACTION_TYPE_ADD_CUST = "C1";

    /**
     * 修改客户信息
     */
    public static final String ACTION_TYPE_EDIT_CUST = "C2";

    /**
     * 删除客户信息
     */
    public static final String ACTION_TYPE_DEL_CUST = "C3";

    /**
     * 添加账户信息
     */
    public static final String ACTION_TYPE_ADD_ACCOUNT = "A1";

    /**
     * 修改账户信息
     */
    public static final String ACTION_TYPE_EDIT_ACCOUNT = "A2";

    /**
     * 删除账户信息
     */
    public static final String ACTION_TYPE_DEL_ACCOUNT = "A3";

    /**
     * 增加商户信息
     */
    public static final String ACTION_TYPE_ADD_MERCHANT = "M1";

    /**
     * 编辑商户信息
     */
    public static final String ACTION_TYPE_EDIT_MERCHANT = "M2";

    /**
     * 删除商户信息
     */
    public static final String ACTION_TYPE_DEL_MERCHANT = "M3";


    /**
     * 撤单
     */
    public static final String ACTION_TYPE_CANCEL_ORDER = "D";
}
