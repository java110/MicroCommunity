package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.room.RoomDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.user.bmo.owner.IQueryOwnerStatisticsBMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.ApiOwnerDataVo;
import com.java110.vo.api.ApiOwnerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Java110CmdDoc(title = "查询业主",
        description = "第三方系统，比如招商系统查询业主信息",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/owner.queryOwners",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "owner.queryOwners",
        seq = 12
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "page", type = "int", length = 11, remark = "页数"),
        @Java110ParamDoc(name = "row", type = "int", length = 11, remark = "行数"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "name", length = 64, remark = "业主名称"),
        @Java110ParamDoc(name = "link", length = 11, remark = "业主手机号"),
        @Java110ParamDoc(name = "idCard", length = 30, remark = "业主身份证号"),
        @Java110ParamDoc(name = "ownerTypeCd", length = 12, remark = "业主类型 1001 业主 2002 家庭成员 家庭成员 需要传业主的ownerId"),
        @Java110ParamDoc(name = "ownerId", length = 30, remark = "业主ID，家庭成员业主ID"),
        @Java110ParamDoc(name = "memberId", length = 30, remark = "业主ID，主键ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "owners", type = "Array", length = -1, defaultValue = "成功", remark = "数据"),
                @Java110ParamDoc(parentNodeName = "owners", name = "communityId", length = 30, remark = "小区ID"),
                @Java110ParamDoc(parentNodeName = "owners", name = "name", length = 64, remark = "业主名称"),
                @Java110ParamDoc(parentNodeName = "owners", name = "link", length = 11, remark = "业主手机号"),
                @Java110ParamDoc(parentNodeName = "owners", name = "idCard", length = 30, remark = "业主身份证号"),
                @Java110ParamDoc(parentNodeName = "owners", name = "address", length = 512, remark = "地址"),
                @Java110ParamDoc(parentNodeName = "owners", name = "sex", length = 12, remark = "性别 男 1 女 0"),
                @Java110ParamDoc(parentNodeName = "owners", name = "ownerTypeCd", length = 12, remark = "业主类型 1001 业主 2002 家庭成员 家庭成员 需要传业主的ownerId"),
                @Java110ParamDoc(parentNodeName = "owners", name = "remark", length = 512, remark = "备注"),
                @Java110ParamDoc(parentNodeName = "owners", name = "memberId", length = 30, remark = "业主ID"),
                @Java110ParamDoc(parentNodeName = "owners", name = "ownerPhoto", length = -1, remark = "业主人脸 用于同步门禁 人脸开门"),
        }
)

@Java110ExampleDoc(
        reqBody = "http://ip:port/app/owner.queryOwners?ownerTypeCd=1001&page=1&row=10&communityId=2022121921870161",
        resBody = "{\n" +
                "\t\"owners\": [{\n" +
                "\t\t\"address\": \"张三\",\n" +
                "\t\t\"idCard\": \"\",\n" +
                "\t\t\"link\": \"18909718888\",\n" +
                "\t\t\"memberId\": \"772023012589770046\",\n" +
                "\t\t\"name\": \"王王\",\n" +
                "\t\t\"ownerId\": \"772023012589770046\",\n" +
                "\t\t\"ownerTypeCd\": \"1001\",\n" +
                "\t\t\"remark\": \"\",\n" +
                "\t\t\"sex\": \"0\",\n" +
                "\t\t\"userName\": \"人和物业\"\n" +
                "\t}],\n" +
                "\t\"page\": 0,\n" +
                "\t\"records\": 1,\n" +
                "\t\"rows\": 0,\n" +
                "\t\"total\": 1\n" +
                "}"
)
@Java110Cmd(serviceCode = "owner.queryOwners")
public class QueryOwnersCmd extends Cmd {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IQueryOwnerStatisticsBMO queryOwnerStatisticsBMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        super.validateProperty(cmdDataFlowContext);
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.isInteger(reqJson.getString("page"), "不是有效数字");
        Assert.isInteger(reqJson.getString("row"), "不是有效数字");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);

        //todo 根据房屋查询时 先用 房屋信息查询 业主ID
        freshRoomId(reqJson);
        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);

        int row = reqJson.getInteger("row");
        //查询总记录数
        int total = ownerInnerServiceSMOImpl.queryOwnersCount(ownerDto);
        List<OwnerDto> ownerDtos = null;
        if (total > 0) {
            ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
            // 查询统计数据
            ownerDtos = queryOwnerStatisticsBMOImpl.query(ownerDtos);
            List<Map> mark = getPrivilegeOwnerList("/roomCreateFee", userId);
            for (OwnerDto tmpOwnerDto : ownerDtos) {
                //对业主身份证号隐藏处理
                String idCard = tmpOwnerDto.getIdCard();
                if (mark.size() == 0 && idCard != null && !idCard.equals("") && idCard.length() > 16) {
                    idCard = idCard.substring(0, 6) + "**********" + idCard.substring(16);
                    tmpOwnerDto.setIdCard(idCard);
                }
                //对业主手机号隐藏处理
                String link = tmpOwnerDto.getLink();
                if (mark.size() == 0 && link != null && !link.equals("") && link.length() == 11) {
                    link = link.substring(0, 3) + "****" + link.substring(7);
                    tmpOwnerDto.setLink(link);
                }
            }
        } else {
            ownerDtos = new ArrayList<>();
        }
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, ownerDtos);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void freshRoomId(JSONObject reqJson) {
        if (!reqJson.containsKey("roomName")) {
            return;
        }
        String roomName = reqJson.getString("roomName");
        if (StringUtil.isEmpty(roomName)) {
            return;
        }
        if (!roomName.contains("-")) {
            throw new IllegalArgumentException("房屋格式错误,请写入如 楼栋-单元-房屋 格式");
        }
        String[] params = roomName.split("-", 3);
        if (params.length != 3) {
            throw new IllegalArgumentException("房屋格式错误,请写入如 楼栋-单元-房屋 格式");
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setFloorNum(params[0]);
        roomDto.setUnitNum(params[1]);
        roomDto.setRoomNum(params[2]);
        roomDto.setCommunityId(reqJson.getString("communityId"));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        Assert.listOnlyOne(roomDtos, "未查询到房屋下业主信息");
        reqJson.put("roomId", roomDtos.get(0).getRoomId());
    }


    /**
     * 脱敏处理
     *
     * @return
     */
    public List<Map> getPrivilegeOwnerList(String resource, String userId) {
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource(resource);
        basePrivilegeDto.setUserId(userId);
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        return privileges;
    }
}
