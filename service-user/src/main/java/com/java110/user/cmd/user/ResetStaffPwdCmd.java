package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.AbstractServiceCmdListener;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.store.StoreDto;
import com.java110.dto.store.StoreUserDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.store.IStoreUserV1InnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.user.UserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "user.resetStaffPwd")
public class ResetStaffPwdCmd extends AbstractServiceCmdListener {

    @Autowired
    private IStoreUserV1InnerServiceSMO storeUserV1InnerServiceSMOImpl;

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

        if (!reqJson.containsKey("staffId")) {
            reqJson.put("staffId", reqJson.getString("userId"));
        }

        Assert.jsonObjectHaveKey(reqJson, "staffId", "请求参数中未包含staffId 节点，请确认");

        //首先判断是否为 运营或者开发
        //查询store 信息
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(reqJson.getString("storeId"));
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

        Assert.listOnlyOne(storeDtos, "商户不存在");

        if (StoreDto.STORE_TYPE_ADMIN.equals(storeDtos.get(0).getStoreTypeCd()) || StoreDto.STORE_TYPE_DEV.equals(storeDtos.get(0).getStoreTypeCd())) {
            return;
        }

        //校验 staff 和 store 之间是否有 关系 防止 攻击
        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setUserId(reqJson.getString("staffId"));
        storeUserDto.setStoreId(reqJson.getString("storeId"));
        List<StoreUserDto> storeUserDtos = storeUserV1InnerServiceSMOImpl.queryStoreUsers(storeUserDto);

        Assert.listOnlyOne(storeUserDtos, "非法操作");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        modifyStaff(reqJson, cmdDataFlowContext);
        JSONObject paramOut = new JSONObject();
        paramOut.put("pwd", reqJson.getString("pwd"));
        ResponseEntity<String> responseEntity = new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);

    }


    private void modifyStaff(JSONObject paramObj, ICmdDataFlowContext dataFlowContext) {
        //校验json 格式中是否包含 name,email,levelCd,tel


        UserDto userDto = new UserDto();
        userDto.setStatusCd("0");
        userDto.setUserId(paramObj.getString("staffId"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUserHasPwd(userDto);

        Assert.listOnlyOne(userDtos, "数据错误查询到多条用户信息或单条");

        JSONObject userInfo = JSONObject.parseObject(JSONObject.toJSONString(userDtos.get(0)));
        String pwd = GenerateCodeFactory.getRandomCode(6);
        userInfo.putAll(paramObj);
        userInfo.put("password", AuthenticationFactory.passwdMd5(pwd));
        paramObj.put("pwd", pwd);


        UserPo userPo = BeanConvertUtil.covertBean(userInfo, UserPo.class);

        int flag = userV1InnerServiceSMOImpl.updateUser(userPo);
        if (flag < 1) {
            throw new CmdException("重置失败");
        }

    }
}
