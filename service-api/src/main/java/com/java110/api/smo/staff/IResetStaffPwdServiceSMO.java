package com.java110.api.smo.staff;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 重置员工密码
 * Created by Administrator on 2019/4/2.
 */
public interface IResetStaffPwdServiceSMO {

    /**
     * 保存员工信息
     *
     * @param pd
     * @return
     */
    ResponseEntity<String> reset(IPageData pd);
}
