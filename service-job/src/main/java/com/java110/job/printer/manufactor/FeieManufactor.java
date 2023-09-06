package com.java110.job.printer.manufactor;

import com.java110.core.client.RestTemplate;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.account.AccountReceiptDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.FeeReceiptDetailDto;
import com.java110.dto.fee.FeeReceiptDto;
import com.java110.dto.machine.MachinePrinterDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.job.printer.IPrinter;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
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
     * http://api.feieyun.cn/Api/Open/
     */
    public static final String REQUEST_URL = "http://api.feieyun.cn/Api/Open/";

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;
    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMO;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMO;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IAccountReceiptV1InnerServiceSMO accountReceiptV1InnerServiceSMOImpl;

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
    public ResultVo printPayFeeDetail(String[] detailIds, String communityId, int quantity, MachinePrinterDto machinePrinterDto, String staffName) {

        String printStr = "";
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setDetailId(detailIds[0]);
        feeDetailDto.setCommunityId(communityId);
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
        Assert.listOnlyOne(feeDetailDtos, "交费明细不存在");

        FeeReceiptDetailDto feeReceiptDetailDto = new FeeReceiptDetailDto();
        feeReceiptDetailDto.setDetailIds(detailIds);
        feeReceiptDetailDto.setCommunityId(communityId);
        List<FeeReceiptDetailDto> feeReceiptDetailDtos = feeReceiptDetailInnerServiceSMOImpl.queryFeeReceiptDetails(feeReceiptDetailDto);
        if (feeReceiptDetailDtos == null || feeReceiptDetailDtos.size() < 1) {
            throw new CmdException("未生成收据");
        }

        FeeReceiptDto feeReceiptDto = new FeeReceiptDto();
        feeReceiptDto.setReceiptId(feeReceiptDetailDtos.get(0).getReceiptId());
        feeReceiptDto.setCommunityId(communityId);
        List<FeeReceiptDto> feeReceiptDtos = feeReceiptInnerServiceSMOImpl.queryFeeReceipts(feeReceiptDto);
        Assert.listOnlyOne(feeReceiptDtos, "未生成收据");

        /*************************************头部******************************************/
        List<FeieLine> feieLines = new ArrayList<>();
        feieLines.add(new FeieLine("收据编号", feeReceiptDtos.get(0).getReceiptCode()));
        feieLines.add(new FeieLine("单号", feeReceiptDetailDtos.get(0).getPayOrderId()));
        feieLines.add(new FeieLine("房号", feeReceiptDtos.get(0).getObjName()));
        feieLines.add(new FeieLine("业主", feeReceiptDtos.get(0).getPayObjName()));
        feieLines.add(new FeieLine("时间", DateUtil.getFormatTimeString(DateUtil.getCurrentDate(), DateUtil.DATE_FORMATE_STRING_A)));
        printStr = FeieGetPayFeeDetail.getPrintPayFeeDetailHeaderContent(feieLines);
        /*************************************头部******************************************/

        feieLines = new ArrayList<>();
        Date startTime = null;
        Date endTime = null;
        Calendar endTimeCal = null;
        BigDecimal totalDecimal = new BigDecimal(0);
        for (FeeReceiptDetailDto tmpFeeReceiptDetailDto : feeReceiptDetailDtos) {
            feieLines.add(new FeieLine("收费项目", tmpFeeReceiptDetailDto.getFeeName()));
            startTime = DateUtil.getDateFromStringB(tmpFeeReceiptDetailDto.getStartTime());
            endTime = DateUtil.getDateFromStringB(tmpFeeReceiptDetailDto.getEndTime());
            //周期性和间接性
            if (!FeeDto.FEE_FLAG_ONCE.equals(tmpFeeReceiptDetailDto.getFeeFlag())) {
                endTimeCal = Calendar.getInstance();
                endTimeCal.setTime(endTime);
                endTimeCal.add(Calendar.DAY_OF_MONTH, -1);
                endTime = endTimeCal.getTime();
            }
            feieLines.add(new FeieLine("收费范围", DateUtil.getFormatTimeString(startTime, DateUtil.DATE_FORMATE_STRING_B) + "至" + DateUtil.getFormatTimeString(endTime, DateUtil.DATE_FORMATE_STRING_B)));
            feieLines.add(new FeieLine("单价/固定费", tmpFeeReceiptDetailDto.getSquarePrice()));
            feieLines.add(new FeieLine("面积/用量", tmpFeeReceiptDetailDto.getArea()));
            feieLines.add(new FeieLine("支付方式", tmpFeeReceiptDetailDto.getPrimeRate()));
            if (!StringUtil.isEmpty(tmpFeeReceiptDetailDto.getPreDegrees())) {
                feieLines.add(new FeieLine("表读数", tmpFeeReceiptDetailDto.getPreDegrees() + "至" + tmpFeeReceiptDetailDto.getCurDegrees()));
            }
            feieLines.add(new FeieLine("金额", tmpFeeReceiptDetailDto.getAmount()));
            feieLines.add(new FeieLine("备注", tmpFeeReceiptDetailDto.getRemark()));
            printStr += FeieGetPayFeeDetail.getPrintPayFeeDetailBodyContent(feieLines);
            totalDecimal = totalDecimal.add(new BigDecimal(Double.parseDouble(tmpFeeReceiptDetailDto.getAmount())));
        }
        printStr += FeieGetPayFeeDetail.getPrintPayFeeDetailFloorContent(communityId, totalDecimal.doubleValue(), staffName, smallWeChatInnerServiceSMOImpl);

        doPrint(quantity, machinePrinterDto, printStr);

        return new ResultVo(ResultVo.CODE_OK, "成功");
    }


    @Override
    public ResultVo printRepair(String ruId, String communityId, int quantity, MachinePrinterDto machinePrinterDto) {

        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRuId(ruId);
        repairUserDto.setCommunityId(communityId);
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);

        Assert.listOnlyOne(repairUserDtos, "报修单不存在");

        if (RepairUserDto.STATE_SUBMIT.equals(repairUserDtos.get(0).getState())) {
            return new ResultVo(ResultVo.CODE_OK, "成功");
        }

        String repairId = repairUserDtos.get(0).getRepairId();
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(repairId);
        repairDto.setCommunityId(communityId);
        List<RepairDto> repairDtos = repairInnerServiceSMO.queryRepairs(repairDto);
        Assert.listOnlyOne(repairDtos, "报修单不存在");


        String staffName = repairUserDtos.get(0).getStaffName();

        String printStr = "";
        /*************************************内容******************************************/
        List<FeieLine> feieLines = new ArrayList<>();
        feieLines.add(new FeieLine("标题", repairDtos.get(0).getRepairName()));
        feieLines.add(new FeieLine("联系人", repairDtos.get(0).getRepairName()));
        feieLines.add(new FeieLine("电话", repairDtos.get(0).getTel()));
        feieLines.add(new FeieLine("时间", repairDtos.get(0).getAppointmentTime()));
        feieLines.add(new FeieLine("位置", repairDtos.get(0).getRepairObjName()));
        feieLines.add(new FeieLine("维修师傅", staffName));
        feieLines.add(new FeieLine("单号", repairDtos.get(0).getRepairId()));
        feieLines.add(new FeieLine("内容", repairDtos.get(0).getContext()));
        printStr = FeieGetRepair.getPrintRepairHeaderContent(feieLines);
        /*************************************内容******************************************/

        doPrint(quantity, machinePrinterDto, printStr);

        return new ResultVo(ResultVo.CODE_OK, "成功");
    }

    @Override
    public ResultVo printAccountReceipt(String[] arIds, String communityId, int quantity, MachinePrinterDto machinePrinterDto, String name) {
        String printStr = "";
        AccountReceiptDto accountReceiptDto = new AccountReceiptDto();
        accountReceiptDto.setArIds(arIds);
        accountReceiptDto.setCommunityId(communityId);
        List<AccountReceiptDto> accountReceiptDtos = accountReceiptV1InnerServiceSMOImpl.queryAccountReceipts(accountReceiptDto);
        if(accountReceiptDtos == null || accountReceiptDtos.size() < 1){
            throw new CmdException("没有打印内容");
        }

        /*************************************头部******************************************/
        List<FeieLine> feieLines = new ArrayList<>();
        feieLines.add(new FeieLine("单号", accountReceiptDtos.get(0).getArId()));
        feieLines.add(new FeieLine("时间", DateUtil.getFormatTimeString(DateUtil.getCurrentDate(), DateUtil.DATE_FORMATE_STRING_A)));
        printStr = FeieGetPayFeeDetail.getPrintPayFeeDetailHeaderContent(feieLines);
        /*************************************头部******************************************/

        feieLines = new ArrayList<>();
        BigDecimal totalDecimal = new BigDecimal(0);
        for (AccountReceiptDto tmpAccountReceiptDto : accountReceiptDtos) {
            feieLines.add(new FeieLine("账户名称", tmpAccountReceiptDto.getAcctName()));
            feieLines.add(new FeieLine("账户类型", tmpAccountReceiptDto.getAcctTypeName()));
            feieLines.add(new FeieLine("业主", tmpAccountReceiptDto.getOwnerName()));
            feieLines.add(new FeieLine("预存金额", tmpAccountReceiptDto.getReceivedAmount()));
            feieLines.add(new FeieLine("预存方式", tmpAccountReceiptDto.getPrimeRateName()));
            feieLines.add(new FeieLine("当前余额", tmpAccountReceiptDto.getAmount()));
            feieLines.add(new FeieLine("备注", tmpAccountReceiptDto.getRemark()));
            printStr += FeieGetPayFeeDetail.getPrintPayFeeDetailBodyContent(feieLines);
            totalDecimal = totalDecimal.add(new BigDecimal(Double.parseDouble(tmpAccountReceiptDto.getAmount())));
        }
        printStr += FeieGetPayFeeDetail.getPrintPayFeeDetailFloorContent(communityId, totalDecimal.doubleValue(), name, smallWeChatInnerServiceSMOImpl);

        doPrint(quantity, machinePrinterDto, printStr);

        return new ResultVo(ResultVo.CODE_OK, "成功");
    }


    private static String signature(String USER, String UKEY, String STIME) {
        return DigestUtils.sha1Hex(USER + UKEY + STIME);
    }


    private static void doPrint(int quantity, MachinePrinterDto machinePrinterDto, String printStr) {
        String stime = String.valueOf(System.currentTimeMillis() / 1000);
        String user = MappingCache.getValue(MappingConstant.FEIE_DOMAIN, "user");
        String ukey = MappingCache.getValue(MappingConstant.FEIE_DOMAIN, "ukey");
        // 通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)// 读取超时
                .setConnectTimeout(30000)// 连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        HttpPost post = new HttpPost(REQUEST_URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", user));
        nvps.add(new BasicNameValuePair("stime", stime));
        nvps.add(new BasicNameValuePair("sig", signature(user, ukey, stime)));
        nvps.add(new BasicNameValuePair("apiname", "Open_printMsg"));// 固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", machinePrinterDto.getMachineCode()));
        nvps.add(new BasicNameValuePair("content", printStr));
        nvps.add(new BasicNameValuePair("times", quantity + ""));// 打印联数

        CloseableHttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    // 服务器返回的JSON字符串，建议要当做日志记录起来
                    result = EntityUtils.toString(httpentity);
                    System.out.println(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(response, post, httpClient);
        }
    }

    public static void close(CloseableHttpResponse response, HttpPost post, CloseableHttpClient httpClient) {
        try {
            if (response != null) {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            post.abort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
