package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.PageDto;
import com.java110.dto.community.CommunitySettingDto;
import com.java110.dto.fee.FeeReceiptDto;
import com.java110.dto.fee.FeeReceiptDtoNew;
import com.java110.fee.dao.IFeeReceiptServiceDao;
import com.java110.intf.community.ICommunitySettingInnerServiceSMO;
import com.java110.intf.fee.IFeeReceiptInnerServiceSMO;
import com.java110.po.community.CommunitySettingPo;
import com.java110.po.fee.FeeReceiptPo;
import com.java110.utils.lock.DistributedLock;
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
 * @Description 收据内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeReceiptInnerServiceSMOImpl extends BaseServiceSMO implements IFeeReceiptInnerServiceSMO {

    @Autowired
    private IFeeReceiptServiceDao feeReceiptServiceDaoImpl;

    @Autowired
    private ICommunitySettingInnerServiceSMO communitySettingInnerServiceSMOImpl;


    @Override
    public int saveFeeReceipt(@RequestBody FeeReceiptPo feeReceiptPo) {
        int saveFlag = 1;
        feeReceiptServiceDaoImpl.saveFeeReceiptInfo(BeanConvertUtil.beanCovertMap(feeReceiptPo));
        return saveFlag;
    }

    @Override
    public int saveFeeReceipts(@RequestBody List<FeeReceiptPo> feeReceiptPos) {

        List<Map> fees = new ArrayList<>();
        for (FeeReceiptPo feeReceiptPo : feeReceiptPos) {
            fees.add(BeanConvertUtil.beanCovertMap(feeReceiptPo));
        }

        Map info = new HashMap();
        info.put("feeReceiptPos", fees);
        feeReceiptServiceDaoImpl.saveFeeReceipts(info);
        return 1;
    }

    @Override
    public int updateFeeReceipt(@RequestBody FeeReceiptPo feeReceiptPo) {
        int saveFlag = 1;
        feeReceiptServiceDaoImpl.updateFeeReceiptInfo(BeanConvertUtil.beanCovertMap(feeReceiptPo));
        return saveFlag;
    }

    @Override
    public int deleteFeeReceipt(@RequestBody FeeReceiptPo feeReceiptPo) {
        int saveFlag = 1;
        feeReceiptPo.setStatusCd("1");
        feeReceiptServiceDaoImpl.updateFeeReceiptInfo(BeanConvertUtil.beanCovertMap(feeReceiptPo));
        return saveFlag;
    }

    @Override
    public List<FeeReceiptDto> queryFeeReceipts(@RequestBody FeeReceiptDto feeReceiptDto) {

        //校验是否传了 分页信息

        int page = feeReceiptDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeReceiptDto.setPage((page - 1) * feeReceiptDto.getRow());
        }

        List<FeeReceiptDto> feeReceipts = BeanConvertUtil.covertBeanList(feeReceiptServiceDaoImpl.getFeeReceiptInfo(BeanConvertUtil.beanCovertMap(feeReceiptDto)), FeeReceiptDto.class);

        return feeReceipts;
    }


    @Override
    public List<FeeReceiptDtoNew> queryFeeReceiptsNew(FeeReceiptDtoNew feeReceiptDto) {
        //校验是否传了 分页信息

        int page = feeReceiptDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeReceiptDto.setPage((page - 1) * feeReceiptDto.getRow());
        }

        List<FeeReceiptDtoNew> feeReceipts = BeanConvertUtil.covertBeanList(feeReceiptServiceDaoImpl.getFeeReceiptInfoNew(BeanConvertUtil.beanCovertMap(feeReceiptDto)), FeeReceiptDtoNew.class);

        return feeReceipts;
    }


    @Override
    public int queryFeeReceiptsCount(@RequestBody FeeReceiptDto feeReceiptDto) {
        return feeReceiptServiceDaoImpl.queryFeeReceiptsCount(BeanConvertUtil.beanCovertMap(feeReceiptDto));
    }

    /**
     * 生成收据编号
     *
     * @param communityId 小区ID
     * @return 收据编号
     */
    @Override
    public String generatorReceiptCode(@RequestBody String communityId) {

        // todo 枷锁
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + communityId;
        String receiptCode = "";
        String preReceiptCode = "";
        String startCode = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_M);
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            CommunitySettingDto communitySettingDto = new CommunitySettingDto();
            communitySettingDto.setCommunityId(communityId);
            communitySettingDto.setSettingType(CommunitySettingDto.SETTING_TYPE_FEE);
            communitySettingDto.setSettingKey(CommunitySettingDto.SETTING_KEY_RECEIPT_CODE);
            List<CommunitySettingDto> communitySettingDtos = communitySettingInnerServiceSMOImpl.queryCommunitySettings(communitySettingDto);
            //todo 没有设置 自动生成
            if (communitySettingDtos == null || communitySettingDtos.size() < 1) {
                saveReceiptCode(communityId);
                communitySettingDtos = communitySettingInnerServiceSMOImpl.queryCommunitySettings(communitySettingDto);
            }

            preReceiptCode = communitySettingDtos.get(0).getSettingValue().trim();
            if (!StringUtil.isNumber(preReceiptCode)) {
                return startCode + preReceiptCode;
            }

            if (preReceiptCode.length() > 24) {
                receiptCode = String.format("%024d", (Long.parseLong(preReceiptCode) + 1));
            } else {
                receiptCode = String.format("%0" + preReceiptCode.length() + "d", (Long.parseLong(preReceiptCode) + 1));
            }

            CommunitySettingPo communitySettingPo = new CommunitySettingPo();
            communitySettingPo.setCsId(communitySettingDtos.get(0).getCsId());
            communitySettingPo.setSettingValue(receiptCode);
            communitySettingInnerServiceSMOImpl.updateCommunitySetting(communitySettingPo);

        } finally {
            DistributedLock.releaseDistributedLock(key, requestId);
        }

        return startCode + receiptCode;
    }

    private void saveReceiptCode(String communityId) {
        CommunitySettingPo communitySettingPo = new CommunitySettingPo();
        communitySettingPo.setCommunityId(communityId);
        communitySettingPo.setCsId(GenerateCodeFactory.getGeneratorId("10"));
        communitySettingPo.setSettingType(CommunitySettingDto.SETTING_TYPE_FEE);
        communitySettingPo.setSettingKey(CommunitySettingDto.SETTING_KEY_RECEIPT_CODE);
        communitySettingPo.setSettingValue("001");
        communitySettingPo.setSettingName("收据开始编号");
        communitySettingPo.setRemark("系统自动生成");
        communitySettingInnerServiceSMOImpl.saveCommunitySetting(communitySettingPo);
    }

    public IFeeReceiptServiceDao getFeeReceiptServiceDaoImpl() {
        return feeReceiptServiceDaoImpl;
    }

    public void setFeeReceiptServiceDaoImpl(IFeeReceiptServiceDao feeReceiptServiceDaoImpl) {
        this.feeReceiptServiceDaoImpl = feeReceiptServiceDaoImpl;
    }
}
