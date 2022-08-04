package com.java110.job.task.owner;

import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.owner.OwnerPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author fqz
 * @description 业主年龄更新
 * @date 2022-07-12 14:30
 */
@Component
public class EditAgeTemplate extends TaskSystemQuartz {

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void process(TaskDto taskDto) throws ParseException {
        logger.debug("开始执行微信模板信息推送" + taskDto.toString());
        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();
        for (CommunityDto communityDto : communityDtos) {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setCommunityId(communityDto.getCommunityId());
            //根据小区查询业主信息
            List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
            if (ownerDtos != null && ownerDtos.size() > 0) {
                for (OwnerDto owner : ownerDtos) {
                    //业主年龄更新
                    changeAge(owner);
                }
            }
        }
    }

    /**
     * 业主年龄更新
     *
     * @param ownerDto
     */
    private void changeAge(OwnerDto ownerDto) throws ParseException {
        if (!StringUtil.isEmpty(ownerDto.getIdCard())) {
            //获取业主身份证信息
            String idCard = ownerDto.getIdCard();
            String year = null;
            String month = null;
            String day = null;
            Date birthDay = null;
            //正则匹配身份证号是否是正确的，15位或者17位数字+数字/x/X
            if (idCard.matches("^\\d{15}|\\d{17}[\\dxX]$")) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                year = idCard.substring(6, 10);
                month = idCard.substring(10, 12);
                day = idCard.substring(12, 14);
                birthDay = simpleDateFormat.parse(year + "-" + month + "-" + day);
                Calendar cal = Calendar.getInstance();
                if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
                    logger.error("业主" + ownerDto.getName() + "(" + "业主ID为" + ownerDto.getOwnerId() + ")出生日期晚于当前时间");
                }
                int yearNow = cal.get(Calendar.YEAR); //当前年份
                int monthNow = cal.get(Calendar.MONTH) + 1; //当前月份
                int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
                int age = yearNow - Integer.parseInt(year); //计算整岁
                if (monthNow <= Integer.parseInt(month)) {
                    if (monthNow == Integer.parseInt(month)) {
                        if (dayOfMonthNow < Integer.parseInt(day)) { //当前日期在生日之前，年龄减一岁
                            age--;
                        }
                    } else { //当前月份在生日之前，年龄减一岁
                        age--;
                    }
                }
                OwnerPo ownerPo = new OwnerPo();
                ownerPo.setMemberId(ownerDto.getMemberId());
                ownerPo.setAge(String.valueOf(age));
                //更新业主年龄
                int flag = ownerV1InnerServiceSMOImpl.updateOwner(ownerPo);
                if (flag < 1) {
                    throw new CmdException("更新数据失败");
                }
            } else {
                logger.error("业主" + ownerDto.getName() + "(" + "业主ID为" + ownerDto.getOwnerId() + ")身份证号码不匹配！");
            }
        }
    }
}
