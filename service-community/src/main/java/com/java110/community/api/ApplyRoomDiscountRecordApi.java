package com.java110.community.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.applyRoomDiscountRecord.IDeleteApplyRoomDiscountRecordBMO;
import com.java110.community.bmo.applyRoomDiscountRecord.IGetApplyRoomDiscountRecordBMO;
import com.java110.community.bmo.applyRoomDiscountRecord.ISaveApplyRoomDiscountRecordBMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountRecordDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.applyRoomDiscountRecord.ApplyRoomDiscountRecordPo;
import com.java110.po.file.FileRelPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/applyRoomDiscountRecord")
public class ApplyRoomDiscountRecordApi {

    @Autowired
    private IGetApplyRoomDiscountRecordBMO getApplyRoomDiscountRecordBMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private ISaveApplyRoomDiscountRecordBMO saveApplyRoomDiscountRecordBMOImpl;

    @Autowired
    private IDeleteApplyRoomDiscountRecordBMO deleteApplyRoomDiscountRecordBMOImpl;

    /**
     * 查询验房记录
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /applyRoomDiscountRecord/queryApplyRoomDiscountRecord
     * @path /app/applyRoomDiscountRecord/queryApplyRoomDiscountRecord
     */
    @RequestMapping(value = "/queryApplyRoomDiscountRecord", method = RequestMethod.GET)
    public ResponseEntity<String> queryApplyRoomDiscountRecord(@RequestParam(value = "communityId") String communityId,
                                                               @RequestParam(value = "ardId", required = false) String ardId,
                                                               @RequestParam(value = "roomId", required = false) String roomId,
                                                               @RequestParam(value = "state", required = false) String state,
                                                               @RequestParam(value = "page") int page,
                                                               @RequestParam(value = "row") int row) {
        ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto = new ApplyRoomDiscountRecordDto();
        applyRoomDiscountRecordDto.setCommunityId(communityId);
        applyRoomDiscountRecordDto.setArdId(ardId);
        applyRoomDiscountRecordDto.setPage(page);
        applyRoomDiscountRecordDto.setRow(row);
        return getApplyRoomDiscountRecordBMOImpl.get(applyRoomDiscountRecordDto);
    }

    /**
     * 查询验房记录详情
     *
     * @return
     * @serviceCode /applyRoomDiscountRecord/queryApplyRoomDiscountRecordDetail
     * @path /app/applyRoomDiscountRecord/queryApplyRoomDiscountRecordDetail
     */
    @RequestMapping(value = "/queryApplyRoomDiscountRecordDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryRoomRenovationRecordDetail(@RequestParam(value = "ardrId", required = false) String ardrId,
                                                                  @RequestParam(value = "page", required = false) int page,
                                                                  @RequestParam(value = "row", required = false) int row) {
        ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto = new ApplyRoomDiscountRecordDto();
        applyRoomDiscountRecordDto.setArdrId(ardrId);
        applyRoomDiscountRecordDto.setPage(page);
        applyRoomDiscountRecordDto.setRow(row);
        return getApplyRoomDiscountRecordBMOImpl.getRecord(applyRoomDiscountRecordDto);
    }

    /**
     * 添加验房记录
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscountRecord/addApplyRoomDiscountRecord
     * @path /app/applyRoomDiscountRecord/addApplyRoomDiscountRecord
     */
    @RequestMapping(value = "/addApplyRoomDiscountRecord", method = RequestMethod.POST)
    public ResponseEntity<String> addApplyRoomDiscountRecord(@RequestBody JSONObject reqJson, @RequestHeader(value = "user-id") String userId) {
        ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountRecordPo.class);
        //图片
        List<String> photos = applyRoomDiscountRecordPo.getPhotos();
        //视频
        String videoName = applyRoomDiscountRecordPo.getVideoName();
        //备注
        String remark = applyRoomDiscountRecordPo.getRemark();
        //优惠申请id
        String ardId = applyRoomDiscountRecordPo.getArdId();
        //状态
        String state = applyRoomDiscountRecordPo.getState();
        //是否违规
        String isTrue = applyRoomDiscountRecordPo.getIsTrue();
        //小区id
        String communityId = applyRoomDiscountRecordPo.getCommunityId();
        //查询当前用户信息
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setStatusCd("0");
        List<UserDto> users = userInnerServiceSMOImpl.getUsers(userDto);
        Assert.listOnlyOne(users, "查询用户错误！");
        ApplyRoomDiscountRecordPo applyRoomDiscountRecord = new ApplyRoomDiscountRecordPo();
        applyRoomDiscountRecord.setArdrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ARDRID));
        applyRoomDiscountRecord.setArdId(ardId);
        applyRoomDiscountRecord.setRemark(remark);
        applyRoomDiscountRecord.setState(state);
        applyRoomDiscountRecord.setCreateUserId(userId);
        applyRoomDiscountRecord.setCreateUserName(users.get(0).getName());
        applyRoomDiscountRecord.setIsTrue(isTrue);
        applyRoomDiscountRecord.setCommunityId(communityId);
        applyRoomDiscountRecord.setbId("-1");
        saveApplyRoomDiscountRecordBMOImpl.saveRecord(applyRoomDiscountRecord);
        FileRelPo fileRelPo = new FileRelPo();
        fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
        fileRelPo.setObjId(applyRoomDiscountRecord.getArdrId());
        //table表示表存储 ftp表示ftp文件存储
        fileRelPo.setSaveWay("ftp");
        fileRelPo.setCreateTime(new Date());
        //图片上传
        if (photos != null && photos.size() > 0) {
            //19000表示装修图片
            fileRelPo.setRelTypeCd("19000");
            for (String photo : photos) {
                fileRelPo.setFileRealName(photo);
                fileRelPo.setFileSaveName(photo);
                fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            }
        }
        //视频上传
        if (!StringUtil.isEmpty(videoName)) {
            //21000表示装修视频
            fileRelPo.setRelTypeCd("21000");
            fileRelPo.setFileRealName(videoName);
            fileRelPo.setFileSaveName(videoName);
            fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
        }
        ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto = new ApplyRoomDiscountRecordDto();
        applyRoomDiscountRecordDto.setArdrId(applyRoomDiscountRecord.getArdrId());
        return getApplyRoomDiscountRecordBMOImpl.get(applyRoomDiscountRecordDto);
    }

    /**
     * 删除空置房验房记录
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscountRecord/cutApplyRoomDiscountRecord
     * @path /app/applyRoomDiscountRecord/cutApplyRoomDiscountRecord
     */
    @RequestMapping(value = "/cutApplyRoomDiscountRecord", method = RequestMethod.POST)
    public ResponseEntity<String> deleteApplyRoomDiscountRecord(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "ardrId", "ardrId不能为空");
        ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountRecordPo.class);
        //获取验房记录id
        String ardrId = reqJson.getString("ardrId");
        FileRelPo fileRelpo = new FileRelPo();
        fileRelpo.setObjId(ardrId);
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(ardrId);
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos != null && fileRelDtos.size() > 0) {
            //删除文件表图片和视频
            fileRelInnerServiceSMOImpl.deleteFileRel(fileRelpo);
        }
        return deleteApplyRoomDiscountRecordBMOImpl.delete(applyRoomDiscountRecordPo);
    }

}
