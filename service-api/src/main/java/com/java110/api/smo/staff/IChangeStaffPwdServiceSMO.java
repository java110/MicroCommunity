package com.java110.api.smo.staff;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 修改员工密码
 * Created by Administrator on 2019/4/2.
 */
public interface IChangeStaffPwdServiceSMO {

    /**
     * 保存员工信息
     *
     * @param pd
     * @return
     */
    ResponseEntity<String> change(IPageData pd);
}
