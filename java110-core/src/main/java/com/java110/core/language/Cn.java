package com.java110.core.language;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.menu.MenuCatalogDto;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 繁体
 */
@Component
public class Cn extends DefaultLanguage {
    private static Map<String, String> menuCatalogs = new HashMap<>();
    private static Map<String, String> menus = new HashMap<>();
    private static Map<String, String> msgs = new HashMap<>();


    public List<MenuCatalogDto> getMenuCatalog(List<MenuCatalogDto> menuCatalogDtos) {
        String menuCatalogsName = "";
        for (MenuCatalogDto menuCatalogDto : menuCatalogDtos) {
            menuCatalogsName = menuCatalogs.get(menuCatalogDto.getName());
            if (!StringUtil.isEmpty(menuCatalogsName)) {
                menuCatalogDto.setName(menuCatalogsName);
            }
        }
        return menuCatalogDtos;
    }


    @Override
    public List<Map> getMenuDto(List<Map> menuDtos) {
        String menuName = "";
        for (Map menuDto : menuDtos) {
            menuName = menus.get(menuDto.get("menuGroupName"));
            if (!StringUtil.isEmpty(menuName)) {
                menuDto.put("menuGroupName", menuName);
            }

            menuName = menus.get(menuDto.get("menuName"));
            if (!StringUtil.isEmpty(menuName)) {
                menuDto.put("menuName", menuName);
            }

        }
        return menuDtos;
    }

    @Override
    public JSONArray getPrivilegeMenuDto(JSONArray tmpPrivilegeArrays) {
        String menuName = "";
        JSONObject priObj = null;
        for (int priIndex = 0; priIndex < tmpPrivilegeArrays.size(); priIndex++) {
            priObj = tmpPrivilegeArrays.getJSONObject(priIndex);
            menuName = menus.get(priObj.get("gName"));
            if (!StringUtil.isEmpty(menuName)) {
                priObj.put("gName", menuName);
            }

            menuName = menus.get(priObj.get("mName"));
            if (!StringUtil.isEmpty(menuName)) {
                priObj.put("mName", menuName);
            }

            menuName = menus.get(priObj.get("pName"));
            if (!StringUtil.isEmpty(menuName)) {
                priObj.put("pName", menuName);
            }

        }
        return tmpPrivilegeArrays;
    }

    @Override
    public ResultVo getResultVo(ResultVo resultVo) {
        String msg = msgs.get(resultVo.getMsg());
        if (!StringUtil.isEmpty(msg)) {
            resultVo.setMsg(msg);
        }

        return resultVo;
    }

    public String getLangMsg(String msg) {
        String msgStr = msgs.get(msg);
        if (!StringUtil.isEmpty(msgStr)) {
            return msgStr;
        }

        return msg;
    }

