package com.java110.store.cmd.complaint;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.dto.file.FileDto;
import com.java110.intf.common.IComplaintUserInnerServiceSMO;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IComplaintV1InnerServiceSMO;
import com.java110.po.complaint.ComplaintPo;
import com.java110.po.file.FileRelPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "complaint.saveComplaint")
public class SaveComplaintCmd extends Cmd{

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IComplaintV1InnerServiceSMO complaintV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
//        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择投诉类型");
        Assert.hasKeyAndValue(reqJson, "roomId", "必填，请选择房屋编号");
        Assert.hasKeyAndValue(reqJson, "complaintName", "必填，请填写投诉人");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写投诉电话");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写用户信息");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写投诉内容");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");


    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(reqJson.getString("communityId"));
        communityMemberDto.setMemberTypeCd("390001200002");
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

        Assert.listOnlyOne(communityMemberDtos,"小区未入驻");
        reqJson.put("storeId",communityMemberDtos.get(0).getMemberId());
        reqJson.put("startUserId", reqJson.getString("userId"));
        reqJson.put("complaintId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_complaintId));
        reqJson.put("state", "10001");
        ComplaintPo complaintPo = BeanConvertUtil.covertBean(reqJson, ComplaintPo.class);
        int flag = complaintV1InnerServiceSMOImpl.saveComplaint(complaintPo);
        if(flag < 1){
            throw new CmdException("投诉失败");
        }

        if (reqJson.containsKey("photos") && reqJson.getJSONArray("photos").size() > 0) {
            JSONArray photos = reqJson.getJSONArray("photos");
            for (int photoIndex = 0; photoIndex < photos.size(); photoIndex++) {
                String _photo = photos.getString(photoIndex);
                if(_photo.length()> 512){
                    FileDto fileDto = new FileDto();
                    fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                    fileDto.setFileName(fileDto.getFileId());
                    fileDto.setContext(_photo);
                    fileDto.setSuffix("jpeg");
                    fileDto.setCommunityId(reqJson.getString("communityId"));
                    _photo = fileInnerServiceSMOImpl.saveFile(fileDto);
                }
                JSONObject businessUnit = new JSONObject();
                businessUnit.put("fileRelId", "-1");
                businessUnit.put("relTypeCd", "13000");
                businessUnit.put("saveWay", "table");
                businessUnit.put("objId", reqJson.getString("complaintId"));
                businessUnit.put("fileRealName", _photo);
                businessUnit.put("fileSaveName", _photo);
                FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
                fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            }
        }

        //commit(context);
        ComplaintDto complaintDto = BeanConvertUtil.covertBean(reqJson, ComplaintDto.class);
        complaintDto.setCurrentUserId(reqJson.getString("userId"));
        complaintDto = complaintUserInnerServiceSMOImpl.startProcess(complaintDto);

        //没有配置流程，直接设置为完成
        if(complaintDto == null){
             complaintPo = new ComplaintPo();
             complaintPo.setComplaintId(reqJson.getString("complaintId"));
             complaintPo.setState(ComplaintDto.STATE_FINISH);
            complaintV1InnerServiceSMOImpl.updateComplaint(complaintPo);
        }
    }
}
