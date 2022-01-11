package com.java110.fee.cmd.tempCarFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.AbstractServiceCmdListener;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.couponUser.CouponUserDto;
import com.java110.dto.tempCarFeeConfig.TempCarPayOrderDto;
import com.java110.fee.bmo.tempCarFee.IGetTempCarFeeRules;
import com.java110.intf.acct.ICouponUserDetailV1InnerServiceSMO;
import com.java110.intf.acct.ICouponUserV1InnerServiceSMO;
import com.java110.po.couponUser.CouponUserPo;
import com.java110.po.couponUserDetail.CouponUserDetailPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 类表述：通知
 * 服务编码：tempCarFee.notifyTempCarFeeOrder
 * 请求路劲：/app/tempCarFee.notifyTempCarFeeOrder
 * add by 吴学文 at 2021-09-16 22:26:04 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "tempCarFee.notifyTempCarFeeOrder")
public class NotifyTempCarFeeOrderCmd extends AbstractServiceCmdListener {

    @Autowired
    private IGetTempCarFeeRules getTempCarFeeRulesImpl;
    @Autowired
    private ICouponUserV1InnerServiceSMO couponUserV1InnerServiceSMOImpl;
    @Autowired
    private ICouponUserDetailV1InnerServiceSMO couponUserDetailV1InnerServiceSMOImpl;
    //{"amount":20.0,"payType":"2","orderId":"19c4321c-b5d5-405f-b2ff-20e86a2e7f3e",
    // "payTime":"2021-10-17 17:29:54","paId":"102021101160020175","carNum":"青A88888","oId":"102021101724760012"}
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String paramIn = CommonCache.getAndRemoveValue("getTempCarFeeOrder" + reqJson.getString("oId"));
        JSONObject paramObj = JSONObject.parseObject(paramIn);
        System.out.println("获取到内存中的数据了++++++++++++==》"+paramObj.toJSONString());
        modifyCouponUser(paramObj);
        TempCarPayOrderDto tempCarPayOrderDto = BeanConvertUtil.covertBean(reqJson, TempCarPayOrderDto.class);
        ResponseEntity<String> responseEntity = getTempCarFeeRulesImpl.notifyTempCarFeeOrder(tempCarPayOrderDto);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void modifyCouponUser(JSONObject paramObj) {
        if (!paramObj.containsKey("couponPrice") || paramObj.getDouble("couponPrice") <= 0) {
            return;
        }
        //FeeDto feeInfo = (FeeDto) paramObj.get("feeInfo");
        CouponUserDto couponUserDto = null;
        JSONArray couponUserDtos = paramObj.getJSONArray("couponUserDtos");
        CouponUserDto couponUser = null;
        for (int accountIndex = 0; accountIndex < couponUserDtos.size(); accountIndex++) {
            couponUser = BeanConvertUtil.covertBean(couponUserDtos.getJSONObject(accountIndex), CouponUserDto.class);
            couponUserDto = new CouponUserDto();
            couponUserDto.setCouponId(couponUser.getCouponId());
            couponUserDto.setState(CouponUserDto.COUPON_STATE_RUN);
            List<CouponUserDto> couponUserDtos1 = couponUserV1InnerServiceSMOImpl.queryCouponUsers(couponUserDto);
            if (couponUserDtos1 == null || couponUserDtos1.size() < 1) {
                throw new CmdException("优惠券被使用");
            }
            CouponUserPo couponUserPo = new CouponUserPo();
            couponUserPo.setState(CouponUserDto.COUPON_STATE_STOP);
            couponUserPo.setCouponId(couponUser.getCouponId());
            int fage = couponUserV1InnerServiceSMOImpl.updateCouponUser(couponUserPo);
            if (fage < 1) {
                throw new CmdException("更新优惠卷信息失败");
            }
            CouponUserDetailPo couponUserDetailPo = new CouponUserDetailPo();
            couponUserDetailPo.setUoId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_usId));
            couponUserDetailPo.setCouponId(couponUser.getCouponId());
            couponUserDetailPo.setUserId(couponUser.getUserId());
            couponUserDetailPo.setCouponName(couponUser.getCouponName());
            couponUserDetailPo.setUserName(couponUser.getUserName());
            couponUserDetailPo.setObjId(paramObj.getString("feeId"));
            couponUserDetailPo.setObjType(paramObj.getString("feeTypeCd"));
            couponUserDetailPo.setOrderId(paramObj.getString("oId"));
            fage = couponUserDetailV1InnerServiceSMOImpl.saveCouponUserDetail(couponUserDetailPo);
            if (fage < 1) {
                throw new CmdException("新增优惠卷使用记录信息失败");
            }
        }

        paramObj.put("remark", paramObj.getString("remark") + "-优惠劵抵扣" + paramObj.getDouble("couponPrice") + "元");

    }
}
