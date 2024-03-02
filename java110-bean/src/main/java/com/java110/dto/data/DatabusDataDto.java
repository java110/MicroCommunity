package com.java110.dto.data;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @ClassName 自定义databus
 * @Description 业务数据同步数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class DatabusDataDto extends PageDto implements Serializable {

    // 车辆进出手动触发
    public static final String BUSINESS_TYPE_SAVE_CAR_INOUT_DETAIL = "saveCarInoutDetail";

    public static final String BUSINESS_TYPE_SAVE_PARKING_COUPON_CAR_ORDER = "saveParkingCouponCarOrder";

    // 欠费催缴
    public static final String BUSINESS_TYPE_OWE_FEE_CALLABLE = "oweFeeCallable";

    //todo 房屋批量创建费用
    public static final String BUSINESS_TYPE_ROOM_CREATE_PAY_FEE = "roomCreatePayFee";

    //todo 工作单通知适配器
    public static final String BUSINESS_TYPE_OA_WORK_TASK = "oaWorkTaskToStaff";

    // 欠费催缴
    public static final String BUSINESS_TYPE_SEND_COMMUNITY_DATA_TO_IOT = "sendCommunityDataToIot";


    public static final String BUSINESS_TYPE_SEND_COMPLAINT_NOTIFY_STAFF = "sendComplaintNotifyStaff"; // 发送投诉给员工


    private String businessTypeCd;

    private JSONObject data;

    public DatabusDataDto() {
    }

    public DatabusDataDto(String businessTypeCd, JSONObject data) {
        this.businessTypeCd = businessTypeCd;
        this.data = data;
    }

    public static DatabusDataDto getInstance(String businessTypeCd, JSONObject data) {
        return new DatabusDataDto(businessTypeCd, data);
    }


    public String getBusinessTypeCd() {
        return businessTypeCd;
    }

    public void setBusinessTypeCd(String businessTypeCd) {
        this.businessTypeCd = businessTypeCd;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
