package com.java110.api.listener.ownerRepair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ownerRepair.IOwnerRepairBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.file.FileRelPo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.constant.ServiceCodeRepairDispatchStepConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 完成报修
 * add by wuxw 2019-06-30
 */
@Java110Listener("repairCloseListener")
public class RepairFinishListener extends AbstractServiceApiPlusListener {

    private static Logger logger = LoggerFactory.getLogger(RepairFinishListener.class);

    @Autowired
    private IOwnerRepairBMO ownerRepairBMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMO;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMO;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "context", "未包含派单内容");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "amount", "未包含金额");
        Assert.hasKeyAndValue(reqJson, "feeFlag", "未包含费用标识");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        String userId = reqJson.getString("userId");
        String userName = reqJson.getString("userName");
        String publicArea = reqJson.getString("publicArea");
        //获取报修渠道
        String repairChannel = reqJson.getString("repairChannel");
        //获取维修类型
        String maintenanceType = reqJson.getString("maintenanceType");
        if (!reqJson.getString("outLowPrice").equals(reqJson.getString("outHighPrice"))
                && maintenanceType.equals("1001")) {
            //获取价格
            Double price = Double.parseDouble(reqJson.getString("price"));
            //获取最低价
            Double outLowPrice = Double.parseDouble(reqJson.getString("outLowPrice"));
            //获取最高价
            Double outHighPrice = Double.parseDouble(reqJson.getString("outHighPrice"));
            //物品价格应该在最低价和最高价之间
            if (price < outLowPrice || price > outHighPrice) {
                throw new IllegalArgumentException("输入的价格不正确，请重新输入！");
            }
        }
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setCommunityId(reqJson.getString("communityId"));
        repairUserDto.setState(RepairUserDto.STATE_DOING);
        repairUserDto.setStaffId(userId);
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        Assert.listOnlyOne(repairUserDtos, "当前用户没有需要处理订单");
        // 1.0 关闭自己订单
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(repairUserDtos.get(0).getRuId());
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_CLOSE);
        repairUserPo.setContext(reqJson.getString("context"));
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        super.update(context, repairUserPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR_USER);
        if (repairChannel.equals("Z") || (!StringUtil.isEmpty(maintenanceType) && maintenanceType.equals("1001"))) {  //如果是业主报修或者是有偿的就生成一条新状态，否则不变
            //2.0 给开始节点派支付单
            repairUserDto = new RepairUserDto();
            repairUserDto.setRepairId(reqJson.getString("repairId"));
            repairUserDto.setCommunityId(reqJson.getString("communityId"));
            repairUserDto.setRepairEvent(RepairUserDto.REPAIR_EVENT_START_USER);
            repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
            Assert.listOnlyOne(repairUserDtos, "数据错误 该订单没有发起人");
            repairUserPo = new RepairUserPo();
            repairUserPo.setRuId("-1");
            repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            if (maintenanceType.equals("1001")) { //如果是有偿的就走下面(业主报修有偿或者电话申请有偿或者员工报修有偿)
                repairUserPo.setState(RepairUserDto.STATE_PAY_FEE);
            } else {
                repairUserPo.setState(RepairUserDto.STATE_EVALUATE);
            }
            repairUserPo.setRepairId(reqJson.getString("repairId"));
            if (repairChannel.equals("Z")) {  //如果是业主端报修的走下面的方法
                repairUserPo.setStaffId(repairUserDtos.get(0).getStaffId());
                repairUserPo.setStaffName(repairUserDtos.get(0).getStaffName());
            } else { //如果不是业主报修，并且有偿
                RepairDto repairDto = new RepairDto();
                repairDto.setRepairId(reqJson.getString("repairId"));
                List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
                Assert.listOnlyOne(repairDtos, "数据错误 该用户没有报修单");
                //此时的报修对象ID即房屋ID
                String roomId = repairDtos.get(0).getRepairObjId();
                OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
                ownerRoomRelDto.setRoomId(roomId);
                //查询房屋业主关系表
                List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMO.queryOwnerRoomRels(ownerRoomRelDto);
                Assert.listOnlyOne(ownerRoomRelDtos, "信息错误");
                //获取业主id
                String ownerId = ownerRoomRelDtos.get(0).getOwnerId();
                OwnerDto ownerDto = new OwnerDto();
                ownerDto.setOwnerId(ownerId);
                //根据业主id查询业主信息
                List<OwnerDto> ownerDtos = ownerInnerServiceSMO.queryOwners(ownerDto);
                Assert.listOnlyOne(ownerDtos, "信息错误");
                //获取业主姓名
                String ownerName = ownerDtos.get(0).getName();
                repairUserPo.setStaffId(ownerId);
                repairUserPo.setStaffName(ownerName);
            }
            repairUserPo.setPreStaffId(userId);
            repairUserPo.setPreStaffName(userName);
            repairUserPo.setPreRuId(repairUserDtos.get(0).getRuId());
            repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_PAY_USER);
            repairUserPo.setContext("");
            repairUserPo.setCommunityId(reqJson.getString("communityId"));
            super.insert(context, repairUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_USER);
        }
        //维修前图片处理
        if (reqJson.containsKey("beforeRepairPhotos") && !StringUtils.isEmpty(reqJson.getString("beforeRepairPhotos"))) {
            JSONArray beforeRepairPhotos = reqJson.getJSONArray("beforeRepairPhotos");
            for (int _photoIndex = 0; _photoIndex < beforeRepairPhotos.size(); _photoIndex++) {
                FileDto fileDto = new FileDto();
                fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                fileDto.setFileName(fileDto.getFileId());
                fileDto.setContext(beforeRepairPhotos.getJSONObject(_photoIndex).getString("photo"));
                fileDto.setSuffix("jpeg");
                fileDto.setCommunityId(reqJson.getString("communityId"));
                String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
                reqJson.put("ownerPhotoId", fileDto.getFileId());
                reqJson.put("fileSaveName", fileName);
                JSONObject businessUnit = new JSONObject();
                businessUnit.put("fileRelId", "-" + (_photoIndex + 1));
                businessUnit.put("relTypeCd", FileRelDto.BEFORE_REPAIR_PHOTOS);
                businessUnit.put("saveWay", "ftp");
                businessUnit.put("objId", reqJson.getString("repairId"));
                businessUnit.put("fileRealName", fileName);
                businessUnit.put("fileSaveName", fileName);
                FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
                super.insert(context, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
            }
        }
        //维修后图片处理
        if (reqJson.containsKey("afterRepairPhotos") && !StringUtils.isEmpty(reqJson.getString("afterRepairPhotos"))) {
            JSONArray afterRepairPhotos = reqJson.getJSONArray("afterRepairPhotos");
            for (int _photoIndex = 0; _photoIndex < afterRepairPhotos.size(); _photoIndex++) {
                FileDto fileDto = new FileDto();
                fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                fileDto.setFileName(fileDto.getFileId());
                fileDto.setContext(afterRepairPhotos.getJSONObject(_photoIndex).getString("photo"));
                fileDto.setSuffix("jpeg");
                fileDto.setCommunityId(reqJson.getString("communityId"));
                String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
                reqJson.put("ownerFinishPhotoId", fileDto.getFileId());
                reqJson.put("fileFinishSaveName", fileName);
                JSONObject businessUnit = new JSONObject();
                businessUnit.put("fileRelId", "-" + (_photoIndex + 1));
                businessUnit.put("relTypeCd", FileRelDto.AFTER_REPAIR_PHOTOS);
                businessUnit.put("saveWay", "ftp");
                businessUnit.put("objId", reqJson.getString("repairId"));
                businessUnit.put("fileRealName", fileName);
                businessUnit.put("fileSaveName", fileName);
                FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
                super.insert(context, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
            }
        }
        if (publicArea.equals("F") && "1002".equals(reqJson.getString("maintenanceType"))) { //如果不是公共区域且是无偿的走下面
            //改变r_repair_pool表maintenance_type维修类型
            RepairPoolPo repairPoolPo = new RepairPoolPo();
            repairPoolPo.setRepairId(reqJson.getString("repairId"));
            repairPoolPo.setMaintenanceType(reqJson.getString("maintenanceType"));
            super.update(context, repairPoolPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR);
            if (repairChannel.equals("T") || repairChannel.equals("D")) { //如果是电话报修和员工代客报修结单后状态变为待回访
                ownerRepairBMOImpl.modifyBusinessRepairDispatch(reqJson, context, RepairDto.STATE_RETURN_VISIT);
            } else if (repairChannel.equals("Z")) { //如果是业主自主报修结单后状态变为待评价
                ownerRepairBMOImpl.modifyBusinessRepairDispatch(reqJson, context, RepairDto.STATE_APPRAISE);
            }
        } else if (publicArea.equals("F") && "1001".equals(reqJson.getString("maintenanceType"))) { //如果不是公共区域且是有偿的走下面
            //3.0 生成支付费用
            //查询默认费用项
            FeeConfigDto feeConfigDto = new FeeConfigDto();
            feeConfigDto.setCommunityId(reqJson.getString("communityId"));
            feeConfigDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_REPAIR);
            feeConfigDto.setIsDefault(FeeConfigDto.DEFAULT_FEE_CONFIG);
            List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
            Assert.listOnlyOne(feeConfigDtos, "默认维修费用有多条或不存在");
            PayFeePo feePo = new PayFeePo();
            feePo.setAmount(reqJson.getString("price"));
            feePo.setCommunityId(reqJson.getString("communityId"));
            feePo.setConfigId(feeConfigDtos.get(0).getConfigId());
            feePo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            feePo.setFeeFlag(feeConfigDtos.get(0).getFeeFlag());
            feePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            feePo.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_REPAIR);
            feePo.setIncomeObjId(reqJson.getString("storeId"));
            feePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            RepairDto repairDto = new RepairDto();
            repairDto.setCommunityId(reqJson.getString("communityId"));
            repairDto.setRepairId(reqJson.getString("repairId"));
            List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
            Assert.listOnlyOne(repairDtos, "维修单有多条或不存在");
            feePo.setPayerObjId(repairDtos.get(0).getRepairObjId());
            feePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            feePo.setState(FeeDto.STATE_DOING);
            feePo.setUserId(userId);
            super.insert(context, feePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setAttrId("-1");
            feeAttrPo.setFeeId(feePo.getFeeId());
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_REPAIR);
            feeAttrPo.setCommunityId(reqJson.getString("communityId"));
            feeAttrPo.setValue(reqJson.getString("repairId"));
            super.insert(context, feeAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
            //改变r_repair_pool表maintenance_type维修类型
            RepairPoolPo repairPoolPo = new RepairPoolPo();
            repairPoolPo.setRepairId(reqJson.getString("repairId"));
            repairPoolPo.setMaintenanceType(reqJson.getString("maintenanceType"));
            super.update(context, repairPoolPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR);
            ownerRepairBMOImpl.modifyBusinessRepairDispatch(reqJson, context, RepairDto.STATE_PAY);
        } else if (publicArea.equals("T")) {  //公共区域走这里
            if (repairChannel.equals("T") || repairChannel.equals("D")) { //如果是电话报修和员工代客报修结单后状态变为待回访
                ownerRepairBMOImpl.modifyBusinessRepairDispatch(reqJson, context, RepairDto.STATE_RETURN_VISIT);
            } else if (repairChannel.equals("Z")) { //如果是业主自主报修结单后状态变为待评价
                ownerRepairBMOImpl.modifyBusinessRepairDispatch(reqJson, context, RepairDto.STATE_APPRAISE);
            }
        }
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);
        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeRepairDispatchStepConstant.BINDING_REPAIR_FINISH;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IRepairInnerServiceSMO getRepairInnerServiceSMOImpl() {
        return repairInnerServiceSMOImpl;
    }

    public void setRepairInnerServiceSMOImpl(IRepairInnerServiceSMO repairInnerServiceSMOImpl) {
        this.repairInnerServiceSMOImpl = repairInnerServiceSMOImpl;
    }
}
