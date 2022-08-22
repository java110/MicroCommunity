package com.java110.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IRepairPoolV1InnerServiceSMO;
import com.java110.intf.community.IRepairUserV1InnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.INotepadV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.notepad.NotepadPo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "ownerRepair.saveOwnerRepair")
public class SaveOwnerRepairCmd extends Cmd {

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private INotepadV1InnerServiceSMO notepadV1InnerServiceSMOImpl;

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键(报修业主未处理费用条数)
    public static final String REPAIR_FEE_NUMBER = "REPAIR_FEE_NUMBER";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "repairType", "必填，请选择报修类型");
        Assert.hasKeyAndValue(reqJson, "repairName", "必填，请填写报修人名称");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写报修人手机号");
        Assert.hasKeyAndValue(reqJson, "repairObjType", "必填，请填写报修对象类型");
        Assert.hasKeyAndValue(reqJson, "repairObjId", "必填，请填写报修对象ID");
        Assert.hasKeyAndValue(reqJson, "repairObjName", "必填，请填写报修对象名称");
        Assert.hasKeyAndValue(reqJson, "appointmentTime", "必填，请填写预约时间");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写报修内容");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");

        String userId = context.getReqHeaders().get("user-id");
        Assert.hasLength(userId, "请填写提交用户ID");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setPage(1);
        userDto.setRow(1);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "未查询到用户");
        reqJson.put("userId", userDtos.get(0).getUserId());
        reqJson.put("userName", userDtos.get(0).getName());

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        //获取当前小区id
        String communityId = reqJson.getString("communityId");
        //查询默认费用项
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(communityId);
        feeConfigDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_REPAIR);
        feeConfigDto.setIsDefault(FeeConfigDto.DEFAULT_FEE_CONFIG);
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        if (feeConfigDtos.size() != 1) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "默认维修费用有多条或不存在！");
            context.setResponseEntity(responseEntity);
            return;
        }
        FeeDto feeDto = new FeeDto();
        feeDto.setConfigId(feeConfigDtos.get(0).getConfigId());
        feeDto.setPayerObjId(reqJson.getString("repairObjId"));
        feeDto.setState(FeeDto.STATE_DOING);
        //查询报修业主处理中的报修费
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        //取出开关映射的值(维修师傅未处理最大单数)
        String repairFeeNumber = MappingCache.getValue(DOMAIN_COMMON, REPAIR_FEE_NUMBER);
        if (feeDtos != null && StringUtil.isInteger(repairFeeNumber) && feeDtos.size() >= Integer.parseInt(repairFeeNumber)) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "该房屋存在" + Integer.parseInt(repairFeeNumber) + "条未处理的费用，请缴费后再进行报修！");
            context.setResponseEntity(responseEntity);
            return;
        }
        JSONObject businessOwnerRepair = new JSONObject();
        businessOwnerRepair.putAll(reqJson);
        businessOwnerRepair.put("repairId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_repairId));
        businessOwnerRepair.put("state", RepairDto.STATE_WAIT);
        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(businessOwnerRepair, RepairPoolPo.class);
        int flag = repairPoolV1InnerServiceSMOImpl.saveRepairPoolNew(repairPoolPo);
        if (flag < 1) {
            throw new CmdException("修改失败");
        }
        RepairUserPo repairUserPo = BeanConvertUtil.covertBean(reqJson, RepairUserPo.class);
        repairUserPo.setContext("订单提交");
        repairUserPo.setPreStaffId("-1");
        repairUserPo.setPreStaffName("-1");
        repairUserPo.setPreRuId("-1");
        repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_START_USER);
        repairUserPo.setStaffId(reqJson.getString("userId"));
        repairUserPo.setStaffName(reqJson.getString("userName"));
        repairUserPo.setRepairId(businessOwnerRepair.getString("repairId"));
        repairUserPo.setState(RepairUserDto.STATE_SUBMIT);
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
        flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
        if (flag < 1) {
            throw new CmdException("修改用户失败");
        }
        if (reqJson.containsKey("photos") && !StringUtils.isEmpty(reqJson.getString("photos"))) {
            JSONArray photos = reqJson.getJSONArray("photos");
            for (int _photoIndex = 0; _photoIndex < photos.size(); _photoIndex++) {
                FileDto fileDto = new FileDto();
                fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                fileDto.setFileName(fileDto.getFileId());
                fileDto.setContext(photos.getJSONObject(_photoIndex).getString("photo"));
                fileDto.setSuffix("jpeg");
                fileDto.setCommunityId(reqJson.getString("communityId"));
                String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
                reqJson.put("ownerPhotoId", fileDto.getFileId());
                reqJson.put("fileSaveName", fileName);
                JSONObject businessUnit = new JSONObject();
                businessUnit.put("fileRelId", "-" + (_photoIndex + 1));
                businessUnit.put("relTypeCd", FileRelDto.REL_TYPE_CD_REPAIR);
                businessUnit.put("saveWay", "ftp");
                businessUnit.put("objId", businessOwnerRepair.getString("repairId"));
                businessUnit.put("fileRealName", fileName);
                businessUnit.put("fileSaveName", fileName);
                FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
                flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
                if (flag < 1) {
                    throw new CmdException("保存图片失败");
                }
            }
        }

        if (StringUtil.jsonHasKayAndValue(reqJson, "noteId")) {
            NotepadPo notepadPo = new NotepadPo();
            notepadPo.setNoteId(reqJson.getString("noteId"));
            notepadPo.setThridId(repairPoolPo.getRepairId());
            flag = notepadV1InnerServiceSMOImpl.updateNotepad(notepadPo);
            if (flag < 1) {
                throw new CmdException("修改业主反馈失败");
            }
        }
    }
}