    static {
        menuCatalogs.put("设备", "設備");
        menuCatalogs.put("首页", "首頁");
        menuCatalogs.put("业务受理", "業務受理");
        menuCatalogs.put("费用报表", "費用報表");
        menuCatalogs.put("物业服务", "物業服務");
        menuCatalogs.put("设备停车", "設備停車");
        menuCatalogs.put("常用菜单", "常用菜單");
        menuCatalogs.put("设置", "設置");

        menus.put("小区管理", "小區管理");
        menus.put("我的小区", "我的小區");
        menus.put("小区大屏", "小區大屏");
        menus.put("业务受理", "業務受理");
        menus.put("房屋装修", "房屋裝修");
        menus.put("结构图", "結構圖");
        menus.put("车位结构图", "車位結構圖");
        menus.put("产权登记", "產權登記");
        menus.put("视频监控", "視頻監控");
        menus.put("资产管理", "資產管理");
        menus.put("楼栋信息", "樓棟信息");
        menus.put("单元信息", "單元信息");
        menus.put("房屋管理", "房屋管理");
        menus.put("商铺管理", "商舖管理");
        menus.put("业主管理", "業主管理");
        menus.put("业主信息", "業主信息");
        menus.put("业主成员", "業主成員");
        menus.put("绑定业主", "綁定業主");
        menus.put("业主账号", "業主賬號");
        menus.put("费用管理", "費用管理");
        menus.put("费用项设置", "費用項設置");
        menus.put("房屋收费", "房屋收費");
        menus.put("车辆收费", "車輛收費");
        menus.put("合同收费", "合同收費");
        menus.put("账单催缴", "賬單催繳");
        menus.put("费用导入", "費用導入");
        menus.put("员工收费", "員工收費");
        menus.put("费用汇总表", "費用匯總表");
        menus.put("退费审核", "退費審核");
        menus.put("欠费信息", "欠費信息");
        menus.put("抄表类型", "抄表類型");
        menus.put("水电抄表", "水電抄表");
        menus.put("补打收据", "補打收據");
        menus.put("公摊公式", "公攤公式");
        menus.put("缴费审核", "繳費審核");
        menus.put("折扣设置", "折扣設置");
        menus.put("优惠申请", "優惠申請");
        menus.put("优惠类型", "優惠類型");
        menus.put("临时车收费", "臨時車收費");
        menus.put("临时车缴费清单", "臨時車繳費清單");
        menus.put("取消费用", "取消費用");
        menus.put("智慧停车", "智慧停車");
        menus.put("停车场管理", "停車場管理");
        menus.put("车位信息", "車位信息");
        menus.put("岗亭管理", "崗亭管理");
        menus.put("业主车辆", "業主車輛");
        menus.put("进场记录", "進場記錄");
        menus.put("在场车辆", "在場車輛");
        menus.put("黑白名单", "黑白名單");
        menus.put("剩余车位", "剩餘車位");
        menus.put("车位申请", "車位申請");
        menus.put("报修管理", "報修管理");
        menus.put("报修设置", "報修設置");
        menus.put("电话报修", "電話報修");
        menus.put("工单池", "工單池");
        menus.put("报修待办", "報修待辦");
        menus.put("报修已办", "報修已辦");
        menus.put("报修回访", "報修回訪");
        menus.put("强制回单", "強制回單");
        menus.put("疫情管控", "疫情管控");
        menus.put("疫情设置", "疫情設置");
        menus.put("返省上报", "返省上報");
        menus.put("疫情上报", "疫情上報");
        menus.put("合同管理", "合同管理");
        menus.put("合同类型", "合同類型");
        menus.put("合同甲方", "合同甲方");
        menus.put("起草合同", "起草合同");
        menus.put("合同查询", "合同查詢");
        menus.put("合同变更", "合同變更");
        menus.put("到期合同", "到期合同");
        menus.put("报表管理", "報表管理");
        menus.put("报表专家", "報表專家");
        menus.put("楼栋费用表", "樓棟費用表");
        menus.put("费用分项表", "費用分項表");
        menus.put("费用明细表", "費用明細表");
        menus.put("缴费清单", "繳費清單");
        menus.put("欠费明细表", "欠費明細表");
        menus.put("预缴费提醒", "預繳費提醒");
        menus.put("费用到期提醒", "費用到期提醒");
        menus.put("缴费明细表", "繳費明細表");
        menus.put("报修汇总表", "報修匯總表");
        menus.put("未收费房屋", "未收費房屋");
        menus.put("问卷明细表", "問卷明細表");
        menus.put("业主缴费明细", "業主繳費明細");
        menus.put("华宁物业报表", "華寧物業報表");
        menus.put("押金报表", "押金報表");
        menus.put("报修报表", "報修報表");
        menus.put("营业报表", "營業報表");
        menus.put("协同办公", "協同辦公");
        menus.put("流程实例", "流程實例");
        menus.put("我的流程", "我的流程");
        menus.put("工作办理", "工作辦理");
        menus.put("发布公告", "發佈公告");
        menus.put("电话投诉", "電話投訴");
        menus.put("访客登记", "訪客登記");
        menus.put("信息发布", "信息發布");
        menus.put("信息大类", "信息大類");
        menus.put("智慧考勤", "智慧考勤");
        menus.put("今日考勤", "今日考勤");
        menus.put("月考勤", "月考勤");
        menus.put("考勤记录", "考勤記錄");
        menus.put("问卷投票", "問卷投票");
        menus.put("我的问卷", "我的問卷");
        menus.put("活动规则", "活動規則");
        menus.put("采购管理", "採購管理");
        menus.put("仓库信息", "倉庫信息");
        menus.put("物品信息", "物品信息");
        menus.put("物品类型", "物品類型");
        menus.put("物品供应商", "物品供應商");
        menus.put("物品规格", "物品規格");
        menus.put("采购申请", "採購申請");
        menus.put("调拨记录", "調撥記錄");
        menus.put("出入库明细", "出入庫明細");
        menus.put("调拨明细", "調撥明細");
        menus.put("物品领用", "物品領用");
        menus.put("我的物品", "我的物品");
        menus.put("转赠记录", "轉贈記錄");
        menus.put("物品使用记录", "物品使用記錄");
        menus.put("巡检管理", "巡檢管理");
        menus.put("巡检项目", "巡檢項目");
        menus.put("巡检点", "巡檢點");
        menus.put("巡检路线", "巡檢路線");
        menus.put("巡检计划", "巡檢計劃");
        menus.put("巡检任务", "巡檢任務");
        menus.put("巡检明细", "巡檢明細");
        menus.put("设备管理", "設備管理");
        menus.put("设备信息", "設備信息");
        menus.put("设备类型", "設備類型");
        menus.put("设备数据同步", "設備數據同步");
        menus.put("开门记录", "開門記錄");
        menus.put("访客留影", "訪客留影");
        menus.put("申请钥匙", "申請鑰匙");
        menus.put("钥匙审核", "鑰匙審核");
        menus.put("员工门禁授权", "員工門禁授權");
        menus.put("组织管理", "組織管理");
        menus.put("组织信息", "組織信息");
        menus.put("员工信息", "員工信息");
        menus.put("员工认证", "員工認證");
        menus.put("系统管理", "系統管理");
        menus.put("权限组", "權限組");
        menus.put("员工权限", "員工權限");
        menus.put("小区配置", "小區配置");
        menus.put("流程管理", "流程管理");
        menus.put("修改密码", "修改密碼");
        menus.put("商户信息", "商戶信息");
        menus.put("公众号", "公眾號");
        menus.put("小程序配置", "小程序配置");
        menus.put("短信配置", "短信配置");
        menus.put("位置信息", "位置信息");
        menus.put("资产导入导出", "資產導入導出");
        menus.put("历史缴费导入", "歷史繳費導入");
        menus.put("打印配置", "打印配置");
        menus.put("收据模板", "收據模板");
        menus.put("基础数据", "基礎數據");
        menus.put("小区信息", "小區信息");
        menus.put("物业公司", "物業公司");
        menus.put("周边商家", "周邊商家");
        menus.put("入驻审核", "入駐審核");
        menus.put("租赁管理", "租賃管理");
        menus.put("租赁配置", "租賃配置");
        menus.put("房源信息", "房源信息");
        menus.put("出租审核", "出租審核");
        menus.put("租赁预约", "租賃預約");
        menus.put("出租历史", "出租歷史");
        menus.put("智慧运营", "智慧運營");
        menus.put("旧货甩卖", "舊貨甩賣");
        menus.put("发布需求", "發布需求");
        menus.put("制作优惠券", "製作優惠券");
        menus.put("购买记录", "購買記錄");
        menus.put("商家店铺", "商家店鋪");
        menus.put("商城菜单", "商城菜單");
        menus.put("开店审核", "開店審核");
        menus.put("店铺信息", "店鋪信息");
        menus.put("小区店铺", "小區店鋪");
        menus.put("专区目录", "專區目錄");
        menus.put("专区商品", "專區商品");
        menus.put("店铺类型", "店鋪類型");
        menus.put("上架服务", "上架服務");
        menus.put("上架商品", "上架商品");
        menus.put("店铺变更", "店鋪變更");
        menus.put("财务管理", "財務管理");
        menus.put("提现审核", "提現審核");
        menus.put("提现付款", "提現付款");
        menus.put("提现记录", "提現記錄");
        menus.put("设置保证金", "設置保證金");
        menus.put("保证金明细", "保證金明細");
        menus.put("商户管理", "商戶管理");
        menus.put("商户管理员", "商戶管理員");
        menus.put("营销中心", "營銷中心");
        menus.put("发布广告", "發布廣告");
        menus.put("便民菜单维护", "便民菜單維護");
        menus.put("订单中心", "訂單中心");
        menus.put("受理单", "受理單");
        menus.put("登录日志", "登錄日誌");
        menus.put("商城订单", "商城訂單");
        menus.put("退货订单", "退貨訂單");
        menus.put("服务管理", "服務管理");
        menus.put("应用信息", "應用信息");
        menus.put("服务信息", "服務信息");
        menus.put("服务注册", "服務註冊");
        menus.put("服务实现", "服務實現");
        menus.put("服务提供", "服務提供");
        menus.put("业务轨迹", "業務軌跡");
        menus.put("菜单管理", "菜單管理");
        menus.put("菜单目录", "菜單目錄");
        menus.put("菜单组", "菜單組");
        menus.put("菜单维护", "菜單維護");
        menus.put("映射管理", "映射管理");
        menus.put("编码映射", "編碼映射");
        menus.put("属性管理", "屬性管理");
        menus.put("属性", "屬性");
        menus.put("报表开发", "報表開發");
        menus.put("报表组", "報表組");
        menus.put("报表信息", "報表信息");
        menus.put("报表组件", "報表組件");
        menus.put("缓存管理", "緩存管理");
        menus.put("刷新缓存", "刷新緩存");
        menus.put("定时任务管理", "定時任務管理");
        menus.put("定时任务", "定時任務");
        menus.put("日志中心", "日誌中心");
        menus.put("交互日志", "交互日誌");
        menus.put("初始化小区", "初始化小區");

    }


}
