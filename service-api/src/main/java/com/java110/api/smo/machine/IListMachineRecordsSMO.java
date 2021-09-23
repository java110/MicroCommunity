package com.java110.api.smo.machine;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 开门记录管理服务接口类
 *
 * add by wuxw 2019-06-29
 */
public interface IListMachineRecordsSMO {

    /**
     * 查询开门记录信息
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listMachineRecords(IPageData pd) throws SMOException;
}
