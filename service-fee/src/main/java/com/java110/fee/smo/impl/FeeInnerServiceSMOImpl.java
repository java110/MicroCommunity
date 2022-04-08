package com.java110.fee.smo.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.fee.BillDto;
import com.java110.dto.fee.BillOweFeeDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.fee.dao.IFeeAttrServiceDao;
import com.java110.fee.dao.IFeeServiceDao;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeInnerServiceSMOImpl extends BaseServiceSMO implements IFeeInnerServiceSMO {

    @Autowired
    private IFeeServiceDao feeServiceDaoImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrServiceDao feeAttrServiceDaoImpl;


    @Override
    public List<FeeDto> queryFees(@RequestBody FeeDto feeDto) {

        //校验是否传了 分页信息
        int page = feeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeDto.setPage((page - 1) * feeDto.getRow());
        }

        List<FeeDto> fees = BeanConvertUtil.covertBeanList(feeServiceDaoImpl.getFeeInfo(BeanConvertUtil.beanCovertMap(feeDto)), FeeDto.class);

        if (fees == null || fees.size() == 0) {
            return fees;
        }

//        String[] userIds = getUserIds(fees);
//        //根据 userId 查询用户信息
//        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (FeeDto fee : fees) {
            refreshFee(fee);
        }

        List<String> feeIds = new ArrayList<>();
        for (FeeDto tmpFeeDto : fees) {
            feeIds.add(tmpFeeDto.getFeeId());
        }

        Map info = new HashMap();
        info.put("feeIds", feeIds);
        info.put("communityId", feeDto.getCommunityId());

        List<Map> attrMaps = feeAttrServiceDaoImpl.getFeeAttrInfo(info);

        List<FeeAttrDto> feeAttrDtos = BeanConvertUtil.covertBeanList(attrMaps, FeeAttrDto.class);
        List<FeeAttrDto> tmpFeeAttrDtos = null;
        String payerObjName = "";
        for (FeeDto tmpFeeDto : fees) {
            tmpFeeAttrDtos = new ArrayList<>();
            for (FeeAttrDto feeAttrDto : feeAttrDtos) {
                if (!tmpFeeDto.getFeeId().equals(feeAttrDto.getFeeId())) {
                    continue;
                }
                tmpFeeAttrDtos.add(feeAttrDto);
                if(FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME.equals(feeAttrDto.getSpecCd())){
                    payerObjName = feeAttrDto.getValue();
                }
            }
            tmpFeeDto.setPayerObjName(payerObjName);
            tmpFeeDto.setFeeAttrDtos(tmpFeeAttrDtos);
        }
        return fees;
    }

    @Override
    public List<FeeDto> querySimpleFees(@RequestBody FeeDto feeDto) {

        //校验是否传了 分页信息
        int page = feeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeDto.setPage((page - 1) * feeDto.getRow());
        }

        List<FeeDto> fees = BeanConvertUtil.covertBeanList(feeServiceDaoImpl.getFeeInfo(BeanConvertUtil.beanCovertMap(feeDto)), FeeDto.class);


        for (FeeDto tmpFeeDto : fees) {
            if (!StringUtil.isEmpty(tmpFeeDto.getImportFeeName())) {
                //fee.setFeeName(fee.getImportFeeName() + "(" + fee.getFeeName() + ")");
                tmpFeeDto.setFeeName(tmpFeeDto.getImportFeeName());
            }
        }

        return fees;
    }

    @Override
    public List<FeeDto> queryBusinessFees(@RequestBody FeeDto feeDto) {

        List<Map> fees = feeServiceDaoImpl.getBusinessFeeInfo(BeanConvertUtil.beanCovertMap(feeDto));

        return BeanConvertUtil.covertBeanList(fees, FeeDto.class);

    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param fee 小区费用信息
     *            //@param users 用户列表
     */
    private void refreshFee(FeeDto fee) {
//        for (UserDto user : users) {
//            if (fee.getUserId().equals(user.getUserId())) {
//                BeanConvertUtil.covertBean(user, fee);
//            }
//        }

        if (!StringUtil.isEmpty(fee.getImportFeeName())) {
            //fee.setFeeName(fee.getImportFeeName() + "(" + fee.getFeeName() + ")");
            fee.setFeeName(fee.getImportFeeName());
        }

    }

    /**
     * 获取批量userId
     *
     * @param fees 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<FeeDto> fees) {
        List<String> userIds = new ArrayList<String>();
        for (FeeDto fee : fees) {
            userIds.add(fee.getUserId());

        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryFeesCount(@RequestBody FeeDto feeDto) {
        return feeServiceDaoImpl.queryFeesCount(BeanConvertUtil.beanCovertMap(feeDto));
    }

    @Override
    public List<FeeDto> queryFeeByAttr(FeeAttrDto feeAttrDto) {
        //校验是否传了 分页信息

        int page = feeAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeAttrDto.setPage((page - 1) * feeAttrDto.getRow());
        }

        List<FeeDto> fees = BeanConvertUtil.covertBeanList(feeServiceDaoImpl.queryFeeByAttr(BeanConvertUtil.beanCovertMap(feeAttrDto)), FeeDto.class);

        return fees;
    }

    @Override
    public int queryFeeByAttrCount(FeeAttrDto feeAttrDto) {
        return feeServiceDaoImpl.queryFeeByAttrCount(BeanConvertUtil.beanCovertMap(feeAttrDto));
    }


    @Override
    public int queryBillCount(@RequestBody BillDto billDto) {
        return feeServiceDaoImpl.queryBillCount(BeanConvertUtil.beanCovertMap(billDto));
    }

    /**
     * 查询账期
     *
     * @param billDto
     * @return
     */
    @Override
    public List<BillDto> queryBills(@RequestBody BillDto billDto) {

        //校验是否传了 分页信息

        int page = billDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            billDto.setPage((page - 1) * billDto.getRow());
        }

        List<BillDto> billDtos = BeanConvertUtil.covertBeanList(feeServiceDaoImpl.queryBills(BeanConvertUtil.beanCovertMap(billDto)), BillDto.class);

        return billDtos;

    }

    @Override
    public int computeBillOweFeeCount(@RequestBody FeeDto feeDto) {
        return feeServiceDaoImpl.computeBillOweFeeCount(BeanConvertUtil.beanCovertMap(feeDto));
    }


    @Override
    public List<FeeDto> computeBillOweFee(@RequestBody FeeDto feeDto) {
        int page = feeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeDto.setPage((page - 1) * feeDto.getRow());
        }

        List<FeeDto> fees = BeanConvertUtil.covertBeanList(feeServiceDaoImpl.computeBillOweFee(BeanConvertUtil.beanCovertMap(feeDto)), FeeDto.class);

        return fees;
    }

    @Override
    public List<FeeDto> computeEveryOweFee(FeeDto feeDto) {
        int page = feeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeDto.setPage((page - 1) * feeDto.getRow());
        }

        List<FeeDto> fees = BeanConvertUtil.covertBeanList(feeServiceDaoImpl.computeEveryOweFee(BeanConvertUtil.beanCovertMap(feeDto)), FeeDto.class);

        return fees;
    }

    @Override
    public int computeEveryOweFeeCount(FeeDto feeDto) {
        return feeServiceDaoImpl.computeEveryOweFeeCount(BeanConvertUtil.beanCovertMap(feeDto));
    }


    @Override
    public int queryBillOweFeeCount(@RequestBody BillOweFeeDto billDto) {
        return feeServiceDaoImpl.queryBillOweFeeCount(BeanConvertUtil.beanCovertMap(billDto));
    }

    /**
     * 查询账期
     *
     * @param billDto
     * @return
     */
    @Override
    public List<BillOweFeeDto> queryBillOweFees(@RequestBody BillOweFeeDto billDto) {

        //校验是否传了 分页信息

        int page = billDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            billDto.setPage((page - 1) * billDto.getRow());
        }

        List<BillOweFeeDto> billOweFeeDtos = BeanConvertUtil.covertBeanList(feeServiceDaoImpl.queryBillOweFees(BeanConvertUtil.beanCovertMap(billDto)), BillOweFeeDto.class);

        return billOweFeeDtos;

    }

    /**
     * 保存欠费
     *
     * @param billDto
     * @return
     */
    @Override
    public int insertBillOweFees(@RequestBody BillOweFeeDto billDto) {
        return feeServiceDaoImpl.insertBillOweFees(BeanConvertUtil.beanCovertMap(billDto));
    }

    @Override
    public int updateBillOweFees(@RequestBody BillOweFeeDto billDto) {
        return feeServiceDaoImpl.updateBillOweFees(BeanConvertUtil.beanCovertMap(billDto));
    }

    /**
     * 保存账单
     *
     * @param billDto
     * @return
     */
    @Override
    public int insertBill(@RequestBody BillDto billDto) {
        return feeServiceDaoImpl.insertBill(BeanConvertUtil.beanCovertMap(billDto));
    }

    @Override
    @Java110Transactional
    public int updateFee(@RequestBody PayFeePo payFeePo) {
        feeServiceDaoImpl.updateFeeInfoInstance(BeanConvertUtil.beanCovertMap(payFeePo));
        return 1;
    }

    @Override
    public int saveFee(@RequestBody List<PayFeePo> payFeePos) {
        List<Map> fees = new ArrayList<>();
        for (PayFeePo payFeePo : payFeePos) {
            fees.add(BeanConvertUtil.beanCovertMap(payFeePo));
        }

        Map info = new HashMap();
        info.put("payFeePos", fees);
        return feeServiceDaoImpl.insertFees(info);
    }

    @Override
    @Java110Transactional
    public int saveOneFee(@RequestBody PayFeePo payFeePo) {
        List<Map> fees = new ArrayList<>();
        fees.add(BeanConvertUtil.beanCovertMap(payFeePo));
        Map info = new HashMap();
        info.put("payFeePos", fees);
        return feeServiceDaoImpl.insertFees(info);
    }

    @Override
    public int deleteFeesByBatch(@RequestBody PayFeePo payFeePo) {
        return feeServiceDaoImpl.deleteFeesByBatch(BeanConvertUtil.beanCovertMap(payFeePo));
    }

    @Override
    public JSONArray getAssetsFee(@RequestBody String communityId) {

        JSONArray data = new JSONArray();
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(communityId);
        feeConfigDto.setCurTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
            return data;
        }


        for (FeeConfigDto tmpFeeConfigDto : feeConfigDtos) {
            dealFeeConfig(data, tmpFeeConfigDto);
        }


        return data;
    }


    private void dealFeeConfig(JSONArray data, FeeConfigDto tmpFeeConfigDto) {
        JSONObject config = new JSONObject();
        Map info = new HashMap();
        info.put("configId", tmpFeeConfigDto.getConfigId());
        info.put("communityId", tmpFeeConfigDto.getCommunityId());
        info.put("arrearsEndTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        info.put("state", FeeDto.STATE_DOING);
        int oweFeeCount = feeServiceDaoImpl.queryFeesCount(info);
        config.put("oweFeeCount", oweFeeCount);

        info.put("noArrearsEndTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        info.put("state", FeeDto.STATE_DOING);
        int feeCount = feeServiceDaoImpl.queryFeesCount(info);
        config.put("feeCount", feeCount);

        config.put("feeName", tmpFeeConfigDto.getFeeName());
        data.add(config);
    }


    public IFeeServiceDao getFeeServiceDaoImpl() {
        return feeServiceDaoImpl;
    }

    public void setFeeServiceDaoImpl(IFeeServiceDao feeServiceDaoImpl) {
        this.feeServiceDaoImpl = feeServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
