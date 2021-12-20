package com.java110.dto.smallWechatAttr;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 微信属性数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class SmallWechatAttrDto extends PageDto implements Serializable {

    public static final String SPEC_CD_TOKEN = "33001";//token
    public static final String SPEC_CD_OWE_FEE_TEMPLATE = "33002";//欠费推送模板
    public static final String SPEC_CD_WECHAT_TEMPLATE = "33003";//欠费推送模板、装修跟踪记录通知
    public static final String SPEC_CD_WECHAT_SUCCESS_TEMPLATE = "33004";//业主缴费成功推送模板
    public static final String SPEC_CD_WECHAT_EXPIRE_TEMPLATE = "33005";//业主费用到期通知推送模板
    public static final String SPEC_CD_WECHAT_PROCESS_TEMPLATE = "33006";//空置房验房申请流程、审批流程通知模板（资产调拨审批、领用审批、转赠通知-资产管理待办审批通知）
    public static final String SPEC_CD_WECHAT_ROOM_STATE_TEMPLATE = "33007";//空置房验房状态（通过和不通过）、审批状态（通过和不通过）模板
    public static final String SPEC_CD_WECHAT_WORK_ORDER_REMIND_TEMPLATE = "33008";//报修工单提醒模板
    public static final String SPEC_CD_WECHAT_DISPATCH_REMIND_TEMPLATE = "33009";//报修工单派单和转单提醒给维修师傅
    public static final String SPEC_CD_WECHAT_SCHEDULE_TEMPLATE = "33010";//报修工单派单和抢单提醒给业主，安排师傅维修（进度提醒）
    public static final String SPEC_CD_WECHAT_WORK_ORDER_END_TEMPLATE = "33011";//报修工单维修完成提醒给业主
    public static final String SPEC_CD_WECHAT_HOUSE_DECORATION_APPLY_TEMPLATE = "33012";//装修申请提醒给业主
    public static final String SPEC_CD_WECHAT_HOUSE_DECORATION_CHECK_TEMPLATE = "33013";//装修审核/完成提醒给物业管理人员
    public static final String SPEC_CD_WECHAT_HOUSE_DECORATION_CHECK_RESULT_TEMPLATE = "33014";//装修审核结果提醒给业主
    public static final String SPEC_CD_WECHAT_HOUSE_DECORATION_COMPLETED_TEMPLATE = "33015";//装修验收结果提醒给业主
    public static final String SPEC_CD_WECHAT_REPAIR_CHARGE_SCENE_TEMPLATE = "33016";//报修通知-上门维修现场收费通知业主
    public static final String SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_TEMPLATE = "33017";//流程待审批通知
    public static final String SPEC_CD_WECHAT_OA_WORKFLOW_AUDIT_FINISH_TEMPLATE = "33018";//流程待审批完成通知

    private String attrId;
    private String wechatId;
    private String specCd;
    private String specCdName;
    private String communityId;
    private String value;


    private Date createTime;

    private String statusCd = "0";


    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getSpecCd() {
        return specCd;
    }

    public void setSpecCd(String specCd) {
        this.specCd = specCd;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getSpecCdName() {
        return specCdName;
    }

    public void setSpecCdName(String specCdName) {
        this.specCdName = specCdName;
    }
}
