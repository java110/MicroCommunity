package com.java110.common.constant;

/**
 * @author wux
 * @create 2019-02-05 下午11:28
 * @desc 业务类型
 *
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
public class BusinessTypeConstant {

    /**
     * 保存用户信息
     */
    public final static String BUSINESS_TYPE_SAVE_USER_INFO = "100100030001";


    /**
     * 保存用户地址信息
     */
    public static final String BUSINESS_TYPE_SAVE_USER_ADDRESS = "100100030002";


    /**
     * 用户打标
     */
    public static final String BUSINESS_TYPE_SAVE_USER_TAG = "100100030003";


    /**
     * 用户证件
     */
    public static final String BUSINESS_TYPE_SAVE_USER_CREDENTIALS = "100100030004";


    /**
     * 修改用户信息
     */
    public final static String BUSINESS_TYPE_MODIFY_USER_INFO = "100100040001";

    /**
     * 停用用户信息
     */
    public static final String BUSINESS_TYPE_REMOVE_USER_INFO = "100100040002";


    /**
     * 恢复用户信息
     */
    public static final String BUSINESS_TYPE_RECOVER_USER_INFO = "100100040003";

    /**
     * 增加业主
     */
    public static final String BUSINESS_TYPE_SAVE_OWNER_INFO = "110100030001";

    /**
     * 修改业主
     */
    public static final String BUSINESS_TYPE_UPDATE_OWNER_INFO = "110100040001";


    /**
     * 删除业主
     */
    public static final String BUSINESS_TYPE_DELETE_OWNER_INFO = "110100050001";


    /**
     * 房屋购买
     */
    public static final String BUSINESS_TYPE_SAVE_OWNER_ROOM_REL = "111100030001";

    /**
     * 房屋状态变更
     */
    public static final String BUSINESS_TYPE_UPDATE_OWNER_ROOM_REL = "111100040001";


    /**
     * 房屋转售
     */
    public static final String BUSINESS_TYPE_DELETE_OWNER_ROOM_REL = "111100050001";


    /**
     * 保存业主车辆
     */
    public static final String BUSINESS_TYPE_SAVE_OWNER_CAR = "111200030001";

    /**
     * 修改业主车辆
     */
    public static final String BUSINESS_TYPE_UPDATE_OWNER_CAR = "111200040001";


    /**
     * 删除业主车辆
     */
    public static final String BUSINESS_TYPE_DELETE_OWNER_CAR = "111200050001";





    /**
     * 保存商户信息
     */
    public static final String BUSINESS_TYPE_SAVE_STORE_INFO = "200100030001";

    /**
     * 商户成员加入信息
     */
    public static final String BUSINESS_TYPE_MEMBER_JOINED_STORE = "200100030002";


    /**
     * 修改商户信息
     */
    public static final String BUSINESS_TYPE_UPDATE_STORE_INFO = "200100040001";

    /**
     * 商户成员退出信息
     */
    public static final String BUSINESS_TYPE_MEMBER_QUIT_STORE = "200100040002";

    /**
     * 删除商户信息
     */
    public static final String BUSINESS_TYPE_DELETE_STORE_INFO = "200100050001";


    /**
     * 保存商户员工
     */
    public static final String BUSINESS_TYPE_SAVE_STORE_USER = "200100060001";



    /**
     * 删除商户员工
     */
    public static final String BUSINESS_TYPE_DELETE_STORE_USER = "200100070001";


    /**
     * 保存商品信息
     */
    public static final String BUSINESS_TYPE_SAVE_SHOP_INFO = "300100030001";

    /**
     * 购买商品
     */
    public static final String BUSINESS_TYPE_BUY_SHOP_INFO = "300100030002";

    /**
     * 保存商品目录
     */
    public static final String BUSINESS_TYPE_SAVE_SHOP_CATALOG = "300100030003";


    /**
     * 修改商品信息
     */
    public static final String BUSINESS_TYPE_UPDATE_SHOP_INFO = "300100040001";

    /**
     * 修改商品目录
     */
    public static final String BUSINESS_TYPE_UPDATE_SHOP_CATALOG = "300100040002";

    /**
     * 删除商品信息
     */
    public static final String BUSINESS_TYPE_DELETE_SHOP_INFO = "300100050001";


    /**
     * 修改商品目录
     */
    public static final String BUSINESS_TYPE_DELETE_SHOP_CATALOG = "300100050002";

    /**
     * 保存评论
     */
    public static final String BUSINESS_TYPE_SAVE_COMMENT_INFO = "400100030001";

    /**
     * 删除评论
     */
    public static final String BUSINESS_TYPE_DELETE_COMMENT_INFO = "400100050001";


    /**
     * 保存小区信息
     */
    public static final String BUSINESS_TYPE_SAVE_COMMUNITY_INFO = "500100030001";

    /**
     * 小区成员加入信息
     */
    public static final String BUSINESS_TYPE_MEMBER_JOINED_COMMUNITY = "500100030002";

    /**
     * 修改商户信息
     */
    public static final String BUSINESS_TYPE_UPDATE_COMMUNITY_INFO = "500100040001";


    /**
     * 小区成员退出信息
     */
    public static final String BUSINESS_TYPE_MEMBER_QUIT_COMMUNITY = "500100040002";

    /**
     * 删除商户信息
     */
    public static final String BUSINESS_TYPE_DELETE_COMMUNITY_INFO = "500100050001";

    /**
     * 增加小区楼
     */
    public static final String BUSINESS_TYPE_SAVE_FLOOR_INFO = "510100030001";

    /**
     * 修改小区楼
     */
    public static final String BUSINESS_TYPE_UPDATE_FLOOR_INFO = "510100040001";


    /**
     * 删除小区楼
     */
    public static final String BUSINESS_TYPE_DELETE_FLOOR_INFO = "510100050001";


    /**
     * 增加小区单元
     */
    public static final String BUSINESS_TYPE_SAVE_UNIT_INFO = "520100030001";

    /**
     * 修改小区单元
     */
    public static final String BUSINESS_TYPE_UPDATE_UNIT_INFO = "520100040001";


    /**
     * 删除小区单元
     */
    public static final String BUSINESS_TYPE_DELETE_UNIT_INFO = "520100050001";

    /**
     * 增加房屋
     */
    public static final String BUSINESS_TYPE_SAVE_ROOM_INFO = "530100030001";

    /**
     * 修改房屋
     */
    public static final String BUSINESS_TYPE_UPDATE_ROOM_INFO = "530100040001";


    /**
     * 删除房屋
     */
    public static final String BUSINESS_TYPE_DELETE_ROOM_INFO = "530100050001";

    /**
     * 增加车位
     */
    public static final String BUSINESS_TYPE_SAVE_PARKING_SPACE = "540100030001";

    /**
     * 修改车位
     */
    public static final String BUSINESS_TYPE_UPDATE_PARKING_SPACE = "540100040001";


    /**
     * 删除车位
     */
    public static final String BUSINESS_TYPE_DELETE_PARKING_SPACE = "540100050001";


    /**
     * 发布公告
     */
    public static final String BUSINESS_TYPE_SAVE_NOTICE = "550100030001";

    /**
     * 修改公告
     */
    public static final String BUSINESS_TYPE_UPDATE_NOTICE = "550100040001";


    /**
     * 删除公告
     */
    public static final String BUSINESS_TYPE_DELETE_NOTICE = "550100050001";



    /**
     * 保存费用信息
     */
    public static final String BUSINESS_TYPE_SAVE_FEE_INFO = "600100030001";
    /**
     * 修改费用信息
     */
    public static final String BUSINESS_TYPE_UPDATE_FEE_INFO = "600100040001";
    /**
     * 删除费用信息
     */
    public static final String BUSINESS_TYPE_DELETE_FEE_INFO = "600100050001";

    /**
     * 保存费用明细信息
     */
    public static final String BUSINESS_TYPE_SAVE_FEE_DETAIL = "610100030001";
    /**
     * 修改费用明细信息
     */
    public static final String BUSINESS_TYPE_UPDATE_FEE_DETAIL = "610100040001";
    /**
     * 删除费用明细信息
     */
    public static final String BUSINESS_TYPE_DELETE_FEE_DETAIL = "610100050001";

    /**
     * 保存费用配置信息
     */
    public static final String BUSINESS_TYPE_SAVE_FEE_CONFIG = "620100030001";
    /**
     * 修改费用配置信息
     */
    public static final String BUSINESS_TYPE_UPDATE_FEE_CONFIG = "620100040001";
    /**
     * 删除费用配置信息
     */
    public static final String BUSINESS_TYPE_DELETE_FEE_CONFIG = "620100050001";


    /**
     * 删除住户
     */
    public static final String BUSINESS_TYPE_DELETE_PROPERTY_HOUSE = "600100050005";


    /**
     * 保存代理商信息
     */
    public static final String BUSINESS_TYPE_SAVE_AGENT_INFO = "700100030001";
    /**
     * 保存代理商照片
     */
    public static final String BUSINESS_TYPE_SAVE_AGENT_PHOTO = "700100030002";
    /**
     * 保存代理商证件
     */
    public static final String BUSINESS_TYPE_SAVE_AGENT_CERDENTIALS = "700100030003";
    /**
     * 添加代理商员工
     */
    public static final String BUSINESS_TYPE_SAVE_AGENT_USER = "700100030004";
    /**
     * 修改代理商信息
     */
    public static final String BUSINESS_TYPE_UPDATE_AGENT_INFO = "700100040001";
    /**
     * 修改代理商照片
     */
    public static final String BUSINESS_TYPE_UPDATE_AGENT_PHOTO = "700100040002";
    /**
     * 修改代理商证件
     */
    public static final String BUSINESS_TYPE_UPDATE_AGENT_CERDENTIALS = "700100040003";

    /**
     * 删除代理商属性
     */
    public static final String BUSINESS_TYPE_DELETE_AGENT_ATTR = "700100050001";

    /**
     * 删除代理商照片
     */
    public static final String BUSINESS_TYPE_DELETE_AGENT_PHOTO = "700100050002";

    /**
     * 删除代理商证件
     */
    public static final String BUSINESS_TYPE_DELETE_AGENT_CERDENTIALS = "700100050003";

    /**
     * 删除代理商员工
     */
    public static final String BUSINESS_TYPE_DELETE_AGENT_USER = "700100050004";

    /**
     * 删除方法编码
     */
    public static final String BUSINESS_TYPE_DELETE_DEMO_INFO = "900100050001";
    /**
     * 保存方法编码
     */
    public static final String BUSINESS_TYPE_SAVE_DEMO_INFO = "900100030001";

    /**
     * 保存方法编码
     */
    public static final String BUSINESS_TYPE_UPDATE_DEMO_INFO = "900100040001";
    /**
     * 修改方法编码
     */
    /**
     * 删除BUSINESSTYPE方法编码
     */
    public static final String BUSINESS_TYPE_DELETE_BUSINESSTYPE_INFO = "900100050002";
    /**
     * 保存BUSINESSTYPE方法编码
     */
    public static final String BUSINESS_TYPE_SAVE_BUSINESSTYPE_INFO = "900100030002";
    /**
     * 修改BUSINESSTYPE方法编码
     */
    public static final String BUSINESS_TYPE_UPDATE_BUSINESSTYPE_INFO = "900100040002";
    /**
     * 保存访客信息
     * 11开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_VISIT="110100030001";
    /**
     *  修改访客信息  11开头  4修改
     */
    public static final String BUSINESS_TYPE_UPDATE_VISIT="110100040001";
    /**
     *  删除访客信息  11开头  5修改
     */
    public static final String BUSINESS_TYPE_DELETE_VISIT ="110100050001";

}
