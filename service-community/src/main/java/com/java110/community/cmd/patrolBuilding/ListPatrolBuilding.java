package com.java110.community.cmd.patrolBuilding;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.patrolBuilding.PatrolBuildingDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IPatrolBuildingV1InnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询巡楼信息
 *
 * @author fqz
 * @date 2023-12-26 16:22
 */
@Java110Cmd(serviceCode = "patrolBuilding.listPatrolBuilding")
public class ListPatrolBuilding extends Cmd {

    @Autowired
    private IPatrolBuildingV1InnerServiceSMO iPatrolBuildingV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        PatrolBuildingDto patrolBuildingDto = BeanConvertUtil.covertBean(reqJson, PatrolBuildingDto.class);
        int count = iPatrolBuildingV1InnerServiceSMOImpl.queryPatrolBuildingsCount(patrolBuildingDto);
        List<PatrolBuildingDto> patrolBuildingDtoList = new ArrayList<>();
        if (count > 0) {
            List<PatrolBuildingDto> patrolBuildings = iPatrolBuildingV1InnerServiceSMOImpl.queryPatrolBuildings(patrolBuildingDto);
            for (PatrolBuildingDto patrolBuilding : patrolBuildings) {
                List<String> imgUrls = new ArrayList<>();
                FileRelDto fileRelDto = new FileRelDto();
                fileRelDto.setObjId(patrolBuilding.getPbId());
                List<FileRelDto> fileRelList = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
                if (fileRelList != null && fileRelList.size() > 0) {
                    for (FileRelDto fileRel : fileRelList) {
                        imgUrls.add("/callComponent/download/getFile/file?fileId=" + fileRel.getFileRealName() + "&communityId=-1");
                    }
                }
                patrolBuilding.setImgUrls(imgUrls);
                patrolBuildingDtoList.add(patrolBuilding);
            }
        } else {
            patrolBuildingDtoList = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, patrolBuildingDtoList);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
