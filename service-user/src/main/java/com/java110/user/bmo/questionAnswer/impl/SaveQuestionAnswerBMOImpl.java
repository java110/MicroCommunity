package com.java110.user.bmo.questionAnswer.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.user.IQuestionAnswerInnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.questionAnswer.QuestionAnswerPo;
import com.java110.user.bmo.questionAnswer.ISaveQuestionAnswerBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("saveQuestionAnswerBMOImpl")
public class SaveQuestionAnswerBMOImpl implements ISaveQuestionAnswerBMO {

    @Autowired
    private IQuestionAnswerInnerServiceSMO questionAnswerInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param questionAnswerPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(QuestionAnswerPo questionAnswerPo) {
        questionAnswerPo.setQaId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_qaId));
        int flag = questionAnswerInnerServiceSMOImpl.saveQuestionAnswer(questionAnswerPo);
        if (flag > 0) {
            //图片
            List<String> photos = questionAnswerPo.getPhotos();
            FileRelPo fileRelPo = new FileRelPo();
            fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
            fileRelPo.setObjId(questionAnswerPo.getQaId());
            //table表示表存储 ftp表示ftp文件存储
            fileRelPo.setSaveWay("ftp");
            fileRelPo.setCreateTime(new Date());
            //图片上传
            if (photos != null && photos.size() > 0) {
                //28000表示问卷图片
                fileRelPo.setRelTypeCd("28000");
                for (String photo : photos) {
                    fileRelPo.setFileRealName(photo);
                    fileRelPo.setFileSaveName(photo);
                    fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
                }
            }
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }
}