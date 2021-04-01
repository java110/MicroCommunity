package com.java110.utils.constant;

/**
 * @author wux
 * @create 2019-02-05 下午11:28
 * @desc 业务类型
 * <p>
 * 用户为1001 开头
 * 商户为2001 开头
 * 商品为3001 开头
 * 评论为4001 开头
 * 小区为5001 开头
 * 物业为6001 开头
 * 代理商为7001 开头
 * 第八位 3 代表保存 4代表修改 5 代表删除
 * 第八位之后 为相应业务序列
 **/
public class FeeTypeConstant {

    /**
     * 物业费
     */
    public static final String FEE_TYPE_PROPERTY = "888800010001";

    /**
     * 物业费
     */
    public static final String FEE_TYPE_CAR = "888800010008";

    /**
     * 维修费
     */
    public static final String FEE_TYPE_REPAIR = "888800010012";


    /**
     * 停车费 地上 出售费用类型
     */
    public static final String FEE_TYPE_SELL_UP_PARKING_SPACE = "888800010002";

    /**
     * 停车费 地下 出售费用类型
     */
    public static final String FEE_TYPE_SELL_DOWN_PARKING_SPACE = "888800010003";

    /**
     * 停车费 地上 出租费用类型
     */
    public static final String FEE_TYPE_HIRE_UP_PARKING_SPACE = "888800010004";

    /**
     * 停车费 地下 出租费用类型
     */
    public static final String FEE_TYPE_HIRE_DOWN_PARKING_SPACE = "888800010005";


    /**
     * 停车费 出租费用类型
     */
    public static final String FEE_TYPE_HIRE_PARKING_SPACE = "888800010006";


    /**
     * 停车费 地下 临时车费用
     */
    public static final String FEE_TYPE_TEMP_DOWN_PARKING_SPACE = "888800010007";


    /**
     * 停车费 地上 临时车费用
     */
//    public static final String FEE_TYPE_TEMP_UP_PARKING_SPACE = "888800010008";


}
