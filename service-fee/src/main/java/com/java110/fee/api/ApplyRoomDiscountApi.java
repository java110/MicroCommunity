package com.java110.fee.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountDto;
import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountTypeDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDiscountRuleDto;
import com.java110.fee.bmo.account.IUpdateAccountBMO;
import com.java110.fee.bmo.applyRoomDiscount.IAuditApplyRoomDiscountBMO;
import com.java110.fee.bmo.applyRoomDiscount.IDeleteApplyRoomDiscountBMO;
import com.java110.fee.bmo.applyRoomDiscount.IGetApplyRoomDiscountBMO;
import com.java110.fee.bmo.applyRoomDiscount.ISaveApplyRoomDiscountBMO;
import com.java110.fee.bmo.applyRoomDiscount.IUpdateApplyRoomDiscountBMO;
import com.java110.fee.bmo.applyRoomDiscountType.IDeleteApplyRoomDiscountTypeBMO;
import com.java110.fee.bmo.applyRoomDiscountType.IGetApplyRoomDiscountTypeBMO;
import com.java110.fee.bmo.applyRoomDiscountType.ISaveApplyRoomDiscountTypeBMO;
import com.java110.fee.bmo.applyRoomDiscountType.IUpdateApplyRoomDiscountTypeBMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.fee.IApplyRoomDiscountInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeDiscountRuleInnerServiceSMO;
import com.java110.po.applyRoomDiscount.ApplyRoomDiscountPo;
import com.java110.po.applyRoomDiscountType.ApplyRoomDiscountTypePo;
import com.java110.po.file.FileRelPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/applyRoomDiscount")
public class ApplyRoomDiscountApi {

    private static final String SPEC_RATE = "89002020980013"; // 打折率

    @Autowired
    private ISaveApplyRoomDiscountBMO saveApplyRoomDiscountBMOImpl;

    @Autowired
    private IUpdateApplyRoomDiscountBMO updateApplyRoomDiscountBMOImpl;

    @Autowired
    private IDeleteApplyRoomDiscountBMO deleteApplyRoomDiscountBMOImpl;

    @Autowired
    private IGetApplyRoomDiscountBMO getApplyRoomDiscountBMOImpl;

    @Autowired
    private IAuditApplyRoomDiscountBMO auditApplyRoomDiscountBMOImpl;

    @Autowired
    private ISaveApplyRoomDiscountTypeBMO saveApplyRoomDiscountTypeBMOImpl;

    @Autowired
    private IUpdateApplyRoomDiscountTypeBMO updateApplyRoomDiscountTypeBMOImpl;

    @Autowired
    private IDeleteApplyRoomDiscountTypeBMO deleteApplyRoomDiscountTypeBMOImpl;

