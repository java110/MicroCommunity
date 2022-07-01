package com.java110.fee.bmo.applyRoomDiscount.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.fee.bmo.applyRoomDiscount.IUpdateApplyRoomDiscountBMO;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.fee.IApplyRoomDiscountInnerServiceSMO;
import com.java110.po.applyRoomDiscount.ApplyRoomDiscountPo;
import com.java110.po.file.FileRelPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("updateApplyRoomDiscountBMOImpl")
public class UpdateApplyRoomDiscountBMOImpl implements IUpdateApplyRoomDiscountBMO {

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    /**
     * @param applyRoomDiscountPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ApplyRoomDiscountPo applyRoomDiscountPo) {

        int flag = applyRoomDiscountInnerServiceSMOImpl.updateApplyRoomDiscount(applyRoomDiscountPo);

        if (flag > 0) {
            //获取图片集合
            List<String> photos = applyRoomDiscountPo.getPhotos();
            if (photos != null && photos.size() > 0) {
                FileRelPo fileRelPo = new FileRelPo();
                fileRelPo.setObjId(applyRoomDiscountPo.getArdId());
                fileRelInnerServiceSMOImpl.deleteFileRel(fileRelPo);
                FileRelPo fileRel = new FileRelPo();
                fileRel.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
                fileRel.setObjId(applyRoomDiscountPo.getArdId());
                //table表示表存储 ftp表示ftp文件存储
                fileRel.setSaveWay("ftp");
                fileRel.setCreateTime(new Date());
                //19000表示装修图片
                fileRel.setRelTypeCd("19000");
                for (String photo : photos) {
                    FileDto fileDto = new FileDto();
                    fileDto.setCommunityId("-1");
                    fileDto.setContext(photo);
                    fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                    fileDto.setFileName(fileDto.getFileId());
                    String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
                    fileRel.setFileRealName(fileName);
                    fileRel.setFileSaveName(fileName);
                    fileRelInnerServiceSMOImpl.saveFileRel(fileRel);
                }
            }
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
