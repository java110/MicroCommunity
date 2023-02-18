package com.java110.job.printer.manufactor;

import com.java110.core.log.LoggerFactory;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.feeReceipt.FeeReceiptDetailDto;
import com.java110.dto.feeReceipt.FeeReceiptDto;
import com.java110.dto.machinePrinter.MachinePrinterDto;
import com.java110.intf.fee.*;
import com.java110.job.printer.IPrinter;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 飞蛾厂家云打印机
 * <p>
 * http://www.feieyun.com/open/apidoc-cn.html
 */

@Service("feieManufactor")
public class FeieManufactor implements IPrinter {

    private Logger logger = LoggerFactory.getLogger(FeieManufactor.class);

    @Autowired
    private RestTemplate formRestTemplate;

    /**
     * 请求地址
     */
    public static final String REQUEST_URL = "http://api.de.feieyun.com/Api/Open/";

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;
    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    /**
     * **************************
     * 单号：832023020800440026
     * 房号：1-1-1001
     * 业主：李四1
     * 时间：2023-02-08 14:30:17
     * **************************
     * 收费项目：高层物业费
     * 收费范围：2024-01-01 至2024-12-31
     * 单价/固定费：1.3/0.0
     * 面积/用量：90.00
     * 金额： 1404.00
     * 备注：123
     * **************************
     * 总计：1404
     * **************************
     *
     * @param detailIds
     * @param communityId
     * @param quantity
     * @return
     */
    @Override
    public ResultVo printPayFeeDetail(String[] detailIds, String communityId, int quantity, MachinePrinterDto machinePrinterDto) {

        String printStr = "";
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setDetailId(detailIds[0]);
        feeDetailDto.setCommunityId(communityId);
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
        Assert.listOnlyOne(feeDetailDtos, "交费明细不存在");

//        FeeDto feeDto = new FeeDto();
//        feeDto.setFeeId(feeDetailDtos.get(0).getFeeId());
//        feeDto.setCommunityId(communityId);
//        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
//
//        Assert.listOnlyOne(feeDtos, "费用不存在");

        FeeReceiptDetailDto feeReceiptDetailDto = new FeeReceiptDetailDto();
        feeReceiptDetailDto.setDetailIds(detailIds);
        feeReceiptDetailDto.setCommunityId(communityId);
        List<FeeReceiptDetailDto> feeReceiptDetailDtos = feeReceiptDetailInnerServiceSMOImpl.queryFeeReceiptDetails(feeReceiptDetailDto);
        if(feeReceiptDetailDtos == null || feeReceiptDetailDtos.size() < 1){
            throw new CmdException("未生成收据");
        }

        FeeReceiptDto feeReceiptDto = new FeeReceiptDto();
        feeReceiptDto.setReceiptId(feeReceiptDetailDtos.get(0).getReceiptId());
        feeReceiptDto.setCommunityId(communityId);
        List<FeeReceiptDto> feeReceiptDtos = feeReceiptInnerServiceSMOImpl.queryFeeReceipts(feeReceiptDto);
        Assert.listOnlyOne(feeReceiptDtos,"未生成收据");

        /*************************************头部******************************************/
        List<FeieLine> feieLines = new ArrayList<>();
        feieLines.add(new FeieLine("单号",feeReceiptDetailDtos.get(0).getReceiptId()));
        feieLines.add(new FeieLine("房号",feeReceiptDtos.get(0).getObjName()));
        feieLines.add(new FeieLine("业主",feeReceiptDtos.get(0).getPayObjName()));
        feieLines.add(new FeieLine("时间", DateUtil.getFormatTimeString(DateUtil.getCurrentDate(),DateUtil.DATE_FORMATE_STRING_A)));
        printStr = getPrintPayFeeDetailHeaderContent(feieLines);
        /*************************************头部******************************************/

        feieLines = new ArrayList<>();
        Date startTime = null;
        Date endTime = null;
        Calendar endTimeCal = null;
        BigDecimal totalDecimal = new BigDecimal(0);
        for(FeeReceiptDetailDto tmpFeeReceiptDetailDto: feeReceiptDetailDtos){
            feieLines.add(new FeieLine("收费项目",tmpFeeReceiptDetailDto.getFeeName()));
            startTime = DateUtil.getDateFromStringB(tmpFeeReceiptDetailDto.getStartTime());
            endTime = DateUtil.getDateFromStringB(tmpFeeReceiptDetailDto.getEndTime());
            //周期性和间接性
            if(!FeeDto.FEE_FLAG_ONCE.equals(tmpFeeReceiptDetailDto.getFeeFlag())){
                endTimeCal = Calendar.getInstance();
                endTimeCal.setTime(endTime);
                endTimeCal.add(Calendar.MONTH,-1);
                endTime.getTime();
            }
            feieLines.add(new FeieLine("收费范围",DateUtil.getFormatTimeString(startTime,DateUtil.DATE_FORMATE_STRING_B)+"至"+DateUtil.getFormatTimeString(endTime,DateUtil.DATE_FORMATE_STRING_B)));
            feieLines.add(new FeieLine("单价/固定费",tmpFeeReceiptDetailDto.getSquarePrice()));
            feieLines.add(new FeieLine("面积/用量",tmpFeeReceiptDetailDto.getArea()));
            feieLines.add(new FeieLine("金额",tmpFeeReceiptDetailDto.getAmount()));
            feieLines.add(new FeieLine("备注",tmpFeeReceiptDetailDto.getRemark()));
            printStr += getPrintPayFeeDetailBodyContent(feieLines);

            totalDecimal = totalDecimal.add(new BigDecimal(Double.parseDouble(tmpFeeReceiptDetailDto.getAmount())));

        }
        printStr += getPrintPayFeeDetailFloorContent(totalDecimal.doubleValue());

        String stime = String.valueOf(System.currentTimeMillis() / 1000);
        String user =  MappingCache.getValue(MappingConstant.URL_DOMAIN,"user");
        String ukey =  MappingCache.getValue(MappingConstant.URL_DOMAIN,"ukey");

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("user", MappingCache.getValue(MappingConstant.URL_DOMAIN,"user"));
        postParameters.add("stime", stime);
        postParameters.add("sig", signature(user,ukey,stime));
        postParameters.add("apiname", "Open_printMsg");
        postParameters.add("sn", machinePrinterDto.getMachineCode());
        postParameters.add("content", printStr);
        postParameters.add("times", quantity);

        //添加人脸
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters, httpHeaders);
        ResponseEntity<String> responseEntity = formRestTemplate.exchange(REQUEST_URL, HttpMethod.POST, httpEntity, String.class);
        logger.debug("请求信息 ： " + httpEntity + "，返回信息:" + responseEntity);