    @Autowired
    private IGetApplyRoomDiscountTypeBMO getApplyRoomDiscountTypeBMOImpl;

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountRuleInnerServiceSMO feeDiscountRuleInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IUpdateAccountBMO updateAccountBMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    /**
     * 优惠申请
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/saveApplyRoomDiscount
     * @path /app/applyRoomDiscount/saveApplyRoomDiscount
     */
    @RequestMapping(value = "/saveApplyRoomDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> saveApplyRoomDiscount(@RequestBody JSONObject reqJson) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        Assert.hasKeyAndValue(reqJson, "roomName", "请求报文中未包含roomName");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "applyType", "请求报文中未包含applyType");
        if (!StringUtil.isEmpty(reqJson.getString("startTime"))) {
            String startTime = reqJson.getString("startTime") + " 00:00:00";
            reqJson.put("startTime", startTime);
        }
        if (!StringUtil.isEmpty(reqJson.getString("endTime"))) {
            String endTime = reqJson.getString("endTime") + " 23:59:59";
            reqJson.put("endTime", endTime);
        }
        ApplyRoomDiscountPo applyRoomDiscountPo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountPo.class);
        ApplyRoomDiscountDto applyRoomDiscountDto = new ApplyRoomDiscountDto();
        applyRoomDiscountDto.setCommunityId(applyRoomDiscountPo.getCommunityId());
        applyRoomDiscountDto.setRoomId(applyRoomDiscountPo.getRoomId());
        applyRoomDiscountDto.setFeeId(applyRoomDiscountPo.getFeeId());
        //查询折扣申请表中该房屋下符合条件的最新的一条数据
        List<ApplyRoomDiscountDto> applyRoomDiscountDtos = applyRoomDiscountInnerServiceSMOImpl.queryFirstApplyRoomDiscounts(applyRoomDiscountDto);
        //获取本次填写的开始时间
        Date startDate = simpleDateFormat.parse(applyRoomDiscountPo.getStartTime());
        if (applyRoomDiscountDtos.size() == 0) {
            //空置房优惠可用
            applyRoomDiscountPo.setInUse("0");
            applyRoomDiscountPo.setArdId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ardId));
            applyRoomDiscountPo.setState(ApplyRoomDiscountDto.STATE_APPLY);
            saveFile(applyRoomDiscountPo);
            return saveApplyRoomDiscountBMOImpl.save(applyRoomDiscountPo);
        } else if (applyRoomDiscountDtos.size() > 0) {
            //取出结束时间
            String endTime = applyRoomDiscountDtos.get(0).getEndTime();
            Date finishTime = simpleDateFormat.parse(endTime);
            if (startDate.getTime() - finishTime.getTime() >= 0) {
                //空置房优惠可用
                applyRoomDiscountPo.setInUse("0");
                applyRoomDiscountPo.setArdId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ardId));
                applyRoomDiscountPo.setState(ApplyRoomDiscountDto.STATE_APPLY);
                saveFile(applyRoomDiscountPo);
                return saveApplyRoomDiscountBMOImpl.save(applyRoomDiscountPo);
            } else {
                throw new UnsupportedOperationException("该时间段已经申请过空置房，请重新输入空置房申请开始和结束时间");
            }
        } else {
            throw new UnsupportedOperationException("信息错误");
        }
    }

    /**
     * 上传图片
     *
     * @param applyRoomDiscountPo
     */
    public void saveFile(ApplyRoomDiscountPo applyRoomDiscountPo) {
        //获取图片
        List<String> photos = applyRoomDiscountPo.getPhotos();
        FileRelPo fileRelPo = new FileRelPo();
        fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
        fileRelPo.setObjId(applyRoomDiscountPo.getArdId());
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
    }

    /**
     * 验房接口
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/updateApplyRoomDiscount
     * @path /app/applyRoomDiscount/updateApplyRoomDiscount
     */
    @RequestMapping(value = "/updateApplyRoomDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> updateApplyRoomDiscount(@RequestBody JSONObject reqJson, @RequestHeader(value = "user-id") String userId) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含验房状态");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含结束时间");
        Assert.hasKeyAndValue(reqJson, "checkRemark", "请求报文中未包含验房说明");
        Assert.hasKeyAndValue(reqJson, "ardId", "ardId不能为空");
        ApplyRoomDiscountDto applyRoomDiscountDto = new ApplyRoomDiscountDto();
        applyRoomDiscountDto.setArdId(reqJson.getString("ardId"));
        //查询房屋优惠申请信息
        List<ApplyRoomDiscountDto> applyRoomDiscountDtos = applyRoomDiscountInnerServiceSMOImpl.queryApplyRoomDiscounts(applyRoomDiscountDto);
        Assert.listOnlyOne(applyRoomDiscountDtos, "查询房屋优惠信息错误！");
        //获取房屋优惠审核状态
        String state = applyRoomDiscountDtos.get(0).getState();
        if (!StringUtil.isEmpty(state) && !state.equals("1")) {
            throw new IllegalArgumentException("该房屋已验过房，无法再次进行验房！");
        }
        reqJson.put("checkUserId", userId);
        ApplyRoomDiscountPo applyRoomDiscountPo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountPo.class);
        return updateApplyRoomDiscountBMOImpl.update(applyRoomDiscountPo);
    }

    /**
     * 修改优惠申请
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/editApplyRoomDiscount
     * @path /app/applyRoomDiscount/editApplyRoomDiscount
     */
    @RequestMapping(value = "/editApplyRoomDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> editApplyRoomDiscount(@RequestBody JSONObject reqJson, @RequestHeader(value = "user-id") String userId) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含验房状态");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含结束时间");
        Assert.hasKeyAndValue(reqJson, "ardId", "ardId不能为空");
        ApplyRoomDiscountPo applyRoomDiscountPo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountPo.class);
        return updateApplyRoomDiscountBMOImpl.update(applyRoomDiscountPo);
    }

    /**
     * 审批接口
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/updateReviewApplyRoomDiscount
     * @path /app/applyRoomDiscount/updateReviewApplyRoomDiscount
     */
    @RequestMapping(value = "/updateReviewApplyRoomDiscount", method = RequestMethod.POST)
    @Java110Transactional
    public ResponseEntity<String> updateReviewApplyRoomDiscount(@RequestBody JSONObject reqJson, @RequestHeader(value = "user-id") String userId, DataFlowContext dataFlowContext) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含验房状态");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含结束时间");
        Assert.hasKeyAndValue(reqJson, "reviewRemark", "请求报文中未包含验房说明");
        Assert.hasKeyAndValue(reqJson, "ardId", "ardId不能为空");
        ApplyRoomDiscountDto applyRoomDiscountDto = new ApplyRoomDiscountDto();
        applyRoomDiscountDto.setArdId(reqJson.getString("ardId"));
        //查询房屋优惠申请信息
        List<ApplyRoomDiscountDto> applyRoomDiscountDtos = applyRoomDiscountInnerServiceSMOImpl.queryApplyRoomDiscounts(applyRoomDiscountDto);
        Assert.listOnlyOne(applyRoomDiscountDtos, "查询房屋优惠信息错误！");
        //获取房屋优惠审核状态
        String state = applyRoomDiscountDtos.get(0).getState();
        String stateNow = reqJson.getString("state");
        String returnWay = reqJson.getString("returnWay");
        if (!StringUtil.isEmpty(state) && state.equals("4")) {
            throw new IllegalArgumentException("该房屋已审核过，无法再次进行审核！");
        }
        if (reqJson.containsKey("selectedFees") && !StringUtil.isEmpty(reqJson.getString("selectedFees")) && stateNow.equals("4") && "1002".equals(returnWay)) {
            //获取前端传来的缴费历史
            String selectedFees = reqJson.getString("selectedFees");
            JSONArray feeDetailIds = JSON.parseArray(selectedFees);
            //获取优惠
            JSONArray discounts = reqJson.getJSONArray("discounts");
            BigDecimal cashBackAmount = new BigDecimal("0.00");//返现总金额
            for (int i = 0; i < discounts.size(); i++) {
                JSONObject discountObject = discounts.getJSONObject(i);
                if (!reqJson.getString("discountId").equals(discountObject.getString("discountId"))) {
                    continue;
                }
                JSONArray feeDiscountSpecs = discountObject.getJSONArray("feeDiscountSpecs");
                //获取规则id
                String ruleId = discounts.getJSONObject(i).getString("ruleId");
                FeeDiscountRuleDto feeDiscountRuleDto = new FeeDiscountRuleDto();
                feeDiscountRuleDto.setRuleId(ruleId);
                List<FeeDiscountRuleDto> feeDiscountRuleDtos = feeDiscountRuleInnerServiceSMOImpl.queryFeeDiscountRules(feeDiscountRuleDto);
                Assert.listOnlyOne(feeDiscountRuleDtos, "查询折扣规则错误！");
                //获取折扣类型(1: 打折  2:减免  3:滞纳金  4:空置房打折  5:空置房减免)
                String discountSmallType = feeDiscountRuleDtos.get(0).getDiscountSmallType();
                //获取规则
                //String specValue = feeDiscountSpecs.getJSONObject(1).getString("specValue");
                String specValue = getRateSpecValueByFeeDiscountSpecs(feeDiscountSpecs);
                if (!StringUtil.isEmpty(discountSmallType) && (discountSmallType.equals("1") || discountSmallType.equals("4"))) { //打折
                    for (int index = 0; index < feeDetailIds.size(); index++) {
                        String feeDetailId = String.valueOf(feeDetailIds.get(index));
                        FeeDetailDto feeDetailDto = new FeeDetailDto();
                        feeDetailDto.setDetailId(feeDetailId);
                        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
                        Assert.listOnlyOne(feeDetailDtos, "查询费用明细表错误！");

                        BigDecimal receivedAmount = new BigDecimal(feeDetailDtos.get(0).getReceivedAmount());//获取实收金额
                        BigDecimal spec = new BigDecimal(specValue);//折扣
                        //计算打折后的实收金额
                        BigDecimal money = receivedAmount.multiply(spec);
                        cashBackAmount = cashBackAmount.add(receivedAmount.subtract(money)); //计算优惠的金额
                    }
                } else if (!StringUtil.isEmpty(discountSmallType) && (discountSmallType.equals("2") || discountSmallType.equals("5"))) { //减免
                    for (int index = 0; index < feeDetailIds.size(); index++) {
                        String feeDetailId = String.valueOf(feeDetailIds.get(index));
                        FeeDetailDto feeDetailDto = new FeeDetailDto();
                        feeDetailDto.setDetailId(feeDetailId);
                        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
                        Assert.listOnlyOne(feeDetailDtos, "查询费用明细表错误！");

                        BigDecimal spec = new BigDecimal(specValue);//减免金額
                        cashBackAmount = cashBackAmount.add(spec); //计算优惠的金额
                    }
                }
                DecimalFormat df = new DecimalFormat("0.00");
                reqJson.put("cashBackAmount", df.format(cashBackAmount));
                //处理账户返现
                JSONArray businesses = new JSONArray();
                updateAccountBMOImpl.cashBackAccount(reqJson, dataFlowContext, businesses);
                reqJson.put("inUse", 1);
                reqJson.put("returnAmount", df.format(cashBackAmount));
            }
        } else {
            reqJson.put("inUse", 0);
        }
        reqJson.put("reviewUserId", userId);
        ApplyRoomDiscountPo applyRoomDiscountPo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountPo.class);
        return updateApplyRoomDiscountBMOImpl.update(applyRoomDiscountPo);
    }

    /**
     * 89002020980001	102020001	月份
     * 89002020980002	102020001	打折率
     * 89002020980003	102020002	月份
     * 89002020980004	102020002	减免金额
     * 89002020980005	102020003	滞纳金
     * 89002020980006	102020004	滞纳金
     * 89002020980007	102020005	月份
     * 89002020980008	102020005	打折率
     * 89002020980009	102020005	欠费时长
     * 89002020980010	102020006	月份
     * 89002020980011	102020006	减免金额
     * 89002020980012	102020007	月份
     * 89002020980013	102020007	打折率
     *
     * @param feeDiscountSpecs
     * @return
     */
    private String getRateSpecValueByFeeDiscountSpecs(JSONArray feeDiscountSpecs) {

        for (int specIndex = 0; specIndex < feeDiscountSpecs.size(); specIndex++) {
            if (SPEC_RATE.equals(feeDiscountSpecs.getJSONObject(specIndex).getString("specId"))) {
                return feeDiscountSpecs.getJSONObject(specIndex).getString("specValue");
            }
            if ("89002020980002".equals(feeDiscountSpecs.getJSONObject(specIndex).getString("specId"))) {
                return feeDiscountSpecs.getJSONObject(specIndex).getString("specValue");
            }
            if ("89002020980004".equals(feeDiscountSpecs.getJSONObject(specIndex).getString("specId"))) {
                return feeDiscountSpecs.getJSONObject(specIndex).getString("specValue");
            }
            if ("89002020980008".equals(feeDiscountSpecs.getJSONObject(specIndex).getString("specId"))) {
                return feeDiscountSpecs.getJSONObject(specIndex).getString("specValue");
            }
            if ("89002020980011".equals(feeDiscountSpecs.getJSONObject(specIndex).getString("specId"))) {
                return feeDiscountSpecs.getJSONObject(specIndex).getString("specValue");
            }
        }

        throw new IllegalArgumentException("未找到 打折系数");
    }

    /**
     * 验房接口
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/auditApplyRoomDiscount
     * @path /app/applyRoomDiscount/auditApplyRoomDiscount
     */
    @RequestMapping(value = "/auditApplyRoomDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> auditApplyRoomDiscount(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含验房状态");
        Assert.hasKeyAndValue(reqJson, "reviewRemark", "请求报文中未包含审核说明");
        Assert.hasKeyAndValue(reqJson, "ardId", "ardId不能为空");
        ApplyRoomDiscountPo applyRoomDiscountPo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountPo.class);
        return auditApplyRoomDiscountBMOImpl.audit(applyRoomDiscountPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/deleteApplyRoomDiscount
     * @path /app/applyRoomDiscount/deleteApplyRoomDiscount
     */
    @RequestMapping(value = "/deleteApplyRoomDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> deleteApplyRoomDiscount(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "ardId", "ardId不能为空");
        ApplyRoomDiscountPo applyRoomDiscountPo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountPo.class);
        return deleteApplyRoomDiscountBMOImpl.delete(applyRoomDiscountPo);
    }

    /**
     * 查询优惠申请
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /applyRoomDiscount/queryApplyRoomDiscount
     * @path /app/applyRoomDiscount/queryApplyRoomDiscount
     */
    @RequestMapping(value = "/queryApplyRoomDiscount", method = RequestMethod.GET)
    public ResponseEntity<String> queryApplyRoomDiscount(@RequestParam(value = "communityId") String communityId,
                                                         @RequestParam(value = "ardId", required = false) String ardId,
                                                         @RequestParam(value = "roomName", required = false) String roomName,
                                                         @RequestParam(value = "roomId", required = false) String roomId,
                                                         @RequestParam(value = "state", required = false) String state,
                                                         @RequestParam(value = "applyType", required = false) String applyType,
                                                         @RequestParam(value = "feeId", required = false) String feeId,
                                                         @RequestParam(value = "page") int page,
                                                         @RequestParam(value = "row") int row) {
        ApplyRoomDiscountDto applyRoomDiscountDto = new ApplyRoomDiscountDto();
        applyRoomDiscountDto.setArdId(ardId);
        applyRoomDiscountDto.setPage(page);
        applyRoomDiscountDto.setRow(row);
        applyRoomDiscountDto.setCommunityId(communityId);
        applyRoomDiscountDto.setRoomName(roomName);
        applyRoomDiscountDto.setRoomId(roomId);
        applyRoomDiscountDto.setState(state);
        applyRoomDiscountDto.setApplyType(applyType);
        applyRoomDiscountDto.setFeeId(feeId);
        return getApplyRoomDiscountBMOImpl.get(applyRoomDiscountDto);
    }

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/saveApplyRoomDiscountType
     * @path /app/applyRoomDiscount/saveApplyRoomDiscountType
     */
    @RequestMapping(value = "/saveApplyRoomDiscountType", method = RequestMethod.POST)
    public ResponseEntity<String> saveApplyRoomDiscountType(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "typeName", "请求报文中未包含typeName");
        ApplyRoomDiscountTypePo applyRoomDiscountTypePo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountTypePo.class);
        return saveApplyRoomDiscountTypeBMOImpl.save(applyRoomDiscountTypePo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/updateApplyRoomDiscountType
     * @path /app/applyRoomDiscount/updateApplyRoomDiscountType
     */
    @RequestMapping(value = "/updateApplyRoomDiscountType", method = RequestMethod.POST)
    public ResponseEntity<String> updateApplyRoomDiscountType(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "typeName", "请求报文中未包含typeName");
        Assert.hasKeyAndValue(reqJson, "applyType", "applyType不能为空");
        ApplyRoomDiscountTypePo applyRoomDiscountTypePo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountTypePo.class);
        return updateApplyRoomDiscountTypeBMOImpl.update(applyRoomDiscountTypePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/deleteApplyRoomDiscountType
     * @path /app/applyRoomDiscount/deleteApplyRoomDiscountType
     */
    @RequestMapping(value = "/deleteApplyRoomDiscountType", method = RequestMethod.POST)
    public ResponseEntity<String> deleteApplyRoomDiscountType(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "applyType", "applyType不能为空");
        ApplyRoomDiscountTypePo applyRoomDiscountTypePo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountTypePo.class);
        return deleteApplyRoomDiscountTypeBMOImpl.delete(applyRoomDiscountTypePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /applyRoomDiscount/queryApplyRoomDiscountType
     * @path /app/applyRoomDiscount/queryApplyRoomDiscountType
     */
    @RequestMapping(value = "/queryApplyRoomDiscountType", method = RequestMethod.GET)
    public ResponseEntity<String> queryApplyRoomDiscountType(@RequestParam(value = "communityId") String communityId,
                                                             @RequestParam(value = "applyType", required = false) String applyType,
                                                             @RequestParam(value = "typeName", required = false) String typeName,
                                                             @RequestParam(value = "page") int page,
                                                             @RequestParam(value = "row") int row) {
        ApplyRoomDiscountTypeDto applyRoomDiscountTypeDto = new ApplyRoomDiscountTypeDto();
        applyRoomDiscountTypeDto.setPage(page);
        applyRoomDiscountTypeDto.setRow(row);
        applyRoomDiscountTypeDto.setCommunityId(communityId);
        applyRoomDiscountTypeDto.setApplyType(applyType);
        applyRoomDiscountTypeDto.setTypeName(typeName);
        return getApplyRoomDiscountTypeBMOImpl.get(applyRoomDiscountTypeDto);
    }
}
