package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.user.StaffAppAuthDto;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.po.user.StaffAppAuthPo;
import com.java110.user.dao.IStaffAppAuthServiceDao;
import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 员工微信认证内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class StaffAppAuthInnerServiceSMOImpl extends BaseServiceSMO implements IStaffAppAuthInnerServiceSMO {

    @Autowired
    private IStaffAppAuthServiceDao staffAppAuthServiceDaoImpl;


    @Override
    public int saveStaffAppAuth(@RequestBody StaffAppAuthPo staffAppAuthPo) {
        int saveFlag = 1;
        staffAppAuthServiceDaoImpl.saveStaffAppAuthInfo(BeanConvertUtil.beanCovertMap(staffAppAuthPo));
        return saveFlag;
    }

    @Override
    public int updateStaffAppAuth(@RequestBody StaffAppAuthPo staffAppAuthPo) {
        int saveFlag = 1;

        if (!StringUtil.isEmpty(staffAppAuthPo.getOpenName())) {
            String nickName = "未获取";
            try {
                nickName = Base64Convert.byteToBase64(staffAppAuthPo.getOpenName().getBytes("UTF-8"));
            } catch (Exception e) {
            }

            staffAppAuthPo.setOpenName(nickName);
        }

        staffAppAuthServiceDaoImpl.updateStaffAppAuthInfo(BeanConvertUtil.beanCovertMap(staffAppAuthPo));
        return saveFlag;
    }

    @Override
    public int deleteStaffAppAuth(@RequestBody StaffAppAuthPo staffAppAuthPo) {
        int saveFlag = 1;
        staffAppAuthPo.setStatusCd("1");
        staffAppAuthServiceDaoImpl.updateStaffAppAuthInfo(BeanConvertUtil.beanCovertMap(staffAppAuthPo));
        return saveFlag;
    }

    @Override
    public List<StaffAppAuthDto> queryStaffAppAuths(@RequestBody StaffAppAuthDto staffAppAuthDto) {

        //校验是否传了 分页信息

        int page = staffAppAuthDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            staffAppAuthDto.setPage((page - 1) * staffAppAuthDto.getRow());
        }

        List<StaffAppAuthDto> staffAppAuths = BeanConvertUtil.covertBeanList(staffAppAuthServiceDaoImpl.getStaffAppAuthInfo(BeanConvertUtil.beanCovertMap(staffAppAuthDto)), StaffAppAuthDto.class);

        if (ListUtil.isNull(staffAppAuths)) {
            return staffAppAuths;
        }

        for (StaffAppAuthDto tmpStaffAppAuthDto : staffAppAuths) {
            if (StringUtil.isEmpty(tmpStaffAppAuthDto.getOpenName())) {
                continue;
            }

            String nickName = "未获取";
            try {
                nickName = new String(Base64Convert.base64ToByte(tmpStaffAppAuthDto.getOpenName()), "UTF-8");
            } catch (Exception e) {
            }
            tmpStaffAppAuthDto.setOpenName(nickName);
        }
        return staffAppAuths;
    }


    @Override
    public int queryStaffAppAuthsCount(@RequestBody StaffAppAuthDto staffAppAuthDto) {
        return staffAppAuthServiceDaoImpl.queryStaffAppAuthsCount(BeanConvertUtil.beanCovertMap(staffAppAuthDto));
    }

    public IStaffAppAuthServiceDao getStaffAppAuthServiceDaoImpl() {
        return staffAppAuthServiceDaoImpl;
    }

    public void setStaffAppAuthServiceDaoImpl(IStaffAppAuthServiceDao staffAppAuthServiceDaoImpl) {
        this.staffAppAuthServiceDaoImpl = staffAppAuthServiceDaoImpl;
    }
}
