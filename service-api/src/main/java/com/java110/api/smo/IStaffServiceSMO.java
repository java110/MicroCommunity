package com.java110.api.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 员工管理服务接口类
 * Created by Administrator on 2019/4/2.
 */
public interface IStaffServiceSMO {

    /**
     * 保存员工信息
     *
     * @param pd
     * @return
     */
    ResponseEntity<String> saveStaff(IPageData pd);


    /**
     * 加载 员工信息
     *
     * @param pd
     * @return
     */
    ResponseEntity<String> loadData(IPageData pd);


    /**
     * 修改员工信息
     *
     * @param pd
     * @return
     */
    ResponseEntity<String> modifyStaff(IPageData pd);


    /**
     * 删除员工
     *
     * @param pd
     * @return
     */
    ResponseEntity<String> delete(IPageData pd);

    /**
     * 查询员工没有绑定的权限组
     *
     * @param pd
     * @return
     */
    ResponseEntity<String> listNoAddPrivilegeGroup(IPageData pd);


    /**
     * 查询员工没有绑定的权限
     *
     * @param pd
     * @return
     */
    ResponseEntity<String> listNoAddPrivilege(IPageData pd);

    /**
     * 添加权限
     *
     * @param pd
     * @return
     */
    ResponseEntity<String> addStaffPrivilegeOrPrivilegeGroup(IPageData pd);

    /**
     * 删除权限
     *
     * @param pd
     * @return
     */
    ResponseEntity<String> deleteStaffPrivilege(IPageData pd);
}
