package com.java110.core.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IPhotoSMO;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoSMOImpl implements IPhotoSMO {

    @Autowired(required = false)
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;


    @Autowired(required = false)
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    public int savePhoto(String photo, String objId, String communityId){
        return savePhoto(photo,objId,communityId,"11000");
    }

    @Override
    public int savePhoto(String photo, String objId, String communityId,String relTypeCd) {
        if (StringUtil.isEmpty(photo)) {
            return 0;
        }
        if (photo.length() > 512) { //说明是图片
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(photo);
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(communityId);
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
            photo = fileName;
        }
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("relTypeCd", relTypeCd);
        businessUnit.put("saveWay", "table");
        businessUnit.put("objId", objId);
        businessUnit.put("fileRealName", photo);
        businessUnit.put("fileSaveName", photo);

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(objId);
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        if(fileRelDtos == null || fileRelDtos.size()< 1){
            businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
            return fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
        }

        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
        fileRelPo.setFileRelId(fileRelDtos.get(0).getFileRelId());
        return fileRelInnerServiceSMOImpl.updateFileRel(fileRelPo);
    }

    @Override
    public int savePhoto(JSONObject reqJson, String objId, String communityId) {
        if (!reqJson.containsKey("photo") || StringUtils.isEmpty(reqJson.getString("photo"))) {
            return 0;
        }
        if (reqJson.getString("photo").length() > 512) { //说明是图片
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("photo"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(communityId);
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
            reqJson.put("photo", fileName);

        }
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("relTypeCd", "11000");
        businessUnit.put("saveWay", "table");
        businessUnit.put("objId", objId);
        businessUnit.put("fileRealName", reqJson.getString("photo"));
        businessUnit.put("fileSaveName", reqJson.getString("photo"));

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(objId);
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        if(fileRelDtos == null || fileRelDtos.size()< 1){
            businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
            return fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
        }

        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
        fileRelPo.setFileRelId(fileRelDtos.get(0).getFileRelId());
        return fileRelInnerServiceSMOImpl.updateFileRel(fileRelPo);


    }

}
