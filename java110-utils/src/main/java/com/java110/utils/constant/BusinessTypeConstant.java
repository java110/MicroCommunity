package com.java110.utils.constant;

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
     * 保存用户属性信息
     */
    public final static String BUSINESS_TYPE_SAVE_USER_ATTR_INFO = "100200030001";

    /**
     * 修改用户属性信息
     */
    public final static String BUSINESS_TYPE_UPDATE_USER_ATTR_INFO = "100200040001";

    /**
     * 删除用户属性信息
     */
    public final static String BUSINESS_TYPE_DELETE_USER_ATTR_INFO = "100200050001";

    /**
     * 增加业主
     */
    public static final String BUSINESS_TYPE_SAVE_OWNER_INFO = "110100030001";

    /**tia
     * 修改业主
     */
    public static final String BUSINESS_TYPE_UPDATE_OWNER_INFO = "110100040001";


    /**
     * 删除业主
     */
    public static final String BUSINESS_TYPE_DELETE_OWNER_INFO = "110100050001";


    /**
     * 增加业主
     */
    public static final String BUSINESS_TYPE_SAVE_OWNER_ATTR_INFO = "110200030001";

    /**tia
     * 修改业主
     */
    public static final String BUSINESS_TYPE_UPDATE_OWNER_ATTR_INFO = "110200040001";


    /**
     * 删除业主
     */
    public static final String BUSINESS_TYPE_DELETE_OWNER_ATTR_INFO = "110200050001";


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
     * 保存业主车辆
     */
    public static final String BUSINESS_TYPE_SAVE_OWNER_CAR_ATTR = "111200030002";

    /**
     * 修改业主车辆
     */
    public static final String BUSINESS_TYPE_UPDATE_OWNER_CAR_ATTR = "111200040002";


    /**
     * 删除业主车辆
     */
    public static final String BUSINESS_TYPE_DELETE_OWNER_CAR_ATTR = "111200050002";

    /**
     * 保存车辆黑白名单
     */
    public static final String BUSINESS_TYPE_SAVE_CAR_BLACK_WHITE = "112200030001";

    /**
     * 修改车辆黑白名单
     */
    public static final String BUSINESS_TYPE_UPDATE_CAR_BLACK_WHITE = "112200040001";


    /**
     * 删除车辆黑白名单
     */
    public static final String BUSINESS_TYPE_DELETE_CAR_BLACK_WHITE = "112200050001";






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

    //保存商户属性
    public static final String BUSINESS_TYPE_SAVE_STORE_ATTR = "200100030003";

    //修改商户属性
    public static final String BUSINESS_TYPE_UPDATE_STORE_ATTR = "200100040003";

    //删除商户属性
    public static final String BUSINESS_TYPE_DELETE_STORE_ATTR = "200100050003";

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
     * 保存采购申请
     */
    public static final String BUSINESS_TYPE_SAVE_PURCHASE_APPLY = "210100030001";

    /**
     * 修改采购申请
     */
    public static final String BUSINESS_TYPE_UPDATE_PURCHASE_APPLY = "210100040001";

    /**
     * 删除采购申请
     */
    public static final String BUSINESS_TYPE_DELETE_PURCHASE_APPLY = "210100050001";

    //采购,出库 新增订单明细
    public static final String BUSINESS_TYPE_SAVE_PURCHASE_APPLY_DETAIL = "210100060001";
    //采购/出库 修改订单明细
    public static final String BUSINESS_TYPE_UPDATE_PURCHASE_APPLY_DETAIL = "210100070001";
    //采购,出库 删除订单明细
    public static final String BUSINESS_TYPE_DELETE_PURCHASE_APPLY_DETAIL = "210100080001";


    //保存小程序信息
    public static final String BUSINESS_TYPE_SAVE_SMALL_WE_CHAT = "210200060001";
    //修改小程序信息
    public static final String BUSINESS_TYPE_UPDATE_SMALL_WE_CHAT = "210200070001";
    //删除小程序信息
    public static final String BUSINESS_TYPE_DELETE_SMALL_WE_CHAT = "210200080001";

    //保存小程序信息
    public static final String BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR = "221200030001";
    //修改小程序信息
    public static final String BUSINESS_TYPE_UPDATE_SMALL_WECHAT_ATTR = "221200040001";
    //删除小程序信息
    public static final String BUSINESS_TYPE_DELETE_SMALL_WECHAT_ATTR = "221200050001";


    //保存公众号菜单
    public static final String BUSINESS_TYPE_SAVE_WECHAT_MENU = "221300030001";
    //修改公众号菜单
    public static final String BUSINESS_TYPE_UPDATE_WECHAT_MENU = "221300040001";
    //删除公众号菜单
    public static final String BUSINESS_TYPE_DELETE_WECHAT_MENU = "221300050001";

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

    //删除小区
    public static final String BUSINESS_TYPE_DELETE_COMMUNITY = "500100050011";

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
     * 审核 小区成员
     */
    public static final String BUSINESS_TYPE_AUDIT_COMMUNITY_MEMBER_STATE = "500100040003";

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
     * 增加小区楼属性
     */
    public static final String BUSINESS_TYPE_SAVE_FLOOR_ATTR_INFO = "510100030002";

    /**
     * 修改小区楼属性
     */
    public static final String BUSINESS_TYPE_UPDATE_FLOOR_ATTR_INFO = "510100040002";


    /**
     * 删除小区楼属性
     */
    public static final String BUSINESS_TYPE_DELETE_FLOOR_ATTR_INFO = "510100050002";


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
     * 增加小区单元 属性
     */
    public static final String BUSINESS_TYPE_SAVE_UNIT_ATTR_INFO = "520100030002";

    /**
     * 修改小区单元 属性
     */
    public static final String BUSINESS_TYPE_UPDATE_UNIT_ATTR_INFO = "520100040002";


    /**
     * 删除小区单元 属性
     */
    public static final String BUSINESS_TYPE_DELETE_UNIT_ATTR_INFO = "520100050002";

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
     * 增加停车场
     */
    public static final String BUSINESS_TYPE_SAVE_PARKING_AREA = "541100030001";

    /**
     * 修改车位
     */
    public static final String BUSINESS_TYPE_UPDATE_PARKING_AREA = "541100040001";


    /**
     * 删除车位
     */
    public static final String BUSINESS_TYPE_DELETE_PARKING_AREA = "541100050001";

    /**
     * 增加停车场
     */
    public static final String BUSINESS_TYPE_SAVE_PARKING_AREA_ATTR = "541100030002";

    /**
     * 修改车位
     */
    public static final String BUSINESS_TYPE_UPDATE_PARKING_AREA_ATTR = "541100040002";


    /**
     * 删除车位
     */
    public static final String BUSINESS_TYPE_DELETE_PARKING_AREA_ATTR = "541100050002";

    /**
     * 增加停车场
     */
    public static final String BUSINESS_TYPE_SAVE_PARKING_BOX = "541100030004";

    /**
     * 修改车位
     */
    public static final String BUSINESS_TYPE_UPDATE_PARKING_BOX = "541100040004";


    /**
     * 删除车位
     */
    public static final String BUSINESS_TYPE_DELETE_PARKING_BOX = "541100050004";



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
     * 增加车位
     */
    public static final String BUSINESS_TYPE_SAVE_PARKING_SPACE_ATTR = "540100030002";

    /**
     * 修改车位
     */
    public static final String BUSINESS_TYPE_UPDATE_PARKING_SPACE_ATTR = "540100040002";


    /**
     * 删除车位
     */
    public static final String BUSINESS_TYPE_DELETE_PARKING_SPACE_ATTR = "540100050002";

    /**
     * 添加进场记录
     */
    public static final String BUSINESS_TYPE_SAVE_CAR_INOUT = "541110030001";

    /**
     * 修改车位
     */
    public static final String BUSINESS_TYPE_UPDATE_CAR_INOUT = "541110040001";


    /**
     * 删除车位
     */
    public static final String BUSINESS_TYPE_DELETE_CAR_INOUT = "541110050001";


    /**
     * 添加进场记录详情
     */
    public static final String BUSINESS_TYPE_SAVE_CAR_INOUT_DETAIL = "541120030001";

    /**
     * 修改进场记录详情
     */
    public static final String BUSINESS_TYPE_UPDATE_CAR_INOUT_DETAIL = "541120040001";


    /**
     * 删除进场记录详情
     */
    public static final String BUSINESS_TYPE_DELETE_CAR_INOUT_DETAIL = "541120050001";


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
     * 修改费用信息
     */
    public static final String BUSINESS_TYPE_ONLY_UPDATE_FEE_INFO = "600100040002";
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
     * 保存退费信息
     */
    public static final String BUSINESS_TYPE_SAVE_RETURN_PAY_FEE = "621100030001";
    /**
     * 修改退费信息
     */
    public static final String BUSINESS_TYPE_UPDATE_RETURN_PAY_FEE = "621100040001";
    /**
     * 删除退费信息
     */
    public static final String BUSINESS_TYPE_DELETE_RETURN_PAY_FEE = "621100050001";


    /**
     * 删除住户
     */
    public static final String BUSINESS_TYPE_DELETE_PROPERTY_HOUSE = "600100050005";


    /**
     * 保存水电费
     */
    public static final String BUSINESS_TYPE_SAVE_METER_WATER = "631100030001";
    /**
     * 修改水电费
     */
    public static final String BUSINESS_TYPE_UPDATE_METER_WATER = "631100040001";
    /**
     * 删除水电费
     */
    public static final String BUSINESS_TYPE_DELETE_METER_WATER = "631100050001";





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
    public static final String BUSINESS_TYPE_SAVE_VISIT="120100030001";
    /**
     *  修改访客信息  11开头  4修改
     */
    public static final String BUSINESS_TYPE_UPDATE_VISIT="120100040001";
    /**
     *  删除访客信息  11开头  5修改
     */
    public static final String BUSINESS_TYPE_DELETE_VISIT ="120100050001";
    public static final String BUSINESS_TYPE_EXAMINE_VISIT ="120100060001";


    /**
     * 保存报修信息
     * 11开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_REPAIR="130100030001";
    /**
     *  修改报修信息  11开头  4修改
     */
    public static final String BUSINESS_TYPE_UPDATE_REPAIR="130100040001";
    /**
     *  删除报修信息  11开头  5修改
     */
    public static final String BUSINESS_TYPE_DELETE_REPAIR ="130100050001";

    /**
     * 保存报修信息
     * 11开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_REPAIR_SETTING="131100030001";
    /**
     *  修改报修信息  11开头  4修改
     */
    public static final String BUSINESS_TYPE_UPDATE_REPAIR_SETTING="131100040001";
    /**
     *  删除报修信息  11开头  5修改
     */
    public static final String BUSINESS_TYPE_DELETE_REPAIR_SETTING ="131100050001";

    /**
     * 保存报修信息
     * 11开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_REPAIR_TYPE_USER="132100030001";
    /**
     *  修改报修信息  11开头  4修改
     */
    public static final String BUSINESS_TYPE_UPDATE_REPAIR_TYPE_USER="132100040001";
    /**
     *  删除报修信息  11开头  5修改
     */
    public static final String BUSINESS_TYPE_DELETE_REPAIR_TYPE_USER ="132100050001";



    /**
     * 报修派单信息
     * 11开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_REPAIR_USER="130200030001";

    /**
     *  报修派单变更 11开头  4修改
     */
    public static final String BUSINESS_TYPE_UPDATE_REPAIR_USER="130200040001";

    /**
     *  作废报修派单信息  11开头  5修改
     */
    public static final String BUSINESS_TYPE_DELETE_REPAIR_USER ="130200050001";

    /**
     *  保存组织
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_ORG="140100030001";
    /**
     *  修改组织
     */
    public static final String BUSINESS_TYPE_UPDATE_ORG="140100040001";
    /**
     *  删除组织
     */
    public static final String BUSINESS_TYPE_DELETE_ORG ="140100050001";


    /**
     *  保存员工组织关系
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_ORG_STAFF_REL="140200030001";

    public static final String BUSINESS_TYPE_UPDATE_ORG_STAFF_REL="140200040001";
    /**
     *  删除员工组织关系
     */
    public static final String BUSINESS_TYPE_DELETE_ORG_STAFF_REL ="140200050001";

    /**
     *  保存员工组织关系
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_ORG_COMMUNITY="140300030001";

    public static final String BUSINESS_TYPE_UPDATE_ORG_COMMUNITY="140300040001";
    /**
     *  删除员工组织关系
     */
    public static final String BUSINESS_TYPE_DELETE_ORG_COMMUNITY ="140300050001";

    /**
     *  保存 资源
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_RESOURCE_STORE="150200030001";

    /**
     * 修改资源
     */
    public static final String BUSINESS_TYPE_UPDATE_RESOURCE_STORE="150200040001";
    /**
     *  删除资源
     */
    public static final String BUSINESS_TYPE_DELETE_RESOURCE_STORE ="150200050001";


    /**
     *  保存 审核用户
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_AUDIT_USER="160200030001";

    /**
     * 修改 审核用户
     */
    public static final String BUSINESS_TYPE_UPDATE_AUDIT_USER="160200040001";
    /**
     *  删除 审核用户
     */
    public static final String BUSINESS_TYPE_DELETE_AUDIT_USER ="160200050001";


    /**
     *  保存 审核用户
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_AUDIT_MESSAGE="170200030001";

    /**
     * 修改 审核用户
     */
    public static final String BUSINESS_TYPE_UPDATE_AUDIT_MESSAGE="170200040001";
    /**
     *  删除 审核用户
     */
    public static final String BUSINESS_TYPE_DELETE_AUDIT_MESSAGE ="170200050001";

    /**
     * 启动工作流流程
     */
    public static final String BUSINESS_TYPE_START_PROCESS ="180200030001";
   /* public static final String BUSINESS_TYPE_DELETE_AUDIT_MESSAGE ="170200050001";
    public static final String BUSINESS_TYPE_DELETE_AUDIT_MESSAGE ="170200050001";
*/

    /**
     *  保存 投诉意见
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_COMPLAINT="190200030001";

    /**
     * 修改 投诉意见
     */
    public static final String BUSINESS_TYPE_UPDATE_COMPLAINT="190200040001";
    /**
     *  删除 投诉意见
     */
    public static final String BUSINESS_TYPE_DELETE_COMPLAINT ="190200050001";


    /**
     *  保存 设备
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_MACHINE="200200030001";

    /**
     * 修改 设备
     */
    public static final String BUSINESS_TYPE_UPDATE_MACHINE="200200040001";
    /**
     *  删除 设备
     */
    public static final String BUSINESS_TYPE_DELETE_MACHINE ="200200050001";

    /**
     *  保存 设备同步
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_MACHINE_TRANSLATE="210200030001";

    /**
     * 修改 设备
     */
    public static final String BUSINESS_TYPE_UPDATE_MACHINE_TRANSLATE="210200040001";
    /**
     *  删除 设备
     */
    public static final String BUSINESS_TYPE_DELETE_MACHINE_TRANSLATE ="210200050001";


    /**
     *  保存 文件保存关系
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_FILE_REL="220200030001";

    /**
     * 修改 文件保存关系
     */
    public static final String BUSINESS_TYPE_UPDATE_FILE_REL="220200040001";
    /**
     *  删除 文件保存关系
     */
    public static final String BUSINESS_TYPE_DELETE_FILE_REL ="220200050001";

    /**
     *  保存 文件保存关系
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_MACHINE_RECORD="230200030001";

    /**
     * 修改 文件保存关系
     */
    public static final String BUSINESS_TYPE_UPDATE_MACHINE_RECORD="230200040001";
    /**
     *  删除 文件保存关系
     */
    public static final String BUSINESS_TYPE_DELETE_MACHINE_RECORD ="230200050001";

    /**
     *  保存 文件保存关系
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_APPLICATION_KEY="240200030001";

    /**
     * 修改 文件保存关系
     */
    public static final String BUSINESS_TYPE_UPDATE_APPLICATION_KEY="240200040001";
    /**
     *  删除 文件保存关系
     */
    public static final String BUSINESS_TYPE_DELETE_APPLICATION_KEY ="240200050001";

    /**
     *  保存 消息保存关系
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_MSG="250200030001";

    /**
     * 修改 消息保存关系
     */
    public static final String BUSINESS_TYPE_UPDATE_MSG="250200040001";
    /**
     *  删除 消息_MSG_MSG保存关系
     */
    public static final String BUSINESS_TYPE_DELETE_MSG ="250200050001";

    /**
     *  保存 消息阅读
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_MSG_READ="260200030001";

    /**
     * 修改 消息阅读
     */
    public static final String BUSINESS_TYPE_UPDATE_MSG_READ="260200040001";
    /**
     *  删除 消息阅读
     */
    public static final String BUSINESS_TYPE_DELETE_MSG_READ ="260200050001";

    /**
     *  保存广告
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_ADVERT="270200030001";

    /**
     * 修改广告
     */
    public static final String BUSINESS_TYPE_UPDATE_ADVERT="270200040001";
    /**
     *  删除广告
     */
    public static final String BUSINESS_TYPE_DELETE_ADVERT ="270200050001";

    /**
     *  保存广告
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_ADVERT_ITEM="271200030001";

    /**
     * 修改广告
     */
    public static final String BUSINESS_TYPE_UPDATE_ADVERT_ITEM="271200040001";
    /**
     *  删除广告
     */
    public static final String BUSINESS_TYPE_DELETE_ADVERT_ITEM ="271200050001";


    /**
     *  保存广告
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_OWNER_APP_USER="280200030001";

    /**
     * 修改广告
     */
    public static final String BUSINESS_TYPE_UPDATE_OWNER_APP_USER="280200040001";
    /**
     *  删除广告
     */
    public static final String BUSINESS_TYPE_DELETE_OWNER_APP_USER ="280200050001";


    /**
     *  保存活动
     * 14开头  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_ACTIVITIES="290200030001";

    /**
     * 修改活动
     */
    public static final String BUSINESS_TYPE_UPDATE_ACTIVITIES="290200040001";
    /**
     *  删除活动
     */
    public static final String BUSINESS_TYPE_DELETE_ACTIVITIES ="290200050001";


    /**
     *  保存巡检点
     */
    public static final String BUSINESS_TYPE_SAVE_INSPECTION="510200030001";

    /**
     * 修改巡检点
     */
    public static final String BUSINESS_TYPE_UPDATE_INSPECTION="510200040001";
    /**
     *  删除巡检点
     */
    public static final String BUSINESS_TYPE_DELETE_INSPECTION ="510200050001";

    /**
     *  保存巡检路线
     * 3保存
     */
    public static final String BUSINESS_TYPE_SAVE_INSPECTION_ROUTE="500200030001";

    /**
     * 修改巡检路线
     * 4 修改
     */
    public static final String BUSINESS_TYPE_UPDATE_INSPECTION_ROUTE="500200040001";
    /**
     *  删除巡检路线
     * 5 删除
     */
    public static final String BUSINESS_TYPE_DELETE_INSPECTION_ROUTE ="500200050001";
    /**
     *  保存巡检路线设备关联关系
     * 3保存
     */
    public static final String BUSINESS_TYPE_SAVE_INSPECTION_ROUTE_POINT_REL="500200030002";

    /**
     *  修改巡检路线设备关联关系
     *  4 删除
     */
    public static final String BUSINESS_TYPE_UPDATE_INSPECTION_ROUTE_POINT_REL ="500200040002";

    /**
     *  删除巡检路线设备关联关系
     *  5 删除
     */
    public static final String BUSINESS_TYPE_DELETE_INSPECTION_ROUTE_POINT_REL ="500200050002";


    /**
     *  保存巡检计划
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_INSPECTION_PLAN="520200030001";

    /**
     * 修改巡检计划
     * 4 修改
     */
    public static final String BUSINESS_TYPE_UPDATE_INSPECTION_PLAN="520200040001";

    /**
     *  删除巡计划
     *  5 删除
     */
    public static final String BUSINESS_TYPE_DELETE_INSPECTION_PLAN ="520200050001";

    /**
     *  保存巡检计划
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_PLAN_STAFF="520300030001";

    /**
     * 修改巡检计划
     * 4 修改
     */
    public static final String BUSINESS_TYPE_UPDATE_PLAN_STAFF="520300040001";

    /**
     *  删除巡计划
     *  5 删除
     */
    public static final String BUSINESS_TYPE_DELETE_PLAN_STAFF ="520300050001";


    /**
     *  保存用户标题测试
     * 3保存
     */
    public static final String BUSINESS_TYPE_SAVE_FASTUSER="666666668888888";

    /**
     *  保存巡检任务
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_INSPECTION_TASK="530200030001";
    public static final String BUSINESS_TYPE_UPDATE_INSPECTION_TASK="530200040001";
    public static final String BUSINESS_TYPE_DELETE_INSPECTION_TASK="530200050001";

    /**
     *  保存巡检任务
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_INSPECTION_TASK_DETAIL="540200030001";


    /**
     *  保存巡检任务
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_INSPECTION_TASK_DETAIL="540200040001";

    public static final String BUSINESS_TYPE_DELETE_INSPECTION_TASK_DETAIL="540200050001";

    /**
     *  保存旧货市场
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_JUNK_REQUIREMENT="550200030001";


    /**
     *  修改旧货市场
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_JUNK_REQUIREMENT="550200040001";

    /**
     * 删除旧货市场
     */
    public static final String BUSINESS_TYPE_DELETE_JUNK_REQUIREMENT="550200050001";

    /**
     *  保存任务
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_TASK="560200030001";


    /**
     *  修改任务
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_TASK="560200040001";

    /**
     * 删除任务
     */
    public static final String BUSINESS_TYPE_DELETE_TASK="560200050001";

    /**
     *  保存任务
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_TASK_ATTR="560300030001";


    /**
     *  修改任务
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_TASK_ATTR="560300040001";

    /**
     * 删除任务
     */
    public static final String BUSINESS_TYPE_DELETE_TASK_ATTR="560300050001";



    /**
     *  保存任务
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_LOCATION="570100030001";


    /**
     *  修改任务
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_LOCATION="570100040001";

    /**
     * 删除任务
     */
    public static final String BUSINESS_TYPE_DELETE_LOCATION="570100050001";

    /**
     *  保存任务
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_LOCATION_ATTR="570100030002";


    /**
     *  修改任务
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_LOCATION_ATTR="570100040002";

    /**
     * 删除任务
     */
    public static final String BUSINESS_TYPE_DELETE_LOCATION_ATTR="570100050002";


    /**
     *  保存工作流
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_WORKFLOW="580100030001";


    /**
     *  修改工作流
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_WORKFLOW="580100040001";

    /**
     * 删除工作流
     */
    public static final String BUSINESS_TYPE_DELETE_WORKFLOW="580100050001";

    /**
     *  保存工作流节点
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_WORKFLOW_STEP="580200030001";


    /**
     *  修改工作流节点
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_WORKFLOW_STEP="580200040001";

    /**
     * 删除工作流节点
     */
    public static final String BUSINESS_TYPE_DELETE_WORKFLOW_STEP="580200050001";

    /**
     *  保存工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_WORKFLOW_STEP_STAFF="580300030001";


    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_WORKFLOW_STEP_STAFF="580300040001";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_DELETE_WORKFLOW_STEP_STAFF="580300050001";
    /**
     *  保存工作流节点 处理员工 ORDER_FEE_SERVICE_URL
     *  3保存 feeServiceTopic
     */
    public static final String BUSINESS_TYPE_SAVE_DETAIL_DISCOUNT_INFO="641100030001";


    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_DETAIL_DISCOUNT_INFO="641100040001";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_DELETE_DETAIL_DISCOUNT_INFO="641100050001";

    /**
     *  保存工作流节点 处理员工 ORDER_FEE_SERVICE_URL
     *  3保存 feeServiceTopic
     */
    public static final String BUSINESS_TYPE_SAVE_TEMP_CAR_FEE_CONFIG_INFO="651100030001";


    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_TEMP_CAR_FEE_CONFIG_INFO="651100040001";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_DELETE_TEMP_CAR_FEE_CONFIG_INFO="651100050001";

    /**
     *  保存工作流节点 处理员工 ORDER_FEE_SERVICE_URL
     *  3保存 feeServiceTopic
     */
    public static final String BUSINESS_TYPE_SAVE_TEMP_CAR_FEE_CONFIG_ATTR_INFO="651100030002";


    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_TEMP_CAR_FEE_CONFIG_ATTR_INFO="651100040002";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_DELETE_TEMP_CAR_FEE_CONFIG_ATTR_INFO="651100050002";

    /**
     *  保存工作流节点 处理员工 ORDER_COMMON_SERVICE_URL
     *  3保存 commonServiceTopic
     */
    public static final String BUSINESS_TYPE_SAVE_ATTENDANCE_CLASSES="651100030003";


    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_ATTENDANCE_CLASSES="651100040003";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_DELETE_ATTENDANCE_CLASSES="651100050003";

    /**
     *  保存工作流节点 处理员工 ORDER_COMMON_SERVICE_URL
     *  3保存 commonServiceTopic
     */
    public static final String BUSINESS_TYPE_SAVE_ATTENDANCE_CLASSES_ATTR="651100030004";


    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_ATTENDANCE_CLASSES_ATTR="651100040004";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_DELETE_ATTENDANCE_CLASSES_ATTR="651100050004";


    /**
     *  保存工作流节点 处理员工 ORDER_COMMON_SERVICE_URL
     *  3保存 commonServiceTopic
     */
    public static final String BUSINESS_TYPE_SAVE_MACHINE_AUTH="661100030004";


    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_MACHINE_AUTH="661100040004";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_DELETE_MACHINE_AUTH="661100050004";

    /**
     * 开门记录 送databus
     */
    public static final String BUSINESS_TYPE_DATABUS_SEND_OPEN_LOG = "990000060001";

    /**
     * 物品申请通知节点流
     */
    public static final String BUSINESS_TYPE_DATABUS_PURCHASE_APPLY = "/purchase/purchaseApply";

    /**
     * 物品调拨通知节点流
     */
    public static final String BUSINESS_TYPE_DATABUS_ALLOCATION_STOREHOUSE_APPLY = "671100030005";

    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_STOREHOUSE="671100030004";

    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_STOREHOUSE="671100040004";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_DELETE_STOREHOUSE="671100050004";


    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_ALLOCATION_STOREHOUSE="671100030005";

    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_ALLOCATION_STOREHOUSE="671100040005";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_DELETE_ALLOCATION_STOREHOUSE="671100050005";


    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_USER_STOREHOUSE="671100030006";

    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_USER_STOREHOUSE="671100040006";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_DELETE_USER_STOREHOUSE="671100050006";

    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_RESOURCE_STORE_TYPE="671100030007";

    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_RESOURCE_STORE_TYPE="671100040007";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_DELETE_RESOURCE_STORE_TYPE="671100050007";

    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_RESOURCE_SUPPLIER="671100030008";

    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_RESOURCE_SUPPLIER="671100040008";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_DELETE_RESOURCE_SUPPLIER="671100050008";
    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_ALLOCATION_USER_STOREHOUSE="671100030009";

    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_ALLOCATION_USER_STOREHOUSE="671100040009";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_DELETE_ALLOCATION_USER_STOREHOUSE="671100050009";
    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_RESOURCE_STORE_USE_RECORD="671100030010";

    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_RESOURCE_STORE_USE_RECORD="671100040010";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_DELETE_RESOURCE_STORE_USE_RECORD="671100050010";


    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_ALLOCATION_STOREHOUSE_APPLY="681100030006";

    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_ALLOCATION_STOREHOUSE_APPLY="681100040006";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_SAVE_RESOURCE_STORE_SPECIFICATION="671100030011";

    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_RESOURCE_STORE_SPECIFICATION="671100040011";

    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_DELETE_RESOURCE_STORE_SPECIFICATION="671100050011";


    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_SAVE_APPLY_ROOM_DISCOUNT_RECORD="671200030011";

    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_APPLY_ROOM_DISCOUNT_RECORD="671200040011";

    /**
     *  修改工作流节点 处理员工
     *  3保存
     */
    public static final String BUSINESS_TYPE_DELETE_APPLY_ROOM_DISCOUNT_RECORD="671200050011";

    /**
     * 删除工作流节点 处理员工
     */
    public static final String BUSINESS_TYPE_DELETE_ALLOCATION_STOREHOUSE_APPLY="681100050006";


    /**
     *  添加账户
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_ACCT="121100030001";

    /**
     *  修改账户
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_ACCT="121100040001";

    /**
     * 删除账户
     */
    public static final String BUSINESS_TYPE_DELETE_ACCT="121100050001";


    /**
     *  添加账户明细
     *  3保存
     */
    public static final String BUSINESS_TYPE_SAVE_ACCT_DETAIL="121100030002";

    /**
     *  修改账户明细
     *  3保存
     */
    public static final String BUSINESS_TYPE_UPDATE_ACCT_DETAIL="121100040002";

    /**
     * 删除账户明细
     */
    public static final String BUSINESS_TYPE_DELETE_ACCT_DETAIL="121100050002";


}
