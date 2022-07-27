package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.dto.store.StoreUserDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.intf.store.IStoreUserV1InnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.org.OrgStaffRelPo;
import com.java110.po.store.StoreUserPo;
import com.java110.po.user.UserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "user.staff.modify")
public class UserStaffModifyCmd extends Cmd {
    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelInnerServiceSMO orgStaffRelInnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelV1InnerServiceSMO orgStaffRelV1InnerServiceSMOImpl;

    @Autowired
    private IStoreUserV1InnerServiceSMO  storeUserV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "staffId", "请求参数中未包含员工 节点，请确认");
        //校验json 格式中是否包含 name,email,levelCd,tel
        Assert.jsonObjectHaveKey(reqJson, "name", "请求参数中未包含name 节点，请确认");
        Assert.jsonObjectHaveKey(reqJson, "tel", "请求参数中未包含tel 节点，请确认");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        if (reqJson.containsKey("photo") && !StringUtils.isEmpty(reqJson.getString("photo"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("photo"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(reqJson.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);

            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setRelTypeCd("12000");
            fileRelDto.setObjId(reqJson.getString("userId"));
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (fileRelDtos == null || fileRelDtos.size() == 0) {
                JSONObject businessUnit = new JSONObject();
                businessUnit.put("fileRelId", "-1");
                businessUnit.put("relTypeCd", "12000");
                businessUnit.put("saveWay", "table");
                businessUnit.put("objId", reqJson.getString("userId"));
                businessUnit.put("fileRealName", fileDto.getFileId());
                businessUnit.put("fileSaveName", fileName);
                FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
                int flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
                if (flag < 1) {
                    throw new CmdException("保存图片异常");
                }
            } else {
                JSONObject businessUnit = new JSONObject();
                businessUnit.putAll(BeanConvertUtil.beanCovertMap(fileRelDtos.get(0)));
                businessUnit.put("fileRealName", fileDto.getFileId());
                businessUnit.put("fileSaveName", fileName);
                FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
                int flag = fileRelInnerServiceSMOImpl.updateFileRel(fileRelPo);
                if (flag < 1) {
                    throw new CmdException("保存图片异常");
                }
            }
        }
        modifyStaff(reqJson);

    }

    private void modifyStaff(JSONObject paramObj) {
        UserPo userPo = BeanConvertUtil.covertBean(paramObj, UserPo.class);
        //根据手机号查询用户
        UserDto userDto = new UserDto();
        userDto.setTel(userPo.getTel());
        userDto.setUserFlag("1");
        List<UserDto> users = userInnerServiceSMOImpl.getUsers(userDto);
        if (users != null && users.size() > 0) {
            for (UserDto user : users) {
                if (!user.getUserId().equals(userPo.getUserId())) {
                    throw new IllegalArgumentException("员工手机号不能重复，请重新输入");
                }
            }
        }
        int flag = userV1InnerServiceSMOImpl.updateUser(userPo);

        if (flag < 1) {
            throw new CmdException("保存用户异常");
        }
        OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
        orgStaffRelDto.setStaffId(userPo.getUserId());
        List<OrgStaffRelDto> orgStaffRelDtoList = orgStaffRelInnerServiceSMOImpl.queryOrgInfoByStaffIds(orgStaffRelDto);

        if (orgStaffRelDtoList == null || orgStaffRelDtoList.size() < 1) {
            return;
        }
        OrgStaffRelPo orgStaffRelPo = new OrgStaffRelPo();
        orgStaffRelPo.setRelCd(paramObj.getString("relCd"));
        orgStaffRelPo.setRelId(orgStaffRelDtoList.get(0).getRelId());
        orgStaffRelPo.setOrgId(paramObj.getString("orgId"));

        flag = orgStaffRelV1InnerServiceSMOImpl.updateOrgStaffRel(orgStaffRelPo);
        if (flag < 1) {
            throw new CmdException("保存员工 失败");
        }

        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setUserId(userPo.getUserId());
        List<StoreUserDto> storeUserDtos = storeUserV1InnerServiceSMOImpl.queryStoreUsers(storeUserDto);

        if (storeUserDtos == null || storeUserDtos.size() < 1) {
            return;
        }
        StoreUserPo storeUserPo = new StoreUserPo();
        storeUserPo.setRelCd(paramObj.getString("relCd"));
        storeUserPo.setStoreUserId(storeUserDtos.get(0).getStoreUserId());

        flag = storeUserV1InnerServiceSMOImpl.updateStoreUser(storeUserPo);
        if (flag < 1) {
            throw new CmdException("保存员工 失败");
        }
    }


}
