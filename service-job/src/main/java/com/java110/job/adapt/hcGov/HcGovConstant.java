/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.job.adapt.hcGov;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.AuthenticationFactory;

/**
 * HCGOV 政务常量类
 * 接口协议地址： https://gitee.com/java110/microCommunityInformation/tree/master/info-doc#1%E6%A5%BC%E6%A0%8B%E4%B8%8A%E4%BC%A0
 *
 * @desc add by 吴学文 9:49
 */
public class HcGovConstant {

    public static final int CODE_OK = 200; // 成功


    public static final String GOV_DOMAIN = "DOMAIN_GOV"; // 政务域
    public static final String COMMUNITY_SECURE = "978984641654"; // 小区密钥
    public static final String FLOOR_TYPE = "FLOOR_TYPE"; // 楼栋类型
    public static final String GOV_TOPIC = "hcGov"; // 楼栋类型
    public static final String GOV_SWITCH = "GOV_SWITCH"; // 推送政务开关


    //政务小区编码/楼栋外部便阿门/房屋外部编码/业主外部编码 SPEC_CD/商戶外部编码/员工外部编码
    public static final String EXT_COMMUNITY_ID = "9329000004";

    //添加楼栋
    public static final String ADD_FLOOR_ACTION = "ADD_FLOOR";
    //修改楼栋
    public static final String EDIT_FLOOR_ACTION = "EDIT_FLOOR";
    //添加房屋
    public static final String ADD_ROOM_ACTION = "ADD_ROOM";
    //修改房屋
    public static final String EDIT_ROOM_ACTION = "EDIT_ROOM";
    //添加业主
    public static final String ADD_OWNER_ACTION = "ADD_OWNER";
    //修改业主
    public static final String EDIT_OWNER_ACTION = "EDIT_OWNER";

    //添加员工
    public static final String ADD_STAFF_ACTION = "ADD_STAFF";
    //修改员工
    public static final String EDIT_STAFF_ACTION = "EDIT_STAFF";
    //添加商户company
    public static final String ADD_COMPANY_ACTION = "ADD_COMPANY";

    //添加位置
    public static final String ADD_LOCATION_ACTION = "ADD_LOCATION";
    //修改位置
    public static final String EDIT_LOCATION_ACTION = "EDIT_LOCATION";

    //添加停车场
    public static final String ADD_PARKING_AREA_ACTION = "ADD_PARKING_AREA";
    //修改停车场
    public static final String EDIT_PARKING_AREA_ACTION = "EDIT_PARKING_AREA";

    //添加停车场
    public static final String ADD_CAR_ACTION = "ADD_CAR";
    //修改停车场
    public static final String EDIT_CAR_ACTION = "EDIT_CAR";

    //开门记录
    public static final String ADD_INOUT_RECORD_ACTION = "ADD_INOUT_RECORD";

    public static void generatorProducerSign(JSONObject header, JSONObject body, String code) {

        String newSign = AuthenticationFactory.md5(header.getString("tranId") + header.getString("reqTime") + body.toJSONString() + code).toLowerCase();
        header.put("sign",newSign);
    }

    //商户属性编码
    public static final String STORE_ATTR_ARTIFICIALPERSON = "100201903001"; //企业法人
    public static final String STORE_ATTR_REGISTERTIME = "100201903003"; //成立日期
    public static final String STORE_ATTR_IDCARD = "100201903004"; //营业执照


}
