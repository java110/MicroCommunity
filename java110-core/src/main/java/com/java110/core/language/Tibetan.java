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
 * 藏语
 */
@Component
public class Tibetan extends DefaultLanguage {
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
        for (int priIndex = 0;priIndex < tmpPrivilegeArrays.size(); priIndex++) {
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

    public String getLangMsg(String msg){
        String msgStr = msgs.get(msg);
        if (!StringUtil.isEmpty(msgStr)) {
            return msgStr;
        }

        return msg;
    }
    static {
        menuCatalogs.put("设备", "སྒྲིག་ཆས།");
        menuCatalogs.put("首页", "ཁྱིམ།");
        menuCatalogs.put("业务受理", "བདག་སྤྲོད།");
        menuCatalogs.put("费用报表", "སྙན་ཐོའི་རེའུ་མིག།");
        menuCatalogs.put("物业服务", "ཞབས་ཞུ་འི།");
        menuCatalogs.put("设备停车", "རླངས་འཁོར།");
        menuCatalogs.put("常用菜单", "ཡོ་བྱད།");
        menuCatalogs.put("设置", "བཀོད་སྒྲིག།");

        menus.put("小区管理", "སྡོད་ཁུལ།");
        menus.put("我的小区", "ང་འི་སྡོད་ཁུལ།");
        menus.put("小区大屏", "གཞིས་ཁུལ་ཆེན་པོ་འི་མཐའ་སྐོར།");
        menus.put("业务受理", "ལས་སྒོ་བདག་སྤྲོད།");
        menus.put("房屋装修", "ཁང་བ་སྒྲིག་སྦྱོར།");
        menus.put("结构图", "གྲུབ་ཚུལ།");
        menus.put("车位结构图", "རླངས་འཁོར་འཇོག་ས་འི་གྲུབ་ཚུལ།");
        menus.put("产权登记", "བདག་དབང་ཐོ་འགོད།");
        menus.put("视频监控", "བརྙན་འཕྲིན་དང་ཚོད་འཛིན།");
        menus.put("资产管理", "རྒྱུ་ནོར།");
        menus.put("楼栋信息", "ཐོག་ཁང་ཆ་འཕྲིན།");
        menus.put("单元信息", "སྡེ་ཚན་གྱི་ཆ་འཕྲིན།");
        menus.put("房屋管理", "ཁང་ཁྱིམ་དོ་དམ།");
        menus.put("商铺管理", "ཚོང་ཁང་དོ་དམ།");
        menus.put("业主管理", "བདག་པོ།");
        menus.put("业主信息", "བདག་པོ་འི་ཆ་འཕྲིན།");
        menus.put("业主成员", "བདག་པོ་འི་ཁོངས་མི།");
        menus.put("绑定业主", "གནམ་བདག་པོ།");
        menus.put("业主账号", "བདག་པོ་རྩིས་ཐོའི་ཨང་གྲངས།");
        menus.put("费用管理", "འགྲོ་གྲོན།");
        menus.put("费用项设置", "གྲོན་དངུལ་རྣམ་གྲངས་བཀོད་སྒྲིག།");
        menus.put("房屋收费", "ཁང་བའི་གླ་རིན།");
        menus.put("车辆收费", "འཁོར་ལོ་འི་རིན་བསྡུ་འི།");
        menus.put("合同收费", "གན་རྒྱ་འི་རིན་བསྡུ་འི།");
        menus.put("账单催缴", "བསྡུ།");
        menus.put("费用导入", "དབུགས་འདྲེན་པའི་འགྲོ་གྲོན།");
        menus.put("员工收费", "ལས་བཟོ་པའི་རིན་བསྡུ་འི།");
        menus.put("费用汇总表", "འགྲོ་གྲོན་ཕྱོགས་བསྡུས་རེའུ་མིག།");
        menus.put("退费审核", "ཞིང་གྲོན་དངུལ་ཞིབ་བཤེར།");
        menus.put("欠费信息", "ཆད་དངུལ་ཆ་འཕྲིན།");
        menus.put("抄表类型", "བཤུ་རེའུ་མིག་རིགས་དབྱིབས།");
        menus.put("水电抄表", "ཆུ་གློག་བཤུ་རེའུ་མིག།");
        menus.put("补打收据", "ཁ་གསབ་རྒྱག་པ་འབྱོར་ཐོ།");
        menus.put("公摊公式", "ཆ་གསེས་སྤྱི་འགྲོས།");
        menus.put("缴费审核", "གྲོན་དངུལ་སྤྲོད་པ་ཞིབ་བཤེར།");
        menus.put("折扣设置", "ཕབ་ཆ་བཀོད་སྒྲིག།");
        menus.put("优惠申请", "གཟིགས་སྐྱོང་བྱེད་པའི་རེ་ཞུ།");
        menus.put("优惠类型", "གཟིགས་སྐྱོང་གི་རིགས་དབྱིབས།");
        menus.put("临时车收费", "གནས་སྐབས་ཀྱི་རླངས་འཁོར་རིན་བསྡུ་འི།");
        menus.put("临时车缴费清单", "གནས་སྐབས་ཀྱི་རླངས་འཁོར་གྱི་འཇལ་གྲོན་རྩིས་ཁྲ།");
        menus.put("取消费用", "ཕྱིར་འཐེན་བྱེད་པའི་འགྲོ་གྲོན།");
        menus.put("智慧停车", "རླངས་འཁོར།");
        menus.put("停车场管理", "རླངས་འཁོར་འཇོག་ས་དོ་དམ།");
        menus.put("车位信息", "རླངས་འཁོར་འཇོག་ས་སྒྲིག་འཇོག་བྱ་ཆ་འཕྲིན།");
        menus.put("岗亭管理", "སོ་སྲུང་ཁང་བུ་དོ་དམ།");
        menus.put("业主车辆", "བདག་པོ་འཁོར་ལོ།");
        menus.put("进场记录", "ཉོ་དགོས་ཟིན་ཐོ།");
        menus.put("在场车辆", "དེ་ག་ར་རླངས་འཁོར།");
        menus.put("黑白名单", "དཀར་ནག་མིང་ཐོ།");
        menus.put("剩余车位", "དེ་ལས་ལྷག་པའི་རླངས་འཁོར་འཇོག་ས་སྒྲིག་འཇོག་བྱ།");
        menus.put("车位申请", "རླངས་འཁོར་འཇོག་ས་སྒྲིག་འཇོག་བྱ་རེ་ཞུ།");
        menus.put("报修管理", "སྐྱ་ར།");
        menus.put("报修设置", "སྐྱ་ར་བཀོད་སྒྲིག།");
        menus.put("电话报修", "ཁ་པར་ཉམས་གསོ།");
        menus.put("工单池", "ལས་གཅིག་པུ་གསང།");
        menus.put("报修待办", "རེ་སྒུག་ཉམས་གསོ།");
        menus.put("报修已办", "སྐྱ་མཇུག་རྫོགས།");
        menus.put("报修回访", "སྐྱ་ར་ལན་འདེབས།");
        menus.put("强制回单", "སྐྱ་ར་བཙན་ཤེད་ཀྱིས་ཕྱིར།");
        menus.put("疫情管控", "ནད་ཡམས།");
        menus.put("疫情设置", "ནད་ཡམས་བཀོད་སྒྲིག།");
        menus.put("返省上报", "ཡ་ཐོག་ཞིང་ཆེན་ཞུ།");
        menus.put("疫情上报", "ནད་ཡམས་ཞུ།");
        menus.put("合同管理", "གན་རྒྱ།");
        menus.put("合同类型", "གན་རྒྱ་འི་དབྱིབས།");
        menus.put("合同甲方", "གན་རྒྱ་Aཕྱོགས་ཀྱིས། ");
        menus.put("起草合同", "ཟིན་བྲིས་གན་རྒྱ།");
        menus.put("合同查询", "གན་རྒྱ་འདྲི་རྩད།");
        menus.put("合同变更", "གན་རྒྱ་བསྒྱུར་བཅོས།");
        menus.put("到期合同", "དུས་བཅད་ལོན་པའི་གན་རྒྱ།");
        menus.put("报表管理", "སྙན་ཐོའི་རེའུ་མིག།");
        menus.put("报表专家", "སྙན་ཐོའི་རེའུ་མིག་ཆེད་ལས་མཁས་པ།");
        menus.put("楼栋费用表", "ཐོག་ཁང་འགྲོ་གྲོན་རེའུ་མིག།");
        menus.put("费用分项表", "གྲོན་དངུལ་སྐར་མ་རེའུ་མིག།");
        menus.put("费用明细表", "འགྲོ་གྲོན་ཞིབ་ཕྲ་རེའུ་མིག།");
        menus.put("缴费清单", "གྲོན་དངུལ་སྤྲོད་པའི་རྩིས་ཁྲ།");
        menus.put("欠费明细表", "ཆད་དངུལ་ཞིབ་ཕྲ་རེའུ་མིག།");
        menus.put("预缴费提醒", "སྔོན་གྱི་འཇལ་གྲོན་དྲན་སྐུལ།");
        menus.put("费用到期提醒", "འགྲོ་གྲོན་དུས་བཀག་གཙང་བའི་དྲན་སྐུལ།");
        menus.put("缴费明细表", "གྲོན་དངུལ་སྤྲོད་པར་ཞིབ་ཕྲ་འི་རེའུ་མིག།");
        menus.put("报修汇总表", "སྐྱ་ཕྱོགས་བསྡུས་རེའུ་མིག།");
        menus.put("未收费房屋", "མ་རིན་བསྡུ་འི་ཁང་བ།");
        menus.put("问卷明细表", "དྲི་ཤོག་གི་ཞིབ་ཕྲའི་རེའུ་མིག");
        menus.put("业主缴费明细", "གྲོན་དངུལ་སྤྲོད་པ།");
        menus.put("华宁物业报表", "ཧྭ་ཉིན་དངོས་ལས་རེའུ་མིག");
        menus.put("押金报表", "གཏའ་དངུལ་རེའུ་མིག");
        menus.put("报修报表", "ཞིག་གསོའི་རེའུ་མིག");
        menus.put("营业报表", "ཚོང་གཉེར་རེའུ་མིག");
        menus.put("协同办公", "མཉམ་སྒྲུབ་གཞུང་སྒྲུབ།");
        menus.put("流程实例", "བརྒྱུད་རིམ་དངོས་དཔེ།");
        menus.put("我的流程", "ངའི་བརྒྱུད་རིམ།");
        menus.put("工作办理", "ལས་ཀ་སྒྲུབ་གཉེར།");
        menus.put("发布公告", "སྤྱི་བསྒྲགས་སྤེལ།");
        menus.put("电话投诉", "ཁ་པར་གྱིས་ཞུ་གཏུག");
        menus.put("访客登记", "མགྲོན་པོའི་མིང་ཐོ་འགོད་པ།");
        menus.put("信息发布", "ཆ་འཕྲིན་ཁྱབ་བསྒྲགས།");
        menus.put("信息大类", "ཆ་འཕྲིན་གྱི་རིགས་ཆེན་པོ།");
        menus.put("智慧考勤", "བློ་གྲོས་ཞིབ་བཤེར།");
        menus.put("今日考勤", "དེ་རིང་གི་རྒྱུགས་ལེན།");
        menus.put("月考勤", "ཟླ་དེབ།");
        menus.put("考勤记录", "ལས་བརྩོན་ཟིན་ཐོ།");
        menus.put("问卷投票", "དྲི་ཤོག་འཕེན་པ།");
        menus.put("我的问卷", "ངའི་དྲི་ཤོག་");
        menus.put("活动规则", "བྱེད་སྒོའི་སྒྲིག་སྲོལ།");
        menus.put("采购管理", "ཉོ་སྒྲུབ།");
        menus.put("仓库信息", "མཛོད་ཁང་གི་བརྡ་འཕྲིན།");
        menus.put("物品信息", "དངོས་རྫས་བརྡ་འཕྲིན།");
        menus.put("物品类型", "དངོས་རྫས་ཀྱི་རིགས།");
        menus.put("物品供应商", "མཁོ་འདོན་ཚོང་པ།");
        menus.put("物品规格", "དངོས་རྫས་ཀྱི་ཚད་གཞི།");
        menus.put("采购申请", "ཉོ་སྒྲུབ་རེ་ཞུ།");
        menus.put("调拨记录", "གཏོང་ལེན་ཟིན་ཐོ།");
        menus.put("出入库明细", "མཛོད་ནས་དོན་སྐབས་ཞིབ་ཚགས་པ།");
        menus.put("调拨明细", "གཏོང་འགྲེམ།");
        menus.put("物品领用", "དངོས་རྫས་ལེན་པ།");
        menus.put("我的物品", "ངའི་དངོས་པོ།");
        menus.put("转赠记录", "ཞལ་འདེབས་འབུལ་བའི་ཟིན་ཐོ།");
        menus.put("物品使用记录", "བཀོལ་སྤྱོད་ཟིན་ཐོ།");
        menus.put("巡检管理", "སྐོར་ཞིབ་དོ་དམ།");
        menus.put("巡检项目", "སྐོར་ཞིབ་རྣམ་གྲངས།");
        menus.put("巡检点", "སྐོར་ཞིབ་ས་ཚིགས།");
        menus.put("巡检路线", "སྐོར་ཞིབ་ལམ་ཐིག");
        menus.put("巡检计划", "སྐོར་ཞིབ་འཆར་གཞི།");
        menus.put("巡检任务", "སྐོར་ཞིབ་ལས་འགན།");
        menus.put("巡检明细", "སྐོར་ཞིབ་ཞིབ་གསལ།");
        menus.put("设备管理", "སྒྲིག་ཆས།");
        menus.put("设备信息", "སྒྲིག་ཆས་བརྡ་འཕྲིན།");
        menus.put("设备类型", "སྒྲིག་ཆས་ཀྱི་རྣམ་གྲངས།");
        menus.put("设备数据同步", "སྒྲིག་ཆས་ཀྱི་གཞི་གྲངས་གོམ་མཉམ།");
        menus.put("开门记录", "སྒོ་ཕྱེ་བའི་ཟིན་ཐོ།");
        menus.put("访客留影", "མགྲོན་པོའི་གཟུགས་བརྙན་འཇོག་པ།");
        menus.put("申请钥匙", "རེ་ཞུའི་ལྡེ་མིག");
        menus.put("钥匙审核", "ལྡེ་མིག་ཞིབ་བཤེར།");
        menus.put("员工门禁授权", "ལས་བཟོ་བས་བཀག་སྡོམ་དབང་སྤྲོད།");
        menus.put("组织管理", "རྩ་འཛུགས།");
        menus.put("组织信息", "རྩ་འཛུགས།བརྡ་འཕྲིན།");
        menus.put("员工信息", "ལས་མིའི་བརྡ་འཕྲིན།");
        menus.put("员工认证", "ལས་བཟོ་བས་བདེན་དཔང་ར་སྤྲོད་བྱས།");
        menus.put("系统管理", "རྒྱུད་ཁོངས།");
        menus.put("权限组", "དབང་ཚད།");
        menus.put("员工权限", "ལས་མིའི་དབང་ཚད།");
        menus.put("小区配置", "གཞིས་ཁུལ་བཀོད་སྒྲིག");
        menus.put("流程管理", "བརྒྱུད་རིམ་དོ་དམ།");
        menus.put("修改密码", "གསང་གྲངས།");
        menus.put("商户信息", "ཚོང་བའི་བརྡ་འཕྲིན།");
        menus.put("公众号", "སྤྱི་རྟགས།");
        menus.put("小程序配置", "བྱ་རིམ་ཆུང་བ།");
        menus.put("短信配置", "བརྡ་འཕྲིན།");
        menus.put("位置信息", "གནས་སའི་བརྡ་འཕྲིན།");
        menus.put("资产导入导出", "རྒྱུ་ནོར་འདྲེན་གཏོང་།");
        menus.put("历史缴费导入", "ལོ་རྒྱུས་ཀྱི་གྲོན་དངུལ་སྤྲོད་པ།");
        menus.put("打印配置", "པར་འདེབས་བཀོད་སྒྲིག");
        menus.put("收据模板", "བྱུང་འཛིན་གྱི་འདྲ་དཔེ།");
    }
}
