package com.java110.community.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.roomRenovation.IDeleteRoomRenovationBMO;
import com.java110.community.bmo.roomRenovation.IGetRoomRenovationBMO;
import com.java110.community.bmo.roomRenovation.ISaveRoomRenovationBMO;
import com.java110.community.bmo.roomRenovation.IUpdateRoomRenovationBMO;
import com.java110.community.bmo.roomRenovationDetail.IDeleteRoomRenovationDetailBMO;
import com.java110.community.bmo.roomRenovationDetail.IGetRoomRenovationDetailBMO;
import com.java110.community.bmo.roomRenovationDetail.ISaveRoomRenovationDetailBMO;
import com.java110.community.bmo.roomRenovationRecord.IDeleteRoomRenovationRecordBMO;
import com.java110.community.bmo.roomRenovationRecord.IGetRoomRenovationRecordBMO;
import com.java110.community.bmo.roomRenovationRecord.ISaveRoomRenovationRecordBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IPhotoSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.community.CommunitySettingDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.roomRenovation.RoomRenovationDto;
import com.java110.dto.roomRenovationDetail.RoomRenovationDetailDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.ICommunitySettingInnerServiceSMO;
import com.java110.intf.community.IRoomRenovationInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.file.FileRelPo;
import com.java110.po.roomRenovation.RoomRenovationPo;
import com.java110.po.roomRenovationDetail.RoomRenovationDetailPo;
import com.java110.po.roomRenovationRecord.RoomRenovationRecordPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/roomRenovation")
public class RoomRenovationApi {

    @Autowired
    private ISaveRoomRenovationBMO saveRoomRenovationBMOImpl;

    @Autowired
    private IUpdateRoomRenovationBMO updateRoomRenovationBMOImpl;

    @Autowired
    private IDeleteRoomRenovationBMO deleteRoomRenovationBMOImpl;

    @Autowired
    private IGetRoomRenovationBMO getRoomRenovationBMOImpl;

    @Autowired
    private ISaveRoomRenovationDetailBMO saveRoomRenovationDetailBMOImpl;

    @Autowired
    private IDeleteRoomRenovationDetailBMO deleteRoomRenovationDetailBMOImpl;

    @Autowired
    private IGetRoomRenovationDetailBMO getRoomRenovationDetailBMOImpl;

    @Autowired
    private IRoomRenovationInnerServiceSMO roomRenovationInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private ISaveRoomRenovationRecordBMO saveRoomRenovationRecordBMO;

    @Autowired
    private IGetRoomRenovationRecordBMO getRoomRenovationRecordBMOImpl;

    @Autowired
    private IDeleteRoomRenovationRecordBMO deleteRoomRenovationRecordBMOImpl;

    @Autowired
    private ICommunitySettingInnerServiceSMO communitySettingInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IPhotoSMO photoSMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /roomRenovation/saveRoomRenovation
     * @path /app/roomRenovation/saveRoomRenovation
     */
    @RequestMapping(value = "/saveRoomRenovation", method = RequestMethod.POST)
    public ResponseEntity<String> saveRoomRenovation(@RequestBody JSONObject reqJson,
                                                     @RequestHeader(value = "store-id") String storeId,
                                                     @RequestHeader(value = "user-id") String userId) throws ParseException {

        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        Assert.hasKeyAndValue(reqJson, "roomName", "请求报文中未包含roomName");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "personName", "请求报文中未包含personName");
        Assert.hasKeyAndValue(reqJson, "personTel", "请求报文中未包含personTel");
        //Assert.hasKeyAndValue(reqJson, "isViolation", "请求报文中未包含isViolation");

