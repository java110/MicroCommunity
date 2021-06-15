package com.java110.common.smo.impl;


import com.java110.common.dao.IAppraiseServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.PageDto;
import com.java110.dto.appraise.AppraiseDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IAppraiseInnerServiceSMO;
import com.java110.po.appraise.AppraisePo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 评价内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AppraiseInnerServiceSMOImpl extends BaseServiceSMO implements IAppraiseInnerServiceSMO {

    @Autowired
    private IAppraiseServiceDao appraiseServiceDaoImpl;


    @Override
    public int saveAppraise(@RequestBody AppraisePo appraisePo) {
        int saveFlag = 1;
        if (StringUtil.isEmpty(appraisePo.getAppraiseId()) || appraisePo.getAppraiseId().startsWith("-")) {
            appraisePo.setAppraiseId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appraiseId));
        }
        appraiseServiceDaoImpl.saveAppraiseInfo(BeanConvertUtil.beanCovertMap(appraisePo));
        return saveFlag;
    }

    @Override
    public int updateAppraise(@RequestBody AppraisePo appraisePo) {
        int saveFlag = 1;
        appraiseServiceDaoImpl.updateAppraiseInfo(BeanConvertUtil.beanCovertMap(appraisePo));
        return saveFlag;
    }

    @Override
    public int deleteAppraise(@RequestBody AppraisePo appraisePo) {
        int saveFlag = 1;
        appraisePo.setStatusCd("1");
        appraiseServiceDaoImpl.updateAppraiseInfo(BeanConvertUtil.beanCovertMap(appraisePo));
        return saveFlag;
    }

    @Override
    public List<AppraiseDto> queryAppraises(@RequestBody AppraiseDto appraiseDto) {

        //校验是否传了 分页信息

        int page = appraiseDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            appraiseDto.setPage((page - 1) * appraiseDto.getRow());
        }

        List<AppraiseDto> appraises = BeanConvertUtil.covertBeanList(appraiseServiceDaoImpl.getAppraiseInfo(BeanConvertUtil.beanCovertMap(appraiseDto)), AppraiseDto.class);

        return appraises;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param appraise 小区评价信息
     * @param users    用户列表
     */
    private void refreshAppraise(AppraiseDto appraise, List<UserDto> users) {
        for (UserDto user : users) {
            if (appraise.getAppraiseId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, appraise);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param appraises 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<AppraiseDto> appraises) {
        List<String> userIds = new ArrayList<String>();
        for (AppraiseDto appraise : appraises) {
            userIds.add(appraise.getAppraiseId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryAppraisesCount(@RequestBody AppraiseDto appraiseDto) {
        return appraiseServiceDaoImpl.queryAppraisesCount(BeanConvertUtil.beanCovertMap(appraiseDto));
    }

    public IAppraiseServiceDao getAppraiseServiceDaoImpl() {
        return appraiseServiceDaoImpl;
    }

    public void setAppraiseServiceDaoImpl(IAppraiseServiceDao appraiseServiceDaoImpl) {
        this.appraiseServiceDaoImpl = appraiseServiceDaoImpl;
    }
}
