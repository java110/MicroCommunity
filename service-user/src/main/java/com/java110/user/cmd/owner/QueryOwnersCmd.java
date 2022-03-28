package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.AbstractServiceCmdListener;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.ApiOwnerDataVo;
import com.java110.vo.api.ApiOwnerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "owner.queryOwners")
public class QueryOwnersCmd extends AbstractServiceCmdListener {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.jsonObjectHaveKey(reqJson, "ownerTypeCd", "请求中未包含ownerTypeCd信息");
        Assert.isInteger(reqJson.getString("page"), "不是有效数字");
        Assert.isInteger(reqJson.getString("row"), "不是有效数字");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
//根据房屋查询时 先用 房屋信息查询 业主ID
        freshRoomId(reqJson);

        if (reqJson.containsKey("name")) {
            queryByCondition(reqJson, cmdDataFlowContext);
            return;
        }

        int row = reqJson.getInteger("row");

        ApiOwnerVo apiOwnerVo = new ApiOwnerVo();

        //查询总记录数
        int total = ownerInnerServiceSMOImpl.queryOwnersCount(BeanConvertUtil.covertBean(reqJson, OwnerDto.class));
        apiOwnerVo.setTotal(total);
        List<OwnerDto> ownerDtos = new ArrayList<>();
        if (total > 0) {
            List<OwnerDto> ownerDtoList = ownerInnerServiceSMOImpl.queryOwners(BeanConvertUtil.covertBean(reqJson, OwnerDto.class));
            List<Map> mark = getPrivilegeOwnerList("/roomCreateFee", reqJson.getString("userId"));
            for (OwnerDto ownerDto : ownerDtoList) {
                //对业主身份证号隐藏处理
                String idCard = ownerDto.getIdCard();
                if (mark.size() == 0 && idCard != null && !idCard.equals("") && idCard.length() > 16) {
                    idCard = idCard.substring(0, 6) + "**********" + idCard.substring(16);
                    ownerDto.setIdCard(idCard);
                }
                //对业主手机号隐藏处理
                String link = ownerDto.getLink();
                if (mark.size() == 0 && link != null && !link.equals("") && link.length() == 11) {
                    link = link.substring(0, 3) + "****" + link.substring(7);
                    ownerDto.setLink(link);
                }
                ownerDtos.add(ownerDto);
            }
            apiOwnerVo.setOwners(BeanConvertUtil.covertBeanList(ownerDtos, ApiOwnerDataVo.class));
        }

        apiOwnerVo.setRecords((int) Math.ceil((double) total / (double) row));


        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOwnerVo), HttpStatus.OK);
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

        String[] params = roomName.split("-");
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
     * 根据条件查询
     *
     * @param reqJson         查询信息
     * @param cmdDataFlowContext 上下文
     */
    private void queryByCondition(JSONObject reqJson, ICmdDataFlowContext cmdDataFlowContext) {
        //获取当前用户id
        String userId = reqJson.getString("userId");
        int row = reqJson.getInteger("row");
        ApiOwnerVo apiOwnerVo = new ApiOwnerVo();
        int total = ownerInnerServiceSMOImpl.queryOwnerCountByCondition(BeanConvertUtil.covertBean(reqJson, OwnerDto.class));
        apiOwnerVo.setTotal(total);
        List<OwnerDto> ownerDtos = new ArrayList<>();
        if (total > 0) {
            List<OwnerDto> ownerDtoList = ownerInnerServiceSMOImpl.queryOwnersByCondition(BeanConvertUtil.covertBean(reqJson, OwnerDto.class));
            List<Map> mark = getPrivilegeOwnerList("/roomCreateFee", userId);
            for (OwnerDto ownerDto : ownerDtoList) {
                //对业主身份证号隐藏处理
                String idCard = ownerDto.getIdCard();
                if (mark.size() == 0 && !StringUtil.isEmpty(idCard) && idCard.length() > 16) {
                    idCard = idCard.substring(0, 6) + "**********" + idCard.substring(16);
                    ownerDto.setIdCard(idCard);
                }
                //对业主手机号隐藏处理
                String link = ownerDto.getLink();
                if (mark.size() == 0 && !StringUtil.isEmpty(link) && link.length() == 11) {
                    link = link.substring(0, 3) + "****" + link.substring(7);
                    ownerDto.setLink(link);
                }
                ownerDtos.add(ownerDto);
            }
            apiOwnerVo.setOwners(BeanConvertUtil.covertBeanList(ownerDtos, ApiOwnerDataVo.class));
        }

        apiOwnerVo.setRecords((int) Math.ceil((double) total / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOwnerVo), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
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
