package com.java110.job.task.groupBuy;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.groupBuy.GroupBuySettingDto;
import com.java110.dto.store.StoreDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.goods.IGroupBuyBatchInnerServiceSMO;
import com.java110.intf.goods.IGroupBuyProductInnerServiceSMO;
import com.java110.intf.goods.IGroupBuySettingInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.groupBuyBatch.GroupBuyBatchPo;
import com.java110.po.groupBuyProduct.GroupBuyProductPo;
import com.java110.po.groupBuySetting.GroupBuySettingPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @program: MicroCommunity
 * @description: 定时任务 定时生成批次
 * @author: wuxw
 * @create: 2020-06-15 13:35
 **/
@Component
public class GeneratorGroupBuyBatchTemplate extends TaskSystemQuartz {

    private static Logger logger = LoggerFactory.getLogger(GeneratorGroupBuyBatchTemplate.class);

    private static final int EXPIRE_IN = 7200;

    @Autowired(required = false)
    private IGroupBuySettingInnerServiceSMO groupBuySettingInnerServiceSMOImpl;

    @Autowired(required = false)
    private IGroupBuyBatchInnerServiceSMO groupBuyBatchInnerServiceSMOImpl;

    @Autowired(required = false)
    private IGroupBuyProductInnerServiceSMO groupBuyProductInnerServiceSMOImpl;


    @Override
    protected void process(TaskDto taskDto) {
        logger.debug("开始生成拼团批次" + taskDto.toString());

        // 获取小区
        List<StoreDto> storeDtos = getAllStore(StoreDto.STORE_TYPE_ADMIN);

        for (StoreDto storeDto : storeDtos) {
            try {
                generatorGroupBuyBatch(taskDto, storeDto);
            } catch (Exception e) {
                logger.error("推送消息失败", e);
            }
        }
    }

    /**
     * 轮训派单
     *
     * @param taskDto
     * @param storeDto
     */
    private void generatorGroupBuyBatch(TaskDto taskDto, StoreDto storeDto) {

        //查询需要程序轮训派单 订单
        GroupBuySettingDto groupBuySettingDto = new GroupBuySettingDto();
        groupBuySettingDto.setStoreId(storeDto.getStoreId());
        List<GroupBuySettingDto> groupBuySettingDtos = groupBuySettingInnerServiceSMOImpl.queryGroupBuySettings(groupBuySettingDto);

        Assert.listOnlyOne(groupBuySettingDtos, "未配置 拼团设置");

        for (GroupBuySettingDto tmpGroupBuySettingDto : groupBuySettingDtos) {
            try {
                doGeneratorGroupBuyBatch(tmpGroupBuySettingDto);
            } catch (Exception e) {
                logger.error("生成批次", e);
            }

        }

    }

    /**
     * 生成批次
     *
     * @param tmpGroupBuySettingDto
     */
    private void doGeneratorGroupBuyBatch(GroupBuySettingDto tmpGroupBuySettingDto) throws Exception {

        Date endTime = DateUtil.getDateFromString(tmpGroupBuySettingDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A);
        Date nowTime = DateUtil.getCurrentDate();

        if (endTime.getTime() > nowTime.getTime()) { // 还没有到 批次结束
            return;
        }

        Date startTime = endTime;

        int validHours = Integer.parseInt(tmpGroupBuySettingDto.getValidHours());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);
        calendar.add(Calendar.HOUR_OF_DAY, validHours);

        endTime = calendar.getTime();

        GroupBuyBatchPo groupBuyBatchPo = new GroupBuyBatchPo();
        groupBuyBatchPo.setSettingId(tmpGroupBuySettingDto.getSettingId());
        groupBuyBatchPo.setStoreId(tmpGroupBuySettingDto.getStoreId());
        groupBuyBatchPo.setCurBatch("F");
        groupBuyBatchPo.setDefaultCurBatch("Y");
        groupBuyBatchInnerServiceSMOImpl.updateGroupBuyBatch(groupBuyBatchPo);


        groupBuyBatchPo.setCurBatch("Y");
        groupBuyBatchPo.setBatchStartTime(DateUtil.getFormatTimeString(startTime, DateUtil.DATE_FORMATE_STRING_A));
        groupBuyBatchPo.setBatchEndTime(DateUtil.getFormatTimeString(endTime,DateUtil.DATE_FORMATE_STRING_A));
        groupBuyBatchPo.setBatchId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_batchId));
        int flag = groupBuyBatchInnerServiceSMOImpl.saveGroupBuyBatch(groupBuyBatchPo);

        if(flag < 1){
            throw new IllegalArgumentException("保存批次失败");
        }

        //更新 setting 表
        GroupBuySettingPo groupBuySettingPo = new GroupBuySettingPo();
        groupBuySettingPo.setStartTime(DateUtil.getFormatTimeString(startTime, DateUtil.DATE_FORMATE_STRING_A));
        groupBuySettingPo.setEndTime(DateUtil.getFormatTimeString(endTime,DateUtil.DATE_FORMATE_STRING_A));
        groupBuySettingPo.setSettingId(tmpGroupBuySettingDto.getSettingId());
        flag = groupBuySettingInnerServiceSMOImpl.updateGroupBuySetting(groupBuySettingPo);
        if(flag < 1){
            throw new IllegalArgumentException("保存批次失败");
        }

        GroupBuyProductPo groupBuyProductPo = new GroupBuyProductPo();
        groupBuyProductPo.setStoreId(tmpGroupBuySettingDto.getStoreId());
        groupBuyProductPo.setBatchId(groupBuyBatchPo.getBatchId());

        flag = groupBuyProductInnerServiceSMOImpl.updateGroupBuyProduct(groupBuyProductPo);
        if(flag < 1){
            throw new IllegalArgumentException("保存批次失败");
        }
    }


}
