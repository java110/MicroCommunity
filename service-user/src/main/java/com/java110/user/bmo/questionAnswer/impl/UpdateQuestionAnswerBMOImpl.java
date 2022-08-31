package com.java110.user.bmo.questionAnswer.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileRelDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.user.IQuestionAnswerInnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.questionAnswer.QuestionAnswerPo;
import com.java110.user.bmo.questionAnswer.IUpdateQuestionAnswerBMO;
import com.java110.utils.exception.CmdException;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("updateQuestionAnswerBMOImpl")
public class UpdateQuestionAnswerBMOImpl implements IUpdateQuestionAnswerBMO {

    @Autowired
    private IQuestionAnswerInnerServiceSMO questionAnswerInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    /**
     * @param questionAnswerPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(QuestionAnswerPo questionAnswerPo) {

        int flag = questionAnswerInnerServiceSMOImpl.updateQuestionAnswer(questionAnswerPo);

        if (flag > 0) {
            //图片
            List<String> photos = questionAnswerPo.getPhotos();
            //图片更新
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(questionAnswerPo.getQaId());
            //查询文件表
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            //如果文件表里有这个资源的记录，就先删掉原先的
            if (fileRelDtos != null && fileRelDtos.size() > 0) {
                //先把删除文件表图片
                for (FileRelDto fileRel : fileRelDtos) {
                    FileRelPo fileRelPo = new FileRelPo();
                    fileRelPo.setFileRelId(fileRel.getFileRelId());
                    flag = fileRelInnerServiceSMOImpl.deleteFileRel(fileRelPo);
                    if (flag < 1) {
                        throw new CmdException("保存数据失败");
                    }
                }
                FileRelPo fileRel = new FileRelPo();
                fileRel.setObjId(questionAnswerPo.getQaId());
                //table表示表存储 ftp表示ftp文件存储
                fileRel.setSaveWay("ftp");
                fileRel.setCreateTime(new Date());
                if (photos != null && photos.size() > 0) {
                    //28000表示问卷图片
                    fileRel.setRelTypeCd("28000");
                    for (String photo : photos) {
                        fileRel.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
                        fileRel.setFileRealName(photo);
                        fileRel.setFileSaveName(photo);
                        flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRel);
                        if (flag < 1) {
                            throw new CmdException("保存数据失败");
                        }
                    }
                }
            } else if (photos != null && photos.size() > 0) {  //如果文件表里没有这个资源的记录，就判断是否有图片传过来，有就插入到文件表里
                FileRelPo fileRel = new FileRelPo();
                fileRel.setObjId(questionAnswerPo.getQaId());
                //table表示表存储 ftp表示ftp文件存储
                fileRel.setSaveWay("ftp");
                fileRel.setCreateTime(new Date());
                //28000表示问卷图片
                fileRel.setRelTypeCd("28000");
                for (String photo : photos) {
                    fileRel.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
                    fileRel.setFileRealName(photo);
                    fileRel.setFileSaveName(photo);
                    flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRel);
                    if (flag < 1) {
                        throw new CmdException("保存数据失败");
                    }
                }
            }
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }
}