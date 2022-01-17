package com.java110.job.adapt.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.entity.order.Business;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportFeeYearCollectionInnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.fee.PayFeePo;
import com.java110.po.reportFeeMonthStatistics.ReportFeeMonthStatisticsPo;
import com.java110.po.reportFeeYearCollection.ReportFeeYearCollectionPo;
import com.java110.po.reportOweFee.ReportOweFeePo;
import com.java110.utils.util.BeanConvertUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 缴费收据处理
 *
 * @author fqz
 * @date 2020-12-11  18:54
 */
@Component(value = "deleteFeeRefreshReportAdapt")
public class DeleteFeeRefreshReportAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(DeleteFeeRefreshReportAdapt.class);

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMO;


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;


    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Autowired
    private IReportFeeYearCollectionInnerServiceSMO reportFeeYearCollectionInnerServiceSMOImpl;

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    public final static String ALI_SMS_DOMAIN = "ALI_SMS";

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessPayFees = null;
        if (data.containsKey(PayFeePo.class.getSimpleName())) {
            Object bObj = data.get(PayFeePo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessPayFees = new JSONArray();
                businessPayFees.add(bObj);
            } else if (bObj instanceof Map) {
                businessPayFees = new JSONArray();
                businessPayFees.add(JSONObject.parseObject(JSONObject.toJSONString(bObj)));
            } else if (bObj instanceof List) {
                businessPayFees = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessPayFees = (JSONArray) bObj;
            }
        } else {
            return;
        }

        if (businessPayFees == null) {
            return;
        }
        for (int bPayFeeIndex = 0; bPayFeeIndex < businessPayFees.size(); bPayFeeIndex++) {
            JSONObject businessPayFee = businessPayFees.getJSONObject(bPayFeeIndex);
            doPayFee(business, businessPayFee);
        }
    }

    private void doPayFee(Business business, JSONObject businessPayFee) {
        //查询缴费明细
        PayFeePo payFeePo = BeanConvertUtil.covertBean(businessPayFee, PayFeePo.class);
        //查询欠费报表
        ReportOweFeePo reportOweFeePo = new ReportOweFeePo();
        reportOweFeePo.setFeeId(payFeePo.getFeeId());
        reportOweFeePo.setCommunityId(payFeePo.getCommunityId());
        reportOweFeeInnerServiceSMOImpl.deleteReportOweFee(reportOweFeePo);


        ReportFeeYearCollectionPo reportFeeYearCollectionPo = new ReportFeeYearCollectionPo();
        reportFeeYearCollectionPo.setFeeId(payFeePo.getFeeId());
        reportFeeYearCollectionPo.setCommunityId(payFeePo.getCommunityId());
        reportFeeYearCollectionInnerServiceSMOImpl.deleteReportFeeYearCollection(reportFeeYearCollectionPo);

        ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo = new ReportFeeMonthStatisticsPo();
        reportFeeMonthStatisticsPo.setFeeId(payFeePo.getFeeId());
        reportFeeMonthStatisticsPo.setCommunityId(payFeePo.getCommunityId());
        reportFeeMonthStatisticsInnerServiceSMOImpl.deleteReportFeeMonthStatistics(reportFeeMonthStatisticsPo);


    }


}