        //获取开始时间
        String startTime = reqJson.getString("startTime") + " 00:00:00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date timeOne = format.parse(startTime);
        Calendar now = Calendar.getInstance();
        String newTime = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH) + " 00:00:00";
        Date timeTwo = format.parse(newTime);
        if (timeOne.getTime() < timeTwo.getTime()) {
            return ResultVo.error("装修时间不能小于当前时间！");
        }
        RoomRenovationPo roomRenovationPo = BeanConvertUtil.covertBean(reqJson, RoomRenovationPo.class);

        //判断是否已经存在该房屋的装修记录了
        RoomRenovationDto roomRenovationDto = new RoomRenovationDto();
        roomRenovationDto.setRoomId(roomRenovationPo.getRoomId());
        List<RoomRenovationDto> roomRenovationDtos = roomRenovationInnerServiceSMOImpl.queryRoomRenovations(roomRenovationDto);
        if (roomRenovationDtos.size() > 0) {
            for (RoomRenovationDto renovationDto : roomRenovationDtos) {
                if (renovationDto.getState().equals("1000") || renovationDto.getState().equals("3000") || renovationDto.getState().equals("4000")) {
                    throw new IllegalArgumentException("该房屋正在装修中，请仔细核对房屋信息！");
                }
            }
        }
        //待审核状态
        roomRenovationPo.setState("1000");
        //不违规
        roomRenovationPo.setIsViolation("N");
        roomRenovationPo.setStartTime(reqJson.getString("startTime") + " 00:00:00");
        roomRenovationPo.setEndTime(reqJson.getString("endTime") + " 23:59:59");
        //生成装修费用
        CommunitySettingDto communitySettingDto = new CommunitySettingDto();
        communitySettingDto.setSettingKey("REPAIR_CONFIG_FEE");
        communitySettingDto.setCommunityId(reqJson.getString("communityId"));
        //查询小区配置
        List<CommunitySettingDto> communitySettingDtos = communitySettingInnerServiceSMOImpl.queryCommunitySettings(communitySettingDto);
        String settingValue = null;
        if (communitySettingDtos != null && communitySettingDtos.size() > 0) {
            //获取小区设置值
            settingValue = communitySettingDtos.get(0).getSettingValue();
        }
        //获取房屋id
        String roomId = reqJson.getString("roomId");
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(roomId);
        //查询业主房屋关系表
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        Assert.listOnlyOne(ownerRoomRelDtos, "查询业主房屋关系表错误！");
        //获取业主id
        String ownerId = ownerRoomRelDtos.get(0).getOwnerId();
        if (!StringUtil.isEmpty(settingValue)) {
            String[] settingValues = settingValue.split(",");
            List<PayFeePo> payFeePos = new ArrayList<>();
            List<FeeAttrPo> feeAttrOwnerIdPos = new ArrayList<>();
            List<FeeAttrPo> feeAttrOwnerNamePos = new ArrayList<>();
            List<FeeAttrPo> feeAttrOwnerLinkPos = new ArrayList<>();
            List<FeeAttrPo> feeAttrOwnerTimePos = new ArrayList<>();
            for (int i = 0; i < settingValues.length; i++) {
                String configId = settingValues[i];
                FeeConfigDto feeConfigDto = new FeeConfigDto();
                feeConfigDto.setConfigId(configId);
                //查询费用项
                List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
                Assert.listOnlyOne(feeConfigDtos, "查询费用项错误！");
                PayFeePo payFeePo = new PayFeePo();
                payFeePo.setAmount(feeConfigDtos.get(0).getAdditionalAmount());
                if (!StringUtil.isEmpty(storeId)) {
                    payFeePo.setIncomeObjId(storeId);
                } else {
                    payFeePo.setIncomeObjId(reqJson.getString("storeId"));
                }
                payFeePo.setFeeTypeCd(feeConfigDtos.get(0).getFeeTypeCd());
                payFeePo.setStartTime(newTime);
                payFeePo.setEndTime(format.format(System.currentTimeMillis()));
                payFeePo.setCommunityId(reqJson.getString("communityId"));
                payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
                payFeePo.setUserId(userId);
                payFeePo.setPayerObjId(reqJson.getString("roomId"));
                payFeePo.setFeeFlag(feeConfigDtos.get(0).getFeeFlag());
                payFeePo.setState(FeeDto.STATE_DOING);
                payFeePo.setConfigId(configId);
                payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
                payFeePo.setBatchId("-1");
                payFeePos.add(payFeePo);
                FeeAttrPo feeAttrPo1 = new FeeAttrPo();
                feeAttrPo1.setFeeId(payFeePo.getFeeId());
                feeAttrPo1.setCommunityId(reqJson.getString("communityId"));
                feeAttrPo1.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo1.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_ID); //业主id
                feeAttrPo1.setValue(ownerId);
                feeAttrOwnerIdPos.add(feeAttrPo1);
                FeeAttrPo feeAttrPo2 = new FeeAttrPo();
                feeAttrPo2.setFeeId(payFeePo.getFeeId());
                feeAttrPo2.setCommunityId(reqJson.getString("communityId"));
                feeAttrPo2.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo2.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME); //业主名称
                feeAttrPo2.setValue(reqJson.getString("personName"));
                feeAttrOwnerNamePos.add(feeAttrPo2);
                FeeAttrPo feeAttrPo3 = new FeeAttrPo();
                feeAttrPo3.setFeeId(payFeePo.getFeeId());
                feeAttrPo3.setCommunityId(reqJson.getString("communityId"));
                feeAttrPo3.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo3.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK); //联系方式
                feeAttrPo3.setValue(reqJson.getString("personTel"));
                feeAttrOwnerLinkPos.add(feeAttrPo3);
                if (!FeeDto.FEE_FLAG_CYCLE.equals(feeConfigDtos.get(0).getFeeFlag())) {//一次性费用
                    FeeAttrPo feeAttrPo4 = new FeeAttrPo();
                    feeAttrPo4.setFeeId(payFeePo.getFeeId());
                    feeAttrPo4.setCommunityId(reqJson.getString("communityId"));
                    feeAttrPo4.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                    feeAttrPo4.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME); //结束时间
                    feeAttrPo4.setValue(reqJson.getString("startTime"));
                    feeAttrOwnerTimePos.add(feeAttrPo4);
                }
            }
            //生成费用
            feeInnerServiceSMOImpl.saveFee(payFeePos);
            //插入费用属性(业主id)
            feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrOwnerIdPos);
            //插入费用属性(业主姓名)
            feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrOwnerNamePos);
            //插入费用属性(业主联系方式)
            feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrOwnerLinkPos);
            //插入费用属性(费用截止时间)
            feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrOwnerTimePos);
        }
        return saveRoomRenovationBMOImpl.save(roomRenovationPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /roomRenovation/updateRoomRenovation
     * @path /app/roomRenovation/updateRoomRenovation
     */
    @RequestMapping(value = "/updateRoomRenovation", method = RequestMethod.POST)
    public ResponseEntity<String> updateRoomRenovation(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        Assert.hasKeyAndValue(reqJson, "roomName", "请求报文中未包含roomName");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "personName", "请求报文中未包含personName");
        Assert.hasKeyAndValue(reqJson, "personTel", "请求报文中未包含personTel");
        Assert.hasKeyAndValue(reqJson, "isViolation", "请求报文中未包含isViolation");
        Assert.hasKeyAndValue(reqJson, "rId", "rId不能为空");
        RoomRenovationPo roomRenovationPo = BeanConvertUtil.covertBean(reqJson, RoomRenovationPo.class);
        roomRenovationPo.setStartTime(roomRenovationPo.getStartTime() + " 00:00:00");
        roomRenovationPo.setEndTime(roomRenovationPo.getEndTime() + " 23:59:59");
        //如果状态为装修中、待验收，则房屋状态改为装修中；如果状态为验收成功，则房屋状态改为已装修；如果为待审核、审核失败、验收失败，则房屋状态改为已交房
        if (roomRenovationPo.getState().equals("3000") || roomRenovationPo.getState().equals("4000")) {
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(roomRenovationPo.getRoomId());
            //房屋状态变为装修中
            roomDto.setState("2009");
            updateRoomRenovationBMOImpl.update(roomRenovationPo);
            return updateRoomRenovationBMOImpl.updateRoom(roomDto);
        } else if (roomRenovationPo.getState().equals("1000") || roomRenovationPo.getState().equals("2000")
                || roomRenovationPo.getState().equals("5000")) {
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(roomRenovationPo.getRoomId());
            //房屋状态变为已交房
            roomDto.setState("2003");
            updateRoomRenovationBMOImpl.update(roomRenovationPo);
            return updateRoomRenovationBMOImpl.updateRoom(roomDto);
        } else if (roomRenovationPo.getState().equals("6000")) {
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(roomRenovationPo.getRoomId());
            //房屋状态变为已装修
            roomDto.setState("2005");
            updateRoomRenovationBMOImpl.update(roomRenovationPo);
            return updateRoomRenovationBMOImpl.updateRoom(roomDto);
        } else {
            return updateRoomRenovationBMOImpl.update(roomRenovationPo);
        }
    }

    /**
     * 装修完成
     *
     * @param reqJson
     * @return
     * @serviceCode /roomRenovation/updateRoomRenovationState
     * @path /app/roomRenovation/updateRoomRenovationState
     */
    @RequestMapping(value = "/updateRoomRenovationState", method = RequestMethod.POST)
    public ResponseEntity<String> updateRoomRenovationState(@RequestBody JSONObject reqJson) {
        RoomRenovationPo roomRenovationPo = BeanConvertUtil.covertBean(reqJson, RoomRenovationPo.class);
        //装修完成,状态变为待验收
        roomRenovationPo.setState("4000");
        return updateRoomRenovationBMOImpl.update(roomRenovationPo);
    }

    /**
     * 查询装修记录
     *
     * @param
     * @return
     * @serviceCode /roomRenovation/queryRoomRenovationRecord
     * @path /app/roomRenovation/queryRoomRenovationRecord
     */
    @RequestMapping(value = "/queryRoomRenovationRecord", method = RequestMethod.GET)
    public ResponseEntity<String> queryRoomRenovationRecord(@RequestParam(value = "rId", required = false) String rId,
                                                            @RequestParam(value = "page", required = false) int page,
                                                            @RequestParam(value = "row", required = false) int row) {
        RoomRenovationRecordPo roomRenovationRecord = new RoomRenovationRecordPo();
        roomRenovationRecord.setrId(rId);
        roomRenovationRecord.setPage(page);
        roomRenovationRecord.setRow(row);
        return getRoomRenovationRecordBMOImpl.getRoomRenovationRecord(roomRenovationRecord);
    }


    /**
     * 查询装修详情
     *
     * @param recordId
     * @param page
     * @param row
     * @return
     * @serviceCode /roomRenovation/queryRoomRenovationRecordDetail
     * @path /app/roomRenovation/queryRoomRenovationRecordDetail
     */
    @RequestMapping(value = "/queryRoomRenovationRecordDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryRoomRenovationRecordDetail(@RequestParam(value = "recordId", required = false) String recordId,
                                                                  @RequestParam(value = "page", required = false) int page,
                                                                  @RequestParam(value = "row", required = false) int row) {
        RoomRenovationRecordPo roomRenovationRecord = new RoomRenovationRecordPo();
        roomRenovationRecord.setRecordId(recordId);
        roomRenovationRecord.setPage(page);
        roomRenovationRecord.setRow(row);
        return getRoomRenovationRecordBMOImpl.get(roomRenovationRecord);
    }

    /**
     * 删除装修记录
     *
     * @param reqJson
     * @return
     * @serviceCode /roomRenovation/deleteRoomRenovationRecord
     * @path /app/roomRenovation/deleteRoomRenovationRecord
     */
    @RequestMapping(value = "/deleteRoomRenovationRecord", method = RequestMethod.POST)
    public ResponseEntity<String> deleteRoomRenovationRecord(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "recordId", "recordId不能为空");
        RoomRenovationRecordPo roomRenovationRecordPo = BeanConvertUtil.covertBean(reqJson, RoomRenovationRecordPo.class);
        //获取装修记录id
        String recordId = reqJson.getString("recordId");
        FileRelPo fileRelpo = new FileRelPo();
        fileRelpo.setObjId(recordId);
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(recordId);
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos != null && fileRelDtos.size() > 0) {
            //删除文件表图片和视频
            fileRelInnerServiceSMOImpl.deleteFileRel(fileRelpo);
        }
        return deleteRoomRenovationRecordBMOImpl.delete(roomRenovationRecordPo);
    }

    /**
     * 装修记录
     *
     * @param reqJson
     * @return
     * @serviceCode /roomRenovation/updateRoomDecorationRecord
     * @path /app/roomRenovation/updateRoomDecorationRecord
     */
    @RequestMapping(value = "/updateRoomDecorationRecord", method = RequestMethod.POST)
    public ResponseEntity<String> updateRoomDecorationRecord(@RequestBody JSONObject reqJson, @RequestHeader(value = "user-id") String userId) {
        RoomRenovationPo roomRenovationPo = BeanConvertUtil.covertBean(reqJson, RoomRenovationPo.class);
        //图片
        List<String> photos = roomRenovationPo.getPhotos();
        //视频
        String videoName = roomRenovationPo.getVideoName();
        //备注
        String remark = roomRenovationPo.getRemark();
        //装修id
        String rId = roomRenovationPo.getrId();
        //状态
        String state = roomRenovationPo.getState();
        //是否违规
        String isTrue = roomRenovationPo.getIsTrue();
        //查询当前用户信息
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setStatusCd("0");
        List<UserDto> users = userInnerServiceSMOImpl.getUsers(userDto);
        RoomRenovationRecordPo roomRenovationRecordPo = new RoomRenovationRecordPo();
        roomRenovationRecordPo.setrId(rId);
        roomRenovationRecordPo.setRemark(remark);
        roomRenovationRecordPo.setState(state);
        roomRenovationRecordPo.setStatusCd(roomRenovationPo.getStatusCd());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        roomRenovationRecordPo.setCreateTime(format.format(new Date()));
        roomRenovationRecordPo.setStaffId(userId);
        roomRenovationRecordPo.setStaffName(users.get(0).getName());
        roomRenovationRecordPo.setIsTrue(isTrue);
        saveRoomRenovationRecordBMO.saveRecord(roomRenovationRecordPo);
        for (String photo : photos) {
            photoSMOImpl.savePhoto(photo,roomRenovationRecordPo.getRecordId(),roomRenovationPo.getCommunityId(),"19000");
        }
        FileRelPo fileRelPo = new FileRelPo();
        fileRelPo.setObjId(roomRenovationRecordPo.getRecordId());
        //table表示表存储 ftp表示ftp文件存储
        fileRelPo.setSaveWay("ftp");
        fileRelPo.setCreateTime(new Date());
//        //图片上传
//        if (photos != null && photos.size() > 0) {
//            //19000表示装修图片
//            fileRelPo.setRelTypeCd("19000");
//            for (String photo : photos) {
//                fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
//                fileRelPo.setFileRealName(photo);
//                fileRelPo.setFileSaveName(photo);
//                fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
//            }
//        }
        //视频上传
        if (!StringUtil.isEmpty(videoName)) {
            //21000表示装修视频
            fileRelPo.setRelTypeCd("21000");
            fileRelPo.setFileRealName(videoName);
            fileRelPo.setFileSaveName(videoName);
            fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
        }
        RoomRenovationRecordPo roomRenovationRecord = new RoomRenovationRecordPo();
        roomRenovationRecord.setRecordId(roomRenovationRecordPo.getRecordId());
        return getRoomRenovationRecordBMOImpl.get(roomRenovationRecord);
    }

    /**
     * 装修审核
     *
     * @param reqJson
     * @return
     * @serviceCode /roomRenovation/updateRoomToExamine
     * @path /app/roomRenovation/updateRoomToExamine
     */
    @RequestMapping(value = "/updateRoomToExamine", method = RequestMethod.POST)
    public ResponseEntity<String> updateRoomToExamine(@RequestBody JSONObject reqJson) {
        RoomRenovationPo roomRenovationPo = BeanConvertUtil.covertBean(reqJson, RoomRenovationPo.class);
        //审核通过房屋状态变为装修中
        if (roomRenovationPo.getState().equals("3000")) {
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(roomRenovationPo.getRoomId());
            //房屋状态变为装修中
            roomDto.setState("2009");
            //更新装修信息
            updateRoomRenovationBMOImpl.update(roomRenovationPo);
            return updateRoomRenovationBMOImpl.updateRoom(roomDto);
        } else if (roomRenovationPo.getState().equals("2000")) {
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(roomRenovationPo.getRoomId());
            //房屋状态变为已交房
            roomDto.setState("2003");
            //更新装修信息
            updateRoomRenovationBMOImpl.update(roomRenovationPo);
            return updateRoomRenovationBMOImpl.updateRoom(roomDto);
        } else {
            return updateRoomRenovationBMOImpl.update(roomRenovationPo);
        }
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /roomRenovation/deleteRoomRenovation
     * @path /app/roomRenovation/deleteRoomRenovation
     */
    @RequestMapping(value = "/deleteRoomRenovation", method = RequestMethod.POST)
    public ResponseEntity<String> deleteRoomRenovation(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "rId", "rId不能为空");
        RoomRenovationPo roomRenovationPo = BeanConvertUtil.covertBean(reqJson, RoomRenovationPo.class);
        return deleteRoomRenovationBMOImpl.delete(roomRenovationPo);
    }

    /**
     * 查询房屋装修
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /roomRenovation/queryRoomRenovation
     * @path /app/roomRenovation/queryRoomRenovation
     */
    @RequestMapping(value = "/queryRoomRenovation", method = RequestMethod.GET)
    public ResponseEntity<String> queryRoomRenovation(@RequestParam(value = "communityId", required = false) String communityId,
                                                      @RequestParam(value = "roomId", required = false) String roomId,
                                                      @RequestParam(value = "roomName", required = false) String roomName,
                                                      @RequestParam(value = "personName", required = false) String personName,
                                                      @RequestParam(value = "personTel", required = false) String personTel,
                                                      @RequestParam(value = "state", required = false) String state,
                                                      @RequestParam(value = "isPostpone", required = false) String isPostpone,
                                                      @RequestParam(value = "renovationTime", required = false) String renovationTime,
                                                      @RequestParam(value = "renovationStartTime", required = false) String renovationStartTime,
                                                      @RequestParam(value = "renovationEndTime", required = false) String renovationEndTime,
                                                      @RequestHeader(value = "user-id") String userId,
                                                      @RequestParam(value = "page", required = false) int page,
                                                      @RequestParam(value = "row", required = false) int row) {
        RoomRenovationDto roomRenovationDto = new RoomRenovationDto();
        roomRenovationDto.setPage(page);
        roomRenovationDto.setRow(row);
        roomRenovationDto.setCommunityId(communityId);
        roomRenovationDto.setRoomId(roomId);
        roomRenovationDto.setRoomName(roomName);
        roomRenovationDto.setPersonName(personName);
        roomRenovationDto.setPersonTel(personTel);
        roomRenovationDto.setState(state);
        roomRenovationDto.setUserId(userId);
        roomRenovationDto.setIsPostpone(isPostpone);
        roomRenovationDto.setRenovationTime(renovationTime);
        roomRenovationDto.setRenovationStartTime(renovationStartTime);
        roomRenovationDto.setRenovationEndTime(renovationEndTime);
        return getRoomRenovationBMOImpl.get(roomRenovationDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /roomRenovation/saveRoomRenovationDetail
     * @path /app/roomRenovation/saveRoomRenovationDetail
     */
    @RequestMapping(value = "/saveRoomRenovationDetail", method = RequestMethod.POST)
    @Java110Transactional
    public ResponseEntity<String> saveRoomRenovationDetail(@RequestHeader(value = "user-id") String userId,
                                                           @RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "rId", "请求报文中未包含rId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "detailType", "请求报文中未包含detailType");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setPage(1);
        userDto.setRow(1);
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");

        RoomRenovationDetailPo roomRenovationDetailPo = BeanConvertUtil.covertBean(reqJson, RoomRenovationDetailPo.class);
        roomRenovationDetailPo.setStaffId(userId);
        roomRenovationDetailPo.setStaffName(userDtos.get(0).getName());
        RoomRenovationPo roomRenovationPo = new RoomRenovationPo();
        roomRenovationPo.setrId(roomRenovationDetailPo.getrId());
        roomRenovationPo.setState(roomRenovationDetailPo.getState());
        //修改房屋装修状态
        updateRoomRenovationBMOImpl.update(roomRenovationPo);
        //验收成功后房屋状态变为已装修
        if (roomRenovationDetailPo.getState().equals("5000")) {
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(reqJson.getString("roomId"));
            //状态变为已装修
            roomDto.setState("2005");
            //验收成功
            roomRenovationDetailPo.setState("3000");
            saveRoomRenovationDetailBMOImpl.save(roomRenovationDetailPo);
            return updateRoomRenovationBMOImpl.updateRoom(roomDto);
        } else if (roomRenovationDetailPo.getState().equals("6000")) { //验收失败装修状态变为装修中
            RoomRenovationPo roomRenovation = new RoomRenovationPo();
            roomRenovation.setrId(roomRenovationDetailPo.getrId());
            roomRenovation.setState("3000");
            //验收失败把房屋状态变为装修中，让业主装修整改
            updateRoomRenovationBMOImpl.update(roomRenovation);
            //验收失败
            roomRenovationDetailPo.setState("4000");
            return saveRoomRenovationDetailBMOImpl.save(roomRenovationDetailPo);
        } else {
            return saveRoomRenovationDetailBMOImpl.save(roomRenovationDetailPo);
        }
    }


    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /roomRenovation/deleteRoomRenovationDetail
     * @path /app/roomRenovation/deleteRoomRenovationDetail
     */
    @RequestMapping(value = "/deleteRoomRenovationDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteRoomRenovationDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        RoomRenovationDetailPo roomRenovationDetailPo = BeanConvertUtil.covertBean(reqJson, RoomRenovationDetailPo.class);
        return deleteRoomRenovationDetailBMOImpl.delete(roomRenovationDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /roomRenovation/queryRoomRenovationDetail
     * @path /app/roomRenovation/queryRoomRenovationDetail
     */
    @RequestMapping(value = "/queryRoomRenovationDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryRoomRenovationDetail(@RequestParam(value = "communityId") String communityId,
                                                            @RequestParam(value = "page") int page,
                                                            @RequestParam(value = "row") int row,
                                                            @RequestParam(value = "rId") String rId) {
        RoomRenovationDetailDto roomRenovationDetailDto = new RoomRenovationDetailDto();
        roomRenovationDetailDto.setPage(page);
        roomRenovationDetailDto.setRow(row);
        roomRenovationDetailDto.setCommunityId(communityId);
        roomRenovationDetailDto.setrId(rId);
        return getRoomRenovationDetailBMOImpl.get(roomRenovationDetailDto);
    }
}