        return new ResultVo(ResultVo.CODE_OK, "成功");
    }

    public static String getPrintPayFeeDetailHeaderContent(List<FeieLine> order) {
        String orderInfo = "<CB>缴费通知单</CB><BR>";
        orderInfo += "********************************<BR>";
        for (int i = 0; i < order.size(); i++) {
            String title = order.get(i).getTitle();
            String value = order.get(i).getValue();
            orderInfo += (title + ":" + value + "<BR>");
        }
        orderInfo += "********************************<BR>";
        return orderInfo;
    }

    public static String getPrintPayFeeDetailBodyContent(List<FeieLine> business) {

        String orderInfo = "";
        for (int i = 0; i < business.size(); i++) {
            String title = business.get(i).getTitle();
            String value = business.get(i).getValue();
            orderInfo += (title + ":" + value + "<BR>");
        }
        orderInfo += "********************************<BR>";
        return orderInfo;
    }

    public static String getPrintPayFeeDetailFloorContent(double totals) {
        String orderInfo = "";
        orderInfo += "********************************<BR>";
        orderInfo += "合计：" + totals + "元<BR>";
        orderInfo += "<QR>http://www.feieyun.com</QR>";
        return orderInfo;
    }


    @Override
    public ResultVo printRepair(String repairUserId, String communityId, int quantity, MachinePrinterDto machinePrinterDto) {

        return new ResultVo(ResultVo.CODE_OK, "成功");
    }

    private static String signature(String USER, String UKEY, String STIME) {
        return DigestUtils.sha1Hex(USER + UKEY + STIME);
    }
}
