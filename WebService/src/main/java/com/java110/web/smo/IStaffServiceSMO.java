package com.java110.web.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 员工管理服务接口类
 * Created by Administrator on 2019/4/2.
 */
public interface IStaffServiceSMO {

    /**
     * 保存员工信息
     * @param pd
     * @return
     */
    public ResponseEntity<String> saveStaff(IPageData pd);


    /**
     * 加载 员工信息
     * @param pd
     * @return
     */
    public ResponseEntity<String> loadData(IPageData pd);


    /**
     * 修改员工信息
     * @param pd
     * @return
     */
    public ResponseEntity<String> modifyStaff(IPageData pd);
}
